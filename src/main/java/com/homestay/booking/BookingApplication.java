package com.homestay.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.homestay.booking.repository.UserRepository;

@SpringBootApplication
public class BookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataSeeder(com.homestay.booking.repository.UserRepository userRepository,
                                        org.springframework.security.crypto.password.PasswordEncoder passwordEncoder,
                                        com.homestay.booking.repository.CouponRepository couponRepository,
                                        com.homestay.booking.repository.HomestayRepository homestayRepository) {
        return args -> {
            // Seed Admin
            userRepository.findByUsername("admin").ifPresent(admin -> {
                admin.setPassword(passwordEncoder.encode("123456"));
                userRepository.save(admin);
            });
            
            // Seed Khach hang
            userRepository.findByUsername("khachhang").ifPresent(khach -> {
                khach.setPassword(passwordEncoder.encode("123456"));
                userRepository.save(khach);
            });

            // Seed Coupons
            if (couponRepository.count() == 0) {
                couponRepository.save(com.homestay.booking.entity.Coupon.builder()
                        .code("SUMMER20").discountPercent(20.0)
                        .expiryDate(java.sql.Date.valueOf("2026-12-31")).maxUsage(100).usedCount(0).build());
                couponRepository.save(com.homestay.booking.entity.Coupon.builder()
                        .code("WELCOME10").discountPercent(10.0)
                        .expiryDate(java.sql.Date.valueOf("2026-12-31")).maxUsage(50).usedCount(0).build());
            }
            // Fix missing or broken images
            homestayRepository.findAll().forEach(h -> {
                if(h.getName().contains("Sapa") || (h.getImageUrl() != null && h.getImageUrl().contains("unsplash"))) {
                    if (h.getName().contains("Sapa")) {
                        h.setImageUrl("https://images.pexels.com/photos/147411/italy-mountains-dawn-daybreak-147411.jpeg?auto=compress&cs=tinysrgb&w=800");
                    } else if (h.getName().contains("Villa Đ")) {
                        h.setImageUrl("https://images.pexels.com/photos/106399/pexels-photo-106399.jpeg?auto=compress&cs=tinysrgb&w=800");
                    } else if (h.getName().contains("Nhà gỗ")) {
                        h.setImageUrl("https://images.pexels.com/photos/1370704/pexels-photo-1370704.jpeg?auto=compress&cs=tinysrgb&w=800");
                    } else if (h.getName().contains("Studio")) {
                        h.setImageUrl("https://images.pexels.com/photos/271618/pexels-photo-271618.jpeg?auto=compress&cs=tinysrgb&w=800");
                    } else {
                        h.setImageUrl("https://images.pexels.com/photos/106399/pexels-photo-106399.jpeg?auto=compress&cs=tinysrgb&w=800");
                    }
                    homestayRepository.save(h);
                }
            });
        };
    }
}
