# BorgerInformation

## Vision

  The purpose of this project is to share events and incidents based on a persons location.
  Other people can be informed about what is happening in their neighborhood, their postalcode. 

---

## Links

Deployed application:
borgerhub.dk

Source code repository: 
https://github.com/olfatmir47-arch/Borgerinformation.git

Portfolio website: 
https://olfatmir47-arch.github.io/portfolio/
https://olfatmir47-arch.github.io/portfolio/reflections/
  
---

# Architecture

## System Overview

This project is built as a layered backend architecture using:

- Controller layer (REST endpoints)
- Service layer (business logic)
- DAO layer (Database communication)
- DTO layer (Data exchange)
- Entity layer

Technologies used:

- Java
- Javalin
- JPA / Hibernate
- PostgreSQL
- Maven
- JWT authentication
- RESTful APIs
- DAO Architecture
- DTO Pattern
- JSON
- BCrypt Password Hashing
- GitHub
- Docker
- pgAdmin

## Architecture Diagram

Client
   ↓
REST API (Javalin)
   ↓
Routes
   ↓
Controllers
   ↓
Services
   ↓
DAOs
   ↓
JPA / Hibernate
   ↓
PostgreSQL Database

## Key Design Decisions

Layered architecture
  - Making the project more manageable, and easier to maintain, in case of refactoring
Use of DTOs
  - Making sure the client dont receive more information or data than needed
Input Validation
  - Ensures that the service layer only receives valid and correctly formatted data.
JWT Authentication
  - JWT is implemented to make sure endpoints stay protected

---

# Data Model

## ERD
<img width="812" height="611" alt="ERDiagram" src="https://github.com/user-attachments/assets/2ef9bd77-12e1-478d-ad2c-4652c8463b8c" />

---

## Important Entities

### User
- firstName
- lastName
- birthDate
- email
- password
- role

### Address
- streetName
- number
- floor
- postalCode
- cityName

### Event
- nameOfOccurrence
- description
- startTime
- endTime
- address
- category
  
---

# API Documentation

## Example Endpoints

### Create event

POST /events
Request body:
{
  "name": "Fodboldturnering",
  "description": "Stor lokal turnering for alle aldre",
  "startTime": "20-10-2026 14:00",
  "endTime": "20-10-2026 18:00",
  "categoryId": 1,
  "address": {
    "streetName": "Nørrebrogade",
    "number": "45",
    "floor": "1",
    "postalCode": 2200,
    "cityName": "København"
  }
}

Response:
Response code: 200 (OK)

# User Stories

As a user 
I want to create an event with name, description, time and location 
So that others can see it on the map

As a user
I want to create an incident such as roadwork or closures 
So that citizens can stay informed and adjust their route

As a user 
I want to see all events and incidents displayed as markers on a map 
So that I can quickly understand what happens in my area

As a user 
I want to filter occurrences by category 
So that I only see the types of events or incidents relevant to me

As a new user 
I want to create an account with my personal details 
So that I can contribute events and incidents

As a user 
I want the app to convert addresses into coordinates automatically 
So that my event or incident is placed correctly on the map

As a admin
I want to be able to delete user or occurences
So that unused accounts or finished events doesnt take up space

# Development Notes

- Importance and benefits of dependency injection
- JWT authentication
- User stories are useful when stuck or lost
- Use more DTOs

