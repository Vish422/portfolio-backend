package com.portfolio.service;

import com.portfolio.entity.Video;
import com.portfolio.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {

 private final VideoRepository repo;

 public VideoService(VideoRepository repo) {
  this.repo = repo;
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

 // Admin: create video (URL already uploaded to Cloudinary by frontend)
 public Video create(String title, String videoUrl) {

  if (title == null || title.trim().isEmpty()) {
   throw new IllegalArgumentException("Title is required");
  }

  if (videoUrl == null || videoUrl.trim().isEmpty()) {
   throw new IllegalArgumentException("Video URL is required");
  }

  Video video = new Video();
  video.setTitle(title);
  video.setVideoUrl(videoUrl);

  return repo.save(video);
 }

 // Admin: update video
 public Video update(Long id, String title, String videoUrl) {

  Video video = get(id);

  if (title != null && !title.trim().isEmpty()) {
   video.setTitle(title);
  }

  if (videoUrl != null && !videoUrl.trim().isEmpty()) {
   video.setVideoUrl(videoUrl);
  }

  return repo.save(video);
 }

 // Admin: delete video
 public void delete(Long id) {
  Video video = get(id);
  repo.delete(video);
 }
}