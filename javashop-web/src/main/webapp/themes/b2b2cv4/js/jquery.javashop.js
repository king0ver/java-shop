/**
 * javashop代理对象
 */
var JavashopProxy = {};

/**
 * javashop grid代理对象
 */
JavashopProxy.grid = function ($el, opt) {
    var defaults = {
        'ordering': false
    };
    var options = $.extend({}, defaults, opt);
    return $el.addClass("layui-table site-table table-hover").attr("lay-skin", "line", "background", "green").css("width", "100%").DataTable({
        "processing": true,
        "serverSide": true,
        "ordering": options.ordering,
        "searching": false,
        "lengthChange": true,
        "start": 0,
        "length": 3,
        "pageLength": 3,
        "language": {
            "url": "/datatables/zh_CN.txt"
        },
        ajax: {
            //指定数据源
            type: "get",
            url: options.url
        },
        columns: options.columns
    });
};

/**
 * javashop tab插件
 */
JavashopProxy.tab = function ($this, opt) {

    var defaults = {
        onTabChange: function () {}
    };
    var options = $.extend({}, defaults, opt);

    $this.addClass("layui-tab  layui-tab-brief");
    $this.find(".tab-title").addClass("layui-tab-title");
    $this.find(".tab-title>li:first").addClass("layui-this");
    $this.find(".tab-content").addClass("layui-tab-content");
    $this.find(".tab-content>div:first").addClass("layui-show");
    $this.find(".tab-item").addClass("layui-tab-item");

    layui.use('element', function () {
        var element = layui.element();
        element.on('tab', function (data) {
            var index = data.index;
            var tabContent = $this.find(".tab-content>div").eq(data.index);

            var firstShow = ("yes" != tabContent.data("showed"));

            //执行onChange事件
            options.onTabChange(index, tabContent, firstShow);

            //添加已经显示过的data
            if ("yes" != tabContent.data("showed")) {
                tabContent.data("showed", "yes");
            }

        });
    });

    return $this;
};

JavashopProxy.dialog = function (opt) {
    var defaults = {
        'type': 1,
        move: false,
        zIndex: 100,
        buttons: []
    };

    var options = $.extend({}, defaults, opt);

    //重新组合符合layer 规则的btn
    var _btn = [];
    var btn_array = options.buttons;

    for (var i = 0; i < btn_array.length; i++) {

        //生成按钮文字
        _btn[i] = btn_array[i].text;
        var event = btn_array[i].event;

        //生成按钮事件
        options["btn" + (i + 1)] = function () {
            var p_layer = parent.layer;

            var dialog = function (_layer) {
                this.layar = _layer;
            };
            dialog.prototype = {
                close: function () {
                    this.layar.closeAll();
                },
                verify: function () {
                    return parent.layui.form().doVerify(parent.$(".form"));
                }
            };

            event(new dialog(p_layer));
        };

    }

    options.btn = _btn;
    var old_success = options.success;
    options.success = function (dlgEl) {
        dlgEl.render = function () {
            parent.JavashopProxy.form();
            parent.layui.form().render();
        };
        old_success(dlgEl);
    };

    $.ajax({
        url: options.url,
        proxy: false,
        success: function (cont) {
            options.content = cont;
            parent.layer.open(options);
            parent.JavashopProxy.form();
            parent.layui.form().render();
        }
    });
};

$.dialog = JavashopProxy.dialog;

/**
 * javashop search box对象
 */
JavashopProxy.searchBox = function ($el, opt) {
    var defaults = {
        keywordSearch: function () {},
        formSearch: function () {}
    };
    var options = $.extend({}, defaults, opt);
    var name = $el.attr("name");
    var placeholder = $el.attr("placeholder");
    // 传入"是否启用高级搜索参数",调用HTML生成函数
    var tpl = this.getTpl(opt.advanced);
    tpl.find("input.search-keyword").attr("name", name).attr("placeholder", placeholder);
    tpl.find(".search-panel").html(opt.content);

    tpl.insertBefore($el).find("button.dropdown-toggle").click(function () {
        event = event || window.event;
        event.stopPropagation();
        $(this).next().toggle();
    });

    var panel = tpl.find(".dropdown-menu1");
    panel.click(function (event) {
        event = event || window.event;
        event.stopPropagation();
    });

    $(document).click(function (e) {
        panel.hide();
    });

    tpl.find(".keyword-search-btn").click(options.keywordSearch);
    tpl.find(".form-search-btn").click(function () {
        options.formSearch(panel);

    });

    $el.remove();

    return $el;
};

/*
 *   2017-7-24-陈小博: 添加根据参数选择是否启用高级搜索框的功能.
 *   @advanced: 是否 不 启用高级搜索框.(不启用的话,就是简单搜索)
 *   可选值: true  
 *   默认值: false 即 启用高级搜索框
 */
