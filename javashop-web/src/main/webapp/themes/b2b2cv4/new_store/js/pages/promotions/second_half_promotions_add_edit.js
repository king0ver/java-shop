/**
 * Created by zjp on 2017/3/10.
 */
$(function(){
    var ue = UE.getEditor('editor');
    UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
    UE.Editor.prototype.getActionUrl = function(action) {
        if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadimage') {
            return ctx+"/api/base/upload-image/upload-img.do";
        } else {
            return this._bkGetActionUrl.call(this, action);
        };
    };


       //初始化商品选择器
    		var goods_box = $('.promotions-goods-box'), tag = $("input[name ='range_type']:checked");
        if(tag.val() == '1'){
            goods_box.css({display: 'none'});
        }else if(tag.val() == '2'){
            goods_box.css({display: 'block'});
            var goodsList  =[];
      		$(".goodsId").each(function(index,val){
      			var goods_id = $(this).attr("value");
      			goodsList.push(goods_id);
      		});
              console.log(goodsList)
              $("#test").GoodsSellerSelector({
              	maxLength: 0,  //最大可选商品数量,0代表无数量限制.
              	goodsIdList: goodsList, // 默认选择的商品ID
              	refresh: function (data) {   // 商品数据发生变化时的回调函数.
              		console.log('Goods has changed-->',data);
              	}
              });
        }
    //  全部、部分参加活动商品
    (function(){
        var check = $('#all_goods, #part_goods');
        check.unbind('click').on('click', function(){
            var _this = $(this), goods_box = $('.promotions-goods-box'), tag = _this.attr('tag');
            if(tag == 'all'){
                goods_box.css({display: 'none'});
            }else if(tag == 'part'){
                goods_box.css({display: 'block'});
                var goodsList  =[];
          		$(".goodsId").each(function(index,val){
          			var goods_id = $(this).attr("value");
          			goodsList.push(goods_id);
          		});
                  console.log(goodsList)
                  $("#test").GoodsSellerSelector({
                  	maxLength: 0,  //最大可选商品数量,0代表无数量限制.
                  	goodsIdList: goodsList, // 默认选择的商品ID
                  	refresh: function (data) {   // 商品数据发生变化时的回调函数.
                  		console.log('Goods has changed-->',data);
                  	}
                  });
            }

        });
    })();
    
  
});


