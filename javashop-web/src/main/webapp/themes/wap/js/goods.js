/**
 * Created by Andste on 2017/11/23.
 */

$(function () {
	var module = new Module();
	module.scrollToTopControl.init();
	var goodsHeader = $('#goods-header');
	goodsHeader.on('tap', '.left-back', function () {
		window.history.back();
		return false
	});
	
	var goodsId = $('#goodsId').val();
	var canLoadComment = false;
	
	/** 商品、详情、评价 切换 */
	goodsHeader.on('tap', 'li', function () {
		var $this = $(this), active = $this.data('active');
		$('.layout').hide();
		$('#' + active).show();
		$this.siblings().find('p').removeClass('active');
		$this.find('p').addClass('active');
		canLoadComment = active === 'comment-layout';
	});
	// 查看所有评论
	$(document).on('tap', '.see-all-comment-btn', function () {
		$('.layout').hide();
		$('#comment-layout').show();
		goodsHeader.find('li').find('p').removeClass('active');
		goodsHeader.find('li').eq(2).find('p').addClass('active');
		return false
	});
	
	/** 商品相册 */
	$('.gallery-goods').css({
		width: screen.width,
		height: screen.width
	});
	new Swiper('.gallery-goods', {
		pagination : '.swiper-pagination',
		paginationType : 'fraction'
	});
	
	
	/** 规格相关 */
	var specLayerIndex, specLayer;
	$('#spec-info').on('tap', function () {
		openSpecLayer();
		return false
	});
	
	function openSpecLayer() {
		specLayerIndex = layer.open({
			type: 1,
			content: $('#spec-content').html(),
			anim: 'up',
			style: 'position:fixed; bottom:0; left:0; width: 100%; height: 450px; border:none;',
			success: function (elem) {
				specLayer = elem;
				$('.quantity.number').val(num);
			},
			end: function () {
				specLayerIndex = undefined;
			}
		});
	}
	// 通过关闭按钮关闭规格
	$(document).on('tap', '.clode-header-s-c', function () {
		layer.close(specLayerIndex);
		return false
	});
	
	var selectedSpec = [], selectedProduct;
	var productList, specList, num = 1, specBody = $('.spec-val-body-s-c');
	!function () {
		$.get(ctx + '/shop/goods/' + goodsId + '/skus.do', function (response) {
			var data = composeSpec(response);
			productList = data.productList;
			specList = data.specList;
			var _specs = '';
			if(specList[0]) {
				// 如果有规格
				specList.forEach(function (spec) {
					var _spec_vals = '';
					spec.valueList[0] && spec.valueList.forEach(function (val, _index) {
						_spec_vals += '<a class="item-spec-value'+ (_index === 0 ? ' selected' : '') +'" data-spec_value_id="'+ val.spec_value_id +'">'+ val.spec_value +'</a>'
					});
					_specs += '<div class="val-spec">'
						+ '<span class="title-body-s-c">'+ spec.spec_name +'</span>'
						+ '<p>'+ _spec_vals +'</p>'
						+ '</div>'
				});
				// 渲染第一个product
				renderProduct(productList[0]);
				var _spec_ids = productList[0].spec_ids.split('-');
				_spec_ids.forEach(function (spec_id, index) {
					selectedSpec[index] = spec_id
				});
			} else {
				// 没规格
				renderProduct(productList[0], true)
			}
			specBody.html(_specs);
		});
	}();
	
	// 规格值选择
	$(document).on('tap', '.item-spec-value', function () {
		var $this = $(this), _val = $this.data('spec_value_id');
		if($this.is('.selected')) return;
		// 样式切换
		$this.addClass('selected').siblings().removeClass('selected');
		var _index = $this.closest('.val-spec').index();
		// 筛选出当前符合规格的product
		selectedSpec[_index] = _val;
		var curProduct = productList.filter(function (prod) {
			return prod.spec_ids === selectedSpec.join('-');
		});
		renderProduct(curProduct[0]);
		return false
	});
	
	// 渲染product, noSpec: 没有规格
	function renderProduct(product, noSpec) {
		if(!product) return;
		selectedProduct = product;
		num = 1;
		// 已选规格值
		$('.spec-values').html(noSpec ? '默认' : product.spec_vals);
		// 已选规格数量
		$('.amount').html(product.enable_quantity > 0 ? num + '件' : '缺货');
		$('.quantity.number').val(num);
		// 已选规格sn
		$('.sn-spec-header').html(product.sn);
		// 已选规格图片
		$('.pic-spec-header').attr('src', product.thumbnail);
		// 已选规格价格
		if(groupbuyEndTime === undefined){
			// 如果是团购商品，不改变价格
			$('.price-spec').html('￥' + String(Number(product.price).toFixed(2)).replace(/\B(?=(\d{3})+(?!\d))/g, ','));
		}
		cloneToBack()
	}
	
	$(document).on('tap', '.minus,.plus', function () {
		var $this = $(this);
		if($this.parent().is('.limited')) return;
		changeNum($this);
		return false
	});
	$(document).on('blur', '.quantity.number', function () {
		changeNum($(this));
		return false
	});
	
	function changeNum($this) {
		var enable_quantity = selectedProduct.enable_quantity;
		var _this = $this;
		$this = $this.is('.number') ? $this : $this.parent().siblings('.number');
		var _val = parseInt($this.val());
		_val = isNaN(_val) ? 1 : _val;
		if(_val > enable_quantity){
			_val = num;
		}
		var is_minus = _this.is('.minus'), is_plus = _this.is('.plus');
		
		if(is_minus && _val > 1) {
			_val--;
		}
		if(is_plus && _val < enable_quantity) {
			_val++;
		}
		
		var minus = $('.minus').parent(), plus = $('.plus').parent();
		
		plus.removeClass('limited');
		minus.removeClass('limited');
		if(_val === 1) {
			minus.addClass('limited');
		} else if (_val >= enable_quantity) {
			plus.addClass('limited');
		}
		
		num = _val;
		$('.amount').html(enable_quantity > 0 ? num + '件' : '缺货');
		$('.quantity.number').val(num);
	}
	
	// 把弹出框的内容还原回
	function cloneToBack() {
		$('#spec-content').html($(specLayer).html());
		$('.quantity.number').val(num);
	}
	
	// 重新组合规格，并返回规格和producs
	function composeSpec(spec) {
		const specList = [], productList = [];
		spec.forEach(function (item) {
			var _specList = item['specList'],
			_spec_ids = [],
			_spec_vals = [];
		_specList && _specList.forEach(function (spec) {
			var _spec_id = spec['spec_id'];
			var _specIndex = specList.findIndex(function(_spec){
				return _spec['spec_id'] === _spec_id
			});
			var __spec = {
				spec_id: spec['spec_id'],
				spec_name: spec['spec_name'],
				spec_type: spec['spec_type'],
				spec_image: spec['spec_image']
		};
		var __value = {
			spec_value: spec['spec_value'],
			spec_value_id: spec['spec_value_id']
		};
		_spec_ids.push(spec['spec_value_id']);
		//_spec_vals.push(spec['spec_name'] + ':' + spec['spec_value']);
		_spec_vals.push(spec['spec_value']);
		if(_specIndex === -1){
			__spec.valueList = [__value];
			specList.push(__spec)
		}else if(specList[_specIndex]['valueList'].findIndex(function (_value) {return _value['spec_value_id'] === spec['spec_value_id']}) === -1) {
			specList[_specIndex]['valueList'].push(__value)
		}
	});
		item.spec_ids = _spec_ids.join('-');
		item.spec_vals = _spec_vals.join(' ');
		productList.push(item);
	});
		
		return {specList: specList, productList: productList}
	}
	
	/** 立即购买、加入购物车 */
	$(document).on('tap', '#add-cart-btn,#direct-order-btn', function () {
		var $this = $(this);
		if(specList[0]) {
			// 如果有规格，打开规格选项
			openSpecLayer()
		}else {
			var is_direct = $this.is('#direct-order-btn');
			addToCart(is_direct);
		}
		return false
	});
	$(document).on('tap', '.f-s-c-btn.add-cart,.f-s-c-btn.direct-order', function () {
		var $this = $(this);
		addToCart($this.is('.direct-order'));
		return false
	});
	
	/** 收藏 */
	$(document).on('tap', '.concern-cart-love', function () {
		var $this = $(this), is_collect = $this.is('.active');
		addToCollect(is_collect);
		return false
	});
	
	/** 商品详情【介绍、规格参数】相关 */
	var detailFilter = $('.detail-filter'),
		detailContent = $('.detail-content');
	// 商品介绍、规格参数切换
	detailFilter.on('tap', '.item-filter', function () {
		var $this = $(this), _index = $this.index();
		$this.addClass('active').siblings().removeClass('active');
		detailContent.find('>div').eq(_index).show().siblings().hide();
		return false;
	});
	
	/** 商品评论相关 */
	var commentFilter = $('.comment-filter'),
		commentContent = $('.comment-content');
	var loadFlag = false, page = 1, noData = false, grade = 0;
	//  筛选评论
	commentFilter.on('tap', '.item-filter', function () {
		var $this = $(this);
		$this.addClass('active').siblings().removeClass('active');
		grade = $this.data('grade');
		page = 1;
		loadFlag = false;
		noData = false;
		loadComment();
		return false;
	});
	
	// 滚动加载
	$(window).scroll(function () {
		if(!canLoadComment || loadFlag || noData) return;
		var tHeight = $(window).scrollTop() + $(window).height();
		var dHeight = $(document).height();
		if(tHeight >= dHeight - 200) {
			commentContent.append('<li><p class="is-loading">加载更多中...</p></li>');
			loadFlag = true;
			loadComment()
		}
	});
	
	// 加载评论
	function loadComment() {
		$.ajax({
			data: {
				goods_id: goodsId,
				page: page,
				grade: grade
			},
			url: ctx + '/goods/goods-comment-item.html',
			success: function (html) {
				if(!/class="item-comment"/gi.test(html)){
					noData = true;
					html = page === 1
						? '<li><p class="no-comment">暂无相关评论...</p></li>'
						: '<li><p class="no-more">没有了...</p></li>';
				}
				commentContent.html(page === 1 ? html : commentContent.html() + html);
				commentContent.find('.is-loading').closest('li').remove();
				page += 1;
				loadFlag = false;
			}
		});
	}
	loadComment();
	
	
	/** 获取购物车数量 */
	var cartNum = $('#cart-num');
	function getCartNum() {
		$.get(ctx + '/api/shop/order-create/count.do?v=' + new Date().getTime(), function (response) {
			if(response < 1) {
				cartNum.text('')
			}else if(response > 99) {
				cartNum.text('99+')
			}else {
				cartNum.text(response || '')
			}
		})
	}
	getCartNum();
	
	/** 加入购物车 directOrder: 立即购买*/
	function addToCart(directOrder) {
		$.ajax({
			type: 'post',
			url: ctx + '/api/shop/order-create/cart/product.do?skuid=' + selectedProduct.sku_id + '&num=' + num,
			dataType: 'json',
			success: function (response) {
				if(response.result === 1){
					if(directOrder) {
						location.href = ctx + '/checkout/checkout.html'
					}else {
						module.message.success('加入购物车成功！', function () {
							getCartNum();
							specLayerIndex !== undefined && layer.close(specLayerIndex)
						});
					}
				}else {
					module.message.error(response.error)
				}
			}
		})
	}
	
	/** 加入收藏 favorite_id：如果已收藏，传入favorite_id */
	function addToCollect(is_collect) {
		var $this = $('.concern-cart-love');
		if(is_collect) {
			$.post(ctx + '/api/shop/collect/cancel-favorite.do?goods_id=' + goodsId, function (response) {
				if(response.result === 1) {
					module.message.success('取消收藏成功！', function () {
						$this.removeClass('active').find('.focus-info').text('关注');
					})
				}else {
					module.message.error(response.message || '出现错误，请稍后再试！')
				}
			})
		}else {
			$.post(ctx + '/api/shop/collect/add-collect.do?goods_id=' + goodsId, function (response) {
				if(response.result === 1) {
					module.message.success('收藏成功！', function () {
						$this.addClass('active').find('.focus-info').text('已关注');
					})
				}else {
					module.message.error(response.message || '出现错误，请稍后再试！')
				}
			})
		}
	}
	
	/** 团购相关 */
	var groupbuyEndTime = $('#groupbuyEndTime').val();
	if(groupbuyEndTime !== undefined){
		var tms = [], day = [], hour = [], minute = [], second = [];
		tms[tms.length] = groupbuyEndTime;
		day[day.length] = 'd1';
		hour[hour.length] = 'h1';
		minute[minute.length] = 'm1';
		second[second.length] = 's1';
		
		function takeCount() {
			setTimeout(takeCount, 1000);
			for (var i = 0, j = tms.length; i < j; i++) {
				tms[i] -= 1;
				//计算天、时、分、秒、
				var days = Math.floor(tms[i] / (60 * 60 * 24));
				var hours = Math.floor(tms[i] / (60 * 60)) % 24;
				var minutes = Math.floor(tms[i] / 60) % 60;
				var seconds = Math.floor(tms[i] / 1) % 60;
				if (days < 0) days = 0;
				if (hours < 0) hours = 0;
				if (minutes < 0) minutes = 0;
				if (seconds < 0) seconds = 0;
				//将天、时、分、秒插入到html中
				document.getElementById(day[i]).innerHTML = String(days);
				document.getElementById(hour[i]).innerHTML = String(hours);
				document.getElementById(minute[i]).innerHTML = String(minutes);
				document.getElementById(second[i]).innerHTML = String(seconds);
			}
		}
		setTimeout(takeCount, 1000);
	}
});


