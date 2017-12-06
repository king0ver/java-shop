/**
 * 楼层设计器
 * @author:kingapex
 * @date:2017-06-18
 */
var FloorDesign = function (el, options) {
	//获取楼层的数据源
	this.floorData = this.getDataSource(options.floorid);
	this.designEl = $(".floor-design>.floor");

	this.designEl.data("obj", this);
	this.designEl.on("dataRefresh", this.onDataRefresh);

	var floorTitle = new FloorTitle();
	floorTitle.draw(this.floorData);

	var panelList = this.floorData.panelList;

	//初始化面板，并绘制
	for (i in panelList) {
		var panel = panelList[i];
		var panelObj = new Panel(this.designEl, panel.tpl_content);
		panelObj.draw(panel);
	}

};



/**
 * floor的数据源
 */
FloorDesign.prototype.getDataSource = function (floorid) {
	var floor;
	$.ajax({
		url: ctx + "/cms/admin/floor/mobile/design.do",
		dataType: "json",
		async: false,
		success: function (json) {
			floor = json
		},
	});
	// 遍历检测到品牌面板,然后替换其中的brand属性为brandList.
	// 因为后续操作用的是这个属性,而且设计到后端,所以必须是它.
	if (floor && floor.panelList) {
		floor.panelList.forEach(function (ele) {
			if (ele.layoutList.length > 0) {
				var block = ele.layoutList[0].block_list[0];
				if (block.block_type == 'BRAND') {
					block.brandList = JSON.parse(JSON.stringify(block.brand));
					delete block.brand;
				}
			}
		});
	}
	return floor;
};



/**
 *floor数据刷新事件
 */
FloorDesign.prototype.onDataRefresh = function () {

	var $this = $(this);
	var floor = $this.data("obj");
	var floorData = floor.floorData;
	delete floorData.panelList;
	floorData.panelList = [];
	$this.find(".panel").each(function (i, ele) {
		var panelData = $(ele).data("obj").panelData;
		floorData.panelList.push(panelData);
	});

}


/**
 * 添加一个面板
 */
FloorDesign.prototype.addPanel = function (tpl_id) {
	var self = this;
	var html;
	$.ajax({
		url: ctx + "/cms/admin/panel-tpl/content/" + tpl_id + ".do",
		async: false,
		success: function (data) {
			html = data;
		},
		error: function (e) {
			$.error(e.responseJSON.error_message);
		}
	});
	var panel = self.savePanel(tpl_id);
	var panelObj = new Panel(self.designEl, html);
	panelObj.panelEl.attr("tpl_id", tpl_id);
	//绑定对象
	panel.tpl_content = html;
	panel.tpl_type = "normal";

	var panelO = panelObj.panelEl.data("obj");

	panelO.draw(panel);

	panelObj.panelEl.trigger("dataRefresh");

}

/**
 * 保存面板panel
 */
FloorDesign.prototype.savePanel = function (tpl_id) {
	var self = this;
	var data = {};
	var floor = self.floorData;
	data.floor_id = floor.id;
	data.panel_tpl_id = tpl_id;
	var panel = floor.panelList[floor.panelList.length - 1];
	//	data.panel_id = panel.panel_id;
	// data.sort = panel.sort;
	data.panel_tpl_id = tpl_id;
	var layoutList = JSON.stringify([]);
	data.panel_data = layoutList;
	var result;
	$.ajax({
		url: ctx + "/cms/admin/floor/panel.do",
		method: "post",
		async: false,
		data: data,
		dataType: "json",
		success: function (res) {
			$.success("保存面板信息成功!");
			result = res;
		},
        error: function (e) {
            $.error(e.responseJSON.error_message);
        }
	});

	return result;
}


/**
 * 保存设计
 */
FloorDesign.prototype.save = function () {
	var jsonData = this.floorData;

	var postData = jQuery.extend(true, {}, jsonData);

	var panelList = postData.panelList;
	for (var i in panelList) {
		var panel = panelList[i];
		var layoutList = JSON.stringify(panel.layoutList);
		panel.panel_data = layoutList;
		delete panel.layoutList
	}

	$.ajax({
		url: ctx + "/cms/admin/floor/design.do",
		method: "POST",
		dataType: "json",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(postData),
		success: function (json) {
			$.success("保存楼层设计信息成功!");
		},
		error: function (e) {
			$.error(e.responseJSON.error_message);
		}
	});
}


