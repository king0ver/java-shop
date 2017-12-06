/**
 * Created by Andste on 2016/12/23.
 */
$(function () {
    var module = new Module();
    init();
    
    function init() {
        module.navigator.init('申请售后');
        bindEvents();
    }

    function bindEvents() {

        //  tab切换
        changeTab();

        //  申请退货数量
        salesNum();

        //  提交申请
        submit();

        //  退款账号输入检测
        checkAccountInput();
    }

    //  tab切换
    function changeTab() {
        var afterSalesNav = $('.after-sales-nav');
        if(afterSalesNav.length === 0){return false}
        var contentBox = $('.after-sales-content');
        afterSalesNav.on('tap', '.nav-item', function () {
            var $this = $(this),
                index = $this.index();
            $this.addClass('active').siblings().removeClass('active');
            contentBox.find('.content-item').removeClass('show');
            contentBox.find('.content-item').eq(index).addClass('show');
            return false
        })
    }

    //  申请退货数量
    function salesNum() {
        var input = null,
            symbolLess = null,
            symbolAdd  = null,
            maxNum = null;

        $('.sales-update-num').on('tap', '.goods-symbol', function () {
            var $this     = $(this);
            var _closest = $this.closest('.goods-update-num');
            input = _closest.find('.goods-num');
            symbolAdd = _closest.find('.symbol-add');
            symbolLess = _closest.find('.symbol-less');
            maxNum = parseInt(_closest.find('.max-num').val());
            var _disabled = $this.is('.disabled'),
                _isAdd    = $this.is('.symbol-add');
            if (_disabled) {return false}
            var _val = parseInt(input.val());
            _val     = _isAdd ? _val + 1 : _val - 1;
            _setNum(_val);
            return false
        });

        /*input.on('blur', function () {
            var $this = $(this),
                _val  = $this.val();
            _val = parseInt(_val);
            _val = module.regExp.integer.test(_val) ? _val : 1;
            _val = _val < 1 ? 1 : _val;
            _val = _val > maxNum ? maxNum : _val;
            _setNum(_val);
        });*/


        function _setNum(_val) {
            _val > (maxNum - 1) ? symbolAdd.addClass('disabled') : symbolAdd.removeClass('disabled');
            _val < 2 ? (function () {
                    symbolLess.addClass('disabled');
                    return false
                })() : (function () {
                    symbolLess.removeClass('disabled');
                    return false
                })();
            input.val(_val);
        }
    }


    //  提交申请
    function submit() {
        $('#submit-btn').on('tap', function () {
            $('.content-item.refund').is('.show') ? _submitRefund() : _submitReturns();
            return false
        });
        
        //  退款
        function _submitRefund(){
            var refundType = $('#refundTypeSelect').val();
            var refundTypeAsOnline = $('#refundTypeAsOnline').val();
            var refundTypeAsBank = $('#refundTypeAsBank');
            var refundPrice = $('#refundPrice').val();
            var returnPayprice = $('#refundPayprice').val();
            var refundReason = $('#refundReason').val();
            var refundCustomerRemark = $('#refundCustomerRemark').val();
            if($('#refundTypeSelect').length===1 && (!refundType || refundType === '0')) {
                module.message.error('未选退款方式！');
                return
            }
            if($('#refundTypeSelect').length===1 &&( refundType !== '银行转账' && !refundTypeAsOnline)) {
                module.message.error('退款账号不能为空！');
                return
            }
            if(refundType === '银行转账'){
                var empty = false;
                refundTypeAsBank.find('input').each(function () {
                    if(!$(this).val())empty = true;
                });
                if(empty) {
                    module.message.error('请核对银行信息是否填写完整！');
                    return
                }
            }
            if(!refundPrice) {
                module.message.error('退款金额不能为空！');
                return
            }
            if(parseFloat(refundPrice) > parseFloat(returnPayprice)) {
                module.message.error('退款金额不能大于订单金额！');
                return
            }
            if(!refundReason || refundReason === '0') {
                module.message.error('退款原因不能为空！');
                return
            }
            if(!refundCustomerRemark) {
                module.message.error('问题描述不能为空！');
                return
            }
            
            $('#refund-form').ajaxSubmit({
                url: ctx + '/after-sale/mine/refund/apply.do',
                type: 'POST',
                success: function () {
                    alert('申请成功！');
                    location.href = "return-list.html";
                },
                error: function () {
                    module.message.error('出现错误，请稍后重试！')
                }
            })
        }

        //  退货
        function _submitReturns(){
            var returnsType = $('#returnsTypeSelect').val();
            var returnsTypeAsOnline = $('#returnsTypeAsOnline').val();
            var returnsTypeAsBank = $('#returnsTypeAsBank');
            var returnsPrice = $('#returnsPrice').val();
            var returnsPayprice = $('#returnsPayprice').val();
            var returnsReason = $('#returnsReason').val();
            var returnsCustomerRemark = $('#returnsCustomerRemark').val();
            if(!returnsType || returnsType === '0') {
                module.message.error('未选退款方式！');
                return
            }
            if(returnsType !== '银行转账' && !returnsTypeAsOnline) {
                module.message.error('退款账号不能为空！');
                return
            }
            if(returnsType === '银行转账'){
                var empty = false;
                returnsTypeAsBank.find('input').each(function () {
                    if(!$(this).val())empty = true;
                });
                if(empty) {
                    module.message.error('请核对银行信息是否填写完整！');
                    return
                }
            }
            if(!returnsPrice) {
                module.message.error('退款金额不能为空！');
                return
            }
            if(parseFloat(returnsPrice) > parseFloat(returnsPayprice)) {
                module.message.error('退款金额不能大于订单金额！');
                return
            }
            if(!returnsReason || returnsReason === '0') {
                module.message.error('退款原因不能为空！');
                return
            }
            if(!returnsCustomerRemark) {
                module.message.error('问题描述不能为空！');
                return
            }
            
            var checkboxed = $('.item-goods-checkbox:checked');
            if(checkboxed.length === 0) {
                module.message.error('请选择需要退货的商品！');
                return
            }
            var _data = {};
            _data['order_sn'] = $('#returnsOrderSn').val();
            _data['refund_way'] = returnsType;
            _data['refund_price'] = returnsPrice;
            _data['account_type'] = returnsType;
            _data['refund_way'] = $('.content-item.returns input[name=refund_way]').val();
            _data['return_account'] = returnsTypeAsOnline;
            _data['customer_remark'] = returnsCustomerRemark;
            _data['refund_reason'] = returnsReason;
            
            _data['bank_name'] = $("input[name='bank_name']").val();
            _data['bank_account_number'] = $("input[name='bank_account_number']").val();
            _data['bank_account_name'] = $("input[name='bank_account_name']").val();
            _data['bank_deposit_name'] = $("input[name='bank_deposit_name']").val();
            
            _data['goodsList'] = [];
            checkboxed.each(function () {
                var $this = $(this);
                _data['goodsList'].push({
                    goods_id: $this.data('goods_id'),
                    sku_id: $this.data('sku_id'),
                    return_num: $this.closest('.item-goods').find('.goods-num').val()
                })
            });
            
            $.ajax({
                url: ctx + '/after-sale/mine/return-goods/apply.do',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(_data),
                success: function () {
                    alert('申请成功！');
                    location.href = "return-list.html";
                },
                error: function () {
                    module.message.error('出现错误，请稍后重试！')
                }
            })
        }
        
    }

    //  退款账号输入移除部分emoji，并且检测输入长度
    function checkAccountInput(){
        $('.sales-account-input').on('input propertychange', function () {
            var $this = $(this),
                _val  = $this.val();
            _val = _val.length > 30 ? _val.substring(0, 29) : _val;
            _val = module.removeEmojiCode(_val);
            $this.val(_val);
        })
    }
    
    
    $('#refundTypeSelect').on('change', function () {
        var val = $(this).val(),
            refundTypeAsOnline = $('#refundTypeAsOnline'),
            refundTypeAsBank = $('#refundTypeAsBank');
        val === '银行转账'
            ? refundTypeAsOnline.hide() && refundTypeAsBank.show()
            : refundTypeAsBank.hide() && refundTypeAsOnline.show()
    });
    
    $('#returnsTypeSelect').on('change', function () {
        var val = $(this).val(),
            returnsTypeAsOnline = $('#returnsTypeAsOnline'),
            returnsTypeAsBank = $('#returnsTypeAsBank');
        val === '银行转账'
            ? returnsTypeAsOnline.hide() && returnsTypeAsBank.show()
            : returnsTypeAsBank.hide() && returnsTypeAsOnline.show()
    })
});


	