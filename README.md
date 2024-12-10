![home-page](https://github.com/user-attachments/assets/fb989e83-3a85-4318-af4e-42b648450dec)

---

# Ticky

This project implements a comprehensive and high-performance **Real-Time Ticketing System** designed to handle high volumes of concurrent ticket sales efficiently. 
It leverages the power of Angular to deliver a dynamic and user-friendly frontend experience, while employing Spring Boot to provide a robust and scalable backend infrastructure. 
This combination ensures a seamless and efficient ticketing experience for both vendors and customers.

## Features

- **Admins**:
  - Configuration Management
    
- **Vendors**:
  - Event Creation and Management
  - Dedicated Dashboard
  - Real-Time Ticket Status
 
- **Customers**:
  - Intuitive Ticket Purchase
  - Real-Time Ticket Availability
  - Profile Management
  - Purchase History

## Installation

### Prerequisites
- **Frontend**: Node.js, Angular CLI
- **Backend**: Java 17+, Spring Boot, Maven
- **Database**: PostgreSQL 
- **Other Tools**: WebSocket library

### Steps
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd real-time-ticketing-system
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
   - Use the configuration section to define ticket pool size, vendor/customer count, etc.
   - Press the "Start" button to begin ticket sales.

4. **Monitor Live Updates**:
   - View real-time ticket sales, remaining tickets, and transaction logs.
   - Observe live charts for ticket sales visualization.

---

## API Endpoints

| Method | Endpoint               | Description                          |
|--------|------------------------|--------------------------------------|
| GET    | `/api/tickets`         | Fetches current ticket status.       |
| POST   | `/api/config`          | Updates ticketing system settings.   |
| GET    | `/api/sales/visualize` | Returns data for real-time charts.   |
| POST   | `/api/start`           | Starts the ticketing process.        |
| POST   | `/api/stop`            | Stops the ticketing process.         |

---

## Architecture

### Frontend
- Built with **Angular**.
- Uses **WebSocket** for real-time updates.
- Implements **Chart.js** for dynamic data visualization.

### Backend
- Developed with **Spring Boot**.
- Implements multithreading using the `java.util.concurrent` package.
- Uses a **relational database** for persistence.

---

## Future Enhancements
- Implement advanced analytics for ticket sales patterns.
- Add user authentication and role-based access control.
- Extend support for more complex ticketing scenarios (e.g., priority tickets, refunds).

---

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

---

Let me know if you need any changes or additional sections!
