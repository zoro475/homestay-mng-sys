package com.homestay.booking.controller;

import com.homestay.booking.entity.Homestay;
import com.homestay.booking.service.HomestayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import com.homestay.booking.entity.User;
import com.homestay.booking.service.UserService;
import com.homestay.booking.service.BookingService;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private HomestayService homestayService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/")
    public String home(@org.springframework.web.bind.annotation.RequestParam(required = false) String city, 
                       @org.springframework.web.bind.annotation.RequestParam(defaultValue = "0") int page,
                       Model model) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, 9); // 9 items per page
        org.springframework.data.domain.Page<Homestay> homestayPage;
        
        if (city != null && !city.trim().isEmpty()) {
            homestayPage = homestayService.findByCity(city.trim(), pageable);
        } else {
            homestayPage = homestayService.findAvailable(pageable);
        }
        
        model.addAttribute("homestays", homestayPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", homestayPage.getTotalPages());
        model.addAttribute("city", city);
        return "home";
    }

    @GetMapping("/homestays/{id}")
    public String viewHomestay(@PathVariable Long id, Model model) {
        Homestay homestay = homestayService.findById(id);
        model.addAttribute("homestay", homestay);
        return "homestay_detail";
    }

    @GetMapping("/my-bookings")
    public String myBookings(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("bookings", bookingService.findByUserId(user.getId()));
        return "my_bookings";
    }

    @GetMapping("/flights")
    public String flights() {
        return "flights";
    }

    @GetMapping("/flights-hotels")
    public String flightsHotels() {
        return "flights_hotels";
    }

    @GetMapping("/car-rentals")
    public String carRentals() {
        return "car_rentals";
    }

    @GetMapping("/activities")
    public String activities() {
        return "activities";
    }

    @GetMapping("/airport-taxis")
    public String airportTaxis() {
        return "airport_taxis";
    }
}
