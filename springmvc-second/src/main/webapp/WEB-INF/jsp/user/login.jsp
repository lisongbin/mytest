<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>登录页面</title>

</head>
<body>

	<form id="userForm"
		action="${pageContext.request.contextPath }/user/login.do"
		method="post">

		<table width="100%" border=1>
			<tr>
				<td>用户名</td>
				<td><input type="text" name="username" value="" /></td>
			</tr>
			<tr>
				<td>密码</td>
				<td><input type="password" name="userpwd" value="" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit" value="提交" />
				</td>
			</tr>
		</table>

	</form>
</body>

</html>