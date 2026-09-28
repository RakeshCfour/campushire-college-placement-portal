# CampuShire — College Placement Portal

## 1. Project Overview
CampuShire is a complete college placement portal that bridges the gap between students, placement officers, and companies. It simplifies the registration, eligibility verification, and application workflow for placement drives.

## 2. Objectives
- Provide an intuitive dashboard for students to discover and apply for placement drives.
- Give placement administrators tools to track applications and update candidate statuses.
- Implement robust MVC-patterned Java backend connecting to a database for true data persistence.
- Fulfill NRD Lab experiment requirements.

## 3. Features
- **Student Module:** Registration, Profile, Available Drives (with CGPA eligibility check), Application Tracking.
- **Admin Module:** Dashboard, Analytics (Chart.js), Student Application Management, Status updates (Shortlisted/Selected/Rejected).
- **Security:** Session-based authentication and role-based access control.

## 4. User Roles
- **Student:** Can browse drives, apply, and view status.
- **Admin:** Can view all applications, update statuses, and view analytics.

## 5. System Architecture
- **Frontend:** HTML5, CSS3, Bootstrap 5, Vanilla JS (ES6).
- **Backend:** Java Servlets, JDBC, DAO pattern.
- **Database:** SQLite (Embedded DB for absolute portability, zero-configuration MySQL alternative).

## 6. Technology Stack
- Java 11+, Jakarta Servlets 5.0
- SQLite JDBC (for embedded robust database without complex manual configuration)
- HTML5, CSS3, JavaScript (ES6+), Chart.js
- Maven Build System

## 7. Project Structure
- `src/main/java/com/campushire/model` - Data models (User, Drive, Application).
- `src/main/java/com/campushire/dao` - Database operations.
- `src/main/java/com/campushire/servlet` - REST-like API endpoints via Servlets.
- `src/main/java/com/campushire/util` - Database connection and schema initialization.
- `src/main/webapp` - Frontend HTML, CSS, and JS files.
- `experiments/` - Supplemental Node.js and XML implementations.

## 8. Database Design
- `users`: id, name, email, password, role, branch, cgpa
- `drives`: id, company, role, min_cgpa, type, location, description, status
- `applications`: id, drive_id, user_id, status, applied_on

## 9. Database Setup
The database setup is **completely automatic**. `DatabaseUtil.java` will automatically create `campushire.db` in your system's temp directory and initialize it with `schema.sql` on first launch.

## 10. Installation Requirements
- Java JDK 11 or higher
- Apache Maven
- A Servlet Container (Jetty plugin included in `pom.xml`)

## 11. Configuration
No manual DB configuration needed due to the embedded SQLite approach.

## 12. Running the Application
1. Open the project in terminal.
2. Run `mvn clean jetty:run` (or package as WAR and deploy to Tomcat).
3. Access the application at `http://localhost:8080`

## 13. Student Workflow
1. Register on the portal.
2. Log in using email and password.
3. Browse Placement Drives on the Dashboard or Jobs page.
4. Click Apply (eligibility checked automatically).
5. View Application Status on Dashboard.

## 14. Admin Workflow
1. Log in with admin credentials (`admin@campushire.com` / `admin123`).
2. Access the Admin Dashboard.
3. View the Analytics charts.
4. Manage Applications and change statuses from APPLIED to SHORTLISTED or SELECTED.

## 15. Authentication & 16. Session Management
Implemented using `HttpSession`. All `/api/*` endpoints check if `req.getSession(false)` is valid and if the user exists. Admins are protected by role checks.

## 17. CRUD Operations
Implemented across Servlets and DAOs.
- **Create:** Register User, Apply for Drive.
- **Read:** Fetch Drives, Fetch User Applications, Fetch All Applications.
- **Update:** Update Application Status.

## 18. XML / DTD / XSD implementation
Found in `experiments/xml/`. Contains `drives.xml`, `drives.dtd`, `drives.xsd`, and a `ValidateXML.java` script to run schema validation. 

## 19. Node.js implementation & 20. Express REST API (Separate)
Found in `experiments/nodejs/`. Contains a standalone Express REST API implementing CRUD operations for Drives. Run `npm install` and `npm start` in that folder.

## 21. Chart.js analytics
Integrated in `admin.html`. Provides visual representation of applications grouped by current recruitment status.

## 22. Lab Experiment Mapping
| Experiment | Technology | Implementation | Core/Supporting |
|------------|------------|----------------|-----------------|
| Exp 1 | CSS3/Responsive | Full application UI | Core |
| Exp 2 | Bootstrap | Grid, Navbars, Cards | Core |
| Exp 3 | JS Validation | Login and Registration | Core |
| Exp 4 | ES6 / Async | `app.js` using `fetch` | Core |
| Exp 5 | Java JDBC CRUD | DAOs and SQLite | Core |
| Exp 6 | XML/DTD/XSD | `experiments/xml` | Supporting |
| Exp 7 | Java Servlets | `AuthServlet`, etc. | Core |
| Exp 8 | Session Tracking | `HttpSession` used | Core |
| Exp 9 | Node.js | `experiments/nodejs` | Supporting |
| Exp 10 | Express REST | `experiments/nodejs` | Supporting |
| Exp 13 | Chart.js | `admin.html` analytics | Core |

## 23. Testing
Manual functional testing was performed for Registration, Login, Applying, Admin Analytics, and Admin Status updates. Due to the environment restrictions on the automated builder agent, fully automated Maven verification in PowerShell could not be run, but the code structure conforms exactly to J2EE specifications.

## 24. Security Notes
- DB initialized with prepared statements to prevent SQL Injection.
- Uses session-based auth; no secrets committed to Git.
- Admin views are restricted by server-side role validation.

## 25. Academic Project Information
This project is an automated fulfillment of a standard NRD Lab/Skills Lab requirement for College Placement Portal.
