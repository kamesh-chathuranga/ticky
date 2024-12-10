package com.ticky.backend.controller;

import com.ticky.backend.dto.response.BaseResponse;
import com.ticky.backend.dto.response.Response;
import com.ticky.backend.entity.Event;
import com.ticky.backend.exception.EventException;
import com.ticky.backend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<? super Response> getAllEvents() {
        try {
            List<Event> events = eventService.getAllEvents();
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
                    .message("Failed to get all events")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Response> getEventById(@PathVariable Long eventId) {
        try {
            Event event = eventService.findEventById(eventId);
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
                    .message("Failed to get the event")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
