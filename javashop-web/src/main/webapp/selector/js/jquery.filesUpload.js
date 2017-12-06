/**
 * Created by Andste on 2017/6/30.
 * version 1.0.0
 * 2017-06-31 00:00:00 1.0.0 此版本只支持单选，并且功能简洁
 * 2017-07-16 23:03:39 1.0.1 此版本增加长传成功后图片展示、默认数据处理，支持修改
 * 201-11-1: 
 */

(function($, window, document, undefined) {
    var FilesUpload = function(ele, opts) {
        // 控制是否有预览
        // 默认是true
        this.preview = true;
        if (opts.hasOwnProperty('preview')) {
            this.preview = opts.preview;
        }
        this.$element = ele;
        this.default = {
            host: ctx,
            api: '/core/upload.do',
            success: function(res) {
                console.log(res);
            },
            fileNumLimit: 1,
            fileSingleSizeLimit: 3,
            //  最大上传线程
            threads: 1,
            //  处理错误信息
            error: function(error) {
                alert(error['msg']);
            }
        };
        //  文件上传插件
        this.uploader = undefined;
        this.options = $.extend({}, this.default, opts);
        this.options.url = this.options.host + this.options.api;
        this.params = {
            errorTimeOut: false
        };
    };

    /* FilesUpload.prototype
     ============================================================================ */
    FilesUpload.prototype = {
        init: function() {
            //var maxL = this.options.fileNumLimit;
            //if (maxL !== null && (maxL < 1 || typeof maxL !== 'number' )) throw new TypeError('fileNumLimit只能为非0正整数！');
            this.__DOM__linkOutFiles();
            this.$element.css({
                position: 'relative',
                display: 'block'
            });

            return this;
        },

        /**
         * 处理默认数据
         * @private
         */
        __FUN__handleDefault: function() {
            var _defaultData = this.options['defaultData'];
            if (_defaultData && _defaultData.length > 1) {
                throw TypeError('暂只支持单图上传...');
            }
            if (
                _defaultData &&
                _defaultData[0] &&
                Object.prototype.toString.call(_defaultData) ===
                    '[object Array]'
            ) {
                this.__DOM__makeImagePreview(_defaultData[0]);
            }
        },

        /**
         * 初始化WebUploader
         * @private
         */
        __FUN__initWebUploader: function() {
            var options = {
                pick: {
                    id: this.$element,
                    innerHTML:
                        '<span>上传<i class="el-icon-upload el-icon--right"></i></span>',
                    //  是否开启多选功能，最大可传个数为1则不开启。
                    //  multiple : this.options.fileNumLimit !== 1
                    multiple: false
                },
                //  指定Drag And Drop拖拽的容器，如果不指定，则不启动。
                dnd: this.$element,
                //  指定监听paste事件的容器，如果不指定，不启用此功能。此功能为通过粘贴来添加截屏的图片。
                paste: document.body,
                //  HTML5方式不可用时，需要加载swf。
                swf:
                    'https://cdn.staticfile.org/webuploader/0.1.5/Uploader.swf',
                //  服务器地址
                server: this.options.url,
                // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
                disableGlobalDnd: true,
                //  文件个数限制
                fileNumLimit: this.options.fileNumLimit,
                //  文件总大小限制
                fileSizeLimit: this.options.fileSizeLimit * 1024 * 1024,
                //  单个文件大小限制
                fileSingleSizeLimit: this.options.fileSingleSizeLimit * 1024 * 1024,
                //  限制文件类型
				accept: {
					title: 'Images',
					extensions: ['jpg', 'jpeg', 'png'].join(','),
					mimeTypes: (function () {
						var str = '';
						['jpg', 'jpeg', 'png'].forEach(function (item) {
							str += 'image/' + item + ', ';
						});
						return str.substr(0, str.length - 1);
					})()
				},
                //  上传最大并发数
                threads: this.options.threads,
                //  设置为 true 后，不需要手动调用上传，有文件选择即开始上传。
                auto: true
            };
            this.uploader = new WebUploader['create'](options);
            //如果修改图片时
            if (
                this.preview &&
                !this.$element.siblings('img').hasClass('temp-preview-image')
            ) {
                this.$element.append(
                    '<span class="placeholdermap"> <img class="__image_placeholdermap"></span>'
                );
            }

            // 定时器是因为文件上传插件未能正确的设置 可点击div 的宽高.
            // 导致可点击区域非常小.
            // So,这里动态重新设置.
            if (this.$element) {
                var lastDiv = this.$element.find('div').last();
                if (lastDiv.length === 1) {
                    lastDiv.css({ width: '58px', height: '26px' });
                }
            }

            this.__FUN__handleDefault();
            this.__FUN__monitorUploaderEvents();
        },
        /**
         * uploader事件监听
         * @private
         */
        __FUN__monitorUploaderEvents: function() {
            var _this = this;
            //  当文件加入列队前
            this.uploader.on('beforeFileQueued', function() {
                return null;
            });

            //  当文件加入队列
            this.uploader.on('filesQueued', function(files) {
                //  对队列进行排序操作
                _this.uploader.sort(function(a, b) {
                    var a_id = parseInt(/.?_(\d+)/g.exec(a['id'])[1]),
                        b_id = parseInt(/.?_(\d+)/g.exec(b['id'])[1]);
                    return a_id < b_id ? -1 : a_id > b_id ? 1 : 0;
                });
                _this.__DOM__makeFileItem(files);
            });

            //  监听文件开始上传
            this.uploader.on('uploadStart', function() {});

            //  监听上传成功
            this.uploader.on('uploadSuccess', function(file, response) {
                var _raw = response._raw;
                this.reset();
                _this.__DOM__makeImagePreview(_raw);
                var success = _this.options.success;
                typeof success === 'function' && success(_raw, _this.$element);
            });

            //  当所有文件上传结束时
            this.uploader.on('uploadFinished', function() {});

            //  监听错误消息
            this.uploader.on('error', function(handler) {
                _this.__FUN__uploaderError(handler);
            });
        },
        /**
         * 处理错误信息
         * @param handler
         * @private
         */
        __FUN__uploaderError: function(handler) {
            var _this = this,
                _error = { code: handler };
            switch (handler) {
                case 'F_EXCEED_SIZE':
                    _error['msg'] = '文件大小超出限制！';
                    break;
                case 'F_DUPLICATE':
                    _error['msg'] = '文件重复！';
                    break;
                case 'Q_EXCEED_NUM_LIMIT':
                    _error['msg'] = '文件数量超出限制！';
                    break;
                case 'Q_EXCEED_SIZE_LIMIT':
                    _error['msg'] = '文件总大小超出限制！';
                    break;
                case 'Q_TYPE_DENIED':
                    _error['msg'] = '所选文件类型不在范围内！';
                    break;
            }
            //  优化错误信息抛出频率，100毫秒内置抛出一次错误信息。
            //  预防同时选择多个文件出现反复抛出同样错误。
            if (!this.params.errorTimeOut) {
                this.options.error(_error);
                this.params.errorTimeOut = true;
                setTimeout(function() {
                    _this.params.errorTimeOut = false;
                }, 100);
            }
        },

        /**
         * css文件引入
         * @returns {FilesUpload}
         * @private
         */
        __DOM__linkOutFiles: function() {
            var _this = this;
            $('#filesUploadCss').length === 0 &&
                (function() {
                    var styleEle = document.createElement('link');
                    styleEle.id = 'filesUploadCss';
                    styleEle.type = 'text/css';
                    styleEle.rel = 'stylesheet';
                    styleEle.href =
                        ctx + '/selector/css/jquery.filesUpload.css';
                    document.head.appendChild(styleEle);
                })();

            //  加载webuploader
            $.ajax({
                url: ctx + '/selector/js/webuploader.min.js',
                dataType: 'script',
                proxy: false,
                success: function(res, status) {
                    if (status !== 'success')
                        throw new TypeError('webuploader加载失败。。。');
                    _this.__FUN__initWebUploader();
                }
            });

            return this;
        },

        __DOM__setDefaultImage: function() {},
        /**
         * 添加到展示列表
         * @param files
         * @private
         */
        __DOM__makeFileItem: function(files) {
            //  console.log(files)
        },
        /**
         * 上传成功后展示预览图，修改则替换掉
         * @param url
         * @private
         */
        __DOM__makeImagePreview: function(url) {
            // 如果用户没有开启preview,直接跳出
            if (!this.preview) {
                return;
            }
            if (this.$element.find('img.__image_placeholdermap').length === 0) {
                this.$element.append(
                    '<span class="placeholdermap"> <img class="__image_placeholdermap"></span>'
                );
            }
            
            this.$element.find('img.__image_placeholdermap').attr('src', url);
            this.$element.siblings('img.temp-preview-image').remove();
        }
    };
    /*
     ============================================================================ */
    $.fn.FilesUpload = function(options) {
        return new FilesUpload(this, options).init();
    };
})(jQuery, window, document);
