package com.homestay.booking.repository;

import com.homestay.booking.entity.Homestay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface HomestayRepository extends JpaRepository<Homestay, Long> {
    Page<Homestay> findByStatus(String status, Pageable pageable);
    Page<Homestay> findByCityContainingIgnoreCase(String city, Pageable pageable);
    
    // Keep list versions for simple fetches if needed, or convert all to Page
    List<Homestay> findByStatus(String status);
    List<Homestay> findByCategoryId(Long categoryId);
}
