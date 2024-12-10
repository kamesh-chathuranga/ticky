package com.ticky.backend.service;

import com.ticky.backend.dto.request.TicketPoolConfig;
import com.ticky.backend.exception.TicketPoolException;
import com.ticky.backend.threading.TicketPool;
import org.springframework.stereotype.Service;

@Service
public class TicketPoolService {

    public void createTicketPool(TicketPoolConfig ticketPoolConfig) throws TicketPoolException {
        TicketPool.createTicketPool(ticketPoolConfig);
        System.out.println(ticketPoolConfig);
    }

    public TicketPool getTicketPool() {
        return TicketPool.getTicketPool();
    }
}
