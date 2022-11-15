package servlet;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		return 
			"<form id=\"submitForm\" class=\"form-resize\" action=\"SendMailServlet\" method=\"post\">\r\n"
			+ "		<input type=\"hidden\" name=\"email\" value=\""+email+"\">\r\n"
			+ "		<input type=\"hidden\" name=\"password\" value=\""+pwd+"\">\r\n"
			+ "		<input class=\"single-row-input\" type=\"email\" name=\"receiver\" placeholder=\"Receiver\" required>\r\n"
			+ "		<input class=\"single-row-input\" type=\"text\"  name=\"subject\" placeholder=\"Subject\" required>\r\n"
			+ "		<textarea class=\"textarea-input\" name=\"body\" placeholder=\"Body\" wrap=\"hard\" required></textarea>\r\n"
			+ "		<input type=\"submit\" name=\"sent\" value=\"Send\">\r\n"
			+ "	</form>";
	}

	private String getHtmlForInbox(String receiver, String password, String sender) {
		try (Statement st = conn.createStatement()) {
			ResultSet sqlRes;
			if (sender == null) {
				sqlRes = st.executeQuery(
					"SELECT * FROM mail "
					+ "WHERE receiver='" + receiver + "'"
					+ "ORDER BY time DESC"
				);
			} else {
				sqlRes = st.executeQuery(
					"SELECT * FROM mail "
					+ "WHERE receiver='" + receiver + "'"
						+ "AND sender='" + sender + "'"
					+ "ORDER BY time DESC"
				);
			}
			
			StringBuilder output = new StringBuilder();
			output.append("<div>\r\n");
			
			output.append("<form action=\"NavigationServlet\" method=\"post\">\r\n");
			output.append("		<input type=\"hidden\" name=\"email\" value=\""+receiver+"\">\r\n");
			output.append("		<input type=\"hidden\" name=\"password\" value=\""+password+"\">\r\n");
			output.append("		<input type=\"text\" placeholder=\"Search for sender\" name=\"search\" required>\r\n");
			output.append("		<input type=\"submit\" name=\"inbox\" value=\"Search\">\r\n");
			output.append("</form>\r\n");
			
			if (sender != null)
				output.append("<p>You searched for: " + sender + "</p>\r\n");
			
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
		try (Statement st = conn.createStatement()) {
			ResultSet sqlRes;
			if (receiver == null) {
				sqlRes = st.executeQuery(
					"SELECT * FROM mail "
					+ "WHERE sender='" + sender + "'"
					+ "ORDER BY time DESC"
				);
			} else {
				sqlRes = st.executeQuery(
					"SELECT * FROM mail "
					+ "WHERE sender='" + sender + "'"
						+ "AND receiver='" + receiver + "'"
					+ "ORDER BY time DESC"
				);
			}
			StringBuilder output = new StringBuilder();
			output.append("<div>\r\n");
			
			output.append("<form action=\"NavigationServlet\" method=\"post\">\r\n");
			output.append("		<input type=\"hidden\" name=\"email\" value=\""+sender+"\">\r\n");
			output.append("		<input type=\"hidden\" name=\"password\" value=\""+password+"\">\r\n");
			output.append("		<input type=\"text\" placeholder=\"Search for receiver\" name=\"search\" required>\r\n");
			output.append("		<input type=\"submit\" name=\"sent\" value=\"Search\">\r\n");
			output.append("</form>\r\n");
			
			if (receiver != null)
				output.append("<p>You searched for: " + receiver + "</p>\r\n");
			
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
