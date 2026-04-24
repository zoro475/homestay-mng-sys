package com.homestay.booking.service;

import com.homestay.booking.entity.Homestay;
import com.homestay.booking.repository.HomestayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomestayService {
    @Autowired
    private HomestayRepository homestayRepository;

    public List<Homestay> findAll() {
        return homestayRepository.findAll();
    }

    public org.springframework.data.domain.Page<Homestay> findAll(org.springframework.data.domain.Pageable pageable) {
        return homestayRepository.findAll(pageable);
    }

    public List<Homestay> findAvailable() {
        return homestayRepository.findByStatus("AVAILABLE");
    }

    public org.springframework.data.domain.Page<Homestay> findAvailable(org.springframework.data.domain.Pageable pageable) {
        return homestayRepository.findByStatus("AVAILABLE", pageable);
    }

    public List<Homestay> findByCity(String city) {
        return homestayRepository.findByCityContainingIgnoreCase(city, org.springframework.data.domain.Pageable.unpaged()).getContent();
    }

    public org.springframework.data.domain.Page<Homestay> findByCity(String city, org.springframework.data.domain.Pageable pageable) {
        return homestayRepository.findByCityContainingIgnoreCase(city, pageable);
    }

    public Homestay findById(Long id) {
        return homestayRepository.findById(id).orElseThrow(() -> new RuntimeException("Homestay not found"));
    }

    public Homestay save(Homestay homestay) {
        return homestayRepository.save(homestay);
    }

    public void delete(Long id) {
        homestayRepository.deleteById(id);
    }
}
