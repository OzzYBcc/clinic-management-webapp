package main.java.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import main.java.basicClasses.*;
import main.java.dao.UserDAO;
import main.java.util.passwordUtil;
import main.java.dao.AppointmentDAO;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("login".equals(action)) {
            String userType = request.getParameter("userType");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            HttpSession session = request.getSession();

            System.out.println("🔐 Attempting login...");
            System.out.println(" - userType: " + userType);
            System.out.println(" - username: " + username);

            User user = UserDAO.findByUsernameAndType(username, userType);
            if (user != null) {
                System.out.println("✅ User found in DB: " + user.getUsername());
                System.out.println(" - Salt from DB: " + user.getSalt());

                String hashedInput = passwordUtil.hashPassword(password, user.getSalt());

                System.out.println(" - Hashed input password: " + hashedInput);
                System.out.println(" - Stored hashed password: " + user.getHashedPassword());

                if (hashedInput.equals(user.getHashedPassword())) {
                    System.out.println("🎉 Login successful!");

                    switch (userType) {
                        case "patient":
                            Patient patient = (Patient) user;
                            session.setAttribute("patient", patient);
                            response.sendRedirect("patientProfile.jsp");
                            break;
                        case "doctor":
                            Doctor doctor = (Doctor) user;
                            session.setAttribute("doctor", doctor);
                            AppointmentDAO.removeCompletedAppointments(doctor.getUsername());
                            System.out.println("DEBUG: removeCompletedAppointments called after login for: " + doctor.getUsername());
                            response.sendRedirect("doctorProfile.jsp");
                            break;
                        case "admin":
                            Admin admin = (Admin) user;
                            session.setAttribute("admin", admin);
                            response.sendRedirect("adminPanel.jsp");
                            break;
                    }
                } else {
                    System.out.println("❌ Passwords don't match.");
                    response.sendRedirect("login.html?error=1");
                }
            } else {
                System.out.println("❌ User not found.");
                response.sendRedirect("login.html?error=1");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect("login.html");
        }
    }
}