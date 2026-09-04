package com.portfolio.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;


    // 1. Create Razorpay Order
    @PostMapping("/create-order")
    public Map<String, Object> createOrder() throws Exception {

        RazorpayClient razorpay =
                new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();

        options.put("amount", 9900); // ₹99
        options.put("currency", "INR");
        options.put("receipt", "portfolio_pdf_001");

        Order order = razorpay.orders.create(options);

        Map<String, Object> response = new HashMap<>();

        response.put("success", true);
        response.put("keyId", keyId);
        response.put("order", new JSONObject(order.toString()).toMap());

        return response;
    }


    // 2. Verify Payment
    @PostMapping("/verify")
    public Map<String, Object> verifyPayment(
            @RequestBody Map<String, String> data) {

        Map<String, Object> response = new HashMap<>();

        try {

            String orderId =
                    data.get("razorpay_order_id");

            String paymentId =
                    data.get("razorpay_payment_id");

            String signature =
                    data.get("razorpay_signature");

            String payload =
                    orderId + "|" + paymentId;

            boolean verified =
                    Utils.verifySignature(
                            payload,
                            signature,
                            keySecret
                    );

            if (verified) {

                response.put("success", true);
                response.put("message", "Payment verified successfully");

                return response;
            }

            response.put("success", false);
            response.put("message", "Payment verification failed");

            return response;

        } catch (Exception e) {

            response.put("success", false);
            response.put("message", "Payment verification failed");

            return response;
        }
    }


    // 3. Download PDF
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadPdf() {

        Resource pdf =
                new ClassPathResource(
                        "pdfs/java_developer_interview_200_questions_to_crack_the_java .pdf"
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=java_developer_interview_200_questions_to_crack_the_java .pdf"
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(pdf);
    }
}