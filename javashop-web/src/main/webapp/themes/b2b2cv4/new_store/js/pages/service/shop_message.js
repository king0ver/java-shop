/**
 * Created by fk 2017年10月13日17:00:00
 */
$(function(){

    (function(){
        var btn = $('#filter_seach, .app-tab-tools a');
        btn.unbind('click').on('click', function(){
            var _this = $(this);
            var type = _this.attr('type');
            location.href = './shop_message.html?type='+ type;
        });
    })();
    
    (function(){
    	$(".delete-one").unbind('click').on('click', function(){
        	if(!confirm("确认删除该消息吗？")){	
    			return ;
    		}
            var $this = $(this);
            var messageid = $this.attr("messageid");
            $.ajax({
                url     : ctx + "/api/b2b2c/store-message/delete.do",
                data    : "messageids=" + messageid,
                dataType: "json",
                success : function (result) {
                    if (result.result == 1) {
                    	window.location.reload();
                    } else {
                        $.alert(result.message);
                    }
                },
                error   : function () {
                    $.message.error('出现错误，请重试！');
                }
            });
        });
    	//全部删除
    	$(".delete-all").unbind('click').on('click', function(){
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
            	$.ajax({
            		url     : ctx + "/api/b2b2c/store-message/delete.do",
            		data    : "messageids=" + messageids.substring(0,messageids.length-1),
            		dataType: "json",
            		success : function (result) {
            			if (result.result == 1) {
            				window.location.reload();
            			} else {
            				$.alert(result.message);
            			}
            		},
            		error   : function () {
            			$.message.error('出现错误，请重试！');
            		}
            	});
            }
        });
    	
    	$(".read-one").unbind('click').on('click', function(){
            var $this = $(this);
            var messageid = $this.attr("messageid");
            $.ajax({
                url     : ctx + "/api/b2b2c/store-message/read.do",
                data    : "messageids=" + messageid,
                dataType: "json",
                success : function (result) {
                    if (result.result == 1) {
                    //	$this.remove();
                    	$.alert(result.message);
                    	location.reload();  //刷新页面
                    	MessageFrontTool.loadNum();
                    } else {
                        $.alert(result.message);
                    }
                },
                error   : function () {
                    $.message.error('出现错误，请重试！');
                }
            });
        });
        
        //全部标记为已读
    	$(".read-all").unbind('click').on('click', function(){
            var $this = $(this);
            var messageids="";
            $("input[name=msgcheckbox]:checked").each(function(){
            	messageids+=$(this).val()+",";
            });
            if(messageids.length<=1){
            	alert("请选择要已读状态的消息...");
            }else{
            	$.ajax({
            		url     : ctx + "/api/b2b2c/store-message/read.do",
            		data    : "messageids=" + messageids.substring(0,messageids.length-1),
            		dataType: "json",
            		success : function (result) {
            			if (result.result == 1) {
            				window.location.reload();
            			} else {
            				$.alert(result.message);
            			}
            		},
            		error   : function () {
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
    	
    })();
    /*
	 * 兼容JS
	 * ============================================================================
	 */
    (function(){
        // 修复IE下样式错乱
        if(Sys.ie == 7){
            $('.tools-thead .goods').css({float: 'left'});
            $('#filter_seach').css({lineHeight: '20px'});

            if($('.order-list-item td')){
                $('.order-list-item td').find('span').css({float: 'none'});
            }
        };
    })();
});