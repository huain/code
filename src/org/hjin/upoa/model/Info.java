package org.hjin.upoa.model;

import java.io.Serializable;

/**
 * ͨѶ¼
 * @author Administrator
 *
 */
public class Info implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	/** ְλ*/
	private String post;
	
	private String dep;
	
	private String tel;
	
	private String email;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getDep() {
		return dep;
	}

	public void setDep(String dep) {
		this.dep = dep;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
