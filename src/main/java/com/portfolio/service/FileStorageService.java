package com.portfolio.service;

import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service; import org.springframework.util.StringUtils; import org.springframework.web.multipart.MultipartFile; import java.io.*; import java.nio.file.*; import java.util.*;

@Service public class FileStorageService {
 private final Path root;
 public FileStorageService(@Value("${app.upload.dir}") String dir)throws IOException{root=Paths.get(dir).toAbsolutePath().normalize(); Files.createDirectories(root.resolve("images")); Files.createDirectories(root.resolve("videos"));}
 public String save(MultipartFile file,String type){if(file==null||file.isEmpty())throw new IllegalArgumentException("File is required"); String ct=Optional.ofNullable(file.getContentType()).orElse(""); if(type.equals("images")&&!ct.startsWith("image/"))throw new IllegalArgumentException("Only image files are allowed"); if(type.equals("videos")&&!ct.startsWith("video/"))throw new IllegalArgumentException("Only video files are allowed"); String ext=""; String original=StringUtils.cleanPath(Optional.ofNullable(file.getOriginalFilename()).orElse("file")); int i=original.lastIndexOf('.'); if(i>=0)ext=original.substring(i).toLowerCase(); String name=UUID.randomUUID()+ext; try{Path dir=root.resolve(type).normalize(); Path target=dir.resolve(name).normalize(); if(!target.startsWith(dir))throw new IllegalArgumentException("Invalid filename"); Files.copy(file.getInputStream(),target,StandardCopyOption.REPLACE_EXISTING); return "/uploads/"+type+"/"+name;}catch(IOException e){throw new RuntimeException("Could not store file",e);}}
 public void deleteByUrl(String url){if(url==null||!url.startsWith("/uploads/"))return; try{Files.deleteIfExists(root.resolve(url.substring("/uploads/".length())).normalize());}catch(IOException ignored){}}
}