/**
 * 楼层标题
 */
var FloorTitle = function () {
	var titleEl = $(".floor-design>.header");
	this.titleEl = titleEl;
	titleEl.data("obj", this);
	var handle = new Handle(titleEl);
	handle.draw();
}

/**
 * 楼层标题加载
 */
FloorTitle.prototype.draw = function (floorData) {
	this.floorData = floorData;
	this.titleEl.find("H2.title>span").text(floorData.name)
}

/**
 * 楼层编辑器
 */
FloorTitle.prototype.openEditor = function () {
	var self = this;
	var text = self.titleEl.find("H2.title>span").text();
	var _dlgEl;
	$.dialog({
		url: ctx + 'cms/admin/selector/title_selector.html',
		area: ['500px', '500px'],
		success: function (dlgEl) {
			dlgEl.find("input[name=title]").val(text);
			_dlgEl = dlgEl;
		},
		buttons: [{
			"text": "保存",
			"event": function (dialog) {
				var new_title = _dlgEl.find("input[name=title]").val();
				var data = self.floorData;
				delete data.panelList;
				data.name = new_title;
				$.ajax({
					url: "${ajax}/cms/admin/floor/" + data.id + ".do",
					method: "POST",
					data: data,
					success: function (json) {
						$.success("楼层信息保存成功!");
					},
					error: function () {
						$.error("楼层信息保存失败!");
					}
				});
				dialog.close();
				self.draw(data);
			}
		}]
	});
}


/**
 * 面板对象
 */
var Panel = function (designEl, panelHtml) {

	this.panelHtml = panelHtml;

	var panel_el = $(this.panelHtml);
	panel_el.appendTo(designEl);
	this.panelEl = panel_el;

	//	var panel_id = panel_el.attr("panel_id");
	// var sort = $(".panel").size();
	//设置panel id
	//	if(!panel_id) {
	//		panel_id = "panel_"+sort;
	//	}

	//	panel_el.attr("panel_id",panel_id);

	//绑定对象
	panel_el.data("obj", this);

	//定义数据刷新事件
	panel_el.on("dataRefresh", this.onDataRefresh)

	//对所有的布局绑定对象
	panel_el.find(".layout").each(function (i, ele) {
		var layoutEl = $(ele);
		var layout = new Layout(layoutEl);
		layoutEl.data("obj", layout);
	});
	this.drawHandle();
}

/**
 * 绘制“面板把手”
 */
Panel.prototype.drawHandle = function () {
	var self = this;
	var handleHtml = "";
	handleHtml += '<div class="panel-handle">';
	handleHtml += '<span class="up"><a href="javascript:;" ><i class="layui-icon" style="font-size: 25px; color: #324057;">&#xe619;</i></a></span>';
	handleHtml += '<span  class="down"><a href="javascript:; "><i class="layui-icon" style="font-size: 25px; color: #324057;">&#xe61a;</i></a></span>';
	handleHtml += '<span  class="del"><a href="javascript:;" ><i class="layui-icon" style="font-size: 25px; color: #324057;">&#xe640;</i></a></span>';
	handleHtml += '</div>';

	this.panelEl.append($(handleHtml));

	//删除面板
	// 添加off() 的理由同下(edit)
	$(".del").off().click(function () {
		var _this = $(this);
		$.confirm("确认删除此板块吗？", {
			ok: function () {
				var panelEl = _this.parents(".panel");
				var panel = panelEl.data("obj");
				var tpl_type = panel.panelData.tpl_type;
				//主面板不允许删除
				$.ajax({
					url: ctx + "/cms/admin/floor/panel/" + panel.panelData.id + ".do",
					method: "DELETE",
					success: function (json) {
						panelEl.remove();
						$.success("删除成功!");
					},
					error: function (e) {
						$.error(e.responseJSON.error_message);
					}
				});
			},
			cancel: function () {
				$.warn("取消删除!");
			}
		});
	});

	$(".up").off().click(function (event) {
		var _this = $(this);
		if (_this.closest('.panel').prev().hasClass('floor-top-bar')) {
			// 上面是header的话
			return;
		}
		var panelEl = _this.parents(".panel");
		var panel = panelEl.data("obj");
		var data = panel.panelData;
		$.ajax({
			url: ctx + "/cms/admin/floor/panel/" + panel.panelData.id + "/sort.do",
			method: "POST",
			data: { "sort": "up" },
			success: function (json) {
				var before = _this.closest('.panel').prev();
				if (before.length === 1) {
					_this.closest('.panel').insertBefore(before);
				}
			}
		});
	});


	$(".down").off().click(function (event) {
		var _this = $(this);
		var panelEl = _this.parents(".panel");
		var panel = panelEl.data("obj");
		var data = panel.panelData;
		$.ajax({
			url: ctx + "/cms/admin/floor/panel/" + panel.panelData.id + "/sort.do",
			method: "POST",
			data: { "sort": "down" },
			success: function (json) {
				var after = _this.closest('.panel').next();
				if (after.length === 1) {
					_this.closest('.panel').insertAfter(after);
				}
			}
		});
	});
}


