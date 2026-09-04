package com.portfolio.service;

import com.portfolio.entity.Payment;
import com.portfolio.repository.PaymentRepository;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private final PaymentRepository paymentRepository;


    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    // ==========================================
    // CREATE RAZORPAY ORDER
    // ==========================================

    public String createOrder() throws Exception {

        RazorpayClient razorpay =
                new RazorpayClient(keyId, keySecret);

        int amount = 9900;

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", amount);
        orderRequest.put("currency", "INR");

        orderRequest.put(
                "receipt",
                "portfolio_" + System.currentTimeMillis()
        );

        Order order =
                razorpay.orders.create(orderRequest);


        // Save order in database

        Payment payment = new Payment();

        payment.setRazorpayOrderId(order.get("id"));
        payment.setAmount(amount);
        payment.setCurrency("INR");
        payment.setStatus("CREATED");

        paymentRepository.save(payment);


        return order.toString();
    }


    // ==========================================
    // VERIFY PAYMENT
    // ==========================================

    public boolean verifyPayment(
            String orderId,
            String paymentId,
            String signature
    ) throws Exception {


        JSONObject attributes = new JSONObject();

        attributes.put(
                "razorpay_order_id",
                orderId
        );

        attributes.put(
                "razorpay_payment_id",
                paymentId
        );

        attributes.put(
                "razorpay_signature",
                signature
        );


        boolean verified =
                Utils.verifyPaymentSignature(
                        attributes,
                        keySecret
                );


        if (!verified) {
            return false;
        }


        // Find our order

        Payment payment =
                paymentRepository
                        .findByRazorpayOrderId(orderId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Order not found"
                                )
                        );


        // Already paid

        if ("PAID".equals(payment.getStatus())) {
            return true;
        }


        // Verify payment with Razorpay

        RazorpayClient razorpay =
                new RazorpayClient(
                        keyId,
                        keySecret
                );


        com.razorpay.Payment razorpayPayment =
                razorpay.payments.fetch(paymentId);


        String paymentStatus =
                razorpayPayment.get("status");


        // Only captured payment gets access

        if (!"captured".equals(paymentStatus)) {
            payment.setStatus(paymentStatus);
            paymentRepository.save(payment);

            return false;
        }


        // Amount check

        int razorpayAmount =
                razorpayPayment.get("amount");


        if (razorpayAmount != payment.getAmount()) {
            return false;
        }


        // Currency check

        String currency =
                razorpayPayment.get("currency");


        if (!"INR".equals(currency)) {
            return false;
        }


        // Payment successful

        payment.setRazorpayPaymentId(paymentId);
        payment.setStatus("PAID");
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);


        return true;
    }


    // ==========================================
    // CHECK PAYMENT
    // ==========================================

    public boolean isPaymentSuccessful(
            String orderId
    ) {

        return paymentRepository
                .findByRazorpayOrderId(orderId)
                .map(payment ->
                        "PAID".equals(payment.getStatus())
                )
                .orElse(false);
    }
}