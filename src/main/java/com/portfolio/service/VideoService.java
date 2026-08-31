package com.portfolio.service;

import com.portfolio.entity.Video;
import com.portfolio.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class VideoService {

 private final VideoRepository repo;
 private final CloudinaryService cloudinary;

 private static final String FOLDER = "portfolio_videos";

 public VideoService(VideoRepository repo, CloudinaryService cloudinary) {
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

 // Admin: create video
 public Video create(String title, MultipartFile file) {

  if (title == null || title.trim().isEmpty()) {
   throw new IllegalArgumentException("Title is required");
  }

  if (file == null || file.isEmpty()) {
   throw new IllegalArgumentException("Video file is required");
  }

  // Upload to Cloudinary instead of local disk
  String videoUrl = cloudinary.uploadVideo(file, FOLDER);

  Video video = new Video();
  video.setTitle(title);
  video.setVideoUrl(videoUrl);

  return repo.save(video);
 }

 // Admin: update video
 public Video update(Long id, String title, MultipartFile file) {

  Video video = get(id);

  if (title != null && !title.trim().isEmpty()) {
   video.setTitle(title);
  }

  // If new video is selected
  if (file != null && !file.isEmpty()) {

   String oldUrl = video.getVideoUrl();

   String newUrl = cloudinary.uploadVideo(file, FOLDER);

   video.setVideoUrl(newUrl);

   // Delete old video from Cloudinary
   cloudinary.deleteVideo(oldUrl);
  }

  return repo.save(video);
 }

 // Admin: delete video
 public void delete(Long id) {

  Video video = get(id);

  // Delete video from Cloudinary
  cloudinary.deleteVideo(video.getVideoUrl());

  // Delete database record
  repo.delete(video);
 }
}