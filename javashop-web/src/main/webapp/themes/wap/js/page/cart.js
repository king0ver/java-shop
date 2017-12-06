/**
 * Created by Andste on 2016/12/8.
 */
$(function () {
    var module = new Module();
    init();

    function init() {
        module.navigator.init({ title: '购物车' });
        bindEvents();
    }

    function bindEvents() {

        //  初始化选中状态
        initCheckStatus();

        //  获取总价
        refreshCheckBarData(true);

        //  店铺商品选择
        storeCheck();

        //  商品选择
        goodsCheck();

        //  全选商品
        checkAll();

        //  滑动删除
        deleteGoods();

        //  去结算
        goBill();

        // 促销活动改变
        promotionChanged()
    }

    //  店铺商品选择
    function storeCheck() {
        $('.checkbox-store').on('tap', function () {
            var $this = $(this),
                _checked = $this.is('.checked'),
                data_id = $this.attr('data-id');
            _ajaxCheckStoreAll(!_checked, data_id, $this);

            return false;
        })
    }

    //  商品选择
    function goodsCheck() {
        $('.checkbox-goods').on('tap', function () {
            var $this = $(this),
                _checked = $this.is('.checked'),
                product_id = $this.data('product_id');
            _ajaxCheckdGoods(!_checked, product_id, $this);
            return false;
        });
    }

    //  全选商品
    function checkAll() {
        $(document).on('tap', '#goods-check-all', function () {
            var $this = $(this), _checked = $this.is('.checked');
            doCheckAll(!_checked);
            return false;
        })
    }

    //  全选操作
    function doCheckAll(_checked) {
        $.ajax({
            url: ctx + '/api/shop/order-create/cart.do',
            data: { checked: _checked ? 1 : 0 },
            type: 'post',
            success: function (res) {
                if (res.result !== 1) return;
                refreshCheckBarData();
                var checkItem = $('.checked-item');
                _checked ? checkItem.addClass('checked') : checkItem.removeClass('checked');
                checkAllStatus()
            }
        });
    }

    // 增加商品时的操作
    $(".symbol-add").on("tap", function () {
        var $this = $(this), productItem = $this.closest('.goods-item');
        var num = productItem.find("input[name='num']").val();
        num++;
        productItem.find("input[name='num']").val(num);

        var product_id = productItem.attr('data-product_id');
        var options = {
            url: ctx + '/api/shop/order-create/cart/product/' + product_id + '.do',
            data: {
                type: Math.random(),
                num: num
            },
            type: 'POST',
            success: function () {
                refreshCheckBarData()
            }
        };
        $.ajax(options);
        return false
    });
    //减少商品时的操作
    $(".symbol-less").on("tap", function () {
        var $this = $(this), productItem = $this.closest('.goods-item');
        var num = productItem.find('input[name=num]').val();
        if (num > 1) {
            num--;
            productItem.find('input[name=num]').val(num);
            var product_id = productItem.attr('data-product_id');
            var options = {
                url: ctx + '/api/shop/order-create/cart/product/' + product_id + '.do',
                data: {
                    type: Math.random(),
                    num: num
                },
                type: 'POST',
                success: function () {
                    refreshCheckBarData()
                }
            };
            $.ajax(options);
        }
        return false;
    });
    //更新购物车商品的操作
    function refreshCheckBarData(noreload) {
        if (noreload) {
            $.ajax({
                type: "get",
                url: ctx + "/api/shop/order-create/trade/price.do?_" + new Date().getTime(),
                dataType: "json",
                success: function (res) {
                    if (res.result === 1) {
                        $('#orderTotal').html(res.data.total_price.toFixed(2));
                    }
                },
                error: function (e) {
                    console.log(e)
                }
            });
        } else {
            location.reload()
        }
    }

    //  店铺商品选择ajax
    function _ajaxCheckStoreAll(checked, store_id, $this) {
        module.loading.open();
        $.ajax({
            url: ctx + '/api/shop/order-create/cart/seller/' + store_id + '.do',
            data: { checked: checked ? 1 : 0 },
            type: 'post',
            timeoutTimer: 8000,
            success: function (res) {
                if (res.result !== 1) return;
                var goodsCheckBoxs = $this.closest('.store-item').find('.checkbox-goods');
                checked ? goodsCheckBoxs.addClass('checked') : goodsCheckBoxs.removeClass('checked');
                checked ? $this.addClass('checked') : $this.removeClass('checked');
                checkAllStatus();
                refreshCheckBarData();
                module.loading.close();
            },
            error: function () {
                module.loading.close();
                module.message.error('出现错误，请重试！');
            }
        })
    }

    //  商品单个选择ajax
    function _ajaxCheckdGoods(checked, product_id, $this) {
        module.loading.open();
        $.ajax({
            url: ctx + '/api/shop/order-create/cart/product/' + product_id + '.do',
            data: { checked: checked ? 1 : 0 },
            type: 'POST',
            timeoutTimer: 8000,
            success: function (res) {
                if (res.result !== 1) return;
                var _closest = $this.closest('.goods-item');
                checked ? $this.addClass('checked') : $this.removeClass('checked');
                checked ? _closest.addClass('checked') : _closest.removeClass('checked');

                var _itemLen = _closest.parent().find('.goods-item').length;
                var _checkedLen = _closest.parent().find('.checkbox-goods.checked').length;

                var _checkStore = _closest.closest('.store-item').find('.checkbox-store');
                _itemLen === _checkedLen ? _checkStore.addClass('checked') : _checkStore.removeClass('checked');
                checkAllStatus();
                refreshCheckBarData();
                module.loading.close();
            },
            error: function () {
                module.loading.close();
                module.message.error('出现错误，请重试！');
            }
        })
    }

    //  滑动删除
    function deleteGoods() {
        var goodsItems = $('.goods-item');
        goodsItems.hammer().bind('swipeleft', function () {
            var $this = $(this);
            goodsItems.removeClass('active');
            $this.addClass('active');
            return false
        });

        goodsItems.hammer().bind('swiperight', function () {
            $(this).removeClass('active');
            return false
        });

        $('.cart').on('tap', '.delete-box', function (e) {
            e.stopPropagation();
            var $this = $(this);
            layer.open({
                content: '确定要移除该商品吗？',
                btn: ['确定', '取消'],
                yes: function (index) {
                    __XHR_removeItem($this);
                    layer.close(index);
                }
            });
            return false;
        });

        $(document).on('click', function () {
            goodsItems.removeClass('active');
        })
    }

    var __XHR_removeItem = function ($this) {
        var productItem = $this.is('.goods-item ') ? $this : $this.closest('.goods-item');
        var product_id = parseInt(productItem.attr('data-product_id'));
        var options = {
            url: ctx + '/api/shop/order-create/cart/product/' + product_id + '.do',
            type: 'delete',
            success: function (res) {
                if (res.result !== 1) return;
                refreshCheckBarData();
                productItem.remove();
                if ($this.closest('.store-item').find('.goods-item').length === 0) {
                    $this.closest('.store-item').remove()
                }
                if ($('.goods-item').length < 1) {
                    module.message.error('购物车内没有商品哦，先去逛逛吧！', function () {
                        history.back();
                    });
                    return false
                }
            }
        };
        $.ajax(options)
    };

    //  去结算
    function goBill() {
        $('.go-bill').on('tap', function () {
            //  判断是否有失效商品
            if ($('.goods-item').length < 1) {
                module.message.error('购物车内没有商品哦，先去逛逛吧！', function () {
                    history.back();
                });
                return false
            }

            if ($('.checked-item.checked').length < 1) {
                module.message.error('您没有勾选商品哦！');
                return false
            }

            location.href = ctx + '/checkout/checkout.html';
        });
    }

    function checkAllStatus() {
        var cart = $('.cart'), checkedAll = $('#goods-check-all');
        cart.find('.checkbox-store').length === cart.find('.checkbox-store.checked').length
            ? checkedAll.addClass('checked')
            : checkedAll.removeClass('checked');
    }

    // 初始化选中状态
    function initCheckStatus() {
        $('.inner-cart').each(function () {
            var $this = $(this);
            var items = $this.find('.checkbox-goods'), checkedItems = $this.find('.checkbox-goods.checked');
            var checkStore = $this.find('.checkbox-store');
            items.length === checkedItems.length
                ? checkStore.addClass('checked')
                : checkStore.removeClass('chekced')
        });

        checkAllStatus();
    }

    function promotionChanged() {
        $('.promotion-select').on('change', function () {
            var $this = $(this);
            var activity_id = $this.find("option:selected").val();
            var promotion_type = $this.find("option:selected").attr("promotion-type");
            var skuid = $this.find("option:selected").attr("skuid");
            var sellerid = $this.find("option:selected").attr("sellerid");

            var data = {
                activity_id: activity_id,
                promotion_type: promotion_type,
                skuid: skuid,
                sellerid: sellerid
            };

            $.ajax({
                url: ctx + "/api/shop/order-create/cart/promotion.do",
                type: "post",
                data: data,
                dataType: "json",
                success: function (res) {
                    res.result === 1 && location.reload();
                }
            });
        })
    };

    var selectDOM = $('select.promotion-select');
    if (selectDOM.length === 0) {
        return;
    }
    if (selectDOM.find('option').length === 0) {
        selectDOM.remove();
    } else if (selectDOM.find('option').length === 1) {
        selectDOM.removeClass('promotion-right-arrow');
    } else {
        selectDOM.addClass('promotion-right-arrow');
    }

});