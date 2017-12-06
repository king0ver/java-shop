$(function () {
    // 退货退款方式切换
    $('.type').on('tap', 'a', function () {
        var $this = $(this);
        var status = $this.attr("type");
        $this.addClass('active').siblings().removeClass('active');
        if (status == 1) {
            $(".applica-quantity").hide();
        } else {
            $(".applica-quantity").show();
        }
    });
    $("select[name='method']").change(function () {
        var $val = $(this).val();
        if ($val == 'bank-transfer') {
            $('.box-show').show();
        } else {
            $('.box-show').hide();
        }
    })
})