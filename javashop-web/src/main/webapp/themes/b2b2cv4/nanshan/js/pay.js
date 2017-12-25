$(function(){
	var payNum=$('.chooseNum').eq(0).html();
	$('.chooseNum').on('click',function(){
		$(this).siblings().removeClass('select')
		$(this).addClass('select')
		payNum=$(this).html()
	})
	$('.puyNum').focus(function(){
		$('.chooseNum').removeClass('select')
		$(this).parent().addClass('borderRed')
	})
	$('.puyNum').blur(function(){
		$(this).parent().removeClass('borderRed')
	})

	$('div.dopay').click(function(){
		var gameAccount = $("#gameAccount").val();

		if(gameAccount.length ==0){
			alert("请填写游戏帐号!");
			return false;
		}

		var payNum = $(".czRg .select").attr("data-value");
		if(!payNum && payNum.length == 0){
			payNum = $("input.puyNum").val();
		}
		if(payNum.length == 0){
			alert("请选择游戏金额!");
			return false;
		}
		if(parseFloat(payNum)%50 != 0){
			alert("游戏金额必须为50的倍数!");
			return false;
		}

		$.ajax("/recharge-operation/create.do",{
			data: {gameAccount : gameAccount, points: payNum},
			type : "POST",
			dataType : 'json',
			success : function(result) {
				if(result.result == 1){
					location.href="/nanshan/recharge_pay_desk.html?recharge_sn=" + result.data.recharge_sn;
				}
			},
			error : function(e) {
				$.Loading.show("出现错误 ，请重试");
				$.Loading.hide();
			}
		});

	});
})
