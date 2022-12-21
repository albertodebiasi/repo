package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import cookies.cookies;

import java.io.IOException;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    public LogoutServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            cookies.checkCsrf(request);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            request.getRequestDispatcher("/login.html").forward(request, response);
            e.printStackTrace();
        }
        
        cookies.deleteCookie(request, response);
        request.getSession().invalidate();
        request.getRequestDispatcher("/login.html").forward(request, response);
    }
}