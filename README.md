# 🎯 CareerTrack

**Job Application & Interview Management System**

**Live Demo:** https://career-track-production.up.railway.app

CareerTrack is a full-stack web application that helps job seekers track applications, schedule interviews, manage documents, and get AI-powered interview prep — all in one place, replacing the usual messy spreadsheet.

Built with **Java 21 · Spring Boot 4 · Spring Security · MySQL · Thymeleaf · Bootstrap 5 · Chart.js**.

---

## ✨ Features

### For Job Seekers
- **Application Tracking** — log every job you apply to with company, role, salary, location, status, and notes
- **Status Pipeline** — Applied → In Review → Shortlisted → Offered / Rejected
- **Interview Scheduling** — round type, date/time, mode (online/offline), meeting links, interviewer details
- **Dashboard & Analytics** — status breakdown doughnut chart + 6-month application timeline chart
- **Email & In-App Notifications** — automatic reminder email the day before an interview, plus instant in-app alerts
- **Document Management** — upload/download/preview resumes, cover letters, offer letters, certificates (secure, per-user access only)
- **AI Career Assistant** — instant, role-specific interview tips and mock interview questions, powered by Groq

### For Admins
- **User Management** — view all users, activate/deactivate accounts, promote/demote roles, delete accounts
- **System Analytics** — platform-wide stats: total users, active/inactive, total applications/interviews/documents
- **Role-based Access Control** — `/admin/**` routes are protected at the Spring Security level (`hasRole("ADMIN")`)

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4, Spring MVC, Spring Data JPA |
| Security | Spring Security 6 (session-based auth, CSRF protection, role-based access) |
| Database | MySQL 8 |
| Templating | Thymeleaf (+ Spring Security Thymeleaf Extras) |
| Frontend | Bootstrap 5, Font Awesome, Chart.js |
| Email | Spring Mail (SMTP) |
| Scheduling | Spring `@Scheduled` (daily interview reminders) |
| AI | Groq API (OpenAI-compatible `chat/completions` endpoint) |
| Testing | JUnit 5, Mockito |
| Build Tool | Maven |

---

## 📁 Project Structure

```
careertrack/
├── src/main/java/com/careertrack/
│   ├── config/          # Security config, RestTemplate/PasswordEncoder beans, global model attributes
│   ├── controller/       # All @Controller classes (Auth, Dashboard, Applications, Interviews,
│   │                       Documents, Notifications, AI, Admin, Profile)
│   ├── dto/              # Request/form DTOs
│   ├── enums/             # ApplicationStatus, InterviewStatus, DocumentType, Role
│   ├── model/            # JPA entities (User, JobApplication, Interview, Document, Notification)
│   ├── repository/        # Spring Data JPA repositories
│   ├── scheduler/          # Interview reminder scheduled job
│   ├── security/           # UserDetailsService, Spring Security wiring
│   └── service/            # Business logic (interfaces + Impl classes)
├── src/main/resources/
│   ├── static/css/         # style.css (custom theme)
│   ├── static/js/          # app.js
│   ├── templates/          # Thymeleaf views, organized by module
│   └── application.properties
├── src/test/java/com/careertrack/service/   # JUnit + Mockito unit tests
└── pom.xml
```

---

## 🚀 Getting Started

### Prerequisites
- Java 21+
- MySQL 8+ (running locally, or via XAMPP/Docker)
- A Gmail account with an [App Password](https://myaccount.google.com/apppasswords) (for email notifications)
- A free [Groq API key](https://console.groq.com) (for the AI Assistant feature)

### 1. Clone the repository
```bash
git clone https://github.com/Rabbani19/Career-Track.git
cd Career-Track/careertrack
```

### 2. Create the database
```sql
CREATE DATABASE careertrack_db;
```
Tables are created automatically on first run (`spring.jpa.hibernate.ddl-auto=update`).

### 3. Set environment variables

CareerTrack reads sensitive config from environment variables rather than hardcoding them in `application.properties`:

| Variable | Description |
|---|---|
| `DB_PASSWORD` | Your local MySQL root password |
| `MAIL_USERNAME` | Gmail address used to send notification emails |
| `MAIL_APP_PASSWORD` | Gmail App Password (not your regular Gmail password) |
| `GROQ_API_KEY` | API key from [console.groq.com](https://console.groq.com) |

Set these in your terminal/IDE run configuration before starting the app, e.g. (PowerShell):
```powershell
$env:DB_PASSWORD="yourpassword"
$env:MAIL_USERNAME="you@gmail.com"
$env:MAIL_APP_PASSWORD="your16charapppassword"
$env:GROQ_API_KEY="gsk_your_key_here"
```

### 4. Run the application

Using the Maven wrapper (no separate Maven install needed):
```bash
./mvnw spring-boot:run        # macOS/Linux
.\mvnw.cmd spring-boot:run     # Windows
```

The app starts on **http://localhost:8085**.

### 5. Make yourself an admin (optional)
There's intentionally no self-service "become admin" button. Promote your account directly in MySQL:
```sql
UPDATE users SET role='ADMIN' WHERE username='your_username';
```
Restart the app and the "Admin" link will appear in your navbar.

---

## 🧪 Running Tests

**Unit tests (JUnit 5 + Mockito)** — covers service-layer business logic (defaults, ownership checks, exceptions, stats aggregation):
```bash
./mvnw test
```

**API testing (Postman)** — a full Postman collection is included, covering every endpoint (Auth, Applications, Interviews, Documents, Notifications, AI, Admin, Profile) with automatic session/CSRF token handling via pre-request scripts. Import `CareerTrack.postman_collection.json` into Postman, set your credentials in the collection variables, then run `Auth > 1. Get Login Page (CSRF)` followed by `Auth > 2. Login` before any other request.

---

## 🔒 Security Notes

- Passwords are hashed with BCrypt — never stored or logged in plain text
- CSRF protection is enabled globally; Thymeleaf auto-injects tokens into every `th:action` form
- All application/interview/document/notification data is scoped per-user — one user can never view or modify another user's data, even by guessing IDs in the URL
- `/admin/**` routes require `ROLE_ADMIN`, enforced at the Spring Security filter chain level (not just hidden in the UI)
- File downloads go through an authenticated controller endpoint with ownership checks — not served as static files

---

## 📌 Roadmap / Project Stages

- [x] Stage 1 — Project Setup & Configuration
- [x] Stage 2 — Database Design & Entity Classes
- [x] Stage 3 — User Module (Register, Login, Profile)
- [x] Stage 4 — Job Application Module
- [x] Stage 5 — Interview Management Module
- [x] Stage 6 — Dashboard & Analytics
- [x] Stage 7 — Notification & Email System
- [x] Stage 8 — Document Management
- [x] Stage 9 — AI Features Integration
- [x] Stage 10 — Admin Module
- [x] Stage 11 — Testing & Final Polish

---

## 👤 Author

**Yasir Rabbani**
GitHub: [@Rabbani19](https://github.com/Rabbani19)

---

## 📄 License

This project is available for educational and portfolio purposes.
