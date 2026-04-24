package com.homestay.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "homestays")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Homestay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "price_per_night", nullable = false)
    private Double pricePerNight;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 100)
    private String city;

    @Builder.Default
    @Column(length = 20)
    private String status = "AVAILABLE"; // AVAILABLE, MAINTENANCE

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Builder.Default
    @Column(name = "housekeeping_status", length = 20)
    private String housekeepingStatus = "CLEAN"; // CLEAN, DIRTY, CLEANING

    @Builder.Default
    @Column(nullable = false)
    private Integer capacity = 1;

    @Builder.Default
    @Column(name = "bed_count", nullable = false)
    private Integer bedCount = 1;

    @Builder.Default
    @Column(name = "bath_count", nullable = false)
    private Integer bathCount = 1;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @ManyToMany
    @JoinTable(
        name = "homestay_amenities",
        joinColumns = @JoinColumn(name = "homestay_id"),
        inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private List<Amenity> amenities;

    @OneToMany(mappedBy = "homestay", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "homestay", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;
}
