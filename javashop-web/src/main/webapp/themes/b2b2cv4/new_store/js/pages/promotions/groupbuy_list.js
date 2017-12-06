/**
 * Created by Andste on 2016/9/2.
 */
$(function(){

	//  删除
	$('.groupbuy-delete').on('click', function(){
		var gb_id = $(this).attr('gb_id');
		$.confirm('确定要删除吗？', function(){
			$.ajax({
				url: ctx + '/api/b2b2c/groupBuy/delete.do?gb_id=' + gb_id,
				type: 'POST',
				success: function(result){
					if(result && typeof result == 'string'){
						result = JSON.parse(result)
					}
					if(result.result == 1){
						$.message.success('删除成功', 'reload')
					}else {
						$.message.error(result.message)
					}
				},
				error: function(){
					$.message.error('出现错误，请重试！')
				}
			})
		})
	})
	//  查询
	$('#filter_seach').on('click', function(){
		var filter_name = $("#filter_name").val();
		var groupbuy_state = $("#groupbuy-state").val();
		if(filter_name!=""&&groupbuy_state!=""){
			location.href="./groupbuy_list.html?gb_name="+filter_name+"&gb_status="+groupbuy_state;
		}else if(filter_name!=""){
			location.href="./groupbuy_list.html?gb_name="+filter_name;
		}else if(groupbuy_state!=""){
			location.href="./groupbuy_list.html?gb_status="+groupbuy_state;
		}else{
			location.href="./groupbuy_list.html"
		}
	})
});