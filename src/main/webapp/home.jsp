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
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
		<div class="container-fluid">
			<a class="navbar-brand" href="#"> <img
				src="images/email_icon.png" class="d-inline-block align-text-top" /></a>
			<div id="navbarSupportedContent">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item navbar-dark">
						<p class="mt-4" style="color: #fff">
							<%
							out.println(request.getAttribute("email"));
							%>
						</p>
					</li>
				</ul>
			</div>
			<form class="d-flex">
				<button class="btn btn-secondary btn me-2 mx-auto" type="button">
					<a href="login.html" style="color: #fff">Logout</a>
				</button>
			</form>
		</div>
	</nav>
	<div class="container-fluid">
		<div class="row">
			<div class="col me-1">
				<div class="d-flex flex-column flex-shrink-0 p-3 collapse"
					style="width: 280px">
					<ul class="nav nav-pills flex-column mb-auto">
						<li class="nav-item">
							<form action="NavigationServlet" method="post">
								<div class="row my-1">
									<input class="btn btn-primary" type="hidden" name="email"
										value="<%=request.getAttribute("email")%>"> <input
										type="hidden" name="password"
										value="<%=request.getAttribute("password")%>">
								</div>
								<div class="row my-1">
									<input class="btn btn-primary" type="submit" name="newMail"
										value="New Mail" />
								</div>
								<div class="row my-1">
									<input class="btn btn-primary" type="submit" name="inbox"
										value="Inbox" />
								</div>
								<div class="row my-1">
									<input class="btn btn-primary" type="submit" name="sent"
										value="Sent" />
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
