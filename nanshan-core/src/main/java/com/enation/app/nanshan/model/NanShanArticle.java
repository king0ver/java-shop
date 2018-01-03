package com.enation.app.nanshan.model;

import java.io.Serializable;

public class NanShanArticle implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id; 
	private String title;  //标题
	private int cat_id;    //分类id
	private String url;    //链接
	private long create_time; //时间
	private String summary;  // 摘要
	private String pic_url; //图片链接
	private int is_del; //是否删除
	private int content; //内容id		
	private String work_place;//工作地点
	private String job_cat;//职位类别
	private String dept_name;//部门名称
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getCat_id() {
		return cat_id;
	}
	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public int getIs_del() {
		return is_del;
	}
	public void setIs_del(int is_del) {
		this.is_del = is_del;
	}
	public int getContent() {
		return content;
	}
	public void setContent(int content) {
		this.content = content;
	}


	public String getJob_cat() {
		return job_cat;
	}
	public void setJob_cat(String job_cat) {
		this.job_cat = job_cat;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getWork_place() {
		return work_place;
	}
	public void setWork_place(String work_place) {
		this.work_place = work_place;
	}
	
	
	
	

}
