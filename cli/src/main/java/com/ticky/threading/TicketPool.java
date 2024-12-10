package com.ticky.threading;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    private final Queue<Ticket> ticketQueue;
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    private final int MAX_TICKET_CAPACITY;
    private final Long TICKET_RELEASE_RATE;
    private final Long CUSTOMER_RETRIEVAL_RATE;

    public TicketPool(int maxTicketCapacity, Long ticketReleaseRate, Long customerRetrievalRate) {
        this.ticketQueue = new ConcurrentLinkedQueue<>();
        this.MAX_TICKET_CAPACITY = maxTicketCapacity;
        this.TICKET_RELEASE_RATE = ticketReleaseRate;
        this.CUSTOMER_RETRIEVAL_RATE = customerRetrievalRate;
    }

    public void addTicket(Ticket ticket, int vendorId) throws InterruptedException {
        lock.lock();
        try {
            while (ticketQueue.size() == MAX_TICKET_CAPACITY) {
                System.out.println("\nMax ticket capacity reached, Vendor " + vendorId + " waiting for ticket to be consumed");
                notFull.await();
            }
            Thread.sleep(TICKET_RELEASE_RATE);
            ticketQueue.add(ticket);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public Ticket removeTicket(int customerId) throws InterruptedException {
        lock.lock();
        try {
            while (ticketQueue.isEmpty()) {
                System.out.println("\nTicket pool is empty, Customer " + customerId + " waiting for ticket to be added");
                notEmpty.await();
            }
            Thread.sleep(CUSTOMER_RETRIEVAL_RATE);
            Ticket ticket = ticketQueue.remove();
            notFull.signalAll();
            return ticket;
        } finally {
            lock.unlock();
        }
    }
}
