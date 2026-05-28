# AI-Powered Task Management Portal

An intelligent full-stack task management application with AI-powered productivity insights, smart task suggestions, JWT authentication, and cloud deployment.

---

# Features

## Authentication
- User Registration
- Secure Login with JWT
- Password Encryption using BCrypt
- Reset Password Feature

## Task Management
- Create Tasks
- Update Tasks
- Delete Tasks
- Set Priorities
- Due Date Tracking
- Task Status Management

## AI Productivity Assistant
- AI-powered productivity insights
- Smart task suggestions
- Task prioritization recommendations
- Workflow improvement suggestions
- Task breakdown assistance
- Project planning guidance

## Cloud Deployment
- Backend deployed on Render
- Frontend deployed on Vercel
- PostgreSQL cloud database

---

# Tech Stack

## Frontend
- React.js
- Vite
- Tailwind CSS
- Axios
- React Router

## Backend
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate

## Database
- PostgreSQL

## AI Integration
- OpenRouter API
- GPT-4.1 Mini

## Deployment
- Render
- Vercel

---

# Architecture Overview

```text
Frontend (React)
        |
        v
REST APIs (Spring Boot)
        |
        v
PostgreSQL Database
        |
        v
AI Integration (OpenRouter GPT)
```

---

# Authentication Flow

1. User registers/login
2. JWT token generated
3. Token stored in sessionStorage
4. Protected APIs accessed using Bearer token
5. Spring Security validates JWT

---

# AI Integration

The application integrates GPT-powered AI features using OpenRouter API.

### AI Capabilities
- Productivity insights
- Task prioritization
- Smart recommendations
- Project planning assistance
- Task creation guidance
- Deadline management suggestions

### Prompt Engineering
Custom system prompts are used to:
- Restrict AI to productivity-related topics
- Generate concise responses
- Improve task analysis quality
- Avoid irrelevant conversations

---

# API Endpoints

## Authentication APIs
| Method | Endpoint |
|---|---|
| POST | /api/auth/register |
| POST | /api/auth/login |
| POST | /api/auth/reset-password |

## Task APIs
| Method | Endpoint |
|---|---|
| GET | /api/tasks |
| POST | /api/tasks |
| PUT | /api/tasks/{id} |
| DELETE | /api/tasks/{id} |

## AI APIs
| Method | Endpoint |
|---|---|
| POST | /api/ai/chat |

---

# Setup Instructions

## Backend Setup

### Clone Repository
```bash
git clone YOUR_BACKEND_REPO_LINK
```

### Configure Environment Variables
Create `.env`:
```env
DB_URL=YOUR_DATABASE_URL
DB_USERNAME=YOUR_DB_USERNAME
DB_PASSWORD=YOUR_DB_PASSWORD
OPENAI_API_KEY=YOUR_OPENROUTER_API_KEY
JWT_SECRET=YOUR_SECRET
```

### Run Backend
```bash
mvn spring-boot:run
```

---

## Frontend Setup
```bash
npm install
npm run dev
```

---

# Database Schema

## User
- id
- name
- email
- password

## Task
- id
- title
- description
- priority
- status
- dueDate
- createdAt
- user_id

---

# Screenshots

## Login Page
(Add Screenshot)

## Dashboard
(Add Screenshot)

## AI Assistant
(Add Screenshot)

## Task Management
(Add Screenshot)

---

# Challenges Faced
- JWT authentication integration
- AI prompt engineering
- Cloud deployment configuration
- PostgreSQL migration
- CORS handling
- Frontend-backend integration

---

# Future Enhancements
- Email OTP verification
- AI-generated subtasks
- Team collaboration
- Real-time notifications
- Dark mode
- Calendar integration

---

# Author
Sagar H N
