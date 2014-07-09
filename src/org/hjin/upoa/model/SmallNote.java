package org.hjin.upoa.model;

import java.io.Serializable;
/**
 * 小字报
 * @author Administrator
 *
 */
public class SmallNote implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/** id*/
	private String id;
	/** 序号id*/
	private String indexid;
	/** 标题*/
	private String title;
	/** 发布者姓名*/
	private String username;
	/** 发布者id*/
	private String userid;
	/** 发布者部门*/
	private String userdep;
	/** 发布时间*/
	private String time;
	/** 内容*/
	private String content;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIndexid() {
		return indexid;
	}
	public void setIndexid(String indexid) {
		this.indexid = indexid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUserdep() {
		return userdep;
	}
	public void setUserdep(String userdep) {
		this.userdep = userdep;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}
