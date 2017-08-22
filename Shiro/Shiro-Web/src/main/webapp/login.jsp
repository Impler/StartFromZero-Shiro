<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
</head>
<body>
	<%
		String[] names = ((org.apache.shiro.web.servlet.ShiroHttpSession)session).getValueNames();
		for(String name : names)
		out.println(name + ":" + session.getAttribute(name));
	%>
	<br />
	<form action="" method="post">
		<label>用户名：</label> <input type="text" name="userName"> <br />
		<label>密码：</label> <input type="password" name="passWord"> <br />
		<input type="submit" value="登录" />
	</form>
</body>
</html>