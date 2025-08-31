<%@ page import="main.java.basicClasses.*" %>
<%@ page import="main.java.dao.AppointmentDAO" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%
    Patient patient = (Patient) session.getAttribute("patient");
    if (patient == null) {
        response.sendRedirect(request.getContextPath() + "/login.html");
        return;
    }
    String message = request.getParameter("message");
    String reason = request.getParameter("reason"); // Νέο parameter για τον λόγο
    String displayMessage = "";
    if ("Success".equals(message)) {
        displayMessage = "✅ Action successful.";
    } else if ("Failure".equals(message)) {
        displayMessage = "❌ Action failed." + (reason != null ? " (" + reason + ")" : "");
    }
    List<Appointment> appointments = AppointmentDAO.getAppointmentsByPatient(patient.getUsername());
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Patient Profile</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        h2 { color: #333; }
        ul { list-style-type: none; padding: 0; }
        li { margin: 10px 0; padding: 10px; border: 1px solid #ddd; border-radius: 5px; }
        a { color: #4CAF50; text-decoration: none; }
        a:hover { text-decoration: underline; }
        .cancel-btn { background-color: #f44336; color: white; border: none; padding: 5px 10px; cursor: pointer; }
        .cancel-btn:hover { background-color: #d32f2f; }
        .complete-btn { background-color: #4CAF50; color: white; border: none; padding: 5px 10px; cursor: pointer; margin-left: 5px; }
        .complete-btn:hover { background-color: #45a049; }
        .message { margin: 10px 0; padding: 10px; border-radius: 5px; }
        .success { background-color: #dff0d8; color: #3c763d; }
        .error { background-color: #f2dede; color: #a94442; }
    </style>
</head>
<body>
<h2>Welcome, <%= patient.getName() %>!</h2>
<% if (!displayMessage.isEmpty()) { %>
<div class="message <%= displayMessage.contains("✅") ? "success" : "error" %>"><%= displayMessage %></div>
<% } %>
<h3>Upcoming Appointments:</h3>
<ul>
    <%
        for (Appointment appt : appointments) {
            if (!"CANCELLED".equals(appt.getStatus()) && !"COMPLETED".equals(appt.getStatus().toUpperCase())) {
    %>
    <li>
        Doctor: <%= appt.getDoctor().getUsername() %> |
        Date & Time: <%= appt.getDateTime() %> |
        Status: <%= appt.getStatus() %> |
        <form method="post" action="/Assignment2/patient" style="display:inline;">
            <input type="hidden" name="action" value="cancelAppointment">
            <input type="hidden" name="appointmentId" value="<%= appt.getId() %>">
            <button type="submit" class="cancel-btn" onclick="return confirm('Are you sure?');">Cancel</button>
        </form>
        <form method="post" action="/Assignment2/patient" style="display:inline;">
            <input type="hidden" name="action" value="markCompleted">
            <input type="hidden" name="appointmentId" value="<%= appt.getId() %>">
            <button type="submit" class="complete-btn" onclick="return confirm('Mark as completed?');">Completed</button>
        </form>
    </li>
    <%
            }
        }
        if (appointments.stream().noneMatch(appt -> !"CANCELLED".equals(appt.getStatus()) && !"COMPLETED".equals(appt.getStatus().toUpperCase()))) {
    %>
    <li>No upcoming appointments.</li>
    <%
        }
    %>
</ul>

<h3>Past Appointments:</h3>
<ul>
    <%
        for (Appointment appt : appointments) {
            if ("COMPLETED".equals(appt.getStatus().toUpperCase()) || "CANCELLED".equals(appt.getStatus())) {
    %>
    <li>
        Doctor: <%= appt.getDoctor().getUsername() %> |
        Date & Time: <%= appt.getDateTime() %> |
        Status: <%= appt.getStatus() %>
    </li>
    <%
            }
        }
        if (appointments.stream().noneMatch(appt -> "COMPLETED".equals(appt.getStatus().toUpperCase()) || "CANCELLED".equals(appt.getStatus()))) {
    %>
    <li>No past appointments.</li>
    <%
        }
    %>
</ul>

<a href="/Assignment2/patient?action=searchDoctor">Search Appointments</a> |
<a href="/Assignment2/login?action=logout">Logout</a>
</body>
</html>