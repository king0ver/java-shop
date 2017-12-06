/**
 * Created by Andste on 2016/11/18.
 */
$(function () {
    var module = new Module();
    init();

    //  初始化
    function init() {
        module.navigator.init('填写订单');
        bindEvents();
    }

    function bindEvents() {

        //  跳转到新建地址
        $('.no-address').on('tap', function () {
            location.replace('../member/address-add.html?back=checkout');
            return false
        });

        //  备注信息
        $('#open-remark').on('tap', function () {
            openRemark();
            return false
        });

        //  提交订单
        $('#cerate-order').on('click', function () {
            cerateOrder();
        })
    }

    //  打开备注输入框
    function openRemark() {
        var _input;
        $.ajax({
            url: './checkout-remark.html',
            type: 'GET',
            success: function (html) {
                layer.open({
                    content: html,
                    btn: ['确定', '取消'],
                    yes: function (index) {
                        var _remark = $("#input-remark").val();
                        $.ajax({
                            url: ctx + '/api/shop/order-create/checkout-param/remark.do',
                            data: { remark: _remark },
                            type: "POST",
                            success: function (res) {
                                res.result === 1
                                    ? location.reload()
                                    : module.message.error(res.message);
                            },
                            error: function () {
                                layer.close(index);
                                module.message.error('出现错误，请稍候重试！')
                            }
                        });
                    },
                    success: function () {
                        _input = $('#input-remark');
                        $('#clean-remark').on('tap', function () {
                            _input.val('').focus();
                            return false
                        })
                    }
                });
            }
        })
    }

    //  提交订单
    function cerateOrder() {
        if ($('.checkout .address-list').find('.no-address').length === 1) {
            module.message.error('请编辑收货地址信息!');
            return;
        }
        $.ajax({
            url: ctx + '/api/shop/order-create/trade.do',
            type: 'POST',
            success: function (res) {
                res.result === 1
                    ? (location.href = './checkout-success.html?tradesn=' + res.data['trade_sn'])
                    : module.message.error('创建订单出错：' + res.message);
            },
            error: function () {
                module.message.error('出现错误，请重试！');
            }
        })
    }
});