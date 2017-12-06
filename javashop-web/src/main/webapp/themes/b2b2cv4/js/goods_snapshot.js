/**
 * Created by Andste on 2017/6/7.
 */
$(function () {
    //  商品详情导航滚动悬浮
    var goodsId     = $('#goodsId').val(),
        detailNav   = $('#details-nav'),
        plDetailNav = $('#pl-details-nav'),
        snapshot_id = $('#snapshotId').val(),
        product_id = $('#productId').val(),
        navTop      = detailNav.offset().top;
    $(window).scroll(function () {
        var scrollTop = $('html body').scrollTop();
        scrollTop >= navTop
            ? detailNav.css('position', 'fixed') && plDetailNav.css('display', 'block')
            : detailNav.css('position', 'relative') && plDetailNav.css('display', 'none');
    });
    
    Spec.init(goodsId,snapshot_id,product_id);

    //  商品详情导航切换
    detailNav.on('click', '.link-suit', function () {
        var $this = $(this), _index = $this.index();
        $('html body').animate({'scrollTop': navTop}, 200, function () {
            $('.s-content').eq(_index).css('display', 'block')
                .siblings().css('display', 'none');
        });
        $this.addClass('selected').siblings().removeClass('selected');
    });

    //  评论图片展示配置
    lightbox.option({
        positionFromTop: 100,
        maxWidth: 500,
        maxHeight: 500,
        fadeDuration: 300,
        resizeDuration: 200,
        albumLabel: '第%1张 / 共%2张'
    });

    //  图片展示切换
    var infoLeft = $('.info_left');
    infoLeft.on('mouseenter', '.img-select', function () {
        var $this = $(this);
        $this.addClass('on').siblings().removeClass('on');
    });

    //  数量加减
    var countNum = $('.count-num'), numEl = $('#buyNumVal');
    var oldVal = 1, stock = 1;
    countNum.on('click', '.oper-num', function () {
        var $this = $(this), isAdd = $this.is('.up');
        var curNum = parseInt(numEl.val());
        stock = parseInt($('#stock').val());
        isAdd ? (function () {
            curNum < stock ? numEl.val(curNum += 1) : $.message.error('超出库存数量！');
            curNum >= stock && $this.addClass('not-oper');
            $this.siblings('.oper-num').removeClass('not-oper');
        })() : (function () {
            curNum >= 2 && numEl.val(curNum -= 1);
            curNum < 2 && $this.addClass('not-oper');
            $this.siblings('.oper-num').removeClass('not-oper');
        })();
    });

    numEl.on('focus', function () {
        oldVal = parseInt($(this).val());
    });

    numEl.on('blur', function () {
        var _val = $(this).val();
        stock = parseInt($('#stock').val());
        if (!Base.regExp.integer.test(_val)) {
            numEl.val(oldVal);
            return;
        }
        if (_val > stock) {
            numEl.val(oldVal);
            $.message.error('超出库存数量！');
        }
    });

    $('#iWantAsk').on('click', function () {
        $.get(ctx + '/detail/detail_content_ask_form.html', function (html) {
            $.dialogModal({
                title: '商品咨询',
                html: html,
                width: 300,
                callBack: function () {
                    var content = $('#dialogModal').find('.form-control').val();
                    if (!content) return false;
                    $.ajax({
                        url: ctx + '/api/b2b2c/store-comment-api/add.do',
                        data: {
                            goods_id: goodsId,
                            commenttype: 2,
                            content: content
                        },
                        success: function (res) {
                            res.result === 1 ? $.message.success(res.message + ',请等待审核') : $.message.error(res.message);
                        }
                    });
                }
            });
        });
    });

    var zoomSwiper = new Swiper('.swiper-container-zoom', {
        direction: 'vertical',
        slidesPerView: 5,
        spaceBetween: 0,
        prevButton:'.swiper-btn-prev',
        nextButton:'.swiper-btn-next'
    });

    $('.swiper-container-zoom').on('mouseenter mouseleave', function (event) {
        var $this = $(this),  btns = $this.find('.swiper-btn'), mouseenter = event.type === 'mouseenter';
        mouseenter ? btns.addClass('show') : btns.removeClass('show');
    });

    $('#motorbike').on('mouseenter', function () {
        var $this = $(this);
        var __ = setInterval(function () {
            var crMz = $this.find("[href]");
            crMz.length > 0 && crMz.parent().remove() && clearInterval(__)
        }, 10)
    });

    $('.pro-activity').on('mouseenter mouseleave', '.parent_tips_prom', function (event) {
        var $this = $(this), mouseenter = event.type === 'mouseenter';
        mouseenter ? $this.addClass('menu-hover') : $this.removeClass('menu-hover');
    })

});