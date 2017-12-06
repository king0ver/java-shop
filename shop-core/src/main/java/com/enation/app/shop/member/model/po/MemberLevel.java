package com.enation.app.shop.member.model.po;

/**
 * 会员等级
 * @author fk
 * @version v1.0
 * 2017年5月8日 下午2:26:34
 */
public class MemberLevel  implements java.io.Serializable {

	private static final long serialVersionUID = 6326245513557314992L;

    private Integer level_id; //ID
    private String name;	//名称
    private Integer default_lv;	//是否为默认会员级别
    private Integer discount;	//折扣，如果80，表示该会员等级以销售价80%的价格购买。
    private int point;		//需要积分
    
	public Integer getLevel_id() {
		return level_id;
	}
	public void setLevel_id(Integer level_id) {
		this.level_id = level_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getDefault_lv() {
		return default_lv;
	}
	public void setDefault_lv(Integer default_lv) {
		this.default_lv = default_lv;
	}
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
    
    
}