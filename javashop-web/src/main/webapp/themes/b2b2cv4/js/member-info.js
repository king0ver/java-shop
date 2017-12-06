/**
 * Created by Andste on 2017/7/20.
 */
$(function(){
    //  居住地发生改变时触发
    $('.choseAddr').change(function(){
        var province_id = $('[name="province_id"]'),
            city_id = $('[name="city_id"]'),
            region_id = $('[name="region_id"]');
        if(province_id.val() === 0){
            province_id.css({border: '1px solid #a94442'});
            $('.choseAddr').addClass('error');
        }else {
            province_id.css({border: '1px solid #cccccc'});
            $('.choseAddr').removeClass('error');
        }
        if(city_id.val() === 0){
            city_id.css({border: '1px solid #a94442'});
            $('.choseAddr').addClass('error');
        }else {
            city_id.css({border: '1px solid #cccccc'});
            $('.choseAddr').removeClass('error');
        }
        if(region_id.val() === 0){
            region_id.css({border: '1px solid #a94442'});
            $('.choseAddr').addClass('error');
        }else {
            region_id.css({border: '1px solid #cccccc'});
            $('.choseAddr').removeClass('error');
        }
    });
     	$('#mobile').on('blur', function(){
		var _this = $(this),
			_val  = _this.val();
		    if(!(/^1[34578]\d{9}$/.test(_val))){
                _this.parent().addClass('error has-error');
			}else {
				_this.parent().removeClass('error has-error');
			}
	});
     
     
    $('#email').on('change', function(){
        var _this = $(this),
            _val  = _this.val();
        if(!/^([a-z0-9+_]|\-|\.|\-)+@([\w|\-]+\.)+[a-z]{2,4}$/i.test(_val)){
            _this.parent().addClass('error has-error')
        }else {
            $.ajax({
                url     : ctx + "/api/shop/member/checkemailInEdit.do",
                type    : "POST",
                data    : "email=" + _val,
                dataType: "json",
                success : function (res) {
                    if (res.result === 0) {
                        $.message.error('邮箱重复');
                        _this.parent().addClass('error has-error')
                    } else {
                        _this.parent().removeClass('error has-error')
                    }
                }
            });
        }
    });

    $("#btnSubmit").on('click', function () {
        var form = $("#form_saveMember");
        form.find("input[type='text']").each(function () {
            var $this = $(this), _val = $this.val();
            if (!_val && $this.attr("isrequired") === "true") {
                $this.addClass('error')
                     .css({border: '1px solid #a94442'})
                     .blur(function () {
                         if (_val) {
                             $this.removeClass("error");
                             $this.css({border: '1px solid #cccccc'});
                         }
                     })
            } else {
                $(this).removeClass('error');
                $(this).css({border: '1px solid #cccccc'});
            }
        });
        var _this = $(this);
        if (form.find('.error').length > 0) {
            $.message.error('表单填写有误，请检查高亮！');
            return false;
        }
        if($("input[name='province_id']").val()==""){
	    		$.message.error('请填写地址信息！');
	        return false;
        }
        $.blockUI({
            message: '<P style="line-height: 35px; font-size: 12px;">正在保存，请稍候...</P>',
            onBlock: function () {
                _this.attr('disabled', 'disabled')
            }
        });

        var options = {
            url     : ctx + "/api/shop/member/re-send-reg-mailsave-info.do",
            dataType: "json",
            type    : 'post',
            success : function (result) {
                if (result.result === 1) {
                    unlock(_this);
                    setTimeout(location.href = "member.html", 1000);
                }
                else {
                    $.message.error(result.message);
                    unlock(_this)
                }
            },
            error   : function () {
                $.message.error('出现错误，请重试！');
            }
        };

        setTimeout(function () {
            $("#form_saveMember").ajaxSubmit(options)
        }, 1000);

        function unlock(_this) {
            $.unblockUI({
                onUnblock: function () {
                    _this.removeAttr('disabled')
                }
            });
        }
    });

    //  修复IE8下造成的下拉选择框宽度问题
    if(Sys.ie === 8){
        $('#province_id').css({width: 100});
        $('#city_id').css({width: '28%'});
        $('#region_id').css({width: '30%'});
    }

    //  头像修改预览【IE只兼容10+】
    if(window.FileReader){
        $("#changeFace").on('change', function () {
            var $this = $(this);
            var file = $this[0].files[0], type = file.type;
            if(!/image\/\w+/.test(type)){ $.message.error('文件必须为图片类型！'); return }
            var reader = new window.FileReader();
            reader.readAsDataURL(file);
            reader.onload = function () {
                $('.__face__').attr('src', this.result);
            };
        })
    }
});