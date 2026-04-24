package com.homestay.booking.repository;

import com.homestay.booking.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByHomestayId(Long homestayId);
    List<Review> findByUserId(Long userId);
}
