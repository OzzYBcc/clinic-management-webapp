package main.java.servlets;

import javax.servlet.RequestDispatcher;
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

@WebServlet("/patient")
public class PatientServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action required");
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("patient") == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        switch (action) {
            case "schedule":
                handleSchedule(request, response);
                break;
            case "cancelAppointment":
                handleCancelAppointment(request, response);
                break;
            case "markCompleted":
                handleMarkCompleted(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action required");
            return;
        }

        switch (action) {
            case "viewProfile":
                RequestDispatcher dispatcher = request.getRequestDispatcher("patientProfile.jsp");
                if (dispatcher != null) {
                    dispatcher.forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
                }
                break;
            case "searchDoctor":
                handleSearch(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    private void handleSchedule(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("patient") == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }
        Patient patient = (Patient) session.getAttribute("patient");
        String doctorUsername = request.getParameter("doctorUsername");
        String dateTime = request.getParameter("datetime");

        // Έλεγχος αν το datetime είναι διαθέσιμο
        List<String> availableSlots = AvailabilityDAO.getAvailableSlots(null); // null για όλες τις ειδικότητες
        if (availableSlots.contains(dateTime)) {
            if (AppointmentDAO.bookAppointment(doctorUsername, patient.getUsername(), dateTime)) {
                response.sendRedirect(request.getContextPath() + "/patient?action=viewProfile&message=Success");
            } else {
                response.sendRedirect(request.getContextPath() + "/patient?action=viewProfile&message=Failure&reason=Appointment could not be booked. Check doctor username and time.");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/patient?action=viewProfile&message=Failure&reason=Selected time is not available.");
        }
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("patient") == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }
        request.getRequestDispatcher("searchResults.jsp").forward(request, response);
    }

    private void handleCancelAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("patient") == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

        // Λήψη πληροφοριών ραντεβού
        List<Appointment> appointments = AppointmentDAO.getAppointmentsByPatient(((Patient) session.getAttribute("patient")).getUsername());
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
                    response.sendRedirect(request.getContextPath() + "/patient?action=viewProfile&message=Failure&reason=Cancellation is not allowed less than 3 days before the appointment.");
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/patient?action=viewProfile&message=Failure&reason=Invalid date format.");
                return;
            }

            // Αν περάσει ο έλεγχος, ακυρώνουμε το ραντεβού
            if (AppointmentDAO.cancelAppointment(appointmentId)) {
                response.sendRedirect(request.getContextPath() + "/patient?action=viewProfile&message=Success");
            } else {
                response.sendRedirect(request.getContextPath() + "/patient?action=viewProfile&message=Failure");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/patient?action=viewProfile&message=Failure&reason=Appointment not found.");
        }
    }

    private void handleMarkCompleted(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("patient") == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        if (AppointmentDAO.markCompleted(appointmentId)) {
            response.sendRedirect(request.getContextPath() + "/patient?action=viewProfile&message=Success");
        } else {
            response.sendRedirect(request.getContextPath() + "/patient?action=viewProfile&message=Failure");
        }
    }
}