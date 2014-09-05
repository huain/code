package org.hjin.upoa.model;

import java.io.Serializable;
/**
 * 日报
 * @author Administrator
 *
 */
public class Daily implements Serializable {
	
	private static final long serialVersionUID = 5242247679567764092L;

	private String id;
	
	private String code;
	
	private String name;
	/** 主题*/
	private String subject;
	
	private String begintime;
	
	private String endtime;
	/** 历时*/
	private String time;
	
	private String desc;
	/** 任务类型*/
	private String type;
	/** 工作地点*/
	private String position;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	
	

}
