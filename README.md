# 🚀 Orbit Task Manager: Full Stack Application

Orbit Task Manager is a production-ready, highly aesthetic Full Stack application that relies on an expertly tailored standard vanilla frontend bridging natively into a robustly scaled Spring Boot / MySQL database layer. It is built natively for Docker. 

---

## ✨ Core Features

### 1. Robust JWT Authentication Engine (Backend)
- Passwords are encrypted before database insertion using BCrypt.
- Authenticating generates deeply signed JSON Web Tokens (10 hr expiry).
- Global API firewall automatically blocks anonymous traffic with standard `401 Unauthorized` / `403 Forbidden` responses.
- Exceptions handled uniformly gracefully across bad routes and payloads.

### 2. Glassmorphic SPA Frontend (Frontend)
- **High Aesthetic Values**: Orbit incorporates floating gradients, soft glass backdrops, CSS micro-animations, and pure SPA `DOM` wiping, meaning the page natively processes queries and animations locally without aggressively reloading the URL.
- **Client Side State**: Maintains logical token parameters locally so your `Authorization` bearer token handles the handshake invisibly.
- **Dynamic Task Loading**: Incorporates robust native pagination mapping natively back to the endpoints. Status toggle switches limit API load, sorting tasks flawlessly natively to JPA.

### 3. Integrated Tooling 
- **Swagger Documentation:** All URL mappings natively self-document under `http://localhost:9090/swagger-ui.html`. The dashboard incorporates an 'Authorize' token module allowing API manipulation inside the documentation.
- **Docker Ready:** Deploy cleanly off a tiny containerized footprint via pure CLI.

---

## 🛠 Setup & Run Instructions

You have two pathways to launch the Orbit backend. 

### Method 1: Bare Metal Java
1. Ensure your local `mysql` server is actively running matching `Kowshik@1234` alongside the `app_user` in port `3306`.
2. Ensure you have executed `mvn clean compile`.
3. In your terminal run:
   ```bash
   mvn spring-boot:run
   ```
4. Navigate locally to `http://localhost:9090/swagger-ui.html` for API analysis.

### Method 2: Docker Containers 🐳
If you want to boot the entire MySQL server plus the Java Spring application flawlessly dynamically without relying on localized environments:
1. Fire up a terminal in the root environment.
2. Build and stand up the internal networks natively:
   ```bash
   docker-compose up -d --build
   ```

### Booting the Frontend
Since Orbit's UI operates as a fully native application layout dynamically calling our backend ports securely through configured CORS policies:
1. Open a terminal anywhere and dive into your internal files natively:
   ```bash
   cd frontend
   ```
2. Spawn a lightweight HTTP engine via Python:
   ```bash
   python3 -m http.server 5500
   ```
3. Boot up your web browser dynamically: **[http://localhost:5500](http://localhost:5500)**

---

## 📚 Application Walkthrough

### 1. Access & The Void Interface
Upon launching the browser, you are introduced to the **Auth Landing View**. The Spring Backend has inherently blocked all API access globally except the `POST /api/users` and `POST /api/auth/login`. 
- Utilize the toggle beneath the core login logic box to bounce between Registration and Loading. 
- Try registering. The system immediately processes your request, throws it into MySQL, and turns the localized message green dynamically parsing the OK state locally!

### 2. The Command Dashboard
If the response returned is `200 OK`, the UI seamlessly extracts your unique Bearer Token from the JSON return and hides standard auth layers bringing in the core list.
- **Creating Tasks:** Use the text field near the top. Press `Add Task`. Your API resolves cleanly dynamically rebuilding your task loop immediately upon network resolve.
- **Dynamic Paginator:** You are locked statically to resolving chunks dynamically (5-items). Press 'Next'. Orbit loads the remaining chunks dynamically mapping Spring's `Pageable` backend interface correctly updating standard numerical limits gracefully.
- **Manipulating Properties:**
    - Change any task's label out to "IN_PROGRESS". Your system silently intercepts the option click, natively routes a clean `PATCH` across Port 9090 modifying only the database integer structure inherently speeding the DOM up.
    - Click `Edit` to call Orbit's layered modal logic dynamically preloading strings flawlessly over `PUT` commands.
- **Status Tabs**: The core toggle arrays internally fire native strings seamlessly intercepting our updated Backend parameter rules rebuilding identical pagination cleanly through your active filtering states.

---

## 📁 Project Structure

```text
📦 Task-Manager
 ┣ 📂 frontend               # Glassmorphic SPA Javascript Frontend layer
 ┃ ┣ 📜 app.js              # State manager and JWT hook rules
 ┃ ┣ 📜 index.html          # Core modal mappings
 ┃ ┗ 📜 styles.css          # Design tokens and visual animations
 ┣ 📂 src
 ┃ ┣ 📂 main/java/com/kowshik/taskmanager
 ┃ ┃ ┣ 📂 controller       # RESTful Controller Endpoints mapping Swagger
 ┃ ┃ ┣ 📂 dto              # Internal Java payload structs
 ┃ ┃ ┣ 📂 entity           # Native Hibernate/MySQL class binds
 ┃ ┃ ┣ 📂 exception        # Global 400/401/403/500 JSON Interceptors
 ┃ ┃ ┣ 📂 repository       # JPA Data layer 
 ┃ ┃ ┣ 📂 security         # JWT logic, SpringSecurity CORS pipelines
 ┃ ┃ ┗ 📂 service          # Core Java pagination algorithms
 ┣ 📜 docker-compose.yml     # Master Docker bridge orchestrator 
 ┣ 📜 Dockerfile             # Multi-layer Maven compilation routines
 ┗ 📜 pom.xml               # Standard dependencies
```
