<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>layout 后台大布局 - Layui</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/LayUI/css/layui.css">
<script src="${pageContext.request.contextPath}/static/LayUI/layui.js"></script>
<script
	src="${pageContext.request.contextPath}/static/Jquery/jquery.min.js"></script>
</head>
<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header">
			<div class="layui-logo">layui 后台布局</div>
			<!-- 头部区域（可配合layui已有的水平导航） -->
			<ul class="layui-nav layui-layout-left">
				<li class="layui-nav-item"><a href="">控制台</a></li>
				<li class="layui-nav-item"><a href="">商品管理</a></li>
				<li class="layui-nav-item"><a href="">用户</a></li>
				<li class="layui-nav-item"><a href="javascript:;">其它系统</a>
					<dl class="layui-nav-child">
						<dd>
							<a href="">邮件管理</a>
						</dd>
						<dd>
							<a href="">消息管理</a>
						</dd>
						<dd>
							<a href="">授权管理</a>
						</dd>
					</dl></li>
			</ul>
			<ul class="layui-nav layui-layout-right">
				<li class="layui-nav-item"><a href="javascript:;"> <img
						src="http://t.cn/RCzsdCq" class="layui-nav-img">${user.userName }
				</a>
					<dl class="layui-nav-child">
						<dd>
							<a href="">基本资料</a>
						</dd>
						<dd>
							<a href="">安全设置</a>
						</dd>
					</dl></li>
				<li class="layui-nav-item"><a
					href="${pageContext.request.contextPath}/loginOutServlet">退了</a></li>
			</ul>
		</div>

		<div class="layui-side layui-bg-black">
			<div class="layui-side-scroll">
				<!-- 左侧导航区域（可配合layui已有的垂直导航） -->
				<ul class="layui-nav layui-nav-tree" lay-filter="test">
					<li class="layui-nav-item layui-nav-itemed"><a class=""
						href="####" id="userManage">用户管理</a></li>
					<li class="layui-nav-item"><a href="javascript:;">解决方案</a>
						<dl class="layui-nav-child">
							<dd>
								<a href="javascript:;">列表一</a>
							</dd>
							<dd>
								<a href="javascript:;">列表二</a>
							</dd>
							<dd>
								<a href="">超链接</a>
							</dd>
						</dl></li>
					<li class="layui-nav-item"><a href="####">云市场</a></li>
					<li class="layui-nav-item"><a href="">发布商品</a></li>
				</ul>
			</div>
		</div>

		<div class="layui-body">
			<div class="">
				<!-- 内容主体区域 -->
				<table id="demo" lay-filter="test">
				</table>
			</div>
		</div>

		<div class="layui-footer">
			<!-- 底部固定区域 -->
			© layui.com - 底部固定区域
		</div>
	</div>
	<script src="../src/layui.js"></script>
	<script>
		//Jquery 消息队列MQ???发布-订阅模式   有订阅的信息接收到之后通知所有订阅者==》》》OOP？？？面向对象编程
		//计算表格高度
		var windowHeight=$(window).height();
		var headerHeight=$(".layui-header").height();
		var footerHeight=$(".layui-footer").height();
		var tableHeight=windowHeight-headerHeight-footerHeight;
		$('#userManage').click(function(){
			layui.use('table', function(){
				  var table = layui.table;
				  //第一个实例
				  table.render({
				    elem: '#demo'
				    ,height: tableHeight
				    ,url: '${pageContext.request.contextPath}/getUserList.action' //数据接口
				    ,page: true //开启分页
				    ,cols: [[ //表头
				      {field: 'userId',checkbox:true}
				      ,{field: 'userName', title: '用户名', width:130}
				      ,{field: 'userTell', title: '手机号', width: 200}
				      ,{field: 'department', title: '公寓', width: 140}
				      ,{field: 'createTime', title: '创建时间', width:120}
				      ,{field: 'type', title: '用户类型', width: 135}
				    ]]
				  });
				  
				});
		})
		//JavaScript代码区域
		layui.use('element', function() {
			var element = layui.element;

		});
	</script>
</body>
</html>