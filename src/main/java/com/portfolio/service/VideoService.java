package com.portfolio.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.portfolio.entity.Video;
import com.portfolio.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class VideoService {

 private final VideoRepository repo;
 private final Cloudinary cloudinary;

 public VideoService(VideoRepository repo, Cloudinary cloudinary) {
  this.repo = repo;
  this.cloudinary = cloudinary;
 }

 // Public: all videos
 public List<Video> publicAll() {
  return repo.findAll();
 }

 // Single video
 public Video get(Long id) {
  return repo.findById(id)
          .orElseThrow(() ->
                  new RuntimeException("Video not found with id: " + id)
          );
 }

 // Admin: all videos
 public List<Video> adminAll() {
  return repo.findAll();
 }

 // Create video
 // React -> Spring Boot -> Cloudinary -> PostgreSQL
 public Video create(String title, MultipartFile file) {

  if (title == null || title.trim().isEmpty()) {
   throw new IllegalArgumentException("Title is required");
  }

  if (file == null || file.isEmpty()) {
   throw new IllegalArgumentException("Video file is required");
  }

  try {

   Map uploadResult = cloudinary.uploader().upload(
           file.getBytes(),
           ObjectUtils.asMap(
                   "resource_type", "video",
                   "folder", "portfolio/videos"
           )
   );

   String videoUrl = (String) uploadResult.get("secure_url");

   if (videoUrl == null || videoUrl.isEmpty()) {
    throw new RuntimeException("Cloudinary did not return video URL");
   }

   Video video = new Video();
   video.setTitle(title.trim());
   video.setVideoUrl(videoUrl);

   return repo.save(video);

  } catch (IOException e) {
   throw new RuntimeException(
           "Video upload to Cloudinary failed",
           e
   );
  }
 }

 // Update video
 public Video update(Long id, String title, MultipartFile file) {

  Video video = get(id);

  if (title != null && !title.trim().isEmpty()) {
   video.setTitle(title.trim());
  }

  // Agar new video file di gayi hai
  if (file != null && !file.isEmpty()) {

   try {

    Map uploadResult = cloudinary.uploader().upload(
            file.getBytes(),
            ObjectUtils.asMap(
                    "resource_type", "video",
                    "folder", "portfolio/videos"
            )
    );

    String videoUrl = (String) uploadResult.get("secure_url");

    if (videoUrl == null || videoUrl.isEmpty()) {
     throw new RuntimeException(
             "Cloudinary did not return video URL"
     );
    }

    video.setVideoUrl(videoUrl);

   } catch (IOException e) {
    throw new RuntimeException(
            "Video upload to Cloudinary failed",
            e
    );
   }
  }

  return repo.save(video);
 }

 // Delete video
 public void delete(Long id) {
  Video video = get(id);
  repo.delete(video);
 }
}