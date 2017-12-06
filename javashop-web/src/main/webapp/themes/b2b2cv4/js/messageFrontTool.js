/**
 * 站内消息Tool
 * @author fk
 * 提供站内消息未读数量加载
 */
var MessageFrontTool={
		/**
		 * 初始化方法 
		 * 1.加载数量
		 * 2.绑定hover事件
		 */
		init:function(){
			 this.toolWrapper = $("#message_tool_wrapper"); //站内消息tool主元素
			 this.numBox = this.toolWrapper.find(".message-num"); //数量元素
//			 var listBox = this.toolWrapper.find(".messageFront_content02"); 
			 this.loadNum();
		},
		/**
		 * 加载站内消息中的未读数量
		 * 
		 */
		loadNum:function(){
			 $.ajax({
				 url:ctx+"/api/shop/member-message/get-message-data.do",
				 dataType:'json',
				 cache:false,
				 success:function(result){
					 if(result.result === 1){
					 	var count = result.data.count;
						 $(".index-go-message .message-num").text(count > 99 ? '99+' : count);
						 if(result.data.count<=3){
							 $(".jscroll-e").hide();
							 $(".my_MessageFrontlist_all").css("height","auto");
						 }
					 }
				 }
			 });
		}
};

