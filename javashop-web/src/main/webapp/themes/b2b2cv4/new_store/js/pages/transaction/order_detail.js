/**
 * Created by Andste on 2016/6/17.
 */

$(function(){

    //  查看物流信息
    (function(){
        var btn = $('.order-exp-info');
        btn.unbind('click').on('click', function(){
            var _this = $(this), order_sn = _this.attr('order_sn');
            $.ajax({
                url: './order_exp_info.html?ordersn=' + order_sn,
                type: 'GET',
                success: function(html){
                    $.dialogModal({
                        title    : '物流信息',
                        html     : html,
                        btn      : false,
                        backdrop : false
                    });
                },
                error: function(){
                    $.message.error('出现错误，请重试！');
                }
            });
        });
    })();

    //  确认订单
    $("a[name='operation']").on('click', function(){
        var action = $(this).attr("action");
        var ordersn = $("#sn").val();
        var url="";
        if(action=="pay"){
            showPay();
            return false;
        }
        $.confirm('要确认订单吗？', function(){
        	$.ajax({
                url:ctx+"/order-opration/seller/order/confirm/"+ordersn+".do",
                data:"is_agree=true",
                method:"POST",
                dataType:"json",
                success:function (result) {
                	$.message.success("确认成功", 'reload');
                },
                error:function () {
                    console.error(" confirm error");
                }
            })
        })
    });

    //  修改收货人信息

    $("a[name='editOrderInfo']").on('click', function(){
    	var ordersn = $(this).attr('order_sn');   	
        $.ajax({
            url  : './order_info.html?ordersn=' + ordersn,
            type : 'GET',
            success: function(html){
                $.dialogModal({
                    title: '修改收货人信息',
                    html : html,
                    width: 650,
                    callBack: function(){
                        var id = $('#dialogModal #orderInfo');
                        var name     = id.find("input[name='ship_name']").val(),
                            mobile   = id.find("input[name='ship_mobile']").val(),
                            regionid = id.find('#region_id').val(),
                            address  = id.find("input[name='ship_addr']").val();
                        	zip  = id.find("input[name='ship_zip']").val();
                        if(!name){
                            $.message.error('姓名不能为空！');
                            return false;
                        }else if(!mobile){
                            $.message.error('手机号不能为空！');
                            return false;
                        }else if(!/^0?(13[0-9]|15[0-9]|18[0-9]|14[0-9]|17[0-9])[0-9]{8}$/.test(mobile)){
                            $.message.error('手机号格式不正确！');
                            return false;
                        }else if(regionid == 0){
                            $.message.error('地区信息不完整！');
                            return false;
                        }else if(!address){
                            $.message.error('详细地址不能为空！');
                            return false;
                        };

                        id.ajaxSubmit({
                            url  : ctx + '/order-opration/seller/order/'+ordersn+'/address.do',
                            type : 'POST',
                            success: function(result){
                               $.message.success("修改成功", 'reload');
                            },
                            error: function(){
                                $.message.error('出现错误，请重试！');
                            }
                        })
                    }
                });
            },
            error: function(){
                $.message.error('出现错误，请重试！');
            }
        });
    });

    //  调整价格
    $("a[name='editOrderPrice']").on('click', function(){
    	var ordersn = $(this).attr('order_sn');
        $.ajax({
            url :'./order_price_info.html?ordersn=' + ordersn,
            success : function(html) {
                $.dialogModal({
                    title: '调整价格',
                    html : html,
                    width: 300,
                    callBack: function(){
                    		var form= $('#dialogModal').find('#orderPriceForm');
                    		var paymoney=form.find("input[name='order_price']").val();
                    		if(!/^\d+(\.\d+)?$/.test(paymoney)){
                    			$.message.error('价格输入有误！');
                             return false;
                    		}
                    		form.ajaxSubmit({
                            url: ctx + '/order-opration/seller/order/'+ordersn+'/price.do',
                            type: 'POST',
                            success: function(result){
                               $.message.success("修改成功", 'reload');
                            },
                            error: function(){
                                $.message.error('出现错误，请重试！');
                            }
                        })
                    }
                });
            },
            error : function() {
                $.message.error('出现错误，请重试！');
            }
        });
    });

    //  确认收款
    $('#confirm_payment').on('click', function(){
        var _this = $(this);
        $.confirm('确认收到款项了吗？', function(){
            var order_id = _this.attr('order_id'),
                needpay_money = _this.attr('needpay_money'),
                payment_id = _this.attr('payment_id');
            $.ajax({
                url: ctx + '/api/store/store-order/cod-order-pay.do',
                data: {
                    orderId: order_id,
                    paymoney: needpay_money,
                    payment_id: payment_id
                },
                type: 'POST',
                success: function(result){
                    if(result && typeof result == 'string'){
                        result = JSON.parse(result);
                    };
                    if(result.result == 1){
                        $.message.success(result.message, 'reload');
                    }else {
                        $.message.error(result.message);
                    };
                },
                error: function(){
                    $.message.error('出现错误，请重试！');
                }
            });
        })
    });

    //  切换其它信息
    $("#order_info").click(function(){
        $("div[name='order_info']").slideDown();
        $("div[name='order_other']").slideUp();
    });
    $("#order_other").click(function(){
        $("div[name='order_info']").slideUp();
        $("div[name='order_other']").slideDown();
    });

    //  修复IE样式问题
    (function(){
        if(Sys.ie){
            $('#order_info').css({
                padding    : 0,
                height     : 30,
                lineHeight : '30px'
            });
            $('#order_other').css({
                padding    : 0,
                height     : 30,
                lineHeight : '30px'
            });
        };

        if(Sys.ie == 7){
            $('.order_intro').css({
                display: 'inline',
                zoom: 1
            });

            $('.intro_staus').css({
                display: 'inline',
                zoom: 1
            });

            $('.shipintro .order-btn3').css({float: ''});

        };
    })()
     $(window).keydown(function(e){
    		if(e.keyCode == 13 && $(e.target).is('input.form-control')){
    			$('#dialog-true-btn').click();
    			e.preventDefault();
    		}
    		return;
    	})
});