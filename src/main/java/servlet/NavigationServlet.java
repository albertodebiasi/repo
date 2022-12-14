package servlet;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.owasp.encoder.Encode;

import cookies.cookies;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.Util;

/**
 * Servlet implementation class NavigationServlet
 */
@WebServlet("/NavigationServlet")
public class NavigationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Connection conn;
	String sql= "SELECT * FROM mail WHERE receiver=? ORDER BY time DESC";
	String sql1= "SELECT * FROM mail WHERE receiver=? AND sender=? ORDER BY time DESC";
	String sql2= "SELECT * FROM mail WHERE sender=? ORDER BY time DESC";
	String sql3= "SELECT * FROM mail WHERE sender=? AND receiver=? ORDER BY time DESC";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NavigationServlet() {
		super();
	}

	public void init() throws ServletException {
		conn = Util.initDbConnection();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		cookies.checkCsrf(request);

		response.setContentType("text/html");

		String email = request.getParameter("email").replace("'", "''");
		String pwd = request.getParameter("password").replace("'", "''");

		if (request.getParameter("newMail") != null)
			request.setAttribute("content", getHtmlForNewMail(email, pwd));
		else if (request.getParameter("inbox") != null)
			request.setAttribute("content", getHtmlForInbox(email, pwd, request.getParameter("search")));
		else if (request.getParameter("sent") != null)
			request.setAttribute("content", getHtmlForSent(email, pwd, request.getParameter("search")));

		request.setAttribute("email", email);
		request.getRequestDispatcher("home.jsp").forward(request, response);
	}

	private String getHtmlForNewMail(String email, String pwd) {
		return "<div class=\"col col-8 mt-4\"><form id=\"submitForm\" class=\"form-resize\" action=\"SendMailServlet\" method=\"post\">\r\n"
				+ "		<input type=\"hidden\" name=\"email\" value=\"\"" + email + "\">\r\n"
				+ "		<input type=\"hidden\" name=\"password\" value=\"\"" + pwd + "\">\r\n"
				+ "		<div class=\"col my-1\"><input class=\"form-control\" type=\"email\" name=\"receiver\" placeholder=\"Receiver\" value=\"\" required></div>\r\n"
				+ "		<div class=\"col my-1\"><input class=\"form-control\" type=\"text\"  name=\"subject\" placeholder=\"Subject\" value=\"\" required></div>\r\n"
				+ "		<div class=\"col my-1\"><textarea class=\"form-control\" name=\"body\" placeholder=\"Body\" wrap=\"hard\" required></textarea></div>\r\n"
				+ "		<input class=\"btn\" style=\"background-color:dodgerblue; color: #fff;\" type=\"submit\" name=\"sent\" value=\"Send\">\r\n"
				+ "		<input class=\"ms-3\" type=\"checkbox\" id=\"check\" name=\"check\" value=\"check\">\n"
				+ "  <label for=\"check\"> Digitally Sign!</label><br><br>\n"
				+ "	</form></div>";
	}

	private String getHtmlForInbox(String receiver, String password, String sender) {
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, receiver);
			ResultSet sqlRes;
			if (sender == null) {
				sqlRes = st.executeQuery();
			} else {
				PreparedStatement st1 = conn.prepareStatement(sql1);
				st1.setString(1, receiver);
				st1.setString(2, sender);
				sqlRes = st.executeQuery();
			}

			StringBuilder output = new StringBuilder();
			output.append("<div class=\"col-8 mt-4\">\r\n");

			output.append("<div class=\"col col-8 mb-3\"><form action=\"NavigationServlet\" method=\"post\">\r\n");
			output.append("		<input type=\"hidden\" name=\"email\" value=\"" + receiver + "\">\r\n");
			output.append("		<input type=\"hidden\" name=\"password\" value=\"" + password + "\">\r\n");
			output.append("		<div class=\"row my-1\"><div class=\"col-auto\"><input class=\"form-control\" type=\"text\" placeholder=\"Search for sender\" name=\"search\" value=\"\"required></input></div>\r\n");
			output.append("		<div class=\"col\"><input class=\"btn\" style=\"background-color:dodgerblue; color: #fff;\" type=\"submit\" name=\"inbox\" value=\"Search\"></div></div>\r\n");
			output.append("</form></div>\r\n");

			if (sender != null)
				output.append("<p>You searched for: " + Encode.forHtml(sender) + "</p>\r\n");

			while (sqlRes.next()) {
				output.append("<div style=\"white-space: pre-wrap;\"><span style=\"color:grey;\">");
				output.append("FROM:&emsp;" + sqlRes.getString(1) + "&emsp;&emsp;AT:&emsp;" + sqlRes.getString(5));
				output.append("</span>");
				output.append("<br><b>" + sqlRes.getString(3) + "</b>\r\n");
				output.append("<br>" + sqlRes.getString(4));
				output.append("</div>\r\n");

				output.append("<hr style=\"border-top: 2px solid black;\">\r\n");
			}

			output.append("</div>");

			return output.toString();

		} catch (SQLException e) {
			e.printStackTrace();
			return "ERROR IN FETCHING INBOX MAILS!";
		}
	}

	private String getHtmlForSent(String sender, String password, String receiver) {
		try{
			PreparedStatement st = conn.prepareStatement(sql2);
			st.setString(1, sender);
			ResultSet sqlRes;
			if (receiver == null) {
				sqlRes = st.executeQuery();
			} else {
				PreparedStatement st1 =conn.prepareStatement(sql3);
				st1.setString(1, sender);
				st1.setString(2,receiver);
				sqlRes = st.executeQuery();
			}
			StringBuilder output = new StringBuilder();
			output.append("<div class=\"col-8 mt-4\">\r\n");

			output.append("<div class=\"col col-8 mb-3\"><form action=\"NavigationServlet\" method=\"post\">\r\n");
			output.append("		<input type=\"hidden\" name=\"email\" value=\"" + sender + "\">\r\n");
			output.append("		<input type=\"hidden\" name=\"password\" value=\"" + password + "\">\r\n");
			output.append(
					"		<div class=\"row my-1\"><div class=\"col-auto\"><input class=\"form-control\" type=\"text\" placeholder=\"Search for receiver\" name=\"search\" value=\"\" required></div>\r\n");
			output.append("		<div class=\"col\"><input class=\"btn\" style=\"background-color:dodgerblue; color: #fff;\" type=\"submit\" name=\"sent\" value=\"Search\"></div></div>\r\n");
			output.append("</form></div>\r\n");

			if (receiver != null)
				output.append("<p>You searched for: " + Encode.forHtml(receiver) + "</p>\r\n");

			while (sqlRes.next()) {
				output.append("<div style=\"white-space: pre-wrap;\"><span style=\"color:grey;\">");
				output.append("TO:&emsp;" + sqlRes.getString(2) + "&emsp;&emsp;AT:&emsp;" + sqlRes.getString(5));
				output.append("</span>");
				output.append("<br><b>" + sqlRes.getString(3) + "</b>\r\n");
				output.append("<br>" + sqlRes.getString(4));
				output.append("</div>\r\n");

				output.append("<hr style=\"border-top: 2px solid black;\">\r\n");
			}

			output.append("</div>");

			return output.toString();

		} catch (SQLException e) {
			e.printStackTrace();
			return "ERROR IN FETCHING INBOX MAILS!";
		}
	}
}
