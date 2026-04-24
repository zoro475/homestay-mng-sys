package com.homestay.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_addons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceAddon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private Double price;

    @Column(columnDefinition = "TEXT")
    private String description;
}
