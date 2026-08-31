package com.portfolio.service;

import com.portfolio.entity.Video;
import com.portfolio.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class VideoService {

 private final VideoRepository repo;
 private final FileStorageService files;

 public VideoService(VideoRepository repo, FileStorageService files) {
  this.repo = repo;
  this.files = files;
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

  // Save video inside uploads/videos
  String videoUrl = files.save(file, "videos");

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

   String newUrl = files.save(file, "videos");

   video.setVideoUrl(newUrl);

   // Delete old video
   files.deleteByUrl(oldUrl);
  }

  return repo.save(video);
 }

 // Admin: delete video
 public void delete(Long id) {

  Video video = get(id);

  // Delete physical video file
  files.deleteByUrl(video.getVideoUrl());

  // Delete database record
  repo.delete(video);
 }
}