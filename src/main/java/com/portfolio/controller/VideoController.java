package com.portfolio.controller;

import com.portfolio.entity.Video;
import com.portfolio.service.VideoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

 private final VideoService s;

 public VideoController(VideoService s) {
  this.s = s;
 }

 // Request body for create/update (title + Cloudinary URL)
 public record VideoRequest(String title, String videoUrl) {}

 // Public: saare videos
 @GetMapping
 public List<Video> publicAll() {
  return s.publicAll();
 }

 // Single video
 @GetMapping("/{id}")
 public Video get(@PathVariable Long id) {
  return s.get(id);
 }

 // Admin: saare videos
 @GetMapping("/admin/all")
 public List<Video> adminAll() {
  return s.adminAll();
 }

 // Admin: create video (Cloudinary URL already uploaded by frontend)
 @PostMapping("/admin")
 public Video create(@RequestBody VideoRequest request) {
  return s.create(request.title(), request.videoUrl());
 }

 // Admin: update video
 @PutMapping("/admin/{id}")
 public Video update(
         @PathVariable Long id,
         @RequestBody VideoRequest request
 ) {
  return s.update(id, request.title(), request.videoUrl());
 }

 // Admin: delete video
 @DeleteMapping("/admin/{id}")
 public void delete(@PathVariable Long id) {
  s.delete(id);
 }
}