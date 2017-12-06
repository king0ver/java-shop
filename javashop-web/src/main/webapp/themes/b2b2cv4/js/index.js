$(function() {
    setLiftLeft();

    // 调整楼层面板排序
    // 确保第一个面板是主面板
    // 品牌面板是最后一个面板
    var adjustFloorIndex = function() {
        // 遍历楼层
        $('.index_content .floor-line-con .floor-body').each(function() {
            // 遍历面板
            $(this)
                .find('.panel')
                .each(function() {
                    if ($(this).find('.bd-left').length === 1) {
                        // 说明是主模板
                        // 移到第一面板位置
                        $(this)
                            .closest('.floor-body')
                            .prepend($(this).closest('.panel'));
                    }

                    // 如果没有这样的Class, 说明是品牌面板
                    if (!$(this).hasClass('floor-bd')) {
                        // 移到最后一个面板位置
                        $(this)
                            .closest('.floor-body')
                            .append($(this).closest('.panel'));
                    }
                });
        });
    };

    adjustFloorIndex();

    //  监听滚动，显示或隐藏搜索栏、楼层焦点
    var searchBar = $('.search-bar');
    var liftList = $('#lift-list');
    var indexLift = $('.index-lift');
    var lifts = $('[data-lift]'),
        liftsLen = lifts.length - 1,
        lastTop = lifts.eq(liftsLen).offset().top;
    $(window).scroll(function() {
        var scrollTop = $(document).scrollTop();
        //  显示或隐藏搜索栏
        scrollTop > 619
            ? indexLift.removeClass('index-lift-hide') &&
              searchBar.addClass('show')
            : indexLift.addClass('index-lift-hide') &&
              searchBar.removeClass('show');

        //  电梯锚点监听
        for (var i = 0; i < liftsLen; i++) {
            var _st = scrollTop + 60,
                curTop = lifts.eq(i).offset().top,
                nextTop = lifts.eq(i + 1).offset().top;
            _st >= curTop &&
                _st < nextTop &&
                setLiftCur(lifts.eq(i).attr('data-lift'));
            _st >= lastTop && setLiftCur(lifts.eq(liftsLen).attr('data-lift'));
        }
    });

    //  设置电梯锚点
    function setLiftCur(attr) {
        liftList
            .find('[data-lift-cur=' + attr + ']')
            .addClass('current')
            .siblings()
            .removeClass('current');
    }

    //  点击电梯锚点滚动到指定位置点击电梯锚点滚动到指定位置
    liftList.on('click', '.index-lift-item', function() {
        var $this = $(this);
        if ($this.is('.index-lift-item-top')) return;
        var dataLift = $this.attr('data-lift-cur');
        $('html, body').animate(
            {
                scrollTop: $('[data-lift=' + dataLift + ']').offset().top - 60
            },
            300
        );
    });

    //  设置电梯left
    function setLiftLeft() {
        var _set = function() {
            var _liftDom = $('.index-lift');
            _liftDom.css(
                'left',
                ($('body').width() - 1200) / 2 - _liftDom.width() - 20
            );
        };
        _set();
        window.onresize = function() {
            _set();
        };
    }

    //  返回顶部
    $('.index-lift-item-top').on('click', function() {
        $('html, body').animate(
            {
                scrollTop: 0
            },
            300
        );
    });

    //  设置轮播banner
    var bannerSwiper = new Swiper('.swiper-container-index-banner', {
        autoplay: 2000,
        speed: 800,
        paginationType: 'custom',
        pagination: '.swiper-pagination-index-banner',
        paginationCustomRender: function(swiper, current, total) {
            var _btns = '';
            for (var i = 0; i < total; i++) {
                i === total - 1
                    ? (_btns +=
                          '<i class="custom-pagination-btn __last__' +
                          (current === i + 1 ? ' __active__' : '') +
                          '"></i>')
                    : (_btns +=
                          '<i class="custom-pagination-btn' +
                          (current === i + 1 ? ' __active__' : '') +
                          '"></i>');
            }
            //  修正pagination偏移量
            return (
                '<div class="custom-pagination-inner" style="margin-left: -' +
                (total * 22 + 6) / 2 +
                'px">' +
                _btns +
                '</div>'
            );
        },
        autoplayDisableOnInteraction: false,
        effect: 'fade'
    });

    /* 
     * --轮播背景色控制中心--
     * 
     * 现阶段,轮播背景色主要有人工+代码控制,
     * 需要你再下方的themeColor数组中,依次输入背景色.
     * 剩下就交给代码了.
     */
    var themeColor = ['#f1f1f1', '#f2f4f6'];

    $('.index_top').css('background', themeColor[0]);
    bannerSwiper.on('slideChangeStart', function() {
        var activeSpan = $(
            '.swiper-pagination-index-banner .custom-pagination-btn.__active__'
        );
        var index = activeSpan.index();
        $('.index_top').css('background', themeColor[index]);
    });

    $('.swiper-pagination-index-banner').on('mouseenter', 'i', function() {
        var $this = $(this),
            index = $this.index();
        bannerSwiper.slideTo(index, 500, false);
        $('.index_top').css('background', themeColor[index]);
    });

    // ---------------------------------------------------------------------------
    // 2017-8-17-陈小博: 重写首页楼层的一些方法

    // 如果存在品牌面板的话, 删除掉楼层Tab的最后一个
    $('.index_content .floor-line-con').each(function(index) {
        // 最后一个面板没有floor-bd 则确定存在
        var panels = $(this).find('.floor-body .panel');
        if (!$(panels[panels.length - 1]).hasClass('floor-bd')) {
            var tabs = $(this).find('.floor-hd .hd-tags li');
            tabs[tabs.length - 1].remove();
        }
    });

    // 动态给楼层主模板,普通模板,品牌面板设置position
    $('.floor-body').each(function() {
        $(this)
            .find('.panel')
            .each(function(index, ele) {
                // 主面板
                if (index === 0) {
                    $(this).css({
                        // 'width': '45%'
                    });
                    // 品牌面板
                } else if (!$(this).hasClass('floor-bd')) {
                    $(this).css({
                        height: '100px'
                    });
                } else {
                    // 其他普通面板
                    $(this).css({
                        right: '0',
                        width: '60%'
                    });
                }
            });
    });

    var floors = $('.index_content');
    //  设置鼠标悬浮切换tabs
    floors.find('.hd-tags').on('mouseenter', 'li', function() {
        var $this = $(this),
            _index = $this.index();
        $this
            .addClass('current')
            .siblings()
            .removeClass('current');
        // 2017-8-2-陈小博: 为了实现: panel的左边不动,只切换右半部分的效果.把display..换成z-index
        $this
            .closest('.floor-line-con')
            .find('.floor-bd')
            .eq(_index)
            .css('z-index', 100)
            .siblings()
            .css('z-index', 1);
    });

    $(document).ready(function() {
        $(
            'body > div.index_content > div.floor-line-con > div.floor-body > div > div > ul > li > div > a.g-name'
        ).each(function() {
            if ($(this).text().length > 26) {
                $(this).text(
                    $(this)
                        .text()
                        .substring(0, 26) + '...'
                );
            }
        });
    });

    //  设置楼层轮播
    var indexSwiper = new Swiper('.floor-swiper-container', {
        autoplay: 2000,
        speed: 800,
        loop: true,
        paginationType: 'progress',
        pagination: '.test-swiper-pagination',
        autoplayDisableOnInteraction: false
    });

    var setIn = setInterval(function() {
        var hide = $('.index_content li.brand-hidden');
        if (hide.length !== 0) {
            clearInterval(setIn);
            hide.remove();
        }
    }, 100);
    
    $('.user-info-above-banner').load(ctx + "/index/index_card.html?_="+new Date().getTime(),function(){
		var avatar = $('.user-info-above-banner .avatar img');
		var reg = new RegExp(ctxPath);
		if(reg.test(avatar.attr('src'))){
			return;
		}
		if(/http/.test(avatar.attr('src'))){
			return;
		}
    	avatar.attr('src',ctxPath + avatar.attr('src'));    			
})

});
