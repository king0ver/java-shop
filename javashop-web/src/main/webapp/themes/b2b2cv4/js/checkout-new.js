/**
 * Created by Andste on 2016/8/10.
 */

var Ckt = {};
$(function(){
	/* 初始化方法
	 ============================================================================ */
	(function(){
		//  收货人信息事件绑定
		infoEvent();

		//  支付方式事件绑定
		payEvent();

		//  配送清单事件绑定
		inventoryEvent();

		//  发票信息事件绑定
		invoiceEvent();

		//  送货时间事件绑定
		timeEvent();
	})();

	/* 收货人信息事件绑定
	 ============================================================================ */
	function infoEvent(){
		var info = $('.center-ckt-info');

		//  新增收货地址
		$('.c-new-address').on('click', function(){
			Ckt.info.a_InfoAdd();
		});

		//  收货人信息设置默认
		info.on('click', '.set', function(){
			var _this = $(this);
			Ckt.info.s_InfoSetDefAdd(_this);
		});

		//  收货人信息编辑
		info.on('click', '.edit', function(){
			var _this = $(this);
			Ckt.info.e_InfoEdit(_this);
		});

		//  收货人信息删除
		info.on('click', '.delete', function(){
			var _this = $(this);
			Ckt.info.d_InfoItem(_this);
		});

		//  收货人信息选中
		info.on('click', '.ckt-checkbox.info', function(){
			var _this = $(this);
			var _state = _this.is('.selected');
			!_state && Ckt.info.s_InfoItem(_this)
		});

		//  地区列表项鼠标移入移出
		info.on('mouseover mouseout', '.li-ckt-info', function(event){
			var _this = $(this);
			Ckt.info.c_InfoItem(_this, event);
		});

		//  更多地址、收起地址
		$('.ckt-item.info').on('click', '.collapse-ckt-info', function(){
			var _this = $(this);
			Ckt.info.c_InfoCollapse(_this);
		});
	}

	/* 支付方式事件绑定
	 ============================================================================ */
	function payEvent(){
		var pay = $('.content-ckt-pay');

		//  支付方式选中
		pay.on('click', '.ckt-checkbox.pay', function(){
			var _this = $(this);
			Ckt.payMent.s_PayItem(_this);
		});

		//  更多方式、收起
		$('.collapse-ckt-pay').on('click', function(){
			var _this = $(this);
			Ckt.payMent.c_PayCollapse(_this);
		});

		//  如果支付方式大于6种 显示更多选项
		if($('.ul-ckt-pay').find('li').length > 5){
			$('.collapse-ckt-pay').css({display : 'block'});
		}
	}

	/* 配送清单事件绑定
	 ============================================================================ */
	function inventoryEvent(){
		var inventory = $('#inventory');
		//  快递方式选中
		inventory.on('click', '.ckt-checkbox.express', function(){
			var _this = $(this);
			Ckt.inventory.s_express(_this);
		});

		//  优惠劵鼠标移入移出效果
		inventory.on('mouseover mouseout', '.item-discount-inventory', function(event){
			var _this = $(this);
			Ckt.inventory.c_discount(_this, event);
		});

		//  优惠劵鼠选择
		inventory.on('click', '.item-discount-inventory', function(){
			var _this = $(this);
			Ckt.inventory.s_discount(_this);
		});

		//  动态设置配送清单高度
		var items = $('.item-ckt-inventory');
		for(var i = 0, len = items.length; i < len; i++) {
			var _thisItem   = items.eq(i),
				leftHeight  = _thisItem.find('.left-item-inventory').height(),
				rightHright = _thisItem.find('.right-item-inventory').height();
			if(!leftHeight || !rightHright){
				return
			}
			leftHeight > rightHright ? (function(){
				_thisItem.find('.right-item-inventory').css('height', leftHeight)
			}()) : (function(){
				_thisItem.find('.left-item-inventory').css('height', rightHright)
			}());
		}
	}

	/* 发票信息事件绑定
	 ============================================================================ */
	function invoiceEvent(){
		var invoice = $('.content-ckt.invoice'), dialogModal = $('#dialogModal');
		//  发票鼠标悬浮显示按钮
        dialogModal.on('mouseenter mouseleave', '.head-invoice', function (event) {
            Ckt.invoice.h_invoiceTitle(event, $(this));
        });

        //  发票抬头编辑
        dialogModal.on('click', '.__edit__', function () {
            Ckt.invoice.e_invoiceTitle($(this));
        });

        //  增加发票
        dialogModal.on('click', '.add-invoice', function () {
            Ckt.invoice.a_invoice($(this));
        });

        //  保存发票抬头
        dialogModal.on('click', '.__save__', function (event) {
            Ckt.invoice.s_invoiceTitle($(this));
            event.stopPropagation();
        });

        //  编辑发票
        dialogModal.on('click', '.__update__', function (event) {
            Ckt.invoice.u_inboiceTitle($(this));
            event.stopPropagation();
        });

        //  删除发票
        dialogModal.on('click', '.__delete__', function (event) {
            Ckt.invoice.d_invoiceTitle($(this));
            event.stopPropagation();
        });

		//  发票信息编辑
		$('.edit-invoice').on('click', function(){
			Ckt.invoice.e_invoice();
		});

		//  发票抬头选择
        dialogModal.on('click', '.ckt-checkbox.head-invoice', function(){
			Ckt.invoice.s_invoiceHead($(this));
		});

		//  发票内容选择
        dialogModal.on('click', '.ckt-checkbox.content-invoice', function(){
			var _this = $(this);
			Ckt.invoice.s_invoiceContent(_this);
		});

        //  取消发票
        invoice.on('click', '.cancel-invoice', function () {
            var $this = $(this);
            $.loading();
            $.ajax({
                url: ctx + '/api/shop/order-create/checkout-param/receipt.do',
                data:{
                    need_receipt: 'no',
                    title : '',
                    duty_invoice: '',
                    content: ''
                },
                type:'POST',
                success: function (res) {
                    $.closeLoading();
                    if(res.result === 1){
                        $this.siblings('.receipt-title').html('');
                        $this.siblings('.receipt-content').html('无需发票');
                        $this.remove();
    
                        invoice.find("input[name='receiptContent']").val('');
                        invoice.find("input[name='receiptTitle']").val('');
                        invoice.find("input[name='receiptDuty']").val('');
                    }else {
                        $.message.error(res.message)
                    }
                },
                error: function () {
                    $.closeLoading();
                    $.message.error('出现错误，请稍候重试！')
                }
            });
        })
	}

	/* 送货时间事件绑定
	 ============================================================================ */
	function timeEvent(){
		var time = $('.content-ckt.time');
		time.on('click', '.ckt-checkbox', function(){
			Ckt.time.e_time($(this));
		});
	}

	/* 收货人信息逻辑处理
	 ============================================================================ */
	Ckt.info = (function(){
		var info = $('.center-ckt-info');

		//  收货人信息删除
		function deleteInfo(_this){
			var address_id = _this.attr('address_id');
			if(typeof $.confirm === 'function'){
				$.confirm('确认要删除吗？', function(){
					// ...
					delAddress();
				});
			}else {
				if(confirm('确认要删除吗？')){
					// ...
					delAddress();
				}
			}

			function delAddress(){
				$.loading();
				$.ajax({
					url     : ctx + '/api/shop/member-address/delete.do?addr_id=' + address_id,
					type    : 'POST',
					success : function(result){

						if(result && typeof result === 'string'){
							result = JSON.parse(result);
						}
						if(result.result === 1){
							$.closeLoading(function(){
								initAddressList();
								$.message.success('删除成功！');
							});
						}else {
							$.message.error(result.message);
						}
						$.closeLoading();
					},
					error   : function(){
						$.message.error('出现错误，请重试！');
					}
				});
			}
		}

		//  收货人信息选择
		function selectInfo(_this, _global){
			var address_id = _this.closest('.li-ckt-info').data('address_id');
			$.loading();
			$.ajax({
				url     : ctx + '/api/shop/order-create/checkout-param/addressid/'+ address_id +'.do',
				type    : 'post',
				global  : _global,
				success : function(res){
                    $.closeLoading();
                    res.result === 1
                        ? (function () {
                            $('#inventory').load( ctx + '/checkout/checkout-inventory.html');
                            Ckt.public.refreshTotal();
                            Ckt.info.i_InfoInitAddress()
                        })()
                        : $.message.error(res.message);
				},
				error   : function(){
                    $.closeLoading();
					$.message.error('出现错误，请重试！');
				}
			});
		}

		//  地区鼠标移入
		function curInfo(_this, _event){
			var _type = _event.type;
			var InfoInfo = _this.find('.address-li-ckt-info'),
				title    = InfoInfo.attr('data-title'),
				html     = InfoInfo.html(),
				oper     = _this.find('.operate-li-ckt-info');
			if(_type === 'mouseover'){
				//  InfoInfo.addClass('min');
				oper.addClass('show');
				if(html && html.length > 29){
					InfoInfo.html(html.substring(0, 29) + '...');
				}
			}else if(_type === 'mouseout'){
				//  InfoInfo.removeClass('min');
				oper.removeClass('show');
				InfoInfo.html(title);
			}
		}

		//  更多地址、收起地址
		function collInfo(_this){
			var state = _this.is('.more'),
				liLen = info.find('.li-ckt-info').length;
			if(state){
				//  收起
				_this.removeClass('more')
					 .find('span').html('更多地址');
				_this.find('i').removeClass('more');
				info.css({height : 42});
				Ckt.public.insertEletTo($('.li-ckt-info.selected'))
			}else {
				//  更多
				_this.addClass('more')
					 .find('span').html('收起地址');
				_this.find('i').addClass('more');
                liLen > 3 ? info.css({height : 42 * 4}) : info.css({height : 42 * liLen});
			}
		}

		//  新增收货人信息【没有地址时】
		function newInfo(){
			$.ajax({
				url     : ctx + '/checkout/address-add.html',
				type    : 'GET',
				success : function(html){
					var _api = '/api/shop/member-address/add-new-address.do';
					$.dialogModal({
						title    : '新增收货人信息',
						html     : html,
						top      : 100,
						btn      : false,
						backdrop : false,
						showCall : function(){
							$('#dialogModal').find('.close').css('display', 'none');
							$('#dialogModal').find('.modal-body').css({overflow : 'scroll'});
							return showDialog(_api, true);
						}
					})
				}
			})
		}

		//  新增收货人信息【有地址时】
		function addInfo(){
			$.ajax({
				url     : ctx + '/checkout/address-add.html',
				type    : 'GET',
				success : function(html){
					var _api = '/api/shop/member-address/add-new-address.do';
					$.dialogModal({
						title    : '新增收货人信息',
						html     : html,
						top      : 100,
						btn      : false,
						showCall : function(){
							$('#dialogModal').find('.modal-body').css({overflow : 'scroll'});
							return showDialog(_api);
						}
					})
				}
			})
		}

		//  编辑收货人信息
		function editInfo(_this){
			var address_id = _this.attr('address_id');
			$.ajax({
				url     : ctx + '/checkout/address-edit.html?addressid=' + address_id,
				type    : 'GET',
				success : function(html){
					var _api = '/api/shop/member-address/edit.do?addr_id=' + address_id;
					$.dialogModal({
						title    : '修改收货人信息',
						html     : html,
						top      : 100,
						btn      : false,
						showCall : function(){
							$('#dialogModal').find('.modal-body').css({overflow : 'scroll'});
							return showDialog(_api);
						}
					})
				}

			})
		}

		//  新增、修改地址dialog逻辑
		function showDialog(_api, bool){
			var app  = $('#dialogModal').find('.address_div');
			var shipAddressName = app.find("input[name='shipAddressName']");

			var inputs = app.find('.form-control');

			//  别名
			app.find('.example-aliases').on('click', function(){
				var _val = $(this).html();
				shipAddressName.val(_val);
			});

			inputs.on('input propertychange blur', function(){
				var _this = $(this),
					_name = _this.attr('name'),
					_val  = _this.val();
				if(_name === 'mobile' || _name === 'shipMobile'){
					testMobile(_val) ? _this.parent().removeClass('has-error error') : _this.parent().addClass('has-error error');
				}else if(_name !== 'shipAddressName'){
					_val ? _this.parent().removeClass('has-error error') : _this.parent().addClass('has-error error');
				}
			});

			//  保存地址
			$('.add-btn').on('click', function(){
				sendAjax($(this));
			});

			function sendAjax(_this){
				for(var i = 0, len = inputs.length; i < len; i++) {
					var __this = inputs.eq(i),
						_val   = __this.val();
					if(!_val && __this.attr('name') !== 'shipAddressName'){
						__this.parent().addClass('has-error error');
						if(i === len) return
					}
				}

				if(app.find('.error').length > 0){
					$.message.error('请核对表单填写是否有误！');
					return false;
				}

				/* 对地址信息进行校验
				 ============================================================================ */
				var aLen     = $('.app-address-tab').find('a').length,
					_inputLen = 0,
					_input   = $('.app-address').find("input[type='hidden']");
				for(var j = 0, jlen = _input.length; j < jlen; j ++){
					if(_input.eq(j).val()) _inputLen += 1
				}
				if(aLen !== _inputLen / 2){
					$.message.error('请核对收货地址是否完整！');
					return false;
				}
				/* 对地址信息进行校验
				 ============================================================================ */
				_this.off('click');

				var options = {
					url     : ctx + _api,
					type    : 'POST',
					success : function(result){
						if(result && typeof result === 'string'){
							result = JSON.parse(result);
						}
						if(result.result === 1){
							$.closeLoading(function(){
								$.message.success('保存成功！');
								console.log('result:', result);
								bool ? $.ajax({
									url: ctx + '/api/shop/order-create/checkout-param/addressid/'+ result.data.addr_id +'.do',
									type: 'POST',
									success: reloadData
								}) : reloadData();
								
								function reloadData() {
                                    initAddressList();
                                    Ckt.public.addressTotal();
                                    Ckt.public.refreshTotal();
                                    $('#inventory').load( ctx + '/checkout/checkout-inventory.html');
                                    $('#dialogModal').modal('hide').find('.close').css({display : 'block'});
                                }
							});
						}else {
							shipAddressName.attr('name', 'shipAddressName');
							$.message.error(result.message);
							_this.on('click', function(){
								sendAjax(_this);
							})
						}
					},
					error   : function(){
						shipAddressName.attr('name', 'shipAddressName');
						$.message.error('出现错误，请重试！');
						_this.on('click', function(){
							sendAjax(_this);
						})
					}
				};

				$.loading();
				app.find('#address_form').ajaxSubmit(options);
				$.closeLoading();
			}

			function testMobile(mobile){
				return /^0?(13[0-9]|15[0-9]|18[0-9]|14[0-9]|17[0-9])[0-9]{8}$/.test(mobile);
			}
		}

		//  显示、隐藏【更多地址】
		function initAddressList(str){
		    var addressList = $('#address_list');
			$.ajaxSetup ({cache: false});
			if(str && str === 'setDef'){
                addressList.empty().load(ctx + '/checkout/address-list.html', function(){
					Ckt.public.addressTotal();
				});
				return
			}
            addressList.empty().load(ctx + '/checkout/address-list.html', function(){
				var collapse = $('.collapse-ckt-info'),
					liCkt    = $('.li-ckt-info'),
					liCktLen = liCkt.length;
				if(liCktLen > 1){
					collapse.addClass('show more');
					collapse.find('.more-collapse-ckt').html('收起地址');
					collapse.find('.icon-collapse-ckt-info').addClass('more');
					var liLen = $('.li-ckt-info').length;
					liLen > 3 ? info.css({height : 42 * 4}) : info.css({height : 42 * liLen});
					Ckt.public.addressTotal();
				}else if(liCktLen === 0){
					collapse.removeClass('show');
					newInfo();
				}else {
					collapse.removeClass('show');
					Ckt.public.addressTotal();
				}
			});
		}

		//  设置默认收货地址
		function setDefaultAddress(_this){
			var add_id = _this.attr('address_id');
			$.loading();
			$.ajax({
				url     : ctx + '/api/shop/member-address/set-def-address.do?addr_id=' + add_id,
				type    : 'POST',
				success : function(result){
					if(result && typeof result === 'string'){
						result = JSON.parse(result);
					}
					if(result.result === 1){
						$.closeLoading(function(){
							initAddressList('setDef');
							$.message.success('设置成功！');
						});
					}else {
						$.message.error(result.message);
					}
				},
				error   : function(){
					$.message.error('出现错误，穷重试！');
				}
			});
			$.closeLoading();
		}

		return {
			i_InfoInitAddress : function(){
				return initAddressList();
			},
			d_InfoItem        : function(_this){
				return deleteInfo(_this);
			},
			s_InfoItem        : function(_this, g){
				return selectInfo(_this, g);
			},
			c_InfoItem        : function(_this, _event){
				return curInfo(_this, _event);
			},
			c_InfoCollapse    : function(_this){
				return collInfo(_this);
			},
			a_InfoAdd         : function(){
				return addInfo();
			},
			e_InfoEdit        : function(_this){
				return editInfo(_this);
			},
			s_InfoSetDefAdd   : function(_this){
				return setDefaultAddress(_this);
			}
		}
	})();

	/* 支付方式逻辑处理
	 ============================================================================ */
	Ckt.payMent = (function(){
		var pay = $('.content-ckt-pay');

		function selectPay(_this){
			var index     = _this.index(),
				ulPlay    = _this.parent();

			_coll($('.collapse-ckt-pay'));
			if(index > 4){
				ulPlay.css({width : 110 * 6});
				Ckt.public.insertEletTo(_this, $('.ckt-checkbox.pay'), 5);
			}else {
				ulPlay.css({width : ''});
			}
			$.loading();
			$.ajax({
				url  : ctx + '/api/shop/order-create/checkout-param/payment-type.do',
				data : { payment_type: _this.data('payment_type') },
				type : 'POST',
				success : function(res){
                    $.closeLoading();
                    res.result === 1
                        ? _this.addClass('selected').siblings().removeClass('selected')
                        : $.message.error(result.message);
				},
				error   : function(){
                    $.closeLoading();
					$.message.error('出现错误，穷重试！');
				}
			})
			
		}

		//  点击展开更多方式、收起
		function C_collPay(_this){
			var _state  = _this.is('.more'),
				_payUl  = pay.find('.ul-ckt-pay'),
				_payLen = pay.find('.ckt-checkbox.pay').length;
			if(!_state){
				//  展开
				_this.addClass('more').find('span').html('收起');
				_this.find('i').addClass('more');
				if(_payLen < 5){
					//
				}else if(_payLen < 9){
					//  展开就行
					_payUl.css({width : _payLen * 110});
				}else {
					//  去掉宽度限制
					_payUl.removeClass('min');
					_this.find('i').removeClass('more');
					_this.find('i').addClass('coll');
				}
			}else {
				//  收起
				_coll(_this);
				if(_payLen < 5){
					//
				}else if(_payLen < 9){
					//  收起就行
					_payUl.css({width : ''});
				}else {
					//  加上宽度限制
					_payUl.addClass('min');
					_this.find('i').removeClass('coll');
				}
			}
		}

		//  收起体现
		function _coll(_this){
			_this.find('span').html('更多方式');
			_this.find('i').removeClass('more');
			_this.removeClass('more');
		}

		return {
			s_PayItem     : function(_this){
				return selectPay(_this);
			},
			c_PayCollapse : function(_this){
				return C_collPay(_this);
			}
		}

	})();

	/* 配送清单逻辑处理
	 ============================================================================ */
	Ckt.inventory = (function(){
		//  快递方式选择
		function S_express(_this){
			discountExpress(_this, 'express');
		}

		//  优惠劵鼠标移入移出效果
		function C_discount(_this, event){
			var _type = event.type;
			var detailItemDiscount = _this.find('.detail-item-discount');
			if(_type === 'mouseover'){
				//  鼠标移入
				detailItemDiscount.addClass('show');

			}else if(_type === 'mouseout'){
				//  鼠标移出
				detailItemDiscount.removeClass('show');
			}
		}

		//  优惠劵选择
		function S_discount(_this){
			discountExpress(_this, 'discount');
		}

		//  优惠劵、快递方式选择
		function discountExpress(_this, _str){
			var _state   = _this.is('.selected'),
				_closest = _this.closest('.item-ckt-inventory');

			//  初始化数据
			var regionid = $('#regionid').val(),
				storeid  = '',
				typeid   = '',
				bonusid  = '';

			var url;
			//  组织数据
			if(_str === 'express'){
				url = ctx + '/api/store/store-order/change-args-type.do';
				
				if(_state) return;
				var _item = _closest.find("div[class='item-discount-inventory selected']");
				typeid = _this.attr('type_id');
				storeid = _this.closest('.item-ckt-inventory').attr('storeid');
				bonusid = _item.attr('bonusid') || 0;
			}else {
				storeid = _this.closest('.item-ckt-inventory').attr('storeid');
				typeid = _this.closest('.item-ckt-inventory').find("div[class='ckt-checkbox express selected']").attr('type_id');
				bonusid = _this.attr('bonusid');
				if(_state) bonusid = 0;
				url = ctx + '/api/shop/order-create/cart/'+storeid+'/coupon/'+bonusid+'.do';
			}

			var options = {
				url     : url,
				data    : {
					shop_id : storeid,
					regionid : regionid,
					type_id  : typeid,
					coupon_id : bonusid
				},
				type    : 'POST',
				dataType: 'json',
				success : function(result){
					if(result.result === 1){
						$.closeLoading(function(){
							$('#inventory').load( ctx + '/checkout/checkout-inventory.html');
							Ckt.public.refreshTotal();
						});
					}else {
						$.message.error(result.message);
					}
				},
				error   : function(){
					$.message.error('出现错误，请重试！');
				}
			};

			$.loading();
			$.ajax(options);
			$.closeLoading();
		}

		return {
			s_express  : function(_this){
				return S_express(_this);
			},
			c_discount : function(_this, event){
				return C_discount(_this, event);
			},
			s_discount : function(_this){
				return S_discount(_this);
			}
		}
	})();

	/* 发票信息逻辑处理
	 ============================================================================ */
	Ckt.invoice = (function(){

		//  修改发票信息
		function E_invoice(){
			var invoiceDialog = $('#invoice_dialog');
			invoiceDialog.dialogModal({
				title    : '发票信息',
				top      : 100,
                alwaysBackfill: true,
				showCall : function(){
                    invoiceDialog.on('click', '.company-invoice-input', function(e){
                        e.stopPropagation();
                    });
				},
				callBack : function(){
					var dialogModal = $('#dialogModal');
                    if($('.company-invoice-input:visible').length > 0){
                        $.message.error('请先保存发票抬头信息！');
                        return false;
                    }else {
                        if($('.head-invoice.selected').length === 0) {
                            $.message.error('请选择一个发票抬头！');
                            return false
                        }
                        setTnvoice();
                        return true;
                    }

					function setTnvoice(){
						//发票抬头
						var receipt_title = dialogModal.find('.head-invoice.selected').find(".company-invoice-input").val();
						//发票内容
						var receipt_content = dialogModal.find('.content-invoice.selected').find('span').html();
						//税号
						var duty_invoice = dialogModal.find('.duty-invoice-input').val();
						
						$.loading();
                        $.ajax({
                            url: ctx + '/api/shop/order-create/checkout-param/receipt.do',
                            data:{
                                need_receipt: 'yes',
                                title : receipt_title,
                                duty_invoice: receipt_title === '个人' ? '' : duty_invoice,
                                content: receipt_content
                            },
                            type:'POST',
							success: function (res) {
                            	$.closeLoading();
								if(res.result === 1){
								
								}else {
									$.message.error(res.message)
								}
                            },
							error: function () {
								$.closeLoading();
								$.message.error('出现错误，请稍候重试！')
                            }
                        });
						
						$('.receipt-title').html(dialogModal.find('.head-invoice.selected').find(".company-invoice-input").val());
						$('.receipt-content').html(dialogModal.find('.content-invoice.selected').find('span').html());
						$('.cancel-invoice').length < 1 && $('.edit-invoice').after('<a href="javascript: void(0);" class="cancel-invoice" style="margin-left: 10px; color: #ff3500;">取消发票</a>')
					}
				}
			});
		}

		//  显示编辑删除按钮
        function H_invoiceTitle(event, $this) {
		    if($this.is('.__add_ele__')) return;
            var mouseenter = event.type === 'mouseenter', btns = $this.find('.__btns__');
            mouseenter ? btns.addClass('show') : btns.removeClass('show');
        }

        //  编辑发票抬头
        function E_invoiceTitle($this) {
            var _item = $this.closest('.head-invoice');
            _item.addClass('__editing__');
            _item.find('.__title__').hide();
            _item.find('.__edit__').hide();
            _item.find('.__update__').show();
            _item.find('.company-invoice-input').show().focus();
            $('#dialogModal').find('.duty-invoice-input').prop('readonly', false);
        }

        //  添加发票
        function A_invoiceTitle($this) {
		    var addEle = $this.siblings('.__add_ele__');
		    $this.css('display', 'none');
            addEle.css('display', 'block').addClass('selected').find('.company-invoice-input').css('display', 'block').focus();
            addEle.siblings().removeClass('selected');
            $('#dialogModal').find('.invoice-head.duty').show().find('.duty-invoice-input').val('').prop('readonly', false);
            cancelUpdate(addEle);
        }

        //  保存添加
        function S_invoicetTitle($this) {
		    var dialogModal = $('#dialogModal'),
                _input = $this.closest('.head-invoice').find('input'),
                _val = _input.val(),
                _duty = dialogModal.find('.duty-invoice-input').val();
		    if(!_val){ $.message.error('发票抬头不能为空！'); _input.focus(); return }
            if(!_duty){ $.message.error('纳税人识别号不能为空！'); dialogModal.find('.duty-invoice-input').focus(); return }
            if(!/^[0-9a-zA-Z]+$/.test(_duty)){ $.message.error('纳税人识别号不能包含特殊字符！'); return }
            $.ajax({
                url: ctx + '/api/shop/member-receipt/add.do',
                data: {
                    title: _val,
                    content: $('.content-invoice.selected').find('span').html(),
                    duty: _duty,
                    type: 2
                },
                success: function (res) {
                    res.result === 1 ? addSuccess(res.data): $.message.error(res.message)
                },
                error: function () {
                    $.message.error('出现错误，请稍后重试！');
                }
            })
        }

        function addSuccess(data) {
		    var dialogModal = $('#dialogModal'), add_ele = dialogModal.find('.__add_ele__');
            var __ele__ = '\
		        <div class="ckt-checkbox head-invoice company selected">\
                    <div class="__title__">'+ data['title'] +'</div>\
                        <input type="text" class="company-invoice-input" maxlength="30" value="'+ data['title'] +'" data-old_value="'+ data['title'] +'" style="border: none; outline: none;">\
                    <div class="__btns__">\
                        <a href="javascript:;" class="__edit__">编辑</a>\
                        <a href="javascript:;" class="__update__">保存</a>\
                        <a href="javascript:;" class="__delete__">删除</a>\
                    </div>\
                    <input type="hidden" name="receipt_id" value="'+ data['id'] +'">\
                    <input type="hidden" name="receipt_content" value="'+ data['content'] +'">\
                    <input type="hidden" name="receipt_duty" value="'+ data['duty'] +'">\
                </div>\
		    ';
            add_ele.siblings().removeClass('selected');
            add_ele.before(__ele__);
            dialogModal.find('.head-invoice.company').length < 10 && dialogModal.find('.add-invoice').show();
            dialogModal.find('.duty-invoice-input').prop('readonly', true);
		    add_ele.hide().find('input').val('');
            S_invoiceHead($(__ele__));
        }

        //  保存编辑
        function U_updateEdit($this) {
            var dialogModal = $('#dialogModal'),
                _item = $this.closest('.head-invoice'),
                _id = _item.find("input[name='receipt_id']").val(),
                _input = _item.find('.company-invoice-input'),
                _title = _input.val(),
                _duty = dialogModal.find('.duty-invoice-input').val(),
                _content = dialogModal.find('.content-invoice.selected').find('span').html();
            if(!_title){ $.message.error('发票抬头不能为空！'); _item.find("input[name='receipt_id']").focus(); return }
            if(!_duty){ $.message.error('纳税人识别号不能为空！'); dialogModal.find('.duty-invoice-input').focus(); return }
            if(!/^[0-9a-zA-Z]+$/.test(_duty)){ $.message.error('纳税人识别号不能包含特殊字符！'); return }
            $.ajax({
                url: ctx + '/api/shop/member-receipt/edit.do',
                data: {
                    id: _id,
                    title: _title,
                    content: _content,
                    duty: _duty,
                    type: 2
                },
                success: function (res) {
                    res.result === 1 ? (function () {
                        $.message.success('修改成功！');
                        updateSuccess();
                    })() : $.message.error(res.message)
                },
                error: function () {
                    $.message.error('出现错误，请稍候重试！');
                }
            });

            function updateSuccess() {
                _item.find('.__title__').html(_title).show();
                _input.data('old_value', _title);
                _item.find('.company-invoice-input').hide();
                _item.find('.__edit__').show();
                _item.find('.__update__').hide();
                _item.removeClass('__editing__');
                dialogModal.find('.invoice-head.duty').show();
                dialogModal.find('.duty-invoice-input').prop('readonly', true);
                _item.find("input[name='receipt_content']").val(dialogModal.find('.content-invoice.selected').find('span').html());
                _item.find("input[name='receipt_duty']").val(dialogModal.find('.duty-invoice-input').val())
            }
        }

        //  删除发票
        function D_invoiceTitle($this) {
            //  虽然是删除抬头，但是是删除整个发票
            var _invoice = $this.closest('.head-invoice'), _id = _invoice.find("input[name='receipt_id']").val(), dialogModal = $('#dialogModal');
            $.confirm('确认删除这条数据吗？', function () {
                $.ajax({
                    url: ctx + '/api/shop/member-receipt/delete.do',
                    data: { id: _id },
                    success: function (res) {
                        res.result === 1 ? (function () {
                            $.message.success('删除成功！');
                            _invoice.siblings('.__add_ele__').removeClass('selected').hide();
                            _invoice.siblings('.add-invoice').show();
                            _invoice.remove();
                            dialogModal.find('.company.selected').length === 0 && dialogModal.find('.personal').addClass('selected');
                            dialogModal.find('.invoice-head.duty').val('').hide()
                        })() : $.message.error(res.message);
                    },
                    error: function () {
                        $.message.error('出现错误，请稍后重试！');
                    }
                })
            })
        }

		//  发票抬头选择
		function S_invoiceHead(_this){
			var is_add  = _this.is('.__add_ele__'),
                is_personal = _this.is('.personal'),
                dialogModal = $('#dialogModal'), _content = _this.find("input[name='receipt_content']").val();
            cancelUpdate(_this);
			_this.addClass('selected').siblings().removeClass('selected');
			!is_add && ($('.head-invoice.__add_ele__').hide()) && (dialogModal.find('.head-invoice.company').length < 10 && $('.add-invoice').show());

            dialogModal.find('.content-invoice').each(function () {
                var $this = $(this), __content = $this.find('span').html();
                _content === __content && ($this.addClass('selected').siblings().removeClass('selected'));
            });
            is_personal && dialogModal.find('.content-invoice.personal').addClass('selected');
            !is_personal && dialogModal.find('.duty-invoice-input').val(_this.find("input[name='receipt_duty']").val());
            !is_personal ? dialogModal.find('.invoice-head.duty').show() : dialogModal.find('.invoice-head.duty').hide();

			dialogModal.find("input[name='receiptContent']").val(dialogModal.find('.content-invoice.selected').find('span').html());
			dialogModal.find("input[name='receiptTitle']").val(_this.find('.company-invoice-input').val());
			dialogModal.find("input[name='receiptDuty']").val(is_personal ? '' : _this.find("input[name='receipt_duty']").val());
		}

		//  取消编辑
		function cancelUpdate($this) {
            var __item = $this.siblings('.__editing__'), __input = __item.find('.company-invoice-input');
            __item.find('.__title__').show();
            __item.find('.__edit__').show();
            __item.find('.__update__').hide();
            __input.val(__input.data('old_value')).hide();
        }

		//  发票内容选择
		function S_invoiceContent(_this){
			var receiptContent = $('#dialogModal').find('.invoice-inputs').find("input[name='receiptContent']");
			_this.addClass('selected')
				 .siblings().removeClass('selected');
			receiptContent.val(_this.find('span').html());
		}

		return {
			e_invoice        : function(){
				return E_invoice();
			},
            e_invoiceTitle   : function ($this) {
                return E_invoiceTitle($this);
            },
            a_invoice        : function ($this) {
                return A_invoiceTitle($this);
            },
            s_invoiceTitle   : function ($this) {
                return S_invoicetTitle($this)
            },
            u_inboiceTitle   : function ($this) {
                return U_updateEdit($this);
            },
            d_invoiceTitle   : function ($this) {
			    return D_invoiceTitle($this);
            },
            h_invoiceTitle   : function (event, $this) {
                return H_invoiceTitle(event, $this)
            },
			s_invoiceHead    : function(_this){
				return S_invoiceHead(_this);
			},
			s_invoiceContent : function(_this){
				return S_invoiceContent(_this);
			}
		}
	})();

	/* 送货时间逻辑处理
	 ============================================================================ */
	Ckt.time = (function(){
		//  修改送货时间
		function E_time(_this){
		    $.loading();
			$.ajax({
				url  : ctx + '/api/shop/order-create/checkout-param/receivetime.do',
				data : { receivetime:  _this.find('span').html() },
				type : 'POST',
				success : function(result){
				    $.closeLoading();
					if(result.result === 1){
                        _this.siblings('input').val(_this.find('span').html());
                        _this.addClass('selected')
                             .siblings('.ckt-checkbox').removeClass('selected');
					}else {
						$.message.error(result);
					}
				},
				error   : function(){
                    $.closeLoading();
					$.message.error('出现错误，穷重试！');
				}
			})
		}

		return {
			e_time : function(_this){
				return E_time(_this);
			}
		}
	})();

	/* 公共方法
	 ============================================================================ */
	Ckt.public = (function(){
		//  将此元素放到第一位
		function insertEletTo(element, toEle, eq){
			var _this = element,
				toEle = toEle ? toEle : _this.siblings(),
				eq    = eq ? eq : 0;
			//  克隆
			var _clone = _this.clone(true);
			$(_clone).insertBefore(toEle.eq(eq));
			_this.remove();
            $('.center-ckt-info')[0].scrollTop = 0;
		}

		//  刷新金额统计
		function refreshTotal(){
			var total = $('.total-ckt-total');
			var credit = $("#isUseCredit");
			var  checked=false;
			if(typeof(credit) !== "undefined"){
				checked= credit.prop("checked");
			}
			if(checked){
				total.load(ctx + '/checkout/checkout-total.html?isUseCredit=1');
			}else{
				total.load(ctx + '/checkout/checkout-total.html');
			}
		}

		//  设置底部地址信息
		function addressTotal(){
			var _selected = $('.li-ckt-info.selected');
			var address_info   = _selected.find('.address-li-ckt-info').attr('title'),
				address_name   = _selected.find('.name-li-ckt-info').attr('title'),
				address_mobile = _selected.find('.mobile-li-ckt-info').attr('title');
			$('#address-info').html(address_info);
			$('#address-name').html(address_name);
			$('#address-mobile').html(address_mobile);
			//  设置地址id
		}

		return {
			insertEletTo : function(ele, toEle, eq){
				return insertEletTo(ele, toEle, eq);
			},
			refreshTotal : function(){
				return refreshTotal();
			},
			addressTotal : function(){
				return addressTotal();
			}
		}
	})();

	/* 创建订单逻辑
	 ============================================================================ */
	//  初始化数据
	initData();
	function initData(){

		//  支付方式
		$("input[name='paymentId']").val($('.ckt-checkbox.pay.selected').attr('pay_ment_id'));

		//  配送方式、优惠劵
		(function(){
			var items = $('.item-ckt-inventory');
			for(var i = 0, len = items.length; i < len; i++) {
				var _item          = items.eq(i),
					_shippingId    = _item.find("input[name='shippingId']"),
					_bonusid       = _item.find("input[name='bonusid']"),
					_shippingIdVal = _item.find('.ckt-checkbox.express.selected').attr('type_id'),
					_bonusidVal    = '';
				var _bon = _item.find('.item-discount-inventory.selected');
				_bon.length > 0 ? (_bonusidVal = _bon.attr('bonusid') + '-' + _bon.attr('price')) : (_bonusidVal = '0-0');
				_shippingId.val(_shippingIdVal);
				_bonusid.val(_bonusidVal);
			}
		})();
	}

	//  创建订单事件
	$('#bill_btn').on('click', function(){
		createOrder();
	});

	function createOrder(){
		var options = {
			url     : ctx + '/api/shop/order-create/trade.do',
			type    : 'POST',
			dataType: 'JSON',
			success : function(result){
				if(result.result === 1){
					$.closeLoading(function(){
						location.href = ctx + '/trade_pay_desk.html?tradesn=' + result.data['trade_sn'];
					});
				}else {
					var demoData = [{
						name: '(模拟数据，等许哥写好API，自然不会出现1)',
						reason: '发生变动',
						solution: '移除后重新加入购物车'
					},{
						name: '(模拟数据，等许哥写好API，自然不会出现2)',
						reason: '发生变动',
						solution: '移除后重新加入购物车'
					},{
						name: '戴尔DELL INS15-7567湛黑版15.6英寸笔记本电脑i7-7700 8G 1T+128G固态GTX1050t3',
						reason: '发生变动',
						solution: '移除后重新加入购物车'
					}];

					if(result.data && result.data.length > 0){
						// 如果API返回数据，就不使用模拟数据
						demoData = result.data;
					}

					var modal = $('.trade-exception-modal');
					var tbody = modal.find('table tbody');
					tbody.empty();

                    demoData.forEach(function(item,index) {
						if(!item){
							return;
						}
						var tr = $('<tr><td></td><td></td><td></td></tr>');
						tbody.append(tr);

						$(tr.find('td')[0]).text(item.name);
						$(tr.find('td')[1]).text(item.reason);
						$(tr.find('td')[2]).text(item.solution);
					});
					modal.modal('show');


					$('#bill_btn').on('click', function(){
						createOrder();
					});
				}
			},
			error   : function(){
				$.message.error('出现错误，请重试！');
				$('#bill_btn').on('click', function(){
					createOrder();
				});
			}
		};

		$('#bill_btn').off('click');

		$.loading();
		$('#checkoutForm').ajaxSubmit(options);
		$.closeLoading();
	}
	
	$('#remark-confirm').on('click', function () {
		var _remark = $('#remark').val();
		$.loading();
		$.ajax({
			url: ctx + '/api/shop/order-create/checkout-param/remark.do',
			data: { remark: _remark },
            type: "POST",
            success: function (res) {
				$.closeLoading(function () {
                    res.result === 1
                        ? $(this).blur() && $.message.success('保存成功！')
                        : $.message.error(res.message);
                });
            },
            error  : function () {
                $.closeLoading();
                $.message.error('出现错误，请稍候重试！')
            }
		})
    })
});