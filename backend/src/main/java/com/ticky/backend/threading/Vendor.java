package com.ticky.backend.threading;

import com.ticky.backend.entity.Event;
import com.ticky.backend.entity.Ticket;
import com.ticky.backend.exception.EventException;
import com.ticky.backend.notification.Notification;
import com.ticky.backend.notification.NotificationService;
import com.ticky.backend.repository.EventRepository;
import com.ticky.backend.service.TicketPoolService;
import com.ticky.backend.service.TicketService;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class Vendor implements Runnable {
    private final Event event;
    private final int totalTickets;
    private final TicketService ticketService;
    private final TicketPoolService ticketPoolService;
    private final NotificationService notificationService;
    private final EventRepository eventRepository;
    private volatile boolean isRunning;

    @Override
    public void run() {
        notificationService.sendNotification(
                event.getId().toString(),
                Notification.builder()
                        .eventId(event.getId())
                        .message("Ticket releasing started for Event ID")
                        .timestamp(LocalDateTime.now())
                        .build()
        );

        TicketPool ticketPool = ticketPoolService.getTicketPool();

        if (ticketPool == null) {
            throw new RuntimeException("Ticket pool not found");
        }

        for (int i = 0; i < totalTickets && isRunning; i++) {
            try {
                Ticket ticket = ticketService.createTicket(event.getId());
                ticketPool.addTicket(ticket);
                ticketService.saveTicket(ticket);

                event.setAvailableTickets(event.getAvailableTickets() + 1);
                eventRepository.save(event);
                notificationService.sendAvailableTickets(event.getId().toString(), totalTickets);

                notificationService.sendNotification(
                        event.getId().toString(),
                        Notification.builder()
                                .eventId(event.getId())
                                .message("Ticket added to the ticket pool for Event ID")
                                .timestamp(LocalDateTime.now())
                                .build()
                );

            } catch (InterruptedException | EventException e) {
                throw new RuntimeException(e);
            }
        }

        if (isRunning) {
            notificationService.sendNotification(
                    event.getId().toString(),
                    Notification.builder()
                            .eventId(event.getId())
                            .message("Ticket release completed for Event ID")
                            .timestamp(LocalDateTime.now())
                            .build()
            );
        }
    }

    public void stop() {
        isRunning = false;
        notificationService.sendNotification(
                event.getId().toString(),
                Notification.builder()
                        .eventId(event.getId())
                        .message("Ticket releasing stopped for Event ID")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
