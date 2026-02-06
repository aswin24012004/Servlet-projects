package demo.learn.project;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

  
@WebServlet(name="LoginServlet" ,value="/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		String username=request.getParameter("username");
		String pass=request.getParameter("password");
        response.setContentType("application/json");
        UserDAO userDAO=new UserDAO();
        User user=new User();
        
        try {
			user=userDAO.login(username,pass);
			if(user.getUsername().equalsIgnoreCase(username))
			response.getWriter().println("{\"status\":\"success\""
					+ "\"message\":\"user Login Successfull\"}");
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("{\"status\":\"failed\""
					+ ",\"message\":\"no user found\"}");
		}

    }
}

