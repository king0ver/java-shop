/**
 * Created by Andste on 2017/11/23.
 */
 
$(function () {
	var module = new Module();
	module.navigator.init('积分商城');
	module.scrollToTopControl.init();
	
	/** exchangeSwiper */
	new Swiper('#exchange-container', {
		autoplay: 3000,
		loop : true,
		pagination : '.swiper-pagination'
	});
	
	/** 商品列表 */
	var goodsWrapper = $('#goods-wrapper');
	var loadFlag = false, noData = false, page = 1, cat_id;
	
	loadGoods();
	function loadGoods() {
		$.ajax({
			type: 'get',
			url: ctx + '/shop/mine/exchange-goods.do',
			data: {page_no: page, cat_id: cat_id},
			success: function (response) {
				var data = response.data, html = '';
				if(data && data[0]) {
					data.forEach(function (item) {
						html += '<li class="item-goods">'
							+ '<dt><a href="'+ ctx + '/goods-' +item.goods_id +'.html">'
							+ '<img src="'+ item.thumbnail +'" alt="'+ item.goods_name +'">'
							+ '</a></dt>'
							+ '<dd>'
							+ '<div class="name-goods">'+ item.goods_name +'</div>'
							+ '<div class="descrip-goods"><p></p>'
							+ '<p>'+ (item.exchange_money > 0 ? '￥' + String(Number(item.exchange_money).toFixed(2)).replace(/\B(?=(\d{3})+(?!\d))/g, ',') + ' + ' : '') + item.exchange_point +'分</p>'
							+ '</div>'
							+ '<div class="price-goods"><div>原价：￥'+ String(Number(item.price).toFixed(2)).replace(/\B(?=(\d{3})+(?!\d))/g, ',') +'</div><div>已兑换：<span>'+ item.buy_count +'</span>人</div></div>'
							+ '<a href="'+ ctx + '/goods-' +item.goods_id +'.html" class="grab-btn">兑换</a>'
							+ '</dd>'
							+ '</li>'
					})
				}else {
					noData = true;
					html = page === 1
						? '<li><p class="no-goods">暂无相关商品..</p></li>'
						: '<li><p class="no-more">没有了...</p></li>';
				}
				goodsWrapper.find('.is-loading').closest('li').remove();
				goodsWrapper.html(page === 1 ? html : goodsWrapper.html() + html);
				page += 1;
				loadFlag = false;
			},
			error: function () {
				module.message.error('加载更多订单失败，请稍后再试！');
				loadFlag = false;
			}
		})
	}
	
	/** 分类 */
	$(document).on('tap', '.item-category', function () {
		var $this = $(this);
		var _cat_id = $this.data('cat_id');
		page = 1;
		cat_id = _cat_id;
		noData = false;
		loadGoods();
		showSlider(false);
		$('#exchange').find('.item-category').removeClass('active');
		$this.addClass('active');
		return false
	});
	
	/** 固定头部分类筛选 */
	var categoryWrapper = $('#category-wrapper');
	$(window).scroll(function () {
		var scrollTop = $(window).scrollTop();
		if(scrollTop >= 174) {
			categoryWrapper.addClass('fixed-top');
			goodsWrapper.addClass('margin-top');
		} else {
			categoryWrapper.removeClass('fixed-top');
			goodsWrapper.removeClass('margin-top');
		}
		
		if(loadFlag || noData) return;
		var tHeight = scrollTop + $(window).height();
		var dHeight = $(document).height();
		if(tHeight >= dHeight - 200) {
			goodsWrapper.append('<li><p class="is-loading">加载更多中...</p></li>');
			loadFlag = true;
			loadGoods()
		}
	});
	
	/** 打开侧边栏 */
	var openSlider = $('#open-slider'),
		sliderWrapper = $('.slider-wrapper'),
	    sliderMask = $('.slider-mask');
	
	openSlider.on('tap', function () {
		showSlider(true);
		return false
	});
	sliderWrapper.on('tap', '.close-slider', function () {
		showSlider(false);
		return false
	});
	sliderMask.on('tap', function () {
		var $this = $(this), is_open = $this.is('.open');
		showSlider(!is_open);
	});
	
	function showSlider(open) {
		if(open) {
			sliderMask.addClass('open');
			setTimeout(function () {
				sliderMask.addClass('show')
			});
			sliderWrapper.addClass('open')
		}else {
			sliderMask.removeClass('show');
			setTimeout(function () {
				sliderMask.removeClass('open')
			}, 300);
			sliderWrapper.removeClass('open')
		}
	}
	
	/** 侧边栏IScroll */
	new IScroll('#slider-content');
	document.getElementById('slider-mask').addEventListener('touchmove', function (e) { e.preventDefault() }, false);
	document.getElementById('slider-wrapper').addEventListener('touchmove', function (e) { e.preventDefault() }, false);
});