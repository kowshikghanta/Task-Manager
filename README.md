# 🧠 Task Manager Backend

A RESTful backend application built using Spring Boot that allows users to manage tasks with proper structure, validation, and scalable architecture.

---

## 🚀 Features Implemented

### 👤 User Management

* Create users via REST API
* Email uniqueness validation
* DTO-based request/response handling
* Clean separation between API and database models

---

### 📋 Task Management

* Create tasks for specific users
* Fetch tasks by user
* Update task status (PENDING → IN_PROGRESS → COMPLETED)
* Delete tasks

---

### 🔗 Relationships

* One-to-Many relationship:

    * One User → Multiple Tasks
* Implemented using JPA (`@ManyToOne`)

---

### 🧱 Architecture

* Layered structure:

    * Controller → Service → Repository
* DTO-based API design
* Clean separation of concerns

---

### ✅ Validation & Error Handling

* Input validation using `@Valid`
* Custom validation messages
* Global exception handling (`@ControllerAdvice`)
* Proper HTTP status codes:

    * `400` → Validation errors
    * `409` → Business conflicts
    * `204` → Successful deletion

---

### 🔄 Task Lifecycle

* Enum-based status management:

    * `PENDING`
    * `IN_PROGRESS`
    * `COMPLETED`

---

## 🛠️ Tech Stack

* Java
* Spring Boot
* Spring Data JPA
* MySQL
* Maven
* REST APIs

---

## 📡 API Endpoints

### Users

* `POST /api/users` → Create user
* `GET /api/users` → Get all users

---

### Tasks

* `POST /api/users/{userId}/tasks` → Create task
* `GET /api/users/{userId}/tasks` → Get tasks for user
* `PATCH /api/users/{userId}/tasks/{taskId}/status` → Update status
* `DELETE /api/users/{userId}/tasks/{taskId}` → Delete task

---

## ⚠️ Current Limitations

* No authentication (JWT not implemented yet)
* All endpoints are publicly accessible
* Passwords are currently stored without hashing (to be fixed)

---

## 🔜 Next Steps

### 🔐 Authentication & Security

* Hash passwords using BCrypt
* Implement login API
* Generate JWT tokens
* Add JWT filter for request validation
* Secure endpoints (user-specific access)

---

### 🚀 Enhancements

* Pagination for tasks
* Filtering (status-based tasks)
* Logging improvements
* Dockerization & deployment

---

## 🧠 Learning Outcomes

* Built a layered backend architecture
* Understood REST API design principles
* Implemented validation and global error handling
* Designed entity relationships using JPA
* Learned how to structure scalable backend systems

---

## 📌 Status

🟡 In Progress — Core backend completed, moving towards authentication and deployment
