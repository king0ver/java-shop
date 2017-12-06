/**
 * Created by Andste on 2017/4/7.
 */
var Base = {};
(function () {
    jQuery.extend({
        alert: function (message, fn) {
            layer.alert(message || '', function (index) {
                typeof (fn) === 'function' && fn(index);
                layer.close(index);
            });
        },
        confirm: function (message, fn) {
            layer.confirm(message || '确认这个操作吗？', {icon: 3, title: '提示'}, function (index) {
                typeof (fn) === 'function' && fn(index);
                layer.close(index);
            });
        },
        message: {
            success: function (message, fn) {
                layer.msg(message || '成功提示', {icon: 1,time:1000}, function (index) {
                    typeof (fn) === 'function' ? fn(index) : (fn === 'reload' && location.reload());
                });
            },
            error: function (message, fn) {
                layer.msg(message || '错误提示', {icon: 2}, function (index) {
                    typeof (fn) === 'function' && fn(index);
                });
            }
        }
    });

    Base.regExp = {
        //  手机号
        mobile  : /^0?(13[0-9]|15[0-9]|18[0-9]|14[0-9]|17[0-9])[0-9]{8}$/,
        //  电子邮箱
        email   : /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
        //  密码【6-20位】
        password: /^[\@A-Za-z0-9\!\#\$\%\^\&\*\.\~\,]{6,20}$/,
        //  正整数【不包含0】
        integer : /^[1-9]\d*$/,
        //  price
        price   : /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/
    };
})();

$(function () {
    $(".lazy").show().lazyload({ effect : 'fadeIn' });
});