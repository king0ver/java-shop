
$(function () {
   var module = new Module();
    init();

    var backToCheckout = function () {location.replace('./checkout.html')};
    function init() {
        module.navigator.init({
            title: '发票信息',
            left : {click: function () {backToCheckout()}}
        });
        initIcheck();
        bindEvents();
    }

    function initIcheck() {
        $('.icheck').iCheck({
            checkboxClass: 'icheckbox_flat-red',
            radioClass: 'iradio_flat-red'
        });
    }
    
    function bindEvents() {
        $('.eui-checkbox-btn').on('click', function () {
            $(this).addClass('checked').siblings().removeClass('checked');
        });
		
        $('#save-btn').on('click', function () {
            var need_receipt = $('#dont-need-receipt').is('.checked') ? 'no' : 'yes';  //是否开具发票
            var receiptTitle = $('#receiptTitle').val();  //发票抬头
			var receiptDuty = $('#receiptDuty').val();  //纳税人识别号
            var receiptContent = $('.content-receipt .iradio_flat-red.checked').find('.icheck').val();  //发票内容
			
            if(need_receipt) {
                if(!receiptTitle) {
                    module.message.error('请填写发票抬头！');
                    return
                }else if(!receiptContent) {
                    module.message.error('请填写发票内容！');
                    return
                }else if((receiptTitle !== '个人' && !receiptDuty)){
                    module.message.error('非个人类型发票请填写纳税人识别号！');
                    return
                }
            }
        
            $.ajax({
            	url:ctx+'/api/shop/order-create/checkout-param/receipt.do',
            	data:{
            		need_receipt: need_receipt,
            		title : receiptTitle,
                    duty_invoice: receiptTitle === '个人' ? '' : receiptDuty,
            		content: receiptContent
            	},
            	type:'POST',
            	dataType:"json",
                success: function (res) {
                    res.result === 1
                        ? backToCheckout()
                        : module.message.error(res.message);
                },
				error: function () {
					module.message.error('出现错误，请稍候重试！')
                }
            })
        })
    }
});