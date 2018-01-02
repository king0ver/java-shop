$(function(){
	$("#submit-btn").click(function(){


		var activityId = $("#activityId").val();
		var endTime = $("#reservation input[name='yyEnd']").val();
		var uname = $("#uname").val();
		var age = $("#age").val();
		var phone = $("#phone").val();
		var email = $("#email").val();

		if(endTime.length == 0){
			alert("请填写参加活动日期!");
			return false;
		}
		if(uname.length == 0){
			alert("请填写参加成员!");
			return false;
		}
		if(age.length == 0){
			alert("请填写年龄!");
			return false;
		}
		if(phone.length == 0){
			alert("请填写联系电话!");
			return false;
		}
		if(email.length == 0){
			alert("请填写电子邮件!");
			return false;
		}

		$.post("/activity-operation/appoint.do",{activity_id:activityId,activity_time:endTime,member_name: uname,
			member_age:age, phone_number:phone, email: email},function(data){

			if(data.result == 1){
				alert("预约成功!");
			}if(data.result == 0 && data.message == "not login") {
				alert("您还未登录系统!");
			}else{
				alert("预约异常!");
			}
		},"json")

	});
	$('.yuyue').on('click',function(){
		$('.bgColorShadow').show()
		$('.activity-reservation').animate({"right":"0px"},400).show()
	})
	$('.item-title').on('click',function(){
		$('.bgColorShadow').hide()
		$('.activity-reservation').animate({"right":"-549px"},400)
	});
	$("div[reservation-id]").on('click',function(){
		$("#activityId").val($(this).attr("reservation-id"));
		$('.bgColorShadow').show()
		$('.activity-reservation').animate({"right":"0px"},400).show();
	})
	$('.Wdate').on('click',function(){
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})
	})
});
