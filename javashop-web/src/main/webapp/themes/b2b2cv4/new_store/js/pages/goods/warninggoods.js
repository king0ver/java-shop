/**
 * Created by zjp on 2016/12/12.
 */
$(function(){

    //  商品库存修改
    (function(){
        var warehouseBtn = $('.warning-goods');
        warehouseBtn.unbind('click').on('click', function(){
            var _this = $(this);
            $.ajax({
                url : "warninggoods_edit.html?goods_id="+_this.attr("goods_id")+"&store_id="+_this.attr("store_id"),
                success : function(html) {
                    $.dialogModal({
                        title: '库存信息',
                        html: html,
                        btn: false,
                        //width: 500,
                        //  当dialog显示时，判断是否有规格，如果没有规格则将dialog宽度设置为300
                        showCall: function(){
                            if(!$('#dialogModal').find('*').is('.have_spec')){
                                if(lteIE8){
                                    $('#dialogModal').css({
                                        width: 300,
                                        marginLeft: -(280-(560-300)/2)
                                    });
                                }else {
                                    $('#dialogModal').find('.modal-dialog').css({width: 300});
                                };
                            };
                        },

                        callBack: function(){
                            var input = $('#dialogModal').find('.form-control'), inputLen = input.length;
                            for(var i = 0; i < inputLen; i ++){
                                var _thisInput = input.eq(i), val = _thisInput.val();
                                if(!val || isNaN(val) || val < 0 || !/^\d+$/.test(val)){
                                    input.eq(i).closest('.form-group').addClass('has-error error');
                                }else {
                                    input.eq(i).closest('.form-group').removeClass('has-error error');
                                };
                            };
                            for(var i = 0; i < inputLen; i ++){
                                var _thisInput = input.eq(i);
                                if(_thisInput.closest('.form-group').is('.error')){
                                    return false;
                                };
                            };

                            update();

                            function update(){
                                var options = {
                                    url : ctx + "/api/b2b2c/store-goods/save-goods-store.do?storeid="+_this.attr("store_id"),
                                    type : "POST",
                                    dataType : 'json',
                                    success : function(result) {
                                        if(result.result==1){
                                            $.message.success('修改成功！', 'reload');
                                        }else {
                                            $.message.error(result.message);
                                        };
                                    },
                                    error : function(e) {
                                        $.message.error("出现错误 ，请重试");
                                    }
                                };
                                $('#dialogModal').find("#goodsStoreForm").ajaxSubmit(options);
                            };
                        }
                    });
                },
                error : function() {
                    $.message.error("出现错误，请重试");
                },
                cache : false
            });
        });
    })();

    //  全选
    (function(){
        var checkAllBtn = $('.check-all'), checkBox = $('.checkbox'), checkBoxLen = checkBox.length;
        checkAllBtn.unbind('click').on('click', function(){
            var _this = $(this);
            if(_this.is(':checked')){
                checkBox.prop('checked', true);
            }else {
                checkBox.removeAttr('checked');
            };
        });
    })();

    

    /* 筛选JS
     ============================================================================ */
    //  筛选
    (function(){
        var store_cat = $('.store_cat'),
            market_enable = $('.market_enable'),
            store_cat_val = '',
            market_enable_val = '',
            seach_keyword_val = '';

        //  分类发生改变时
        store_cat.change(function(){
            var _this = $(this),
                store_cat_val   = _this.val();
            market_enable_val = market_enable.val();
            seach_keyword_val = $('.seach-keyword').val();
            if(!testStr(seach_keyword_val)){
                $.message.error('不能包含特殊字符！');
                return false;
            };
            select(seach_keyword_val, store_cat_val, market_enable_val);
        });

        //  上下架状态发生改变时
        market_enable.change(function(){
            var _this = $(this), market_enable_val = _this.val();
            store_cat_val     = store_cat.val();
            seach_keyword_val = $('.seach-keyword').val();
            if(!testStr(seach_keyword_val)){
                $.message.error('不能包含特殊字符！');
                return false;
            };
            select(seach_keyword_val, store_cat_val, market_enable_val);
        });

        //  按下回车键时
        $('.seach-keyword').keydown(function (event) {
            if (event.keyCode == 13) {
                seach_keyword_val = $('.seach-keyword').val();
                if(!testStr(seach_keyword_val)){
                    $.message.error('不能包含特殊字符！');
                    return false;
                };
                store_cat_val     = store_cat.val();
                market_enable_val = market_enable.val();
                select(seach_keyword_val, store_cat_val, market_enable_val);
            }
        });

        //  搜索关键词时
        $('#key_seach_btn').on('click', function(){
            var  _this = $(this),
                seach_keyword_val = $('.seach-keyword').val();
            if(!testStr(seach_keyword_val)){
                $.message.error('不能包含特殊字符！');
                return false;
            };
            store_cat_val     = store_cat.val();
            market_enable_val = market_enable.val();
            select(seach_keyword_val, store_cat_val, market_enable_val);
        });

        //  如果之前有搜索过，则获取url中的搜索关键词赋值给搜索框
        (function(){
            var str = GetQueryString('goodsName');
            if(str){
                $('.seach-keyword').val(str)
            };
        })();

        function select(k, s ,m){
                if(m == -1){
                    location.href="warning_goods.html?goodsName="+k+"&store_cat="+s/*+"&menu=store_goods"+url_parame*/;
                }else {
                    location.href="warning_goods.html?goodsName="+k+"&store_cat="+s/*+"&menu=store_goods"+url_parame*/;
                }
                /*<#if type??>
                url_parame = "&type=${type}&mark=${mark}&tagId=${tagId}";
                </#if>*/
        };

        function testStr(str){
            var pattern	= /[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im;
            if(pattern.test(str)){
                return false
            }else {
                return true;
            };
        };
    })();

    //  搜索框效果
    (function(){
        var seach = $('.seach'),
            input = seach.find('input'),
            seachBtn = seach.find('.seach-btn');
        input.on('click', function(e){
            bindFocus();
            e.stopPropagation();
        });

        $(document).on('click', function(e){
            bindBlur();
            e.stopPropagation();
        });

        seachBtn.hover(function(){
            $(document).off('click');
        }, function(){
            $(document).on('click', function(e){
                bindBlur();
                e.stopPropagation();
            });
        });

        function bindFocus(){
            input.addClass('focus');
            seach.addClass('focus');
            seachBtn.addClass('show');
        };

        function bindBlur(){
            input.removeClass('focus');
            seach.removeClass('focus');
            seachBtn.removeClass('show');
        };
    })();


    (function(){
    //  修复IE7下 .right-top-tools样式错乱问题
        if(Sys.ie == 7){
            $('.right-top-tools').css({width: '49%'});
        };

    //  修复IE下 顶部工具栏位置问题
        if(Sys.ie > 7) {
            $('.top-tools').css({height: 48});
        };

    //  修复IE78下搜索框样式错乱问题
        if(Sys.ie < 9){
            $('.seach-keyword').css({height: 20});
            $('.seach .icomoon').css({top: 0});
        };
    })();

});