JavashopProxy.searchBox.prototype.getTpl = function (advanced) {
    var tpl = "";

    tpl += '<div class="input-group" id="adv-search" style=" float: right;">';
    tpl += '    <input type="text" class="form-control search-keyword" placeholder="" style="width:403px;height: 20px;"  />';
    tpl += '    <div class="input-group-btn" style="width:72px;height:34px;    margin-left: -1px;"> ';
    tpl += '        <div class="btn-group" role="group">';
    if (!advanced) {
        tpl += '            <div class="dropdown dropdown-lg" style="width:33px;height:34px"> ;';
        tpl += '                <button type="button" class="btn btn-default dropdown-toggle"  style="width:36px;height:34px" data-toggle="dropdown" aria-expanded="false"><span class="caret"></span></button>';
        tpl += '                <div class="dropdown-menu1 dropdown-menu-right form-horizontal "  > ';
        tpl += '                   <div class="search-panel"  > </div>';
        tpl += '					<button type="button" class="btn btn-primary form-search-btn" style="border-top-left-radius: 4px;border-bottom-left-radius: 4px;width: 80px"><span class="glyphicon glyphicon-search layui-icon" aria-hidden="true">&#xe615</span></button>';
        tpl += '                </div>';
        tpl += '            </div>';
    }
    tpl += '            <button type="button" class="btn btn-primary keyword-search-btn" style="width: 40px;"><span class="glyphicon glyphicon-search layui-icon" aria-hidden="true">&#xe615</span></button>';
    tpl += '        </div>';
    tpl += '    </div>';
    tpl += '</div>';

    return $(tpl);
};





/**
 * javashop form代理
 */
JavashopProxy.form = function () {
    $(".form").addClass("layui-form ");
    $(".form>.form-item").addClass("layui-form-item");
    $(".form>.form-item .form-label").addClass("layui-form-label");
    $(".form>.form-item .input-block").addClass("layui-input-block");
    $(".form>.form-item .input-inline").addClass("layui-input-inline");
    $(".form>.form-item .inline").addClass("layui-inline");
    $(".form>.form-item .form-mid").addClass("layui-form-mid");
    $(".form>.form-item .word-aux").addClass("layui-word-aux");
    $(".form>.form-item .input").addClass("layui-input");
    $(".form>.form-item .textarea").addClass("layui-textarea");

    $(".form>.form-item .btn").addClass("layui-btn");
    $(".form>.form-item .btn").addClass("layui-btn");

    $(".form>.form-item  [submit]").each(function (i, el) {
        var $this = $(this);
        $this.attr("lay-submit", "yes");
    });

    $(".form>.form-item  [filter!='']").each(function (i, el) {
        var $this = $(this);
        var verify = $this.attr("filter");
        $this.attr("lay-filter", verify);
    });

    $(".form>.form-item  [verify!='']").each(function (i, el) {
        var $this = $(this);
        var verify = $this.attr("verify");
        $this.attr("lay-verify", verify);
    });

    $(".form>.form-item  [skin!='']").each(function (i, el) {
        var $this = $(this);
        var skin = $this.attr("skin");
        $this.attr("lay-skin", skin);
    });

    $(".tool-bar>button").addClass("layui-btn");
    $(".tool-bar>button.btn-normal").addClass("layui-btn-normal");
    $(".tool-bar>button>i").addClass("layui-icon");

    $(".form .datepicker").click(function () {
        var _format = $(this).attr("format");
        !_format && (_format = 'YYYY-MM-DD');
        layui.laydate({
            elem: this,
            istime: true,
            format: _format
        });
    });

};



(function ($) {

    $.openTab = function (_title, _url) {
        parent.openNewTab(_title, _url);
    };

    $.success = function (msg) {
        layer.msg(msg, {
            time: 3000, //3s后自动关闭
            offset: "t"
        });
    };

    $.error = function (msg) {
        layer.msg(msg, {
            time: 3000, //3s后自动关闭
            offset: "t",
            icon: 5
        });
    };

    $.confirm = function (text, opt) {

        var defaults = {
            ok: function () {},
            cancel: function () {}
        };
        var options = $.extend({}, defaults, opt);

        parent.layer.confirm(text, {
            btn: ['确认', '取消'] //按钮
        }, function () {
            options.ok();
            parent.layer.closeAll();
        }, function () {
            options.cancel();
        });

    };

    $.loading = {};
    $.loading.show = function () {
        // 2017-7-26-陈小博 重新改下Loading.
        layer.load(1);

        // PS: 发现其他地方的代码在调用这个方法时,居然传参了.... 尽管并没有用.(改变之前的代码)
        // $.loading.index = layer.msg('加载中', {
        //     icon: 16,
        //     time: 0,
        //     shade: 0
        // });
    };

    $.loading.close = function () {
        // 2017-7-26-陈小博 同上
        layer.closeAll("loading");

        // layer.close($.loading.index);
    };

    $.getUrlParam = function (name) {

        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r !== null) return unescape(r[2]);
        return "";

    };

    $.fn.grid = function (options) {
        var ele = this;
        return new JavashopProxy.grid(ele, options);
    };

    $.fn.tab = function (options) {
        var ele = this;
        return new JavashopProxy.tab(ele, options);
    };

    $.fn.searchBox = function (options) {
        var ele = this;
        return new JavashopProxy.searchBox(ele, options);
    };

    $.dateFormat = function (unix, format) {
        var _format = format || 'yyyy-MM-dd hh:mm:ss';
        var d = new Date(unix * 1000);
        var o = {
            "M+": d.getMonth() + 1,
            "d+": d.getDate(),
            "h+": d.getHours(),
            "m+": d.getMinutes(),
            "s+": d.getSeconds(),
            "q+": Math.floor((d.getMonth() + 3) / 3),
            "S": d.getMilliseconds()
        };
        if (/(y+)/.test(_format)) _format = _format.replace(RegExp.$1, (d.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(_format)) _format = _format.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return _format;
    };

    //$.ajax已经被代理，下面代码重复了，但未验证文件上传是否也是走的ajax,暂时保留
    //	$.fn.ajaxSubmitBak=$.fn.ajaxSubmit;
    //
    //	$.fn.ajaxSubmit=function(opt){
    //		opt.url="http://192.168.1.178:5555/api"+opt.url;
    //		return this.ajaxSubmitBak(opt);
    //	}

})(jQuery);