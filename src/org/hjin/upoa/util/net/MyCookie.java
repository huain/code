package org.hjin.upoa.util.net;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class MyCookie {
	
	public static MyCookie mycookie;
	
	private Map<String,String> mKeyValueMap;
	
	public static MyCookie getInstance(){
		if(null == mycookie){
			mycookie = new MyCookie();
		}
		return mycookie;
	}
	
	private MyCookie(){
		mKeyValueMap = new HashMap<String,String>();
	}
	
	public void put(String name,String value){
		mKeyValueMap.put(name, value);
	}
	
	public void remove(String name){
		mKeyValueMap.remove(name);
	}
	
	public void clear(){
		mKeyValueMap.clear();
	}
	
	public String getCookieString(){
		if(null != mKeyValueMap && mKeyValueMap.size()>0){
			int i=0;
			Set<Entry<String,String>> params = mKeyValueMap.entrySet();
			StringBuffer cookieBuffer = new StringBuffer("");
			for(Entry<String,String> h:params){
				cookieBuffer.append(h.getKey());
				cookieBuffer.append("=");
				cookieBuffer.append(h.getValue());
				cookieBuffer.append(";");
				i++;
			}
			cookieBuffer.deleteCharAt(cookieBuffer.lastIndexOf(";"));
			return cookieBuffer.toString();
		}
		return null;
	}
	
}
