package com.portfolio.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name="images")
public class Image {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String title;
    @Column(length=2000) private String description;
    @Column(nullable=false) private String imageUrl;
    private String thumbnailUrl;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="category_id") private Category category;
    @Column(nullable=false) private Integer displayOrder=0;
    @Column(nullable=false) private Boolean visible=true;
    @Column(nullable=false) private Instant createdAt=Instant.now();
    @Column(nullable=false) private Instant updatedAt=Instant.now();
    public Long getId(){return id;} public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;} public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
    public String getThumbnailUrl(){return thumbnailUrl;} public void setThumbnailUrl(String v){thumbnailUrl=v;} public Category getCategory(){return category;} public void setCategory(Category v){category=v;}
    public Integer getDisplayOrder(){return displayOrder;} public void setDisplayOrder(Integer v){displayOrder=v;} public Boolean getVisible(){return visible;} public void setVisible(Boolean v){visible=v;}
    public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;} public void touch(){updatedAt=Instant.now();}
}
