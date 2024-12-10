package com.ticky.threading;

import com.ticky.util.ANSIColors;

import java.time.LocalDateTime;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int customerId;

    public Customer(TicketPool ticketPool, int customerId) {
        this.ticketPool = ticketPool;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        try {
            var ticket = ticketPool.removeTicket(customerId);

            if (ticket != null) {
                System.out.println(ANSIColors.BRIGHT_GREEN +
                        "\nTicket brought by Customer " + customerId + " at " + LocalDateTime.now() +
                        ANSIColors.RESET);
            } else {
                System.out.println("No tickets available");
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }

    }
}
