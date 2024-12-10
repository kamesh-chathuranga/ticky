package com.ticky.backend.repository;

import com.ticky.backend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByCustomerIdAndIsSoldTrue(Long customerId);

    @Query("""
             SELECT MONTH(t.purchaseTimestamp) AS month, COUNT(t) AS count
             FROM Ticket t
             WHERE YEAR(t.purchaseTimestamp) = :year AND t.event.vendor.id = :vendorId
             GROUP BY MONTH(t.purchaseTimestamp)
            """)
    List<Object[]> findTicketSalesPerMonthByVendor(@Param("year") int year, @Param("vendorId") Long vendorId);
}
