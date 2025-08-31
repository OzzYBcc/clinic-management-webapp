<%@ page import="main.java.dao.AvailabilityDAO" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  HttpSession session1 = request.getSession(false);
  if (session == null || session.getAttribute("patient") == null) {
    response.sendRedirect(request.getContextPath() + "/login.html");
    return;
  }
  // Χρήση TreeMap για αυτόματη χρονολογική ταξινόμηση
  Map<String, Map<String, String>> slotDetailsMap = new TreeMap<>(AvailabilityDAO.getAvailableSlotsWithDetails());
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Available Appointments</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 40px; }
    h2 { color: #333; }
    ul { list-style-type: none; padding: 0; }
    li { margin: 10px 0; padding: 10px; border: 1px solid #ddd; border-radius: 5px; }
    form { display: inline; }
    button { background-color: #4CAF50; color: white; border: none; padding: 5px 10px; cursor: pointer; }
    button:hover { background-color: #45a049; }
    a { color: #4CAF50; text-decoration: none; }
    a:hover { text-decoration: underline; }
  </style>
</head>
<body>
<h2>Available Appointments</h2>
<ul>
  <%
    if (!slotDetailsMap.isEmpty()) {
      for (Map.Entry<String, Map<String, String>> entry : slotDetailsMap.entrySet()) {
        String slot = entry.getKey();
        Map<String, String> details = entry.getValue();
        String doctorUsername = details.get("doctorUsername");
        String specialty = details.get("specialty");
        String doctorName = "Dr. " + doctorUsername;
  %>
  <li>
    Date & Time: <%= slot %> | Doctor: <%= doctorName %> | Specialty: <%= specialty %> |
    <form method="post" action="/Assignment2/patient">
      <input type="hidden" name="action" value="schedule">
      <input type="hidden" name="doctorUsername" value="<%= doctorUsername %>">
      <input type="hidden" name="datetime" value="<%= slot %>">
      <button type="submit" onclick="return confirm('Are you sure you want to book this appointment?');">Book</button>
    </form>
  </li>
  <%
    }
  } else {
  %>
  <li>No available appointments.</li>
  <%
    }
  %>
</ul>
<a href="/Assignment2/patient?action=viewProfile">Back to Profile</a>
</body>
</html>