package com.futurecollege.backend.service;
import com.futurecollege.backend.model.Review;
import com.futurecollege.backend.repository.ReviewRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepo;

    public ReviewService(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    public Review addReview(Review review) {
        return reviewRepo.save(review);
    }

    public List<Review> getReviewsByCollege(String collegeName) {
        return reviewRepo.findByCollegeName(collegeName);
    }

    public void deleteReview(Long id) {
        reviewRepo.deleteById(id);
    }

    public List<Review> getAllReviews() {
        return reviewRepo.findAll();
    }
}
