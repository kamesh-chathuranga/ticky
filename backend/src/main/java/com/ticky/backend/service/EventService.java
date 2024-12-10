package com.ticky.backend.service;

import com.ticky.backend.dto.request.EventCreationRequest;
import com.ticky.backend.entity.Event;
import com.ticky.backend.entity.User;
import com.ticky.backend.exception.EventException;
import com.ticky.backend.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final CloudinaryService cloudinaryService;

    public Event createNewEvent(EventCreationRequest eventCreationRequest, MultipartFile image, User vendor) {
        String imageUrl = cloudinaryService.uploadImage(image);
        var event = Event.builder()
                .name(eventCreationRequest.getName())
                .description(eventCreationRequest.getDescription())
                .ticketPrice(eventCreationRequest.getTicketPrice())
                .totalTickets(eventCreationRequest.getTotalTickets())
                .vendor(vendor)
                .location(eventCreationRequest.getLocation())
                .eventDate(eventCreationRequest.getEventDate())
                .category(eventCreationRequest.getCategory())
                .imageUrl(imageUrl)
                .availableTickets(0)
                .build();

        return eventRepository.save(event);
    }

    public Event findEventById(Long eventId) throws EventException {
        var event = eventRepository.findById(eventId);

        if (event.isEmpty()) {
            throw new EventException("Event not found with id: " + eventId);
        }
        return event.get();
    }

    public void deleteEventById(Long eventId, Long vendorId) throws EventException {
        Event event = findEventById(eventId);
        if (!Objects.equals(event.getVendor().getId(), vendorId)) {
            throw new EventException("Event not found with id: " + eventId);
        }
        eventRepository.delete(event);
    }

    public Event updateEvent(EventCreationRequest updatedEvent, Long eventId, Long vendorId) throws EventException {
        Event event = findEventById(eventId);

        if (!event.getVendor().getId().equals(vendorId)) {
            throw new EventException("Event not found with id: " + eventId);
        }
        BeanUtils.copyProperties(updatedEvent, event, "id");
        return eventRepository.save(event);
    }

    public int updateEventAvailableTicket(Long eventId) throws EventException {
        Event event = findEventById(eventId);

        if (event.getAvailableTickets() <= 0) {
            throw new EventException("No tickets available for the event with id: " + eventId);
        }
        event.setAvailableTickets(event.getAvailableTickets() - 1);
        Event updatedEvent = eventRepository.save(event);
        return updatedEvent.getAvailableTickets();
    }

    public List<Event> getAllEventsByVendorId(Long vendorId) throws EventException {
        List<Event> events = eventRepository.findAllByVendorId(vendorId);

        if (events.isEmpty()) {
            throw new EventException("No events found for vendor with id: " + vendorId);
        }
        return events;
    }

    public List<Event> getAllEvents() throws EventException {
        List<Event> events = eventRepository.findAll();

        if (events.isEmpty()) {
            throw new EventException("No events found");
        }
        return events;
    }

}
