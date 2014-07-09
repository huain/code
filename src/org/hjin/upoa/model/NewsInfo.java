package org.hjin.upoa.model;

import java.io.Serializable;

/**
 * 新闻信息
 * 
 * @author Administrator
 * 
 */
public class NewsInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String mId;

	private String mTitle;

	private String mTime;

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmTime() {
		return mTime;
	}

	public void setmTime(String mTime) {
		this.mTime = mTime;
	}

}
