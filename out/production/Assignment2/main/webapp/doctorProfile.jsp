<%@ page import="main.java.basicClasses.*" %>
<%@ page import="main.java.dao.AppointmentDAO" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  Doctor doctor = (Doctor) session.getAttribute("doctor");
  if (doctor == null) {
    response.sendRedirect(request.getContextPath() + "/login.html");
    return;
  }
  // Κλήση της removeCompletedAppointments όταν φορτώνεται το JSP
  AppointmentDAO.removeCompletedAppointments(doctor.getUsername());
  System.out.println("DEBUG: removeCompletedAppointments called in doctorProfile.jsp for: " + doctor.getUsername());

  String message = request.getParameter("message");
  String displayMessage = "";
  if ("Success".equals(message)) {
    displayMessage = "✅ Appointment cancelled.";
  } else if ("Failure".equals(message)) {
    displayMessage = "❌ You cannot cancel an appointment less than 3 days prior to it!";
  }
  List<Appointment> appointments = AppointmentDAO.getAppointmentsByDoctor(doctor.getUsername());
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Doctor Profile</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 40px; }
    h2 { color: #333; }
    ul { list-style-type: none; padding: 0; }
    li { margin: 10px 0; padding: 10px; border: 1px solid #ddd; border-radius: 5px; }
    a { color: #4CAF50; text-decoration: none; }
    a:hover { text-decoration: underline; }
    .cancel-btn { background-color: #f44336; color: white; border: none; padding: 5px 10px; cursor: pointer; }
    .cancel-btn:hover { background-color: #d32f2f; }
    .message { margin: 10px 0; padding: 10px; border-radius: 5px; }
    .success { background-color: #dff0d8; color: #3c763d; }
    .error { background-color: #f2dede; color: #a94442; }
  </style>
</head>
<body>
<h2>Welcome, <%= doctor.getName() %>!</h2>
<% if (!displayMessage.isEmpty()) { %>
<div class="message <%= displayMessage.contains("✅") ? "success" : "error" %>"><%= displayMessage %></div>
<% } %>
<h3>Appointments:</h3>
<ul>
  <%
    for (Appointment appt : appointments) {
      if (!"CANCELLED".equals(appt.getStatus())) {
  %>
  <li>
    Patient: <%= appt.getPatient().getUsername() %> |
    Date & Time: <%= appt.getDateTime() %> |
    Status: <%= appt.getStatus() %> |
    <form method="post" action="/Assignment2/doctor" style="display:inline;">
      <input type="hidden" name="action" value="cancelAppointment">
      <input type="hidden" name="appointmentId" value="<%= appt.getId() %>">
      <button type="submit" class="cancel-btn" onclick="return confirm('Are you sure you want to cancel this appointment?');">Cancel</button>
    </form>
  </li>
  <%
      }
    }
    if (appointments.isEmpty()) {
  %>
  <li>No appointments scheduled.</li>
  <%
    }
  %>
</ul>
<a href="/Assignment2/doctor?action=registerAvailability">Register Availability</a> |
<a href="/Assignment2/login?action=logout">Logout</a>
</body>
</html>