/**
 * 绘制面板方法
 */
Panel.prototype.draw = function (panelData) {
	//板绑定数据,并绘制
	this.panelData = panelData;
	if (this.panelData) {
		var layout_list = panelData.layoutList;
		for (var i in layout_list) {
			var layoutData = layout_list[i];
			var layoutEl = this.panelEl.find(".layout[layout_id=" + layoutData.layout_id + "]");
			//找到对象并绘制
			var layout = layoutEl.data("obj");
			// 2017-8-2-陈小博: 加上是否存在的判断条件.
			// 因为:如果用户改变了模板的结构,这时候点开楼层设计的时候,从服务器传来的数据是基于上一个模板的.
			// 所以,在初始化(draw)的时候,就会"对不上".所以这里加个判断.对不上的话,就跳过.
			if (layout) {
				layout.draw(layoutData);
			}
		}
		this.panelEl.prepend("<div class='panelhandle'>" + panelData.panel_name + "</div>");

	}
}

Panel.prototype.onDataRefresh = function () {
	var $this = $(this);
	var panelObj = $this.data("obj");
	var panelData = panelObj.panelData;
	//重新建立panel data
	if (panelData) {
		delete panelData.layoutList;
	} else {
		panelData = {
			//				panel_id:$this.attr("panel_id"),
			// sort: 0,
			panel_tpl_id: $this.attr("tpl_id"),
		}
	}

	panelData.layoutList = [];

	panelObj.panelEl.find(".layout").each(function (i, ele) {
		var layoutData = $(ele).data("obj").layoutData;
		if (layoutData) {
			panelData.layoutList.push(layoutData);
		}
	});
	panelObj.panelData = panelData;
}

/**
 * 布局对象
 */
var Layout = function (layoutEl) {

	this.layoutEl = layoutEl;

	//定义数据刷新事件
	this.layoutEl.on("dataRefresh", this.onDataRefresh)

	//为所有的区块绑定对象
	this.layoutEl.find(".block").each(function (i, ele) {
		var blockEl = $(ele);
		var block = new Block(blockEl);
	});
}


/**
 * 布局的绘制方法
 */
Layout.prototype.draw = function (layoutData) {

	var layoutEl = this.layoutEl;
	this.layoutData = layoutData;

	//绘制区块
	var block_list = layoutData.block_list;
	if (block_list) {
		for (var i in block_list) {
			var blockData = block_list[i];
			var blockEl = layoutEl.find(".block[block_id=" + blockData.block_id + "]");

			if (blockEl.size() > 0) {
				//找到对象并绘制
				var block = blockEl.data("obj");
				if (block) {
					block.draw(blockData);
				}
			}


		}
	}

}

/**
 * 布局的数据刷新方法
 */
Layout.prototype.onDataRefresh = function () {
	var $this = $(this);
	var layoutObj = $this.data("obj");
	var layoutData = layoutObj.layoutData;

	//重新建立layout data
	if (layoutData) {
		delete layoutData.block_list;
	} else {
		layoutData = {
			layout_id: $this.attr("layout_id")
		}
	}

	layoutData.block_list = [];


	//使block data和layout建立关联
	$this.find(".block").each(function (i, ele) {
		var blockData = $(ele).data("obj").blockData;
		layoutData.block_list.push(blockData);
	});
	layoutObj.layoutData = layoutData;
}

