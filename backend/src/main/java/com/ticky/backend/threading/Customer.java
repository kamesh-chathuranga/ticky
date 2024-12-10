package com.ticky.backend.threading;

import com.ticky.backend.entity.User;
import com.ticky.backend.notification.Notification;
import com.ticky.backend.notification.NotificationService;
import com.ticky.backend.service.EventService;
import com.ticky.backend.service.TicketPoolService;
import com.ticky.backend.service.TicketService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@RequiredArgsConstructor
public class Customer implements Runnable {
    private final User customer;
    private final Long eventId;
    private final TicketService ticketService;
    private final TicketPoolService ticketPoolService;
    private final NotificationService notificationService;
    private final EventService eventService;

    @Override
    public void run() throws RuntimeException {
        try {
            TicketPool ticketPool = ticketPoolService.getTicketPool();

            if (ticketPool == null) {
                throw new RuntimeException("Ticket pool not found");
            }
            var ticket = ticketPool.removeTicket(eventId);
            ticketService.purchaseTicket(ticket, customer);

            notificationService.sendNotification(
                    eventId.toString(),
                    Notification.builder()
                            .eventId(eventId)
                            .message("Ticket brought by " + customer.getFirstName() + " for Event ID")
                            .timestamp(LocalDateTime.now())
                            .build()
            );

            int availableTickets = eventService.updateEventAvailableTicket(eventId);
            notificationService.sendAvailableTickets(eventId.toString(), availableTickets);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
