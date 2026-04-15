# Coding Club Resource Hub

The Coding Club Resource Hub is a centralized platform for hosting student made and open-source course resources, worksheets, and academic discussions for Computer Science, Mathematics, Physics, and Statistics students.

The project is developed by the Coding Club as a collaborative software engineering initiative. Students contribute to the platform as part of a real-world development team, gaining experience with modern full-stack technologies while building tools that benefit the university community.

---

## Features

- Blog for CMPS student opportunities on campus.
- Centralized repository for COSC, MATH, PHYS, and STAT course resources.
- Past exams and student-created worksheets.
- Discussion pages for courses and topics.
- Authentication system for CSCU members and online tutoring services.
- Faculty research pages highlighting research interests and opportunities.
- Event and announcement pages for Coding Club, CSCU, and Girls in Tech (GIT).

---

## Tech Stack

The platform is built using Spring Boot + Vite(React).

### Backend

- **Gradle**
- **Spring Boot**
- **Java**
- **PostgreSQL**

The backend provides a REST API that handles authentication, and resource management.

### Frontend

- **Vite**
- **TypeScript**
- **React**

Vite provides a fast development environment and optimized production builds for the frontend application.

### Database

- **PostgreSQL**

PostgreSQL is used to store user accounts, course resources, discussion posts, and metadata associated with uploaded files.

---

## Architecture

```
Browser
↓
Frontend (Vite + React + TypeScript)
↓
Backend API (Gradle + Spring Boot + Java)
↓
PostgreSQL Database
```

TODO: Replace this with an image

Uploaded documents such as past exams and worksheets are stored on the server, with metadata stored in the database.

---

## Project Structure

```
CC-Website/
│
├── .github/workflows
│ ├── docker-image.yml                  # Docker build and testing workflow
│ ├── gradle.yml                        # Backend build and testing workflow
│ └── node.js.yml                       # Frontend build and testing workflow
│
├── backend/
│ ├── gradle/wrapper/
│ │ ├── gradle-wrapper.jar
│ │ └── gradle-wrapper.properties
│ │
│ ├── src/
│ │ ├── main/
│ │ │ ├── java/com/example/app
│ │ │ │ ├── config/                     # Reuseable backend logic (security, beans, etc...)
│ │ │ │ ├── controller/                 # Routes
│ │ │ │ ├── model/                      # Classes and object interfaces
│ │ │ │ ├── repository/                 # Data access layer
│ │ │ │ ├── service/                    # Main backend logic
│ │ │ │ └── SpringAppApplication.java   # Main Spring application
│ │ │ │
│ │ │ └── resources/
│ │ │   ├── static/
│ │ │   ├── templates/
│ │ │   └── application.properties      # Environment variable config
│ │ │
│ │ └── test/java/com/example/app       # Testing
│ │
│ ├── .gitattributes
│ ├── .gitignore
│ ├── build.gradle
│ ├── dockerfile
│ ├── gradlew
│ ├── gradlew.bat
│ └── settings.gradle
│
├── frontend/
│ ├── src/
│ │ ├── assets/          # Static assets
│ │ ├── components/      # React components
│ │ ├── pages/           # Client-facing webpages
│ │ ├── App.tsx          # Main App component
│ │ └── main.tsx         # React entry point
│ │
│ ├── index.html
│ ├── Dockerfile
│ ├── package.json
│ ├── tsconfig.json
│ ├── eslint.config.js
│ └── vite.config.ts     # Build configuration
│
├── docker-compose.yml   # Multi-container setup
├── README.md
├── LICENSE
└── .gitignore
```

---

## Getting Started

### Prerequisites

- Node.js(v20.x or v22.x)
- Java Open JDK v25.0.2
- Docker and Docker Compose
- npm

### Development

Clone the repository:

```bash
    git clone https://github.com/Coding-Club-SUO/CC-Website.git
    cd CC-Website
```

Once cloned depending on your role cd into the appropriate folder:

```bash
    cd backend # if you're working on backend.
    cd frontend # if you're working on frontend.
```

Open `application.properties` and fill in the required values.

IMPORTANT: The main branch is protected so pushes have to be made on seperate branches before being merged into the main branch via pull requests. A pull-request must be approved before being merged.

## Running and Testing

For the backend:

```bash
cd backend
./gradlew build         # This will run all the tests and attempt to re-build the application
./gradlew bootRun       # This will run the spring application
```

For the frontend:

```bash
cd frontend
npm run dev             # This will run the vite application
```

IMPORTANT: The frontend relies on the backend to function correctly. For proper testing and operation, run them simultaneously in separate terminals, one for the frontend and one for the backend.

## Deployment

### Step 1: Install Docker

Docker is a container that holds all our code and dependencies so it runs the same way everywhere.

**For Windows or Mac:**

1. Go to https://www.docker.com/products/docker-desktop
2. Download Docker Desktop
3. Install it and restart your computer
4. Open a terminal and check it worked:
   ```bash
   docker --version
   docker-compose --version
   ```

**For Linux:**

```bash
sudo apt-get update
sudo apt-get install docker.io docker-compose
sudo usermod -aG docker $USER
newgrp docker
```

### Step 2: Start Everything

```bash
docker-compose up --build
```

This command builds the Docker images and starts all the services. You'll see a bunch of text scrolling - that's normal.

### Step 3: Open It Up

Once you see "Application startup complete" in the logs, open your browser and go to:

- **Frontend**: http://localhost:5173
- **Backend**: http://localhost:8080

---

## Common Docker Commands

### Start the system

```bash
# See all the logs in your terminal
docker-compose up

# Run it in the background
docker-compose up -d

# Rebuild everything from scratch
docker-compose up --build
```

### Stop the system

```bash
# Stop everything
docker-compose down

# Stop and delete everything (this deletes your data!)
docker-compose down -v
```

### Check what's happening

```bash
# See all logs from both backend and frontend
docker-compose logs

# Follow backend logs as they happen
docker-compose logs -f backend

# Follow frontend logs
docker-compose logs -f frontend
```

### Restart things

```bash
# Restart everything
docker-compose restart

# Just restart the backend
docker-compose restart backend
```

---
