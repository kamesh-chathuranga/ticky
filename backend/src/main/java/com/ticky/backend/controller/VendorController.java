package com.ticky.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticky.backend.dto.request.EventCreationRequest;
import com.ticky.backend.dto.response.BaseResponse;
import com.ticky.backend.dto.response.Response;
import com.ticky.backend.entity.Event;
import com.ticky.backend.entity.User;
import com.ticky.backend.exception.EventException;
import com.ticky.backend.exception.ImageUploadException;
import com.ticky.backend.service.EventService;
import com.ticky.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vendor")
public class VendorController {
    private final UserService userService;
    private final EventService eventService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/new-event", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> createNewEvent(@RequestPart("event") String event,
                                                   @RequestPart("image") MultipartFile image,
                                                   @RequestHeader("Authorization") String token) {
        try {
            var vendor = userService.findUserFromJwtToken(token);
            EventCreationRequest eventCreationRequest = objectMapper.readValue(event, EventCreationRequest.class);
            Event createdEvent = eventService.createNewEvent(eventCreationRequest, image, vendor);
            return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
        } catch (UsernameNotFoundException | ImageUploadException e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message("Failed to create a new event")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/events")
    public ResponseEntity<? super Response> getAllEventsByVendorId(@RequestHeader("Authorization") String token) {
        try {
            User vendor = userService.findUserFromJwtToken(token);
            List<Event> events = eventService.getAllEventsByVendorId(vendor.getId());
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (EventException e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message("Failed to get events")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<Response> deleteEventById(@PathVariable Long eventId,
                                                    @RequestHeader("Authorization") String token) {
        try {
            var vendor = userService.findUserFromJwtToken(token);
            eventService.deleteEventById(eventId, vendor.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EventException e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message("Failed to delete event")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<Response> updateEventById(@RequestBody EventCreationRequest updatedEvent,
                                                    @PathVariable Long eventId,
                                                    @RequestHeader("Authorization") String token) {
        try {
            var vendor = userService.findUserFromJwtToken(token);
            Event event = eventService.updateEvent(updatedEvent, eventId, vendor.getId());
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (EventException e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message("Failed to update event")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<Response> releaseTickets(@PathVariable Long eventId,
                                                   @RequestParam boolean release,
                                                   @RequestHeader("Authorization") String token) {
        try {
            var vendor = userService.findUserFromJwtToken(token);
            Event event = eventService.findEventById(eventId);

            if (!event.getVendor().getId().equals(vendor.getId())) {
                var errorResponse = BaseResponse.builder()
                        .isSuccess(false)
                        .message("Event not found with id: " + eventId)
                        .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            if (release) {
                userService.releaseTicket(event, event.getTotalTickets());
                var successResponse = BaseResponse.builder()
                        .isSuccess(true)
                        .message("Ticket released successfully")
                        .build();
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            } else {
                userService.stopTicketReleasing(eventId);
                var successResponse = BaseResponse.builder()
                        .isSuccess(true)
                        .message("Ticket release stopped")
                        .build();
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            }
        } catch (EventException e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message("Failed to release ticket")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
