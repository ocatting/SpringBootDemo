$(function() {

	// init date tables
	var jobTable = $('#job_list').dataTable({
		"deferRender": true,
		"processing" : true,
	    "serverSide": true,
		"ajax": {
			url: base_url + "/task/page",
			type:"post",
	        data : function ( d ) {
	        	var obj = {};
	        	obj.current = d.start;
	        	obj.size = d.length;
                return obj;
            }
	    },
	    "searching": false,
	    "ordering": false,
	    //"scrollX": true,	// scroll x，close self-adaption
	    "columns": [{
	                	"data": 'id',
						"bSortable": false,
						"visible" : true
					},
	                {
	                	"data": 'taskCron',
	                	"visible" : true
            		},
	                {
	                	"data": 'taskDesc',
						"visible" : true
					},
					{
						"data": 'readDbId',
						"visible" : true
					},
					{
						"data": 'writeDbId',
						"visible" : true
					},
					{
						"data": 'triggerLastTime',
						"visible" : true,
						"render": function ( data, type, row ) {
							return data?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
						}
					},
					{
						"data": 'triggerNextTime',
						"visible" : true,
						"render": function ( data, type, row ) {
							return data?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
						}
					},
					{
						"data": 'triggerStatus',
						"visible" : true,
						"render": function ( data, type, row ) {
							if (1 === data) {
								return '<small class="label label-success" >RUNNING</small>';
							} else {
								return '<small class="label label-default" >STOP</small>';
							}
							return data;
						}
					},
	                {
						"data": 'opt',
						"render": function ( data, type, row ) {
							// return '<a href="javascript:void(0);" className="job_operate" _type="job_del">详情</a>'
							return '<a href="javascript:void(0);" class="detailModel">详情</a>'
						}
	                }
	            ],
		"language" : {
			"sProcessing" : '处理中...' ,
			"sLengthMenu" : '每页 _MENU_ 条记录' ,
			"sZeroRecords" : '没有匹配结果' ,
			"sInfo" : '第 _PAGE_ 页 ( 总共 _PAGES_ 页，_TOTAL_ 条记录 )' ,
			"sInfoEmpty" : '无记录' ,
			"sInfoFiltered" : '(由 _MAX_ 项结果过滤)' ,
			"sInfoPostFix" : "",
			"sSearch" : '搜索',
			"sUrl" : "",
			"sEmptyTable" : '表中数据为空' ,
			"sLoadingRecords" : '载入中...' ,
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : '首页' ,
				"sPrevious" : '上页' ,
				"sNext" : '下页' ,
				"sLast" : '末页'
			},
			"oAria" : {
				"sSortAscending" : ': 以升序排列此列' ,
				"sSortDescending" : ': 以降序排列此列'
			}
		}
	});

    // table data
    var tableData = {};

	// search btn
	$('#searchBtn').on('click', function(){
		jobTable.fnDraw();
	});

	// jobGroup change
	$('#jobGroup').on('change', function(){
        //reload
        var jobGroup = $('#jobGroup').val();
        window.location.href = base_url + "/jobinfo?jobGroup=" + jobGroup;
    });

	// job operate
	$('#job_list').on('click', '.job_operate',function() {
		var typeName;
		var url;
		var needFresh = false;

		var type = $(this).attr("_type");
		if ("job_pause" === type) {
			typeName = '停止' ;
			url = base_url + "/jobinfo/stop";
			needFresh = true;
		} else if ("job_resume" === type) {
			typeName = '开始' ;
			url = base_url + "/jobinfo/start";
			needFresh = true;
		} else if ("job_del" === type) {
			typeName = '删除' ;
			url = base_url + "/jobinfo/remove";
			needFresh = true;
		} else {
			return;
		}

		var id = $(this).parents('ul').attr("_id");

		layer.confirm( '确定' + typeName + '?', {
			icon: 3,
			title: '系统提示' ,
            btn: [ '确定', '取消' ]
		}, function(index){
			layer.close(index);

			$.ajax({
				type : 'POST',
				url : url,
				data : {
					"id" : id
				},
				dataType : "json",
				success : function(data){
					if (data.code === 200) {
                        layer.msg( typeName + '成功' );
                        if (needFresh) {
                            //window.location.reload();
                            jobTable.fnDraw(false);
                        }
					} else {
                        layer.msg( data.msg || typeName + '失败' );
					}
				}
			});
		});
	});

    // job trigger
    $("#job_list").on('click', '.job_trigger',function() {
        var id = $(this).parents('ul').attr("_id");
        var row = tableData['key'+id];

        $("#jobTriggerModal .form input[name='id']").val( row.id );
        $("#jobTriggerModal .form textarea[name='executorParam']").val( row.executorParam );

        $('#jobTriggerModal').modal({backdrop: false, keyboard: false}).modal('show');
    });
    $("#jobTriggerModal .ok").on('click',function() {
        $.ajax({
            type : 'POST',
            url : base_url + "/jobinfo/trigger",
            data : {
                "id" : $("#jobTriggerModal .form input[name='id']").val(),
                "executorParam" : $("#jobTriggerModal .textarea[name='executorParam']").val()
            },
            dataType : "json",
            success : function(data){
                if (data.code === 200) {
                    $('#jobTriggerModal').modal('hide');

                    layer.msg( '执行一次' + '成功' );
                } else {
                    layer.msg( data.msg || '执行一次' + '失败' );
                }
            }
        });
    });

    $("#jobTriggerModal").on('hide.bs.modal', function () {
        $("#jobTriggerModal .form")[0].reset();
    });

    // job registryinfo
    $("#job_list").on('click', '.job_registryinfo',function() {
        var id = $(this).parents('ul').attr("_id");
        var row = tableData['key'+id];

        var jobGroup = row.jobGroup;

        $.ajax({
            type : 'POST',
            url : base_url + "/jobgroup/loadById",
            data : {
                "id" : jobGroup
            },
            dataType : "json",
            success : function(data){

                var html = '<center>';
                if (data.code == 200 && data.content.registryList) {
                    for (var index in data.content.registryList) {
                        html += '<span class="badge bg-green" >' + data.content.registryList[index] + '</span><br>';
                    }
                }
                html += '</center>';

                layer.open({
                    title: '注册节点' ,
                    btn: [ '确定' ],
                    content: html
                });

            }
        });

    });

    // job_next_time
    $("#job_list").on('click', '.job_next_time',function() {
        var id = $(this).parents('ul').attr("_id");
        var row = tableData['key'+id];

        var jobCron = row.jobCron;

        $.ajax({
            type : 'POST',
            url : base_url + "/jobinfo/nextTriggerTime",
            data : {
                "cron" : jobCron
            },
            dataType : "json",
            success : function(data){
            	
            	if (data.code != 200) {
                    layer.open({
                        title: '下次执行时间' ,
                        btn: [ '确定' ],
                        content: data.msg
                    });
				} else {
                    var html = '<center>';
                    if (data.code === 200 && data.content) {
                        for (var index in data.content) {
                            html += '<span>' + data.content[index] + '</span><br>';
                        }
                    }
                    html += '</center>';

                    layer.open({
                        title: '下次执行时间' ,
                        btn: [ '确定' ],
                        content: html
                    });
				}

            }
        });

    });

	// add
	$(".add").click(function(){

		// init-cronGen
        $("#addModal .form input[name='jobCron']").show().siblings().remove();
        $("#addModal .form input[name='jobCron']").cronGen({});

		$('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var addModalValidate = $("#addModal .form").validate({
		errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,
        rules : {
			jobDesc : {
				required : true,
				maxlength: 50
			},
            jobCron : {
            	required : true
            },
			author : {
				required : true
			},
            executorTimeout : {
                digits:true
            },
            executorFailRetryCount : {
                digits:true
            }
        },
        messages : {
            jobDesc : {
            	required : '请输入' + '任务描述'
            },
            jobCron : {
            	required : '请输入' + "Cron"
            },
            author : {
            	required : '请输入' + '负责人'
            },
            executorTimeout : {
                digits: '请输入' + '整数'
            },
            executorFailRetryCount : {
                digits: '请输入' + '整数'
            }
        },
		highlight : function(element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        success : function(label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },
        errorPlacement : function(error, element) {
            element.parent('div').append(error);
        },
        submitHandler : function(form) {

			// process
            var executorTimeout = $("#addModal .form input[name='executorTimeout']").val();
            if(!/^\d+$/.test(executorTimeout)) {
                executorTimeout = 0;
			}
            $("#addModal .form input[name='executorTimeout']").val(executorTimeout);
            var executorFailRetryCount = $("#addModal .form input[name='executorFailRetryCount']").val();
            if(!/^\d+$/.test(executorFailRetryCount)) {
                executorFailRetryCount = 0;
            }
            $("#addModal .form input[name='executorFailRetryCount']").val(executorFailRetryCount);

            // process-cronGen
            $("#addModal .form input[name='jobCron']").val( $("#addModal .form input[name='cronGen_display']").val() );

        	$.post(base_url + "/jobinfo/add",  $("#addModal .form").serialize(), function(data, status) {
    			if (data.code == "200") {
					$('#addModal').modal('hide');
					layer.open({
						title: '系统提示' ,
                        btn: [ '确定' ],
						content: '新增成功' ,
						icon: '1',
						end: function(layero, index){
							jobTable.fnDraw();
							//window.location.reload();
						}
					});
    			} else {
					layer.open({
						title: '系统提示',
                        btn: [ '确定' ],
						content: (data.msg || '失败'),
						icon: '2'
					});
    			}
    		});
		}
	});
	$("#addModal").on('hide.bs.modal', function () {
        addModalValidate.resetForm();
		$("#addModal .form")[0].reset();
		$("#addModal .form .form-group").removeClass("has-error");
		$(".remote_panel").show();	// remote

		$("#addModal .form input[name='executorHandler']").removeAttr("readonly");
	});


    // glueType change
    $(".glueType").change(function(){
		// executorHandler
        var $executorHandler = $(this).parents("form").find("input[name='executorHandler']");
        var glueType = $(this).val();
        if ('BEAN' != glueType) {
            $executorHandler.val("");
            $executorHandler.attr("readonly","readonly");
        } else {
            $executorHandler.removeAttr("readonly");
        }
    });

	$("#addModal .glueType").change(function(){
		// glueSource
		var glueType = $(this).val();
		if ('GLUE_GROOVY'==glueType){
			$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_java").val() );
		} else if ('GLUE_SHELL'==glueType){
			$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_shell").val() );
		} else if ('GLUE_PYTHON'==glueType){
			$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_python").val() );
		} else if ('GLUE_PHP'==glueType){
            $("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_php").val() );
        } else if ('GLUE_NODEJS'==glueType){
			$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_nodejs").val() );
		} else if ('GLUE_POWERSHELL'==glueType){
            $("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_powershell").val() );
        } else {
            $("#addModal .form textarea[name='glueSource']").val("");
		}
	});

	// update
	$("#job_list").on('click', '.update',function() {

        var id = $(this).parents('ul').attr("_id");
        var row = tableData['key'+id];

		// base data
		$("#updateModal .form input[name='id']").val( row.id );
		$('#updateModal .form select[name=jobGroup] option[value='+ row.jobGroup +']').prop('selected', true);
		$("#updateModal .form input[name='jobDesc']").val( row.jobDesc );
		$("#updateModal .form input[name='jobCron']").val( row.jobCron );
		$("#updateModal .form input[name='author']").val( row.author );
		$("#updateModal .form input[name='alarmEmail']").val( row.alarmEmail );
		$("#updateModal .form input[name='executorTimeout']").val( row.executorTimeout );
        $("#updateModal .form input[name='executorFailRetryCount']").val( row.executorFailRetryCount );
		$('#updateModal .form select[name=executorRouteStrategy] option[value='+ row.executorRouteStrategy +']').prop('selected', true);
		$("#updateModal .form input[name='executorHandler']").val( row.executorHandler );
		$("#updateModal .form textarea[name='executorParam']").val( row.executorParam );
        $("#updateModal .form input[name='childJobId']").val( row.childJobId );
		$('#updateModal .form select[name=executorBlockStrategy] option[value='+ row.executorBlockStrategy +']').prop('selected', true);
		$('#updateModal .form select[name=glueType] option[value='+ row.glueType +']').prop('selected', true);

        $("#updateModal .form select[name=glueType]").change();

        // init-cronGen
        $("#updateModal .form input[name='jobCron']").show().siblings().remove();
        $("#updateModal .form input[name='jobCron']").cronGen({});

		// show
		$('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var updateModalValidate = $("#updateModal .form").validate({
		errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,

		rules : {
			jobDesc : {
				required : true,
				maxlength: 50
			},
			jobCron : {
				required : true
			},
			author : {
				required : true
			},
            executorTimeout : {
                digits:true
            },
            executorFailRetryCount : {
                digits:true
            }
		},
		messages : {
			jobDesc : {
                required : '请输入' + '任务描述'
			},
			jobCron : {
				required : '请输入' + "Cron"
			},
			author : {
				required : '请输入' + '负责人'
			},
            executorTimeout : {
                digits: '请输入' + '整数'
            },
            executorFailRetryCount : {
                digits: '请输入' + '整数'
            }
		},
		highlight : function(element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        success : function(label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },
        errorPlacement : function(error, element) {
            element.parent('div').append(error);
        },
        submitHandler : function(form) {

            // process
            var executorTimeout = $("#updateModal .form input[name='executorTimeout']").val();
            if(!/^\d+$/.test(executorTimeout)) {
                executorTimeout = 0;
            }
            $("#updateModal .form input[name='executorTimeout']").val(executorTimeout);
            var executorFailRetryCount = $("#updateModal .form input[name='executorFailRetryCount']").val();
            if(!/^\d+$/.test(executorFailRetryCount)) {
                executorFailRetryCount = 0;
            }
            $("#updateModal .form input[name='executorFailRetryCount']").val(executorFailRetryCount);

            // process-cronGen
            $("#updateModal .form input[name='jobCron']").val( $("#updateModal .form input[name='cronGen_display']").val() );

			// post
    		$.post(base_url + "/jobinfo/update", $("#updateModal .form").serialize(), function(data, status) {
    			if (data.code == "200") {
					$('#updateModal').modal('hide');
					layer.open({
						title: '系统提示',
                        btn: [ '确定' ],
						content: '更新成功' ,
						icon: '1',
						end: function(layero, index){
							//window.location.reload();
							jobTable.fnDraw();
						}
					});
    			} else {
					layer.open({
						title: '系统提示' ,
                        btn: [ '确定' ],
						content: (data.msg || '更新失败' ),
						icon: '2'
					});
    			}
    		});
		}
	});
	$("#updateModal").on('hide.bs.modal', function () {
        updateModalValidate.resetForm();
        $("#updateModal .form")[0].reset();
        $("#updateModal .form .form-group").removeClass("has-error");
	});

    /**
	 * find title by name, GlueType
     */
	function findGlueTypeTitle(glueType) {
		var glueTypeTitle;
        $("#addModal .form select[name=glueType] option").each(function () {
            var name = $(this).val();
            var title = $(this).text();
            if (glueType == name) {
                glueTypeTitle = title;
                return false
            }
        });
        return glueTypeTitle;
    }

});
