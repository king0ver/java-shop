package com.enation.app.base.progress.model;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * 任务进度
 * @author kingapex
 * @version v1.0
 * @since v1.0
 * 2015年5月13日 下午5:54:43
 */
public class TaskProgress  implements Serializable{
	
	
	public static final String PROCESS="PROCESS_";
	
	/** id */
	private String id;
	/** 百分比 */
	private  double sum_per; 
	/** 每步占比 */
	private double step_per;
	/** 正在生成的内容 */
	private  String text;
	/** 生成状态 */
	private String task_status;
	/** 任务总数 */
	private  int task_total; 


	/**
	 * 构造时要告诉任务总数，以便计算每步占比
	 * @param total
	 */
	public  TaskProgress(int total){

		/** 计算每步的百分比 */
		this.task_total =total;
		this.task_status=ProgressEnum.ing.name();
		BigDecimal b1 = new BigDecimal("100");
		BigDecimal b2 = new BigDecimal(""+task_total);
		step_per = b1.divide(b2,5,BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	/**
	 * 完成一步
	 */
	public void step(String text){

		this.sum_per += this.step_per;
		this.text= text;
	}

	/**
	 * 成功
	 */
	public void success(){
		this.sum_per=100;
		this.text="完成";
		this.task_status=ProgressEnum.complete.name();
	}

	/**
	 * 失败
	 * @param text
	 */
	public void fail(String text){
		this.task_status=ProgressEnum.error.name();
		this.text= text;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public int getTask_total() {
		return task_total;
	}
	public void setTask_total(int task_total) {
		this.task_total = task_total;
	}

	public int getSum_per() {
		return Double.valueOf(sum_per).intValue();
	}

	public void setSum_per(int sum_per) {
		this.sum_per = sum_per;
	}

	public double getStep_per() {
		return step_per;
	}

	public void setStep_per(double step_per) {
		this.step_per = step_per;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTask_status() {
		return task_status;
	}

	public void setTask_status(String task_status) {
		this.task_status = task_status;
	} 



}
