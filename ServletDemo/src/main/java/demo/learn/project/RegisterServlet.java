package demo.learn.project;
//import com.google.code.gson.Gson;


import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet  {
 
	private static final long serialVersionUID = 1L;
 
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	    	
			String username=request.getParameter("username");
			String email=request.getParameter("email");
			String pass=request.getParameter("password");
			
	        response.setContentType("application/json");
	        UserDAO user=new UserDAO();
	        try {
				user.registerUser(username, email, pass);
				response.getWriter().println("{\"status\":\"success\""
						+ "{\"message\":\"user registered\"}");
				response.getWriter().println(user);
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().println("{\"status\":\"failed\""
						+ ",\"message\":\"user not registered\"}");
			}
	        
	        
	    }
	}