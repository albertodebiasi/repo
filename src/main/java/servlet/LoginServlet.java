package servlet;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cookies.cookies;
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
		try {
			cookies.setCsrfToken(request,response);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		response.setContentType("text/html");
		
		String email = request.getParameter("email");
		String pwd = request.getParameter("password");
		byte[] salt = null;
		String sql = "SELECT * FROM user WHERE email=?";
		String sqlString = "SELECT * FROM user WHERE email=? AND password=?";
	
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, email);
			ResultSet sql1 = st.executeQuery();
			if (sql1.next()) {
				salt= sql1.getString(5).getBytes("Utf-8");
				System.out.println(pwd);
				pwd = hashing.generateHash(pwd, algorithm, salt);
				sql1.close();
				System.out.println("password: " + pwd);
				String salted = new String(salt);
				System.out.println("salted: " + salted);	
			}		
			PreparedStatement st1 = conn.prepareStatement(sqlString);
			st1.setString(1, email);
			st1.setString(2, pwd);
			ResultSet sqlRes = st.executeQuery();
			
			if (sqlRes.next()) {
				if(pwd.equals(sqlRes.getString(4))) {
					request.setAttribute("email", sqlRes.getString(3));
					request.setAttribute("password", sqlRes.getString(4));
					
					System.out.println("Login succeeded!");
					request.setAttribute("content", "");
					request.getRequestDispatcher("home.jsp").forward(request, response);
				
				}else {
					System.out.println("Login failed!");
					request.getRequestDispatcher("login.html").forward(request, response);
				}	
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