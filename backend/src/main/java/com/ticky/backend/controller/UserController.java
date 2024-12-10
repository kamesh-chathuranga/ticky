package com.ticky.backend.controller;

import com.ticky.backend.dto.request.UserUpdateRequest;
import com.ticky.backend.dto.response.BaseResponse;
import com.ticky.backend.dto.response.Response;
import com.ticky.backend.entity.Event;
import com.ticky.backend.entity.Ticket;
import com.ticky.backend.exception.EventException;
import com.ticky.backend.service.EventService;
import com.ticky.backend.service.TicketService;
import com.ticky.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final EventService eventService;
    private final TicketService ticketService;

    @GetMapping()
    public ResponseEntity<Response> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            var user = userService.findUserFromJwtToken(token);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message("Failed to get user")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/events/{eventId}/buy")
    public ResponseEntity<Response> buyTickets(@PathVariable Long eventId,
                                               @RequestHeader("Authorization") String token) {
        try {
            var user = userService.findUserFromJwtToken(token);
            Event event = eventService.findEventById(eventId);

            if (event.getAvailableTickets() <= 0) {
                var errorResponse = BaseResponse.builder()
                        .isSuccess(false)
                        .message("No tickets available for event ID " + eventId)
                        .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            userService.purchaseTicket(event.getId(), user);

            var successResponse = BaseResponse.builder()
                    .isSuccess(true)
                    .message("Ticket purchased successfully")
                    .build();
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (EventException e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message("Failed to buy ticket")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateCurrentUser(@RequestBody UserUpdateRequest userUpdateRequest,
                                                      @RequestHeader("Authorization") String token) {
        try {
            var user = userService.findUserFromJwtToken(token);
            var updatedUser = userService.updateUserDetails(userUpdateRequest, user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message("Failed to update user")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tickets")
    public ResponseEntity<? super Response> getAllSoldTicketsByCustomerId(@RequestHeader("Authorization") String token) {
        try {
            var user = userService.findUserFromJwtToken(token);
            List<Ticket> tickets = ticketService.getAllSoldTicketsByCustomerId(user.getId());
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (Exception e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message("Failed to get all sold tickets")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