if (!Array.prototype.forEach) {
	Array.prototype.forEach = function(callback, thisArg) {
		var T, k;
		if (this == null) {
			throw new TypeError(' this is null or not defined');
		}
		var O = Object(this);
		var len = O.length >>> 0;
		if (typeof callback !== "function") {
			throw new TypeError(callback + ' is not a function');
		}
		if (arguments.length > 1) {
			T = thisArg;
		}
		k = 0;
		while (k < len) {
			var kValue;
			if (k in O) {
				kValue = O[k];
				callback.call(T, kValue, k, O);
			}
			k++;
		}
	};
}

if (!Array.prototype.findIndex) {
	Object.defineProperty(Array.prototype, 'findIndex', {
		value: function(predicate) {
			if (this == null) {
				throw new TypeError('"this" is null or not defined');
			}
			
			var o = Object(this);
			var len = o.length >>> 0;
			if (typeof predicate !== 'function') {
				throw new TypeError('predicate must be a function');
			}
			var thisArg = arguments[1];
			var k = 0;
			while (k < len) {
				var kValue = o[k];
				if (predicate.call(thisArg, kValue, k, o)) {
					return k;
				}
				k++;
			}
			return -1;
		}
	});
}

if (!Array.prototype.filter) {
	Array.prototype.filter = function(fun /* , thisArg*/) {
		"use strict";
		if (this === void 0 || this === null) throw new TypeError();
		var t = Object(this);
		var len = t.length >>> 0;
		if (typeof fun !== "function") throw new TypeError();
		var res = [];
		var thisArg = arguments.length >= 2 ? arguments[1] : void 0;
		for (var i = 0; i < len; i++) {
			if (i in t) {
				var val = t[i];
				if (fun.call(thisArg, val, i, t)) res.push(val);
			}
		}
		return res;
	};
}