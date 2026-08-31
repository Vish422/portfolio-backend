package com.portfolio.repository;
import com.portfolio.entity.Image; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface ImageRepository extends JpaRepository<Image,Long>{ List<Image> findByVisibleTrueOrderByDisplayOrderAscCreatedAtDesc(); List<Image> findAllByOrderByDisplayOrderAscCreatedAtDesc(); }
