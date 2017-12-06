var Products = [];
var Spec = {
    init: function (goodsid) {
        var self = this;
        $("#buyNow").on('click', function () {
            if ($(this).hasClass('not-oper')) {
                // 如果库存为0
                return;
            }
            var $this = $(this);
            self.addToCart($this);

        });
        $(".addToCart").on('click', function () {
            if ($(this).hasClass('not-oper')) {
                // 如果库存为0
                return;
            }
            var $this = $(this);
            self.addToCart($this);
        });
        $(".addGroupbuyGoods").on('click', function () {
            var $this = $(this);
            self.addGroupBuy($this);
        });


    }
    ,
    specClick: function (specLink) {
        specLink.addClass("on").siblings().removeClass('on');
        this.refresh(specLink);
    },

    //根据当前选择的规格找到货品
    findProduct: function (vidAr) {
        var pros = [];
        //判断两个数组元素值是否相同，不判断位置情况
        function arraySame(ar1, ar2) {
            for (var i in ar1) {
                if ($.inArray(ar1[i], ar2) === -1) { //不存在
                    return false;
                }
            }
            return true;
        }
        for (var i in Products) {
            var product = Products[i];
            if (arraySame(vidAr, product['specs'])) {
                pros[pros.length] = product;
            }
        }
        return pros;
    }
    ,
    refresh: function (specLink) {
        var product_ar = [];
        $(".pro-list_box li.on").each(function () {
            var $this = $(this);
            product_ar[product_ar.length] = parseInt($this.attr("specvid"));
        });

        var pro = this.findProduct(product_ar);
        for (var i in Refresh) {
            Refresh[i].refresh(pro, specLink, product_ar);
        }
        if (pro.length === 1) {
            $(".enable_store").html(pro[0].enable_store);
            $('#stock').val(pro[0].enable_store);
            $("input[name='productid']").val(pro[0].product_id);
        }
    },

    //添加购物车
    addToCart: function (btn) {
        var is_exchange_ele = $("#is_exchange");
        var checkState = is_exchange_ele[0] && is_exchange_ele[0].checked;
        if (checkState && !isLogin) {
            //积分兑换选中,并且未登录
            $.message.error('请先登录！');
            return false
        }

        var self = this;
        $.Loading.show("请稍候...");
        btn.attr("disabled", true);
        var options = {
            url: ctx + "/api/shop/order-create/cart/product.do",
            dataType: "json",
            type: "post",
            cache: false,
            success: function (result) {
                $.Loading.hide();
                if (result.result === 1) {
                    if (btn.is('.addToCart')) {
                        self.showAddSuccess();
                    } else {
                        window.location.href = ctx + "/cart.html";
                    }
                } else {
                    $.message.error(result.message);
                }
                btn.attr("disabled", false);
            },
            error: function () {
                $.Loading.hide();
                $.message.error('抱歉,发生错误');
                btn.attr("disabled", false);
            }
        };
        $("#goodsForm").ajaxSubmit(options);
    },
    showCartCount: function () {
        headerCart.refresh();
    },
    showAddSuccess: function () {
        this.showCartCount();
        layer.confirm('加入购物车成功！要去看看吗？', { icon: 1, title: '提示' }, function (index) {
            layer.close(index);
            location.href = ctx + '/cart.html'
        });
    },
    addGroupBuy: function (btn) {
        var self = this;
        $.Loading.show("请稍候...");
        btn.attr("disabled", true);
        var options = {
            url: ctx + "/api/store/store-cart/add-goods.do?ajax=yes&store_id=" + $("#storeid").val(),
            dataType: "json",
            async: false,
            cache: false,             //清楚缓存，暂时测试，如果产生冲突，请优先考虑是否是这条语句。
            success: function (result) {
                $.Loading.hide();
                if (result.result === 1) {
                    self.showAddSuccess();
                } else {
                    $.message.error(result.message);
                }
                btn.attr("disabled", false);
            },
            error: function () {
                $.Loading.hide();
                $.message.error('抱歉,发生错误');
                btn.attr("disabled", false);
            }
        };
        $("#goodsForm").ajaxSubmit(options);
    }
};
var StateRefresh = {
    ArrrRemove: function (ar, obj) {
        var new_ar = [];
        for (var i in ar) {
            if (obj !== ar[i]) {
                new_ar.push(ar[i]);
            }
        }
        return new_ar;
    },

    refresh: function (pro, specLink, product_ar) {
        var self = this;
        if (product_ar.length > 0) {
            //从目前未选中的规格中循环
            $(".item-spec").not('.on').each(function () {
                var $this = $(this);
                var proar = product_ar;
                $(".item-spec").not('.on').each(function () {
                    var specvid = parseInt($(this).attr("specvid"));
                    proar = self.ArrrRemove(proar, specvid);
                });

                var specvid = parseInt($this.attr("specvid"));
                proar.push(specvid);

                var result = Spec.findProduct(proar);
                if (!result || result.length === 0) {
                    $this.addClass("disabled");
                } else {
                    $this.removeClass("disabled");
                }
                proar.pop();
            });
        }
    }
};
var SelectTipRefresh = {
    refresh: function (pro) {
        return;
        var i = 0;
        var specHtml = "";
        $(".pro-list_box li.on").each(function () {
            if (i === 0) specHtml = "";
            if (i !== 0) specHtml += "、";
            specHtml += $(this).attr("title") + "";
            i++;
        });
        if (i > 0) {
            specHtml = "<dt>您已选择：</dt>" + "<dd><font color='red'>" + specHtml + "</font></dd>";
        } else {
            specHtml = "<dt>请选择：</dt><dd>下列规格</dd>";
        }
        //$(".spec-tip").html(specHtml);
    }
};
var PriceRefresh = {
    refresh: function (pro) {

        if (pro.length === 1) {
            $("#secooPriceJs").html(price_format(pro[0].price));
            $("#goodsSnJs").html('商品编码：' + pro[0].sn);     //更改商品货号
            $("#productIdJs").val(pro[0].product_id);
        } else {
            var maxPrice = 0, minPrice = -1;
            for (var i in pro) {
                if (maxPrice < pro[i].price) {
                    maxPrice = pro[i].price;
                }
                if (minPrice === -1 || minPrice > pro[i].price) {
                    minPrice = pro[i].price;
                }
            }
            $("#secooPriceJs").html(price_format(minPrice) + "-" + price_format(maxPrice));
        }
    }
};
function canBuy() {
    $("#buyNow").css("cursor", "pointer");
    $("#buyNow").tip({ 'disable': true });
    $("#buyNow").removeClass('disabled');
    $("#addCart").removeClass('disabled');
}

