package com.homestay.booking.controller;

import com.homestay.booking.entity.Booking;
import com.homestay.booking.entity.Homestay;
import com.homestay.booking.entity.User;
import com.homestay.booking.repository.CouponRepository;
import com.homestay.booking.repository.UserRepository;
import com.homestay.booking.service.BookingService;
import com.homestay.booking.service.HomestayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private HomestayService homestayService;
    
    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Booking> bookings = bookingService.findAll();
        double totalRevenue = bookings.stream()
                .filter(b -> ("COMPLETED".equals(b.getStatus()) || "PAID".equals(b.getPaymentStatus())) && b.getTotalPrice() != null)
                .mapToDouble(Booking::getTotalPrice)
                .sum();
        
        model.addAttribute("totalHomestays", homestayService.findAll().size());
        model.addAttribute("totalBookings", bookings.size());
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("recentBookings", bookings.size() > 5 ? bookings.subList(bookings.size() - 5, bookings.size()) : bookings);
        return "admin/dashboard";
    }

    // --- HOMESTAY MANAGEMENT ---
    @GetMapping("/homestays")
    public String manageHomestays(@RequestParam(defaultValue = "0") int page, Model model) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, 10);
        org.springframework.data.domain.Page<Homestay> homestayPage = homestayService.findAll(pageable);
        
        model.addAttribute("homestays", homestayPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", homestayPage.getTotalPages());
        return "admin/homestays";
    }

    @Autowired
    private com.homestay.booking.repository.CategoryRepository categoryRepository;

    @GetMapping("/homestays/new")
    public String newHomestay(Model model) {
        model.addAttribute("homestay", new Homestay());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/homestay_form";
    }

    @GetMapping("/homestays/edit/{id}")
    public String editHomestay(@PathVariable Long id, Model model) {
        model.addAttribute("homestay", homestayService.findById(id));
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/homestay_form";
    }

    @PostMapping("/homestays/save")
    public String saveHomestay(@ModelAttribute Homestay homestay) {
        // Simple fix for category if missing
        if (homestay.getCategory() == null || homestay.getCategory().getId() == null) {
            homestay.setCategory(categoryRepository.findAll().get(0));
        }
        homestayService.save(homestay);
        return "redirect:/admin/homestays";
    }

    @GetMapping("/homestays/delete/{id}")
    public String deleteHomestay(@PathVariable Long id) {
        homestayService.delete(id);
        return "redirect:/admin/homestays";
    }

    @PostMapping("/homestays/housekeeping/{id}")
    public String updateHousekeeping(@PathVariable Long id, @RequestParam String status) {
        Homestay h = homestayService.findById(id);
        h.setHousekeepingStatus(status);
        homestayService.save(h);
        return "redirect:/admin/homestays";
    }

    // --- BOOKING MANAGEMENT ---
    @GetMapping("/bookings")
    public String manageBookings(Model model) {
        model.addAttribute("bookings", bookingService.findAll());
        return "admin/bookings";
    }

    @PostMapping("/bookings/status/{id}")
    public String updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        bookingService.updateStatus(id, status);
        return "redirect:/admin/bookings";
    }

    @PostMapping("/bookings/finance/{id}")
    public String updateBookingFinance(@PathVariable Long id, 
                                       @RequestParam Double deposit, 
                                       @RequestParam Double surcharge) {
        Booking b = bookingService.findById(id);
        b.setDepositAmount(deposit);
        b.setSurchargeAmount(surcharge);
        bookingService.save(b);
        return "redirect:/admin/bookings";
    }

    @GetMapping("/bookings/delete/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.delete(id);
        return "redirect:/admin/bookings";
    }

    // --- USER MANAGEMENT ---
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @PostMapping("/users/blacklist/{id}")
    public String toggleBlacklist(@PathVariable Long id) {
        User u = userRepository.findById(id).orElseThrow();
        boolean currentStatus = u.getIsBlacklisted() != null && u.getIsBlacklisted();
        u.setIsBlacklisted(!currentStatus);
        userRepository.save(u);
        return "redirect:/admin/users";
    }

    // --- COUPON MANAGEMENT ---
    @GetMapping("/coupons")
    public String manageCoupons(Model model) {
        model.addAttribute("coupons", couponRepository.findAll());
        return "admin/coupons";
    }

    // --- REPORTS ---
    @GetMapping("/reports")
    public String viewReports(Model model) {
        List<Booking> bookings = bookingService.findAll();
        
        // Basic stats
        long pending = bookings.stream().filter(b -> "PENDING".equals(b.getStatus())).count();
        long completed = bookings.stream().filter(b -> "COMPLETED".equals(b.getStatus())).count();
        long canceled = bookings.stream().filter(b -> "CANCELED".equals(b.getStatus())).count();
        
        double totalRevenue = bookings.stream()
                .filter(b -> ("COMPLETED".equals(b.getStatus()) || "PAID".equals(b.getPaymentStatus())) && b.getTotalPrice() != null)
                .mapToDouble(Booking::getTotalPrice)
                .sum();

        model.addAttribute("totalBookings", bookings.size());
        model.addAttribute("pendingBookings", pending);
        model.addAttribute("completedBookings", completed);
        model.addAttribute("canceledBookings", canceled);
        model.addAttribute("totalRevenue", totalRevenue);
        
        return "admin/reports";
    }
}
