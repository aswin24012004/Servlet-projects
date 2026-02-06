package demo.learn.project.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session != null && "ADMIN".equals(session.getAttribute("role"))) {
            chain.doFilter(request, response);
        } else {
            res.setStatus(403);
            res.getWriter().print("{\"error\": \"Admin access required\"}");
        }
    }
}