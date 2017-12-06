/**
 * Created by Andste on 2017/4/13.
 */
$(function () {
    var Cart = {};
    Cart.elements = {
        cartContainer: $('.container-cart'),
        cartContent: $('.content-cart'),
        checkoutBar: $('.check-bar-cart'),
        checkBar: $('.check-bar-cart').find('.check-bar'),
        allItemNum: $('#allItemNum'),
        checkedNum: $('#checkedNum'),
        orderTotal: $('#orderTotal'),
        promotionTool: $('.promotion-tool')
    };
    Cart.params = {
        checkBarTop: 0,
        focusProductNum: 1,
        allItemsNum: 0
    };
    
    Cart.init = function () {
        this.__DOM_checkBarFiexd();
        this.__EVE_bindEvents();
        this.__FUN_initAllStatus();
        this.__FUN_countAllItemNum(false);
    };
    
    /*  事件绑定[EVE]
     ============================================================================ */
    Cart.__EVE_bindEvents = function () {
        var _this = this, ele = _this.elements;
        //  绑定全选
        ele.cartContainer.on('click', '.select-all', function () {
            _this.__XHR_checkAll($(this))
        });
        //  绑定店铺选择
        ele.cartContainer.on('click', '.check-store', function () {
            _this.__XHR_checkStore($(this))
        });
        //  绑定促销活动选择
        ele.cartContainer.on('click', '.check-activity', function () {
            _this.__FUN_checkActivity($(this));
        });
        //  绑定商品选择
        ele.cartContainer.on('click', '.check-item', function () {
            _this.__XHR_checkItem($(this))
        });
        //  绑定数量加减【加减号】
        ele.cartContainer.on('click', '.oper', function () {
            _this.__FUN_checkUpdateNum($(this))
        });
        //  绑定数据变更【获得焦点】
        ele.cartContainer.on('focus', '.product-num', function () {
            _this.params.focusProductNum = parseInt($(this).val())
        });
        //  绑定数量变更【失去焦点】
        ele.cartContainer.on('blur', '.product-num', function () {
            _this.__FUN_checkUpdateNum($(this))
        });
        //  给输入框绑定回车键
        ele.cartContainer.on('keyup', '.product-num', function (e) {
            e.which === 13 && _this.__FUN_checkUpdateNum($(this))
        });
        //  绑定商品项删除
        ele.cartContainer.on('click', '.delete-item', function () {
            var $this = $(this);
            window.layer.confirm('确定要将这个商品移出购物车吗？', { icon: 3, title: '提示' }, function (index) {
                _this.__XHR_removeItem($this);
                window.layer.close(index)
            });
        });
        //  绑定清空购物车
        ele.cartContainer.on('click', '.clean-all', function () {
            window.layer.confirm('确定要清空购物车吗？', { icon: 3, title: '提示' }, function (index) {
                _this.__XHR_cleanItems();
                window.layer.close(index)
            });
        });
        //  绑定批量删除
        ele.cartContainer.on('click', '.batch-delete', function () {
            window.layer.confirm('确定要将这些商品移出购物车吗？', { icon: 3, title: '提示' }, function (index) {
                _this.__FUN_batchDeleteItems();
                window.layer.close(index)
            });
        });
        //  绑定去结算
        ele.cartContainer.on('click', '.checkout-btn', function () {
            location.href = ctx + '/checkout.html'
        });
        // 修改参与的活动
        ele.cartContainer.on('change', '.promotion-tool', function () {
            _this.__FUN_editPromotion($(this));
        });
        return this;
    };
    
    /*  异步请求[XHR]
     ============================================================================ */
    /**
     * 全选异步请求
     * @param $this
     * @private
     */
    Cart.__XHR_checkAll = function ($this) {
        var _this = this, checked = $this.find('.check').is('.checked');
        var loading = window.layer.load(1);
        var options = {
            url: ctx + '/api/shop/order-create/cart.do',
            data: { checked: checked ? 0 : 1 },
            type: 'POST',
            success: function (res) {
                window.layer.close(loading);
                res.result === 1 ? (function () {
                    _this.__XHR_RefreshCartDatas()
                })() : (function () {
                    _this.message.failed(res.message)
                })()
            },
            error: function () {
                window.layer.close(loading);
                _this.message.error()
            }
        };
        $.ajax(options)
    };
    
    /**
     * 选择店铺商品项异步请求
     * @param $this
     * @private
     */
    Cart.__XHR_checkStore = function ($this) {
        var _this = this, checked = $this.is('.checked'), storeItemCart = $this.closest('.store-item-cart');
        var loading = window.layer.load(1);
        var store_id = parseInt(storeItemCart.data('store_id'));
        var options = {
            url: ctx + '/api/shop/order-create/cart/seller/'+ store_id +'.do',
            data: { checked: checked ? 0 : 1 },
            type: 'POST',
            success: function (res) {
                window.layer.close(loading);
                res.result === 1 ? (function () {
                    _this.__XHR_RefreshCartDatas()
                })() : (function () {
                    _this.message.failed(res.message)
                })()
            },
            error: function () {
                window.layer.close(loading);
                _this.message.error()
            }
        };
        $.ajax(options)
    };
    
    /**
     * 选择单个商品项异步请求
     * @param $this
     * @param _checked
     * @private
     */
    Cart.__XHR_checkItem = function ($this, _checked) {
        var _this = this, checked = _checked !== undefined ? _checked : $this.is('.checked'),
            productItem = $this.closest('.product-item-cart');
        var loading = window.layer.load(1);
        var product_id = productItem.data('product_id');
        var options = {
            url: ctx + '/api/shop/order-create/cart/product/' + product_id + '.do',
            data: { checked: checked ? 0 : 1 },
            type: 'POST',
            success: function (res) {
                window.layer.close(loading);
                res.result === 1 ? (function () {
                    _this.__XHR_RefreshCartDatas();
                })() : (function () {
                    _this.message.failed(res.message)
                })()
            },
            error: function () {
                window.layer.close(loading);
                _this.message.error()
            }
        };
        $.ajax(options)
    };
    
    /**
     * 更新商品项数量异步请求
     * @param $this
     * @param num
     * @private
     */
    Cart.__XHR_updateProductNum = function ($this, num) {
        var _this = this, productItem = $this.closest('.product-item-cart');
        var loading = window.layer.load(1);
        var product_id = productItem.data('product_id');
        var goods_id = productItem.data('goods_id');
        
        var options = {
            url: ctx + '/api/shop/order-create/cart/product/' + product_id + '.do',
            data: {
                goodsid: goods_id,
                type: Math.random(),
                num: num
            },
            type: 'POST',
            success: function (res) {
                window.layer.close(loading);
                res.result === 1 ? (function () {
                    _this.__XHR_RefreshCartDatas()
                })() : (function () {
                    _this.message.failed(res.message);
                    _this.__DOM_updateProductNum($this, _this.params.focusProductNum);
                })()
            },
            error: function () {
                window.layer.close(loading);
                _this.message.error();
                _this.__DOM_updateProductNum($this, _this.params.focusProductNum);
            }
        };
        $.ajax(options)
    };
    
    /**
     * 删除商品项异步请求
     * @param $this
     * @private
     */
    Cart.__XHR_removeItem = function ($this) {
        var _this = this;
        var productItem = $this.is('.product-item-cart') ? $this : $this.closest('.product-item-cart');
        var loading = window.layer.load(1);
        var product_id = parseInt(productItem.data('product_id'));
        var options = {
            url: ctx + '/api/shop/order-create/cart/product/' + product_id + '.do',
            type: 'delete',
            success: function (res) {
                window.layer.close(loading);
                res.result === 1 ? (function () {
                    _this.message.success('移出购物车成功！');
                    _this.__XHR_RefreshCartDatas()
                })() : (function () {
                    _this.message.failed(res.message);
                })()
            },
            error: function () {
                window.layer.close(loading);
                _this.message.error()
            }
        };
        $.ajax(options)
    };
    
    /**
     * 清空购物车异步请求
     * @private
     */
    Cart.__XHR_cleanItems = function () {
        var _this = this;
        var loading = window.layer.load(1);
        var options = {
            url: ctx + '/api/shop/order-create/cart.do',
            type: 'delete',
            success: function (res) {
                window.layer.close(loading);
                res.result === 1 ? (function () {
                    _this.__XHR_RefreshCartDatas()
                })() : (function () {
                    _this.message.failed(res.message)
                })()
            },
            error: function () {
                window.layer.close(loading);
                _this.message.error()
            }
        };
        $.ajax(options)
    };
    
    /**
     * 刷新整个购物车数据
     * @private
     */
    Cart.__XHR_RefreshCartDatas = function () {
        var _this = this;
        $('.list-content-cart').load(ctx + '/cart/cart-item.html', function () {
            _this.__XHR_RefreshCheckBarData();
            _this.__DOM_countAllCheckStatus();
        });
    };
    
    /**
     * 加载空购物车提示
     * @private
     */
    Cart.__XHR_loadEmptyHtml = function () {
        this.elements.cartContainer.find('.list-content-cart').load(ctx + '/cart/cart-empty.html');
    };
    
    /**
     * 刷新结算栏数据
     * @returns {Cart}
     * @private
     */
    Cart.__XHR_RefreshCheckBarData = function () {
        var _this = this;
        $.ajax({
            url: ctx + '/api/shop/order-create/cart/total.do',
            type: 'GET',
            dataType: 'json',
            success: function (res) {
                res.result === 1
                    ? _this.__DOM_RefreshCheckBarData(res.data)
                    : $.message.error(res.message);
            },
            error: function () {
                $.message.error('出现错误，请稍候重试！')
            }
        });
    };
    
    /* DOM操作[DOM]
     ============================================================================ */
    /**
     * 计算结算栏浮动与否
     * @returns {Cart}
     */
    Cart.__DOM_checkBarFiexd = function () {
        var _this = this;
        var checkBar = this.elements.checkBar;
        _this.params.checkBarTop = getOffsetTop(checkBar[0]);
        $(document).scroll(function () { countFiexd() });
        var countFiexd = function () {
            var innerHeight = window.innerHeight - 60;
            var bodyScrollTop = document.documentElement.scrollTop || document.body.scrollTop;
            bodyScrollTop > _this.params.checkBarTop
                ? checkBar.addClass('fiexd-top')
                : checkBar.removeClass('fiexd-top');
            bodyScrollTop < (_this.params.checkBarTop - innerHeight)
                ? checkBar.addClass('fiexd-bottom')
                : checkBar.removeClass('fiexd-bottom');
        };
        
        function getOffsetTop(e) {
            var offsetTop = e.offsetTop;
            e.offsetParent !== null && (offsetTop += getOffsetTop(e.offsetParent));
            return offsetTop;
        }
        
        countFiexd();
        return this;
    };
    
    /**
     * 是否勾选全部按钮【勾选后调用是否禁用结算按钮方法】
     * @param checked
     * @returns {Cart}
     * @private
     */
    Cart.__DOM_checkAll = function (checked) {
        var checks = this.elements.cartContainer.find('.check-all');
        checked === true ? (function () {
            checks.addClass('checked');
        })() : (function () {
            checks.removeClass('checked');
        })();
        this.__DOM_disabledCheckoutBtn();
        return this;
    };
    
    /**
     * 是否勾选全部商品项
     * @param checked
     * @returns {Cart}
     * @private
     */
    Cart.__DOM_checkAllItem = function (checked) {
        var checkItems = this.elements.cartContent.find('.check-item'),
            products = this.elements.cartContent.find('.product-num');
        checked === true ? (function () {
            checkItems.addClass('checked');
            products.addClass('checked')
        })() : (function () {
            checkItems.removeClass('checked');
            products.removeClass('checked')
        })();
        this.__DOM_disabledCheckoutBtn();
        return this;
    };
    
    /**
     * 更新数量到输入框 同时更新小计
     * @param $this
     * @param num
     * @returns {Cart}
     * @private
     */
    Cart.__DOM_updateProductNum = function ($this, num) {
        num < 2 && $this.siblings('.minus').addClass('unable');
        num > 1 && $this.siblings('.minus').removeClass('unable');
        $this.val(num);
        var productItem = $this.closest('.product-item-cart');
        var _weight = num * productItem.attr('data-weight'),
            _money = num * productItem.attr('data-price');
        productItem.find('.weight-item').attr('title', _weight + 'g')
                   .html(_weight > 999 ? (_weight / 1000).toFixed(3) + 'kg' : _weight.toFixed(2) + 'g');
        productItem.find('.money-item').attr('title', _money).html((_money).toFixed(2));
        return this;
    };
    
    /**
     * 使用jquery animate改变高度，完成后再移除, 并更新结算栏信息
     * @param $this
     * @param callback
     * @returns {Cart}
     * @private
     */
    Cart.__DOM_removeItem = function ($this, callback) {
        var _this = this;
        $this.animate({ height: 0 }, {
            complete: function () {
                var itemCart = $this.closest('.item-cart');
                $this.remove();
                _this.params.checkBarTop -= 116;
                var storeItemNum = _this.__FUN_countStoreCheckItemNum(itemCart, false);
                storeItemNum === 0 && (function () {
                    itemCart.remove();
                    _this.params.checkBarTop -= 129;
                    _this.__FUN_countAllItemNum(false) === 0 && (function () {
                        _this.params.checkBarTop += 351;
                        _this.__XHR_loadEmptyHtml();
                        _this.__DOM_checkAll(false)
                    })();
                })();
                callback && typeof (callback) === 'function' && callback();
            }
        });
        return this;
    };
    
    /**
     * 计算是否禁用结算按钮
     * @returns {Cart}
     * @private
     */
    Cart.__DOM_disabledCheckoutBtn = function () {
        var checkoutBtn = $('.checkout-btn'),
            disabled = this.__FUN_countAllItemNum(true) === 0;
        disabled === true
            ? checkoutBtn.addClass('disabled')
            : checkoutBtn.removeClass('disabled');
        return this;
    };
    
    /**
     * 刷新结算栏订单金额等信息
     * @param orderPrice
     * @returns {Cart}
     * @private
     */
    Cart.__DOM_RefreshCheckBarData = function (orderPrice) {
        var _allItemsNum = this.__FUN_countAllItemNum(false);
        this.params.allItemsNum = _allItemsNum;
        this.elements.allItemNum.html(_allItemsNum);
        this.elements.checkedNum.html(this.__FUN_countAllItemNum(true));
        this.params.allItemsNum = _allItemsNum;
        orderPrice && this.elements.orderTotal.html(orderPrice.total_price);
        this.__DOM_disabledCheckoutBtn();
        return this;
    };
    
    /**
     * 计算当前全选状态
     * @returns {Cart}
     * @private
     */
    Cart.__DOM_countAllCheckStatus = function () {
        var allCheck = $('.check-all'),
            allStore = $('.check-store').length,
            allStoreChecked = $('.check-store.checked').length;
        allStore === allStoreChecked
            ? allCheck.addClass('checked')
            : allCheck.removeClass('checked');
        return this;
    };
    
    /* 计算方法[FUN]
     ============================================================================ */
    
    /**
     * 计算选中、商品总数量
     * @param checked       获取选中商品
     * @returns {number}
     */
    Cart.__FUN_countAllItemNum = function (checked) {
        var num = 0;
        var productNums = checked ? $('.product-num.checked') : $('.product-num'),
            _length = productNums.length;
        for (var i = 0; i < _length; i++) {
            num += parseInt(productNums.eq(i)[0].value)
        }
        !checked && (this.params.allItemsNum = num);
        return num;
    };
    
    /**
     * 当有促销活动时，选择或者取消选择促销活动
     * @param $this
     * @private
     */
    Cart.__FUN_checkActivity = function ($this) {
        var _this = this, checked = $this.is('.checked');
        var group = $this.closest('.group-item-cart');
        var products = [];
        group.find('.check-item').each(function () {
            var product_id = $(this).closest('.product-item-cart').data('product_id');
            products.push(product_id)
        });
        var loading = window.layer.load(1);
        $.ajax({
            url: ctx + '/api/shop/order-create/cart/product/' + products.join(',') + '.do',
            data: { checked: checked ? 0 : 1 },
            type: 'POST',
            success: function (res) {
                window.layer.close(loading);
                res.result === 1 ? (function () {
                    _this.__XHR_RefreshCartDatas();
                })() : (function () {
                    _this.message.failed(res.message)
                })()
            },
            error: function () {
                window.layer.close(loading);
                _this.message.error()
            }
        })
    };
    
    /**
     * 在发起更新数量ajax之前，做检查操作。
     * @param $this
     * @returns {Cart}
     * @private
     */
    Cart.__FUN_checkUpdateNum = function ($this) {
        var num, is_unable = $this.is('.unable'), is_input = $this.is('.product-num'), _minus, _add;
        //  如果按钮有禁用的class，则{ return }
        if (is_unable) {
            return this
        }
        var input = is_input ? $this : $this.siblings('.product-num'), _val = input.val();
        //  如果是输入框，则{ 需要获取到附近的加减按钮，用来禁用或者启用它 }
        is_input && (_minus = $this.siblings('.minus')) && (_add = $this.siblings('.add'));
        var is_NaN = isNaN(parseInt(_val));
        //  正则匹配。 如果是num是int类型，则{ num直接取值 }
        //  否则{ 如果是判断为NaN，则num赋值为null }
        num = Base.regExp.integer.test(_val)
            ? parseInt(_val)
            : (is_NaN ? null : parseInt(_val));
        //  如果点击的是加减号 则{ 将num赋值给全局变量【原始数据: 获得焦点时的值】}
        !is_input && (this.params.focusProductNum = num || 1);
        //  如果为NaN 或者 num小于1 或者 (是输入框并且新的值等于原始值)，则{ 把获取到焦点时的值赋值回输入框，并且return }
        if (is_NaN || num < 1 || (is_input && num === this.params.focusProductNum)) {
            input.val(this.params.focusProductNum);
            return this;
        }
        //  下面是输入值和加减按钮逻辑
        is_input ? (function () {
            //  如果是输入框失去焦点的情况
            //  如果值大于1，则{ 移除掉禁用class，否则{ 如果值 = 0，num赋值为1，并且给减号添加禁用class } }
            num > 1 ? _minus.removeClass('unable') : (function () {
                _minus.addClass('unable');
            })()
        })() : (function () {
            //  如果是点击加减号的情况
            $this.is('.add') ? (function () {
                //  如果点的是加号，则{ num+=1，并且把减号的禁用class去除掉 }
                num += 1;
                $this.siblings('.minus').removeClass('unable');
            })() : (num > 2 ? num -= 1 : (function () {
                //  如果点的是减号，并且{ num大于2 }，则 { num-=1 }
                //  否则 { num赋值为1，并且给减号添加禁用class }
                num = 1;
                $this.addClass('unable');
            })());
        })();
        this.__XHR_updateProductNum(input, num);
        return this;
    };
    
    /**
     * 初始化时调用所有方法
     * @returns {Cart}
     * @private
     */
    Cart.__FUN_initAllStatus = function () {
        var _this = this;
        _this.__DOM_countAllCheckStatus();
        _this.__XHR_RefreshCheckBarData();
        return this;
    };
    
    /**
     * 计算当前点击的商品的店铺已选项目个数、总个数
     * @param $this
     * @param checked
     * @private
     */
    Cart.__FUN_countStoreCheckItemNum = function ($this, checked) {
        var closest = $this.is('.item-cart') ? $this : $this.closest('.item-cart');
        var items = checked === true ? closest.find('.check-item.checked') : closest.find('.check-item');
        return items.length;
    };
    
    /**
     * 批量删除
     * @private
     */
    Cart.__FUN_batchDeleteItems = function () {
        var _this = this;
        var checkeds = _this.elements.cartContainer.find('.product-num.checked');
        
        var productIds = [];
        var loading;
        checkeds.each(function(index){
            var $this = $(this);
            var productItem = $this.is('.product-item-cart') ? $this : $this.closest('.product-item-cart');
            loading = window.layer.load(1);
            productIds[index] = parseInt(productItem.attr('data-product_id'));
        });
        
        var options = {
            url: ctx + '/api/shop/order-create/cart/product/' + productIds + '.do',
            type: 'delete',
            success: function (res) {
                window.layer.close(loading);
                if(res.result === 1){
                    _this.__XHR_RefreshCartDatas();
                }else{
                    _this.message.failed(res.message)
                }
            },
            error: function () {
                window.layer.close(loading);
                _this.message.error()
            }
        };
        $.ajax(options)
        
    };
    
    /**
     * 修改参与的活动，并计算价格
     */
    Cart.__FUN_editPromotion = function ($this) {
        var _this = this;
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
            type: "post",
            url: ctx + "/api/shop/order-create/cart/promotion.do",
            data: data,
            dataType: "json",
            success: function (res) {
                res.result === 1
                    ? _this.__XHR_RefreshCartDatas()
                    : $.message.error(res.message)
            }
        });
    };
    
    /* 弹窗和消息提示[UI]
     ============================================================================ */
    Cart.message = (function () {
        var showMsg = function (content, icon, callback) {
            window.layer.msg(content || '出现错误，请稍后重试！', { time: 2000, icon: icon }, function () {
                callback && typeof (callback) === 'function' && callback();
            });
        };
        return {
            error: function (_content, _callback) { showMsg(_content, 2, _callback) },
            failed: function (_content, _callback) { showMsg(_content, 5, _callback) },
            success: function (_content, _callback) { showMsg(_content || '操作成功！', 1, _callback) }
        }
    })();
    
    window.__Cart__ = Cart;
    Cart.init();
});