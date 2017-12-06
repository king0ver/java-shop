/* 
 * 积分商城首页JS
 * @author: chenxiaobo
 * @created time: 2017-10-27
 */
(function() {
    // ---------------------------------全局变量---------------------------------
    // 分页数,即请求数据的 分段数.
    // 默认设置为 全部 分类所需变量
    var page_no = 2;
    var categoryId;
    var url = '/shop/mine/exchange-goods.do?page_no=' + page_no;

    // ---------------------------------END-------------------------------------
    // 绑定 点击刷新页面数据 事件
    // 点击切换导航样式
    // 初步加载数据
    $('div.index-body .category-nav li').each(function() {
        $(this).click(function() {
            // 切换时初始化全局变量
            page_no = 1;
            categoryId = $(this).attr('data-id');
            var container = $('body > div.index-body > div.category-body > ul');
            $(this)
                .addClass('this')
                .siblings()
                .removeClass('this');

            if ($(this).hasClass('all')) {
                // 如果点击的是 全部 分类
                url = '/shop/mine/exchange-goods.do?page_no=' + page_no;
            } else {
                // 如果不是全部分类
                // 定义url
                url =
                    '/shop/mine/exchange-goods.do?cat_id=' +
                    categoryId +
                    '&page_no=' +
                    page_no;
            }
            container.empty();
            $.get(ctx + url, function(response) {
                var dom;
                if (!response || !response.data) {
                    return;
                }
                if (response.data.length === 0) {
                    var tipHTML =
                        '<div class="alert alert-info" role="alert">抱歉，店家还未给此分类添加过商品哦！</div>';
                    container.append(tipHTML);
                    return;
                }
                response.data.forEach(function(item) {
                    dom = $(
                        '<li><a><img></a><div><p class="integral"><span></span><span></span><span class="origin-price"></span></p><p class="name"></p><p><span>0</span>人兑换</p></div></li>'
                    );
                    dom
                        .find('a')
                        .attr(
                            'href',
                            ctx + '/goods-' + item.goods_id + '.html'
                        );
                    dom.find('img').attr('src', item.thumbnail);
                    var pNumber = dom.find('p.integral');
                    $(pNumber.children('span')[1]).text(
                        '￥' +
                            item.exchange_money +
                            '+' +
                            item.exchange_point +
                        '积分'
                    );
                    pNumber.children('span.origin-price').text('原价：' +item.price);
                    dom.find('p.name').text(item.goods_name);
                    // 多少人兑换
                    dom
                        .find('p')
                        .last()
                        .children('span')
                        .text(item.buy_count);
                    container.append(dom);
                });
                page_no++;
            });
        });
    });

    // 滚动加载数据
    $(window).scroll(function() {
        //获取网页的总高度，考虑兼容性所以把Ie支持的documentElement也写了.支持IE8
        var htmlHeight =
            document.body.scrollHeight || document.documentElement.scrollHeight;
        //网页在浏览器中的可视高度，
        var clientHeight = document.documentElement.clientHeight;
        //浏览器滚动条的top位置，
        var scrollTop =
            document.body.scrollTop || document.documentElement.scrollTop;

        if ($('div.index-body .category-nav ul li.this').hasClass('all')) {
            // 如果点击的是 全部 分类
            url = '/shop/mine/exchange-goods.do?page_no=' + page_no;
        } else {
            // 如果不是全部分类
            // 定义url
            url =
                '/shop/mine/exchang-goods.do?cat_id=' +
                categoryId +
                '&page_no=' +
                page_no;
        }

        //通过判断滚动条的top位置与可视网页之和与整个网页的高度是否相等来决定是否加载内容；
        if (scrollTop + clientHeight == htmlHeight) {
            $.get(ctx + url, function(response) {
                var dom;
                var container = $(
                    'body > div.index-body > div.category-body > ul'
                );
                if (!response || !response.data) {
                    return;
                }
                response.data.forEach(function(item) {
                    dom = $(
                        '<li><a><img></a><div><p class="integral"><span></span><span></span><span class="origin-price"></span></p><p class="name"></p><p><span>0</span>人兑换</p></div></li>'
                    );
                    dom
                        .find('a')
                        .attr(
                            'href',
                            ctx + '/goods-' + item.goods_id + '.html'
                        );
                    dom.find('img').attr('src', item.thumbnail);
                    var pNumber = dom.find('p.integral');
                    $(pNumber.children('span')[1]).text(
                        '￥' +
                            item.exchange_money +
                            ' + ' +
                            item.exchange_point +
                            '积分'
                    );
                    pNumber.children('span.origin-price').text('原价:' + item.price);
                    dom.find('p.name').text(item.goods_name);
                    // 多少人兑换
                    dom
                        .find('p')
                        .last()
                        .children('span')
                        .text(item.buy_count);
                    container.append(dom);
                });
                page_no++;
            });
        }
    });

    // 修正积分商城分类Dialog 弹出时机,改为悬浮时才弹出
    var menu = $('.menu');
    menu.each(function() {
        var _this = $(this),
            isIndex = _this.is('.is-index');

        _this.css('overflow', 'hidden');
        isIndex &&
            _this.hover(function(event) {
                var mouseenter = event.type === 'mouseenter';
                _this.css('overflow', mouseenter ? 'inherit' : 'hidden');
            });
    });
})();
