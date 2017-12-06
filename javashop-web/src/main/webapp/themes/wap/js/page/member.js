/**
 * Created by Andste on 2016/12/9.
 */
$(function () {
    var module = new Module();
    init();

    function init() {
        module.navigator.init({
            title: '会员中心',
            left : {
                click: function () {
                    location.href = ctx + '/index.html'
                }
            }
        })
        module.scrollToTopControl.init({bottom: 0});
        bindEvents();
    }

    //  事件绑定
    function bindEvents() {
        var memberBox = $('.member'), settingBox = $('.setting'), headBg = $('.head-bg');
        $('.member-setting').on('tap', function () {
            //  memberBox.addClass('hide');
            settingBox.addClass('open');
            return false
        })

        $('.setting-navigator-left').on('tap', function () {
            settingBox.removeClass('open');
            //  memberBox.removeClass('hide');
            return false
        })

        //  退出账号
        $('.login-out-btn').on('tap', function () {
            module.layerConfirm({
                content: '确定要退出吗？',
                yes: function () {
                    $.ajax({
                        url: ctx + '/api/shop/member/logout.do',
                        success: function (res) {
                            if(res.result == 1){
                                location.replace('./index.html');
                            }else {
                                module.message.error(res.message);
                            }
                        },
                        error: function () {
                            module.message.error('出现错误，请重试！');
                        }
                    })
                }
            });
            return false
        })
    }
})