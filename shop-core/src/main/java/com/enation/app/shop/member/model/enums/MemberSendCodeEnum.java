package com.enation.app.shop.member.model.enums;

/**
 * 会员发送验证码枚举
 * @author dongxin
 * @since 6.4.0
 * @version 1.0
 * 2017-8-3
 */
public enum MemberSendCodeEnum {

	LOGINMOBILE("会员登陆"),
	REGISTERMOBILE("会员注册"),
	FINDPASSWORDMOBILE("手机修改密码"),
	BINDINGMOBILE("绑定手机"),
	UPDATEPASSWORDMOBILE("修改密码"),
	CHECKMOBILE("普通校验");
	// 构造方法
    private MemberSendCodeEnum(String key) {
        this.key = key;
    }

    public String getKey(){
        return this.key;
    }

    private String key;
}
