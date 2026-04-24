package com.homestay.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Column(name = "discount_percent", nullable = false)
    private Double discountPercent;

    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;

    @Column(name = "max_usage")
    private Integer maxUsage;

    @Column(name = "used_count")
    private Integer usedCount = 0;
}
