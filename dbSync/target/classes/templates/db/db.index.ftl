<!DOCTYPE html>
<html>
<head>
  	<#import "../common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/css/dataTables.bootstrap.min.css">
    <title>任务同步控制台</title>
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && cookieMap["xxljob_adminlte_settings"]?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "jobinfo" />
	
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>任务管理</h1>
		</section>
		
		<!-- Main content -->
	    <section class="content">
	    
	    	<div class="row">
                <div class="col-xs-2">
                    <div class="input-group">
                        <input type="text" class="form-control" id="jobDesc" autocomplete="on" placeholder="请输入数据库名称" >
                    </div>
                </div>
	            <div class="col-xs-1">
	            	<button class="btn btn-block btn-info" id="searchBtn">搜索</button>
	            </div>
	            <div class="col-xs-1">
	            	<button class="btn btn-block btn-success add" type="button">新增</button>
	            </div>
          	</div>
	    	
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <div class="box-body" >
			              	<table id="db_list" class="table table-bordered table-striped" width="100%" >
				                <thead>
					            	<tr>
					            		<th name="id" >数据库ID</th>
					                	<th name="taskCron" >名称</th>
					                  	<th name="taskDesc" >类型</th>
                                        <th name="readDbId" >JdbcUrl</th>
                                        <th name="writeDbId" >Driver</th>
					                  	<th name="triggerLastTime" >Username</th>
					                  	<th name="triggerNextTime" >Password</th>
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

<!-- db新增.模态框 -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >新增</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<div class="form-group">
						<label for="dbName" class="col-sm-2 control-label">数据库名称<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="dbName" placeholder="请输入数据库名称" maxlength="50" ></div>
					</div>
                    <div class="form-group">
                        <label for="dbType" class="col-sm-2 control-label">数据库类型<font color="red">*</font></label>
                        <div class="col-sm-4">
                            <select class="form-control" name="dbType" >
                                <option value="mysql" >MySQL</option>
                                <option value="postgresql" >PostgreSQL</option>
                                <option value="hive" >Hive</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="jdbcUrl" class="col-sm-2 control-label">JdbcUrl<font color="red">*</font></label>
                        <div class="col-sm-6">
                            <textarea class="textarea form-control" name="jdbcUrl" placeholder="请输入JdbcUrl " maxlength="512" style="height: 63px; line-height: 1.2;"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="driver" class="col-sm-2 control-label">Driver<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="driver" placeholder="com.mysql.cj.jdbc.Driver" maxlength="100" ></div>
                    </div>
                    <div class="form-group">
                        <label for="username" class="col-sm-2 control-label">Username<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="username" placeholder="数据库用户名" maxlength="6" ></div>
                    </div>
					<div class="form-group">
                        <label for="password" class="col-sm-2 control-label">Password<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="password" placeholder="数据库密码" maxlength="50" ></div>
					</div>
                    <hr>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-6">
							<button type="submit" class="btn btn-primary" >保存</button>
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
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >更新任务</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="dbName" class="col-sm-2 control-label">数据库名称<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="dbName" placeholder="请输入数据库名称" maxlength="50" ></div>
                    </div>
                    <div class="form-group">
                        <label for="dbType" class="col-sm-2 control-label">数据库类型<font color="red">*</font></label>
                        <div class="col-sm-4">
                            <select class="form-control" name="dbType" >
                                <option value="-1" >MySQL</option>
                                <option value="0" >PostgrepSql</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="firstname" class="col-sm-2 control-label">JdbcUrl<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <textarea class="textarea form-control" name="executorParam" placeholder="请输入JdbcUrl " maxlength="512" style="height: 63px; line-height: 1.2;"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="driver" class="col-sm-2 control-label">Driver<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="driver" placeholder="com.mysql.cj.jdbc.Driver" maxlength="100" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">Username<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="executorTimeout" placeholder="数据库用户名" maxlength="6" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">Password<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="author" placeholder="数据库密码" maxlength="50" ></div>
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
<!-- moment -->
<script src="${request.contextPath}/static/adminlte/bower_components/moment/moment.min.js"></script>
<#-- cronGen -->
<script src="${request.contextPath}/static/plugins/cronGen/cronGen.js"></script>
<script src="${request.contextPath}/static/js/db.index.1.js"></script>
</body>
</html>
