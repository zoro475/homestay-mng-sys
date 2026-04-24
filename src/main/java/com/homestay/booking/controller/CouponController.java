package com.homestay.booking.controller;

import com.homestay.booking.entity.Coupon;
import com.homestay.booking.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.util.Collections;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    
    @Autowired
    private CouponRepository couponRepository;

    @GetMapping("/validate")
    public ResponseEntity<?> validateCoupon(@RequestParam String code) {
        return couponRepository.findByCode(code.toUpperCase())
            .map(coupon -> {
                if (coupon.getExpiryDate().before(new Date(System.currentTimeMillis()))) {
                    return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Mã giảm giá đã hết hạn!"));
                }
                if (coupon.getMaxUsage() != null && coupon.getUsedCount() >= coupon.getMaxUsage()) {
                    return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Mã giảm giá đã hết lượt sử dụng!"));
                }
                return ResponseEntity.ok(coupon);
            })
            .orElseGet(() -> ResponseEntity.badRequest().body(Collections.singletonMap("error", "Mã giảm giá không tồn tại!")));
    }
}
