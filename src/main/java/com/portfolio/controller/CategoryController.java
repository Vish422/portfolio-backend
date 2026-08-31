package com.portfolio.controller;
import com.portfolio.dto.CategoryRequest; import com.portfolio.entity.Category; import com.portfolio.service.CategoryService; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/categories")
public class CategoryController {private final CategoryService s;

    public CategoryController(CategoryService s){this.s=s;}
    @GetMapping public List<Category> all(){return s.all();}
    @GetMapping("/{id}")
    public Category get(@PathVariable Long id){return s.get(id);}
    @PostMapping public Category create(@RequestBody CategoryRequest r)
    {return s.create(r);}
    @PutMapping("/{id}")
    public Category update(@PathVariable Long id,@RequestBody CategoryRequest r)
    {
        return s.update(id,r);} @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){s.delete(id);}}
