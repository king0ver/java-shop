package com.enation.app.shop.component.shortmsg.plugin;

import java.util.ArrayList;
import java.util.List;

import com.enation.app.shop.aftersale.model.enums.RefuseType;
import com.enation.app.shop.shop.apply.model.enums.ShopDisableStatus;
import com.enation.app.shop.shop.setting.model.po.StoreSetting;
import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.base.core.model.ShortMsg;
import com.enation.app.base.core.plugin.shortmsg.IShortMessageEvent;
import com.enation.app.base.core.service.auth.IPermissionManager;
import com.enation.app.base.core.service.auth.impl.PermissionConfig;
import com.enation.app.shop.ShopApp;
import com.enation.app.shop.aftersale.model.enums.RefundStatus;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegister;
import com.enation.framework.plugin.AutoRegisterPlugin;

/**
 * 网店短消息提醒插件
 *
 * @author Kanon  Kanon                yanlin
 * @version 增加自营店取消订单、    v1.0,代码迁移
 * 取消订单、申请退货、
 * 申请退款消息提醒
 * @date 2016-7-14            2017年8月17日 下午4:13:44
 * @since v6.4.0
 */

@AutoRegister
public class B2b2cShortMsgPlugin extends AutoRegisterPlugin implements IShortMessageEvent {

    @Autowired
    private IDaoSupport daoSupport;

    @Autowired
    private IPermissionManager permissionManager;

    @Override
    public List<ShortMsg> getMessage() {

        List<ShortMsg> msgList = new ArrayList<ShortMsg>();

        ShortMsg msg = new ShortMsg();
        /** 需要审核店铺数量 */
        msg = getShopApply();
        if (msg != null) {
            msgList.add(msg);
        }

        /** 需要审核商品数量 */
        msg = getApplyGoods();
        if (msg != null) {
            msgList.add(msg);
        }

        /** 需要平台处理的退款单 */
        msg = getApplyRefund();
        if (msg != null) {
            msgList.add(msg);
        }
        return msgList;
    }

    /**
     * 获取需要审核店铺数量
     * @return 消息
     */
    private ShortMsg getShopApply() {
        int count = 0;
        StringBuffer sql = new StringBuffer("select count(*) from es_shop where shop_disable = ?");
        count = this.daoSupport.queryForInt(sql.toString(), ShopDisableStatus.apply.value());
        if (count > 0) {
            ShortMsg msg = new ShortMsg();
            msg.setUrl("/b2b2c/admin/shop/audit-list.do");
            msg.setTitle("开店审核");
            msg.setTarget("ajax");
            msg.setContent("有" + count + "个开店申请需要处理");
            return msg;
        }
        return null;
    }
    /**
     * 获取需要审核商品数量
     * @return 消息
     */
    private ShortMsg getApplyGoods(){
        if (StoreSetting.getAuth() == 1){
            int count = 0;
            StringBuffer sql = new StringBuffer("select count(*) from es_goods where is_auth = 0");
            count = this.daoSupport.queryForInt(sql.toString());
            if (count > 0) {
                ShortMsg msg = new ShortMsg();
                msg.setUrl("/shop/admin/goods/auth/list-page.do");
                msg.setTitle("商品审核");
                msg.setTarget("ajax");
                msg.setContent("有" + count + "个商品需要审核处理");
                return msg;
            }

        }
        return null;
    }

    /**
     * 获取需要处理的退款单信息
     * @return 消息
     */
    private ShortMsg getApplyRefund(){
        int count = 0;
        StringBuffer sql = new StringBuffer("select  count(*) from es_refund where (refund_status = ? AND refuse_type = ?) OR (refund_status = ? AND refuse_type = ?)");
        count = this.daoSupport.queryForInt(sql.toString(),RefundStatus.pass.value(),RefuseType.return_money.value(),RefundStatus.all_stock_in.value(), RefuseType.return_goods.value());
        if (count > 0) {
            ShortMsg msg = new ShortMsg();
            msg.setUrl("/shop/admin/refund-list.do?ident=1");
            msg.setTitle("退款单");
            msg.setTarget("ajax");
            msg.setContent("有" + count + "个退款单需要处理");
            return msg;
        }
        return null;
    }

}
