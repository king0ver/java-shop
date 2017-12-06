/**
 * Created by Andste on 2016/12/20.
 */
$(function () {
    var module = new Module();
    init();

    function init() {
        module.navigator.init('评论商品');

        bindEvents();
    }

    function bindEvents() {

        //  评分星星
        $('.content-item').on('tap', '.comment-star', function () {
            var $this  = $(this),
                _input = $this.siblings('input');

            $this.removeClass('off').addClass('on');
            $this.nextAll('i').removeClass('on').addClass('off');
            $this.prevAll('i').removeClass('off').addClass('on');
            _input.val(parseInt($this.index() + 1));
            return false
        });

        //  评级
        $('.grade-comment-content').on('tap', '.comment-grade', function () {
            var $this = $(this),
                _rel  = $this.attr('data-rel');
            $this.siblings('input').val(_rel);
            $this.addClass('selected').siblings('.comment-grade').removeClass('selected');
            return false
        });

        //  提交评论
        $('#save-btn').on('tap', function () {
            var $this = $(this);
            $this[0].disable = true;

            if (!$('#content').val()) {
                module.message.error('评论内容不能为空！');
                $this[0].disable = false;
                return false
            } else {
                $('#goodsComment').ajaxSubmit({
                    url    : ctx + '/api/b2b2c/store-comment-api/addComment.do',
                    type   : 'POST',
                    success: function (res) {
                        if (res.result === 1) {
                            module.message.success('评论成功！', function () {
                                var orderSn = $('#orderSn').val();
                                orderSn
                                    ? location.replace(ctx + '/member/goods-list-comment.html?ordersn=' + orderSn)
                                    : location.replace(ctx + '/member/wait-comment.html')
                                
                            })
                        } else {
                            $this[0].disable = false;
                            module.message.error(res.message);
                        }
                    },
                    error  : function () {
                        $this[0].disable = false;
                        module.message.error('出现错误，请重试！');
                    }
                })
            }
            return false
        })
    }
});