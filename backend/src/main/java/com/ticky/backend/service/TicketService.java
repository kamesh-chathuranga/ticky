package com.ticky.backend.service;

import com.ticky.backend.dto.response.TicketSaleHistory;
import com.ticky.backend.entity.Ticket;
import com.ticky.backend.entity.User;
import com.ticky.backend.exception.EventException;
import com.ticky.backend.repository.TicketRepository;
import com.ticky.backend.util.MonthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final EventService eventService;
    private final TicketRepository ticketRepository;

    public Ticket createTicket(Long eventId) throws EventException {
        var event = eventService.findEventById(eventId);

        return Ticket.builder()
                .price(event.getTicketPrice())
                .isSold(false)
                .event(event)
                .build();
    }

    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    @Transactional
    public void purchaseTicket(Ticket ticket, User customer) {
        ticket.setIsSold(true);
        ticket.setCustomer(customer);
        ticket.setPurchaseTimestamp(LocalDateTime.now());
        ticketRepository.save(ticket);
    }

    public List<Ticket> getAllSoldTicketsByCustomerId(Long customerId) throws EventException {
        return ticketRepository.findAllByCustomerIdAndIsSoldTrue(customerId);
    }

    public List<TicketSaleHistory> getTicketSalesForYear(int year, Long vendorId) {
        List<Object[]> results = ticketRepository.findTicketSalesPerMonthByVendor(year, vendorId);

        if (results.isEmpty()) {
            throw new IllegalArgumentException("No ticket sales found for the year: " + year);
        }

        List<TicketSaleHistory> ticketSales = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            long count = 0;
            String month = MonthUtil.getMonthName(i + 1);
            for (Object[] result : results) {
                if ((int) result[0] == i + 1) {
                    count = (long) result[1];
                    break;
                }
            }
            ticketSales.add(TicketSaleHistory.builder()
                    .month(month)
                    .totalSoldTickets(count)
                    .build());
        }

        return ticketSales;
    }
}
