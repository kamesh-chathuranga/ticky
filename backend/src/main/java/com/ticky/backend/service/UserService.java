package com.ticky.backend.service;

import com.ticky.backend.config.JwtService;
import com.ticky.backend.dto.request.UserUpdateRequest;
import com.ticky.backend.entity.Event;
import com.ticky.backend.entity.User;
import com.ticky.backend.exception.EventException;
import com.ticky.backend.notification.NotificationService;
import com.ticky.backend.repository.EventRepository;
import com.ticky.backend.repository.UserRepository;
import com.ticky.backend.threading.Customer;
import com.ticky.backend.threading.Vendor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final EventService eventService;
    private final TicketService ticketService;
    private final UserRepository userRepository;
    private final TicketPoolService ticketPoolService;
    private final NotificationService notificationService;
    private final EventRepository eventRepository;
    private final Map<Long, Vendor> vendorThreads = new ConcurrentHashMap<>();

    public User findUserFromJwtToken(String token) {
        token = token.substring(7);
        String userName = jwtService.getUserNameFromToken(token);
        return findUserFromEmail(userName);
    }

    public User findUserFromEmail(String email) {
        var user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Unauthorized");
        }

        return user.get();
    }

    public void releaseTicket(Event event, int totalTickets) throws EventException {
        var vendor = Vendor.builder()
                .event(event)
                .totalTickets(totalTickets)
                .ticketService(ticketService)
                .ticketPoolService(ticketPoolService)
                .notificationService(notificationService)
                .eventRepository(eventRepository)
                .isRunning(true)
                .build();

        Thread vendorThread = new Thread(vendor);
        vendorThreads.put(event.getId(), vendor);
        vendorThread.start();
    }

    public void stopTicketReleasing(Long eventId) {
        Vendor vendorThread = vendorThreads.get(eventId);
        if (vendorThread != null) {
            vendorThread.stop();
            vendorThreads.remove(eventId);
        }
    }

    public void purchaseTicket(Long eventId, User user) throws RuntimeException {
        var customer = Customer.builder()
                .customer(user)
                .eventId(eventId)
                .ticketService(ticketService)
                .ticketPoolService(ticketPoolService)
                .notificationService(notificationService)
                .eventService(eventService)
                .build();

        Thread customerThread = new Thread(customer);
        customerThread.start();
    }

    public User updateUserDetails(UserUpdateRequest updatedUser, User user) {
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setContactNumber(updatedUser.getContactNumber());
        return userRepository.save(user);
    }
}
