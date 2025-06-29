
# SplitBuddy Backend

A microservices-based backend application for SplitBuddy, a group expense splitting platform.  
This backend consists of two Spring Boot microservices:

- **Group Service:** Manages groups and users.  
- **Settlement Service:** Handles expenses, calculates user balances, and provides settlement summaries.

---

## Table of Contents

- [Overview](#overview)  
- [Features](#features)  
- [Architecture](#architecture)  
- [Technologies Used](#technologies-used)  
- [Setup & Installation](#setup--installation)  
- [How to Run](#how-to-run)  
- [API Endpoints](#api-endpoints)  
- [Swagger Documentation](#swagger-documentation)  
- [Future Improvements](#future-improvements)  
- [Author](#author)

---

## Overview

SplitBuddy helps groups track shared expenses, calculate each member’s fair share, and determine who owes whom.  
The backend is designed as two microservices communicating via REST:

- **Group Service:** Manages users and groups.  
- **Settlement Service:** Manages expenses, calculates totals, and computes settlements.

---

## Features

- Create, update, and manage groups and users (Group Service).  
- Record expenses paid by group members (Settlement Service).  
- Calculate fair share and balances for each user.  
- Provide detailed user expense summaries.  
- Debt calculation logic to resolve who owes whom.  
- Integration between microservices using REST clients.  
- Swagger API documentation for easy exploration.

---

## Architecture

```plaintext
+------------------+          REST          +---------------------+
|  Group Service   | <--------------------> | Settlement Service  |
| (User & Group)   |                        | (Expenses & Settlements) |
+------------------+                        +---------------------+
````

Each service is an independent Spring Boot application.

* Group Service runs on port 9090.
* Settlement Service runs on port 8082.
* Uses Spring Data JPA for persistence.
* RESTTemplate used for inter-service communication.

---

## Technologies Used

* Java 17
* Spring Boot
* Spring Data JPA
* Spring Security (Basic config)
* Swagger/OpenAPI for API docs
* Maven for build management
* H2 (or your choice) database for development
* Git for version control

---

## Setup & Installation

Clone the repo:

```bash
git clone https://github.com/mnaaaav/split-buddy-backend.git
cd split-buddy-backend
```

Build each service separately using Maven:

```bash
cd group-service/group-service
mvn clean install
cd ../../settlement-service
mvn clean install
```

Configure your database settings in each service's `application.properties`.

---

## How to Run

Run Group Service:

```bash
cd group-service/group-service
mvn spring-boot:run
```

Run Settlement Service:

```bash
cd settlement-service
mvn spring-boot:run
```

Access Swagger UI for each service:

* Group Service: [http://localhost:9090/swagger-ui/index.html](http://localhost:9090/swagger-ui/index.html)
* Settlement Service: [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)

---

## API Endpoints

**Group Service**

* `/groups` - CRUD for groups
* `/users` - CRUD for users
* `/groups/{id}/users` - Get users in a group

**Settlement Service**

* `/expenses` - Manage expenses
* `/settlements/group/{groupId}/summary` - Get settlement summary for a group
* `/settlements/group/{groupId}/debts` - Calculate debts between users

---

## Swagger Documentation

Both microservices include Swagger UI for API testing and documentation. Visit:

* Group Service: [http://localhost:9090/swagger-ui/index.html](http://localhost:9090/swagger-ui/index.html)
* Settlement Service: [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)

---

## Future Improvements

* Add authentication and authorization with JWT.
* Frontend integration for user-friendly UI.
* Support for multiple currencies.
* Notification system for reminders.
* More comprehensive unit and integration tests.

---

## Author

Manav Goel
Email: [desktop14103@gmail.com](mailto:desktop14103@gmail.com)
GitHub: [mnaaaav](https://github.com/mnaaaav)


