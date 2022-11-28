package servlet;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import salt.hashing;
import java.security.NoSuchAlgorithmException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.Util;

/**
 * Servlet implementation class HelloWorldServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static Connection conn;
	String algorithm = "MD5";

	/**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }
    
    public void init() throws ServletException {
    	conn = Util.initDbConnection();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		String email = request.getParameter("email");
		String pwd = request.getParameter("password");
		byte[] salt = null;
		
		try (Statement st = conn.createStatement()) {
			ResultSet sql1 = st.executeQuery(
				"SELECT * "
				+ "FROM user "
				+ "WHERE email='" + email + "' "
			);
			if (sql1.next()) {
				salt= sql1.getString(5).getBytes("Utf-8");
				sql1.close();
				pwd = hashing.generateHash(pwd, algorithm, salt);
				System.out.println("password: " + pwd);
				String salted = new String(salt);
				System.out.println("salted: " + salted);	
			}		

			ResultSet sqlRes = st.executeQuery(
				"SELECT * "
				+ "FROM user "
				+ "WHERE email='" + email + "' "
					+ "AND password='" + pwd + "'"
			);
			
			if (sqlRes.next()) {
				request.setAttribute("email", sqlRes.getString(3));
				request.setAttribute("password", sqlRes.getString(4));
				
				System.out.println("Login succeeded!");
				request.setAttribute("content", "");
				request.getRequestDispatcher("home.jsp").forward(request, response);
				
				
			} else {
				System.out.println("Login failed!");
				request.getRequestDispatcher("login.html").forward(request, response);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			request.getRequestDispatcher("login.html").forward(request, response);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}