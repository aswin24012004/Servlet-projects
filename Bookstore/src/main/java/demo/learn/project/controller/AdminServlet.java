package demo.learn.project.controller;

import com.google.gson.Gson;
import demo.learn.project.model.User;
import demo.learn.project.utils.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/admin/users")
public class AdminServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, username, email, role FROM users")) {
            while (rs.next()) {
                users.add(
                		new User
                		(
                		rs.getInt("id"),
                		rs.getString("username"),
                		rs.getString("email"), 
                		rs.getString("role")
                		)
                	);
            }
            res.getWriter().print(gson.toJson(users));
        } catch (Exception e) { e.printStackTrace(); }
    }
}