/**
 * 焦点图设计器
 * @author:fk
 * @date:2017-06-22
 */
var FocusDesign = function (el) {
	var _this = this;
	_this.focusHtml = '<div class="panel" style="height:180px;position:relative;">' +
		'<div class="body" >	' +
		'<div class="main layout " layout_id="main" >' +
		'<a class = "block edit-enable"> <img/> </a>' +
		'</div>' +
		'</div>' +
		'</div>";';
	_this.focusEl = el;
	//获取数据
	var focusData = _this.getDataSource();
	if (focusData && focusData.length > 0) {
		_this.drawFocuces(focusData);
	}

};

/**
 * 获取焦点图
 */
FocusDesign.prototype.getDataSource = function () {
	var _this = this;
	var focusData = [];
	$.ajax({
		url: ctx + "/cms/admin/focus.do?client_type=pc",
		async: false,
		success: function (json) {
			focusData = json;
		}
	});

	return focusData;
};


/**
 * 添加焦点图
 */
FocusDesign.prototype.addFocus = function () {
	var _this = this;
	$.SingleImageSelector({
		confirm: function (image) {

			var data = {
				pic_url: image.image_url,
				operation_type: image.op_type,
				operation_param: image.op_value,
				client_type: 'pc'
			};
			$.ajax({
				url: ctx + "/cms/admin/focus.do?pc",
				data: data,
				async: false,
				method: "post",
				success: function (focus) {
					$.success("成功");
					_this.draw(focus);
				},
				error: function (e) {
					$.error(e.responseJSON.error_message);
				}
			});

		}
	});
};

/**
 * 渲染单个图片
 */
FocusDesign.prototype.draw = function (focus) {
	var _this = this;
	//页面动态
	var html = $(_this.focusHtml);
	html.find("img").attr("src", focus.pic_url);
	html.find("img").attr("focus_id", focus.id);
	_this.focusEl.append(html);
	var single = new SingleFocus(html, focus);
	//把手
	var handle = new Handle(html);
	handle.draw();
}

/**
 * 循环渲染多个焦点图
 */
FocusDesign.prototype.drawFocuces = function (focusData) {
	var _this = this;
	for (var i in focusData) {
		var focus = focusData[i];
		_this.draw(focus);
	}
}

/**
 * 单个焦点
 */
var SingleFocus = function (el, focus) {
	$(el).data("obj", this);
	this.el = el;
	this.data = focus;
}

/**
 * 单个焦点编辑，修改
 */
SingleFocus.prototype.openEditor = function () {
	var _this = this;
	var focus_id = _this.el.find("img").attr("focus_id");
	var defaultimage = _this.data;
	if (!defaultimage) {
		defaultimage = {};
		defaultimage.image_url = "";
		defaultimage.op_type = "";
		defaultimage.op_value = "";
	}
	$.SingleImageSelector({
		confirm: function (image) {
			var data = {
				pic_url: image.image_url,
				operation_type: image.op_type,
				operation_param: image.op_value,
				client_type: 'pc'
			};

			$.ajax({
				url: ctx + "/cms/admin/focus/" + focus_id + ".do",
				data: data,
				async: false,
				method: "post",
				success: function (response) {
					$.success("成功");
					_this.data = response;
					_this.draw(response);
				},
				error: function (e) {
                    $.error(e.responseJSON.error_message);
				}
			});

		},
		defaultvalue: [
			{
				img_url: defaultimage.pic_url,
				customParams: [
					{ name: 'op_type', value: defaultimage.operation_type },
					{ name: 'op_value', value: defaultimage.operation_param },
				]
			}
		]
	});
}

SingleFocus.prototype.draw = function (focus) {
	var _this = this;
	_this.el.find("img").attr("src", focus.pic_url);
}

/**
 * 删除
 */
SingleFocus.prototype.del = function () {
	var _this = this;
	var focus_id = _this.el.find("img").attr("focus_id");
	$.confirm("您确定删除该图片吗？", {
		ok: function () {
			$.ajax({
				url: ctx + "/cms/admin/focus/" + focus_id + ".do",
				async: false,
				type: 'DELETE',
				success: function () {
					$.success("成功");
					_this.el.remove();
				},
				error: function (e) {
					$.error(e.responseJSON.error_message);
				}
			});
		},
		cancel: function () {
			console.log("点了取消");
		}
	});
}


/**
 * 可编辑"把手"对象
 */
var Handle = function (targetEl) {

	var handelHtml = "";
	handelHtml += '<div class="handle">';
	handelHtml += '<span id = "handle-edit"><i class="fa fa-paint-brush"></i>编辑</span>';
	handelHtml += '<span id = "handle-delete"><i class="fa fa-paint-brush"></i>删除</span>';
	handelHtml += '<div class="handle-body"></div>';
	handelHtml += '</div>';

	this.handleHtml = handelHtml;
	this.targetEl = targetEl;
}

Handle.prototype.draw = function () {
	var self = this;
	var el = this.targetEl;
	var block = el.data("obj");
	var handle = $(this.handleHtml);
	el.append(handle).children(".handle").hover(function () {
		$(this).children().show();
	}, function () {
		$(this).children().hide();
	});
	el.find("#handle-edit").click(function () {
		block.openEditor();
	});
	el.find("#handle-delete").click(function () {
		block.del();
	});
}
