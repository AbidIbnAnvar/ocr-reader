# 📑 Receipt Expense Tracker

A full-stack application to manage and track expenses through receipts.  
This project consists of:

- **Frontend**: Angular (inside `frontend/`)
- **Backend**: Spring Boot (inside `backend/`)
- **Database**: PostgreSQL (running as a Docker container)

---

## 🚀 Getting Started

### 1. Clone the repository

```
git clone https://github.com/AbidIbnAnvar/ocr-reader.git
```
---

### 2. Environment Configuration
Inside the Spring Boot backend, there’s a sample environment file located at:
```
backend/src/main/resources/.env.example
```
Copy this file and rename it to .env, then update the values according to your setup (e.g., database credentials, JWT secrets, etc.):
```
cp backend/src/main/resources/.env.example backend/src/main/resources/.env
```
---

### 3. Start PostgreSQL with Docker

The backend uses PostgreSQL as its database.
If you already have Docker installed, run:

```
docker run --name expense-db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=expenses \
  -p 5432:5432 \
  -d postgres
```
---

### 4. Run the Backend (Spring Boot)

Navigate into the backend directory and start the Spring Boot server:
```
cd backend/
mvn spring-boot:run
```

### 5. Run the Frontend (Angular)

Navigate into the frontend directory and start the Angular dev server:
```
cd frontend/
ng serve
``

