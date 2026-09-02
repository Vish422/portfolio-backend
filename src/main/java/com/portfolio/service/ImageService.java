package com.portfolio.service;

import com.portfolio.entity.Image;
import com.portfolio.repository.CategoryRepository;
import com.portfolio.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ImageService {

 private final ImageRepository repo;
 private final CategoryRepository cats;
 private final CloudinaryService cloudinary;

 public ImageService(
         ImageRepository repo,
         CategoryRepository cats,
         CloudinaryService cloudinary
 ) {
  this.repo = repo;
  this.cats = cats;
  this.cloudinary = cloudinary;
 }

 public List<Image> publicAll() {
  return repo.findByVisibleTrueOrderByDisplayOrderAscCreatedAtDesc();
 }

 public List<Image> adminAll() {
  return repo.findAllByOrderByDisplayOrderAscCreatedAtDesc();
 }

 public Image get(Long id) {
  return repo.findById(id)
          .orElseThrow(() -> new NoSuchElementException("Image not found"));
 }

 public Image create(
         String title,
         String description,
         Long categoryId,
         Integer order,
         Boolean visible,
         MultipartFile file
 ) {

  if (title == null || title.isBlank()) {
   throw new IllegalArgumentException("Title is required");
  }

  if (file == null || file.isEmpty()) {
   throw new IllegalArgumentException("Image file is required");
  }

  Image x = new Image();

  x.setTitle(title);
  x.setDescription(description);

  // Upload image to Cloudinary
  String imageUrl = cloudinary.uploadImage(file);

  x.setImageUrl(imageUrl);

  x.setDisplayOrder(order == null ? 0 : order);
  x.setVisible(visible == null || visible);

  if (categoryId != null) {
   x.setCategory(
           cats.findById(categoryId)
                   .orElseThrow(() ->
                           new NoSuchElementException("Category not found")
                   )
   );
  }

  return repo.save(x);
 }

 public Image update(
         Long id,
         String title,
         String description,
         Long categoryId,
         Integer order,
         Boolean visible,
         MultipartFile file
 ) {

  Image x = get(id);

  if (title != null && !title.isBlank()) {
   x.setTitle(title);
  }

  x.setDescription(description);

  if (categoryId != null) {
   x.setCategory(
           cats.findById(categoryId)
                   .orElseThrow(() ->
                           new NoSuchElementException("Category not found")
                   )
   );
  }

  if (order != null) {
   x.setDisplayOrder(order);
  }

  if (visible != null) {
   x.setVisible(visible);
  }

  // New image uploaded
  if (file != null && !file.isEmpty()) {

   String imageUrl = cloudinary.uploadImage(file);

   x.setImageUrl(imageUrl);
  }

  x.touch();

  return repo.save(x);
 }

 public void delete(Long id) {

  Image x = get(id);

  // Database se image record delete
  repo.delete(x);
 }
}