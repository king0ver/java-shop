/**
 * Created by Andste on 2016/12/5.
 */
$(function () {
    var module = new Module();
    var indexScroll;
    var searchNav = $('#index-search-nav');

    init();

    function init() {
        module.scrollToTopControl.init({
            rollListen: false,
            clickToTop: function () {
                indexScroll.isAnimating = false;
                setTimeout(function () {
                    indexScroll.scrollTo(0, 0, 300, IScroll.utils.ease.quadratic)
                }, 20)
            }
        });

        bindEvents();
    }

    function bindEvents() {

        //  初始化iscroll
        initIScroll();
        indexScroll.on('scroll', updateHeader);
        indexScroll.on('scrollEnd', updateHeader);

        //  初始化【广告横幅】
        initAdvSlide();

        //  商城公告事件绑定
        storeEvents();

        //  新品上市事件绑定
        newListingEvents();

        //  底部内容
        navContentEvents();

        //  搜索栏事件绑定
        searchEvents();

    }

    function initIScroll() {
        indexScroll = new IScroll('#index', {
            probeType: 3,
            mouseWheel: false,
            disableTouch: false,
            tap: true
        });
        window.indexScroll = indexScroll;
        var supportsPassive = false;
        try {
            var opts = Object.defineProperty({}, 'passive', {
                get: function() { supportsPassive = true }
            });
            window.addEventListener("test", null, opts);
          } catch (e) {}
			  document.addEventListener('touchmove', function (e) { e.preventDefault() }, supportsPassive ? { passive: false } : false);
		}

    //  更新header透明度
    function updateHeader() {
        var _top = -(this.y >> 0);
        _top > 150 ? module.scrollToTopControl.show() : module.scrollToTopControl.hide();
        _top < 0 ? searchNav.addClass('hide') : searchNav.removeClass('hide');
        if (_top < 0) { _top = 0 }
        if (_top > 80) { _top = 80 }
        $('#index-search-nav').css({ backgroundColor: 'rgba(201, 21, 35, ' + _top / 100 + ')' })
    }

    //  初始化广告横幅
    function initAdvSlide() {
        TouchSlide({
            slideCell: '#slide',
            titCell: '.hd ul',
            mainCell: '.bd ul',
            effect: 'leftLoop',
            autoPlay: true,
            autoPage: true,
            switchLoad: "_src"
        });
    }

    //  商城公告事件绑定
    function storeEvents() {
        var bulletinBoard = $('.index-bulletin-board');
        var items = bulletinBoard.find('.item'),
            itemsLen = items.length;
        $('.more-bulletin-board').on('tap', function () {
            module.message.error('抱歉，没有更多了。。。');
            return false
        });
        if (itemsLen < 1) {
            return
        }
        var bulletinSwiper = new Swiper('.index-bulletin-board', {
            loop: true,
            autoplay: 3000,
            autoplayDisableOnInteraction: false,
            direction: 'vertical',
            preventLinksPropagation: false
        })
    }

    //  新品上市事件绑定
    function newListingEvents() {
        var newListing = $('.list-new-listing'),
            items = newListing.find('.item'),
            itemsLen = items.length;
        if (itemsLen < 4) { return }
        newListing.css({ width: itemsLen * 0.3 * 100 + '%' });
        //  初始化iscroll
        var newListingScroll = new IScroll("#content-new-listing", {
            scrollX: true,
            scrollY: false,
            probeType: 1,
            mouseWheel: false,
            disableTouch: false,
            tap: true
        });
    }

    //  底部内容事件绑定
    function navContentEvents() {
        $(".nav-back-to-top").on('tap', function () {
            $('body').animate({
                scrollTop: 0
            }, 1000)
            return false;
        })
    }

    //  搜索栏事件绑定
    function searchEvents() {
        $('#search-nav').on('tap', function () {
            module.searchControl.init();
            return false
        })
    }


    //  整个文档加载完成后再次刷新iscroll
    window.onload = function () {
        indexScroll.refresh()
    };

    var setSwiper = function () {
        if (document.getElementsByClassName('test-swiper-pagination').length != 0) {
            //clearInterval(timeMachine);
            var swiperInFloor = new Swiper('.floor-swiper-container', {
                pagination: '.test-swiper-pagination',
                // paginationType: 'custom',
                paginationClickable: true,
                spaceBetween: 30,
                autoplay: 2500,
            });
        }
        $(".floor-swiper-container").addClass("floor-swiper-replace");
        $(".floor-swiper-container").removeClass("floor-swiper-container")
    }
    setSwiper();

    // 解决Chrome模拟移动端时,点击不了a标签的问题
    $('body').on('tap', 'a', function () {
        var _href = $(this).attr('data-href') ? $(this).attr('data-href') : $(this).attr('href');
        location.href = _href ? _href : location.href;
        return false
    });

});