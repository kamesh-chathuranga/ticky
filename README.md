![home-page](https://github.com/user-attachments/assets/fb989e83-3a85-4318-af4e-42b648450dec)

---

# Ticky 

This project implements a comprehensive and high-performance **Real-Time Ticketing System** designed to handle high volumes of concurrent ticket sales efficiently. 
It leverages the power of Angular to deliver a dynamic and user-friendly frontend experience, while employing Spring Boot to provide a robust and scalable backend infrastructure. 
This combination ensures a seamless and efficient ticketing experience for both vendors and customers.

---

## Tech Stack

### Frontend
- Built with **Angular**.
- Uses **WebSocket** for real-time updates.
- Implements **Chart.js** for dynamic data visualization.

### Backend
- Developed with **Spring Boot**.
- Implements multithreading using the `java.util.concurrent` package.
- Uses a **relational database** for persistence.

---

## Features

- **Admins**:
  - ✅ Configuration Management
    
- **Vendors**:
  - ✅ Event Creation and Management
  - ✅ Dedicated Dashboard
  - ✅ Real-Time Ticket Status
 
- **Customers**:
  - ✅ Intuitive Ticket Purchase
  - ✅ Real-Time Ticket Availability
  - ✅ Profile Management
  - ✅ Purchase History
  - ✅ User authentication and role-based access control

## Installation

### Prerequisites
- **Frontend**: Node.js, Angular CLI
- **Backend**: Java 17+, Spring Boot, Maven
- **Database**: PostgreSQL 
- **Other Tools**: WebSocket library

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/kamesh-chathuranga/ticky.git
   cd ticky
   ```

2. Set up the backend:
   - Navigate to the `backend` directory.
   - Configure the `application.properties` file for database connection.
   - Build and run the Spring Boot application:
     ```bash
     mvn clean install
     mvn spring-boot:run
     ```

3. Set up the frontend:
   - Navigate to the `frontend` directory.
   - Install dependencies:
     ```bash
     npm install
     ```
   - Start the Angular development server:
     ```bash
     ng serve
     ```

---

## Usage

1. **Start the Backend**:
   - Launch the Spring Boot application to initialize the API and ticketing logic.

2. **Launch the Frontend**:
   - Open the browser and navigate to `http://localhost:4200`.

3. **Configure Ticketing System**:
   - Use the configuration section to define ticket pool size.
   - Press the "Release Ticket" button to begin ticket sales.

4. **Monitor Live Updates**:
   - View real-time ticket sales, remaining tickets, and transaction logs.
   - Observe live charts for ticket sales visualization.

## Login Page
![Ticky - Google Chrome 12_11_2024 10_29_10 PM](https://github.com/user-attachments/assets/9f160a02-7a61-4f6a-9a03-49fe189eb514)

## Registration Page
![Ticky - Google Chrome 12_11_2024 10_29_23 PM](https://github.com/user-attachments/assets/984d852a-acb9-420c-a1dc-383516fd393f)

## Admin Configuration Page
![Ticky - Google Chrome 12_11_2024 10_31_12 PM](https://github.com/user-attachments/assets/097fc1a1-21be-4698-b4d8-01c8455c3c6a)

## Event Details Page
![Ticky - Google Chrome 12_11_2024 10_30_04 PM](https://github.com/user-attachments/assets/8e2d0a03-6188-4dcd-9c48-13b5ac61a2e7)

## User Profile Page
![Ticky - Google Chrome 12_11_2024 10_33_54 PM](https://github.com/user-attachments/assets/43096b54-25d1-4304-8630-4442542c5fd1)

## My Events Page
![Ticky - Google Chrome 12_11_2024 10_30_16 PM](https://github.com/user-attachments/assets/69593146-80be-4c1e-b113-e1f70e27938e)

## Ticket Status Page
![Ticky - Google Chrome 12_11_2024 10_33_32 PM](https://github.com/user-attachments/assets/68de8744-7988-4b06-9e58-77ca6cb25696)

---

## Main API Endpoints

| Method | Endpoint                          | Description                          |
|--------|-----------------------------------|--------------------------------------|
| POST   | `/api/admin/ticket-pool`          | Configure the Ticket Pool.           |
| GET    | `/api/events`                     | Get all events.                      |
| GET    | `/api/vendor/events/{eventId}`    | Release tickets for the event.       |
| GET    | `/api/user/event/{eventId}/buy`   | Buy ticket for the event.            |
| GET    | `/api/tickets/sales/{year}`       | Yearly ticket sales for the event.   |

---

## Future Enhancements
- Implement advanced analytics for ticket sales patterns.
- Add Provider user authentication (Google).
- Extend support for more complex ticketing scenarios (e.g., priority tickets).
