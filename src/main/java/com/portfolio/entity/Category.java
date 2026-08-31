package com.portfolio.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name="categories")
public class Category {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true) private String name;
    private String description;
    @Column(nullable=false) private Instant createdAt=Instant.now();
    public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;} public Instant getCreatedAt(){return createdAt;}
}
