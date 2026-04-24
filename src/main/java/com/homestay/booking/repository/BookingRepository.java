package com.homestay.booking.repository;

import com.homestay.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByHomestayId(Long homestayId);
    
    @Query("SELECT b FROM Booking b WHERE b.homestay.id = :homestayId " +
           "AND b.status IN ('PENDING', 'APPROVED', 'CHECKED_IN') " +
           "AND ((b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate))")
    List<Booking> findConflictingBookings(@Param("homestayId") Long homestayId, 
                                          @Param("checkInDate") Date checkInDate, 
                                          @Param("checkOutDate") Date checkOutDate);
}
