import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;


@WebServlet("/login")    
public class login extends HttpServlet{
  
	    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		public void doPost(HttpServletRequest request, HttpServletResponse response) 
	            throws ServletException, IOException {
	        
	        String user = request.getParameter("username");
	        String pass = request.getParameter("password");

	        if ("admin".equals(user) && "123".equals(pass)) {
	            
	            HttpSession session = request.getSession();
	            session.setAttribute("user", user);
	        	System.out.println("Login");
	            
	        } else {
	            // 5. Authentication failed
//	            response.sendRedirect("login.html?error=invalid");
	        	System.out.println(" not Login");
	        }
	    }
	}

