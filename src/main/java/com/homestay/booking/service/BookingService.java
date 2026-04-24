package com.homestay.booking.service;

import com.homestay.booking.entity.Booking;
import com.homestay.booking.entity.Homestay;
import com.homestay.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private HomestayService homestayService;

    @Autowired
    private com.homestay.booking.repository.CouponRepository couponRepository;

    public Booking createBooking(Booking booking) {
        // Validate dates
        if (booking.getCheckInDate().after(booking.getCheckOutDate())) {
            throw new RuntimeException("Check-out date must be after check-in date");
        }

        // Check availability
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                booking.getHomestay().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Homestay is already booked for these dates.");
        }

        // Calculate total price
        Homestay homestay = homestayService.findById(booking.getHomestay().getId());
        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate().toLocalDate(), booking.getCheckOutDate().toLocalDate());
        if (nights <= 0) nights = 1;

        double basePrice = homestay.getPricePerNight() * nights;

        // Apply discount if a coupon was used
        if (booking.getCouponCode() != null && !booking.getCouponCode().isEmpty()) {
            couponRepository.findByCode(booking.getCouponCode().toUpperCase()).ifPresent(coupon -> {
                double discount = Math.min(basePrice, (basePrice * coupon.getDiscountPercent() / 100));
                booking.setDiscountAmount(discount);
                
                // Increment usage
                coupon.setUsedCount(coupon.getUsedCount() + 1);
                couponRepository.save(coupon);
            });
        }
        
        booking.setTotalPrice(basePrice - (booking.getDiscountAmount() != null ? booking.getDiscountAmount() : 0));
        booking.setStatus("PENDING");
        booking.setPaymentStatus("UNPAID");

        return bookingRepository.save(booking);
    }

    public List<Booking> findByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }
    
    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public void updateStatus(Long bookingId, String newStatus) {
        Booking booking = findById(bookingId);
        booking.setStatus(newStatus);
        bookingRepository.save(booking);
    }
    
    public void updatePaymentStatus(Long bookingId, String paymentStatus) {
        Booking booking = findById(bookingId);
        booking.setPaymentStatus(paymentStatus);
        bookingRepository.save(booking);
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }
}
