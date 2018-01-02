package com.enation.app.nanshan.controller;

import com.enation.app.base.core.model.Member;
import com.enation.app.nanshan.model.NanShanActReserve;
import com.enation.app.nanshan.service.ICatManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yulong on 18/1/1.
 */
@Api(description="预约活动")
@RestController
@RequestMapping("/activity-operation")
@Validated
public class AppointController {

    @Autowired
    private ICatManager catManager;

    @ApiOperation(value="活动预约", notes="活动预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameAccount", value = "游戏帐号", required = true, dataType = "String",paramType="query"),
            @ApiImplicitParam(name = "points", value = "点数", required =true, dataType = "int" ,paramType="query" ),
    })
    @ResponseBody
    @PostMapping(value="/appoint")
    public JsonResult create(@RequestBody NanShanActReserve actReserve){

        try {

            Member member  = UserConext.getCurrentMember();

            if(member == null){
                return JsonResultUtil.getErrorJson("用户未登录!");
            }

            catManager.reserve(actReserve);

        } catch (Exception e) {
            if (!StringUtil.isEmpty(e.getMessage())) {
                return JsonResultUtil.getErrorJson(e.getMessage());
            }
            return JsonResultUtil.getErrorJson("预约失败");
        }
        return JsonResultUtil.getSuccessJson("预约成功!");


    }

}
