package com.ticky.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ticky.threading.Customer;
import com.ticky.threading.TicketPool;
import com.ticky.threading.Vendor;
import com.ticky.util.ANSIColors;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Scanner;

public class TicketManagementSystem {
    private final Scanner scanner;
    private int maxTicketCapacity;
    private int totalTickets;
    private Long ticketReleaseRate;
    private Long customerRetrievalRate;

    public TicketManagementSystem() {
        this.scanner = new Scanner(System.in);
    }

    private static void errorMessagePrinter(String message) {
        System.out.println(ANSIColors.BRIGHT_RED + "\nError: " + message + ANSIColors.RESET);
    }

    private static void successMessagePrinter(String message) {
        System.out.println(ANSIColors.BRIGHT_GREEN + "\nSuccess: " + message + ANSIColors.RESET);
    }

    private int getConfigInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("Q")) {
                return -1;
            }

            if (input.isBlank()) {
                errorMessagePrinter("Input cannot be blank.\n");
            } else if (!input.matches("^[0-9]+")) {
                errorMessagePrinter("Input must be a number.\n");

            } else if (Integer.parseInt(input) <= 0) {
                errorMessagePrinter("Input must be greater than 0.\n");
            } else {
                return Integer.parseInt(input);
            }
        }
    }


    private boolean createNewConfiguration() {
        System.out.println(ANSIColors.BRIGHT_GREEN + "\n>>  Creating a new configuration..." + ANSIColors.RESET);
        System.out.println(ANSIColors.BRIGHT_BLUE + "\tEnter 'Q' to back\n" + ANSIColors.RESET);

        int maxTicketCapacity = getConfigInput("Enter the Max Ticket Capacity: ");

        if (maxTicketCapacity == -1) {
            return false;
        }

        int totalTickets = getConfigInput("Enter the Total Number of Tickets: ");

        if (totalTickets == -1) {
            return false;
        }

        if (totalTickets > maxTicketCapacity) {
            errorMessagePrinter("Total tickets cannot be greater than the maximum ticket capacity.\n");
            return false;
        }

        long ticketReleaseRate = getConfigInput("Enter the Ticket Release Rate in Milliseconds: ");

        if (ticketReleaseRate == -1) {
            return false;
        }

        long customerRetrievalRate = getConfigInput("Enter the Customer Retrieval Rate in Milliseconds: ");

        if (customerRetrievalRate == -1) {
            return false;
        }

        JsonObject config = new JsonObject();
        config.addProperty("maxTicketCapacity", maxTicketCapacity);
        config.addProperty("totalTickets", totalTickets);
        config.addProperty("ticketReleaseRate", ticketReleaseRate);
        config.addProperty("customerRetrievalRate", customerRetrievalRate);

        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;

        try {
            Path projectFolder = Paths.get(System.getProperty("user.dir"), "src", "config");
            Path configFile = projectFolder.resolve("config.json");

            if (!Files.exists(projectFolder)) {
                Files.createDirectories(projectFolder);
            }

            try (FileWriter file = new FileWriter(configFile.toFile())) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(config, file);
                successMessagePrinter("Configuration saved to config.json\n");
                return true;
            }
        } catch (IOException e) {
            errorMessagePrinter("Failed to save configuration to file.\n");
        }
        return false;
    }

    private void startTicketSystem() {
        int numberOfVendors = getConfigInput("\nEnter the Number of Vendors: ");
        if (numberOfVendors == -1) {
            return;
        }

        int numberOfCustomers = getConfigInput("Enter the Number of Customers: ");
        if (numberOfCustomers == -1) {
            return;
        }

        TicketPool ticketPool = new TicketPool(
                this.maxTicketCapacity,
                this.ticketReleaseRate,
                this.customerRetrievalRate
        );

        for (int i = 0; i < numberOfVendors; i++) {
            Vendor vendor = new Vendor(ticketPool, i + 1, this.totalTickets);
            Thread vendorThread = new Thread(vendor);
            vendorThread.start();
        }

        for (int i = 0; i < numberOfCustomers; i++) {
            Customer customer = new Customer(ticketPool, i + 1);
            Thread customerThread = new Thread(customer);
            customerThread.start();
        }
    }

    private void ticketSystemStartHandler() {
        System.out.println(ANSIColors.BRIGHT_GREEN + ">>  Starting Ticket System..." + ANSIColors.RESET);
        Scanner scanner = new Scanner(System.in);

        int option = 0;
        String leftAlignFormat = "| %-6d | %-27s |%n";
        String[] description = {"Start Ticket Release", "Stop Ticket Release"};

        while (option != 2) {
            System.out.println("+--------+-----------------------------+");
            System.out.println("| Option | Description                 |");
            System.out.println("+--------+-----------------------------+");

            for (int i = 0; i < description.length; i++) {
                System.out.format(leftAlignFormat, i + 1, description[i]);
            }

            System.out.println("+--------+-----------------------------+\n");
            System.out.print("Please enter your choice: ");

            try {
                option = Integer.parseInt(scanner.nextLine().trim());

                switch (option) {
                    case 1 -> this.startTicketSystem();
                    case 2 -> this.exitProgram();
                    default -> errorMessagePrinter("Invalid choice please try again :(\n");
                }
            } catch (NumberFormatException e) {
                errorMessagePrinter("Please enter a valid choice!\n");
            } catch (Exception e) {
                errorMessagePrinter("Something went wrong!\n" + e.getMessage());
            }
        }
    }


    private void configureTicketSystem() {
        boolean isConfigured = this.createNewConfiguration();
        if (isConfigured) {
            this.ticketSystemStartHandler();
        }
    }

    private void loadPreviousConfiguration() {
        System.out.println(ANSIColors.BRIGHT_GREEN + "\n>>  Loading previous configuration..." + ANSIColors.RESET);

        try {
            Path configFile = Paths.get(System.getProperty("user.dir"), "src", "config", "config.json");
            String config = Files.readString(configFile);

            JsonObject jsonObject = new Gson().fromJson(config, JsonObject.class);
            this.maxTicketCapacity = jsonObject.get("maxTicketCapacity").getAsInt();
            this.totalTickets = jsonObject.get("totalTickets").getAsInt();
            this.ticketReleaseRate = jsonObject.get("ticketReleaseRate").getAsLong();
            this.customerRetrievalRate = jsonObject.get("customerRetrievalRate").getAsLong();

            successMessagePrinter("Success: Configuration loaded successfully!\n");
            this.ticketSystemStartHandler();
        } catch (Exception e) {
            errorMessagePrinter("Failed to load configuration from file.\n");
        }
    }

    private void exitProgram() {
        System.out.println(ANSIColors.BRIGHT_GREEN + "\n>>  Exiting program..." + ANSIColors.RESET);
        System.exit(0);
    }

    public void mainMenuHandler() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=============================");
        System.out.println(ANSIColors.BRIGHT_MAGENTA + "| Welcome to the Ticky " + Calendar.getInstance().get(Calendar.YEAR) + " |" + ANSIColors.RESET);
        System.out.println("=============================\n");

        int option = 0;
        String leftAlignFormat = "| %-6d | %-27s |%n";
        String[] description = {"Create New Configuration", "Load Previous Configuration", "Exit Program"};

        while (option != 3) {
            System.out.println("+--------+-----------------------------+");
            System.out.println("| Option | Description                 |");
            System.out.println("+--------+-----------------------------+");

            for (int i = 0; i < description.length; i++) {
                System.out.format(leftAlignFormat, i + 1, description[i]);
            }

            System.out.println("+--------+-----------------------------+\n");
            System.out.print("Please enter your choice: ");

            try {
                option = Integer.parseInt(scanner.nextLine().trim());

                switch (option) {
                    case 1 -> this.configureTicketSystem();
                    case 2 -> this.loadPreviousConfiguration();
                    case 3 -> this.exitProgram();
                    default -> errorMessagePrinter("Invalid choice please try again :(\n");
                }
            } catch (NumberFormatException e) {
                errorMessagePrinter("Please enter a valid choice!\n");
            } catch (Exception e) {
                errorMessagePrinter("Something went wrong!\n" + e.getMessage());
            }
        }
    }
}
