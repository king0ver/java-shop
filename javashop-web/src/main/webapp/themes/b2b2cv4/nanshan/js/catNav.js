/**
 * Created by yulong on 17/12/18.
 */
$(function(){
    $('.navList .item').on({
        mouseover : function(){
            $(this).children('.itmeList').show()
        } ,
        mouseout : function(){
            $(this).children('.itmeList').hide()
        }
    })
})
