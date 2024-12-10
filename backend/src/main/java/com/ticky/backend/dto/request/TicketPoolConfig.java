package com.ticky.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketPoolConfig {
    private int maxTicketCapacity;
    private Long ticketReleaseRate;
    private Long customerRetrievalRate;
}
