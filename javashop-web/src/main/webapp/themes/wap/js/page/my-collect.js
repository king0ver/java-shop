/**
 * Created by Andste on 2016/12/14.
 */
$(function () {
    var module = new Module();
    var type;
    var collectNav = $('.collect-nav'), collectContent = $('.collect-content');
    init();

    function init() {
        module.navigator.init({
            title: '我的收藏',
            left: {
				click: function () {
                    location.href = ctx + '/member.html'
				}
            }
        });
        type = module.getQueryString('type') || null;
        bindEvents();
        changeType(type);
    }


    function bindEvents() {
        collectNav.on('tap', '.nav-item', function () {
            changeType($(this).attr('data-type'));
            return false
        })

        //  取消收藏
        deleteCollect();

        //  加入购物车
        addToCart();

        //  取消店铺收藏
        deleteStoreCollect();
    }

    function changeType(type) {
        collectNav.find('.nav-' + type).addClass('active')
            .siblings().removeClass('active');
        collectContent.find('.content-' + type).addClass('show')
            .siblings().removeClass('show');
    }

    //  取消收藏
    function deleteCollect() {
        $('.goods-detai-buttons').on('tap', '.delete', function () {
            var $this = $(this),
                _id = $this.attr('data-id');
            module.loading.open();
            module.layerConfirm({
                content: '取消收藏该商品吗？',
                yes: function () {
                    _ajax();
                }
            })

            function _ajax() {
                $.ajax({
                    url: ctx + '/api/shop/collect/cancel-collect.do',
                    data: { favorite_id: _id },
                    type: 'POST',
                    success: function (res) {
                        if (res.result == 1) {
                            module.message.success('取消成功！', function () {
                                location.replace(ctx + '/member/my-collect.html?type=goods');
                            });
                        } else {
                            module.loading.close();
                            module.message.error(res.message);
                        }
                    },
                    error: function () {
                        module.loading.close();
                        module.message.error('出现错误，请重试！');
                    }
                })
            }

            return false
        })
    }


    //  添加到购物车
    function addToCart() {
        $('.goods-detai-buttons').on('tap', '.to-cart', function () {
            var $this = $(this),
                _id = $this.attr('data-id');

            $.ajax({
                url: ctx + '/api/shop/order-create/cart/product.do?skuid=' + _id + '&num=1',
                data: {
                    goodsid: _id,
                    num: 1
                },
                type: 'POST',
                success: function (res) {
                    if (res.result == 1) {
                        module.layerConfirm({
                            content: '添加到购物车成功，要去看看吗？',
                            yes: function () {
                                location.href = ctx + '/cart.html'
                            }
                        });
                    } else {
                        module.message.error(res.message);
                    }
                },
                error: function () {
                    module.message.error('出现错误，请重试！');
                }
            })
        })
    }


    function deleteStoreCollect() {
        $('.store-item-btns').on('tap', '.delete', function () {
            var $this = $(this),
                store_id = $this.attr('store-id'),
                colletc_id = $this.attr('collect-id');

            module.layerConfirm({
                content: '取消收藏该店铺吗？',
                yes: function () {
                    _ajax();
                }
            })

            function _ajax() {
                $.ajax({
                    url: ctx + '/api/b2b2c/store-collect/del.do',
                    data: {
                        store_id: store_id,
                        celloct_id: colletc_id
                    },
                    type: 'POST',
                    success: function (res) {
                        if (res.result == 1) {
                            module.message.success('取消成功！', function () {
                                location.replace(ctx + '/member/my-collect.html?type=store');
                            });
                        } else {
                            module.message.error(res.message);
                        }
                    },
                    error: function () {
                        module.message.error('出现错误，请重试！');
                    }
                })
            }
            return false
        })
    }
})