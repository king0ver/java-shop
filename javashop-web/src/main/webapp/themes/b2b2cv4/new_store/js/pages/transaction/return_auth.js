/**
 * Created by Andste on 2016/6/20.
 */
$(function(){
    //  审核通过与否
    (function(){
        $('.check-btn').unbind('click').on('click', function(){
        
    		var refund_price =$("input[name='refund_price']").val();
    		//商品总金额
    		var i_price =$("#i_price").val();
    		//退货数
    		var i_num =$("#i_num").val();
    		//购买商品总数
    		var i_ship_num =$("#i_ship_num").val();
    		//每一个商品金额
    		var single_price= Number(i_price/i_ship_num);
    		if(refund_price==""){
    			alert("退款金额不能为空")
    			return false;
    		}
    		if(refund_price > single_price*i_num){
    			alert("退款金额不能大于支付金额")
    			return false;
    		}
            var _this = $(this), status = _this.attr('status'),sn = $("input[name=sn]").val();
            $.confirm('确定' + _this.html() + '吗？', function(){
            	$('#agree').val(status);
                var options = {
                    url:ctx+ '/after-sale/seller/refund/approval/'+sn+'.do',
                    type: 'POST',
                    dataType : 'json',
                    success: function(result){
                        $.message.success(_this.html() + '成功！', 'reload');
                    },
                    error: function(){
                        $.message.error('出现错误，请重试！');
                        return false;
                    }
                };
                $('#theForm').ajaxSubmit(options);
            } );
        });
        /**
         * 全部入库
         */
        $('.stock-in').unbind('click').on('click', function(){
        	var sn = $("input[name=sn]").val();
        	$.confirm('确定已经收到买家退货的商品吗？',function(){
        		var options = {
        				url:ctx+ '/after-sale/seller/refund-goods/stock-in/'+sn+'.do',
        				type: 'POST',
        				dataType : 'json',
        				success: function(result){
        					$.message.success('全部入库成功！请等待平台退款给买家。', 'reload');
        				},
        				error: function(){
        					$.message.error('出现错误，请重试！');
        					return false;
        				}
        		};
        		$('#theForm').ajaxSubmit(options);
        	} );
        });
    })();

    //  退款操作
    (function(){
        var btn = $('.refund'), input = $("input[name='alltotal_pay']");
        var paymoney = $("input[name='paymoney']").val();
        input.blur(function(){
            check();
        });
        btn.unbind('click').on('click', function(){
        	if($("#refundWay").val() == 1){
        		if(Number($("#alltoty_pay").val())==0 ){

            		alert("退款金额不能为零！");
            		return;
            	}
    		}
        	if(Number($("#alltoty_pay").val()) > Number(paymoney)){
        		alert("退款金额不能大于支付金额！！")
        		return;
        	}
            var _this = $(this), sell_back_id = _this.attr('sell_back_id');
            check();
            if(input.parent().is('.error')){
                return false;
            }else {
                $.confirm('确定退款吗？', function(){
                    $.ajax({
                        type: 'post',
                        url: ctx+ '/api/store/store-sell-back/refund.do',
                        data: {
                            id: sell_back_id,
                            status: $("#status").val(),
                            alltotal_pay:$("#alltoty_pay").val()
                        },
                        dataType: "json",
                        success : function(data) {
                            if(data.result == 1){
                                $.message.success('退款成功！请等待平台退款给买家。', 'reload');
                            }else {
                                $.message.error(data.message);
                            };
                        },
                        error : function() {
                            $.message.error('出现错误，请重试！');
                        }
                    });
                });
            };
        });

        function check(){
            if(!input.val()){
                input.parent().addClass('has-error error');
                return false;
            }else {
                input.parent().removeClass('has-error error');
            };

            return this;
        }
    })();

    //  货、款区分
    (function(){
        var type = $('#i_type').val();
        if(type == "return_money"){
            $('.i-type').html('款');
        }else if(type == "return_goods") {
            $('.i-type').html('货');
        };
    })();

    //  修复IE8（包括IE7）下样式问题
    (function(){
        if(lteIE8){
            //  IE7、8下h1高度太高问题
            $('.return-info').find('h1').css({
                paddingLeft: 10,
                paddingTop : 0,
                paddingRight: 10,
                paddingBottom: 0
            });
        };

    })();
});