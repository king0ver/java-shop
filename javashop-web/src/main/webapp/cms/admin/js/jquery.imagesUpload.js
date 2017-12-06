/**
 * Created by Andste on 2017/6/26.
 * version 1.0.0
 * 1.0版本未完善图片更换功能
 * 2017-09-30 自定义数据增加下拉选择
 */
//  一些兼容扩展
;
(function () {
    if (!Array.isArray) {
        Array.isArray = function (arg) {
            return Object.prototype.toString.call(arg) === '[object Array]';
        };
    }
    if (!Array.prototype.forEach) {
        Array.prototype.forEach = function (callback, thisArg) {
            var T, k;
            if (this === null) {
                throw new TypeError(' this is null or not defined');
            }
            var O = Object(this),
                len = O.length >>> 0;
            if (typeof callback !== "function") {
                throw new TypeError(callback + ' is not a function');
            }
            if (arguments.length > 1) {
                T = thisArg;
            }
            k = 0;
            while (k < len) {
                var kValue;
                if (k in O) {
                    kValue = O[k];
                    callback.call(T, kValue, k, O);
                }
                k++;
            }
        };
    }
})();
(function ($, window, document, undefined) {
    var ImagesUpload = function (ele, opts) {
        var _this = this;
        this.isDialog = typeof (ele) === 'function';
        this.$element = this.isDialog ? null : ele;
        this.default = {
            host: '',
            api: ctx + '/core/upload.do',
            defaultData: [],
            confirm: function () {
                return null;
            },
            title: '图片上传',
            //  是否可通过点击遮罩关闭
            closeOnClickMask: false,
            //  打开时是否锁定滚动条 x
            lockScroll: true,
            //  关闭之前的回调，返回false则不关闭。x
            beforeClose: null,
            //  关闭之后的回调 x
            afterClose: null,
            fileNumLimit: 10,
            fileSingleSizeLimit: 10,
            //  最大上传线程
            threads: 1,
            //  处理错误信息
            handleError: function (error) {
                alert(error['msg']);
            }
        };
        //  文件上传插件
        this.uploader = undefined;
        //  拖动排序插件
        this.sortable = undefined;
        this.options = $.extend({}, this.default, opts);
        this.default['fileSizeLimit'] = this.options.fileNumLimit * this.options.fileSingleSizeLimit;
        this.options.url = this.options.host + this.options.api;
        this.params = {
            imageTypes: (function () {
                var optionTypes = _this.options.fileTypes;
                return (optionTypes && Array.isArray(optionTypes) && optionTypes.length > 0) ?
                    optionTypes : ['jpg', 'jpeg', 'png', 'bmp', 'gif'];
            })(),
            defaultDataIndex: 0,
            currentFileNum: 0
        };
    };

    /* ImagesUpload.prototype
     ============================================================================ */
    ImagesUpload.prototype = {
        init: function () {
            var maxL = this.options.fileNumLimit;
            if (maxL !== null && (maxL < 1 || typeof maxL !== 'number')) throw new TypeError('fileNumLimit只能为非0正整数！');
            this.__DOM__createAppEle()
                .__DOM__createFileIntroduction()
                .bindEvents();
            return this;
        },
        //  检查默认数据是否符合规则，如果符合规范则处理默认数据
        checkDefaultData: function () {
            var defaultData = this.options.defaultData,
                defaultDataLen = defaultData.length;
            if (!Array.isArray(defaultData)) throw new TypeError('传入默认数据应该为数组！');
            if (defaultDataLen > this.options.fileNumLimit) throw new TypeError('传入默认数据长度超出限制！');
            defaultDataLen > 0 && this.__FUN__defaultDataProcessing();
        },

        /*  事件处理
         ============================================================================ */
        open: function () {
            if (!this.isDialog) return;
            var _this = this;
            this.$mask.addClass('show');
            this.$element.addClass('show');
            setTimeout(function () {
                _this.$mask.addClass('open');
                _this.$element.addClass('open');
            });
        },
        close: function () {
            if (!this.isDialog) return;
            var _beforeClose = this.options.beforeClose,
                _afterClose = this.options.afterClose;
            if (typeof (_beforeClose) === 'function' && _beforeClose() === false) return;
            var _this = this;
            this.$mask.removeClass('open');
            this.$element.removeClass('open');
            this.uploader.destroy();
            setTimeout(function () {
                _this.$mask.remove();
                _this.$element.remove();
            }, 350);
            typeof (_afterClose) === 'function' && _afterClose();
        },
        /**
         * 各个事件绑定
         * @returns {ImagesUpload}
         */
        bindEvents: function () {
            var _this = this;
            //  点击遮罩关闭
            this.options.closeOnClickMask &&
                this.$mask.on('click', function () {
                    _this.close();
                });
            //  点击关闭按钮关闭
            this.$container.on('click', '.__IUD__close', function () {
                _this.close();
            });
            //  点击确认，回调
            this.$container.on('click', '.__footer-btn.confirm', function () {
                _this.__FUN__confirm();
            });
            //  点击取消，关闭
            this.$container.on('click', '.__footer-btn.cancel', function () {
                _this.close();
            });
            
            //  下拉选择绑定
            this.$container.on('click', '.el-select', function (event) {
                event.stopPropagation();
                _this.__DOM__selectClick($(this))
            });
            
            this.$element.on('click', '.el-select-dropdown__item', function (event) {
                _this.__FUN__dropdownItemClick($(this))
            });
            
            $(document).on('click', function () {
                _this.$element.find('.el-select-dropdown');
                _this.$element.find('.el-select').removeClass('is-open')
                     .find('.el-input__icon').removeClass('is-reverse');
                _this.__DOM__dropdownHiden(_this.$element.find('.el-select-dropdown'))
            });

            return this;
        },

        /* 逻辑处理
         ============================================================================ */
        /**
         * 初始化webUploader
         * @private
         */
        __FUN__initWebUploader: function () {
            var _this = this,
                imageTypes = this.params.imageTypes;
            var options = {
                pick: {
                    id: this.$imagesBox.find('.__IUD__upload'),
                    innerHTML: '<i class="__IUD__upload-icon"></i><div class="__IUD__upload-text">将文件拖到此处，或<em>点击上传</em></div>',
                    //  是否开启多选功能，最大可传个数为1则不开启。
                    multiple: this.options.fileNumLimit !== 1
                },
                //  指定Drag And Drop拖拽的容器，如果不指定，则不启动。
                dnd: _this.$imagesBox.find('.__IUD__upload'),
                //  指定监听paste事件的容器，如果不指定，不启用此功能。此功能为通过粘贴来添加截屏的图片。
                paste: document.body,
                //  HTML5方式不可用时，需要加载swf。
                swf: 'https://cdn.staticfile.org/webuploader/0.1.5/Uploader.swf',
                //  服务器地址
                server: this.options.url,
                // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
                disableGlobalDnd: true,
                //  文件个数限制
                fileNumLimit: this.options.fileNumLimit,
                //  文件总大小限制
                fileSizeLimit: this.options.fileSizeLimit * 1024 * 1024 * 100,
                //  单个文件大小限制
                fileSingleSizeLimit: this.options.fileSingleSizeLimit * 1024 * 1024 * 10,
                //  图片类型配置
                accept: {
                    title: 'Images',
                    extensions: imageTypes.join(','),
                    mimeTypes: (function () {
                        var str = '';
                        imageTypes.forEach(function (item) {
                            str += 'image/' + item + ', ';
                        });
                        return str.substr(0, str.length - 1);
                    })()
                },
                //  上传最大并发数
                threads: this.options.threads,
                //  设置为 true 后，不需要手动调用上传，有文件选择即开始上传。
                auto: true,
                //  文件压缩
				resize: false,
				compress: false
            };
            this.uploader = new WebUploader['create'](options);
            this.__FUN__monitorUploaderEvents();
            this.checkDefaultData();
        },

        /**
         * uploader事件监听
         * @private
         */
        __FUN__monitorUploaderEvents: function () {
            var _this = this;

            //  当文件加入列队前
            this.uploader.on('beforeFileQueued', function () {
                //  检查是否超过文件最大个数限制
                _this.params.currentFileNum++;
                if (_this.params.currentFileNum > _this.options.fileNumLimit) {
                    _this.__FUN__uploaderError('Q_EXCEED_NUM_LIMIT');
                    return false;
                }
            });

            //  当文件加入队列
            this.uploader.on('filesQueued', function (files) {
                //  对队列进行排序操作
                _this.uploader.sort(function (a, b) {
                    var a_id = parseInt((/.?_(\d+)/g.exec(a['id']))[1]),
                        b_id = parseInt((/.?_(\d+)/g.exec(b['id']))[1]);
                    return a_id < b_id ? -1 : (a_id > b_id ? 1 : 0);
                });
                _this.__DOM__makeThumb(files);
                _this.__DOM__updateUploadStatus({
                    progressNum: this.options.threads,
                    queueNum: files.length
                });
            });

            //  监听文件开始上传
            this.uploader.on('uploadStart', function (file) {
                // 2017-8-2-陈小博: 加上是否存在的判断.
                if ($this) {
                    var $this = _this.$uploadList.find('#' + file['id']),
                        _top = $this[0].offsetTop;
                    _this.$uploadList.animate({
                        scrollTop: _top - 10
                    }, 50);
                }
            });

            //  监听上传进度
            this.uploader.on('uploadProgress', function (file, percentage) {
                _this.__DOM__uploadProgress(file, percentage);
                _this.__DOM__updateUploadStatus(_this.uploader.getStats());
            });

            //  监听上传成功
            this.uploader.on('uploadSuccess', function (file, response) {
                _this.__DOM__uploadSuccess(file, response);
                _this.__DOM__updateUploadStatus(_this.uploader.getStats(), true);
            });

            //  当所有文件上传结束时
            this.uploader.on('uploadFinished', function () {
                //_this.$uploadList.animate({scrollTop: 0 - 10}, 500);
            });

            //  监听错误消息
            this.uploader.on('error', function (handler) {
                _this.__FUN__uploaderError(handler);
            });

            //  移除文件
            this.$uploadList.on('click', '.__success', function (event) {
                _this.__DOM__removeFile($(this).closest('.__IUD__item'));
                event.stopPropagation();
            });

            //  显示编辑
            this.$uploadList.on('mouseenter mouseleave', 'li', function (event) {
                var $this = $(this),
                    _mask = $this.find('.__mask__'),
                    mouseenter = event.type === 'mouseenter';
                if (_mask.length === 0) return;
                mouseenter ? _mask.removeClass('hidden') : _mask.addClass('hidden');
            });

            //  更换图片
            this.$uploadList.on('click', '.__mask__update', function () {
                var $this = $(this),
                    _item = $this.closest('.__IUD__item');
                //  to do something...
            });

            //  编辑属性
            this.$uploadList.on('click', '.__mask__edit', function () {
                var $this = $(this),
                    _item = $this.closest('.__IUD__item');
                _this.__DOM__eidtItemAttr(_item);
            });

            //  保存编辑
            this.$uploadEdit.on('click', '.__edit-btn.confirm', function () {
                _this.__DOM__confirmEdit();
            });

            //  取消编辑
            this.$uploadEdit.on('click', '.__edit-btn.cancel', function () {
                _this.__DOM__showItemAttr(false);
            });
        },

        /**
         * 处理错误信息
         * @param handler
         * @private
         */
        __FUN__uploaderError: function (handler) {
            var _this = this,
                _error = {
                    code: handler
                };
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
                this.options.handleError(_error);
                this.params.errorTimeOut = true;
                setTimeout(function () {
                    _this.params.errorTimeOut = false;
                }, 100);
            }
            this.params.currentFileNum--;
        },

        /**
         * 初始化Sortable排序插件
         * @private
         */
        __FUN__initSortable: function () {
            var itemEle = this.$uploadList[0];
            this.sortable = Sortable.create(itemEle, {
                animation: 150,
                scroll: true
            });
        },

        /**
         * 确认回调
         * @private
         */
        __FUN__confirm: function () {
            var _this = this;
            var _confirm = _this.options.confirm;
            if(typeof _confirm !== 'function') return;
            var _datas = [],
                imageDataEles = _this.$uploadList.find('.__IUD__item');
            imageDataEles.each(function () {
                var $item = $(this),
                    _tempItem = {},
                    _inputs = $item.find('.item-value');
                _inputs.each(function () {
                    var $this = $(this);
                    _tempItem[$this.attr('name')] = $this.val();
                });
                _datas.push(_tempItem);
            });
            // 这里限制了上传的图片中,只有第一张有用.
            _this.options.fileNumLimit === 1 && (_datas = _datas[0]);
            _confirm(_datas);
            _this.close();
        },
        /**
         * 处理默认数据
         * @private
         */
        __FUN__defaultDataProcessing: function () {
            var _defaultData = this.options.defaultData;
            this.params.dafaultDataIndex = 0;
            this.params.hasDefaultData = (_defaultData && _defaultData.length > 0);
            this.__DOM__makeThumb(_defaultData[this.params.dafaultDataIndex]);
            this.params.currentFileNum = _defaultData.length;
        },
        /**
         * 计算已上传文件个数
         * @private
         */
        __FUN__countUploadedNum: function () {
            return this.$uploadList.find('.__success__').length;
        },
    
        /**
         * 下拉选择项选择
         * @param $this
         * @private
         */
        __FUN__dropdownItemClick: function ($this) {
            var _span = $this.find('span'),
                _text = _span.html(),
                _value = _span.data('value'),
                _name = $this.closest('.el-select-dropdown').data('name');
            var _input = this.$element.find(".el-select[data-name='"+ _name +"']").find('input');
            _input.val(_text);
            _input.closest('.__IUD__input-group').find("input[name='"+ _name +"']").val(_value);
            $this.addClass('selected').siblings().removeClass('selected')
        },

        /* DOM操作相关
         ============================================================================ */
        /**
         * 创建文件预览图
         * @param files
         * @private
         */
        __DOM__makeThumb: function (files) {
            //  如果没有默认数据了，就把标记改成false
            if (!files) {
                this.params.hasDefaultData = false;
                return;
            }
            var _this = this,
                imagesList = this.$uploadList,
                _index = 0,
                _hasDefault = this.params.hasDefaultData;
            var _makeThumb = function (_file) {
                if (!_file) return;
                _this.uploader.makeThumb(_file, function (error, ret) {
                    error ? console.log('生成预览出现错误...') : (function () {
                        __appendThumb(_file['id'], ret);
                        _index++;
                        _makeThumb(files[_index]);
                    })();
                }, 1, 1);
            };

            function __appendThumb(_id, _ret) {
                imagesList.append('\
                            <li class="__IUD__item' + (_hasDefault ? ' __is-default' : '') + '" id="' + _id + '">\
                                <img src="' + _ret + '" alt="">\
                                <span class="__success' + (_hasDefault ? '' : ' hidden') + '"><i></i></span>\
                                ' + (_hasDefault ? '' : '<div class="__mask"><span class="__percentage">准备上传</span><span class="__bar"></span></div>') + '\
                            </li>\
                        ');
            }

            _hasDefault ? (function () {
                var _id = 'CU_FILE_' + _this.params.defaultDataIndex,
                    img_url = files['img_url'];
                __appendThumb(_id, img_url);
                _this.__DOM__uploadSuccess({
                    id: _id
                }, {
                    _raw: img_url
                }, files['customParams']);
            })() : _makeThumb(files[_index]);
        },

        /**
         * 移除文件
         * @param _item
         * @private
         */
        __DOM__removeFile: function (_item) {
            var _this = this,
                _fileId = _item.attr('id'),
                _isDefault = _item.is('.__is-default');
            _item.is('.is-edit') && this.__DOM__showItemAttr(false);
            !_isDefault && this.uploader.removeFile(_fileId);
            _item.addClass('hidden');
            setTimeout(function () {
                _item.remove();
                _this.__DOM__updateUploadStatus(false, true);
            }, 300);
        },

        /**
         * 处理文件上传进度
         * @param file
         * @param percentage
         * @private
         */
        __DOM__uploadProgress: function (file, percentage) {
            var _item = this.$uploadList.find("#" + file['id']),
                per = (percentage * 100).toFixed(0) + '%';
            _item.find(".__percentage").html(per);
            _item.find('.__bar').css('width', per);
        },
        /**
         * 当文件上传成功
         * @param file
         * @param response
         * @param customParams
         * @private
         */
        __DOM__uploadSuccess: function (file, response, customParams) {
            var _this = this,
                _item = this.$uploadList.find("#" + file['id']),
                _mask = _item.find('.__mask'),
                _customParams = this.options.customParams,
                _hasDefaultData = this.params.hasDefaultData;
            _item.addClass('__success__');
            // 2017-8-2-陈小博: 加上是否存在的判断.
            response && _item.append('<input type="hidden" class="item-value" name="image_url" value="' + response['_raw'] + '" />');

            //  插入自定义属性input【这时候没有值】
            Array.isArray(_customParams) && _customParams.length > 0 && (function () {
                _customParams.forEach(function (_item_) {
                    var _value = _item_['value'];
                    customParams && customParams.forEach(function (__item__) {
                        _item_['name'] === __item__['name'] && (_value = __item__['value']);
                    });
                    
                    // 2017-09-29 增加下拉选择模式 __by: Andste
                    var options;
                    _item_.type === 'select' && _item_.options && (options = JSON.stringify(_item_.options));
                    _item.append('<input type="hidden" class="item-value __custom" data-title="' + _item_['label'] + '" data-type="' + (_item_['type'] || '') + '" '+ (options && "data-options=" + options) +' name="' + _item_['name'] + '" value="' + (_value || '') + '" />');
                });
            })();
            var _time = _hasDefaultData ? 0 : 500;
            setTimeout(function () {
                _mask.find('.__bar').remove();
                _mask.find('.__percentage').remove();
                _mask.addClass('hidden');
                setTimeout(function () {
                    _item.find('.__success').removeClass('hidden');
                    _item.find('.__mask').remove();
                    // '<i class="__mask__update" title="更换图片"></i>';
                    _item.append('\
                        <div class="__mask__ hidden">\
                            <i class="__mask__edit" title="编辑属性"></i>\
                        </div>\
                        ');
                    if (_hasDefaultData) {
                        _this.params.defaultDataIndex++;
                        _this.__DOM__makeThumb(_this.options.defaultData[_this.params.defaultDataIndex]);
                        _this.__DOM__updateUploadStatus(false, true);
                    }
                }, _time);
            }, _time);
        },

        /**
         * 属性编辑
         * @param item
         * @private
         */
        __DOM__eidtItemAttr: function (item) {
            var $element = this.$element,
                _eidtEle = this.$uploadEdit,
                _inputs = item.find('.__custom'),
                _temp = [];
            _inputs.each(function () {
                var $this = $(this);
                _temp.push({
                    label: $this.data('title'),
                    type: $this.data('type'),
                    options: $this.data('options'),
                    name: $this.attr('name'),
                    value: $this.val(),
                    text: $this.data('text')
                });
            });
            item.addClass('is-edit').siblings().removeClass('is-edit');
            var _html = '';
            $element.find('.el-select-dropdown').remove();
            _temp.forEach(function (_item_) {
                if (_item_['type'] === 'hidden') return;
                var is_select = _item_['type'] === 'select' && _item_['options'];
                var _selectDom = (function () {
                    if(!is_select) return '';
                    var _text = '';
                    _item_['options'].forEach(function (__) {
                        if(__.value === _item_['value']) _text = __.text
                    });
                    return '\
                        <div class="el-select" data-name="'+ _item_['name'] +'">\
                            <div class="el-input">\
                                <i class="el-input__icon el-icon-caret-top"></i>\
                                <input placeholder="请选择" readonly autocomplete="off" class="el-input__inner" value="' + _text + '">\
                            </div>\
                        </div>\
                        '
                })();
                _html += '<div class="__IUD__input-group">\
                    <div class="__IUD__input-group-prepend">' + _item_['label'] + '</div>\
                        <input class="__IUD__input-group-input" '+ (is_select ? 'type="hidden"' : '') +' name="' + _item_['name'] + '" value="' + _item_['value'] + '" placeholder="请输入内容">\
                       '+ _selectDom +'\
                    </div>';
    
                if(!is_select) return;
                var _optionsEle = (function () {
                    var __ = '';
                    _item_['options'].forEach(function (item) {
                        __ += '<li class="el-select-dropdown__item'+ (_item_['value'] === item.value ? ' selected' : '') +'"><span data-value="'+ item.value +'">'+ item.text +'</span></li>'
                    });
                    return __;
                })();
                $element.append('\
                 <div class="el-select-dropdown el-select-'+ _item_['name'] +'" data-name="'+ _item_['name'] +'" style="width: 210px; transform-origin: center top 0; z-index: 19941025; position: absolute; top: 95px; left: 208px;">\
                    <div class="el-scrollbar" style="">\
                       <div class="el-select-dropdown__wrap el-scrollbar__wrap el-scrollbar__wrap--hidden-default">\
                           <ul class="el-scrollbar__view el-select-dropdown__list" style="position: relative;">\
                           '+ _optionsEle +'\
                           </ul>\
                       </div>\
                    </div>\
                 </div>');
            });
            _eidtEle.find('.__inner').html(_html);
            this.__DOM__showItemAttr(true);
        },
        /**
         * 是否显示属性编辑框
         * @param hidden
         * @private
         */
        __DOM__showItemAttr: function (hidden) {
            var _this = this;
            hidden ? (function () {
                _this.$uploadEdit.removeClass('hidden');
                setTimeout(function () {
                    _this.$uploadEdit.addClass('visible');
                });
            })() : (function () {
                _this.$uploadEdit.removeClass('visible');
                setTimeout(function () {
                    _this.$uploadEdit.addClass('hidden');
                }, 300);
                _this.$uploadList.find('.is-edit').removeClass('is-edit');
            })();
        },
        /**
         * 确认编辑
         * @private
         */
        __DOM__confirmEdit: function () {
            var _this = this,
                inputs = this.$uploadList.find('.is-edit').find('.__custom');
            inputs.each(function () {
                var $this = $(this);
                $this.val(_this.$uploadEdit.find("input[name=" + $this.attr('name') + "]").val());
            });
            this.__DOM__showItemAttr(false);
        },

        /**
         * 创建主体
         * @returns {ImagesUpload}
         * @private
         */
        __DOM__createAppEle: function () {
            var info = {
                imagesType: this.params.imageTypes.join('/'),
                singleSizeLimit: this.options.fileSingleSizeLimit * 1024
            };
            var _ele = '\
                <div id="__IUD__">\
                    <div class="__IUD__controller">\
                        <div class="__IUD__inner">\
                            <div class="__IUD__title">\
                                <h2>' + this.options.title + '</h2>\
                                <i class="__IUD__close"></i>\
                            </div>\
                            <div class="__IUD__body">\
                                <div class="__IUD__images-box">\
                                    <div class="__IUD__images">\
                                        <div class="__IUD__upload-edit hidden">\
                                            <div class="__IUD__upload-edit-body">\
                                                <div class="__inner"></div>\
                                            </div>\
                                            <div class="__IUD__upload-edit-footer">\
                                                <button type="button" class="__edit-btn confirm">保存</button>\
                                                <button type="button" class="__edit-btn cancel">取消</button>\
                                            </div>\
                                        </div>\
                                        <div class="__IUD__upload-body">\
                                            <div class="__IUD__upload"></div>\
                                            <div class="__IUD__upload-tip">只能上传 ' + info.imagesType + ' 文件，且每张不超过' + info.singleSizeLimit + 'kb</div>\
                                        </div>\
                                        <div class="__IUD__upload-list"></div>\
                                    </div>\
                                </div>\
                            </div>\
                            <div class="__IUD__footer">\
                                <div class="__IUD__footer_s_info">\
                                    <span>最多可上传文件个数: <em>' + (this.options.fileNumLimit || '无限制') + '</em></span>\
                                    <span>已上传文件个数: <em class="__IUD__uploadedCount">0</em></span>\
                                    <span>等待上传: <em class="__IUD__waitUploadCount">0</em></span>\
                                </div>\
                                <button class="__footer-btn confirm" type="button">确认</button>\
                                <button class="__footer-btn cancel" type="button">取消</button>\
                            </div>\
                        </div>\
                    </div>\
                </div>\
                <div id="__IUD__mask"></div>';
            //  判断调用方式，如果为dialog，则弹框。
            this.isDialog ? (function () {
                $(_ele).appendTo($('body'));
            })() : this.$element[0].innerHTML = _ele;
            //  转移控制权哈哈哈
            this.$element = $('#__IUD__');
            this.$mask = $("#__IUD__mask");
            this.$container = this.$element.find('.__IUD__inner');
            this.$imagesBox = this.$element.find('.__IUD__images');
            this.$uploadEdit = this.$element.find('.__IUD__upload-edit');
            this.$uploadList = this.$element.find('.__IUD__upload-list');
            return this;
        },
        /**
         * 各个文件引入
         * @returns {ImagesUpload}
         * @private
         */
        __DOM__createFileIntroduction: function () {
            var _this = this;
            $('#imagesUploadCss').length === 0 && (function () {
                var styleEle = document.createElement('link');
                styleEle.id = 'imagesUploadCss';
                styleEle.type = 'text/css';
                styleEle.rel = 'stylesheet';
                styleEle.href = ctx + '/cms/admin/css/jquery.imagesUpload.css';
                document.head.appendChild(styleEle);
            })();

            //  加载webuploader
            $.ajax({
                url: ctx + '/selector/js/webuploader.min.js',
                dataType: 'script',
                proxy: false,
                success: function (res, status) {
                    if (status !== 'success') {
                        throw new TypeError('webuploader加载失败。。。');
                    }
                    _this.__FUN__initWebUploader();
                }
            });

            //  加载Sortable排序插件
            $.ajax({
                url: '//cdnjs.cloudflare.com/ajax/libs/Sortable/1.6.0/Sortable.min.js',
                dataType: 'script',
                proxy: false,
                success: function (res, status) {
                    if (status !== 'success') {
                        throw new TypeError('Sortable加载失败。。。');
                    }
                    _this.__FUN__initSortable();
                }
            });

            return this;
        },
        /**
         * 更新底部信息栏，顺便给当前文件个数做标记
         * @param status
         * @param coutNum
         * @private
         */
        __DOM__updateUploadStatus: function (status, coutNum) {
            this.params.currentFileNum = this.__FUN__countUploadedNum();
            coutNum && this.$element.find('.__IUD__uploadedCount').html(this.params.currentFileNum);
            status && this.$element.find('.__IUD__waitUploadCount').html(status['queueNum']);
        },
    
        /**
         * 下拉选择点击
         * @param $this
         * @private
         */
        __DOM__selectClick: function ($this) {
            var $element = this.$element, _is_open = $this.is('.is-open'), _name = $this.data('name');
            var selectDropdown = $element.find('.el-select-' + _name);
            //  重置同级下的选择状态
            $this.closest('.__IUD__input-group').siblings().find('.el-select').removeClass('is-open')
                 .find('.el-input__icon').removeClass('is-reverse');
            this.__DOM__dropdownHiden(selectDropdown.siblings('.el-select-dropdown'));
            
            //  计算偏移量
            var $thisOffset = $this.offset(), $elementOffset = this.$element.offset();
            var _top = $thisOffset.top - $elementOffset.top, _left = $thisOffset.left - $elementOffset.left;
            selectDropdown.css({ top: _top + 35, left: _left });
    
            //  切换当前选择状态
            if(!_is_open) {
                $this.addClass('is-open')
                     .find('.el-input__icon').addClass('is-reverse');
                selectDropdown.addClass('el-select-dropdown-show');
            }else {
                $this.removeClass('is-open')
                     .find('.el-input__icon').removeClass('is-reverse');
                this.__DOM__dropdownHiden(selectDropdown)
            }
        },
        
        //  隐藏下拉选择框
        __DOM__dropdownHiden: function (ele) {
            ele.addClass('el-select-dropdown-hide');
            setTimeout(function () {
                ele.removeClass('el-select-dropdown-show');
                ele.removeClass('el-select-dropdown-hide');
            }, 300)
        }
    };
    /*
     ============================================================================ */
    $.extend({
        ImagesUpload: function (options) {
            var imagesUpload = new ImagesUpload(this, options);
            imagesUpload.init().open();
            return imagesUpload;
        }
    });
})(jQuery, window, document);
