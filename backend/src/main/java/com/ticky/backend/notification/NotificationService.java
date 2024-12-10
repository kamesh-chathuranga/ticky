package com.ticky.backend.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendNotification(String eventId, Notification notification) {
        simpMessagingTemplate.convertAndSendToUser(eventId, "/notification", notification);
    }

    public void sendAvailableTickets(String eventId, int availableTickets) {
        simpMessagingTemplate.convertAndSendToUser(eventId, "/availableTickets", availableTickets);
    }
}
