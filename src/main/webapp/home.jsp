<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi"
	crossorigin="anonymous" />
<title>Home page</title>
<link rel="icon" type="image/png" href="images/icons8-email-96.png">
</head>
<header>
	<nav
		class="navbar navbar-expand-lg navbar-dark bd-navbar sticky-top bg-primary align-content-between">
		<div class="container-fluid mx-5 align-content-center">
			<p class="mb-0 text-center" style="color: #fff">
				Welcome:	
				<%
				out.println(request.getAttribute("email"));
				%>
			</p>
		</div>
	</nav>
	<nav class="navbar navbar-expand-lg navbar-dark flex-wrap"
		style="background-color: dodgerblue">
		<div class="container-fluid mx-5">
			<a class="navbar-brand"> <img src="images/icons8-email-96.png"
				class="d-inline-block align-text-top" width="50 px" height="50 px"/></a>
			<form class="d-flex" action="login.html">
				<input class="btn btn-primary btn me-2 mx-auto" type="submit"
					value="Logout"></input>
			</form>
		</div>
	</nav>
</header>
<body>
	<div class="container-fluid mx-5">
		<div class="row">
			<div class="col me-1">
				<div class="d-flex flex-column flex-shrink-0 p-3 collapse"
					style="width: 280px">
					<ul class="nav nav-pills flex-column mb-auto">
						<li class="nav-item">
							<form action="NavigationServlet" method="post">
								<div class="row my-1">
									<input class="btn" style="background-color: dodgerblue"
										type="hidden" name="email"
										value="<%=request.getAttribute("email")%>"> <input
										type="hidden" name="password"
										value="<%=request.getAttribute("password")%>">
								</div>
								<div class="row my-1">
									<input class="btn"
										style="background-color: dodgerblue; color: #fff;"
										type="submit" name="newMail" value="New Mail" />
								</div>
								<div class="row my-1">
									<input class="btn"
										style="background-color: dodgerblue; color: #fff;"
										type="submit" name="inbox" value="Inbox" />
								</div>
								<div class="row my-1">
									<input class="btn"
										style="background-color: dodgerblue; color: #fff;"
										type="submit" name="sent" value="Sent" />
								</div>
							</form>
						</li>
					</ul>
				</div>
			</div class="col col-8 me-5">
			<%=request.getAttribute("content") != null ? request.getAttribute("content") : ""%>
			<div class="col col-1"></div>
		</div>
	</div>
</body>
</html>
