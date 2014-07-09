package org.hjin.upoa.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.util.net.MyParameters;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;


public class Utility {
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final char[] encodes = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			.toCharArray();
	private static final byte[] decodes = new byte[256];
	
	/**
	 * 判断字符串是否为“”
	 * @param target
	 * @return null或者“”，均返回true，否则false
	 */
	public static boolean isBlank(String target){
		if(null == target || "".equals(target)){
			return true;
		}else{
			return false;
		}
	}

	public static Bundle parseUrl(String url) {
		try {
			URL u = new URL(url);
			Bundle b = decodeUrl(u.getQuery());
			b.putAll(decodeUrl(u.getRef()));
			return b;
		} catch (MalformedURLException e) {
		}
		return new Bundle();
	}

	public static void showToast(String content, Context ct) {
		Toast.makeText(ct, content, 1).show();
	}

	public static Bundle decodeUrl(String s) {
		Bundle params = new Bundle();
		if (s != null) {
			String[] array = s.split("&");
			for (String parameter : array) {
				String[] v = parameter.split("=");
				try {
					params.putString(URLDecoder.decode(v[0], "UTF-8"),
							URLDecoder.decode(v[1], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return params;
	}

	public static String packUrl(HashMap<String, String> params) {
		if (params == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		String value = "";
		boolean bFirst = true;
		for (String key : params.keySet()) {
			value = (String) params.get(key);
			if ((TextUtils.isEmpty(key)) || (TextUtils.isEmpty(value)))
				continue;
			try {
				if (bFirst)
					bFirst = false;
				else {
					sb.append("&");
				}
				sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
						.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}

		return sb.toString();
	}

	public static Bundle unpackUrl(String url) {
		Bundle params = new Bundle();
		if (url != null) {
			String[] array = url.split("&");
			for (String parameter : array) {
				String[] v = parameter.split("=");
				try {
					params.putString(URLDecoder.decode(v[0], "UTF-8"),
							URLDecoder.decode(v[1], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}

		return params;
	}

	public static String encodeUrl(MyParameters parameters) {
		if (parameters == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (int loc = 0; loc < parameters.size(); loc++) {
			if (first)
				first = false;
			else {
				sb.append("&");
			}
			String _key = parameters.getKey(loc);
			String _value = parameters.getValue(_key);
			if (_value == null)
				LogUtil.i("encodeUrl", "key:" + _key + " 's value is null");
			else {
				try {
					sb.append(URLEncoder.encode(parameters.getKey(loc), "UTF-8")
							+ "="
							+ URLEncoder.encode(parameters.getValue(loc),
									"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			LogUtil.i("encodeUrl", sb.toString());
		}
		return sb.toString();
	}

	public static String encodeParameters(MyParameters httpParams) {
		if ((httpParams == null) || (isBundleEmpty(httpParams))) {
			return "";
		}
		StringBuilder buf = new StringBuilder();
		int j = 0;
		for (int loc = 0; loc < httpParams.size(); loc++) {
			String key = httpParams.getKey(loc);
			if (j != 0)
				buf.append("&");
			try {
				buf.append(URLEncoder.encode(key, "UTF-8"))
						.append("=")
						.append(URLEncoder.encode(httpParams.getValue(key),
								"UTF-8"));
			} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			}
			j++;
		}
		return buf.toString();
	}

	public static Bundle formErrorBundle(Exception e) {
		Bundle params = new Bundle();
		params.putString("error", e.getMessage());
		return params;
	}
	
	private static boolean isBundleEmpty(MyParameters bundle) {
		return (bundle == null) || (bundle.size() == 0);
	}
	
	public static String md5(String str){
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			byte []bytes=md5.digest(str.getBytes("utf8"));
			StringBuilder ret=new StringBuilder(bytes.length<<1);
			for(int i=0;i<bytes.length;i++){
			  ret.append(Character.forDigit((bytes[i]>>4)&0xf,16));
			  ret.append(Character.forDigit(bytes[i]&0xf,16));
			}
			String result = ret.toString();
			
//			md5.update(str.getBytes());  
//	        byte[] m = md5.digest();//加密 
//	        String result="";
//	        try {
//				result = new String(m,"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
	        Log.d("md5", "=====:"+str);
	        Log.d("md5", "=====:"+result);
	        return result;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return "";
	}

	public static String encodeBase62(byte[] data) {
		StringBuffer sb = new StringBuffer(data.length * 2);
		int pos = 0;
		int val = 0;
		for (int i = 0; i < data.length; i++) {
			val = val << 8 | data[i] & 0xFF;
			pos += 8;
			while (pos > 5) {
				pos -= 6;
				char c = encodes[(val >> pos)];
				sb.append(c == '/' ? "ic" : c == '+' ? "ib" : c == 'i' ? "ia"
						: Character.valueOf(c));
				val &= (1 << pos) - 1;
			}
		}
		if (pos > 0) {
			char c = encodes[(val << 6 - pos)];
			sb.append(c == '/' ? "ic" : c == '+' ? "ib" : c == 'i' ? "ia"
					: Character.valueOf(c));
		}
		return sb.toString();
	}

	public static byte[] decodeBase62(String string) {
		if (string == null) {
			return null;
		}
		char[] data = string.toCharArray();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				string.toCharArray().length);
		int pos = 0;
		int val = 0;
		for (int i = 0; i < data.length; i++) {
			char c = data[i];
			if (c == 'i') {
				i++;
				c = data[i];

				i--;

				c = c == 'c' ? '/' : c == 'b' ? '+' : c == 'a' ? 'i' : data[i];
			}
			val = val << 6 | decodes[c];
			pos += 6;
			while (pos > 7) {
				pos -= 8;
				baos.write(val >> pos);
				val &= (1 << pos) - 1;
			}
		}
		return baos.toByteArray();
	}
	
	public static String encodeBase64(String s){
		if(null != s){
			return android.util.Base64.encodeToString(s.getBytes(),Base64.NO_WRAP);
		}
		return null;
		
	}
	
	public static String decodeBase64(String s){
		if(null != s){
			return android.util.Base64.decode(s.getBytes(),Base64.NO_WRAP).toString();
		}
		return null;
		
	}
	

	private static void mkdirs(File dir_) {
		if (dir_ == null) {
			return;
		}
		if ((!dir_.exists()) && (!dir_.mkdirs()))
			throw new RuntimeException("fail to make " + dir_.getAbsolutePath());
	}

	private static void createNewFile(File file_) {
		if (file_ == null) {
			return;
		}
		if (!__createNewFile(file_))
			throw new RuntimeException(file_.getAbsolutePath()
					+ " doesn't be created!");
	}

	private static void delete(File f) {
		if ((f != null) && (f.exists()) && (!f.delete()))
			throw new RuntimeException(f.getAbsolutePath()
					+ " doesn't be deleted!");
	}

	private static boolean __createNewFile(File file_) {
		if (file_ == null) {
			return false;
		}
		makesureParentExist(file_);
		if (file_.exists())
			delete(file_);
		try {
			return file_.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}


	private static boolean doesExisted(File file) {
		return (file != null) && (file.exists());
	}

	private static boolean doesExisted(String filepath) {
		if (TextUtils.isEmpty(filepath))
			return false;
		return doesExisted(new File(filepath));
	}

	private static void makesureParentExist(File file_) {
		if (file_ == null) {
			return;
		}
		File parent = file_.getParentFile();
		if ((parent != null) && (!parent.exists()))
			mkdirs(parent);
	}

	private static void makesureFileExist(File file) {
		if (file == null)
			return;
		if (!file.exists()) {
			makesureParentExist(file);
			createNewFile(file);
		}
	}

	private static void makesureFileExist(String filePath_) {
		if (filePath_ == null)
			return;
		makesureFileExist(new File(filePath_));
	}

	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService("connectivity");
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

		return (activeNetInfo != null) && (activeNetInfo.getType() == 1);
	}

	public static String generateGUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static boolean isChineseLocale(Context context) {
		try {
			Locale locale = context.getResources().getConfiguration().locale;
			if ((Locale.CHINA.equals(locale))
					|| (Locale.CHINESE.equals(locale))
					|| (Locale.SIMPLIFIED_CHINESE.equals(locale))
					|| (Locale.TAIWAN.equals(locale)))
				return true;
		} catch (Exception e) {
			return true;
		}
		return false;
	}

	public static Bundle errorSAX(String responsetext) {
		Bundle mErrorBun = new Bundle();
		if ((responsetext != null) && (responsetext.indexOf("{") >= 0)) {
			try {
				JSONObject json = new JSONObject(responsetext);
				mErrorBun.putString("error", json.optString("error"));
				mErrorBun.putString("error_code", json.optString("error_code"));
				mErrorBun.putString("error_description",
						json.optString("error_description"));
			} catch (JSONException e) {
				mErrorBun.putString("error", "JSONExceptionerror");
			}
		}

		return mErrorBun;
	}
	
	public static Calendar getMonday(Calendar time){
		if(null == time){
			time = Calendar.getInstance();
		}
		Date date = time.getTime(); 
        Calendar calendar = new GregorianCalendar(); 
        int curDay = calendar.get(Calendar.DAY_OF_WEEK); 
        calendar.setTime(date); 
        if (curDay == 1) { 
            calendar.add(GregorianCalendar.DATE, -6); 
        } else { 
            calendar.add(GregorianCalendar.DATE, 2 - curDay); 
        } 
        Log.d("", "===calendar:"+calendar);
        return calendar; 
		
		
//		if(null == time){
//			time = Calendar.getInstance();
//		}
//		int dayOfWeek = time.get(Calendar.DAY_OF_WEEK);
//		int plus = 0;
//		if(dayOfWeek == 1){
//			plus = -6;
//		}else{
//			plus = 2 - dayOfWeek;
//		}
//		Log.d("", "===plus:"+plus+";dayOfWeek:"+dayOfWeek);
//		GregorianCalendar cd = (GregorianCalendar)time;
//		cd.add(GregorianCalendar.DATE, plus);
//		time.setTime(cd.getTime());
//		Log.d("", "===cd:"+cd);
//		return cd;
	}
	
	public static Drawable getDrawableFromSD(String path){
		String imageName = Utility.md5(path);//Common.md5(source);
        String sdcardPath = Environment.getExternalStorageDirectory().toString(); // 获取SDCARD的路径
        // 获取图片后缀名
        String[] ss = path.split("\\.");
        String ext = ss[ss.length - 1];
        // 最终图片保持的地址
        String savePath = sdcardPath + "/" + AppConstants.appname + "/"
                + imageName + "." + ext;
 
        File file = new File(savePath);
        if (file.exists()) {
            // 如果文件已经存在，直接返回
            Drawable drawable = Drawable.createFromPath(savePath);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            return drawable;
        }
        return null;
	}
	
	public static void saveFile(String path,byte[] data){
		String imageName = Utility.md5(path);//Common.md5(source);
        String sdcardPath = Environment.getExternalStorageDirectory().toString(); // 获取SDCARD的路径
        // 获取图片后缀名
        String[] ss = path.split("\\.");
        String ext = ss[ss.length - 1];
        // 最终图片保持的地址
        String savePath = sdcardPath + "/" + AppConstants.appname + "/"
                + imageName + "." + ext;
        if(null != data){
        	try {
				InputStream in = new ByteArrayInputStream(data);
				File file = new File(savePath);
				String basePath = file.getParent();
				File basePathFile = new File(basePath);
				if (!basePathFile.exists()) {
				    basePathFile.mkdirs();
				}
				file.createNewFile();
				FileOutputStream fileout = new FileOutputStream(file);
				byte[] buffer = new byte[4 * 1024];
				while (in.read(buffer) != -1) {
				    fileout.write(buffer);
				}
				fileout.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}

}
