package com.portfolio.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // Upload video and return secure URL
    public String uploadVideo(MultipartFile file, String folder) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "video",
                            "folder", folder
                    )
            );
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload video to Cloudinary", e);
        }
    }

    // Delete video using its URL (extracts public_id)
    public void deleteVideo(String videoUrl) {
        try {
            String publicId = extractPublicId(videoUrl);
            if (publicId != null) {
                cloudinary.uploader().destroy(
                        publicId,
                        ObjectUtils.asMap("resource_type", "video")
                );
            }
        } catch (IOException e) {
            // Log and ignore - don't block delete flow if Cloudinary cleanup fails
            System.err.println("Failed to delete video from Cloudinary: " + e.getMessage());
        }
    }

    // Extract Cloudinary public_id from secure_url
    private String extractPublicId(String url) {
        if (url == null) return null;

        // Example URL:
        // https://res.cloudinary.com/<cloud>/video/upload/v12345/portfolio_videos/abc123.mp4
        Pattern pattern = Pattern.compile("/upload/(?:v\\d+/)?(.+)\\.[a-zA-Z0-9]+$");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}