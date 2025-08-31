<%@ page import="main.java.basicClasses.Admin" %>
<%@ page import="main.java.basicClasses.Doctor" %>
<%@ page import="main.java.dao.UserDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.*" %>

<%
    Admin admin = (Admin) session.getAttribute("admin");
    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/login.html");
        return;
    }

    // Fetch real list of doctors from database
    List<Doctor> doctors = new java.util.ArrayList<>();
    String query = "SELECT username, hashed_password, salt, name, surname, age, specialty FROM users WHERE role = 'doctor'";
    try (Connection conn = main.java.dao.DBUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String username = rs.getString("username");
            String hashedPassword = rs.getString("hashed_password");
            String salt = rs.getString("salt");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            int age = rs.getInt("age");
            String specialty = rs.getString("specialty");
            doctors.add(new Doctor(username, hashedPassword, name, surname, age, specialty, salt));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Get message from request
    String message = request.getParameter("message");
    String action = request.getParameter("action");
    String displayMessage = "";
    if ("Success".equals(message)) {
        displayMessage = ("removeDoctor".equals(action))
                ? "✅ Doctor removed successfully!"
                : "✅ User added successfully!";
    } else if ("Failure".equals(message)) {
        displayMessage = "❌ Failed to add user or remove doctor.";
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Panel</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        h2, h3 { color: #333; }
        form { max-width: 300px; margin: 20px 0; }
        label { display: block; margin: 10px 0 5px; }
        input, button { width: 100%; padding: 8px; margin: 5px 0; box-sizing: border-box; }
        button { background-color: #4CAF50; color: white; border: none; cursor: pointer; }
        button:hover { background-color: #45a049; }
        ul { list-style-type: none; padding: 0; }
        li { margin: 10px 0; padding: 10px; border: 1px solid #ddd; border-radius: 5px; }
        a { color: #4CAF50; text-decoration: none; }
        a:hover { text-decoration: underline; }
        .message { margin: 10px 0; padding: 10px; border-radius: 5px; text-align: center; }
        .success { background-color: #dff0d8; color: #3c763d; }
        .error { background-color: #f2dede; color: #a94442; }
    </style>
</head>
<body>
<h2>Welcome, <%= admin.getName() %>!</h2>
<% if (!displayMessage.isEmpty()) { %>
<div class="message <%= displayMessage.contains("✅") ? "success" : "error" %>"><%= displayMessage %></div>
<% } %>

<h3>Add Doctor</h3>
<form method="post" action="admin">
    <input type="hidden" name="action" value="addDoctor">
    <label>Username: <input type="text" name="username" required></label>
    <label>Password: <input type="password" name="password" required></label>
    <label>First Name: <input type="text" name="name" required></label>
    <label>Last Name: <input type="text" name="surname" required></label>
    <label>Age: <input type="number" name="age" required></label>
    <label>Specialty: <input type="text" name="specialty" required></label>
    <label>AMKA: <input type="text" name="amka" required placeholder="e.g., 12345678901"></label>
    <button type="submit">Add Doctor</button>
</form>

<h3>Add User</h3>
<form method="post" action="admin">
    <input type="hidden" name="action" value="addUser">
    <label>Username: <input type="text" name="username" required></label>
    <label>Password: <input type="password" name="password" required></label>
    <label>First Name: <input type="text" name="name" required></label>
    <label>Last Name: <input type="text" name="surname" required></label>
    <label>Age: <input type="number" name="age" required></label>
    <label>Role: <select name="role" required>
        <option value="patient">Patient</option>
        <option value="admin">Admin</option>
    </select></label>
    <label>AMKA: <input type="text" name="amka" required placeholder="e.g., 12345678901"></label>
    <button type="submit">Add User</button>
</form>

<h3>Remove Doctor</h3>
<form method="post" action="admin">
    <input type="hidden" name="action" value="removeDoctor">
    <label>Doctor Username: <input type="text" name="doctorUsername" required></label>
    <button type="submit">Remove Doctor</button>
</form>

<h3>Doctor List</h3>
<ul>
    <% for (Doctor doc : doctors) { %>
    <li>Dr. <%= doc.getName() %> <%= doc.getSurname() %> (Specialty: <%= doc.getSpecialty() %>)</li>
    <% } %>
    <% if (doctors.isEmpty()) { %>
    <li>No doctors registered.</li>
    <% } %>
</ul>

<a href="login?action=logout">Logout</a>
</body>
</html>