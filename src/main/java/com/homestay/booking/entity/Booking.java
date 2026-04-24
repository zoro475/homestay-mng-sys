package com.homestay.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homestay_id", nullable = false)
    private Homestay homestay;

    @Column(name = "check_in_date", nullable = false)
    private Date checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private Date checkOutDate;

    @Builder.Default
    @Column(name = "guest_count", nullable = false)
    private Integer guestCount = 1;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "coupon_code", length = 50)
    private String couponCode;

    @Builder.Default
    @Column(name = "discount_amount")
    private Double discountAmount = 0.0;

    @Builder.Default
    @Column(name = "payment_status", length = 20)
    private String paymentStatus = "UNPAID"; // UNPAID, PAID, REFUNDED

    @Builder.Default
    @Column(length = 20)
    private String status = "PENDING"; // PENDING, APPROVED, CHECKED_IN, COMPLETED, CANCELED

    @Column(columnDefinition = "TEXT")
    private String note;

    @Builder.Default
    @Column(name = "deposit_amount")
    private Double depositAmount = 0.0;

    @Builder.Default
    @Column(name = "surcharge_amount")
    private Double surchargeAmount = 0.0;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;
}
