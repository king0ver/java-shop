/**
 * Created by Andste on 2016/12/21.
 */
$(function () {
    var module = new Module();
    var store_id = module.getQueryString('store_id');
    var bonusScroll;

    initBonusScroll();
    receiveBuons();

    //  领取优惠券
    function receiveBuons() {
        var bonus_id;
        var $this;
        $('#store-bonus-box').on('tap', '.bonus-item', function () {
            $this          = $(this);
            var is_receive = $this.is('.is-receive');
            bonus_id       = $this.attr('data-id');
            if(is_receive){module.message.error('已经领取过了哦！'); return false}

            _ajax();
            return false
        })

        function _ajax() {
            $.ajax({
                url : ctx + '/api/b2b2c/bonus/receive-bonus.do',
                data: {
                    store_id: store_id,
                    type_id : bonus_id
                },
                type: 'POST',
                success: function (res) {
                    if(res.result == 1){
                        module.message.success(res.message, received);
                    }else {
                        module.message.error(res.message);
                    }
                },
                error: function () {
                    module.message.error('出现错误，请重试！');
                }
            })
        }

        //  已领
        function received() {
            $this.addClass('is-receive');
            $this.find('.top').html('已');
            $this.find('.bottom').html('领');
        }
    }


    //  初始化优惠券iScroll
    function initBonusScroll() {
        var bonusBox = $('.content-store-bonus');
        if(bonusBox.length == 0){return};
        var items = $('.bonus-item'), itemsLen = items.length;
        $('.bonus-items').css('width', itemsLen * 119);
        bonusScroll = new IScroll('#store-bonus-box', {
            scrollX: true,
            scrollY: false,
            probeType   : 1,
            disableTouch: false,
            tap         : true
        });

        document.getElementById('store-bonus-box').addEventListener('touchmove', function (e) { e.preventDefault() }, false);
    }
})