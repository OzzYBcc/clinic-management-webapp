<%@ page import="main.java.basicClasses.*" %>
<%@ page import="main.java.dao.AvailabilityDAO" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%
  Doctor doctor = (Doctor) session.getAttribute("doctor");
  if (doctor == null) {
    response.sendRedirect(request.getContextPath() + "/login.html");
    return;
  }
  String message = "";
  if ("POST".equalsIgnoreCase(request.getMethod())) {
    String date = request.getParameter("date");
    String startTime = request.getParameter("startTime");
    String endTime = request.getParameter("endTime");
    if (date != null && startTime != null && endTime != null && !date.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty()) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      String slot = date + " " + startTime + " to " + date + " " + endTime;
      try {
        sdf.parse(date + " " + startTime);
        sdf.parse(date + " " + endTime);
        if (AvailabilityDAO.addAvailability(doctor.getUsername(), slot)) {
          message = "✅ Availability added successfully.";
        } else {
          message = "❌ Failed to add availability. Please check the console for details.";
        }
      } catch (Exception e) {
        message = "❌ Invalid date or time format.";
      }
    } else {
      message = "❌ All fields are required.";
    }
  }
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Register Availability</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 40px; }
    h2 { color: #333; }
    form { max-width: 400px; }
    label { display: block; margin: 10px 0 5px; }
    input { width: 100%; padding: 5px; margin-bottom: 10px; }
    button { background-color: #4CAF50; color: white; border: none; padding: 10px 20px; cursor: pointer; }
    button:hover { background-color: #45a049; }
    .message { margin: 10px 0; padding: 10px; border-radius: 5px; }
    .success { background-color: #dff0d8; color: #3c763d; }
    .error { background-color: #f2dede; color: #a94442; }
  </style>
</head>
<body>
<h2>Register Availability for <%= doctor.getName() %>:</h2>
<% if (!message.isEmpty()) { %>
<div class="message <%= message.startsWith("✅") ? "success" : "error" %>"><%= message %></div>
<% } %>
<form method="post" action="registerAvailability.jsp">
  <label for="date">Date (YYYY-MM-DD):</label>
  <input type="date" id="date" name="date" value="<%= new SimpleDateFormat("yyyy-MM-dd").format(new Date()) %>" required>
  <label for="startTime">Start Time (HH:MM):</label>
  <input type="time" id="startTime" name="startTime" required>
  <label for="endTime">End Time (HH:MM):</label>
  <input type="time" id="endTime" name="endTime" required>
  <button type="submit">Submit Availability</button>
</form>
<a href="/Assignment2/doctorProfile.jsp">Back to Profile</a>
</body>
</html>