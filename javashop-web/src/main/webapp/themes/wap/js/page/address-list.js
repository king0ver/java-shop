/**
 * Created by Andste on 2016/11/23.
 */
$(function () {
    var module = new Module();
    var urlStr = module.getQueryString('back');
    var isCheckout = urlStr === 'checkout';
    var refreshDom = '<div class="address-refresh-icon"></div>';
    init();

    var backToCheckout = function () {location.replace(ctx + '/checkout/checkout.html')};

    function init() {
        isCheckout ? module.navigator.init({title: '收货地址', left : {click: function () {backToCheckout()}}}) : module.navigator.init({title: '收货地址', right: {element: refreshDom, click: function () {location.reload()}}});
        bindEvents();
    }


    function bindEvents() {

        //  添加地址
        $('#add-btn').on('tap', function () {
            isCheckout ? location.replace('./address-add.html?back=' + urlStr) : location.href = './address-add.html';
        })

        //  编辑地址
        $('.address-list').on('tap', '.edit', function () {
            var $this  = $(this),
                add_id = $this.attr('address_id');
            isCheckout ? location.replace('./address-edit.html?address_id=' + add_id + '&back=' + urlStr) : location.href = './address-edit.html?address_id=' + add_id + '&back=' + urlStr;
        })

        //  删除手势绑定
        var Items = $('.address-list .item');
        Items.hammer().bind('swipeleft',function(){
            var $this = $(this);
            Items.removeClass('active');
            $this.addClass('active');
            return false
        })

        Items.hammer().bind('swiperight',function(){
            $(this).removeClass('active');
            return false
        })

        $(document).on('tap', function (e) {
            Items.removeClass('active');
        })

        //  删除地址
        $('.address-list').on('tap', '.delete', function () {
            var $this  = $(this),
                add_id = $this.attr('address_id');
            module.layerConfirm({
                content: '确定要删除这条地址吗？',
                yes: function () {
                    $.ajax({
                        url: ctx + '/api/shop/member-address/delete.do?addr_id=' + add_id,
                        type: 'POST',
                        success: function (res) {
                            if(res.result == 1){
                                location.reload();
                            }else {
                                module.message.error(res.message);
                            }
                        },
                        error: function () {
                            module.message.error('出现错误，请重试！');
                        }
                    })
                },
                no: function () {
                    console.log('不删别瞎点...')
                }
            });
            return false
        });

        //  结算页选择地址
        if(isCheckout){
            $('.address-list').on('tap', '.content', function () {
                $.ajax({
                    url    : ctx + '/api/shop/order-create/checkout-param/addressid/' + $(this).attr('address_id') + '.do',
                    type   : 'POST',
                    success: function (res) {
                        res.result === 1
                            ? backToCheckout()
                            : module.message.error(res.message);
                    },
                    error: function () {
                        module.message.error('出现错误，请重试！');
                    }
                })
            })
        }
    }
});