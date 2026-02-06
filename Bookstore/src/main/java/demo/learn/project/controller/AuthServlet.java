package demo.learn.project.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import demo.learn.project.utils.DBConnection;

@WebServlet("/login")
public class AuthServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String pass = request.getParameter("password");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        
        String sql = "SELECT id, username, role FROM users WHERE email=? AND password=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setString(2, pass);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 1. Create or already there  the session
                    HttpSession session = request.getSession();
                    
                    // 2. Store user specific data in the session
                    int userId = rs.getInt("id");
                    String username = rs.getString("username");
                    String role = rs.getString("role");
                    
                    session.setAttribute("userId", userId);
                    session.setAttribute("username", username);
                    session.setAttribute("role", role);

                    // 3. Send success response
                    response.getWriter().print(String.format(
                        "{\"status\": \"success\", \"message\": \"Welcome %s\", \"userId\": %d, \"role\": \"%s\"}",
                        username, userId, role
                    ));
                    
                } else {
                    // Invalid credentials
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().print("{\"status\": \"error\", \"message\": \"Invalid email or password\"}");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"status\": \"error\", \"message\": \"Database connection failed\"}");
        }
    }

    // Optional: Add a doGet for logout or status check
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Logs the user out
            response.getWriter().print("{\"message\": \"Logged out successfully\"}");
        } else {
            response.getWriter().print("{\"message\": \"No active session found\"}");
        }
    }
}