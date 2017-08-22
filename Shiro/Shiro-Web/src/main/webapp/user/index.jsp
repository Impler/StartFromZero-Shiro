<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
WELCOME ${currUser.username}<br />
<%
	String[] names = ((org.apache.shiro.web.servlet.ShiroHttpSession)session).getValueNames();
	for(String name : names)
	out.println(name + ":" + session.getAttribute(name));
%>
<br />
<a href="../logout">退出</a>
</body>
</html>