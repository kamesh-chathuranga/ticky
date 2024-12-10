package com.ticky.backend.entity;

import com.ticky.backend.dto.response.Response;
import com.ticky.backend.enums.EventCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Response {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private double ticketPrice;

    private int totalTickets;

    private int availableTickets;

    private String imageUrl;

    @ManyToOne
    private User vendor;

    private String location;

    private LocalDateTime eventDate;

    @Enumerated(EnumType.STRING)
    private EventCategory category;

}
