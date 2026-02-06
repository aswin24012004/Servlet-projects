package demo.learn.project.controller;

import com.google.gson.Gson;
import demo.learn.project.model.Book;
import demo.learn.project.utils.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/") 
public class BookServlet extends HttpServlet {
    
    private final Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        
        // Ensure all responses are JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            switch (action) {
                case "/insert":
                    insertBook(request, response);
                    break;
                case "/update":
                    updateBook(request, response);
                    break;
                case "/delete":
                    deleteBook(request, response);
                    break;
                case "/get":
                    getBook(request, response);
                    break;
                default:
                    listBooks(request, response);
                    break;
            }
        } catch (Exception ex) {
            sendError(response, ex.getMessage());
        }
    }

    private void listBooks(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Book> listBook = new ArrayList<>();
        String sql = "SELECT * FROM books";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                listBook.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getDouble("price")));
            }
            response.getWriter().print(gson.toJson(listBook));
            
        } catch (SQLException e) {
            sendError(response, "Database Error: " + e.getMessage());
        }
    }

    private void insertBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String priceParam = request.getParameter("price");

        if (title == null || author == null || priceParam == null) {
            sendError(response, "Missing parameters: title, author, and price are required.");
            return;
        }

        double price = Double.parseDouble(priceParam);
        String sql = "INSERT INTO books (title, author, price) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setDouble(3, price);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Book savedBook = new Book(rs.getInt(1), title, author, price);
                    sendResponse(response, "Book saved successfully", savedBook);
                }
            }
        } catch (SQLException e) {
            sendError(response, e.getMessage());
        }
    }
    private void getBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.isEmpty()) {
            sendError(response, "Book ID is required.");
            return;
        }

        int id = Integer.parseInt(idParam);
        String sql = "SELECT * FROM books WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price")
                    );
                    
                    response.getWriter().print(gson.toJson(book));
                } else {
                    sendError(response, "Book with ID " + id + " not found.");
                }
            }
        } catch (SQLException e) {
            sendError(response, "Database Error: " + e.getMessage());
        }
    }
    private void updateBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        double price = Double.parseDouble(request.getParameter("price"));

        String sql = "UPDATE books SET title = ?, author = ?, price = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, id);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                sendResponse(response, "Book updated successfully", new Book(id, title, author, price));
            } else {
                sendError(response, "Book with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            sendError(response, e.getMessage());
        }
    }

    private void deleteBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            
            Map<String, Object> res = new HashMap<>();
            res.put("status", rows > 0 ? "success" : "fail");
            res.put("message", rows > 0 ? "Book deleted" : "Book not found");
            response.getWriter().print(gson.toJson(res));
            
        } catch (SQLException e) {
            sendError(response, e.getMessage());
        }
    }

    // Helper method for Success Responses
    private void sendResponse(HttpServletResponse response, String message, Object data) throws IOException {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("message", message);
        res.put("data", data);
        response.getWriter().print(gson.toJson(res));
    }

    // Helper method for Error Responses
    private void sendError(HttpServletResponse response, String message) throws IOException {
        Map<String, String> res = new HashMap<>();
        res.put("status", "error");
        res.put("message", message);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().print(gson.toJson(res));
    }
}