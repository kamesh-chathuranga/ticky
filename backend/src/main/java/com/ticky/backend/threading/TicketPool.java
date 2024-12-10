package com.ticky.backend.threading;

import com.ticky.backend.dto.request.TicketPoolConfig;
import com.ticky.backend.entity.Ticket;
import com.ticky.backend.exception.TicketPoolException;
import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    @Getter
    private static TicketPool ticketPool;
    private final Queue<Ticket> ticketQueue;
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    private final int MAX_TICKET_CAPACITY;
    private final Long TICKET_RELEASE_RATE;
    private final Long CUSTOMER_RETRIEVAL_RATE;

    private TicketPool(int maxTicketCapacity, Long ticketReleaseRate, Long customerRetrievalRate) {
        this.ticketQueue = new ConcurrentLinkedQueue<>();
        this.MAX_TICKET_CAPACITY = maxTicketCapacity;
        this.TICKET_RELEASE_RATE = ticketReleaseRate;
        this.CUSTOMER_RETRIEVAL_RATE = customerRetrievalRate;
    }

    public static void createTicketPool(TicketPoolConfig ticketPoolConfig) throws TicketPoolException {
        if (ticketPool != null) {
            throw new TicketPoolException("Ticket pool already exists");
        }
        TicketPool.ticketPool = new TicketPool(ticketPoolConfig.getMaxTicketCapacity(), ticketPoolConfig.getTicketReleaseRate(), ticketPoolConfig.getCustomerRetrievalRate());
    }

    public void addTicket(Ticket ticket) throws InterruptedException {
        lock.lock();
        try {
            while (ticketQueue.size() == MAX_TICKET_CAPACITY) {
                System.out.println("Max ticket capacity reached, waiting for ticket to be consumed");
                notFull.await();
            }
            Thread.sleep(TICKET_RELEASE_RATE);
            ticketQueue.add(ticket);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public Ticket removeTicket(Long eventId) throws InterruptedException {
        lock.lock();
        try {
            while (ticketQueue.isEmpty()) {
                System.out.println("Ticket pool is empty, waiting for ticket to be added");
                notEmpty.await();
            }
            Thread.sleep(CUSTOMER_RETRIEVAL_RATE);

            Ticket removedTicket = ticketQueue.stream()
                    .filter(ticket -> Objects.equals(ticket.getEvent().getId(), eventId))
                    .findFirst()
                    .orElse(null);

            if (removedTicket != null) {
                ticketQueue.remove(removedTicket);
                notFull.signalAll();
                return removedTicket;
            }
            return null;
        } finally {
            lock.unlock();
        }
    }
}
