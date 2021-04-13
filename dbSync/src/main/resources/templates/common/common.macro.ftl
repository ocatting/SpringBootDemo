<#macro commonStyle>

	<#-- favicon -->
	<link rel="icon" href="${request.contextPath}/static/favicon.ico" />

	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="Cache-Control" content="no-cache, must-revalidate"/>
	<meta http-equiv="expires" content="0"/>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/font-awesome/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/Ionicons/css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/dist/css/skins/_all-skins.min.css">
      
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

	<!-- pace -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/PACE/themes/blue/pace-theme-flash.css">

</#macro>

<#macro commonScript>
	<!-- jQuery 2.1.4 -->
	<script src="${request.contextPath}/static/adminlte/bower_components/jquery/jquery.min.js"></script>
	<!-- Bootstrap 3.3.5 -->
	<script src="${request.contextPath}/static/adminlte/bower_components/bootstrap/js/bootstrap.min.js"></script>
	<!-- FastClick -->
	<script src="${request.contextPath}/static/adminlte/bower_components/fastclick/fastclick.js"></script>
	<!-- AdminLTE App -->
	<script src="${request.contextPath}/static/adminlte/dist/js/adminlte.min.js"></script>
	<!-- jquery.slimscroll -->
	<script src="${request.contextPath}/static/adminlte/bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>

    <!-- pace -->
    <script src="${request.contextPath}/static/adminlte/bower_components/PACE/pace.min.js"></script>
    <#-- jquery cookie -->
	<script src="${request.contextPath}/static/plugins/jquery/jquery.cookie.js"></script>
	<#-- jquery.validate -->
	<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>

	<#-- layer -->
	<script src="${request.contextPath}/static/plugins/layer/layer.js"></script>

	<#-- common -->
    <script src="${request.contextPath}/static/js/common.1.js"></script>
    <script>
		var base_url = '${request.contextPath}';
	</script>

</#macro>

<#macro commonHeader>
	<header class="main-header">
		<a href="${request.contextPath}/" class="logo">
			<span class="logo-mini"><b>DB</b></span>
			<span class="logo-lg"><b>任务同步控制台</b></span>
		</a>
		<nav class="navbar navbar-static-top" role="navigation">

			<a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>

          	<div class="navbar-custom-menu">
				<ul class="nav navbar-nav">
					<#-- login user -->
                    <li class="dropdown">
                        <a href="javascript:" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                            欢迎 ${Session["SYNC_UMS"].username}
                            <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu" role="menu">
                            <li id="updatePwd" ><a href="javascript:">修改密码</a></li>
                            <li id="logoutBtn" ><a href="javascript:">注销</a></li>
                        </ul>
                    </li>
				</ul>
			</div>

		</nav>
	</header>

	<!-- 修改密码.模态框 -->
	<div class="modal fade" id="updatePwdModal" tabindex="-1" role="dialog"  aria-hidden="true">
		<div class="modal-dialog ">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" >修改密码</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal form" role="form" >
						<div class="form-group">
							<label for="lastname" class="col-sm-2 control-label">新密码<font color="red">*</font></label>
							<div class="col-sm-10"><input type="text" class="form-control" name="password" placeholder="请输入新密码" maxlength="18" ></div>
						</div>
						<hr>
						<div class="form-group">
							<div class="col-sm-offset-3 col-sm-6">
								<button type="submit" class="btn btn-primary"  >保存</button>
								<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

</#macro>

<#macro commonLeft pageName >
	<!-- Left side column. contains the logo and sidebar -->
	<aside class="main-sidebar">
		<!-- sidebar: style can be found in sidebar.less -->
		<section class="sidebar">
			<!-- sidebar menu: : style can be found in sidebar.less -->
			<ul class="sidebar-menu">
                <li class="header">导航</li>
                <li class="nav-click <#if pageName == "index">active</#if>" ><a href="${request.contextPath}/"><i class="fa fa-circle-o text-aqua"></i><span>运行监控</span></a></li>
				<li class="nav-click <#if pageName == "joblog">active</#if>" ><a href="${request.contextPath}/db"><i class="fa fa-circle-o text-green"></i><span>数据库配置</span></a></li>
				<li class="nav-click <#if pageName == "jobinfo">active</#if>" ><a href="${request.contextPath}/task/info"><i class="fa fa-circle-o text-yellow"></i><span>任务管理</span></a></li>
				<li class="nav-click <#if pageName == "joblog">active</#if>" ><a href="${request.contextPath}/log"><i class="fa fa-circle-o text-green"></i><span>调度日志</span></a></li>
				<#if Session["SYNC_UMS"].role == 1>
                    <li class="nav-click <#if pageName == "user">active</#if>" ><a href="${request.contextPath}/user"><i class="fa fa-circle-o text-purple"></i><span>用户管理</span></a></li>
				</#if>
			</ul>
		</section>
		<!-- /.sidebar -->
	</aside>
</#macro>

<#macro commonFooter >
	<footer class="main-footer">
        Powered by <b>sync-db</b>
		<div class="pull-right hidden-xs">
            <strong>Copyright &copy; 2021-${.now?string('yyyy')} &nbsp
            </strong><!-- All rights reserved. -->
		</div>
	</footer>
</#macro>