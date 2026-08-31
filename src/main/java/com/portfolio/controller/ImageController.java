package com.portfolio.controller;
import com.portfolio.entity.Image; import com.portfolio.service.ImageService; import org.springframework.http.MediaType; import org.springframework.web.bind.annotation.*; import org.springframework.web.multipart.MultipartFile; import java.util.*;
@RestController
@RequestMapping("/api/images")

public class ImageController {private final ImageService s;
 public ImageController(ImageService s){this.s=s;}
 @GetMapping public List<Image> publicAll(){
  return s.publicAll();
 }
 @GetMapping("/{id}") public Image get(@PathVariable Long id){return s.get(id);}

 @GetMapping("/admin/all") public List<Image> adminAll(){return s.adminAll();}
 @PostMapping(value="/admin",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
 public Image create(@RequestParam String title,
                     @RequestParam(required=false) String description,@RequestParam(required=false) Long categoryId,@RequestParam(required=false) Integer displayOrder,@RequestParam(required=false) Boolean visible,@RequestPart MultipartFile file){return s.create(title,description,categoryId,displayOrder,visible,file);}
 @PutMapping(value="/admin/{id}",consumes=MediaType.MULTIPART_FORM_DATA_VALUE) public Image update(@PathVariable Long id,@RequestParam(required=false) String title,@RequestParam(required=false) String description,@RequestParam(required=false) Long categoryId,@RequestParam(required=false) Integer displayOrder,@RequestParam(required=false) Boolean visible,@RequestPart(required=false) MultipartFile file){return s.update(id,title,description,categoryId,displayOrder,visible,file);}
 @DeleteMapping("/admin/{id}") public void delete(@PathVariable Long id){s.delete(id);}}
