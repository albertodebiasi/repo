package servlet;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import salt.hashing;
import rsa.rsa;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.Util;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Connection conn;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
	}

	public void init() throws ServletException {
		conn = Util.initDbConnection();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String algorithm = "MD5";

		// The replacement escapes apostrophe special character in order to store it in
		// SQL
		String name = request.getParameter("name").replace("'", "''");
		String surname = request.getParameter("surname").replace("'", "''");
		String email = request.getParameter("email").replace("'", "''");
		String pwd = request.getParameter("password").replace("'", "''");
		byte[] saltInput = hashing.createSalt();
		String salt = hashing.bytesToStringHex(saltInput);
		byte[] salted = salt.getBytes();
		String hashedPassword = null;
		KeyPair keyPairGen = null;
		PublicKey publicKey = null;
		PrivateKey privateKey = null;
		String sql = "SELECT * FROM user WHERE email=?";
		
		try {
			hashedPassword = hashing.generateHash(pwd, algorithm, salted);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		rsa rsa = new rsa();
		try {
			keyPairGen = rsa.keyPairGen();
			publicKey = keyPairGen.getPublic();
			privateKey = keyPairGen.getPrivate();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, email);
			ResultSet sqlRes = st.executeQuery();

			if (sqlRes.next()) {
				System.out.println("Email already registered!");
				request.getRequestDispatcher("register.html").forward(request, response);

			} else {
				st.execute(
						"INSERT INTO user ( name, surname, email, password, salt, privatekey, publickey) "
								+ "VALUES ( '" + name + "', '" + surname + "', '" + email + "', '" + hashedPassword + "', '" + salt + "', '" + privateKey + "', '" + publicKey + "' )");

				request.setAttribute("email", email);
				request.setAttribute("password", pwd);

				System.out.println("Registration succeeded!");
				request.getRequestDispatcher("home.jsp").forward(request, response);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			request.getRequestDispatcher("register.html").forward(request, response);
		}
	}
}