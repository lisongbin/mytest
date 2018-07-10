<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	<style type="text/css">
	body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
	</style>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=bUiOQDOyAu6gKuLbMeTBs8pP4T42bGk2"></script>
	<title>地图展示</title>
</head>
<body>
	<div id="allmap"></div>
</body>
</html>
<script type="text/javascript">
	// 百度地图API功能
	var map = new BMap.Map("allmap");    // 创建Map实例
	var point = new BMap.Point(113.434439,23.13526);
	map.centerAndZoom(point, 18);  // 初始化地图,设置中心点坐标和地图级别

	//创建一个文字标签
	/* var opts = {
			  position : point,    // 指定文本标注所在的地理位置
			  offset   : new BMap.Size(30, -30)    //设置文本偏移量
			}
	var label = new BMap.Label("欢迎使用百度地图，这是一个简单的文本标注哦~", opts);  // 创建文本标注对象
				label.setStyle({
					 color : "blue",
					 fontSize : "20px",
					 height : "20px",
					 lineHeight : "20px",
					 fontFamily:"微软雅黑"
				 });
			
	//在地图添加文字标签
	map.addOverlay(label);    */
	
	
	//创建小狐狸
	var myIcon = new BMap.Icon("http://cms-bucket.nosdn.127.net/catchpic/3/34/34e31342095a467229c8eef9134ceca9.png?imageView&thumbnail=550x0", new BMap.Size(300,157));
	var marker2 = new BMap.Marker(point,{icon:myIcon});  // 创建标注
	
	map.addOverlay(marker2);              // 将标注添加到地图中
</script>
