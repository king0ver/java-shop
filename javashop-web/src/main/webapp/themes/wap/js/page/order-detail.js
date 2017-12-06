/**
 * Created by Andste on 2017/11/27.
 */

$(function () {
	var moduel = new Module();
	moduel.navigator.init('订单详情');
	moduel.scrollToTopControl.init();
	
	var orderSn = $('#order-sn').val();
	
	/** 取消订单 */
	$(document).on('tap', '.cancel-order', function () {
		var reason = window.prompt('请输入取消原因');
		if(reason === null) return false;
		if(!reason) {
			module.message.error('请输入取消原因！');
			return false
		}else if(!reason || reason.length > 50){
			module.message.error('输入字数应在50字以内哦！');
			return false
		}else {
			Order.cancel({order_sn: orderSn, reason: reason}, function () {
				module.message.success('取消订单申请成功！', function () {
					location.reload()
				})
			}, function () {
				module.message.error('取消订单出现错误，请稍候再试！')
			})
		}
	});
	
	/** 确认收货 */
	$(document).on('tap', '.rog-order', function () {
		layer.open({
			content: '请确认您的操作',
			btn: ['确定', '取消'],
			yes: function (index) {
				Order.rog(orderSn, function () {
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
	})
	
});