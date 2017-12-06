/**
 * 楼层设计器
 * @author:kingapex
 * @date:2017-06-18
 */
var FloorDesign = function(el, options) {
    //获取楼层的数据源
    this.floorData = this.getDataSource(options.floorid);
    this.designEl = $('.floor-design>.floor');

    this.designEl.data('obj', this);
    this.designEl.on('dataRefresh', this.onDataRefresh);

    // var floorTitle = new FloorTitle();
    // floorTitle.draw(this.floorData);

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
FloorDesign.prototype.getDataSource = function(floorid) {
    var floor;
    $.ajax({
        url: ctx + '/cms/admin/floor/pc/' + floorid + '/design.do',
        dataType: 'json',
        async: false,
        success: function(json) {
            floor = json;
        }
    });
    // 遍历检测到品牌面板,然后替换其中的brand属性为brandList.
    // 因为后续操作用的是这个属性,而且设计到后端,所以必须是它.
    if (floor && floor.panelList) {
        floor.panelList.forEach(function(ele) {
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
FloorDesign.prototype.onDataRefresh = function() {
    var $this = $(this);
    var floor = $this.data('obj');
    var floorData = floor.floorData;
    delete floorData.panelList;
    floorData.panelList = [];
    $this.find('.panel').each(function(i, ele) {
        var panelData = $(ele).data('obj').panelData;
        floorData.panelList.push(panelData);
    });
};

/**
 * 添加一个面板
 */
FloorDesign.prototype.addPanel = function(tpl_id) {
    var self = this;
    var html;
    $.ajax({
        url: ctx + '/cms/admin/panel-tpl/content/' + tpl_id + '.do',
        async: false,
        success: function(data) {
            html = data;
        },
        error: function(e) {
            $.error(e.responseJSON.error_message);
        }
    });
    var panel = self.savePanel(tpl_id);
    var panelObj = new Panel(self.designEl, html);
    panelObj.panelEl.attr('tpl_id', tpl_id);
    //绑定对象
    panel.tpl_content = html;
    panel.tpl_type = 'normal';

    var panelO = panelObj.panelEl.data('obj');
    panelO.draw(panel);

    panelObj.panelEl.trigger('dataRefresh');
};

/**
 * 保存面板panel
 */
FloorDesign.prototype.savePanel = function(tpl_id) {
    var self = this;
    var data = {};
    var floor = self.floorData;
    data.floor_id = floor.id;
    data.panel_tpl_id = tpl_id;
    var panel = floor.panelList[floor.panelList.length - 1];
    //	data.panel_id = panel.panel_id;
    data.sort = panel.sort;
    data.panel_tpl_id = tpl_id;
    var layoutList = JSON.stringify([]);
    data.panel_data = layoutList;
    var result;
    $.ajax({
        url: ctx + '/cms/admin/floor/panel.do',
        method: 'post',
        async: false,
        data: data,
        dataType: 'json',
        success: function(res) {
            $.success('保存面板信息成功!');
            result = res;
        },
        error: function(e) {
            $.error(e.responseJSON.error_message);
        }
    });

    return result;
};

/**
 * 保存设计
 */
FloorDesign.prototype.save = function() {
    var jsonData = this.floorData;

    var postData = jQuery.extend(true, {}, jsonData);

    var panelList = postData.panelList;
    for (var i in panelList) {
        var panel = panelList[i];
        var layoutList = JSON.stringify(panel.layoutList);
        panel.panel_data = layoutList;
        delete panel.layoutList;
    }

    $.ajax({
        url: ctx + '/cms/admin/floor/design.do',
        method: 'POST',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(postData),
        success: function(json) {
            $.success('保存楼层设计信息成功!');
        },
        error: function(e) {
            $.error(e.responseJSON.error_message);
        }
    });
};

/**
 * 楼层标题
 */
var FloorTitle = function() {
    var titleEl = $('.floor-design>.header');
    this.titleEl = titleEl;
    titleEl.data('obj', this);
    var handle = new Handle(titleEl);
    handle.draw();
};

/**
 * 楼层标题加载
 */
FloorTitle.prototype.draw = function(floorData) {
    this.floorData = floorData;
    this.titleEl.find('.title>span').text(floorData.name);
};

/**
 * 楼层编辑器
 */
// 设计页里的标题 已经废弃
FloorTitle.prototype.openEditor = function() {
    var self = this;
    var text = self.titleEl.find('.title>span').text();
    var _dlgEl;
    layer.open({
        content: ctx + '/cms/admin/floor/title-selector-page.do',
        type: 2,
        area: ['500px', '500px'],
        success: function(dlgEl) {
            dlgEl.find('input[name=title]').val(text);
            _dlgEl = dlgEl;
        },
        buttons: [
            {
                text: '保存',
                event: function(dialog) {
                    var new_title = _dlgEl.find('input[name=title]').val();
                    var data = self.floorData;
                    delete data.panelList;
                    data.name = new_title;
                    $.ajax({
                        url: ctx + '/cms/admin/floor/' + data.id + '.do',
                        method: 'POST',
                        data: data,
                        success: function(json) {
                            $.success('楼层信息保存成功!');
                        },
                        error: function() {
                            $.error('楼层信息保存失败!');
                        }
                    });
                    dialog.close();
                    self.draw(data);
                }
            }
        ]
    });
};

/**
 * 面板对象
 */
var Panel = function(designEl, panelHtml) {
    this.panelHtml = panelHtml;

    var panel_el = $(this.panelHtml);
    panel_el.appendTo(designEl);
    this.panelEl = panel_el;

    //	var panel_id = panel_el.attr("panel_id");
    var sort = $('.panel').size();
    //设置panel id
    //	if(!panel_id) {
    //		panel_id = "panel_"+sort;
    //	}

    //	panel_el.attr("panel_id",panel_id);

    //绑定对象
    panel_el.data('obj', this);

    //定义数据刷新事件
    panel_el.on('dataRefresh', this.onDataRefresh);

    //对所有的布局绑定对象
    panel_el.find('.layout').each(function(i, ele) {
        var layoutEl = $(ele);
        var layout = new Layout(layoutEl);
        layoutEl.data('obj', layout);
    });
    this.drawHandle();
};

/**
 * 绘制“面板把手”
 */
Panel.prototype.drawHandle = function() {
    var self = this;
    var handleHtml = '';
    handleHtml += '<div class="panel-handle">';
    handleHtml +=
        '<span class="up"><a href="javascript:;" ><i class="layui-icon" style="font-size: 25px; color: #324057;">&#xe619;</i></a></span>';
    handleHtml +=
        '<span  class="down"><a href="javascript:; "><i class="layui-icon" style="font-size: 25px; color: #324057;">&#xe61a;</i></a></span>';
    handleHtml +=
        '<span  class="edit"><a href="javascript:;" ><i class="layui-icon" style="font-size: 25px; color: #324057;">&#xe620;</i></a></span>';
    handleHtml +=
        '<span  class="del"><a href="javascript:;" ><i class="layui-icon" style="font-size: 25px; color: #324057;">&#xe640;</i></a></span>';
    handleHtml += '</div>';

    this.panelEl.append($(handleHtml));

    //删除面板
    // 添加off() 的理由同下(edit)
    $('.del')
        .off()
        .click(function() {
            var _this = $(this);
            $.confirm('确认删除此板块吗？', {
                ok: function() {
                    var panelEl = _this.parents('.panel');
                    var panel = panelEl.data('obj');
                    var tpl_type = panel.panelData.tpl_type;
                    //主面板不允许删除
                    if (tpl_type != 'main') {
                        $.ajax({
                            url:
                                ctx +
                                '/cms/admin/floor/panel/' +
                                panel.panelData.id +
                                '.do',
                            method: 'DELETE',
                            success: function(json) {
                                panelEl.remove();
                                $.success('删除成功!');
                            },
                            error: function(e) {
                                $.error(e.responseJSON.error_message);
                            }
                        });
                    } else {
                        $.error('主板块不能删除!');
                    }
                },
                cancel: function() {
                    $.warn('取消删除!');
                }
            });
        });

    // 修改面板标题

    // 2017-8-4-陈小博: 如果不加上.off()的话,会出现触发多次click.
    // 应该是click累加绑定的问题. 具体哪部分出现的问题,我没有去仔细看.
    // 直接加上off().移除掉前面所有绑定的事件.再重新绑定一次!
    // 上面的delete和以后会添加的 上/下移 都要加上off()
    $('.edit')
        .off()
        .click(function(event) {
            var _this = $(this);
            var panelEl = _this.parents('.panel');
            var panel = panelEl.data('obj');
            var data = panel.panelData;
            var panel_name = panelEl.children('.panelhandle').text();
            var _dlgEl;
            layer.open({
                title: '修改标题', //标题
                maxmin: true, //右上角可否放大缩小
                type: 2,
                content:
                    ctx +
                    '/cms/admin/floor/panel-name-page.do?id=' +
                    data.id +
                    '&name=' +
                    panel_name,
                area: ['500px', '500px'],
                scrollbar: false //是否允许浏览器出现滚动,
            });
        });

    $('.up')
        .off()
        .click(function(event) {
            var _this = $(this);
            var panelEl = _this.parents('.panel');
            var panel = panelEl.data('obj');
            var data = panel.panelData;
            $.ajax({
                url:
                    ctx +
                    '/cms/admin/floor/panel/' +
                    panel.panelData.id +
                    '/sort.do',
                method: 'POST',
                data: { sort: 'up' },
                success: function(json) {
                    var before = _this.closest('.panel').prev();
                    if (before.length === 1) {
                        _this.closest('.panel').insertBefore(before);
                    }
                }
            });
        });

    $('.down')
        .off()
        .click(function(event) {
            var _this = $(this);
            var panelEl = _this.parents('.panel');
            var panel = panelEl.data('obj');
            var data = panel.panelData;
            $.ajax({
                url:
                    ctx +
                    '/cms/admin/floor/panel/' +
                    panel.panelData.id +
                    '/sort.do',
                method: 'POST',
                data: { sort: 'down' },
                success: function(json) {
                    var after = _this.closest('.panel').next();
                    if (after.length === 1) {
                        _this.closest('.panel').insertAfter(after);
                    }
                }
            });
        });
};

/**
 * 绘制面板方法
 */
Panel.prototype.draw = function(panelData) {
    //板绑定数据,并绘制
    this.panelData = panelData;

    // 绑定panel id属性
    this.panelEl.attr('data-id', panelData.id);

    if (this.panelData) {
        var layout_list = panelData.layoutList;
        for (var i in layout_list) {
            var layoutData = layout_list[i];
            var layoutEl = this.panelEl.find(
                '.layout[layout_id=' + layoutData.layout_id + ']'
            );
            //找到对象并绘制
            var layout = layoutEl.data('obj');
            // 2017-8-2-陈小博: 加上是否存在的判断条件.
            // 因为:如果用户改变了模板的结构,这时候点开楼层设计的时候,从服务器传来的数据是基于上一个模板的.
            // 所以,在初始化(draw)的时候,就会"对不上".所以这里加个判断.对不上的话,就跳过.
            if (layout) {
                layout.draw(layoutData);
            }
        }
        this.panelEl.prepend(
            "<div class='panelhandle'>" + panelData.panel_name + '</div>'
        );
    }
};

Panel.prototype.onDataRefresh = function() {
    var $this = $(this);
    var panelObj = $this.data('obj');
    var panelData = panelObj.panelData;
    //重新建立panel data
    if (panelData) {
        delete panelData.layoutList;
    } else {
        panelData = {
            //				panel_id:$this.attr("panel_id"),
            sort: 0,
            panel_tpl_id: $this.attr('tpl_id')
        };
    }

    panelData.layoutList = [];

    panelObj.panelEl.find('.layout').each(function(i, ele) {
        var layoutData = $(ele).data('obj').layoutData;
        if (layoutData) {
            panelData.layoutList.push(layoutData);
        }
    });
    panelObj.panelData = panelData;
};

/**
 * 布局对象
 */
var Layout = function(layoutEl) {
    this.layoutEl = layoutEl;

    //定义数据刷新事件
    this.layoutEl.on('dataRefresh', this.onDataRefresh);

    //为所有的区块绑定对象
    this.layoutEl.find('.block').each(function(i, ele) {
        var blockEl = $(ele);
        var block = new Block(blockEl);
    });
};

/**
 * 布局的绘制方法
 */
Layout.prototype.draw = function(layoutData) {
    var layoutEl = this.layoutEl;
    this.layoutData = layoutData;
    //绘制区块
    var block_list = layoutData.block_list;
    if (block_list) {
        for (var i in block_list) {
            var blockData = block_list[i];
            var blockEl = layoutEl.find(
                '.block[block_id=' + blockData.block_id + ']'
            );

            if (blockEl.size() > 0) {
                //找到对象并绘制
                var block = blockEl.data('obj');
                if (block) {
                    block.draw(blockData);
                }
            }
        }
    }
};

/**
 * 布局的数据刷新方法
 */
Layout.prototype.onDataRefresh = function() {
    var $this = $(this);
    var layoutObj = $this.data('obj');
    var layoutData = layoutObj.layoutData;

    //重新建立layout data
    if (layoutData) {
        delete layoutData.block_list;
    } else {
        layoutData = {
            layout_id: $this.attr('layout_id')
        };
    }

    layoutData.block_list = [];

    //使block data和layout建立关联
    $this.find('.block').each(function(i, ele) {
        var blockData = $(ele).data('obj').blockData;
        layoutData.block_list.push(blockData);
    });
    layoutObj.layoutData = layoutData;
};

/**
 * 区块对象
 */
var Block = function(blockEl) {
    var block_type = blockEl.attr('block_type');

    if (block_type == 'MANUAL_GOODS') {
        return new ManualGoodsBlcok(blockEl);
    }

    if (block_type == 'SINGLE_IMAGE') {
        return new SignleImageBlcok(blockEl);
    }

    if (block_type == 'BRAND') {
        return new BrandBlcok(blockEl);
    }

    if (block_type == 'MULTI_IMAGE') {
        return new MultiImageBlcok(blockEl);
    }

    if (block_type == 'TEXT') {
        return new TextBlcok(blockEl);
    }

    if (block_type == 'AUTO_GOODS') {
        return new AutoGoodsBlcok(blockEl);
    }
};

/**
 * 自动规则商品区块
 */
var AutoGoodsBlcok = function(blockEl) {
    this.ruleOrder = {
        buy_count: '销量',
        price: '价格',
        create_time: '上新'
    };
    this.ruleSort = {
        asc: '升序',
        desc: '降序'
    };

    this.blockEl = blockEl;
    this.blockEl.data('obj', this);
    var handle = new Handle(this.blockEl);
    handle.draw();
    //定义数据刷新事件
    this.blockEl.on('dataRefresh', this.onDataRefresh);
};

/**
 * 自动规则商品区块的绘制方法
 */
AutoGoodsBlcok.prototype.draw = function(blockData) {
    this.blockData = blockData;
    var blockEl = this.blockEl;

    var rule = blockData.rule;
    this.drawAutoGoods(blockEl, rule);
};

/**
 * 自动规则商品区块的绘制方法
 */
AutoGoodsBlcok.prototype.drawAutoGoods = function(blockEl, rule) {
    var self = this;
    if (rule) {
        blockEl.find('span[name=nc-category]').text(rule.category_name);
        blockEl
            .find('span[name=nc-rule]')
            .text(self.ruleOrder[rule.order] + ' ' + this.ruleSort[rule.sort]);
        blockEl.find('span[name=nc-number]').text(rule.number);
        blockEl.find('dl').attr('data-order', rule.order);
        blockEl.find('dl').attr('data-sort', rule.sort);
        blockEl
            .find('dl')
            .find('dd:first')
            .attr('data-id1', rule.category_id1);
        blockEl
            .find('dl')
            .find('dd:first')
            .attr('data-id2', rule.category_id2);
        blockEl
            .find('dl')
            .find('dd:first')
            .attr('data-id3', rule.category_id3);
        blockEl.show();
    }
};

/**
 * 自动规则商品的数据刷新方法
 */
AutoGoodsBlcok.prototype.onDataRefresh = function(event, rule) {
    var blockEl = $(this);
    var block = blockEl.data('obj');
    var blockData = block.blockData;

    if (blockData) {
        blockData.rule = rule;
    } else {
        blockData = {
            block_type: 'AUTO_GOODS',
            block_id: blockEl.attr('block_id'),
            rule: rule
        };
    }
    //刷新数据
    block.blockData = blockData;
};

/**
 * 自动规则商品的编辑器
 */
AutoGoodsBlcok.prototype.openEditor = function() {
    var self = this;
    var obj = this.blockEl.data('obj');
    var blockEl = this.blockEl.find('dl');
    $.AutogoodsSelector({
        defaultData: {
            category_id1: blockEl.find('dd:first').attr('data-id1'),
            category_id2: blockEl.find('dd:first').attr('data-id2'),
            category_id3: blockEl.find('dd:first').attr('data-id3'),
            order: blockEl.attr('data-order'),
            number: blockEl.find('span[name=nc-number]').text(),
            sort: blockEl.attr('data-sort')
        },

        confirm: function(autoResult) {
            var arrName = autoResult.category_name.split('-'); //将字符串转化为数组
            var rule = {
                category_id1: autoResult.category_id1,
                category_id2: autoResult.category_id2,
                category_id3: autoResult.category_id3,
                category_name: arrName[0] + '-' + arrName[1] + '-' + arrName[2],
                order: autoResult.order,
                sort: autoResult.sort,
                number: autoResult.number
            };
            //重新绘制自动规则商品
            self.drawAutoGoods(autoGoodsEl, rule);
            //触发数据刷新事肵
            self.blockEl.trigger('dataRefresh', [rule]);
        }
    });
    var autoGoodsEl = this.blockEl;
};

/**
 * 文本区块
 */
var TextBlcok = function(blockEl) {
    this.blockEl = blockEl;
    this.blockEl.data('obj', this);
    var handle = new Handle(this.blockEl);
    handle.draw();
    //定义数据刷新事件
    this.blockEl.on('dataRefresh', this.onDataRefresh);
};

/**
 * 文本区块的绘制方法
 */
TextBlcok.prototype.draw = function(blockData) {
    this.blockData = blockData;
    var blockEl = this.blockEl;

    var text = blockData.text;
    var textEl = blockEl.find('.text');
    this.drawText(textEl, text);
};

/**
 * 文本区块的绘制方法
 */
TextBlcok.prototype.drawText = function(textEl, text) {
    if (text) {
        textEl.find('a').text(text.title_text);
        textEl.find('a').attr('href', text.value);
        textEl.find('a').attr('data-type', text.text_type);
        textEl.show();
    }
};

/**
 * 文本区块的数据刷新方法
 */
TextBlcok.prototype.onDataRefresh = function(event, text) {
    var blockEl = $(this);
    var block = blockEl.data('obj');
    var blockData = block.blockData;

    if (blockData) {
        blockData.text = text;
    } else {
        blockData = {
            block_type: 'TEXT',
            block_id: blockEl.attr('block_id'),
            text: text
        };
    }
    //刷新数据
    block.blockData = blockData;
};

/**
 * 文本区块的编辑器
 */
TextBlcok.prototype.openEditor = function() {
    var obj = this.blockEl.data('obj');
    var textEl = this.blockEl.find('.text');
    var self = this;
    $.TextSelector({
        defaultData: {
            content: textEl.find('a').text(),
            op_type: textEl.find('a').attr('data-type'),
            op_value: textEl.find('a').attr('href')
        },
        confirm: function(textResult) {
            var text = {
                title_text: textResult.content,
                text_type: textResult.op_type,
                value: textResult.op_value
            };
            //重新绘制文本
            self.drawText(textEl, text);
            //触发数据刷新事肵
            self.blockEl.trigger('dataRefresh', [text]);
        }
    });
};

/**
 * 手动规则商品区块
 */
var ManualGoodsBlcok = function(blockEl) {
    this.blockEl = blockEl;
    this.blockEl.data('obj', this);
    var handle = new Handle(this.blockEl);
    handle.draw();

    var goods = {
        thumbnail: '../../images/empty-goods.png',
        goods_name: '暂无商品',
        price: '0.0'
    };
    this.drawGoods(blockEl, goods);

    //定义数据刷新事件
    this.blockEl.on('dataRefresh', this.onDataRefresh);
};

/**
 * 区块的绘制方法
 */
ManualGoodsBlcok.prototype.draw = function(blockData) {
    this.blockData = blockData;
    var blockEl = this.blockEl;

    var goods = blockData.goods;
    var goodsEl = blockEl.find('.goods');
    this.drawGoods(goodsEl, goods);
};

/**
 * 绘制"手动商品"的方法
 */
ManualGoodsBlcok.prototype.drawGoods = function(goodsEl, goods) {
    if (goods) {
        var img = goodsEl.find('.g-img img');
        goods.thumbnail = goods.thumbnail.replace('180x180', '400x400');
        img.attr('src', goods.thumbnail);
        goodsEl.find('.g-name').text(goods.goods_name);
        goodsEl.find('.g-price span').text(goods.price);
        goodsEl.show();
    }
};

ManualGoodsBlcok.prototype.onDataRefresh = function(event, goods) {
    var blockEl = $(this);
    var block = blockEl.data('obj');
    var blockData = block.blockData;

    if (blockData) {
        blockData.goods_id = goods.goods_id; //更新goodsid
    } else {
        blockData = {
            block_type: 'MANUAL_GOODS',
            block_id: blockEl.attr('block_id'),
            goods_id: goods.goods_id
        };
    }
    //刷新数据
    block.blockData = blockData;
};

/**
 * 打开编辑器的方法
 */
ManualGoodsBlcok.prototype.openEditor = function() {
    var self = this;
    var goodsEl = this.blockEl.find('.goods');
    $.GoodsAdminSelector({
        maxLength: 1,
        defaultData:
            self && self.blockData && self.blockData.goods_id
                ? [self.blockData.goods_id]
                : [],
        confirm: function(goods) {
            if (goods) {
                //重新商品
                self.drawGoods(goodsEl, goods);
                //触发数据刷新事肵
                self.blockEl.trigger('dataRefresh', [goods]);
            }
        }
    });
};

/**
 * 单图片区块
 */
var SignleImageBlcok = function(blockEl) {
    this.blockEl = blockEl;
    this.blockEl.data('obj', this);
    var handle = new Handle(this.blockEl);
    handle.draw();
    //定义数据刷新事件
    this.blockEl.on('dataRefresh', this.onDataRefresh);
};

/**
 * 区块的绘制方法
 */
SignleImageBlcok.prototype.draw = function(blockData) {
    this.blockData = blockData;
    var blockEl = this.blockEl;

    var image = blockData.image;
    var blockEl = blockEl.find('.single_image');
    this.drawImage(blockEl, image);
};

/**
 * 区块的绘制方法
 */
SignleImageBlcok.prototype.drawImage = function(blockEl, imageData) {
    // 这里..
    var blockEl = this.blockEl;
    var image = imageData;
    if (image) {
        var img = blockEl.find('img');
        img.attr('src', imageData.image_url);
        blockEl.show();
    }
};

/**
 * 区块的编辑器(上传图片) 单图上传
 */
SignleImageBlcok.prototype.openEditor = function() {
    var self = this;
    var imageEl = this.blockEl.find('.single_image');

    var defaultvalue = [];
    if (self.blockData && self.blockData.image) {
        var defaultimage = self.blockData.image;
        defaultvalue = [
            {
                img_url: defaultimage.image_url,
                customParams: [
                    { name: 'op_type', value: defaultimage.op_type },
                    { name: 'op_value', value: defaultimage.op_value }
                ]
            }
        ];
    }

    $.SingleImageSelector({
        confirm: function(image) {
            //重新绘制图片
            self.drawImage(imageEl, image);
            //触发数据刷新事肵
            self.blockEl.trigger('dataRefresh', [image]);
        },
        defaultvalue: defaultvalue
    });
};

/**
 * 区块的刷新
 */
SignleImageBlcok.prototype.onDataRefresh = function(event, image) {
    var blockEl = $(this);
    var block = blockEl.data('obj');
    var blockData = block.blockData;

    if (blockData) {
        blockData.image = image;
    } else {
        blockData = {
            block_type: 'SINGLE_IMAGE',
            block_id: blockEl.attr('block_id'),
            image: image
        };
    }
    //刷新数据
    block.blockData = blockData;
};

/**
 * 品牌区块
 */
var BrandBlcok = function(blockEl) {
    this.blockEl = blockEl;
    this.blockEl.data('obj', this);
    var handle = new Handle(this.blockEl);
    handle.draw();
    //定义数据刷新事件
    this.blockEl.on('dataRefresh', this.onDataRefresh);
};

/**
 * 品牌区块的刷新
 */
BrandBlcok.prototype.onDataRefresh = function(event, brandList) {
    var blockEl = $(this);
    var block = blockEl.data('obj');
    var blockData = block.blockData;

    if (blockData) {
        blockData.brandList = brandList;
    } else {
        blockData = {
            block_type: 'BRAND',
            block_id: blockEl.attr('block_id'),
            brandList: brandList
        };
    }
    //刷新数据
    block.blockData = blockData;
};

// 把参数中的logo属性转换成brand_image属性.统一一下.
// 因为openEditor,draw都调用了drawBrand方法,但是两者传进去的参数里的属性分别是logo和brand_iamge
function convert(brandList) {
    var newBrandList = [];
    for (var i in brandList) {
        var brand = {
            brand_id: brandList[i].brand_id,
            brand_image: brandList[i].logo
        };
        newBrandList.push(brand);
    }

    return newBrandList;
}

/**
 * 品牌区块的编辑器
 */
BrandBlcok.prototype.openEditor = function() {
    var self = this;
    var defaultData = [];
    if (self && self.blockData && self.blockData.brandList) {
        defaultData = self.blockData.brandList.map(function(brand) {
            return brand.brand_id;
        });
    }

    brandSelector = $.BrandSelector({
        defaultData: defaultData,
        //  最大可选个数，如果为1，则为单选模式。
        maxLength: 10,
        //  确认回调，如果为单选模式 返回单个object，否则为数组
        confirm: function(brandList) {
            // 陈小博: 点击品牌保存按钮后,会调用到这里.data里包含了选择的品牌的object数据.
            //重新绘制图片
            var newlist = convert(brandList);
            self.drawBrand(newlist);
            //触发数据刷新事肵
            self.blockEl.trigger('dataRefresh', [newlist]);
        }
    });
};

/**
 * 品牌区块的绘画
 */
BrandBlcok.prototype.drawBrand = function(brandList) {
    var blockEl = this.blockEl;

    if (brandList) {
        // 首先把存在的品牌清除
        blockEl.find('li').each(function(index) {
            // 第一个是隐藏的模板,不清除
            if (index !== 0) {
                $(this).remove();
            }
        });
        // 然后再遍历,进行数据填充.
        for (var i = 0; i < brandList.length; i++) {
            var _li = blockEl.find('li:first').clone();
            if (!/140x70/.test(brandList[i].brand_image)) {
                brandList[i].brand_image = brandList[i].brand_image + '_140x70';
            }
            _li
                .find('img')
                .attr('src', brandList[i].brand_image)
                .attr('data-id', brandList[i].brand_id);
            _li.show();
            blockEl.append(_li);
        }
        blockEl.show();
    }
};

/**
 * 品牌区块的绘画
 */
BrandBlcok.prototype.draw = function(blockData) {
    this.blockData = blockData;
    var blockEl = this.blockEl;

    var brandList = blockData.brandList;
    this.drawBrand(brandList);
};

/**
 * 多图片区块
 */
var MultiImageBlcok = function(blockEl) {
    this.blockEl = blockEl;
    this.blockEl.data('obj', this);
    var handle = new Handle(this.blockEl);
    handle.draw();
    //定义数据刷新事件
    this.blockEl.on('dataRefresh', this.onDataRefresh);
};

/**
 * 多图片区块的刷新
 */
MultiImageBlcok.prototype.onDataRefresh = function(event, data) {
    var blockEl = $(this);
    var block = blockEl.data('obj');
    var blockData = block.blockData;

    if (blockData) {
        blockData.image = data.imageArray;
    } else {
        blockData = {
            block_type: 'MULTI_IMAGE',
            block_id: blockEl.attr('block_id'),
            image: data.imageArray
        };
    }
    //刷新数据
    block.blockData = blockData;
};

/**
 * 多图片区块的编辑器
 */
MultiImageBlcok.prototype.openEditor = function() {
    var self = this;
    $.MultipleImageSelector({
        fileNumLimit: 3,
        confirm: function(goods) {
            //重新绘制图片
            self.drawImage(goods);

            //触发数据刷新事肵
            self.blockEl.trigger('dataRefresh', {imageArray: goods});
        }
    });
};

/**
 * 多图片区块的绘画
 */
MultiImageBlcok.prototype.drawImage = function(images) {
    var blockEl = this.blockEl;
    if (!images || images.length === 0) {
        return;
    }
    images.forEach(function(image,index) {
        var img = $(blockEl.find('img')[index]);
        img.attr('src', image.image_url);

        if(index + 1 === images.length){
            // 最后一个的话，不添加li
            return;
        }
        var liCloned = $(blockEl.find('li')[index]).clone(true);
        liCloned.find('img').attr('src','');
        blockEl.find('ul').append(liCloned);
    });
    blockEl.show();
};

/**
 * 多图片区块的绘画
 */
MultiImageBlcok.prototype.draw = function(blockData) {
    this.blockData = blockData;
    var blockEl = this.blockEl;

    var image = blockData.image;
    this.drawImage(image);
};

/**
 * 可编辑"把手"对象
 */
var Handle = function(targetEl) {
    var handelHtml = '';
    handelHtml += '<div class="handle">';
    handelHtml += '<span><i class="fa fa-paint-brush"></i>编辑</span>';
    handelHtml += '<div class="handle-body"></div>';
    handelHtml += '</div>';

    this.handleHtml = handelHtml;
    this.targetEl = targetEl;
};

Handle.prototype.draw = function() {
    var self = this;
    var el = this.targetEl;
    var block = el.data('obj');
    var handle = $(this.handleHtml);
    el
        .append(handle)
        .children('.handle')
        .hover(
            function() {
                $(this)
                    .children()
                    .show();
            },
            function() {
                $(this)
                    .children()
                    .hide();
            }
        )
        .click(function() {
            block.openEditor();
        });
};
