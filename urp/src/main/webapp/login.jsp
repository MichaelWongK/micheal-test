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
<body>
	<div class="layui-container">
		<div class="layui-col-md5 layui-col-md-offset4" id="wrapDIV">
			<div class="layui-row">
				<form id="loginForm" class="layui-form"
					action="${pageContext.request.contextPath}/user/login.action">
					<div class="layui-form-item">
						<label class="layui-form-label">用户名</label>
						<div class="layui-input-inline">
							<input type="text" name="userName" required lay-verify="required"
								placeholder="请输入用户名" autocomplete="off" class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux" style="color:red;">${error}</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">密&nbsp;&nbsp;&nbsp;&nbsp;码</label>
						<div class="layui-input-inline">
							<input type="password" name="password" required
								lay-verify="required" placeholder="请输入密码" autocomplete="off"
								class="layui-input">
						</div>
						 
					</div>
					<div class="layui-form-item">
						<div class="layui-input-block">
							<button class="layui-btn" lay-submit lay-filter="formDemo">登录</button>
							<button type="reset" class="layui-btn layui-btn-primary">重置</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script>
		//Demo
		layui.use('form', function() {
			var form = layui.form;
			//监听提交
			form.on('submit(formDemo)', function(data) {
				layer.msg(JSON.stringify(data.field));
				return true;
			});
		});
		//jquery
		var bodyHeight = $(window).height();
		var formHeight = $("#loginForm").height();
		var realHeight = (bodyHeight - formHeight) / 2;
		$('#wrapDIV').css({marginTop:realHeight});
	</script>
</body>
</html>