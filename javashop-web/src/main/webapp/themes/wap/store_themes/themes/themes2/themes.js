$(function(){
	
	 bindEvents();
	 function bindEvents() {
		 //返回顶部显示、隐藏
		 isShowTop();
		 //领取优惠券
		 receive();
		 //菜单操作
		 navTools();
		 //最小高度
		 listHeight();
		 //搜索
		 storeGoodsSrarch();
	 }

	 function isShowTop(){
		 var prevTop = 0;
	   	 var currTop = 0;
	   	$(window).scroll( function() {
	   		currTop = $(window).scrollTop();  //滚动值
	   		//判断是否是向下滚动
			if(currTop > prevTop){
				$(".go-top").fadeIn(500);
			}else{
				$(".go-top").fadeOut(500);
			}
	   	});
	 }
	 
	 function receive(){
		 $(".store-bonus").click(function(){
				var bonusId = $(this).attr("rel");
				var storeId = $(this).attr("store_id");
				$.ajax({
					url : ctx + "/api/b2b2c/bonus/receive-bonus.do?store_id="+storeId+"&type_id="+bonusId,
					cache : false,
					dataType: "json",
					success : function(data) {
						if (data.result == 1) {
							alert("领取成功");
							window.location.href;
						}
						if (data.result == 0) {
							alert(data.message);
						}
						
					},
					error : function() {
						alert("出现错误,请重试！");
					}
				});
	 		})
	 }
	 
	 function navTools(){
		 //初始化菜单高度，使菜单高度和满屏幕一样高
		 var navHeight =  $(window).height();
		 $(".nav-box").css("min-height",navHeight+"px");
		 
		 //点击按钮滑动显示、隐藏导航
		 $("#nav-right").click(function(event){
			 $(".nav-box").show();
			 $(".nav-box").animate({"right": "0px"}, 300, "swing"); 
			 $(document.body).css({
				   "overflow-x":"hidden",
				   "overflow-y":"hidden"
				 });
			 event.stopPropagation();
			 return false;
		 })
		 
		$(document).click(function(){
			 $(".nav-box").animate({"right": "-70%"}, 300, "swing");
			 $(".nav-box").hide();
			 $(document.body).css({"overflow-x":"auto","overflow-y":"auto"});
//			 return false;
         }); 
		 
		 $(".nav-close").click(function(){
			 $(".nav-box").animate({"right": "-70%"}, 300, "swing"); 
			 $(".nav-box").hide();
			 $(document.body).css({"overflow-x":"auto", "overflow-y":"auto"});
			 return false;
		 })
	 }
	 
	 function listHeight(){
		 var navHeight =  $(window).height();
		 $(".themes-list-box").css("min-height",navHeight+"px");
	 }
	 
	 function storeGoodsSrarch(){
		 $('input[nctype="search_in_store"]').click(function() {
				$('#search_act').val('show_store');
				$('#formSearch').submit();
			});
			$('input[nctype="search_in_shop"]').click(function() {
				location.href=ctx+"/goods_list.html?keyword="+$("#keyword").val()+"&search=goods";
			});
	 }
	 
	 
})