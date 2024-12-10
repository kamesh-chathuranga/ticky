package com.ticky.backend.controller;

import com.ticky.backend.dto.response.BaseResponse;
import com.ticky.backend.dto.response.Response;
import com.ticky.backend.dto.response.TicketSaleHistory;
import com.ticky.backend.service.TicketService;
import com.ticky.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final UserService userService;

    @GetMapping("/sales/{year}")
    public ResponseEntity<? super Response> getTicketSales(@PathVariable int year,
                                                           @RequestHeader("Authorization") String token) {
        try {
            var vendor = userService.findUserFromJwtToken(token);
            List<TicketSaleHistory> sales = ticketService.getTicketSalesForYear(year, vendor.getId());
            return new ResponseEntity<>(sales, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message("Failed to get event sales history")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
