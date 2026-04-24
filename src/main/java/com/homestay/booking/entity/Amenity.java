package com.homestay.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "amenities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 50)
    private String icon;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @ManyToMany(mappedBy = "amenities")
    private List<Homestay> homestays;
}