function cantbuy() {
    $("#buyNow").css("cursor", "not-allowed");
    $("#addCart").css("cursor", "not-allowed");
}

var BtnTipRefresh = {
    refresh: function (pro) {
        $("#buyNow").attr('tip', '');
        $("#addCart").attr('tip', '');

        $("#buyNow").addClass('disabled');
        $("#addCart").addClass('disabled');

        if (pro.length === 1) {
            if (pro[0].enable_store == 0) {
                cantbuy();
                $("#addCart,#buyNow").tip({ 'disable': false, className: "cantbuy", text: "此商品库存不足" });
            } else {
                canBuy();
            }
        } else {
            var i = 0, tip = '';
            $("#goodsForm .spec-item em").each(function () {
                var em = $(this);
                if (em.attr("class") !== 'checked') {
                    if (i !== 0) tip += "、";
                    tip += em.text();
                    i++;
                }
            });
            $("#addCart,#buyNow").tip({ 'disable': false, className: "cantbuy", text: "请选择:" + tip });
            cantbuy();
        }
    }
};
var Refresh = [SelectTipRefresh, PriceRefresh, BtnTipRefresh, StateRefresh];

//tip插件
(function ($) {
    $.fn.tip = function (options) {

        var opts = $.extend({}, $.fn.tip.defaults, options);
        var tipEl = $(".tipbox");
        if (tipEl.size() === 0) {
            var html = "<div class='tipbox' style='position: absolute;z-index:99'>";
            html += '<div class="tip-top"></div>';
            html += '<div class="tip">';
            html += '<div class="tip-text"></div>';
            html += '</div>';
            html += '<div class="tip-bottom"></div>';
            html += '</div>';
            tipEl = $(html).appendTo($("body"));
            tipEl.addClass(opts.className);
            tipEl.hide();
        }
        tipEl.find(".tip>.tip-text").html(opts.text);
        if (opts.disable) {
            $(this).unbind("mouseover").unbind("mousemove").unbind("mouseout");
        } else {
            $(this).bind("mouseover", function (e) {
                tipEl.show();
            }).bind("mousemove", function (e) {
                tipEl.css('top', e.pageY + 15).css('left', e.pageX + 15);
            }).bind("mouseout", function () {
                tipEl.hide();
            });
        }
    };

    $.fn.tip.defaults = {
        className: "tip",
        text: "",
        disable: false
    };

    var setInter = setInterval(function () {
        var parmasArray = $('.group-inner .control-group p');
        if (parmasArray.length !== 0) {
            clearInterval(setInter);
            parmasArray.each(function () {
                var value = $(this).find('span.value');
                var text = $.trim(value.text());
                if (text.length > 7) {
                    value.text(text.substring(0, 6) + '..');
                }else{
                    value.text(text);
                }
                $(this).mouseover(function () {
                    $(this).find('span.param-hover-span').css('display', 'inline-block');
                }).mouseout(function () {
                    $(this).find('span.param-hover-span').css('display', 'none');
                })
            });
        }
    }, 500)


})(jQuery);



