// 2017-9-6-陈小博: 商品规格sku的动态筛选

(function($) {
    // ---------------------------------全局作用域下的变量-------------------------------------
    var skuRender = {
        DOM: undefined, // skuRender作用域内的DOM
        refresh: null //存储用户传进来的回调函数的引用
    };
    // ------------源数据------------
    // skuList数据
    var specListData;
    // var specListData = [{ "price": 23, "weight": 13, "quantity": 0, "cost": 1, "sn": 123, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XXS", "spec_value_id": 10, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 1 }, { "price": 23, "weight": 23, "quantity": 0, "cost": 2, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XXS", "spec_value_id": 10, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 2 }, { "price": 23, "weight": 23, "quantity": 0, "cost": 3, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "S", "spec_value_id": 12, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 3 }, { "price": 23, "weight": 23, "quantity": 0, "cost": 4, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "S", "spec_value_id": 12, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 4 }, { "price": 23, "weight": 23, "quantity": 0, "cost": 5, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "M", "spec_value_id": 13, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 5 }, { "price": 23, "weight": 23, "quantity": 0, "cost": 6, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "M", "spec_value_id": 13, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 6 }, { "price": 23, "weight": 23, "quantity": 0, "cost": 734, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XS", "spec_value_id": 11, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 7 }, { "price": 23, "weight": 23, "quantity": 0, "cost": 52314, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XS", "spec_value_id": 11, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 8 }, { "price": 23, "weight": 23, "quantity": 0, "cost": 23, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XXS", "spec_value_id": 10, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 9 }, { "price": 23, "weight": 23, "quantity": 0, "cost": 34, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XXS", "spec_value_id": 10, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 10 }, { "price": 23, "weight": 23, "quantity": 0, "cost": 45, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "S", "spec_value_id": 12, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 11 }, { "price": 23, "weight": 23, "quantity": 0, "cost": 23, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "S", "spec_value_id": 12, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 12 }, { "price": 1, "weight": 4, "quantity": 0, "cost": 1, "sn": 2, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "M", "spec_value_id": 13, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 13 }, { "price": 2, "weight": 3, "quantity": 0, "cost": 2, "sn": 3, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "M", "spec_value_id": 13, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 14 }, { "price": 4, "weight": 3, "quantity": 0, "cost": 3, "sn": 2, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XS", "spec_value_id": 11, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 15 }, { "price": 5, "weight": 2, "quantity": 0, "cost": 6, "sn": 1, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XS", "spec_value_id": 11, "spec_type": 0, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 0, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 0, "spec_image": "http" }], "sku_id": 16 }];
    // specListData = [{ "price": 23, "weight": 13, "quantity": 2, "cost": 1, "sn": 123, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XXS", "spec_value_id": 12, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 1 }, { "price": 23, "weight": 23, "quantity": 2, "cost": 2, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XXS", "spec_value_id": 12, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 2 }, { "price": 23, "weight": 23, "quantity": 2, "cost": 3, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "S", "spec_value_id": 12, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 3 }, { "price": 23, "weight": 23, "quantity": 2, "cost": 4, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "S", "spec_value_id": 12, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 4 }, { "price": 23, "weight": 23, "quantity": 2, "cost": 5, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "M", "spec_value_id": 13, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 5 }, { "price": 23, "weight": 23, "quantity": 2, "cost": 6, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "M", "spec_value_id": 13, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 6 }, { "price": 23, "weight": 23, "quantity": 2, "cost": 734, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XS", "spec_value_id": 11, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 7 }, { "price": 23, "weight": 23, "quantity": 2, "cost": 52314, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "黄色", "spec_value_id": 41, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XS", "spec_value_id": 11, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 8 }, { "price": 23, "weight": 23, "quantity": 2, "cost": 23, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XXS", "spec_value_id": 12, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 9 }, { "price": 23, "weight": 23, "quantity": 2, "cost": 34, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XXS", "spec_value_id": 12, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 10 }, { "price": 23, "weight": 23, "quantity": 2, "cost": 45, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "S", "spec_value_id": 12, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 11 }, { "price": 23, "weight": 23, "quantity": 2, "cost": 23, "sn": 23, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "S", "spec_value_id": 12, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 12 }, { "price": 1, "weight": 4, "quantity": 2, "cost": 1, "sn": 2, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "M", "spec_value_id": 13, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 13 }, { "price": 2, "weight": 3, "quantity": 2, "cost": 2, "sn": 3, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "M", "spec_value_id": 13, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 14 }, { "price": 4, "weight": 3, "quantity": 2, "cost": 3, "sn": 2, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XS", "spec_value_id": 11, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "16GB", "spec_value_id": 62, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 15 }, { "price": 5, "weight": 2, "quantity": 2, "cost": 6, "sn": 1, "specList": [{ "spec_name": "颜色", "spec_id": 1, "spec_value": "绿色", "spec_value_id": 42, "spec_type": 1, "spec_image": "http" }, { "spec_name": "尺寸", "spec_id": 2, "spec_value": "XS", "spec_value_id": 11, "spec_type": 2, "spec_image": "http" }, { "spec_name": "容量", "spec_id": 3, "spec_value": "2GB", "spec_value_id": 59, "spec_type": 2, "spec_image": "http" }, { "spec_name": "选择尺码", "spec_id": 4, "spec_value": "36", "spec_value_id": 67, "spec_type": 2, "spec_image": "http" }], "sku_id": 16 }];

    // ------------END-----------

    // 商品可选规格数据
    // var list = [
    //     {
    //         spec_name: "颜色",
    //         item_list:
    //         [
    //             {
    //                 item_name: "绿色", item_id: 1
    //             },
    //             {
    //                 item_name: "黄色", item_id: 2
    //             }
    //         ]
    //     }
    // ];
    var list;
    var skuHTML =
        '<style>.goods-sku .sku{display:inline-block;border:2px solid #e2e1e3;font-size:12px;margin:5px;height:30px;min-width:50px;text-align:center;line-height:30px;position:relative;color:black;font-weight:bold}.goods-sku .sku:hover{cursor:pointer}.goods-sku .sku .right{background:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAMCAMAAABhq6zVAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAJUExURUxpcf8AN////7f4NBoAAAABdFJOUwBA5thmAAAAMUlEQVQI103MAQ4AMAQEQev/j66i6YrEXIKIX9jY2NjYyDmhZnlCo5rdyWvebfYDVAcSmABbA7WD+QAAAABJRU5ErkJggg==);position:absolute;right:0;bottom:0;display:none;width:12px;height:12px}.goods-sku .sku.goods-selected .right{display:inline-block}.goods-sku .sku-body{width:100%}.goods-sku .sku-body .goods-spec-name{font-size:12px;color:grey; float:left;margin-top:1rem;}.goods-sku .goods-selected{border:2px solid #f00}.goods-sku .disabled{border:2px dashed #b8b7bd;color:#b8b7bd}.goods-sku span.disabled:hover{cursor:not-allowed}.goods-sku .sku-hover{border:2px solid #f00}.goods-sku .sku-value-container{width:80%;margin-left:18%;}</style>' +
        '<!-- 商品规格部分 -->\
        <div class="pro-list goods-sku">\
            <div class="goods-sku-model" style="display: none;">\
                <!-- 规格模板 -->\
                <div>\
                    <span class="goods-spec-name">规格名模板</span>\
                    <div class="sku-value-container">\
                        <span class="sku" attr_id="绿色模板">规格值模板</span>\
                    </div>\
                </div>\
            </div>\
            <div class="sku-body">\
            </div>\
        </div>';

    var skuDOM = $(skuHTML);
    // 根据数据源产生的数据
    // var keys = [
    //     ['黄色', '绿色'],
    //     ['XXS', 'XS', 'S'],
    //     ['36'],
    //     ['4GB', '16GB']
    // ]
    var keys;
    // 数据对象. 重要! 只在初始化时生成一次,后面用户选取不用的规格值时,直接查询就ok
    var dataObj = {};
    // 把DOM的引用传出去.
    skuRender.DOM = skuDOM;
    //保存最后的组合结果信息
    var SKUResult = {};
    var goodsID;

    // ----------------------------数据转换方法----------------------------
    // 获取skuList数据
    var getRemoteData = function(data) {
        // 如果data存在则说明: 不需要高大上的选择功能,
        // 只要展示sku的功能
        // 不需要点击的功能了.
        if (data) {
            specListData = data;
            list = transformDefaultData(specListData);
            keys = generateKey(list);
            fillUpDataObj();
            generateSkuHTML(list);

            initSKU();
            bindClickEvent();
        } else {
            $.get(ctx + '/shop/goods/' + goodsID + '/skus.do', function(
                response
            ) {
                // 只有一条数据且不存在skuList数据的话,
                // 把得来的数据返回回去.
                // return;
                if (response.length === 1 && !response[0].specList) {
                    skuRender.refresh({
                        sku_id: response[0].sku_id,
                        price: response[0].price,
                        enable_quantity: response[0].enable_quantity
                    });
                    return;
                }

                // 遍历检测数据的有效性.
                // 如果不存在有效的specList. 从源数据中删除掉
                response.forEach(function(skuObj, index) {
                    // 不存在或者为空数组
                    if (!skuObj.specList || skuObj.specList.length === 0) {
                        response.splice(index, 1);
                    }
                });

                specListData = response;
                list = transformDefaultData(specListData);
                keys = generateKey(list);
                fillUpDataObj();
                generateSkuHTML(list);

                initSKU();
                bindClickEvent();

                // 开始初始化
                setDefaultSku();
            });
        }
    };

    // 把skuList结构的数据,转换成规格向的数据
    var transformDefaultData = function(data) {
        var specDataObj = {};
        var tempObj;
        data.forEach(function(skuObj) {
            skuObj.specList.forEach(function(spec) {
                if (specDataObj.hasOwnProperty(spec.spec_name)) {
                    if (
                        !specDataObj[spec.spec_name].hasOwnProperty(
                            spec.spec_value
                        )
                    ) {
                        specDataObj[spec.spec_name][spec.spec_value] =
                            spec.spec_value_id;
                    }
                } else {
                    tempObj = {};
                    tempObj[spec.spec_value] = spec.spec_value_id;
                    specDataObj[spec.spec_name] = tempObj;
                }
            });
        });

        // 继续转换数据成最终需要
        var transformDataAgain = function(data) {
            var tempArray = [];
            for (var specName in data) {
                if (!specName) {
                    continue;
                }
                var tempObj = {
                    spec_name: specName,
                    item_list: []
                };
                var specObj;
                for (specValue in data[specName]) {
                    if (!specValue) {
                        continue;
                    }
                    specObj = {
                        item_name: specValue,
                        item_id: data[specName][specValue]
                    };
                    tempObj.item_list.push(specObj);
                }
                tempArray.push(tempObj);
            }
            return tempArray;
        };

        return transformDataAgain(specDataObj);
    };

    // 根据list数据生成keys数据
    var generateKey = function(data) {
        var keyArray = [];
        var tempArray;
        data.forEach(function(tempObj) {
            tempArray = [];
            tempObj.item_list.forEach(function(specObj) {
                tempArray.push(specObj.item_name);
            });
            keyArray.push(tempArray);
        });
        return keyArray;
    };

    // -------------------------END----------------------

    //根据数据源 产生 sku算法需要的数据结构
    // 填充dataObj
    var fillUpDataObj = function() {
        specListData.forEach(function(item) {
            var key = item.specList
                .map(function(sku) {
                    return sku.spec_value;
                })
                .join(';');

            dataObj[key] = {
                price: item.price,
                count: item.enable_quantity
            };
        });
    };

    // 根据源数据动态生成商品的 sku HTML,并插入对应位置
    var generateSkuHTML = function(data) {
        var clonedSkuDom;
        data.forEach(function(goodsSku) {
            clonedSkuDom = skuDOM.find('.goods-sku-model > div').clone(true);
            clonedSkuDom.find('.goods-spec-name').text(goodsSku.spec_name);
            var clonedSku;
            goodsSku.item_list.forEach(function(value) {
                clonedSku = $(clonedSkuDom.find('.sku')[0]).clone(true);
                clonedSku.text(value.item_name);
                clonedSku.attr('attr_id', value.item_name);
                clonedSku.attr('id', value.item_id);
                clonedSku.append($("<i class='right'></i>"));
                clonedSkuDom.find('.sku-value-container').append(clonedSku);
            });
            // 最后把模板span移除
            $(clonedSkuDom.find('.sku')[0]).remove();
            // 添加
            skuDOM.find('.sku-body').append(clonedSkuDom);
        });
    };

    //获得对象的key
    var getObjKeys = function(obj) {
        if (obj !== Object(obj)) throw new TypeError('Invalid object');
        var keys = [];
        for (var key in obj)
            if (Object.prototype.hasOwnProperty.call(obj, key))
                keys[keys.length] = key;
        return keys;
    };

    //把组合的key放入结果集SKUResult
    var add2SKUResult = function(combArrItem, sku) {
        var key = combArrItem.join(';');
        if (SKUResult[key]) {
            //SKU信息key属性·
            SKUResult[key].count += sku.count;
            SKUResult[key].prices.push(sku.price);
        } else {
            SKUResult[key] = {
                count: sku.count,
                prices: [sku.price]
            };
        }
    };

    //初始化得到结果集
    var initSKU = function() {
        var i,
            j,
            skuKeys = getObjKeys(dataObj);
        for (i = 0; i < skuKeys.length; i++) {
            var skuKey = skuKeys[i]; //一条SKU信息key
            var sku = dataObj[skuKey]; //一条SKU信息value
            var skuKeyAttrs = skuKey.split(';'); //SKU信息key属性值数组
            var len = skuKeyAttrs.length;

            //对每个SKU信息key属性值进行拆分组合
            var combArr = arrayCombine(skuKeyAttrs);
            for (j = 0; j < combArr.length; j++) {
                add2SKUResult(combArr[j], sku);
            }

            //结果集接放入SKUResult
            SKUResult[skuKey] = {
                count: sku.count,
                prices: [sku.price]
            };
        }
    };

    /**
     * 从数组中生成指定长度的组合
     */
    var arrayCombine = function(targetArr) {
        if (!targetArr || !targetArr.length) {
            return [];
        }

        var len = targetArr.length;
        var resultArrs = [];

        // 所有组合
        for (var n = 1; n < len; n++) {
            var flagArrs = getFlagArrs(len, n);
            while (flagArrs.length) {
                var flagArr = flagArrs.shift();
                var combArr = [];
                for (var i = 0; i < len; i++) {
                    flagArr[i] && combArr.push(targetArr[i]);
                }
                resultArrs.push(combArr);
            }
        }
        return resultArrs;
    };

    /**
     * 获得从m中取n的所有组合
     */
    var getFlagArrs = function(m, n) {
        if (!n || n < 1) {
            return [];
        }

        var resultArrs = [],
            flagArr = [],
            isEnd = false,
            i,
            j,
            leftCnt;

        for (i = 0; i < m; i++) {
            flagArr[i] = i < n ? 1 : 0;
        }

        resultArrs.push(flagArr.concat());

        while (!isEnd) {
            leftCnt = 0;
            for (i = 0; i < m - 1; i++) {
                if (flagArr[i] == 1 && flagArr[i + 1] == 0) {
                    for (j = 0; j < i; j++) {
                        flagArr[j] = j < leftCnt ? 1 : 0;
                    }
                    flagArr[i] = 0;
                    flagArr[i + 1] = 1;
                    var aTmp = flagArr.concat();
                    resultArrs.push(aTmp);
                    if (
                        aTmp
                            .slice(-n)
                            .join('')
                            .indexOf('0') == -1
                    ) {
                        isEnd = true;
                    }
                    break;
                }
                flagArr[i] == 1 && leftCnt++;
            }
        }
        return resultArrs;
    };

    //初始化用户选择事件
    var bindClickEvent = function() {
        skuDOM
            .find('.sku-body .sku')
            .each(function() {
                var self = $(this);
                var attr_id = self.attr('attr_id');

                if (!SKUResult[attr_id]) {
                    self.addClass('disabled');
                }
            })
            .click(function() {
                var self = $(this);
                //|| self.hasClass('goods-selected')
                if (
                    (self.hasClass('disabled') &&
                        !self.hasClass('default-function')) ||
                    self.hasClass('goods-selected')
                ) {
                    // 如果点击了disabled或者已经选择了的按钮,return
                    return;
                }

                //选中自己，兄弟节点取消选中
                self
                    .toggleClass('goods-selected')
                    .siblings()
                    .removeClass('goods-selected');

                //已经选择的节点
                var selectedObjs = skuDOM.find('.sku-body .goods-selected');

                if (selectedObjs.length) {
                    var getIndex = function(item) {
                        var tempIndex;
                        keys.forEach(function(items, index) {
                            if (items.indexOf(item) >= 0) {
                                tempIndex = index;
                            }
                        });
                        return tempIndex;
                    };

                    //获得组合key价格
                    var selectedIds = [];
                    selectedObjs.each(function() {
                        selectedIds.push($(this).attr('attr_id'));
                    });
                    selectedIds.sort(function(pre, cur) {
                        return getIndex(pre) - getIndex(cur);
                    });
                    var len = selectedIds.length;
                    var prices = SKUResult[selectedIds.join(';')].prices;
                    var maxPrice = Math.max.apply(Math, prices);
                    var minPrice = Math.min.apply(Math, prices);
                    var enable_quantity =
                        SKUResult[selectedIds.join(';')].count;
                    // $('.Dprice').text(maxPrice > minPrice ? minPrice + "-" + maxPrice : maxPrice);

                    //用已选中的节点验证待测试节点 underTestObjs
                    skuDOM
                        .find('.sku-body .sku')
                        .not(selectedObjs)
                        .not(self)
                        .each(function() {
                            var siblingsSelectedObj = $(this).siblings(
                                '.goods-sku .goods-selected'
                            );
                            var testAttrIds = []; //从选中节点中去掉选中的兄弟节点
                            if (siblingsSelectedObj.length) {
                                var siblingsSelectedObjId = siblingsSelectedObj.attr(
                                    'attr_id'
                                );
                                for (var i = 0; i < len; i++) {
                                    selectedIds[i] != siblingsSelectedObjId &&
                                        testAttrIds.push(selectedIds[i]);
                                }
                            } else {
                                testAttrIds = selectedIds.concat();
                            }
                            testAttrIds = testAttrIds.concat(
                                $(this).attr('attr_id')
                            );
                            // 给预备好的数据排序

                            testAttrIds.sort(function(pre, cur) {
                                return getIndex(pre) - getIndex(cur);
                            });
                            if (
                                !SKUResult[testAttrIds.join(';')] ||
                                SKUResult[testAttrIds.join(';')].count == 0
                            ) {
                                $(this)
                                    .addClass('disabled')
                                    .removeClass('goods-selected');
                            } else {
                                $(this).removeClass('disabled');
                            }
                        });

                    // 执行回调
                    callRefresh(
                        1,
                        enable_quantity,
                        maxPrice > minPrice ? [minPrice, maxPrice] : maxPrice
                    );
                } else {
                    //设置属性状态
                    skuDOM.find('.sku').each(function() {
                        SKUResult[$(this).attr('attr_id')]
                            ? $(this).removeClass('disabled')
                            : $(this)
                                  .addClass('disabled')
                                  .removeClass('goods-selected');
                    });
                }
            });
    };

    // 执行回调函数.
    // caller === 0, 则调用者是初始化函数.
    // caller === 1, 则调用者是点击事件.
    // 区别是 等于0话,传入回调参数的价格要用这个函数里面生成的skuPrice.因为此时priceArray为undefined
    var callRefresh = function(caller, enable_quantity, priceArray) {
        if (!skuRender.refresh) {
            return;
        }
        var specIDArray = getSelectedSpecID();
        // match: 用户选择的这组规格,能匹配当前sku吗 ?
        var match;
        // 遍历搜寻完成?
        var done = false;
        var skuID;
        var skuPrice;

        //遍历数据源, 根据规格值id查找所属sku的id
        specListData.forEach(function(skuObj) {
            if (!done) {
                // 默认假设它能匹配
                match = true;
                skuObj.specList.forEach(function(specObj) {
                    // 如果发现有一个规格不匹配.
                    if (specIDArray.indexOf(specObj.spec_value_id) < 0) {
                        match = false;
                    }
                });
                // 当前sku对象的遍历搜寻完成.
                // 如果match为true,则找到了匹配的sku对象
                if (match) {
                    // 完成
                    done = true;
                    skuID = skuObj.sku_id;
                    // PS: 这里可以获取到这对sku的价格信息.
                    // 所以可以不从外部传入.
                    skuPrice = skuObj.price;
                }
            }
        });

        // 执行回调函数
        skuRender.refresh({
            sku_id: skuID,
            price: caller === 1 ? priceArray : skuPrice,
            enable_quantity: enable_quantity
        });
    };

    // -----------------------初始化选择项-----------------------
    // 可以选择这个选项 ?
    // 需要判断: 如果选择了该项,后面每行必须有至少一个可选择项.
    var canChooseTheOptopn = function(which) {
        if (which.hasClass('disabled')) {
            return false;
        }
        // 后面的行至少有一个可选项
        var haveLeastOneOptionAfterThis = function(divArray) {
            var ok = true;
            divArray.each(function() {
                // 遍历每个sku元素,判断是否有disabled类
                if (
                    $(this).children('.sku.disabled').length ==
                    $(this).children('.sku').length
                ) {
                    ok = false;
                }
            });
            return ok;
        };
        if (!haveLeastOneOptionAfterThis(which.parent().nextAll())) {
            return false;
        }

        return true;
    };

    //页面加载时 默认设置选中一个sku,即一组规格
    var setDefaultSku = function() {
        skuDOM.find('.sku-body >div').each(function() {
            // 遍历判断规格值,返回所有没有disabled属性的规格值
            // 触发第一个的点击事件
            $(
                $(this)
                    .find('.sku')
                    .filter(function() {
                        return canChooseTheOptopn($(this));
                    })[0]
            ).trigger('click');
        });

        // 遍历检测
        // 如果根据模拟用户点击进行初始化操作之后, 仍然没有完成'选中一组sku'.(即有一个行规格值的库存为0), 这时强行选择这行规格的第一个规格值.
        skuDOM.find('.sku-body div.sku-value-container').each(function() {
            var self = $(this);

            // 如果某行规格值皆为不可选状态(disabled)
            if (
                self.children('.sku.disabled').length ===
                self.children('.sku').length
            ) {
                // 强行选择第一个. 然后设置特殊标记的class: default-function
                $(self.children('.sku')[0])
                    .addClass('default-function goods-selected')
                    .removeClass('disabled');

                callRefresh(0, 0);
            }
        });
    };

    // ---------------------------END---------------------------

    // 遍历获取用户选择的所有的规格的id数组
    var getSelectedSpecID = function() {
        var specValueIDArray = [];
        skuDOM.find('.sku-body > div').each(function() {
            specValueIDArray.push(
                Number(
                    $(this)
                        .find('.sku.goods-selected')
                        .attr('id')
                )
            );
        });
        return specValueIDArray;
    };

    $.fn.skuRender = function(options) {
        // 提前设置goodID
        goodsID = options.goodsID;

        // 把回调引用传递到全局变量中.供IIFE中的其他方法调用
        skuRender.refresh = options.refresh;

        // 获取远程数据,然后执行进行一系列函数.
        getRemoteData(options.setData);

        // 把DOM渲染到页面中
        $(this).html(skuRender.DOM);
    };
})(jQuery);
