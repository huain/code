package org.hjin.upoa.model;

import java.io.Serializable;
/**
 * С�ֱ�
 * @author Administrator
 *
 */
public class SmallNote implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/** id*/
	private String id;
	/** ���id*/
	private String indexid;
	/** ����*/
	private String title;
	/** ����������*/
	private String username;
	/** ������id*/
	private String userid;
	/** �����߲���*/
	private String userdep;
	/** ����ʱ��*/
	private String time;
	/** ����*/
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
