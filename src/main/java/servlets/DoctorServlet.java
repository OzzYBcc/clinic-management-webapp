package main.java.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import main.java.basicClasses.*;
import main.java.dao.AppointmentDAO;
import main.java.dao.AvailabilityDAO;

@WebServlet("/doctor")
public class DoctorServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DEBUG: Servlet initialized - Entering doPost");
        String action = request.getParameter("action");
        System.out.println("DEBUG: Action received in doPost: " + action);
        if (action == null) {
            System.out.println("DEBUG: No action parameter in doPost");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action is required");
            return;
        }

        HttpSession session = request.getSession(false);
        System.out.println("DEBUG: Session exists: " + (session != null));
        System.out.println("DEBUG: Doctor in session: " + (session != null ? session.getAttribute("doctor") : "null"));
        if (session == null || session.getAttribute("doctor") == null) {
            System.out.println("DEBUG: No valid session or doctor in doPost");
            response.sendRedirect("login.html");
            return;
        }

        switch (action) {
            case "registerAvailability":
                handleRegisterAvailability(request, response);
                break;
            case "cancelAppointment":
                handleCancelAppointment(request, response);
                break;
            default:
                System.out.println("DEBUG: Unknown action in doPost: " + action);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DEBUG: Servlet initialized - Entering doGet");
        String action = request.getParameter("action");
        System.out.println("DEBUG: Action received in doGet: " + action);
        if (action == null) {
            System.out.println("DEBUG: No action parameter in doGet");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action is required");
            return;
        }

        switch (action) {
            case "viewProfile":
                System.out.println("DEBUG: Entering viewProfile action");
                HttpSession session = request.getSession(false);
                System.out.println("DEBUG: Session exists: " + (session != null));
                System.out.println("DEBUG: Doctor in session: " + (session != null ? session.getAttribute("doctor") : "null"));
                if (session != null && session.getAttribute("doctor") != null) {
                    Doctor doctor = (Doctor) session.getAttribute("doctor");
                    System.out.println("DEBUG: Doctor found: " + doctor.getUsername());
                    AppointmentDAO.removeCompletedAppointments(doctor.getUsername());
                    System.out.println("DEBUG: removeCompletedAppointments called for: " + doctor.getUsername());
                } else {
                    System.out.println("DEBUG: No valid session or doctor");
                }
                response.sendRedirect("doctorProfile.jsp");
                break;
            case "registerAvailability":
                System.out.println("DEBUG: Redirecting to registerAvailability.jsp");
                response.sendRedirect("registerAvailability.jsp");
                break;
            default:
                System.out.println("DEBUG: Unknown action in doGet: " + action);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    private void handleRegisterAvailability(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("DEBUG: Entering handleRegisterAvailability");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("doctor") == null) {
            System.out.println("DEBUG: No valid session or doctor in handleRegisterAvailability");
            response.sendRedirect("login.html");
            return;
        }
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        String date = request.getParameter("date");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String slot = date + " " + startTime + " to " + date + " " + endTime;
        boolean success = AvailabilityDAO.addAvailability(doctor.getUsername(), slot);
        if (success) {
            System.out.println("DEBUG: Availability added successfully");
            response.sendRedirect("registerAvailability.jsp?message=Success");
        } else {
            System.out.println("DEBUG: Failed to add availability");
            response.sendRedirect("registerAvailability.jsp?message=Failure");
        }
    }

    private void handleCancelAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("DEBUG: Entering handleCancelAppointment");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("doctor") == null) {
            System.out.println("DEBUG: No valid session or doctor in handleCancelAppointment");
            response.sendRedirect("login.html");
            return;
        }
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

        // Λήψη πληροφοριών ραντεβού
        List<Appointment> appointments = AppointmentDAO.getAppointmentsByDoctor(doctor.getUsername());
        Appointment appointmentToCancel = null;
        for (Appointment appt : appointments) {
            if (appt.getId() == appointmentId) {
                appointmentToCancel = appt;
                break;
            }
        }

        if (appointmentToCancel != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sdf.setLenient(false);
            try {
                Date appointmentDate = sdf.parse(appointmentToCancel.getDateTime());
                Date currentDate = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(appointmentDate);
                cal.add(Calendar.DAY_OF_MONTH, -3); // 3 ημέρες πριν το ραντεβού
                Date threeDaysBefore = cal.getTime();

                if (currentDate.after(threeDaysBefore)) {
                    System.out.println("DEBUG: Cannot cancel, less than 3 days");
                    response.sendRedirect("doctorProfile.jsp?message=Failure");
                    return;
                }
            } catch (ParseException e) {
                System.out.println("DEBUG: ParseException in handleCancelAppointment: " + e.getMessage());
                e.printStackTrace();
                response.sendRedirect("doctorProfile.jsp?message=Failure");
                return;
            }

            // Αν περάσει ο έλεγχος, ακυρώνουμε το ραντεβού
            if (AppointmentDAO.cancelAppointment(appointmentId)) {
                System.out.println("DEBUG: Appointment cancelled successfully");
                response.sendRedirect("doctorProfile.jsp?message=Success");
            } else {
                System.out.println("DEBUG: Failed to cancel appointment");
                response.sendRedirect("doctorProfile.jsp?message=Failure");
            }
        } else {
            System.out.println("DEBUG: Appointment to cancel not found");
            response.sendRedirect("doctorProfile.jsp?message=Failure");
        }
    }
}