/**
 * Created by Andste on 2017/6/9.
 */
$(function () {
    var goodsList = $('.goods-list'),
        brandList = $('.brand-list'),
        otherFilter = $('.other-filter'),
        priceInputs = $('.custom-pro'),
        curInputVal = '';

    //  导航鼠标悬浮
    goodsList.on('mouseenter mouseleave', '.gl-bar-trigger', function (event) {
        var $this = $(this), mouseenter = event.type === 'mouseenter';
        mouseenter ? (function () {
            $this.addClass('trig-act').next('.gl-bar-cont').addClass('show');
        })() : (function () {
            $this.removeClass('trig-act').next('.gl-bar-cont').removeClass('show');
        })();
    });
    goodsList.on('mouseenter mouseleave', '.gl-bar-cont', function (event) {
        var $this = $(this), mouseenter = event.type === 'mouseenter';
        mouseenter ? (function () {
            $this.addClass('show').prev('.gl-bar-trigger').addClass('trig-act');
        })() : (function () {
            $this.removeClass('show').prev('.gl-bar-trigger').removeClass('trig-act');
        })();
    });

    //  点击已选择跳转url
    goodsList.on('click', '.item-selected', function () {
//        var $this = $(this), _href = $this.attr('data-href'), _url = ctx + '/goods_list.html';
//        $this.parent().find('.item-selected').length > 1 && (_url = _href);
//        location.href = _url;
        var $this = $(this), _href = $this.attr('data-href'), _url = 'goods_list.html';
        $this.parent().find('.item-selected').length > 0 && (_url = _href);
        location.href = ctx + "/"+ _url;
    });

    //  更多【展开】
    goodsList.on('click', '.choice', function () {
        var $this = $(this), isMore = $this.is('.choice-on'), isBrand = $this.is('.brand-choice');
        isMore ? ($this.removeClass('choice-on') && $this.html('更多')) : ($this.addClass('choice-on') && $this.html('收起'));
        isBrand && (isMore ? brandList.removeClass('show-more') : brandList.addClass('show-more'));
    });

    //  筛选结果展开
    goodsList.on('click', '.control a', function () {
        var $this = $(this), isOpen = $this.is('.open');
        $this.css('display', 'none').siblings().css('display', 'inline-block');
        isOpen ? otherFilter.addClass('show') : otherFilter.removeClass('show')
    });

    //  筛选价格鼠标悬浮
    goodsList.on('mouseenter mouseleave', '.product-control-price', function (event) {
        var $this = $(this), mouseenter = event.type === 'mouseenter';
        mouseenter
            ? ($this.css('overflow', 'inherit') && $this.find('.p-c-b').addClass('on'))
            : ($this.css('overflow', 'hidden') && $this.find('.p-c-b').removeClass('on'));
    });

    //  筛选价格输入框获得焦点
    goodsList.on('focusin focusout', '.custom-pro', function (event) {
        var $this = $(this), focus = event.type === 'focusin';
        focus ? (function () {
            curInputVal = $this.val() || '';
            priceInputs.addClass('custom-pro_in')
        })() : (function () {
            !/^\d+$/g.test($this.val()) && $this.val(curInputVal);
            priceInputs.removeClass('custom-pro_in');
        })()
    });

    //  筛选价格清空
    goodsList.on('click', '.empty-pro', function () {
        priceInputs.val('');
        priceInputs.eq(0).focus()
    });

    //  筛选价格确认
    goodsList.on('click', '.enter-pro', function () {
        enterFiler();
    });

    //  筛选价格取人【回车】
    goodsList.on('keyup', '.custom-pro', function (event) {
        event.which === 13 && enterFiler();
    });

    //  筛选价格跳转
    function enterFiler() {
        var _href = location.href,
            hasPrice = _href.indexOf("price=") > -1,
            hasArrow = _href.indexOf("?") > -1,
            l = priceInputs.eq(0).val(),
            h = priceInputs.eq(1).val();
        if(!h || parseInt(l) > parseInt(h)) return;
        var _str = "price="+ l +"_" + h;
        location.href = hasPrice ? _href.replace(/price=.*_[^&]*/g, _str) : (_href += hasArrow ? "&" + _str : "?" + _str)
    }

    //  商品列表鼠标悬浮
    goodsList.on('mouseenter mouseleave', '.item-product', function (event) {
        var $this = $(this), mouseenter = event.type === 'mouseenter';

        mouseenter ? (function () {
            $this.addClass('hover');
        })() : (function () {
            $this.removeClass('hover');
        })()
    });

    //  页面滚动监听;
    var pC = $('.product_control'), pCTop = pC.offset().top;
    $(window).scroll(function () {
        var scrollTop = $('html body').scrollTop();
        scrollTop >= pCTop ? pC.addClass('filter-fix') : pC.removeClass('filter-fix')
    });
});