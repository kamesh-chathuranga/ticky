package com.ticky.backend.controller;

import com.ticky.backend.dto.request.TicketPoolConfig;
import com.ticky.backend.dto.response.BaseResponse;
import com.ticky.backend.dto.response.Response;
import com.ticky.backend.exception.TicketPoolException;
import com.ticky.backend.service.TicketPoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final TicketPoolService ticketPoolService;

    @PostMapping(value = "/ticket-pool")
    public ResponseEntity<Response> configureTicketPool(@RequestBody TicketPoolConfig ticketPoolConfig
    ) {
        try {
            ticketPoolService.createTicketPool(ticketPoolConfig);
            var successResponse = BaseResponse.builder()
                    .isSuccess(true)
                    .message("Ticket Pool configured successfully")
                    .build();
            return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
        } catch (TicketPoolException e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        } catch (Exception e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message("Failed to configure the Ticket Pool")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
