<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/citypicker/js/city-picker.data.js"></script>
<script type="text/javascript" src="../js/citypicker/js/city-picker.js"></script>
<link rel="stylesheet" type="text/css" href="../js/citypicker/css/city-picker.css">
</head>
<body>
请选择省市区：
<div style="position: relative;"><!-- container -->
  <input readonly id="area" type="text" data-toggle="city-picker" style="width:30%;" placeholder="点击从下拉面板中选择省/市/区" data-simple="true">
</div>
<hr/>
<input type="button" value="切换城市" id="changeCity"/>

<script type="text/javascript">
	$(function(){
		$("#changeCity").click(function(){
			//重置citypicker
			$("#area").citypicker("reset");
			//销毁citypicker
			$("#area").citypicker("destroy");
			
			//修改citypicker值
			$("#area").citypicker({
				province:"广东省",
				city:"广州市",
				district:"天河区"
			});
		});
	});
</script>
</body>
</html>