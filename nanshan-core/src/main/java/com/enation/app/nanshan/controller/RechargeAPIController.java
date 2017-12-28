package com.enation.app.nanshan.controller;

import com.enation.app.base.core.model.Member;
import com.enation.app.nanshan.service.RechargeService;
import com.enation.app.nanshan.vo.RechargeVo;
import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.app.shop.trade.controllor.front.TradeController;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yulong on 17/12/23.
 */
@Api(description="充值订单生成")
@RestController
@RequestMapping("/recharge-operation")
@Validated
public class RechargeAPIController {

    @Autowired
    private RechargeService rechargeService;


    @ApiOperation(value="创建充值订单", notes="创建充值订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameAccount", value = "游戏帐号", required = true, dataType = "String",paramType="query"),
            @ApiImplicitParam(name = "points", value = "点数", required =true, dataType = "int" ,paramType="query" ),
    })
    @ResponseBody
    @PostMapping(value="/create")
    public JsonResult create(String gameAccount, int points){

        String client_type;
        if(isWap()){
            client_type = ClientType.WAP.name();
        }else{
            client_type = ClientType.PC.name();
        }

        RechargeVo rechargeVo;
        try {

            Member member  = UserConext.getCurrentMember();

            rechargeVo = rechargeService.create(gameAccount, points, client_type,
                    member.getMember_id(), member.getName());


        } catch (Exception e) {
            if (!StringUtil.isEmpty(e.getMessage())) {
                return JsonResultUtil.getErrorJson(e.getMessage());
            }
            return JsonResultUtil.getErrorJson("创建充值订单失败");
        }
        return JsonResultUtil.getObjectJson(rechargeVo);


    }

    /**
     * 判断是否是wap访问
     * @param header
     * @return 是否是wap
     */
    public static boolean isWap(){
        String header = ThreadContextHolder.getHttpRequest().getHeader("User-Agent");
        boolean flag = false;
        String[] keywords = { "Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser","Mobile" };
        for (String s : keywords) {
            if(header.contains(s)){
                flag = true;
                break;
            }
        }
        return flag;
    }

}
