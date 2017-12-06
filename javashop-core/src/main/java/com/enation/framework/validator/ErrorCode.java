package com.enation.framework.validator;
/**
 * 
 * 异常常量类
 * @author jianghongyan
 * @version v1.0.0
 * @since v1.0.0
 * 2017年3月27日 下午6:56:35
 */
public class ErrorCode {
	/**
	 * CMS ERROR CODE
	 */
	public final static String CLASSIFYNAV_NOT_EXIST="classifyNav_not_exist";//分类导航不存在
	public final static String CHANNEL_NOT_EXIST="channel_not_exist";//频道不存在
	public final static String ADVERT_OVER_LIMIT="advert_over_limit";//分类广告数量超过限制
	public final static String FOCUS_OVER_LIMIT="focus_over_limit";//焦点图数量超过限制
	public final static String ARTICLE_CLASSIFY_LOCKED="article_classify_locked";
	public final static String NO_PERMISSION="no_permission";//无权限异常
	public final static String RESOURCE_NOT_FOUND="resource_not_found";//资源未能找到
	public final static String DEMO_SITE_TIP="demo_site_tip";//演示站点
	
	public final static String ORDER_PAY_VALID_ERROR="order_pay_valid_error";//订单支付校验错误
	
	
	public final static String INVALID_REQUEST_PARAMETER="Invalid_Request_Parameter";//错误参数
	
	/**
	 * Goods Error Code
	 */
	public final static String GOODS_PARAM_ERROR = "goods_param_error";//商品参数错误
	
	/**
	 * member Error Code
	 */
	public final static String MEMBER_PARAM_ERROR = "member_param_error";//会员服务参数错误
	public final static String RECEIPT_DELETE_FAILED="receipt_delete_failed";
	
	
	/**
	 * comment Error Code
	 */
	public final static String COMMENT_PARAM_ERROR = "comment_param_error";//评价咨询服务参数错误
	
	/**
	 * shop Error Code
	 */
	public final static String SHOP_PARAM_ERROR = "shop_param_error";//评价咨询服务参数错误


	
}
