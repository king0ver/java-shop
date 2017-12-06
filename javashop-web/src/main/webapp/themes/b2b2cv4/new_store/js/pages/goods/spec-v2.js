function zuhe_ar(ar1, ar2) {
    var new_ar = [];
    for (var i = 0; i < ar1.length; i++) {
        for (var j = 0; j < ar2.length; j++) {
            //可能是白，也可能是[白,x]
            var __temp = cloneAr(ar1[i]);
            if (__temp.value) {
                new_ar.push([__temp, ar2[j]]); //白的情况
            } else {
                __temp.push(ar2[j]);
                new_ar.push(__temp);
            }
        }
    }
    return new_ar;
}

function cloneAr(ar) {
    var new_ar = [];
    for (var i in ar) {
        new_ar[i] = ar[i];
    }
    return new_ar;
}

var SpecOper = {
    specProp: {},
    init: function () {
        var self = this;
        self.syncSpecFromTable();
        self.bindTableEvent();
        self.buttonBind();
        self.changeSpecValue();
        setTimeout(function () {
            self.createSpec();
            self.bindSpecImgEvents();
            self.initSpecImg()
        }, 500);

        $("#spec-input .spec .chk").on('click', function () {
            var $this = $(this);
            var _li = $this.closest('li');
            var change = _li.find(".change-spec-value");
            var _readOnly = change[0].readOnly;
            change[0].readOnly = !_readOnly;
            _readOnly ? change.addClass('ipt') : change.removeClass('ipt');
            self.createSpecImg($this);
            self.createSpec();
        });
    },

    initSpecImg: function () {
        var self = this;
        var checkboxs = $("input[specmemo='type_color']");
        for (var i = 0; i < checkboxs.length; i++) {
            var _checkbox = checkboxs[i];
            if (_checkbox.checked) {
                self.createSpecImg($(_checkbox))
            }
        }
    },

    createSpecImg: function (_this) {
        var $this = _this;
        var type_color = $this.attr('specmemo') === 'type_color';
        if (!type_color) { return }
        var checked = $this[0].checked,
            closestLi = $this.closest('li'),
            _index = closestLi.attr('change-spec-index'),
            colorImgBox = $('.image-update-box');

        var __tempArr = {};
        checked ? _addColorImg() : _removeColorImg();
        function _addColorImg() {
            if (__tempArr[_index]) { return }
            var __color = closestLi.find('.change-spec-value').val(), specId = closestLi.find('.chk').val(), specImg = closestLi.find('img');
            var bg_url = specImg.attr('src'), fs_url = specImg.attr('data-fs');
            var is_add = /^fs:.?/.test(fs_url);
            bg_url = is_add ? bg_url : ctxPath + '/images/icon-img-upload.png';
            var bg_str = "url(" + bg_url + ") center center / cover no-repeat;";
            var _html = '\
                    <label class="item" __index="'+ _index + '" style="background: ' + bg_str + '">\
                        <span class="item-title">'+ __color + '</span>\
                        <input type="file" id="spec_image_id" class="spec-item-input">\
                        <input type="hidden" name="spec_value" value="'+ specId + '">\
                        <input type="hidden" name="spec_image" class="spec-item-input" value="'+ (is_add ? fs_url : '') + '">\
                    </label>';
            colorImgBox.append(_html);
            __tempArr[_index] = true;
        }

        function _removeColorImg() {
            colorImgBox.find("label[__index='" + _index + "']").remove();
            __tempArr[_index] = false;
        }
    },

    bindSpecImgEvents: function () {
        var self = this;
        $('.image-update-box').on('change', '.spec-item-input', function () {
            var $this = $(this);
            lrz(this.files[0], { quality: 0.3 })
                .then(function (res) {
                    self.updateImg($this, res.base64);
                })
        })
    },

    updateImg: function ($this, base64) {
        $.ajax({
            url: ctx + '/api/base/upload-image/upload-base64.do',
            type: 'post',
            data: {
                base64Len: base64.length,
                base64: base64,
                type: 'spec_image'
            },
            success: function (res) {
                res.result === 1 ? (function () {
                    $this.closest('.item').css({
                        background: 'url(' + base64 + ') no-repeat center',
                        backgroundSize: 'cover'
                    }).find("input[name='spec_image']").val(res.data['fsimg'])
                })() : $.message.error(res.message);
            }
        });
    },

    /**
     * 开启或关闭规格按钮事件
     */
    buttonBind: function () {
        var self = this;
        $("#specOpenBtn").on('click', function () {
            if (!$("input[name='sn']").val()) {
                alert('请先填写位于基本信息中的【商品编号】！')
            } else {
                self.openSpec();
            }
        });

        $("#specCloseBtn").on('click', function () {
            var goodsid = $(this).attr("goodsid");
            if (0 !== goodsid) {
                $.ajax({
                    url: ctx + "/shop/admin/goods-spec/close-spec.do?action=check-goods-in-order&goodsid=" + goodsid,
                    dataType: "json",
                    success: function (result) {
                        if (result.result === 1) {
                            if (confirm("此商品已经有客户购买，如果关闭规格此订单将无法配货发货，请谨慎操作!\n点击确定关闭规格，点击取消保留此规格。")) {
                                self.closeSpec();
                            }
                        } else {
                            self.closeSpec();
                        }
                    },
                    error: function () {
                        alert("检测出错");
                    }
                });
            } else {
                self.closeSpec();
            }
        });
    },

    /**
     * 关闭规格
     */
    closeSpec: function () {
        $("#spec-input").hide();
        $("#no-spec-input").show();
        $("#haveSpec").val(0);
    },
    /**
     * 开启规格
     */
    openSpec: function () {
        // $("#spec-input").show();
        // here
        $(".goods-spec-control").empty().append(skuObject.getSkuDOM());
        $("#haveSpec").val(1);
    },
    createSpec: function () {
        var self = this;
        var specUl = $("#spec-input ul");
        var specs = [];
        var specnames = [];
        $.each(specUl, function (i, ul) {

            var chks = $(ul).find(".chk:checked");
            if (chks.size() > 0) {
                var valueAr = self.createSpecFromChk(chks); //根据chk生成的规格值数组
                specs.push(valueAr);
                specnames.push($(ul).attr("specname"));
            }
        });

        if (specs.length > 1) {
            $(".spec_table").show();
            var temp = specs[0];
            for (var i = 1; i < specs.length; i++) {
                temp = zuhe_ar(temp, specs[i]);
            }
            self.createSpecHead(specnames);
            self.createSpecBody(temp);
            self.bindTableEvent();
            MemberPrice.bindMbPricBtnEvent();
        } else {
            $(".spec_table").show();
            if (specs.length === 1) {
                $("#haveSpec").val(1);
                var one = specs[0];
                var _temp = [];
                for (var tempi = 0; tempi < one.length; tempi++) {
                    _temp[tempi] = [one[tempi]];
                }
                self.createSpecHead(specnames);
                self.createSpecBody(_temp);
                self.bindTableEvent();
                MemberPrice.bindMbPricBtnEvent();
            }
        }
        if (specs.length === 0) {
            $(".spec_table").hide();
            $("#haveSpec").val(0);
        }
    },
    bindTableEvent: function () {
        var self = this;
        $(".ipt").on('blur', function () {
            var $this = $(this);
            var prop = $this.attr('prop');
            var value = $this.val();
            if (('price' === prop || 'weight' === prop || 'cost' === prop) && (!SpecOper.isNum(value))) {
                alert("请输入数字");
                $this.select();
            } else {
                var propid = $this.attr("propid");
                if (!self.specProp[propid]) {
                    self.specProp[propid] = [];
                }
                self.specProp[propid][prop] = value;
            }
        });

        $(".spec_table .delete").on('click', function () {
            self.deleteProRow($(this));
        });
    },
    isNum: function (num) {
        var reg = /^(-?\d*)\.?\d{1,4}$/;
        return reg.test(num);
    },
    createSpecHead: function (specnames) {
        var thead = $(".spec_table thead tr").empty();
        for (var i = 0; i < specnames.length; i++) {
            thead.append('<th>' + specnames[i] + '</th>');
        }
        var _th = '<th>货号</th><th>销售价</th><th>重量</th><th>成本价</th>';
        thead.append(_th);
    },

    /**
     * 生成货品表格
     * @param specAr  规格数组
     */
    createSpecBody: function (specAr) {
        var self = this;
        var body = $(".spec_table tbody");
        body.empty();
        var index = 0;
        var nowsn = $("input[name='sn']").val();
        var nowPrice = $("#price").val();
        var nowWeight = $("#weight").val();
        var nowCost = $("#cost").val();
        for (var i = 0; i < specAr.length; i++) {

            var childAr = specAr[i];//这是一行

            var tr = $("<tr></tr>");

            var propid = "";
            var specvids = "";
            var specids = "";

            for (var j = 0; j < childAr.length; j++) { //这是一列

                var spec = childAr[j];
                if (propid !== "") propid += "_";
                propid += spec.valueid;
                tr.append($('<td class="spec-index" __index="' + spec.index + '"><input type="text" class="ipt form-control" name="specvalue_' + i + '" value="' + spec.value + '" readonly /></td>'));

                if (j !== 0) {
                    specvids += ",";
                    specids += ",";
                }
                specvids += spec.valueid;
                specids += spec.specid;

            }
            var _specProp = self.specProp[propid];
            var price = 0;
            var weight = 0;
            var cost = 0;
            var sn = "";
            var productid = "";

            if (_specProp) {
                price = _specProp["price"];
                weight = _specProp["weight"];
                cost = _specProp["cost"];
                sn = _specProp["sn"];
                productid = _specProp["productid"];

                if (!price) price = 0;
                if (!weight) weight = 0;
                if (!cost) cost = 0;
                if (!sn) sn = "";
                if (!productid) productid = "";
            }

            var hidden = '<input type="hidden" value="' + specvids + '" name="specvids">';
            hidden += '<input type="hidden" value="' + specids + '" name="specids">';
            var td = '';
            if (sn === "") {
                td = '<td><input type="text" class="ipt form-control" data-options="required:true" name="sns" value="' + nowsn + "-" + index + '" autocomplete="off" propid="' + propid + '" prop="sn">';
            } else {
                td = '<td><input type="text" class="ipt form-control" data-options="required:true" name="sns" value="' + sn + '" autocomplete="off" propid="' + propid + '" prop="sn">';
            }

            td += '<input type="hidden" class="ipt" name="productids" value="' + productid + '" propid="' + propid + '" prop="productid"></td>';

            //如果商品价格为0 也就是默认值，那么
            if (price === 0) {
                if (nowPrice === "") {
                    nowPrice = 0;
                }
                td += '<td style="width: 120px;">' + hidden + '<input type="text" class="ipt price form-control" propid="' + propid + '" prop="price" size="8"  value="' + nowPrice + '" name="prices" />';
            } else {
                td += '<td style="width: 120px;">' + hidden + '<input type="text" class="ipt price form-control" propid="' + propid + '" prop="price" size="8"  value="' + price + '" name="prices" />';
            }

            td += '<div class="member_price_box" index="' + i + '" style="display: none;"></div></td>';

            if (weight === 0) {
                if (nowWeight === "") {
                    nowWeight = 0;
                }
                td += '<td style="width: 120px;"><input type="text" class="ipt form-control" size="10" name="weights" value="' + nowWeight + '" autocomplete="off" propid="' + propid + '" prop="weight"></td>';
            } else {
                td += '<td style="width: 120px;"><input type="text" class="ipt form-control" size="10" name="weights" value="' + weight + '" autocomplete="off" propid="' + propid + '" prop="weight"></td>';
            }

            if (cost === 0) {
                if (nowCost === "") {
                    nowCost = 0;
                }
                td += '<td style="width: 120px;"><input type="text" class="ipt form-control" size="8" name="costs" value="' + nowCost + '" autocomplete="off" propid="' + propid + '" prop="cost"></td>';
            } else {
                td += '<td style="width: 120px;"><input type="text" class="ipt form-control" size="8" name="costs" value="' + cost + '" autocomplete="off" propid="' + propid + '" prop="cost"></td>';
            }

            tr.append($(td));

            body.append(tr);
            index++;
            if (_specProp) {
                var member_price = _specProp["member_price"];//会员价格是数组

                //同步会员价格
                if (member_price) {
                    var lvPriceEl = tr.find(".member_price_box .lvPrice");
                    lvPriceEl.each(function (pindex, v) {
                        $(this).val(member_price[pindex]);
                    });
                }
            }
        }
    },

    /**
     * 根据checkbox生成规格数组
     */
    createSpecFromChk: function (chks) {
        var ar = [];
        $.each(chks, function (i, c) {
            var chk = $(c);
            var spec = {};
            spec.valueid = parseInt(chk.val());
            spec.specid = parseInt(chk.attr("specid"));
            spec.value = chk.attr("spec_value");
            spec.index = parseInt(chk.closest('li').attr('change-spec-index'));
            ar.push(spec);
        });
        return ar;
    },

    /**
     * 由规格表格同步规格
     * 1.价格、重量等属性至specProp对象
     * 2.选中checkbox
     */
    syncSpecFromTable: function () {
        var self = this;
        $(".spec_table tbody tr").each(function () {
            var tr = $(this);
            var inputs = tr.find(".ipt");
            var propid = inputs.attr("propid");
            self.specProp[propid] = [];

            //同步各个属性
            inputs.each(function () {
                var $this = $(this);
                var propname = $this.attr("prop");
                self.specProp[propid][propname] = $this.val();
            });

            //同步规格复选框
            var propidAr = propid.split("_");
            for (var k = 0; k < propidAr.length; k++) {
                var _ = $("input[value=" + propidAr[k] + "]").prop("checked", true);
            }
        });
    },
    /**
     * 由规格表格中同步会员价
     */
    syncMemberPriceFromTable: function () {
        var self = this;
        $(".spec_table tbody tr").each(function () {
            var tr = $(this);
            var inputs = tr.find(".ipt");
            var propid = inputs.attr("propid");
            var member_price = [];
            //同步会员价信息
            inputs = tr.find(".member_price_box input.lvPrice");
            inputs.each(function () {
                var value = $(this).val();
                member_price.push(value);
            });
            self.syncMemberPrice(propid, member_price);
        });
    },

    /**
     * 同步某个货品的会员价格
     * @param propid
     * @param member_price
     */
    syncMemberPrice: function (propid, member_price) {
        var oneSpecProp = this.specProp[propid];
        if (!oneSpecProp) {
            this.specProp[propid] = [];
            oneSpecProp = this.specProp[propid];
        }
        oneSpecProp["member_price"] = member_price;
    }

    /**
     * 删除一行规格
     */
    ,
    deleteProRow: function (link) {
        if (confirm("确定删除此货品吗？删除后不可恢复")) {
            var productid = link.attr("productid");
            if (" " !== productid) {
                $.ajax({
                    url: ctx + "/shop/admin/goods-spec/delete-spec.do?productid=" + productid,
                    dataType: "json",
                    success: function (result) {
                        if (result.result === 1) {
                            if (confirm("此货品已经有顾客购买，不能删除规格!订单id是" + result.message)) {

                            }
                        } else {
                            link.parents("tr").remove();
                        }
                    },
                    error: function () {

                    }
                });
            } else {
                link.parents("tr").remove();
            }
        }
    },
    changeSpecValue: function () {
        var specInput = $('#spec-input');
        var specTable = $('.spec_table');
        var lis = specInput.find('li');
        for (var i = 0; i < lis.length; i++) {
            lis.eq(i).attr('change-spec-index', i);
        }
        var chks = specInput.find('.chk:checked');
        for (var k = 0; k < chks.length; k++) {
            chks.eq(k).closest('li').find('.change-spec-value').addClass('ipt').removeAttr('readonly');
        }
        specInput.on('blur', '.change-spec-value', function () {
            var $this = $(this);
            var _val = $this.val();
            if (!_val || _val === '') { alert('规格值不能为空！'); return }
            $this[0].value = _val;
            var _cl_li = $this.closest('li');
            _cl_li.find("input[type='checkbox']").attr('spec_value', _val);
            var _index = parseInt(_cl_li.attr('change-spec-index'));
            var specTd = specTable.find("td[__index=" + _index + "]");
            specTd.find('input').val(_val);
            var imageUpdateBox = $('.image-update-box');
            if (imageUpdateBox.find('label').length === 0) { return }
            imageUpdateBox.find("label[__index=" + _index + "]").find('span').html(_val);
        })
    }
};

$(function () {
    SpecOper.init();
});