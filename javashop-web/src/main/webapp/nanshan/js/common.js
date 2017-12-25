
//列表页转换状态
function convertState(state){
	if(state == 1){
		return "已删除"
	}else{
		return "有效";
	}
};

//列表页转换时间
function convertDate(dateInfo){
	var d = new Date(dateInfo * 1000);    //根据时间戳生成的时间对象
	var date = (d.getFullYear()) + "-" + 
	           (d.getMonth() + 1) + "-" +
	           (d.getDate()) + " " + 
	           (d.getHours()) + ":" + 
	           (d.getMinutes()) + ":" + 
	           (d.getSeconds());
	return date;
};

//列表页转换时间
function convertImg(dateInfo){
	if(dateInfo == null){
		return '';
	}
	var img = "<img src='"+dateInfo+"'width='100' height='100' />"
	return img;
};


function delButton(k){
	 layer.confirm('确认要删除吗？', {
		  btn: ['删除','取消'] //按钮
		  ,area:['80px','150px']
		}, function(){
			$(k).parent().parent().remove();
		  	 layer.msg({
			    time: 20000
			 }); 
		}, function(){
		    layer.msg({
			    time: 20000
			}); 
		});
};