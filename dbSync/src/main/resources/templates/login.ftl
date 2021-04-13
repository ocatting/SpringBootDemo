<!DOCTYPE html>
<html>
<head>
  	<#import "./common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/blue.css">
	<title>任务同步控制台</title>
</head>
<body class="hold-transition login-page">
	<div class="login-box">
		<div class="login-logo">
			<a><b>数据库同步中心</b></a>
		</div>
		<form id="loginForm" method="post" >
			<div class="login-box-body">
				<p class="login-box-msg">任务同步控制台</p>
				<div class="form-group has-feedback">
	            	<input type="text" name="username" class="form-control" placeholder="请输入登录账号"  maxlength="18" >
	            	<span class="glyphicon glyphicon-envelope form-control-feedback"></span>
				</div>
	          	<div class="form-group has-feedback">
	            	<input type="password" name="password" class="form-control" placeholder="请输入登录密码"  maxlength="18" >
	            	<span class="glyphicon glyphicon-lock form-control-feedback"></span>
	          	</div>
				<div class="row">
					<div class="col-xs-8">
		              	<div class="checkbox icheck">
		                	<label>
		                  		<input type="checkbox" name="remember" > &nbsp; 记住密码
		                	</label>
						</div>
		            </div>
		            <div class="col-xs-4">
						<button type="submit" class="btn btn-primary btn-block btn-flat">登录</button>
					</div>
				</div>
			</div>
		</form>
	</div>
<@netCommon.commonScript />
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script src="${request.contextPath}/static/js/login.1.js"></script>

</body>
</html>
