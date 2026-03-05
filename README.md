# Coding Club Resource Hub

The Coding Club Resource Hub is a centralized platform for hosting student made and open-source course resources, worksheets, and academic discussions for Computer Science, Mathematics, Physics, and Statistics students.

The project is developed by the Coding Club as a collaborative software engineering initiative. Students contribute to the platform as part of a real-world development team, gaining experience with modern full-stack technologies while building tools that benefit the university community.

Access to course materials is restricted to members of the Computer Science Course Union (CSCU) to ensure resources remain available exclusively to students within the department.

---

## Features

- Centralized repository for COSC, MATH, PHYS, and STAT course resources
- Past exams and student-created worksheets
- Discussion pages for courses and topics
- Authentication system for CSCU members
- Faculty research pages highlighting research interests and opportunities
- Event and announcement pages for Coding Club, CSCU, and Girls in Tech (GIT)

---

## Tech Stack

The platform is built using a modern TypeScript-based web stack.

### Backend

- **Node.js**
- **Express**
- **TypeScript**
- **PostgreSQL**

The backend provides a REST API that handles authentication, resource management, and discussions.

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
Backend API (Node.js + Express + TypeScript)
↓
PostgreSQL Database
```

TODO: Replace this with an image

Uploaded documents such as past exams and worksheets are stored on the server, with metadata stored in the database.

---

## Project Structure

```
ccwebsite/
│
├── backend/
│ ├── src/
│ │ ├── routes/
│ │ ├── controllers/
│ │ ├── services/
│ │ └── database/
│ └── package.json
│
├── frontend/
│ ├── src/
│ │ ├── components/
│ │ ├── pages/
│ │ └── api/
│ └── package.json
│
└── README.
```
---

## Getting Started

### Development

### Prerequisites

- Node.js
- PostgreSQL
- npm or pnpm

### Installation

Clone the repository:

    ````bash
    git clone https://github.com/Coding-Club-SUO/CC-Website.git
    cd CC-Website

`````

Once cloned depending on your role cd into the appropriate folder:

    ````bash
    cd backend # if you're working on backend.
    cd frontend # if you're working on frontend.
`````

IMPORTANT: The main branch is protected so pushes have to be made on seperate branches before being merged into the main branch via pull requests. A pull-request must be approved before being merged.

## Testing and Running

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

- **Frontend**: http://localhost:3000 (the user-facing website)
- **Backend**: http://localhost:8000 (the API)

---

## Common Commands

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
