package com.homestay.booking.repository;

import com.homestay.booking.entity.ServiceAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAddonRepository extends JpaRepository<ServiceAddon, Long> {
}
