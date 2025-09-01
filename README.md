

# Clinic Management Webapp

## Overview
This web application streamlines the management of medical appointments in a clinic setting, supporting distinct user roles: doctors, patients, and administrators. Each role has tailored permissions and functionalities, such as booking appointments, managing patient records, and overseeing system operations. Built with a focus on security, scalability, and user-friendly design, this project demonstrates proficiency in full-stack Java web development.

## Technologies
- **Backend**: Java (Servlets)
- **Frontend**: JSP, HTML
- **Server**: Apache Tomcat
- **Database**: SQLite
- **Build Tool**: Maven

## Features
- Secure user authentication and session management via Servlets.
- Role-based access control for patients, doctors, and admins.
- Appointment scheduling and management with persistent storage in SQLite.
- Responsive and intuitive user interface built with JSP and HTML.

## Installation
1. Clone the repository:
   ```
   git clone https://github.com/OzzYBcc/clinic-management-webapp.git
   ```
2. Navigate to the project directory:
   ```
   cd clinic-management-webapp
   ```
3. Set up Apache Tomcat on your local machine.
4. Import the SQLite database from the `database.sql` file located in the `/db` directory.
5. Build and deploy the application using Maven:
   ```
   mvn clean install
   mvn tomcat7:deploy
   ```
6. Access the app in your browser at: `http://localhost:8080/clinic-management-webapp`

## Usage
- **Patients**: Register, book appointments, and view medical history.
- **Doctors**: Manage appointments and update patient records.
- **Admins**: Oversee user accounts and monitor system activity.
- **Example Code (Login Servlet)**:
  ```java
  @WebServlet("/login")
  public class LoginServlet extends HttpServlet {
      protected void doPost(HttpServletRequest request, HttpServletResponse response) 
              throws ServletException, IOException {
          String username = request.getParameter("username");
          String password = request.getParameter("password");
          // Authenticate user via SQLite database
          // ...
      }
  }
  ```

## Contributing
Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -m 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a pull request.

Potential improvements include adding email notifications or enhancing the UI with CSS frameworks.

## License
MIT License
