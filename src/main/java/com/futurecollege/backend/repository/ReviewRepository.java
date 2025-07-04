package com.futurecollege.backend.repository;

import com.futurecollege.backend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCollegeName(String collegeName);
}
