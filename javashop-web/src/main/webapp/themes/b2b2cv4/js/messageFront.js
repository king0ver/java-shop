var MessageFront= {
    init     : function (staticserver) {
        var self = this;
        this.bindEvent();
    },
    bindEvent: function () {
        var self = this;
        //火狐操作后会将下个复选框选中，所以默认设置false
        $("input[name=msgcheckbox]").each(function(){
        	$(this).prop('checked',false);
        });
        $("#all-check").prop('checked',false);
        //放入回收站
        $(".recycle-one").click(function () {
        	if(!confirm("确认删除该消息吗？")){	
    			return ;
    		}
            var $this = $(this);
            var messageid = $this.attr("messageid");
            $.Loading.show("正在删除...");
            $.ajax({
                url     : ctx + "/api/shop/member-message/msg-delete.do",
                data    : "messageids=" + messageid,
                dataType: "json",
                success : function (result) {
                    if (result.result == 1) {
                    	window.location.reload();
                    } else {
                        $.alert(result.message);
                    }
                    $.Loading.hide();
                },
                error   : function () {
                    $.Loading.hide();
                    $.message.error('出现错误，请重试！');
                }
            });
        });
        
        //全部放入回收站
        $("#delete-all").click(function () {
            var $this = $(this);
            var messageids="";
            $("input[name=msgcheckbox]:checked").each(function(){
            	messageids+=$(this).val()+",";
            });
            if(messageids.length<=1){
            	alert("请选择要删除的消息...");
            }else{
            	if(!confirm("确认将这些消息全部删除吗？")){	
        			return ;
        		}
            	$.Loading.show("正在删除...");
            	$.ajax({
            		url     : ctx + "/api/shop/member-message/msg-delete.do",
            		data    : "messageids=" + messageids.substring(0,messageids.length-1),
            		dataType: "json",
            		success : function (result) {
            			if (result.result == 1) {
            				window.location.reload();
            			} else {
            				$.alert(result.message);
            			}
            			$.Loading.hide();
            		},
            		error   : function () {
            			$.Loading.hide();
            			$.message.error('出现错误，请重试！');
            		}
            	});
            }
        });
        
        //标记为已读
        $(".have-read-one").click(function () {
            var $this = $(this);
            var messageid = $this.attr("messageid");
            alert("消息已读...");
            $.ajax({
                url     : ctx + "/api/shop/member-message/have-read.do",
                data    : "messageids=" + messageid,
                dataType: "json",
                success : function (result) {
                    if (result.result == 1) {
                    	location.reload();  //刷新页面
                    	MessageFrontTool.loadNum();
                    } else {
                        $.alert(result.message);
                    }
                    $.Loading.hide();
                },
                error   : function () {
                    $.Loading.hide();
                    $.message.error('出现错误，请重试！');
                }
            });
        });
        
        //全部标记为已读
        $("#have-read-all").click(function () {
            var $this = $(this);
            var messageids="";
            $("input[name=msgcheckbox]:checked").each(function(){
            	messageids+=$(this).val()+",";
            });
            if(messageids.length<=1){
            	alert("请选择要已读状态的消息...");
            }else{
            	$.ajax({
            		url     : ctx + "/api/shop/member-message/have-read.do",
            		data    : "messageids=" + messageids.substring(0,messageids.length-1),
            		dataType: "json",
            		success : function (result) {
            			if (result.result == 1) {
            				window.location.reload();
            			} else {
            				$.alert(result.message);
            			}
            			$.Loading.hide();
            		},
            		error   : function () {
            			$.Loading.hide();
            			$.message.error('出现错误，请重试！');
            		}
            	});
            }
        });
        
        
        //全选
        $("#all-check").click(function () {
        	var check = $( this ).is( ":checked" );
        	if (check) {
        		$("input[name=msgcheckbox]").prop('checked',true);
            } else {
            	$("input[name=msgcheckbox]").removeAttr("checked");
            }
        });
        
        
        $("input[name=msgcheckbox]").click(function(){
        	var flag = true;
        	$("input[name=msgcheckbox]").each(function(){
        		var check = $(this).is( ":checked" );
        		if(!check){
        			flag = false;
        		}
        	});
        	$("#all-check").prop('checked',flag);
        });
    }
        
};
