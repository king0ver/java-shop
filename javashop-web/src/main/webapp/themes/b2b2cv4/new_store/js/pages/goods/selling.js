/**
 * Created by Andste on 2016/6/15.
 */
$(function () {
    var typeStr = GetQueryString('type');

    //  如果是保存标签商品页
    (function () {
        if (typeStr == 'tag') {
            var saveAll = $('.down-all-goods'), tagId = parseInt(GetQueryString('tagId')), mark = GetQueryString('mark');
            saveAll.html('保存选中');
            var options = {
                url: ctx + '/api/b2b2c/store-goods-tag/save-rel.do?tagId=' + tagId,
                type: "POST",
                dataType: 'json',
                success: function (result) {
                    if (result.result == 1) {
                        $.message.success('保存成功！', function(){
                            location.href = './tags_edit.html?tagid=' + tagId + '&mark=' + mark;
                        });
                    } else {
                        $.message.error(result.message);
                    }
                    ;
                },
                error: function (e) {
                    $.message.error("出现错误 ，请重试！");
                }
            };
            saveAll.unbind('click').on('click', function () {
                $.confirm('确定保存选中吗？', function () {
                    $('#selling_form').ajaxSubmit(options);
                })
            });
        }
        ;
    })();

    //  商品单个下架操作
    (function () {
        var downBtn = $('.down-goods');
        downBtn.unbind('click').on('click', function (e) {
            e.stopPropagation();
            var _this = $(this), goods_id = _this.attr('goods_id');
            $.confirm('确定要下架此商品吗？', function () {
                setTimeout(function () {
                    downGood();
                }, 500);
            });

            function downGood() {
                $.ajax({
                    url: ctx + "/goods/seller/goods/under.do?goods_ids=" + goods_id,
                    type: "POST",
                    cache: false,
                    success: function (data) {
                    		 $.message.success('下架成功！', 'reload');
                    },
                    error: function (e) {
                    	$.message.error(e.responseJSON.error_message);
                    }
                });
            };
        });
    })();

    //  商品批量下架操作
    (function () {
        if (!typeStr) {
            var downAllBtn = $('.down-all-goods');
            downAllBtn.unbind('click').on('click', function () {
                var _this = $(this);
                $.confirm('确定要下架这些商品吗？', function () {
                    downAllGoods();
                });
            });

            function downAllGoods() {
                var options = {
                    url: ctx + "/goods/seller/goods/under.do",
                    type: "POST",
                    success: function (data) {
                            $.message.success('下架成功！', 'reload');
                    },
                    error: function (e) {
                        $.message.error(e.responseJSON.error_message);
                    }
                };
                $('#selling_form').ajaxSubmit(options);
            };
        }
        ;
    })();

    //  商品编辑
    (function(){
        var btn = $('.edit-goods');
        btn.unbind('click').on('click', function(e){
            var goods_id = $(this).attr('goods_id') || 0;
            e.stopPropagation();
            location.href = './goods_publish.html?goods_id=' + goods_id;
        });
    })();

    //  修复IE8下选择框样式问题
    (function () {
        if (Sys.ie == 8) {
            $('.checkbox').removeClass('check-width');
        };
    })();

    //  全选
    (function () {
        var checkAllBtn = $('.check-all'), checkBox = $('.checkbox');
        checkAllBtn.unbind('click').on('click', function () {
            var _this = $(this);
            if (_this.is(':checked')) {
                checkBox.prop('checked', true);
                showAllDown('show');
            } else {
                checkBox.removeAttr('checked');
                showAllDown('hide');
            };
        });
    })();

    //  手动选择
    (function () {
        var checkBox = $('.checkbox'), checkBoxLen = checkBox.length, tr = $('#selling_form').find('tr');
        checkBox.unbind('click').on('click', function () {
            var _this = $(this);
            if (_this.is(':checked')) {
                _this.removeAttr('checked');
            } else {
                _this.prop('checked', true);
            };
            f();
        });
        $('.th-name').find('a').on('click', function () {
            window.open($(this).attr('href'));
            return false;
        });
        tr.unbind('click').on('click', function () {
            var _this = $(this), c = _this.find('.checkbox');
            if (c.is(':checked')) {
                c.removeAttr('checked');
            } else {
                c.prop('checked', true);
            };

            f();
        });

        function f() {
            var temp = 0;
            for (var i in checkBox) {
                if (checkBox.eq(i).is(':checked')) {
                    temp++;
                };
            };
            m(temp);
        };

        function m(temp) {
            if (temp == checkBoxLen && temp != 0) {
                $('.check-all').prop('checked', true);
            } else {
                $('.check-all').removeAttr('checked');
            };
            if (temp > 0) {
                showAllDown('show');
            } else {
                showAllDown('hide');
            };
        };

    })();

    //  显示、隐藏【批量下架】按钮
    function showAllDown(str) {
        var downAllGoods = $('.down-all-goods');
        if (str == 'show') {
            downAllGoods.addClass('show');
            if (Sys.ie < 10) {
                downAllGoods.animate({marginLeft: 0}, 300);
            };
        } else {
            downAllGoods.removeClass('show');
            if (Sys.ie < 10) {
                downAllGoods.animate({marginLeft: -90}, 300);
            };
        };
    };


    /* 筛选JS
     ============================================================================ */
    //  筛选
    (function () {
        var store_cat = $('.store_cat'),
            market_enable = $('.market_enable'),
            store_cat_val = '',
            seach_keyword_val = '';

        //  分类发生改变时
        store_cat.change(function () {
            var _this = $(this),
                store_cat_val = _this.val();
            seach_keyword_val = $('.seach-keyword').val();
            if (!testStr(seach_keyword_val)) {
                $.message.error('不能包含特殊字符！');
                return false;
            };
            select(seach_keyword_val, store_cat_val);
        });

        //  按下回车键时
        $('.seach-keyword').keydown(function (event) {
            if (event.keyCode == 13) {
                seach_keyword_val = $('.seach-keyword').val();
//                if (!testStr(seach_keyword_val)) {
//                    $.message.error('不能包含特殊字符！');
//                    return false;
//                };
                store_cat_val = store_cat.val();
                select(seach_keyword_val, store_cat_val);
            }
        });

        //  搜索关键词时
        $('#key_seach_btn').on('click', function () {
            var _this = $(this),
                seach_keyword_val = $('.seach-keyword').val();
            //搜索去掉关键词限制
//            if (!testStr(seach_keyword_val)) {
//                $.message.error('不能包含特殊字符！');
//                return false;
//            };
            store_cat_val = store_cat.val();
            select(seach_keyword_val, store_cat_val);
        });

        //  如果之前有搜索过，则获取url中的搜索关键词赋值给搜索框
        (function () {
            var k = GetQueryString('goodsName'), s = GetQueryString('store_cat');
            if (k) {
                $('.seach-keyword').val(k);
            };
            if(s){
                $('.store_cat').val(s);
            };
        })();

        function select(k, s) {
            if (!k && (s == 0)) {
                location.href = "selling.html?market_enable=1";
            }else if(!k && s){
                location.href = "selling.html?&store_cat=" + s +"&market_enable=1";
            } else {
                location.href = "selling.html?goodsName=" + k + "&store_cat=" + s +"&market_enable=1";
            }
        };

        function testStr(str) {
            var pattern = /[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im;
            if (pattern.test(str)) {
                return false
            } else {
                return true;
            };
        };
    })();

    //  搜索框效果
    (function () {
        var seach = $('.seach'),
            input = seach.find('input'),
            seachBtn = seach.find('.seach-btn');
        input.on('click', function (e) {
            bindFocus();
            e.stopPropagation();
        });

        $(document).on('click', function (e) {
            bindBlur();
            e.stopPropagation();
        });

        seachBtn.hover(function () {
            $(document).off('click');
        }, function () {
            $(document).on('click', function (e) {
                bindBlur();
                e.stopPropagation();
            });
        });

        function bindFocus() {
            input.addClass('focus');
            seach.addClass('focus');
            seachBtn.addClass('show');
        };

        function bindBlur() {
            input.removeClass('focus');
            seach.removeClass('focus');
            seachBtn.removeClass('show');
        };
    })();

    //  修复IE7下 .right-top-tools样式错乱问题
    (function () {
        if (Sys.ie == 7) {
            $('.right-top-tools').css({width: '49%'});
        };
    })();

    //  修复IE下 顶部工具栏位置问题
    (function () {
        if (Sys.ie > 7) {
            $('.top-tools').css({height: 48});
        };
    })();

    //  修复IE78下搜索框样式错乱问题
    (function(){
        if(Sys.ie < 9){
            $('.seach-keyword').css({height: 20});
            $('.seach .icomoon').css({top: 0});
        };
    })();

});