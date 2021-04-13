<!DOCTYPE html>
<html>
<head>
  	<#import "../common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/css/dataTables.bootstrap.min.css">
    <title>任务同步控制</title>
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && cookieMap["xxljob_adminlte_settings"]?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "user" />
	
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>用户管理</h1>
		</section>
		
		<!-- Main content -->
	    <section class="content">
	    
	    	<div class="row">
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">角色</span>
                        <select class="form-control" id="role" >
                            <option value="-1" >全部</option>
                            <option value="1" >管理员</option>
                            <option value="0" >普通用户</option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">账号</span>
                        <input type="text" class="form-control" id="username" autocomplete="on" >
                    </div>
                </div>
	            <div class="col-xs-1">
	            	<button class="btn btn-block btn-info" id="searchBtn">搜索</button>
	            </div>
	            <div class="col-xs-2">
	            	<button class="btn btn-block btn-success add" type="button">新增用户</button>
	            </div>
          	</div>
	    	
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <div class="box-body" >
			              	<table id="user_list" class="table table-bordered table-striped" width="100%" >
				                <thead>
					            	<tr>
                                        <th name="id" >ID</th>
                                        <th name="username" >账号</th>
					                  	<th name="password" >密码</th>
                                        <th name="role" >角色</th>
					                  	<th name="permission" >权限</th>
					                  	<th>操作</th>
					                </tr>
				                </thead>
				                <tbody></tbody>
				                <tfoot></tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
	    </section>
	</div>
	
	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<!-- 新增.模态框 -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >新增用户</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">账号<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="username" placeholder="请输入账户" maxlength="20" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">密码<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="password" placeholder="请输入密码" maxlength="20" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">角色<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <input type="radio" name="role" value="0" checked />普通用户
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="radio" name="role" value="1" />管理员
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">权限<font color="black">*</font></label>
                        <div class="col-sm-10">
							
                        </div>
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

<!-- 更新.模态框 -->
<div class="modal fade" id="updateModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >修改用户</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">账号<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="username" placeholder="请输入账号" maxlength="20" readonly ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">密码<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="password" placeholder="请输入密码" maxlength="20" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">角色<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <input type="radio" name="role" value="0" />普通用户
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="radio" name="role" value="1" />管理员
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">权限<font color="black">*</font></label>
                        <div class="col-sm-10">

                        </div>
                    </div>

					<hr>
					<div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
							<button type="submit" class="btn btn-primary"  >保存</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <input type="hidden" name="id" >
						</div>
					</div>

				</form>
         	</div>
		</div>
	</div>
</div>

<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/static/js/user.index.1.js"></script>
</body>
</html>
