/**
 * Created by Andste on 2016/12/21.
 */
$(function () {
    var module = new Module();
    init();

    function init() {
        module.navigator.init('店铺详情');
        bindEvents();
    }

    //  事件绑定
    function bindEvents() {
        //  收藏店铺
        collectStore();
    }

    //  收藏店铺
    function collectStore() {
        $('.store_in').on('tap', function () {
            var store_id = $(this).attr('rel');
            $.ajax({
                url : ctx + '/api/b2b2c/store-collect/add-collect.do',
                data: {
                    store_id: store_id
                },
                type: 'POST',
                success: function (res) {
                    if(res.result == 1){
                        module.message.success(res.message);
                    }else {
                        module.message.error(res.message);
                    }
                },
                error: function () {
                    module.message.error('出现错误，请重试！');
                }
            })

            return false
        })
    }
})