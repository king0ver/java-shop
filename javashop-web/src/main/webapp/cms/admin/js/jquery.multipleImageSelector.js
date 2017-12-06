(function($) {
    $.MultipleImageSelector = function(options) {
        var ele = this;
        return new Selector(ele, options);
    };

    var Selector = function(container, settings) {
        // 2017-8-2-陈小博: 把图片编辑器换成规范的.
        imagesUpload = $.ImagesUpload({
            //  由于不走$.ajax，所以需要手动指定。
            host: JavashopProxy.gateway(),
            //  可选文件类型，默认为 ['jpg', 'jpeg', 'png', 'bmp', 'gif']
            fileTypes: ['jpg', 'jpeg', 'png'],
            //  最大文件可传个数，默认10个。如果设置为1，回调中会返回一个object。
            fileNumLimit: settings.fileNumLimit,
            //  单个文件最大容量，单位：MB
            fileSingleSizeLimit: 1,
            //  最大上传线程，默认为1【建议设为1】
            threads: 1,
            //  自定义参数，如果设置value值，则默认值统为设置的value
            //  label: 文字描述
            //  type : hidden为隐藏输入框，不隐藏不传
            //  name ：input的name属性
            //  value：input默认值
            customParams: [
                {
                    label: '链接类型',
                    name: 'op_type',
                    type: 'select',
                    options: [
                        { text: '无操作', value: 'none' },
                        { text: '连接地址', value: 'link' },
                        { text: '关键字', value: 'keyword' },
                        { text: '商品编号', value: 'goods-sn' },
                        //						{text: '店铺编号', value: 'shop-sn'},
                        { text: '商品分类', value: 'goods-cat' }
                    ],
                    value: 'none'
                },
                {
                    label: '链接值',
                    name: 'op_value'
                }
                //				{
                //					label: '图片描述',
                //					name: 'op_detail'
                //				}
            ],

            //  默认数据，根据上面自定义的name和value传入默认值
            defaultData: settings.defaultvalue,
            //  处理错误信息，默认为弹框显示，错误信息为error.msg
            handleError: function(error) {
                if (error.msg === '文件数量超出限制！') {
                    $.error('文件数量超出限制, 请先删除存在的文件');
                }
            },
            //  确认回调，如果最大上传个数为1 返回单个object，否则为数组
            confirm: function(data) {
                settings.confirm(data);
                imagesUpload.close();
            }
        });
    };

    $('.app-push-container .select-goods-button').click(function() {
        $.GoodsAdminSelector({
            maxLength: 1, //可选择商品数量
            confirm: function(goods) {
                // 确定之后的回调函数
                // goods 是用户选择的商品数据.
                // 当maxLength为1时,goods为一个对象;>1时,goods为对象数组
            }
        });
    });
})(jQuery);