/**
 * 区块对象
 */
var Block = function (blockEl) {

	var block_type = blockEl.attr("block_type");

	if (block_type == "MANUAL_GOODS") {
		return new ManualGoodsBlcok(blockEl);
	}

	if (block_type == "SINGLE_IMAGE") {
		return new SignleImageBlcok(blockEl);
	}

	if (block_type == "BRAND") {
		return new BrandBlcok(blockEl);
	}

	if (block_type == "MULTI_IMAGE") {
		return new MultiImageBlcok(blockEl);
	}

	if (block_type == "TEXT") {
		return new TextBlcok(blockEl);
	}
}

/**
 * 手动规则商品区块
 */
var ManualGoodsBlcok = function (blockEl) {

	this.blockEl = blockEl;
	this.blockEl.data("obj", this);
	var handle = new Handle(this.blockEl);
	handle.draw();
	//定义数据刷新事件
	this.blockEl.on("dataRefresh", this.onDataRefresh)


}

/**
 * 文本区块
 */
var TextBlcok = function (blockEl) {
	this.blockEl = blockEl;
	this.blockEl.data("obj", this);
	var handle = new Handle(this.blockEl);
	handle.draw();
	//定义数据刷新事件
	this.blockEl.on("dataRefresh", this.onDataRefresh)
}

/**
 * 文本区块的绘制方法
 */
TextBlcok.prototype.draw = function (blockData) {

	this.blockData = blockData;
	var blockEl = this.blockEl;

	var text = blockData.text;
	var textEl = blockEl.find(".text");
	this.drawText(textEl, text);
}

/**
 * 文本区块的绘制方法
 */
TextBlcok.prototype.drawText = function (textEl, text) {
	if (text) {
		textEl.find("a").text(text.title_text);
		textEl.find("a").attr("href", text.value);
		textEl.find("a").attr("data-type", text.text_type)
		textEl.show();
	}
}

/**
 * 文本区块的数据刷新方法
 */
TextBlcok.prototype.onDataRefresh = function (event, text) {

	var blockEl = $(this);
	var block = blockEl.data("obj");
	var blockData = block.blockData;

	if (blockData) {
		blockData.text = text;
	} else {

		blockData = {
			block_type: "TEXT",
			block_id: blockEl.attr("block_id"),
			text: text
		};
	}
	//刷新数据
	block.blockData = blockData;

}

/**
 * 文本区块的编辑器
 */
TextBlcok.prototype.openEditor = function () {
	var obj = this.blockEl.data("obj");

	var textEl = this.blockEl.find(".text");
	var self = this;

	$.TextSelector({
		defaultData: {
			content: textEl.find('a').text(),
			op_type: textEl.find('a').attr('data-type'),
			op_value: textEl.find('a').attr('href')
		},
		confirm: function (textResult) {
			var text = {
				title_text: textResult.content,
				text_type: textResult.op_type,
				value: textResult.op_value
			};

			//重新绘制文本
			self.drawText(textEl, text);
			//触发数据刷新事肵
			self.blockEl.trigger("dataRefresh", [text]);
		}
	});
}


/**
 * 区块的绘制方法
 */
ManualGoodsBlcok.prototype.draw = function (blockData) {
	this.blockData = blockData;
	var blockEl = this.blockEl;

	var goods = blockData.goods;
	var goodsEl = blockEl.find(".goods");
	this.drawGoods(goodsEl, goods);
}


/**
 * 绘制"手动商品"的方法
 */
ManualGoodsBlcok.prototype.drawGoods = function (goodsEl, goods) {
	if (goods) {
		var img = goodsEl.find(".g-img img");
		img.attr("src", goods.thumbnail);
		goodsEl.find(".g-name").text(goods.goods_name);
		goodsEl.find(".g-price span").text(goods.goods_price);
		goodsEl.show();
	}
}

ManualGoodsBlcok.prototype.onDataRefresh = function (event, goods) {

	var blockEl = $(this);
	var block = blockEl.data("obj");
	var blockData = block.blockData;

	if (blockData) {
		blockData.goods_id = goods.goods_id; //更新goodsid
	} else {
		blockData = {
			block_type: "MANUAL_GOODS",
			block_id: blockEl.attr("block_id"),
			goods_id: goods.goods_id
		}
	}
	//刷新数据
	block.blockData = blockData;

}

