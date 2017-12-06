/**
 * Created by Andste on 2016/11/22.
 */
$(function () {
    var module = new Module();
    var bonusItem;
    init();

    var backToCheckout = function () {location.replace('./checkout.html')};
    function init() {
        module.navigator.init({
            title: '优惠券',
            left : {click: function () {backToCheckout()}},
            right: false
        });
        module.scrollToTopControl.init({bottom: 0});
        bindEvents();
    }

    function bindEvents() {
        //  tab切换
        var items = $('.content-bonus').find('.items');
        $('.nav-checkout-bonus').on('click', '.item', function () {
            var $this = $(this),
                index = $this.index();
            $this.addClass('selected')
                 .siblings().removeClass('selected');
            items.removeClass('show')
                 .eq(index).addClass('show');
        });

        //  优惠券使用
        $('.can-use-bonus').on('tap', '.bonus-item', function () {
                bonusItem  = $(this);
            var store_id   = bonusItem.attr('store_id'),
                bonus_id   = bonusItem.attr('bonus_id'),
                is_checked = bonusItem.is('.checked'),
                is_cantUse = bonusItem.is('.unavailable');
            if(is_cantUse){return false}
            useBonus(store_id, bonus_id, is_checked);

            return false
        });

        //  检查是否有已使用优惠券，禁用其它。
        checkHasUsed();
    }

    //  优惠券使用
    function useBonus(store_id, bonus_id, is_checked) {
        if(is_checked){bonus_id = 0}
        module.layerLoading.open();
        $.ajax({
            url : ctx + '/api/shop/order-create/cart/'+ store_id +'/coupon/'+ bonus_id +'.do',
            type: 'POST',
            success: function (res) {
                module.layerLoading.close(function () {
                    if(res.result === 1){
                        is_checked ? (function () {
                                bonusItem.removeClass('checked');
                                bonusItem.siblings().removeClass('unavailable');
                            })() : (function () {
                                bonusItem.addClass('checked');
                                bonusItem.siblings().addClass('unavailable');
                            })()
                    }else {
                        module.message.error(res.message);
                    }
                }, 300)
            },
            error: function () {
                module.message.error('出现错误，请重试！');
            }
        })
    }

    //  检查是否有已使用优惠券，禁用其它。
    function checkHasUsed() {
        var stores = $('.store-item');
        for(var i = 0; i < stores.length; i ++){
            var _store = stores.eq(i);
            _store.find('.checked').length === 1 ? _store.find('.checked').siblings().addClass('unavailable') : null;
        }
    }
});