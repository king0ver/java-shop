
$(function () {
    var module = new Module();
    init();
    var backToCheckout = function () {location.replace('../checkout/checkout.html')};
    function init() {
        module.navigator.init({
            title: '支付配送',
            left : {click: function () {backToCheckout()}}
        });
        bindEvents();
    }

    function bindEvents() {
        //  配送方式选择
        $('.ship-btn').on('tap', function () {
            var $this    = $(this),
                store_id = $this.attr('store_id');
            if($this.is('.checked')){return false}
            return false
        });
    }

    //  支付方式选择
    $('.payment-btn').on('tap', function () {
        var $this = $(this);
        $this.addClass('checked').siblings().removeClass('checked');
        $('#paymentType').val($this.attr('pay_type'));
        return false
    });

    //  配送时间
    $('.ship_day').on('tap', function () {
        var $this = $(this);
        $this.addClass('checked').siblings().removeClass('checked');
        $('#receiveTime').val($this.html());
        return false
    });
    
    //  确定
    $('#save-btn').on('tap', function () {
        $.ajax({
            url: ctx + '/api/shop/order-create/checkout-param/payment-type.do',
            data: { payment_type: $('#paymentType').val()},
            type: 'POST',
            success: function (res) {
                if(res.result === 1) {
                    $.ajax({
                        url : ctx + '/api/shop/order-create/checkout-param/receivetime.do?',
                        data: { receivetime: $('#receiveTime').val() },
                        type: 'POST',
                        success: function (_res) {
                            _res.result === 1
                                ? backToCheckout()
                                : module.message.error(_res.message);
                        },
                        error: function () {
                            module.message.error('出现错误，请重试！');
                        }
                    })
                }else {
                    module.message.error(res.message);
                }
            },
            error: function () {
                module.message.error('出现错误，请重试！');
            }
        });
        return false
    });
});