/**
 * 打开编辑器的方法
 */
ManualGoodsBlcok.prototype.openEditor = function () {
	var self = this;
	var goodsEl = this.blockEl.find(".goods");

	$.GoodsAdminSelector({
		maxLength: 1,
		confirm: function (goods) {
			if (goods) {
				//重新商品
				self.drawGoods(goodsEl, goods);
				//触发数据刷新事肵
				self.blockEl.trigger("dataRefresh", [goods]);
			}
		},
	});
}



/**
 * 单图片区块
 */
var SignleImageBlcok = function (blockEl) {

	this.blockEl = blockEl;
	this.blockEl.data("obj", this);
	var handle = new Handle(this.blockEl);
	handle.draw();
	//定义数据刷新事件
	this.blockEl.on("dataRefresh", this.onDataRefresh)


}

/**
 * 区块的绘制方法
 */
SignleImageBlcok.prototype.draw = function (blockData) {
	this.blockData = blockData;
	var blockEl = this.blockEl;

	var image = blockData.image;
	var blockEl = blockEl.find(".single_image");
	this.drawImage(blockEl, image);
}

/**
 * 区块的绘制方法
 */
SignleImageBlcok.prototype.drawImage = function (blockEl, imageData) {
	// 这里..
	var blockEl = this.blockEl;
	var image = imageData;
	if (image) {
		var img = blockEl.find("img");
		img.attr("src", imageData.image_url);
		blockEl.show();
	}
}

/**
 * 区块的编辑器(用户自己上传图片)
 */
SignleImageBlcok.prototype.openEditor = function () {
	var self = this;
	var imageEl = this.blockEl.find(".single_image");
	var defaultimage;
	if (self.blockData) {
		defaultimage = self.blockData.image;
	}
	if (!defaultimage) {
		defaultimage = {};
		defaultimage.image_url = "";
		defaultimage.op_type = "";
		defaultimage.op_value = "";
	}
	$.SingleImageSelector({
		confirm: function (image) {
			//重新绘制图片
			self.drawImage(imageEl, image);

			//触发数据刷新事肵
			self.blockEl.trigger("dataRefresh", [image]);
		},
		defaultvalue: [
			{
				img_url: defaultimage.image_url,
				customParams: [
					{ name: 'op_type', value: defaultimage.op_type },
					{ name: 'op_value', value: defaultimage.op_value },
				]
			}
		]
	});

}

/**
 * 区块的刷新
 */
SignleImageBlcok.prototype.onDataRefresh = function (event, image) {

	var blockEl = $(this);
	var block = blockEl.data("obj");
	var blockData = block.blockData;

	if (blockData) {
		blockData.image = image;
	} else {
		blockData = {
			block_type: "SINGLE_IMAGE",
			block_id: blockEl.attr("block_id"),
			image: image
		}
	}
	//刷新数据
	block.blockData = blockData;

}

/**
 * 品牌区块
 */
var BrandBlcok = function (blockEl) {

	this.blockEl = blockEl;
	this.blockEl.data("obj", this);
	var handle = new Handle(this.blockEl);
	handle.draw();
	//定义数据刷新事件
	this.blockEl.on("dataRefresh", this.onDataRefresh)


}

/**
 * 品牌区块的刷新
 */
BrandBlcok.prototype.onDataRefresh = function (event, brandList) {


	var blockEl = $(this);
	var block = blockEl.data("obj");
	var blockData = block.blockData;

	if (blockData) {
		blockData.brandList = brandList;
	} else {
		blockData = {
			block_type: "BRAND",
			block_id: blockEl.attr("block_id"),
			brandList: brandList
		}
	}
	//刷新数据
	block.blockData = blockData;

}


// 把参数中的logo属性转换成brand_image属性.统一一下.
// 因为openEditor,draw都调用了drawBrand方法,但是两者传进去的参数里的属性分别是logo和brand_iamge
function convert(brandList) {
	var newBrandList = [];
	for (var i in brandList) {
		var brand = {
			brand_id: brandList[i].brand_id,
			brand_image: brandList[i].logo
		}
		newBrandList.push(brand);

	}

	return newBrandList;
}

