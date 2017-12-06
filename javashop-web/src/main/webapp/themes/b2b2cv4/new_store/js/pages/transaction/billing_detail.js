/**
 * Created by Andste on 2016/6/22.
 */
$(function(){

    //  tab切换
    (function(){
        var tab = $('.app-tab-tools');
        tab.find('a').unbind('click').on('click', function(){
            loadPage($(this).attr('order_state'));
        });
    })();

    loadPage('all')
    // 订单列表页加载
    function loadPage(str){
        var loadBox = $('.bill-box'), bill_id = $('.bill-box').attr('bill_id');
        if(str ==  'all'){
            loadBox.empty().load('./order_detail_all_list.html?bill_id='+ bill_id, function(){
            	$('#order_all_list').addClass('active').siblings().removeClass('active');
                     
                 	$(".bill-box").on("click",".unselected",function(){
                     	var page = $(this).attr("page");
                 		$.ajax({
                 			url:"./order_detail_all_list.html?sn="+ sn +"&page="+page,
                 			dataType:"html",
                 			success:function(result){
                 				$(".bill-box ").html(result);
                 			},
                 			error:function(){
                 				$.alert("出错了:(");
                 			}
                 		});
                     });
            });
        }else if(str == 'return') {
            loadBox.empty().load('./order_detail_return_list.html?bill_id='+ bill_id, function(){
            	$('#order_return_list').addClass('active').siblings().removeClass('active');
            	$(".bill-box").on("click",".unselected",function(){
                 	var page = $(this).attr("page");
             		$.ajax({
             			url:"./order_detail_return_list.html?bill_id="+ bill_id +"&page="+page,
             			dataType:"html",
             			success:function(result){
             				$(".bill-box").html(result);
             			},
             			error:function(){
             				$.alert("出错了:(");
             			}
             		});
                 });
            });
        }else {
            loadBox.empty();
        };
    };
    
     
    
});