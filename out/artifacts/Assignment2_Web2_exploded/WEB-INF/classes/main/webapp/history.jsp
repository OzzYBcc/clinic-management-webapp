<%@ page import="main.java.basicClasses.*" %>
<%@ page import="main.java.dao.AppointmentDAO" %>
<%@ page import="java.util.List" %>
<%
    Patient patient = (Patient) session.getAttribute("patient");
    if (patient == null) {
        response.sendRedirect(request.getContextPath() + "/login.html");
        return;
    }
    List<Appointment> appointments = AppointmentDAO.getAppointmentsByPatient(patient.getUsername());
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Appointment History</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        h2 { color: #333; }
        ul { list-style-type: none; padding: 0; }
        li { margin: 10px 0; padding: 10px; border: 1px solid #ddd; border-radius: 5px; }
        a { color: #4CAF50; text-decoration: none; }
        a:hover { text-decoration: underline; }
        .cancel-btn { background-color: #f44336; color: white; border: none; padding: 5px 10px; cursor: pointer; }
        .cancel-btn:hover { background-color: #d32f2f; }
        .no-appointments { color: #666; font-style: italic; }
    </style>
</head>
<body>
<h2>Appointment History for <%= patient.getName() %>:</h2>
<%
    if (appointments == null || appointments.isEmpty()) {
%>
<p class="no-appointments">No appointments found.</p>
<%
} else {
%>
<ul>
    <%
        for (Appointment appt : appointments) {
            String doctorUsername = (appt.getDoctor() != null) ? appt.getDoctor().getUsername() : "Unknown Doctor";
    %>
    <li>
        Doctor: <%= doctorUsername %> |
        Date & Time: <%= appt.getDateTime() %> |
        Status: <%= appt.getStatus() %> |
        <% if (!"CANCELLED".equals(appt.getStatus())) { %>
        <form method="post" action="/Assignment2/patient" style="display:inline;">
            <input type="hidden" name="action" value="cancelAppointment">
            <input type="hidden" name="appointmentId" value="<%= appt.getId() %>">
            <button type="submit" class="cancel-btn" onclick="return confirm('Are you sure you want to cancel this appointment?');">Cancel</button>
        </form>
        <% } %>
    </li>
    <%
        }
    %>
</ul>
<%
    }
%>
<a href="/Assignment2/profile.jsp">Back to Profile</a>
</body>
</html>