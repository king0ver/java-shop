/**
 * Created by Andste on 2017/11/21.
 */
 
$(function () {
	var module = new Module();
	module.navigator.init('订单列表');
	module.scrollToTopControl.init();
	
	/** 订单筛选相关 */
	var orderFilter = $('.order-filter');
	var status = module.getQueryString('status');
	if(!status){
		orderFilter.find('li').eq(0).addClass('active')
	} else {
		orderFilter.find('li').each(function () {
			var $this = $(this), active = $this.data('active');
			status === active && ($this.addClass('active'))
		})
	}
	orderFilter.on('tap', 'li', function () {
		location.replace(location.href.match(/(http.+\.html)/)[1] + '?status=' + $(this).data('active'));
		return false
	});
	
	/** 订单操作 */
	var orderWapper = $('#order-wapper');
	// 确认收货
	orderWapper.on('tap', '.order-btn._rog', function () {
		var $this = $(this), order_sn = $this.closest('.item-order').data('order_sn');
		layer.open({
			content: '请确认您的操作',
			btn: ['确定', '取消'],
			yes: function (index) {
				Order.rog(order_sn, function () {
					module.message.success('确认收货成功！', function () {
						location.reload()
					})
				}, function () {
					module.message.error('确认收货出错，请稍候再试！')
				});
				layer.close(index);
			}
		});
		return false
	});
	
	// 取消订单
	orderWapper.on('tap', '.order-btn._cancel', function () {
		var $this = $(this), order_sn = $this.closest('.item-order').data('order_sn');
		var reason = window.prompt('请输入取消原因');
		if(reason === null) return false;
		if(!reason) {
			module.message.error('请输入取消原因！');
			return false
		}else if(!reason || reason.length > 50){
			module.message.error('输入字数应在50字以内哦！');
			return false
		}else {
			Order.cancel({order_sn: order_sn, reason: reason}, function () {
				module.message.success('取消订单申请成功！', function () {
					location.reload()
				})
			}, function () {
				module.message.error('取消订单出现错误，请稍候再试！')
			})
		}
		return false
	});
	
	// 订单付款
	orderWapper.on('tap', '.order-btn._pay', function () {
		var $this = $(this), order_sn = $this.closest('.item-order').data('order_sn');
		location.href = ctx + '/member/pay_desk_ordersn.html?ordersn=' + order_sn;
		return false;
	});
	
	// 去评论
	orderWapper.on('tap', '.order-btn._comment', function () {
		var $this = $(this), order_sn = $this.closest('.item-order').data('order_sn');
		location.href = ctx + '/member/order-comment.html?ordersn=' + order_sn;
		return false;
	});
	
	// 申请售后
	orderWapper.on('tap', '.order-btn._service', function () {
		var $this = $(this), order_sn = $this.closest('.item-order').data('order_sn');
		location.href = ctx + '/member/after-sales.html?ordersn=' + order_sn;
		return false;
	});
	
	/** 加载更多 */
	var loadFlag = false, noData = false, page = 1;
	loadMore(page, status);
	$(window).scroll(function () {
		if(loadFlag || noData) return;
		var tHeight = $(window).scrollTop() + $(window).height();
		var dHeight = $(document).height();
		if(tHeight >= dHeight - 200) {
			orderWapper.find('.order-list').append('<li><p class="is-loading">加载更多中...</p></li>');
			loadFlag = true;
			loadMore(page, status)
		}
	});
	
	function loadMore(_page, status) {
		$.ajax({
			type: 'get',
			url: './order-list-item.html?page=' + _page + '&status=' + status,
			success: function (html) {
				if(!/class="item-order"/gi.test(html)){
					noData = true;
					html = page === 1
						? '<li><p class="no-order">暂无相关订单..</p></li>'
						: '<li><p class="no-more">没有了...</p></li>';
				}
				page += 1;
				var orderWapperList = orderWapper.find('.order-list');
				orderWapperList.find('.is-loading').closest('li').remove();
				orderWapperList.html(orderWapperList.html() + html);
				loadFlag = false;
			},
			error: function () {
				module.message.error('加载更多订单失败，请稍后再试！');
				loadFlag = false;
			}
		})
	}
});
