/**
 * Created by Andste on 2016/9/2.
 */
$(function(){
//  添加商品【调用搜索页面】
	$('#goods_name_ipt').on('click', function(){
		$.GoodsSellerSelector({
			maxLength: 1, //最大可选商品数量,0代表无数量限制.
			confirm: function(data){
				console.log('-->',data)
				$("#goods_store_price").html(data.price);
				$("#goods_storage").html(data.quantity);
				$("#goods_name_ipt").val(data.goods_name);
				$("input[name='goods_name']").val(data.goods_name);
				$("input[name='product_id']").val(data.sku_id);
				$("input[name='goods_id']").val(data.goods_id);
				$("#goods_original_price_ipt").val(data.price);
				$("#goods_enable_store").val(data.enable_quantity);
				$(".groupbuy_goods_info").show();
			}
		});
	});

	$("#okBtn").click(function(){
		
		var api = ''
		var str = GetQueryString('goodsid');
		if(str){
			api = '/api/b2b2c/groupBuy/update.do'
		}else {
			api = '/api/b2b2c/groupBuy/add.do'
		}
		if($("#act_id").val()==-1){
			$.message.error("暂无团购活动！敬请期待");
			return false;
		}
		if ( $("#gb_name").val() == "") {
			$.message.error("请输入团购名称");
			return false;
		}
		if(isNaN($("#price").val())){
			$.message.error("团购价格必须是数字");
	        return false;
	    }
		if( $("#price").val() == ""){
			$.message.error("请输入团购价格");
	        return false;
	    }
		var image_ipt = $("#upload_src").val(); 
		if( image_ipt ==""){
			$.message.error("必须上传团购图片");
			return false;
		}
		if($("#goods_name_ipt").val()==""){
			$.message.error("必须选择商品");
			return false;
		}
		if(isNaN($("#goods_num").val())){
			$.message.error("商品总数必须是数字");
	        return false;
	    }
		if(isNaN($("#visual_num").val())){
			$.message.error("虚拟数量必须是数字");
	        return false;
	    }
		if(isNaN($("#limit_num").val())){
			$.message.error("限购数量必须是数字");
			return false;
		}
		if( $("#groupbuy_agreement:checked").size()==0 ){
			$.message.error("您必须同意协议才能保存");
			return false;
		}
		if($("input[name='goods_num']").val()<1){
			$.message.error("团购数量必须大于等于1，并且不能超过商品数量");
			return false;
		}

		$("#gb_form").ajaxSubmit({
			url: ctx + api,
			type: 'POST',
			success:function(result){
				if(result.result==1){
					$.message.success('添加成功', function(){
						location.href = './groupbuy_list.html'
					});
				}else{
					$.message.error(result.message)
				}
			},
			error:function(){
				$.message.error('出现错误，请重试！')
			}

		});
	});
});