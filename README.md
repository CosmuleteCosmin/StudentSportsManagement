# 🏆 Student Sports Management System

A comprehensive management platform built with **Java Spring Boot** and **MS SQL Server**. The application manages the complex relationships between students, faculties, and sports teams, featuring advanced data filtering and performance-optimized reporting.

## 🛠️ Tech Stack

* **Backend:** Java, Spring Boot (MVC Architecture)
* **Database:** Microsoft SQL Server (MSSQL)
* **Data Access:** Spring Data JPA & Native SQL (Custom queries for complex reporting)
* **Frontend**: Thymeleaf, HTML, CSS,
* **Build Tools:** Gradle, Git
* **IDE:** IntelliJ IDEA

## 📂 Project Documentation

This repository includes architectural details and database resources:
* **RAPORT PROIECT AWJ.pdf & Documentatie.pdf** Full project specifications and architectural details.
* **Interogari.pdf** The complex SQL queries used for generating reports (Joins, Subqueries, Aggregations).
* **EvidentaEchipelorSportiveDeStudenti.bak** Database backup file for quick restoration..

## ⚙️ Setup & Installation

### 1. Clone the repository:
```bash
git clone [https://github.com/CosmuleteCosmin/StudentSportsManagement.git](https://github.com/CosmuleteCosmin/StudentSportsManagement.git)
```
### 2. Database Setup:
 * Restore the EvidentaEchipelorSportiveDeStudenti.bak file in MS SQL Server Management Studio (SSMS).

### 3. Configure Application:
 * Open src/main/resources/application.properties.
 * Update the spring.datasource.url, username, and password to match your local SQL Server instance.

### 4.Run the App:
 * Run the project as a Spring Boot App via your IDE (IntelliJ/Eclipse)
```bash
./gradlew bootRun
```

### 5.Access:
 * Go to http://localhost:8080 in your browser.

Developed by Cosmulete Ion-Cosmin
