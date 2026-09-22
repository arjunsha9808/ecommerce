# E-Commerce Backend

A secure and scalable REST API for an e-commerce application built using Java, Spring Boot, Spring Security, JWT, JPA, Hibernate, and MySQL.

## Author

*Arjun Sharma*


---

## Project Overview

This project is a backend application for an e-commerce platform.

It provides REST APIs for user authentication, product management, shopping cart, orders, checkout, and payments.

The application uses JWT-based authentication and role-based authorization to protect APIs.

---

## Features

- User Registration
- User Login
- BCrypt Password Encryption
- JWT Authentication
- ADMIN and USER Role-Based Authorization
- Product CRUD Operations
- Shopping Cart Management
- Add Products to Cart
- Update Cart Quantity
- Remove Products from Cart
- Cart Total Calculation
- Order Management
- Checkout System
- Automatic Stock Reduction
- Payment Processing
- Payment Amount Validation
- Automatic Order Status Update to PAID
- Global Exception Handling
- RESTful APIs
- MySQL Database Integration

---

## Technology Stack

- Java 17
- Spring Boot 3.5.6
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Postman
- IntelliJ IDEA

---

## Project Structure

```text
ecommerce
│
├── src
│   └── main
│       ├── java
│       │   └── com.ecommerce.ecommerce
│       │       ├── controller
│       │       ├── dto
│       │       ├── entity
│       │       ├── exception
│       │       ├── repository
│       │       ├── security
│       │       └── service
│       │
│       └── resources
│           ├── application.properties
│           └── application-example.properties
│
├── .gitignore
├── README.md
└── pom.xml