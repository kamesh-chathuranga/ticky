package com.ticky.threading;

import com.ticky.util.ANSIColors;

import java.time.LocalDateTime;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int vendorId;
    private final int totalTickets;

    public Vendor(TicketPool ticketPool, int vendorId, int totalTickets) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
        this.totalTickets = totalTickets;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < totalTickets; i++) {
                Ticket ticket = new Ticket();
                ticketPool.addTicket(ticket, vendorId);
                System.out.println(ANSIColors.BRIGHT_BLUE +
                        "\nTicket added to the ticket pool by Vendor " + vendorId + " at " + LocalDateTime.now() +
                        ANSIColors.RESET);
            }

        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }

        System.out.println(ANSIColors.BRIGHT_YELLOW +
                        "\nVendor " + vendorId + " has added all tickets to the ticket pool" +
                ANSIColors.RESET);
    }
}