/**
 * 品牌区块的编辑器
 */
BrandBlcok.prototype.openEditor = function () {
	var self = this;

	brandSelector = $.BrandSelector({
		//  最大可选个数，如果为1，则为单选模式。
		maxLength: 7,
		//  确认回调，如果为单选模式 返回单个object，否则为数组
		confirm: function (brandList) {
			// 陈小博: 点击品牌保存按钮后,会调用到这里.data里包含了选择的品牌的object数据.
			//重新绘制图片

			var newlist = convert(brandList);

			self.drawBrand(newlist);

			//触发数据刷新事肵
			self.blockEl.trigger("dataRefresh", [newlist]);
		}
	});


}

/**
 * 品牌区块的绘画
 */
BrandBlcok.prototype.drawBrand = function (brandList) {
	var blockEl = this.blockEl;

	if (brandList) {
		// 首先把存在的品牌清除
		blockEl.find("li").each(function (index) {
			// 第一个是隐藏的模板,不清除
			if (index !== 0) {
				$(this).remove();
			}
		})
		// 然后再遍历,进行数据填充.
		for (var i = 0; i < brandList.length; i++) {
			var _li = blockEl.find("li:first").clone();
			_li.find("img").attr("src", brandList[i].brand_image);
			_li.show();
			blockEl.append(_li);
		}
		blockEl.show();
	}
}

/**
 * 品牌区块的绘画
 */
BrandBlcok.prototype.draw = function (blockData) {
	this.blockData = blockData;
	var blockEl = this.blockEl;

	var brandList = blockData.brandList;
	this.drawBrand(brandList);
}


/**
 * 多图片区块
 */
var MultiImageBlcok = function (blockEl) {

	this.blockEl = blockEl;
	this.blockEl.data("obj", this);
	var handle = new Handle(this.blockEl);
	handle.draw();
	//定义数据刷新事件
	this.blockEl.on("dataRefresh", this.onDataRefresh)


}

/**
 * 多图片区块的刷新
 */
MultiImageBlcok.prototype.onDataRefresh = function (event, image) {

	var blockEl = $(this);
	var block = blockEl.data("obj");
	var blockData = block.blockData;

	if (blockData) {
		blockData.image = image;
	} else {
		blockData = {
			block_type: "MULTI_IMAGE",
			block_id: blockEl.attr("block_id"),
			image: image
		}
	}
	//刷新数据
	block.blockData = blockData;

}

/**
 * 多图片区块的编辑器
 */
MultiImageBlcok.prototype.openEditor = function () {
	var self = this;
	$.GoodsSelector({
		confirm: function (goods) {

			var image = [{
				image_url: "http://static.b2b2cv2.javamall.com.cn/attachment/brand/201506021637048618.jpg",
				op_type: "link",
				op_value: "http://www.javamall.com.cn/abc.html"
			},
			{
				image_url: "http://static.b2b2cv2.javamall.com.cn/attachment//store/15/goods/2016/5/26/15//41475582_thumbnail.jpg",
				op_type: "link",
				op_value: "http://www.javamall.com.cn/abc.html"
			}

			];

			//重新绘制图片
			self.drawImage(image);

			//触发数据刷新事肵
			self.blockEl.trigger("dataRefresh", [image]);

		}
	});
}

/**
 * 多图片区块的绘画
 */
MultiImageBlcok.prototype.drawImage = function (imageData) {
	var blockEl = this.blockEl;
	var image = imageData;
	if (image) {
		var img = blockEl.find("img");
		img.attr("src", imageData[0].image_url);
		blockEl.show();
	}
}

/**
 * 多图片区块的绘画
 */
MultiImageBlcok.prototype.draw = function (blockData) {
	this.blockData = blockData;
	var blockEl = this.blockEl;

	var image = blockData.image;
	this.drawImage(image);
}



/**
 * 可编辑"把手"对象
 */
var Handle = function (targetEl) {

	var handelHtml = "";
	handelHtml += '<div class="handle">';
	handelHtml += '<span><i class="fa fa-paint-brush"></i>编辑</span>';
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
	}).click(function () {
		block.openEditor();
	});
}