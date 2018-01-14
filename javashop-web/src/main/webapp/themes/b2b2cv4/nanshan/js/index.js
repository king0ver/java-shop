$(function () {
    $('#banner').css('height', window.screen.height - 125)
    $('.weix').on({
        mouseover: function () {
            $('.codeGhh,.codeWXSJ').show();
        },
        mouseout: function () {
            $('.codeGhh,.codeWXSJ').hide();
        }
        //    if($(this).hasClass('select')){
        //
        //
        //      $(this).removeClass('select')
        //    }else{
        //      $('.codePH,.codePHSJ').hide();
        //      $('.codeGhh,.codeWXSJ').show();
        //      $(this).siblings().removeClass('select')
        //      $(this).addClass('select')
        //    }
    })
    $('.phone').on({
        mouseover: function () {
            $('.codePH,.codePHSJ').show();
        },
        mouseout: function () {
            $('.codePH,.codePHSJ').hide();
        }
        //    if($(this).hasClass('select')){
        //      $('.codePH,.codePHSJ').hide();
        //      $(this).removeClass('select')
        //    }else{
        //      $('.codeGhh,.codeWXSJ').hide();
        //      $('.codePH,.codePHSJ').show();
        //      $(this).siblings().removeClass('select')
        //      $(this).addClass('select')
        //    }
    })
    //  $('.navList .item').on({
    //          mouseover : function(){
    //            $(this).children('.itmeList').show()
    //          } ,
    //          mouseout : function(){
    //             $(this).children('.itmeList').hide()
    //          }
    //    })
    $('.lszl,.kxyc').on({
        mouseover: function () {
            $(this).addClass('szHover')
        },
        mouseout: function () {
            $(this).removeClass('szHover')
        }
    })
    $('.rightNavList').on({
        mouseover: function () {
            $(this).children('span').show()
        },
        mouseout: function () {
            $(this).children('span').hide()
        }
    })
    $(window).on('scroll', function () {
        var $scroll = $(this).scrollTop();

        $('.louti').each(function (index, el) {
            var $loutitop = $('.louti').eq(index).offset().top + 400;
            console.log(index)
            //console.log($loutitop);
            if ($loutitop > $scroll) {
                $('.loutinav div').removeClass('activeNav');
                $('.loutinav div').eq(index).addClass('activeNav');
                return false;
            }
        });
    });


    var $loutili = $('.loutinav div')
    $loutili.on('click', function () {
        $(this).addClass('active').siblings('div').removeClass('activeNav');
        var $loutitop = $('.louti').eq($(this).index()).offset().top - 100;
        $('html,body').animate({
            scrollTop: $loutitop
        })
    });



})