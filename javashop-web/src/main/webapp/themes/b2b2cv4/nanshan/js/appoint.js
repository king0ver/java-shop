$(function(){
	$("#submit-btn").click(function(){


		var activityId = $("#activityId").val();
		var endTime = $("#reservation input[name='yyEnd']").val();
		var uname = $("#uname").val();
		var phone = $("#phone").val();
		var email = $("#email").val();
		var num = $("#num").val();
		if(endTime.length == 0){
			layer.alert("请填写参加活动日期!");
			return false;
		}
		if(uname.length == 0){
			layer.alert("请填写参加成员!");
			return false;
		}
		if(phone.length == 0){
			layer.alert("请填写联系电话!");
			return false;
		}
		if(email.length == 0){
			layer.alert("请填写电子邮件!");
			return false;
		}
		if(num.length == 0){
			layer.alert("请填写参加人数!");
			return false;
		}
		if(!$.isNumeric(num)){
			layer.alert("参加人数必须为数字!");
			return false;
		}

		$.post("/activity-operation/appoint.do",{activity_id:activityId,activity_time:endTime,member_name: uname,
			phone_number:phone, email: email,num:num},function(data){

			if(data.result == 1){
				layer.alert("预约成功!",function(){
					location.href="/nanshan/myAppointment.html";
				});

			}else if(data.result == 0 && data.message == "not login") {
				layer.alert("您还未登录系统!",function(){
					location.href ="/store/login.html";
				});

			}else{
				layer.alert(data.message);
			}
		},"json")

	});
	$('.yuyue').on('click',function(){

		var activityId = $("#activityId").val();

		$.post("/activity-operation/isAppoint.do",{activity_id:activityId},function(data){

			if(data.result == 1){
				$('.bgColorShadow').show();
				$('.activity-reservation').animate({"right":"0px"},400).show();
			}else if(data.result == 0 && data.message == "not login") {
				layer.alert("您还未登录系统!",function(){
					location.href ="/store/login.html";
				});

			}else{
				layer.alert(data.message);
			}
		},"json");
	})
	$('.item-title').on('click',function(){
		$('.bgColorShadow').hide()
		$('.activity-reservation').animate({"right":"-549px"},400)
	});
	$("div[reservation-id]").on('click',function(e){

		e.stopPropagation();

		var activityId = $(this).attr("reservation-id");

		$.post("/activity-operation/isAppoint.do",{activity_id:activityId},function(data){

			if(data.result == 1){
				$("#activityId").val(activityId);
				$('.bgColorShadow').show()
				$('.activity-reservation').animate({"right":"0px"},400).show();
			}else if(data.result == 0 && data.message == "not login") {
				layer.alert("您还未登录系统!",function(){
					location.href ="/store/login.html";
				});
			}else{
				layer.alert(data.message);
			}
		},"json");
	})

});
