/**
 * Created by Andste on 2017/6/2.
 */
$(function () {
    function HeaderCart() {
        var headerCart = $('.header-cart'),
            cartBox = headerCart.find('#cart-box'),
            cartNum = $('.cart-num'),
            cartTotal = headerCart.find('.cart-total');
        _loadCartItem();
        //  鼠标移入移出事件
        headerCart.on('mouseenter mouseleave', function (event) {
            var $this = $(this), mouseenter = event.type === 'mouseenter';
            mouseenter ? $this.addClass('hover') && _loadCartItem() : $this.removeClass('hover');
        });

        //  删除事件
        headerCart.on('click', '.remove', function () {
            var $this = $(this), 
            _item = $this.closest('.item-header-cart'), 
            cartId = _item.attr('data-item_id'),
            productId = _item.attr('data-item_product_id');
            $.ajax({
                url: ctx + '/api/shop/order-create/cart/product/'+productId+'.do',
                method:"DELETE",
                success: function (res) {
                    res.result === 1 && (function () {
                        _item.animate({height: 0}, 300, function () { _loadCartItem() })
                    })()
                }
            })
        });

        //  数量加减事件
        headerCart.on('click', '.count-oper', function () {
            var $this = $(this), _item = $this.closest('.item-header-cart'), isAdd = $this.is('.count-add');
            var _numEle = _item.find('.num'), _num = parseInt(_numEle.html());
            if(!isAdd && _num < 2) return;
            isAdd ? _num += 1 : _num -= 1;
            var cartId = _item.attr('data-item_id'), productId = _item.attr('data-item_product_id');
            $.ajax({
                url: ctx + '/api/shop/order-create/cart/product/'+productId+'.do',
                data: { type: Math.random(), num: _num},
                type: "post",
                success: function (res) {
                    res.result === 1 && (function () {
                        var _store = res.data['store'];
                        if(isAdd && _num > _store){ $.message.error('超出库存数量！'); return }
                        _numEle.html(_num);
                        _item.find('.item-total').html((_item.attr('data-item_price') * _num).toFixed(2));
                        _loadCartCount();
                    })()
                }
            })
        });

        //  加载购物项
        function _loadCartItem() {
            cartBox.load(ctx + '/common/header_cart_item.html', function () {
                _loadCartCount();
            });
        }

        /**
         * 获取购物车数量、总价
         * @private
         */
        function _loadCartCount() {
            $.ajax({
                url: ctx + '/api/shop/order-create/cart/num-and-price.do',
                success: function (res) {
                    res.result === 1 && (function () {
                        cartNum.html(res.data['total-num']);
                        cartTotal.html(res.data['total-price'].toFixed(2));
                    })()
                }
            })
        }

        return {
            //  暴露接口
            refresh: _loadCartItem
        }
    }

    window.headerCart = new HeaderCart();
});