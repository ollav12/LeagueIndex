# LeagueIndex

LeagueIndex is a website that can be used to gather insightful data & statistics about a user's league of legends
account. By accessing the riot games api, we can get tons of useful data, that we can convert into presentable and
useful data for the end user.

![image](https://github.com/user-attachments/assets/edea9259-b927-419b-9473-fe3d17d8a873)

## Table of Contents

- [Prerequisites](#prerequisites)
- [Backend Setup](#backend-setup)
- [Frontend Setup](#frontend-setup)

## Prerequisites

Before you begin, ensure you have the following installed on your machine:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) (version 16 or
  later)
- [Gradle](https://gradle.org/install/)
- [Node.js and npm](https://nodejs.org/) (version 14 or later)
- [Docker Desktop]()

You also need a riot games dev api key, read more at https://developer.riotgames.com/ on how to get one.
Create your `.env` file inside `backend`, the `.env.example` shows how it should be.

## Backend Setup

1. Compose docker:

   ```bash
   docker compose up -d
   ```

2. Navigate to the `backend` directory:

   ```bash
   cd backend
   ```

3. Build the project:

   ```bash
   ./gradlew build
   ```

4. Run the backend:
   ```bash
   ./gradlew bootRun
   ```

## Frontend Setup

1. Navigate to the `frontend` directory:

   ```bash
   cd frontend
   ```

2. Install the dependencies:

   ```bash
   npm install
   ```

3. Run the frontend development server:
   ```bash
   npm run dev
   ```

## Database access

You can acces postgres using pgAdmin on http://localhost:5050/ with username and password defined in `application.yml`.

Promethues is avaiable at http://localhost:9090/

Grafana is avaiable at http://localhost:3000/ with username `admin` and password `admin`.