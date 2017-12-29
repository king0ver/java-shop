$(function(){
	$('.navList .item').on({  
            mouseover : function(){  
              $(this).children('.itmeList').show()  
            } ,  
            mouseout : function(){  
               $(this).children('.itmeList').hide() 
            }   
      }) 
      $('.rightNavList').on({  
            mouseover : function(){  
              $(this).children('span').show()  
            } ,  
            mouseout : function(){  
               $(this).children('span').hide() 
            }
      })
      $(window).on('scroll',function(){
            var $scroll=$(this).scrollTop();
            
            $('.louti').each(function(index,el){
                var $loutitop=$('.louti').eq(index).offset().top+400;
                console.log(index)
                //console.log($loutitop);
                if($loutitop>$scroll){
                    $('.loutinav div').removeClass('activeNav');
                    $('.loutinav div').eq(index).addClass('activeNav');
                    return false;
                }
            });
        });
      
        
        var $loutili=$('.loutinav div')
        $loutili.on('click',function(){
            $(this).addClass('active').siblings('div').removeClass('activeNav');
            var $loutitop=$('.louti').eq($(this).index()).offset().top-100;
            $('html,body').animate({
                scrollTop:$loutitop
            })
        });
        
        
     
})
