package com.enation.app.nanshan.vo;

import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * 文章信息
 * @author jianjianming
 * @version $Id: ArticleVo.java,v 0.1 2017年12月21日 下午9:44:25$
 */
public class ArticleVo implements Serializable {

	private static final long serialVersionUID = 3854836881136684562L;
	
	private Integer id;
	/**
	 * 标题
	 */
	private String title;  
	/**
	 * 分类id
	 */
	private int catId;
	/**
	 * 链接
	 */
	private String url;
	/**
	 * 图片地址
	 */
	private String imgUrl;
	/**
	 * 摘要
	 */
	private String summary;
	/**
	 * 时间
	 */
	private Long createTime;
	/**
	 * 文件内容
	 */
	private JSONObject content;
	
	
	
	
	private Integer reserveNum; //可预约人数
	private Integer reservedNum; //已预约人数
	private Long expiryDate; //截止时间
	private String  actName;//  活动名称
	private String actCost;//活动费用
	private String actAddress;//活动地址


	private List<ArticleVo>  articleList;


	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getCatId() {
		return catId;
	}
	public void setCatId(int catId) {
		this.catId = catId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}

	public JSONObject getContent() {
		return content;
	}

	public void setContent(JSONObject content) {
		this.content = content;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getReserveNum() {
		return reserveNum;
	}
	public void setReserveNum(Integer reserveNum) {
		this.reserveNum = reserveNum;
	}
	public Integer getReservedNum() {
		return reservedNum;
	}
	public void setReservedNum(Integer reservedNum) {
		this.reservedNum = reservedNum;
	}

	public String getActName() {
		return actName;
	}
	public void setActName(String actName) {
		this.actName = actName;
	}
	
	public String getActAddress() {
		return actAddress;
	}
	public void setActAddress(String actAddress) {
		this.actAddress = actAddress;
	}
	public String getActCost() {
		return actCost;
	}
	public void setActCost(String actCost) {
		this.actCost = actCost;
	}
	public Long getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Long expiryDate) {
		this.expiryDate = expiryDate;
	}
	public List<ArticleVo> getArticleList() {
		return articleList;
	}
	public void setArticleList(List<ArticleVo> articleList) {
		this.articleList = articleList;
	}



	
}
