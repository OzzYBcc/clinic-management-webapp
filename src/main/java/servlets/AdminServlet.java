package main.java.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import main.java.basicClasses.*;
import javax.servlet.annotation.WebServlet;
import main.java.dao.UserDAO;
import main.java.util.passwordUtil;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect("login.html");
            return;
        }
        Admin admin = (Admin) session.getAttribute("admin");

        switch (action) {
            case "addDoctor":
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String name = request.getParameter("name");
                String surname = request.getParameter("surname");
                int age = Integer.parseInt(request.getParameter("age"));
                String specialty = request.getParameter("specialty");
                String amka = request.getParameter("amka");
                String salt = passwordUtil.generateSalt();
                String hashedPassword = passwordUtil.hashPassword(password, salt);
                Doctor doctor = new Doctor(username, hashedPassword, name, surname, age, specialty, salt, amka);
                if (admin.addDoctor(username, hashedPassword, name, surname, age, specialty, salt, amka)) {
                    response.sendRedirect("adminPanel.jsp?message=Success&action=" + action);
                } else {
                    response.sendRedirect("adminPanel.jsp?message=Failure&action=" + action);
                }
                break;
            case "addUser":
                String userUsername = request.getParameter("username");
                String userPassword = request.getParameter("password");
                String userName = request.getParameter("name");
                String userSurname = request.getParameter("surname");
                int userAge = Integer.parseInt(request.getParameter("age"));
                String userRole = request.getParameter("role");
                String userAmka = request.getParameter("amka");
                String userSalt = passwordUtil.generateSalt();
                String userHashedPassword = passwordUtil.hashPassword(userPassword, userSalt);
                User user = new User(userUsername, userHashedPassword, userName, userSurname, userAge, userSalt, userAmka);
                if (admin.addUser(userUsername, userHashedPassword, userName, userSurname, userAge, userRole, userSalt, userAmka)) {
                    response.sendRedirect("adminPanel.jsp?message=Success&action=" + action);
                } else {
                    response.sendRedirect("adminPanel.jsp?message=Failure&action=" + action);
                }
                break;
            case "removeDoctor":
                String doctorUsername = request.getParameter("doctorUsername");
                if (admin.removeDoctor(doctorUsername)) {
                    response.sendRedirect("adminPanel.jsp?message=Success&action=" + action);
                } else {
                    response.sendRedirect("adminPanel.jsp?message=Failure&action=" + action);
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }
}