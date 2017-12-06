/**
 * Created by Andste on 2017/11/22.
 */
 
$(function () {
	var module = new Module();
	module.navigator.init({
		title: '商品列表',
		right: {
			element: '<i class="icon-nav icon-search"></i><i class="icon-nav icon-cart"></i>'
		}
	});
	module.scrollToTopControl.init();
	
	/** 导航栏icon操作 */
	$(document).on('tap', '.icon-search', function () {
		module.searchControl.init();
	});
	
	$(document).on('tap', '.icon-cart', function () {
		location.href = ctx + '/cart.html'
	});
	
	var goodsList = $('.goods-list'), search_str = location.search;
	/** 加载更多 */
	var loadFlag = false, noData = false, page = 1;
	loadMore(page, search_str);
	$(window).scroll(function () {
		if(loadFlag || noData) return;
		var tHeight = $(window).scrollTop() + $(window).height();
		var dHeight = $(document).height();
		if(tHeight >= dHeight - 200) {
			goodsList.append('<li><p class="is-loading">加载更多中...</p></li>');
			loadFlag = true;
			loadMore(page, search_str)
		}
	});
	
	function loadMore(_page, search_str) {
		$.ajax({
			type: 'get',
			url: './goods-list-item.html' + search_str + (search_str ? '&page=' : '?page=') + _page,
			success: function (html) {
				if(!/class="item-goods"/gi.test(html)){
					noData = true;
					html = page === 1
						? '<li><p class="no-goods">暂无相关商品..</p></li>'
						: '<li><p class="no-more">没有了...</p></li>';
				}
				page += 1;
				goodsList.find('.is-loading').closest('li').remove();
				goodsList.html(goodsList.html() + html);
				loadFlag = false;
			},
			error: function () {
				module.message.error('加载更多订单失败，请稍后再试！');
				loadFlag = false;
			}
		})
	}
	
	/** 商品筛选 */
	var goodsListSelector = $('.goods-list-selector');
	goodsListSelector.on('tap', 'li', function () {
		location.replace($(this).data('href'))
	})
});