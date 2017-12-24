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
})
