package com.portfolio.controller;

import com.portfolio.entity.Video;
import com.portfolio.service.VideoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

 private final VideoService s;

 public VideoController(VideoService s) {
  this.s = s;
 }

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

 // Admin: create video
 @PostMapping(
         value = "/admin",
         consumes = MediaType.MULTIPART_FORM_DATA_VALUE
 )
 public Video create(
         @RequestParam String title,
         @RequestPart MultipartFile file
 ) {
  return s.create(title, file);
 }

 // Admin: update video
 @PutMapping(
         value = "/admin/{id}",
         consumes = MediaType.MULTIPART_FORM_DATA_VALUE
 )
 public Video update(
         @PathVariable Long id,
         @RequestParam String title,
         @RequestPart(required = false) MultipartFile file
 ) {
  return s.update(id, title, file);
 }

 // Admin: delete video
 @DeleteMapping("/admin/{id}")
 public void delete(@PathVariable Long id) {
  s.delete(id);
 }
}