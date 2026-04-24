package com.homestay.booking.controller;

import com.homestay.booking.entity.Booking;
import com.homestay.booking.entity.Homestay;
import com.homestay.booking.entity.User;
import com.homestay.booking.service.BookingService;
import com.homestay.booking.service.HomestayService;
import com.homestay.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.sql.Date;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private HomestayService homestayService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public String createBooking(@RequestParam Long homestayId,
                                @RequestParam Date checkInDate,
                                @RequestParam Date checkOutDate,
                                @RequestParam Integer guestCount,
                                @RequestParam(required = false) String couponCode,
                                Principal principal,
                                Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        // --- VALIDATION ---
        
        // 1. Check if user is blacklisted
        if (user.getIsBlacklisted() != null && user.getIsBlacklisted()) {
            model.addAttribute("error", "Tài khoản của bạn đang bị hạn chế đặt phòng. Vui lòng liên hệ hỗ trợ.");
            model.addAttribute("homestay", homestayService.findById(homestayId));
            return "homestay_detail";
        }

        // 2. Date validations
        long todayMillis = System.currentTimeMillis();
        Date today = new Date(todayMillis);
        
        if (checkInDate.before(today)) {
            model.addAttribute("error", "Ngày nhận phòng không thể ở trong quá khứ.");
            model.addAttribute("homestay", homestayService.findById(homestayId));
            return "homestay_detail";
        }

        if (!checkOutDate.after(checkInDate)) {
            model.addAttribute("error", "Ngày trả phòng phải sau ngày nhận phòng.");
            model.addAttribute("homestay", homestayService.findById(homestayId));
            return "homestay_detail";
        }

        // 3. Guest count validation
        Homestay homestay = homestayService.findById(homestayId);
        if (guestCount == null || guestCount < 1) {
            model.addAttribute("error", "Số lượng khách phải ít nhất là 1.");
            model.addAttribute("homestay", homestay);
            return "homestay_detail";
        }
        
        if (homestay.getCapacity() != null && guestCount > homestay.getCapacity()) {
            model.addAttribute("error", "Vượt quá sức chứa tối đa của phòng (" + homestay.getCapacity() + " người).");
            model.addAttribute("homestay", homestay);
            return "homestay_detail";
        }

        Booking booking = Booking.builder()
                .user(user)
                .homestay(homestay)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .guestCount(guestCount)
                .couponCode(couponCode)
                .build();

        try {
            Booking savedBooking = bookingService.createBooking(booking);
            return "redirect:/booking/payment/" + savedBooking.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("homestay", homestay);
            return "homestay_detail";
        }
    }

    @Autowired
    private com.homestay.booking.repository.ReviewRepository reviewRepository;

    @org.springframework.web.bind.annotation.GetMapping("/payment/{id}")
    public String paymentPage(@org.springframework.web.bind.annotation.PathVariable Long id, Principal principal, Model model) {
        if (principal == null) return "redirect:/login";
        try {
            model.addAttribute("booking", bookingService.findById(id));
            // Provide fake Admin bank details
            model.addAttribute("bankName", "Vietcombank (VCB)");
            model.addAttribute("accountNumber", "012345678910");
            model.addAttribute("accountName", "ADMIN HOMESTAY BOOKING");
            return "payment";
        } catch(Exception e) {
            return "redirect:/my-bookings";
        }
    }

    @PostMapping("/pay/{id}")
    public String payBooking(@org.springframework.web.bind.annotation.PathVariable Long id, Principal principal) {
        if (principal == null) return "redirect:/login";
        try {
            bookingService.updatePaymentStatus(id, "PAID");
            bookingService.updateStatus(id, "COMPLETED");
        } catch (Exception e) {}
        return "redirect:/my-bookings";
    }

    @org.springframework.web.bind.annotation.GetMapping("/review/{id}")
    public String reviewPage(@org.springframework.web.bind.annotation.PathVariable Long id, Principal principal, Model model) {
        if (principal == null) return "redirect:/login";
        try {
            model.addAttribute("booking", bookingService.findById(id));
            return "review";
        } catch(Exception e) {
            return "redirect:/my-bookings";
        }
    }

    @PostMapping("/review/{id}")
    public String submitReview(@org.springframework.web.bind.annotation.PathVariable Long id, 
                               @RequestParam Integer rating, 
                               @RequestParam String comment, 
                               Principal principal) {
        if (principal == null) return "redirect:/login";
        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (user == null) return "redirect:/login";

        try {
            Booking booking = bookingService.findById(id);
            com.homestay.booking.entity.Review review = com.homestay.booking.entity.Review.builder()
                .booking(booking)
                .homestay(booking.getHomestay())
                .user(user)
                .rating(rating)
                .comment(comment)
                .build();
            reviewRepository.save(review);
        } catch (Exception e) {}
        return "redirect:/my-bookings";
    }
}
