package com.ticky.backend.dto.request;

import com.ticky.backend.enums.EventCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCreationRequest {
    private String name;

    private String description;

    private double ticketPrice;

    private int totalTickets;

    private String location;

    private LocalDateTime eventDate;

    private EventCategory category;

}
