/**
 * 重构 by 陈小博 on 2018-8-14
 */
(function() {
	window.onload = function() {
		// 加载验证码
		$("#validateCode").attr("src",
				ctx + "/validcode.do?vtype=admin&rmd=" + new Date().getTime());
		// 看不清，换一张
		$("#validateCode").click(
				function() {
					console.log(2)
					$(this).attr(
							"src",
							ctx + "/validcode.do?vtype=admin&rmd="
									+ new Date().getTime());
				});

		var login = function() {
			var ok = true;

			var username = $("input[name=username]");
			var password = $("input[name=password]");
			var valid_code = $("input[name=vadilate]");
			if (username.val().length == 0) {
				username.next().css("display", "block");
				ok = false;
			}
			if (password.val().length == 0) {
				password.next().css("display", "block");
				ok = false;
			}
			if (valid_code.val().length == 0) {
				valid_code.parent().next().css("display", "block");
				ok = false;
			}
			if (ok) {
				// 显示 登录中...
				$(".loginButton").text("登录中...")
				$.ajax({
					url : ctx + "/core/admin/admin-user/login.do",
					type:"POST",
					data : {
						username : username.val(),
						password : password.val(),
						valid_code : valid_code.val()
					},
					success : function(result) {
						location.href = ctx + "/admin/main.do";
					},
					error : function(e) {
						alert(e.responseJSON.error_message);
						$(".loginButton").text("登录")
					}
				})
			}
		}

		$(".loginButton").click(login);

		// 回车触发登录
		$(document).keydown(function(event) {
			if (event.keyCode == 13) {
				login();
			}
		});

		// 遍历绑定blur事件.
		// 选择判断 去选择 提示信息的显隐
		$(".input").each(function(index, ele) {
			$(this).on("blur", function() {
				if ($(this).val().length == 0) {
					if (index == 2) {
						$(this).parent().next().css("display", "block");
					} else {
						$(this).next().css("display", "block");
					}
				} else {
					if (index == 2) {
						$(this).parent().next().css("display", "none");
					} else {
						$(this).next().css("display", "none");
					}
				}

			})
		})
	}
})(window)