/*
 * 商品规格插件
 * @author: 陈小博
 * @date: 2017-9-7
 */

// -----------------------------------------暴露出去的对象-----------------------------------------

var skuEditor = {};
// -----------------------------------------END-----------------------------------------
(function() {
    // CSS
    var skuCss =
        '<style>\
	.sku-container {\
		background: #f8f8f8;\
		padding: 15px\
	}\
\
	.sku-container .sku-tr {\
		width: 100%;\
		margin-top: 1.5rem;\
	}\
\
	.sku-container .sku-tr>div:first-child {\
		width: 8%;\
	}\
\
	.sku-container .sku-tr>div:first-child>span {\
		float: right;\
        margin-top: 0.3rem;\
        font-weight: 700;\
        color: #333;\
	}\
\
	.sku-container .sku-tr:last-child>div:first-child>span {\
		margin-top: .5rem\
	}\
\
	.sku-container .sku-tr .td-body {\
		border: 1px solid #e5e5e5;\
		background: #fff;\
		width: 80%\
	}\
\
	.sku-container .sku-tr .td-section {\
		padding: 20px;\
		box-sizing: border-box\
    }\
    .sku-container .sku-tr .td-section .sku-group-item{\
        max-width: 1049px;\
    }\
\
	.sku-container .sku-tr .td-section .sku-group-item .bg-grey {\
		position: relative\
    }\
    .sku-container .sku-tr .td-section .sku-group-item .bg-grey .checkbox-inline{\
        line-height: 18px;\
        margin-left: 30px;\
        position: relative;\
        top: -3px;\
    }\
\
	.sku-container .sku-tr .td-section .sku-group-item .bg-grey .delete-item {\
		border-radius: 50%;\
		background: #bfbfbf;\
		color: white;\
		display: inline-block;\
		width: 20px;\
		height: 20px;\
		line-height: 19px;\
		text-align: center;\
		position: absolute;\
		top: 10px;\
		right: 10px;\
		display: none\
	}\
\
	.sku-container .sku-tr .td-section .add-sku-value {\
		color: #38f;\
        margin: 10px;\
        width: 40px;\
		display: inline-block\
	}\
\
	.sku-container .sku-tr .td-section .sku-group-item .select-sku {\
		margin-top: 11px\
	}\
\
	.sku-container .sku-tr .td-section .bg-grey>div.select-sku {\
		width: 100px\
	}\
\
	.sku-container .sku-tr .td-section .sku-group-item .select-sku>div {\
		padding: 5px\
	}\
\
	div.td.td-body.td-section>div.sku-group-item>div.bg-grey>div>div.selectize-dropdown.single.select-sku {\
		margin-top: 0;\
		padding: 0;\
		z-index: 99\
	}\
\
	.sku-container .bg-grey {\
		height: 28px;\
		padding: 7px;\
		width: 100%;\
		line-height: 28px;\
		background: #f8f8f8;\
		display: flex;\
		align-items: center\
	}\
    .sku-container .add-sku-group-item button {\
		width: 98px;\
		height: 28px;\
		padding: 0;\
		border-radius: 3px;\
		border: 1px solid #e5e5e5;\
		background: #fafafa;\
		color: #777;\
		font-size: 13px;\
	}\
\
	.sku-container .sku-group-item button {\
		width: 100px;\
		height: 30px;\
		padding: 0;\
	}\
\
	.sku-container .sku-group-item .sku-item-body {\
		display: flex;\
        box-sizing: content-box;\
        flex-wrap: wrap;\
	}\
\
	.sku-container .sku-group-item .sku-item-body .spec-values {\
		display: flex;\
		align-items: center;\
		position: relative;\
	}\
\
	.sku-container .sku-table {\
		display: flex;\
		flex-direction: column;\
	}\
\
	.sku-container .sku-table .sku-tr {\
		display: flex;\
	}\
\
	.sku-container .sku-table .sku-tr:nth-child(3),\
	.sku-container .sku-table .sku-tr:nth-child(4) {\
		height: 50px;\
		min-height: unset;\
	}\
\
	.sku-container .sku-table .sku-tr .td-body {\
		display: flex;\
        flex-direction: column;\
        margin-left: 16px;\
	}\
\
	.sku-container .sku-group-item .sku-value-group-item {\
		top: 3rem;\
		position: absolute;\
		box-sizing: border-box;\
		width: 500px;\
		height: 54px;\
		padding: 0 20px;\
		background: #fff;\
		align-items: center;\
		justify-content: space-around;\
		margin: 1rem 0;\
		box-shadow: 0 1px 6px rgba(0, 0, 0, 0.2);\
		z-index: 50;\
	}\
\
	.sku-container .sku-group-item .sku-value-group-item .select-sku-value {\
		width: 80%;\
		width: calc(100% - 120px);\
	}\
\
	.sku-container .sku-group-item .sku-value-group-item button {\
		width: 50px;\
		margin: 2px;\
	}\
\
	.sku-container .sku-group-item .value-item {\
		width: 90px;\
		text-align: center;\
		position: relative;\
		margin: 7px;\
    }\
    .sku-container .sku-group-item .value-item .origin-value-item{\
        border-radius: 3px;\
        width: 80px;\
        padding: 4px;\
		height: 20px;\
		line-height: 20px;\
		border: 1px solid #ccc;\
    }\
\
	.sku-container .sku-group-item .value-item .delete {\
		position: absolute;\
		top: -10px;\
		right: -10px;\
		border: 1px solid #ddd;\
		border-radius: 50%;\
		width: 20px;\
		display: none;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity table {\
		width: 100%;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity {\
		padding: 10px 20px;\
		box-sizing: border-box;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity table>tbody>tr>td:nth-child(1) {\
		border-left-width: 0;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity table>tbody>tr>td:last-child {\
		border-right-width: 0;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity table thead th,\
	.sku-container .sku-table .sku-tr .td-quantity table tbody td {\
		text-align: center;\
		padding: 10px 6px;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity table thead .th-price,\
	.sku-container .sku-table .sku-tr .td-quantity table thead .th-cost,\
	.sku-container .sku-table .sku-tr .td-quantity table thead .th-weight,\
	.sku-container .sku-table .sku-tr .td-quantity table thead .th-sn,\
	.sku-container .sku-table .sku-tr .td-quantity table thead .th-quantity {\
        width: 100px;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity table tbody tr td {\
        border: 1px solid #e5e5e5;\
        position: relative;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity table tbody tr {\
		height: 47px;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity table tbody input {\
		height: 50%;\
		margin: auto;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity table tfoot tr {\
		border-bottom: 1px solid #e5e5e5;\
		height: 35px;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity table tfoot .foot {\
		display: flex;\
		align-items: center;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity table tfoot .foot>span {\
		color: black;\
		margin-right: .5rem;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity table tfoot .foot .set span,\
	.sku-container .sku-table .sku-tr .td-quantity table tfoot .foot .input span {\
		color: #38f;\
		margin: 0 .2rem;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity table tfoot .foot input {\
		width: 70px;\
		height: 20px;\
	}\
\
	.sku-container .sku-table .sku-tr .td-quantity table tfoot .input {\
		display: none;\
		align-items: center\
	}\
\
	.sku-container .sku-tr .td-seller-code input {\
		width: 102px;\
		height: 32px;\
		box-sizing: border-box\
	}\
\
	.sku-container .sku-table .sku-tr tbody .cost input,\
	.sku-container .sku-table .sku-tr tbody .quantity input,\
	.sku-container .sku-table .sku-tr tbody .weight input,\
	.sku-container .sku-table .sku-tr tbody .sn input {\
        width: 80px;\
	}\
\
	.sku-container .sku-table .sku-tr tbody .price input {\
		max-width: 100px;\
		float: left\
	}\
\
	.sku-container .sku-table .tr-quantity-div {\
		display: none;\
	}\
\
	#storeGoodsForm>div>div>div.store_add_goods.hide0>div>div:nth-child(3)>div>div.info-group-cont.vbox>div.sku-editor-contianer>div>div>div:nth-child(3)>div:nth-child(1)>span>em {\
		color: #f00;\
		font-size: 22px;\
	}\
\
	.sku-container .td .all-quantity {\
        height: 17px;\
        margin-left: 20px;\
	}\
\
	.sku-container .sku-tr .td-right {\
        margin-left: -4px;\
        position: relative;\
	}\
\
	.sku-container .sku-tr .td:first-child span {\
        padding-right: 5px;\
        word-break: keep-all;\
	}\
\
	.sku-container .note-info {\
		position: absolute;\
		top: -8px;\
        display: none;\
        width:100px;\
        left:0px;\
        color: #f00;\
        font-size: 12px;\
    }\
    .sku-container .td-right .note-info{\
        top: -12px;\
        left: 40px;\
    }\
    .sku-container .sku-table div.sku-tr.quantity-management-tip{\
        display: none;\
    }\
    .sku-container .quantity-management-tip div.td-right .alert{\
        margin-left: 20px;\
        letter-spacing: 1px;\
        font-size: 15px;\
    }\
    .sku-container .spec-value-image{\
        width: 90px;\
        height: 120px;\
        margin-top: 5px;\
        display: none;\
    }\
    .sku-container .spec-value-image img{\
        width: 90px;\
        height: 90px;\
        margin-bottom: 5px;\
    }\
</style>';
    // HTML
    var skuHTML =
        skuCss +
        "<div class='sku-container'>\
        <div class='sku-table'>\
            <div class='sku-tr'>\
                <div class='td td-name'>\
                    <span>商品规格:</span>\
                </div>\
                <div class='td td-body td-section'>\
                    <!-- sku规格项目 -->\
                    <div class='add-sku-group-item bg-grey'>\
                        <button type='button'>添加规格项目</button>\
                    </div>\
                </div>\
                <div class='sku-item-model' style='display: none;'>\
                    <!-- sku规格模型 -->\
                    <div class='sku-group-item'>\
                        <!-- 规格key所在 -->\
                        <div class='bg-grey'>\
                            <select class='select-sku' placeholder='请选择...'></select>\
                            <span class='delete-item pointer'>x</span>\
                            <label class='checkbox-inline'>\
                                <input type='checkbox' id='inlineCheckbox'>\
                                <span class='add-spec-image'>添加规格图片</span>\
                            </label>\
                        </div>\
                        <div class='sku-item-body'>\
                            <!-- 规格value所在 -->\
                            <div class='spec-values'>\
                                <span class='add-sku-value pointer'>+添加</span>\
                                <div class='sku-value-group-item' style='display: none;'>\
                                    <select class='select-sku-value' multiple placeholder='请选择...'></select>\
                                    <button class='confirm' type='button'>确认</button>\
                                    <button class='cancel' type='button'>取消</button>\
                                </div>\
                            </div>\
                        </div>\
                    </div>\
                    <!-- 规格值的模型 -->\
                    <div class='value-item pointer'>\
                        <div class='origin-value-item'>\
                            <span class='value'>红色</span>\
                            <span class='delete pointer'>X</span>\
                        </div>\
                        <div class='spec-value-image'>\
                            <img class='temp-preview-image'>\
                            <span class='upload-btn upload-btn1'>上传</span>\
                        </div>\
                    </div>\
                </div>\
            </div>\
            <div class='sku-tr tr-quantity-div'>\
                <div class='td'><span>商品库存:</span></div>\
                <div class='td td-body td-quantity'>\
                </div>\
            </div>\
            <div class='sku-tr'>\
                <div class='td'><span><em class='required'>*</em>总库存:</span></div>\
                <div class='td td-right'>\
                    <span class='note-info'>必填,有效的数字</span>\
                    <input class='form-control all-quantity' disabled>\
                </div>\
            </div>\
            <div class='sku-tr quantity-management-tip'>\
                <div class='td'><span></span></div>\
                <div class='td td-right'>\
                    <div class='alert alert-warning' style='width:100%;' role='alert'>编辑商品不能维护库存（在列表中有独立的库存管理）</div>\
                </div>\
            </div>\
        </div>\
    </div>";

    // ------------------------------------全局属性-------------------------------------
    var specObject = {
        // 存储用户选择的 某区块的 规格对象   {249:{颜色:{红色:1,绿色:2}}}
        // 251: {
        // 	颜色: {
        // 		红色: 1,
        // 		绿色: 2
        // 	}
        // },
        // 252: {
        // 	尺寸: {
        // 		S: 3,
        // 		L: 4,
        // 		M: 5
        // 	}
        // },
    };
    var category_id = undefined;
    var countNumber = 250; //唯一的值,每次使用之后递减. 后面会使用它来实现 项目组的 不同z-index和 id
    var specNameArray = []; // 规格名数据.对象数组 [{spec_id: 1,spec_name: '颜色'}]
    var specNameArrayComputed = []; // 根据已选规格名,筛选后的规格名
    var specNameValueObject = {}; // 根据规格名快速查询规格值数组. {'颜色': [{spec_value: '红色', id: 1}]}
    var tableHTML; // 存储表格的HTMl,供后面快速方便的生成skuList
    var skuDOM = $(skuHTML); // sku html 的 DOM
    var goodsID = undefined; // 商品编号
    var editMode = false; // 记录是否是编辑模式
    var draftMode = false; // 记录用户是否点击了‘保存草稿箱’
    var snapshot = []; // 表格数据的快照 用来用户每次更新规格值后，初始化用户上次输入

    // ------------------------------------公用方法-------------------------------------

    // 为生成的DOM绑定 selectize.js
    var bindSelectizeJS = function(which) {
        $(which.find('.select-sku')).selectize({
            valueField: 'spec_id',
            labelField: 'spec_name',
            options: specNameArrayComputed,
            create: true,
            sortField: 'text',
            onChange: function(id) {
                var selectize = $select[0].selectize;
                // 刷新规格项
                refreshOptions(selectize, id);
                // 重置DOM和数据
                resetSpec(which);
                insertSpecTable();

                // 显示隐藏表格容器div
                if (
                    skuDOM.find('.sku-table .tr-quantity-div tr').length === 0
                ) {
                    skuDOM
                        .find('.sku-table .tr-quantity-div')
                        .css('display', 'none');
                } else {
                    skuDOM
                        .find('.sku-table .tr-quantity-div')
                        .css('display', 'flex');
                }
            },
            onItemAdd: function(value, $item) {
                // 用户输入规格项，添加到数据库中

                var specName = $($item).text();
                // 当前规格项名称存在于 变量数据 吗？
                if (specNameIsExistInData(specName, specNameArray)) {
                    return;
                }
                $.post(
                    ctx +
                        '/goods-info/seller/category/' +
                        category_id +
                        '/spec.do',
                    {
                        cat_id: category_id,
                        spec_name: specName
                    },
                    function(res) {
                        $($item).attr('data-value', res.spec_id);
                        // 规格项添加到数组中
                        specNameArray.push(res);
                        // 增加 规格项值kv
                        specNameValueObject[res.spec_name] = [];
                    }
                );
            }
        });

        // 给规格值的选择调用JS
        var $select = $(which.find('.select-sku-value')).selectize({
            plugins: ['remove_button'],
            maxItems: null,
            valueField: 'spec_value_id',
            labelField: 'spec_value',
            create: true,
            onOptionAdd: function(value, data) {
                var specId = getSkuItemId($select);
                var specName = getSkuItemName($select);

                // 获取当前规格项已存在的所有规格值
                var specValueArray = specNameValueObject[specName];

                // 如果该规格值已经存在同名数据
                if (specValueIsExistInData(data.spec_value, specValueArray)) {
                    return;
                }

                $.post(
                    ctx +
                        '/goods-info/seller/spec/' +
                        specId +
                        '/spec-values.do',
                    {
                        spec_id: specId,
                        spec_value: data.spec_value
                    },
                    function(res) {
                        // 根据返回的规格值id动态更新页面中selectize默认的id
                        $select
                            .next()
                            .find('.item')
                            .attr('data-value', res.spec_value_id);

                        // 更新全局变量
                        var tempArray = specNameValueObject[specName];
                        if (!tempArray) {
                            // 之前数据中不存在value数组数据的话
                            tempArray = [res];
                        } else {
                            tempArray.push(res);
                        }
                    }
                );
            }
        });
    };

    // 根据规格项的变化,刷新规格值选择框中的项.
    var refreshOptions = function(selectize, id) {
        var theNameArray = specNameArray.filter(function(ele) {
            return ele.spec_id == id;
        });
        var name = theNameArray.length > 0 ? theNameArray[0].spec_name : '';
        // 不能给addOption传undefined，否则报错
        var options = specNameValueObject[name];
        selectize.clearOptions();
        selectize.addOption(options ? options : '');
    };

    // 筛选掉已选择的规格名, 形成新的规格名数组
    var computedSepcNameArray = function() {
        var selectedArray = [];
        skuDOM
            .find('select.select-sku option[selected=selected]')
            .each(function() {
                selectedArray.push(Number($(this).val()));
            });
        if (!selectedArray || selectedArray.length === 0) {
            return specNameArray;
        }
        return specNameArray.filter(function(tempObj) {
            return selectedArray.indexOf(tempObj.spec_id) < 0;
        });
    };

    // 移除现有的数据项DOM,更新数据源中的相关数据.
    var resetSpec = function(which) {
        $(which)
            .find('.sku-item-body .value-item')
            .remove();

        var itemID = $(which).data('item-id');
        specObject[itemID] = {};
    };

    // 生成并插入规格项目的值,到DOM流中
    // where 插入地点后面的元素, valuesObj 规格值对象,  specName 规格名称, topItemDOM 所在规格项的顶级DOM(.sku-group-item)
    var createAndInsertSkuValue = function(
        where,
        valuesObj,
        specName,
        topItemDOM
    ) {
        var clonedSpecValueItem;
        for (var prop in valuesObj) {
            if (prop && !specValueIsExist(where, specName, prop)) {
                clonedSpecValueItem = skuDOM
                    .find('.sku-item-model > .value-item')
                    .clone(true);
                clonedSpecValueItem.find('.value').text(prop);
                clonedSpecValueItem.insertBefore(where);

                // 如果当前规格项不是最上面的规格项,即上一个有sku-group-item
                // 则隐藏规格值DOM中的图片部分DOM

                if (topItemDOM.prev('.sku-group-item').length === 1) {
                    clonedSpecValueItem.find('div.spec-value-image').hide();
                } else if (topItemDOM.find('#inlineCheckbox').prop('checked')) {
                    // 是最上面的规格项的话，且用户选择了添加规格图片，则显示图片相关DOM
                    clonedSpecValueItem.find('div.spec-value-image').show();
                }

                // 给新生成的规格值DOM进行上传图片插件的初始化
                clonedSpecValueItem.find('.upload-btn1').FilesUpload({
                    success: function(data, element) {
                        // 把预览图片的DOM移动到上方
                        $(element).prepend(
                            $(element).find('span.placeholdermap')
                        );
                        // 动态把上传图片的URL增加到对应规格数据中:spec_iamge
                        var name = getSkuItemName(element);
                        var value = getSkuValueName(element);
                        specNameValueObject[name].forEach(function(tempObj) {
                            if (tempObj.spec_value === value) {
                                tempObj.spec_image = data;
                                tempObj.spec_type = 1;
                            }
                        });
                    },
                    error: function(error) {
                        console.log(error);
                    }
                });
            }
        }
    };

    // 获取当前规格项的名称
    var getSkuItemName = function(which) {
        return $(which)
            .closest('.sku-group-item')
            .find('.select-sku .item')
            .text();
    };

    // 获取当前规格项的id
    var getSkuItemId = function(which) {
        return $(which)
            .closest('.sku-group-item')
            .find('.select-sku .item')
            .attr('data-value');
    };

    // 获取当前规格值的名称
    // which：当前上传图片插件对应的span DOM
    var getSkuValueName = function(which) {
        return $(which)
            .parent()
            .prev()
            .find('.value')
            .text();
    };

    // 从初始化数据中查询特定规格值id的对象
    var getTheValueObj = function(value_id) {
        var tempObj;
        var ok = false;
        initDataWhenEdit.forEach(function(sku) {
            if (ok) {
                return;
            }
            if (!sku || !sku.specList || sku.specList.length === 0) {
                return;
            }
            sku.specList.forEach(function(specObj) {
                if (specObj.spec_value_id == value_id) {
                    tempObj = specObj;
                    ok = true;
                }
            });
        });
        return tempObj;
    };

    // 当前规格值存在于 specNameValueObject 数据中吗
    // 用户输入规格值，准备调用API将之保存到数据库前调用
    // 存在：return true

    var specValueIsExistInData = function(specValue, specValueArray) {
        return (
            specValueArray.filter(function(tempObj) {
                return tempObj.spec_value === specValue;
            }).length > 0
        );
    };

    // 当前规格项存在于 specNameArray 数据中吗
    // 用户输入规格项名称，准备调用API将之保存到数据库前调用
    // 存在：return true

    var specNameIsExistInData = function(specName, tempSpecNameArray) {
        return (
            tempSpecNameArray.filter(function(tempObj) {
                return tempObj.spec_name === specName;
            }).length > 0
        );
    };

    // 获取所有规格项的name: id 键值对
    var getSpecItemNameIdObject = function() {
        var nameIdObj = {};
        skuDOM
            .find('.td-section .sku-group-item .select-sku .item')
            .each(function() {
                nameIdObj[$(this).text()] = $(this).data('value');
            });
        return nameIdObj;
    };

    // 获取用户输入/选择的 sku 项目的值
    // 不仅可以获取数据,也可以只用来清空选中框
    var getSkuItemValues = function(which) {
        var valueItems = $(which)
            .siblings('div.select-sku-value')
            .find('.item');
        var valueObj = {};
        valueItems.each(function() {
            // 去掉右上角的 X 删除字符
            var text = $(this).text();
            text = text.substring(0, text.length - 1);
            valueObj[text] = $(this).data('value');
            // 模仿点击 X ,达到清空选中框的作用
            $(this)
                .find('a')
                .trigger('click');
        });
        return valueObj;
    };

    // 规格值是否已经存在? （是否存在于 页面DOM中，区别于另一个是否存在的方法，那个是存在于变量数据中。）
    // 准备将规格值插入到页面中时调用
    // specName 规格名称, value 规格值
    var specValueIsExist = function(where, specName, value) {
        var itemID = getItemId(where);
        var tempObj = specObject[itemID];
        // 没有规格名,表示第一次,不可能有属性值对象
        if (tempObj && !tempObj.hasOwnProperty(specName)) {
            return false;
        } else if (tempObj && tempObj[specName].hasOwnProperty(value)) {
            return true;
        }
        return false;
    };

    // 根据 '+添加' 按钮在页面的位置动态调整规格值输入框的 左右边距
    var adjustSpecValueDialog = function() {
        var addButton = skuDOM.find(
            ' .td-section .sku-group-item .add-sku-value'
        );
        var documentWidth = $(document).width();
        var dialogWidth = skuDOM
            .find(' .td-section .sku-group-item .sku-value-group-item')
            .width();
        var leftDistanceOfDialog = addButton.offset().left;
        var result = documentWidth - leftDistanceOfDialog;

        if (result < dialogWidth) {
            skuDOM
                .find(' .td-section .sku-group-item .sku-value-group-item')
                .css('left', '-' + (dialogWidth - result + 100) + 'px');
        } else {
            skuDOM
                .find(' .td-section .sku-group-item .sku-value-group-item')
                .css('left', '0');
        }
    };

    // 获取项目块的id
    var getItemId = function(which) {
        return $(which)
            .closest('.sku-group-item')
            .data('item-id');
    };

    // 把页面所有pointer的class,悬浮效果设置成小手
    var setHoverPointer = function(dom) {
        var targetDom = dom ? dom.find('.pointer') : skuDOM.find('.pointer');
        targetDom.each(function() {
            $(this).mouseover(function() {
                $(this).css('cursor', 'pointer');
            });
            $(this).mouseout(function() {
                $(this).css('cursor', 'normal');
            });
        });
    };

    // 转换源数据结构为利于组合算法使用的数据结构
    var translateData = function(sourceData) {
        var tempData = [];
        for (var itemID in sourceData) {
            var tempObj = sourceData[itemID];
            var specObj = tempObj[Object.keys(tempObj)[0]];

            // 如果 `项目id` 对应的value没有数据或者规格项对应的规格值没有数据, 跳出本次循环
            if (
                Object.keys(tempObj).length == 0 ||
                Object.keys(specObj).length == 0
            ) {
                continue;
            }
            var tempArr = [];
            for (var key in specObj) {
                tempArr.push({
                    name: key,
                    id: specObj[key]
                });
            }
            tempData.push(tempArr);
        }
        return tempData;
    };

    // 核心算法
    // 对数据进行组合.
    var combineData = function(arr) {
        var data = [];
        (function f(t, a, n) {
            if (n == 0) return data.push(t);
            for (var i = 0; i < a[n - 1].length; i++) {
                f(t.concat(a[n - 1][i]), a, n - 1);
            }
        })([], arr, arr.length);
        return data;
    };

    // 生成规格表格的 body
    var generateTbody = function(data) {
        var trList = combineData(data);
        // 给数据加上一层HTML标签;
        var trHtmlList = trList.map(function(tr) {
            var trHTML = '<tr>';
            var tdList = tr.map(function(td) {
                return (
                    '<td data-id=' +
                    td.id +
                    ' class="td-spec">' +
                    td.name +
                    '</td >'
                );
            });
            var getQuantityHTML = function() {
                // 是编辑模式,同时不能是 草稿箱的商品发布/编辑页面
                if (editMode && !/draft_/.test(location.href)) {
                    // 同时隐藏库存设置
                    return '<input class="form-control" value="0" readonly>';
                } else {
                    return '<span class="note-info">必填,有效的数字</span><input class="form-control">';
                }
            };

            var restTdHTML =
                '<td class="sn"><input class="form-control"><span></span></td>' +
                '<td class="weight"><span class="note-info">必填,有效的数字</span><input class="form-control"><span></span></td>' +
                '<td class="quantity">' +
                getQuantityHTML() +
                '<span></span></td>' +
                '<td class="cost"><span class="note-info">必填,有效的数字</span><input class="form-control"><span></span></td>' +
                '<td class="price"><span class="note-info">必填,有效的数字</span><input class="form-control"><span></span></td>';
            return (
                trHTML +
                tdList
                    .toString()
                    .toString()
                    .replace(/,/g, '') +
                restTdHTML +
                '</tr>'
            );
        });

        var tbody = '<tbody>';
        // 遍历拼接<tr>标签
        trHtmlList.forEach(function(ele) {
            tbody += ele;
        });
        tbody += '</tbody>';

        return tbody;
    };

    // 生成规格表格的 head
    var generateThead = function() {
        var specNames = [];
        for (var itemID in specObject) {
            var tempObj = specObject[itemID];
            var specName = Object.keys(tempObj)[0];
            var specObj = tempObj[specName];

            // 如果 `项目id` 对应的value没有数据或者规格项对应的规格值没有数据, 跳出本次循环
            if (
                Object.keys(tempObj).length == 0 ||
                Object.keys(specObj).length == 0
            ) {
                continue;
            }
            // 获取规格项名称
            specNames.push(specName);
        }
        var thead = '<thead><tr>';
        specNames.reverse().forEach(function(specName) {
            thead += '<th class="name">' + specName + '</th>';
        });
        thead =
            thead +
            '<th class="th-sn"><span>货号</span></th>' +
            '<th class="th-weight"><span>重量(g)</span></th>' +
            '<th class="th-quantity"><span>库存</span></th>' +
            '<th  class="th-cost"><span>成本价</span></th>' +
            '<th class="th-price"><span>价格（元）</span></th>' +
            '</tr></thead>';

        return thead;
    };

    // 生成规格表格的 footer
    var generateTfooter = function() {
        var setQuantity =
            editMode && !/draft_/.test(location.href)
                ? ''
                : '<span class="set-quantity pointer">库存</span>';

        var tfoot =
            '<tfoot><tr><td colspan="5"><div class="foot">' +
            '<span>批量设置 :</span>' +
            '<div class="set"><span class="set-price pointer">价格</span>' +
            setQuantity +
            '</div><div class="input" data-which=""><input class="form-control"><span class="confirm pointer">确定</span><span class="cancel pointer">取消</span></div>' +
            '</div></td></tr></tfoot>';
        return tfoot;
    };

    // 生成规格表格/table HTML
    var generateTable = function() {
        var data = translateData(specObject);
        if (data.length == 0) {
            return '';
        }

        var table =
            '<table>' +
            generateThead() +
            generateTbody(data) +
            generateTfooter() +
            '</table>';
        // 把最新的table html 存到全局变量中.
        tableHTML = table;
        return dealRowSpan(data, $(table));
    };

    // 处理可能有的td应该跨行/跨列的情况
    var dealRowSpan = function(formatData, tableDom) {
        // 反转下数据的顺序,达到和 用户添加的一致;
        formatData = formatData.reverse();
        // 计算下标为index及之后的数据 数量的乘积;
        var getLength = function(index, data) {
            var count = 1;
            data.forEach(function(arr, tempIndex) {
                if (tempIndex >= index) {
                    count *= arr.length;
                }
            });
            return count;
        };

        // 规则对象: 记录了某个规格会跨的行数.
        // 后面会用来根据此对象,选择性的设置table的td的 rowspan 属性
        var ruleObj = {};
        for (var index = 0; index < formatData.length; index++) {
            ruleObj[index + 1] = getLength(index + 1, formatData);
        }

        // 垃圾箱: 存储循环完成后需要删除掉的td元素.
        // 不在循环的过程中删除不需要的td元素是因为,
        // 会影响到table结构,下一次循环就获取不到想要的td了.
        var rubbish = [];

        // 循环,设置需要跨行的td的rowspan属性
        // 里面的遍历，是竖向，从左到右开始进行的
        for (var i = 1; i <= formatData.length; i++) {
            // 找到特定列的所有DOM
            var tds = tableDom.find('tbody tr > td:nth-child(' + i + ')');
            // 遍历
            tds.each(function(index, td) {
                // 找到兄弟及自己的规格值DOM
                var tr = $(td).parent();
                var findSpecs = tr.children('.td-spec');
                // 遍历获取规格值id 拼接成的字符串
                var ids = '';
                findSpecs.each(function() {
                    ids += $(this).attr('data-id') + ' ';
                });
                // 把tr下面的规格值id组成的字符串存放到tr属性中
                tr.attr('data-spec-value-ids', ids);
            });

            if (ruleObj[i] == 1) {
                // 如果这一列没有需要跨列的
                // 跳过
                continue;
            }
            // 遍历
            tds.each(function(index, td) {
                var tempIndex = index + 1;
                // 余1 就设置跨列
                // 余0 就标记等着被清除
                if (tempIndex % ruleObj[i] == 1) {
                    $(td).attr('rowspan', ruleObj[i]);
                } else {
                    rubbish.push(td);
                }
            });
        }

        // 清除垃圾箱中 td DOM
        rubbish.forEach(function(ele) {
            $(ele).remove();
        });

        // ↓↓↓↓↓↓↓↓设置tfoot的跨列↓↓↓↓↓↓↓↓
        tableDom.find('tfoot td').attr('colspan', formatData.length + 5);

        return tableDom;
    };

    // 自动生成货号名
    var autoGenerateSN = function() {
        // 遍历货号
        var count = 1;
        skuDOM.find('tbody .sn input').each(function() {
            var $this = $(this);
            if (!$this.val()) {
                $this.val(goodsID + '-' + count);
                count++;
            }
        });
    };

    // 插入表格HTML到页面中
    var insertSpecTable = function() {
        // 插入之前先给当前用户输入数据进行 快照保存
        snapshot = getSpecTableJSON(true);
        var tableDom = $(generateTable());
        skuDOM
            .find(' .sku-table .sku-tr .td-quantity')
            .empty()
            .append(tableDom);
        bindMethodsAfterInsert(tableDom);
        setAllStorage();
        // 尝试进行用户输入恢复
        restoreDataFromSnapShot();
    };

    // 根据snapshot尝试进行用户输入恢复
    var restoreDataFromSnapShot = function() {
        // 如果没有快照数据
        if (!snapshot || snapshot.length === 0) {
            return;
        }
        snapshot.forEach(function(sku) {
            // 获取一组sku的规格值id字符串（空格分割）(尾部也有个空格)
            var ids =
                sku.specList
                    .map(function(specObj) {
                        return specObj.spec_value_id;
                    })
                    .join(' ') + ' ';

            // 遍历table的tr，匹配和ids相同的DOM
            skuDOM.find('table tbody tr').each(function() {
                var $this = $(this);
                // 找到了
                if ($this.attr('data-spec-value-ids') === ids) {
                    // 初始化sku_id
                    $this.attr('data-sku-id', sku.sku_id);
                    // 初始化其他td的输入
                    // key: price,weight,sn之类的
                    for (var key in sku) {
                        $this.find('td.' + key + ' input').val(sku[key]);
                    }
                }
            });
        });
    };

    // 插入到页面之后,开始绑定一些监听.
    var bindMethodsAfterInsert = function(tableDom) {
        // 总库存的检测
        skuDOM.find('.sku-tr .all-quantity').blur(function() {
            var input = $(this).val();
            // 不为零且没有数据 或者 NaN
            if ((input === '' && !input) || Number.isNaN(Number(input))) {
                // 展示span信息.说明必填
                $(this)
                    .prev()
                    .show();
                $(this).val('');
            } else {
                $(this)
                    .prev()
                    .hide();
            }
        });

        // 监听几个按钮的 pointer效果
        setHoverPointer(tableDom);

        tableDom.find('input').each(function() {
            $(this)
                .off()
                .on('blur', function(e) {
                    // 货号忽略
                    if (
                        $(this)
                            .parent()
                            .hasClass('sn')
                    ) {
                        return;
                    }
                    var input = $(this).val();
                    // 不为零且没有数据 或者 NaN
                    if (
                        (input === '' && !input) ||
                        Number.isNaN(Number(input))
                    ) {
                        // 展示span信息.说明必填
                        $(this)
                            .prev()
                            .show();
                        $(this).val('');
                    } else {
                        $(this)
                            .prev()
                            .hide();
                    }
                });
        });

        tableDom.find('.quantity input').on('change', function() {
            setAllStorage();
        });

        // 批量添加 价格
        tableDom.find('tfoot .set-price').click(function(which) {
            // 控制显隐,设置data-which
            // 1 表示 price
            $(this)
                .closest('.set')
                .next()
                .css('display', 'flex')
                .attr('data-which', 1);
            $(this)
                .closest('.set')
                .hide();
        });

        // 批量添加 库存
        tableDom.find('tfoot .set-quantity').click(function(which) {
            // 控制显隐,设置data-which
            // 0 表示 quantity
            $(this)
                .closest('.set')
                .next()
                .css('display', 'flex')
                .attr('data-which', 0);
            $(this)
                .closest('.set')
                .hide();
        });

        // 批量添加输入-确认
        tableDom.find('tfoot .input .confirm').click(function() {
            var $this = $(this);
            var which = $this.closest('.input').attr('data-which');
            var value = $this.siblings('input').val();
            // 如果点击的是 价格
            if (which == '1') {
                tableDom.find('tbody td.price').each(function() {
                    $(this)
                        .find('input')
                        .val(value);
                });
            } else {
                // 如果点击的是 库存
                tableDom.find('tbody td.quantity').each(function() {
                    $(this)
                        .find('input')
                        .val(value);
                });
                setAllStorage();
            }

            // 重置输入框
            $this.siblings('input').val('');
            $this.closest('.input').hide();
            $this
                .closest('.input')
                .prev()
                .show();
        });

        // 批量添加输入-取消
        tableDom.find('tfoot .input .cancel').click(function() {
            var $this = $(this);
            $this.siblings('input').val();
            $this.closest('.input').hide();
            $this
                .closest('.input')
                .prev()
                .show();
        });
    };

    // 获取库存总数
    var getAllStorage = function() {
        if (skuDOM.find('tbody').length === 0) {
            return skuDOM.find('.sku-tr .all-quantity').val();
        }
        var count = 0;
        skuDOM.find('tbody .quantity input').each(function() {
            var val = Number($(this).val());
            count += Number.isNaN(val) ? 0 : val;
        });

        return count;
    };

    // 设置总库存数
    var setAllStorage = function() {
        if (skuDOM.find('tbody').length === 0) {
            skuDOM.find('.sku-tr .all-quantity').removeAttr('disabled');
            skuDOM.find('.sku-tr .all-quantity').val(0);
        } else {
            skuDOM.find('.sku-tr .all-quantity').attr('disabled', true);
            skuDOM.find('.sku-tr .all-quantity').val(getAllStorage());
        }
    };

    // 获取最低价格
    var getMinPrice = function() {
        var minPrice = 0;
        skuDOM.find('tbody .price input').each(function(index) {
            var price = $(this).val();
            if (index == 0) {
                minPrice = price;
                return;
            }

            if (price < minPrice) {
                minPrice = price;
            }
        });
        return minPrice;
    };

    // 商家获取分类的规格
    var getGoodsSpec = function(category_id) {
        $.get(
            ctx + '/goods-info/seller/category/' + category_id + '/spec.do',
            function(response) {
                specNameArray = response;
                if (response && response.length > 0) {
                    response.forEach(function(item) {
                        specNameValueObject[item.spec_name] = item.valueList;
                    });
                }
            }
        );
    };

    // 遍历检查所有的input值 是否为有效值.
    var checkInputValue = function() {
        // 筛选非法的input DOM数组
        var IllegalInput = skuDOM
            .find('table tbody td input')
            .not('.sn input')
            .filter(function() {
                var input = $(this).val();
                return !input || Number.isNaN(Number(input));
            });
        // 如果存在非法输入
        if (IllegalInput.length > 0) {
            return false;
        }
        return true;
    };

    // 根据规格项名称和规格值id 获取对应规格值对象
    var specValueTypeAndImage = function(specName, specValueID) {
        // 遍历筛选,得到那个spec_value对象
        return specNameValueObject[specName].filter(function(item) {
            return item.spec_value_id == specValueID;
        })[0];
    };

    // 获取规格表格JSON
    // uncheck：不进行繁琐的检测？(为了实现用户增删规格值，根据用户上次输入，初始化当前表格用)
    var getSpecTableJSON = function(uncheck) {
        // 如果没有明说不检查
        if (!uncheck) {
            // 如果input值检测不通过(非草稿箱模式才检测)
            if (!draftMode && !checkInputValue()) {
                alert('请输入有效的数字!');
                return;
            }

            // 判断是否用户在选择添加规格图片的同时，完全上传了所有规格图片
            if (skuDOM.find('#inlineCheckbox').prop('checked')) {
                var firstItem = skuDOM.find('.sku-group-item').first();
                var images = firstItem.find('img');
                var status =
                    images.filter(function() {
                        // 如果图片存在url，返回true
                        return $(this).attr('src');
                    }).length === images.length;
                if (!status) {
                    // 1 表示不是所有的图片都有有效的src属性
                    return 1;
                }
            }
        }

        // 如果用户没有添加规格
        if (skuDOM.find('table tr').length === 0) {
            return [];
        }

        var table = $(tableHTML);
        // 规格名称数组.
        var nameArray = [];
        table.find('thead tr th.name').each(function() {
            nameArray.push($(this).text());
        });
        var skuList = [];
        var specItemNameIdObj = getSpecItemNameIdObject();
        table.find('tbody tr').each(function() {
            var $this = $(this);
            var tempObj = {
                price: undefined,
                weight: undefined,
                quantity: undefined,
                cost: undefined,
                sn: undefined,
                sku_id: undefined,
                enable_quantity: undefined
            };
            var tempSkuArray = [];
            $(this)
                .find('td.td-spec')
                .each(function(index) {
                    $this = $(this);
                    // 规格值对象
                    var specValueObject = specValueTypeAndImage(
                        nameArray[index],
                        $this.data('id')
                    );
                    // 获取初始化时的数据中对应的规格值对象(如果存在的话)
                    // 下面使用时，会检测下是否在初始化数据中存在图片数据
                    var initSpecValueObj = getTheValueObj($this.data('id'));

                    tempSkuArray.push({
                        spec_name: nameArray[index],
                        spec_id: specItemNameIdObj[nameArray[index]],
                        spec_value: $this.text(),
                        spec_value_id: $this.data('id'),
                        spec_type: specValueObject.spec_type
                            ? specValueObject.spec_type
                            : initSpecValueObj
                              ? initSpecValueObj.spec_type
                              : '',
                        spec_image: specValueObject.spec_image
                            ? specValueObject.spec_image
                            : initSpecValueObj
                              ? initSpecValueObj.spec_image
                              : ''
                    });
                });
            tempObj.sku = tempSkuArray;
            skuList.push(tempObj);
        });

        // ---------------sku数据从上面存储的tableDOM中获取-----------

        // 用户输入的数据就得从页面中的DOM中获取了
        skuList.forEach(function(item, index) {
            var tr = skuDOM.find(
                ' .sku-tr .td-quantity table tbody tr:nth-child(' +
                    (index + 1) +
                    ')'
            );
            var price = tr.find('.price input').val();
            var weight = tr.find('.weight input').val();
            var quantity = tr.find('.quantity input').val();
            var enable_quantity = Number(tr.attr('data-enable-quantity'));
            var cost = tr.find('.cost input').val();
            var sn = tr.find('.sn input').val();
            var sku_id = Number(tr.attr('data-sku-id'))
                ? Number(tr.attr('data-sku-id'))
                : 0;

            item.price = price ? Number(price) : price;
            item.weight = weight ? Number(weight) : weight;
            item.quantity = quantity ? Number(quantity) : quantity;
            item.cost = cost ? Number(cost) : cost;
            item.sn = sn;
            if (sku_id || sku_id === 0) {
                item.sku_id = sku_id;
            }
            if (enable_quantity === 0 || enable_quantity) {
                item.enable_quantity = enable_quantity;
            }
        });

        // 应yanlin要求把sku改成specList(-_-)
        skuList.forEach(function(oneSku) {
            var tempObj = JSON.parse(JSON.stringify(oneSku.sku));
            delete oneSku.sku;
            oneSku.specList = tempObj;
        });
        return skuList;
    };

    // （此时）有其他的规格项吗？
    // 有/没有：true/false
    var hasOtherSpecItem = function() {
        return skuDOM.find('.sku-tr .td-section .sku-group-item').length !== 0;
    };

    // ------------------------------------DOM绑定方法-------------------------------------

    var initDOM = function() {
        // 添加商品规格
        skuDOM.find('.add-sku-group-item button').click(function() {
            var clonedSkuItem = $(this)
                .closest('.sku-tr')
                .find('.sku-item-model .sku-group-item')
                .clone(true);

            // 如果不是最上面的规格项，则删除其中的 '规格图片' 单选框
            if (hasOtherSpecItem()) {
                clonedSkuItem.find('.checkbox-inline').hide();
            }

            clonedSkuItem.css('z-index', countNumber);
            clonedSkuItem.data('item-id', countNumber);
            countNumber--;
            specNameArrayComputed = computedSepcNameArray();
            bindSelectizeJS(clonedSkuItem);
            clonedSkuItem.insertBefore($(this).parent());
        });

        // hover时显示删除该项的图标
        skuDOM
            .find('.sku-tr .sku-item-model .sku-group-item')
            .mouseover(function() {
                $(this)
                    .find('.delete-item')
                    .show();
            })
            .mouseout(function() {
                $(this)
                    .find('.delete-item')
                    .hide();
            });

        // 删除当前规格项
        skuDOM
            .find(
                '.sku-tr .sku-item-model .sku-group-item .bg-grey .delete-item'
            )
            .click(function() {
                var itemID = getItemId(this);
                delete specObject[itemID];
                $(this)
                    .closest('.sku-group-item')
                    .remove();
                insertSpecTable();

                // 如果没有规格table
                // 隐藏表格容器div
                if (
                    skuDOM.find('.sku-table .tr-quantity-div tr').length === 0
                ) {
                    skuDOM
                        .find('.sku-table .tr-quantity-div')
                        .css('display', 'none');
                } else {
                    skuDOM
                        .find('.sku-table .tr-quantity-div')
                        .css('display', 'flex');
                }

                // （如果删除当前规格项后，还存在其他规格项）最上面的规格项显示‘是否添加规格图片的复选框’
                skuDOM
                    .find('.sku-tr .td-section .sku-group-item')
                    .first()
                    .find('.checkbox-inline')
                    .show();
            });

        // hover时出现'删除规格值'选项
        skuDOM.find('.sku-item-model .value-item').mouseover(function() {
            // 显示 X 按钮
            $(this)
                .find('.delete')
                .show();
        });
        skuDOM.find('.sku-item-model .value-item').mouseout(function() {
            $(this)
                .find('.delete')
                .hide();
        });

        // 删除规格值
        skuDOM.find('.sku-item-model .value-item .delete').click(function() {
            var specName = getSkuItemName(this);
            var specValue = $(this)
                .prev()
                .text();
            var itemID = getItemId(this);
            // 删除数据源中的key和DOM
            delete specObject[itemID][specName][specValue];
            $(this)
                .closest('.value-item')
                .remove();
            insertSpecTable();
            // 隐藏表格容器div
            if (skuDOM.find('.sku-table .tr-quantity-div tr').length === 0) {
                skuDOM
                    .find('.sku-table .tr-quantity-div')
                    .css('display', 'none');
            } else {
                skuDOM
                    .find('.sku-table .tr-quantity-div')
                    .css('display', 'flex');
            }
        });

        // 点击 "+添加" 添加规格值
        skuDOM.find('.sku-tr .sku-item-model .add-sku-value').click(function() {
            adjustSpecValueDialog();
            $(this)
                .siblings('.sku-value-group-item')
                .css('display', 'flex');
        });

        // 确定 增加规格值
        skuDOM
            .find('.sku-item-model .sku-value-group-item .confirm')
            .click(function() {
                var specName = getSkuItemName(this);
                // 如果用户没有选择规格项,弹出框警告
                if (!specName) {
                    alert('请选择规格项!');
                    return;
                }
                var specValuesObj = getSkuItemValues(this);
                createAndInsertSkuValue(
                    $(this).closest('.spec-values'),
                    specValuesObj,
                    specName,
                    $(this).closest('.sku-group-item')
                );
                var itemID = getItemId(this);
                var tempObj = specObject[itemID];

                // 如果key存在并且value也存在 就合并.
                if (tempObj.hasOwnProperty(specName) && tempObj[specName]) {
                    tempObj[specName] = Object.assign(
                        tempObj[specName],
                        specValuesObj
                    );
                } else {
                    // 覆盖
                    tempObj[specName] = specValuesObj;
                }

                $(this)
                    .closest('.sku-value-group-item')
                    .hide();

                insertSpecTable();

                // 显示表格容器div
                skuDOM
                    .find('.sku-table .tr-quantity-div')
                    .css('display', 'flex');
            });

        // 取消 增加规格值
        skuDOM
            .find('.sku-item-model .sku-value-group-item .cancel')
            .click(function() {
                // 不要数据,仅清空选中框
                getSkuItemValues(this);
                $(this)
                    .closest('.sku-value-group-item')
                    .hide();
            });

        // 总库存的输入值校验
        skuDOM.find('.sku-tr .all-quantity').change(function() {
            // 输入值无效,则自动清除
            if (Number.isNaN(Number($(this).val()))) {
                $(this).val('');
            }
        });

        // 点击 增加规格图片
        skuDOM.find('#inlineCheckbox').click(function() {
            var $this = $(this);
            if ($this.prop('checked') === true) {
                // 显示最上面规格的图片块
                $(this)
                    .closest('.sku-table')
                    .find('.sku-group-item')
                    .first()
                    .find('.spec-value-image')
                    .show();
            } else {
                // 隐藏最上面规格的图片块
                $(this)
                    .closest('.sku-table')
                    .find('.spec-value-image')
                    .hide();
            }
            return;
        });
    };

    // ------------------------------------编辑状态下的初始化功能----------------------------
    // ---------------初始化过程中的全局属性----------------

    // 记录specObj属性被添加时的顺序
    // 初始化时用，保证规格项顺序的正确
    var orderOfSpecObj = [];
    var specObj = {};
    // var specObj = {
    //     "颜色": {
    //         "黄色": 41,
    //         "绿色": 42
    //     },
    //     "尺寸": {
    //         "XXS": 10,
    //         "S": 12,
    //         "M": 13,
    //         "XS": 11
    //     },
    //     "容量": {
    //         "16GB": 62,
    //         "2GB": 59
    //     },
    //     "选择尺码": {
    //         "36": 67
    //     }
    // }
    var initDataWhenEdit = []; // 编辑时的初始化Data数据

    // ---------------数据转换函数----------------

    // 转换skuList为 规格向的数据.
    var transformDefaultData = function(data) {
        var specDataObj = {};
        var tempObj;
        data.forEach(function(skuObj) {
            if (!skuObj.specList || skuObj.specList.length == 0) {
                return;
            }
            skuObj.specList.forEach(function(spec) {
                if (!spec) {
                    return;
                }
                if (specDataObj.hasOwnProperty(spec.spec_name)) {
                    if (
                        !specDataObj[spec.spec_name].hasOwnProperty(
                            spec.spec_value
                        )
                    ) {
                        specDataObj[spec.spec_name][spec.spec_value] =
                            spec.spec_value_id;
                    }
                } else {
                    tempObj = {};
                    tempObj[spec.spec_value] = spec.spec_value_id;
                    specDataObj[spec.spec_name] = tempObj;
                    // 记录规格项顺序
                    orderOfSpecObj.push(spec.spec_name);
                }
            });
        });
        return specDataObj;
    };

    // ---------------初始化中的函数----------------

    // 比较初始化数据和当前用户的sku规格数据是否相同
    // 返回值: 0|1
    // 0: 没有变化
    // 1: 有变化
    var skuCompared = function(newData, oldData) {
        // 旧数据不存在，新数据也不存在
        if ((!oldData || !oldData[0].specList) && newData.length === 0) {
            return 0;
        }

        // 同上
        if (
            (!oldData ||
                (oldData[0].specList && oldData[0].specList.length === 0)) &&
            newData.length === 0
        ) {
            return 0;
        }
        // oldData.specList 无效或者[0]为空数组 同时 newData有规格数据
        if (
            (!oldData ||
                !oldData[0].specList ||
                oldData[0].specList.length === 0) &&
            newData.length !== 0
        ) {
            return 1;
        }

        // 如果specList长度不相同,肯定规格项有变化
        // 只需判断第一个元素
        if (newData[0].specList.length !== oldData[0].specList.length) {
            return 1;
        }

        // 如果长度相同时
        // newData是自变量,oldData是因变量
        // 改为：‘只有’ 规格项变化时才返回1，
        // 其他都返回 0
        var hasChanged = false;
        // 因为每个sku中的specList都包含了所有的规格项名称，
        // 所以只判断第一个元素就好
        newData[0].specList.forEach(function(spec, index) {
            // 如果已经判断出变化,
            // 跳过当前遍历
            if (hasChanged) {
                return;
            }

            var oldTempObj = oldData[0].specList[index];
            // 判断名称
            if (spec.spec_name !== oldTempObj.spec_name) {
                hasChanged = true;
            }
        });

        return hasChanged ? 1 : 0;
    };

    // // 判断两个对象数组是相等
    // // key,value层面上的相等.
    // var objectArrayIsEqual = function(arr, arrTwo) {
    //     // 数组长度不相等的话
    //     if (arr.length !== arrTwo.length) {
    //         return false;
    //     }
    //     // 默认相等
    //     var isEqual = true;
    //     // 数组长度相等时
    //     // 遍历判断每个对象
    //     arr.forEach(function(tempObj, index) {
    //         // 如果已经判断出不相等,
    //         // 跳过当前遍历
    //         if (!isEqual) {
    //             return;
    //         }

    //         var oldTempObj = arrTwo[index];

    //         // 对比此对象的每个key,value
    //         for (var key in tempObj) {
    //             // 只判断这几个属性
    //             var skipArray = [
    //                 'spec_id',
    //                 'spec_name',
    //                 'spec_value',
    //                 'spec_value_id'
    //             ];
    //             // 如果不是这几个，跳过
    //             if (skipArray.indexOf(key) < 0) {
    //                 continue;
    //             }
    //             // 如果初始化对应数据不存在此key,或者对应的value不相等
    //             if (
    //                 !oldTempObj.hasOwnProperty(key) ||
    //                 tempObj[key] !== oldTempObj[key]
    //             ) {
    //                 isEqual = false;
    //                 break;
    //             }
    //         }
    //     });
    //     return isEqual;
    // };

    // 触发规格项的click,为促使selectize.js加载select option数据.
    var trigger = function() {
        var skuDOM = $('.sku-container .td-section');
        for (var item in specObj) {
            if (!item) {
                continue;
            }
            // 触发 增加规格项
            skuDOM.find('.add-sku-group-item button').trigger('click');
            // 触发 下拉选择框中的规格项
            skuDOM
                .find('.add-sku-group-item')
                .prev()
                .find('.selectize-input')
                .trigger('click');
        }
    };

    // 开始进行数据的初始化
    var setDefault = function(options) {
        if (options.hasOwnProperty('data') && options.data) {
            initDataWhenEdit = options.data;
        }
        // 先设置总库存数据
        if (options.quantity) {
            $('.sku-container .all-quantity').val(options.quantity);
        }
        // 如果不存在sku数据.
        if (options.data.length === 1 && !options.data[0].specList) {
            return;
        }

        var skuDOM = $('.sku-container .td-section');
        // 规格值名称数组
        var specValueArray;
        // 规格名数据
        var specNames = orderOfSpecObj;
        // 规格DOM数组
        var skuItems = skuDOM.find('.sku-group-item');
        // 特定规格DOM
        var skuItem;
        // 遍历进行初始化
        specNames.forEach(function(name, index) {
            skuItem = $(skuItems[index]);
            // 触发 选择 特定规格名项目
            skuItem
                .find('div.selectize-dropdown.single.select-sku .option')
                .filter(function() {
                    return $(this).text() == name;
                })
                .trigger('click');

            // 规格值数据的初始化
            createAndInsertSkuValue(
                skuItem.find('.spec-values'),
                specObj[name],
                name,
                skuItem
            );
            var itemID = getItemId(skuItem.find('.spec-values'));
            // 项对象, (建议打印出来看看)
            var tempObj = {};
            if (specObject.hasOwnProperty(itemID)) {
                tempObj = specObject[itemID];
            } else {
                specObject[itemID] = {};
            }

            // 如果key存在并且value也存在 就合并.
            if (tempObj && tempObj.hasOwnProperty(name) && tempObj[name]) {
                tempObj[name] = Object.assign(tempObj[name], specObj[name]);
            } else {
                // 覆盖
                tempObj[name] = specObj[name];
            }

            // // 更新筛选后的规格名数组, 重新绑定selectize.js
            // specNameArrayComputed = computedSepcNameArray();
            // bindSelectizeJS(skuItem);

            insertSpecTable();
        });
        // 遍历, 初始化
        options.data.forEach(function(oneSku, index) {
            // 特定行/tr
            var tr = $($('.sku-container table tbody tr')[index]);
            if (oneSku.sku_id) {
                // 如果当前页面不是草稿箱商品发布/编辑页
                // 则设置库存只读(当前还需要有sku_id)
                if (!/draft_/.test(location.href)) {
                    tr.find('.quantity input').attr('readonly', true);
                }
                tr.attr('data-sku-id', oneSku.sku_id);
                tr.attr('data-enable-quantity', oneSku.enable_quantity);
            }
            // key: price,weight,sn之类的
            for (var key in oneSku) {
                if (!key || key === 'sku') {
                    continue;
                }
                tr.find('td.' + key + ' input').val(oneSku[key]);
            }
        });
        // 完成表格数据的填充后,再调用一次 总库存的callback
        setAllStorage();

        // 初始化最上面的规格项的规格值图片（如果用户添加了的话）

        // 聚集带规格图片的规格对象
        var specImageArray = [];
        options.data.forEach(function(sku) {
            // 如果这组sku第一个spec对象存在，且有spec_image属性
            var tempObj = sku.specList[0];
            if (tempObj && tempObj.spec_image) {
                specImageArray.push(tempObj);
            }
        });

        // 过滤数组，因为存在重复的规格值对象（有图片）
        var newSpecImageArray = [];
        specImageArray.forEach(function(specObj) {
            if (newSpecImageArray.length === 0) {
                newSpecImageArray.push(specObj);
                return;
            }
            let exist = false;
            // 判断当前规格值对象是否存在于新数组中
            newSpecImageArray.forEach(function(newSpecObj) {
                if (newSpecObj.spec_value_id == specObj.spec_value_id) {
                    exist = true;
                }
            });
            // 如果不存在
            if (!exist) {
                // 添加进去
                newSpecImageArray.push(specObj);
                return;
            }
        });

        // 开始初始化
        if (newSpecImageArray.length > 0) {
            var firstItem = skuDOM.find('.sku-group-item').first();
            // 模拟点击
            firstItem.find('#inlineCheckbox').click();
            firstItem.find('.sku-item-body .value-item').each(function(index) {
                $(this)
                    .find('.temp-preview-image')
                    .attr(
                        'src',
                        newSpecImageArray[index]
                            ? newSpecImageArray[index].spec_image
                            : ''
                    );
            });
        }

        // 显示表格div
        $('.sku-container .sku-table .tr-quantity-div').css('display', 'flex');
    };

    // 开始初始化函数.
    var startDefault = function(options) {
        // 首先隐藏掉sku-container
        // (因为初始化用到了模拟点击. 所以页面会弹出一些框,用户看起来会觉得奇怪.)
        skuDOM.hide();
        trigger();
        //Warning: 定期器不可去掉.否则会因为selectize.js不能按预期对select 的 option初始化(奇葩的问题)!!!
        // 而且外面调用时也得加个定时器. 时间还不能太短...
        setTimeout(function() {
            setDefault(options);
            // 显示sku-container
            skuDOM.show();
        }, 500);
    };

    // 页面初始化时执行的一些方法.

    var init = function() {
        insertSpecTable();
        setHoverPointer();
        initDOM();
    };
    init();

    skuEditor = {
        // 获取SKU 的DOM数据,可以直接使用
        // 尽量早的从远程获取规格数据
        getEditorDOM: function(options) {
            // 把分类id赋给全局变量
            category_id = options.categoryID;
            getGoodsSpec(options.categoryID);
            return skuDOM;
        },
        getSkuList: getSpecTableJSON,
        getStorages: getAllStorage,
        skuHasChanged: function() {
            var skuList = getSpecTableJSON();
            return skuCompared(skuList, initDataWhenEdit);
        },
        generateSN: function(goods_id, mode) {
            /* 
             * 货号规格不存在
             * 返回true/false表示用户点击了确定/取消
             * 货号规格存在的话
             *  */

            // 如果是保存草稿箱时
            if (mode === 'draft') {
                // 默认设置goodsID
                goodsID = goods_id ? goods_id : 1;
                draftMode = true;
                autoGenerateSN();
            } else {
                // 普通上架
                goodsID = goods_id;
                if (!goodsID && goodsID != 0) {
                    return '商品编号不存在';
                }
            }

            // 如果规格货号存在未输入项
            if (
                skuDOM.find('table tbody .sn input').filter(function() {
                    return !$(this).val();
                }).length !== 0
            ) {
                if (confirm('是否自动生成货号?')) {
                    autoGenerateSN();
                    return true;
                } else {
                    return false;
                }
            }

            // 规格货号全部有输入
            // 判断货号之外的input 是否存在未输入项
            // 如果存在
            if (
                skuDOM
                    .find('table tbody td input')
                    .not('.sn input')
                    .filter(function() {
                        return !$(this).val();
                    }).length !== 0
            ) {
                return 'have';
            } else {
                return 'done';
            }
        },
        initData: function(options) {
            // 修改规格表格的模板数据
            // 给库存input添加readonly属性
            if (skuDOM) {
                // 进入编辑模式
                editMode = true;
                // 编辑模式,同时不能是草稿箱发布/编辑页面,
                // 总库存才不能编辑
                if (!/draft_/.test(location.href)) {
                    skuDOM.find('.sku-tr .all-quantity').attr('readonly', true);
                }
                $('.sku-container div.sku-tr.quantity-management-tip').css(
                    'display',
                    'flex'
                );
            }

            // 首先进行转换数据.
            specObj = transformDefaultData(options.data);

            // 定时器是必要的,否则会遇到问题.
            // 问题大概是触发了select的点击事件后,selectize.js没有立刻就把子option的数据渲染出来.所以依赖于此的代码就会出错.
            // 所以加个定时器,等执行代码时确保对应数据被渲染了出来.
            setTimeout(function() {
                startDefault(options);
            }, 500);
        }
    };
})();
