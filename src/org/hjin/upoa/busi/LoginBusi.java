package org.hjin.upoa.busi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.util.Utility;
import org.hjin.upoa.util.net.HttpManager;
import org.hjin.upoa.util.net.MyCookie;
import org.hjin.upoa.util.net.NetStateManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Context;
import android.util.Log;

/**
 * 登录业务处理
 * 
 * @author Administrator
 * 
 */
public class LoginBusi extends BaseBusi {
	
	private final String TAG = "LoginBusi";
	
	private Context context;
	
	public LoginBusi(Context context){
		this.context = context;
	}
	
	/**
	 * 登录前，获取验证图片
	 * @param username 用户名
	 * @param password 密码
	 * @param listener
	 * @return
	 */
	public void loginBefore(final String username, final String password, final LoginListener listener) {
		new Thread() {
			@Override
			public void run() {
				ByteArrayOutputStream picStream = null;
				try {
					sleep(1000);
					HttpClient client = HttpManager.getNewHttpClient();
					HttpUriRequest request = null;
					client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
							NetStateManager.getAPN());
					MyCookie mycookie = MyCookie.getInstance();
					String username_base64 = Utility.encodeBase64(username+"@yes")+"";
					mycookie.put("pasm_username", username_base64);
					mycookie.put("pasm_password", "");
					if(Utility.isBlank(AppConstants.sLt)){
						HttpGet get1 = new HttpGet(AppConstants.sUrl_login1);
						request = get1;
						request.setHeader("Cookie", mycookie.getCookieString());
						HttpResponse response1 = client.execute(request);
						Header[] firstCookie = response1.getHeaders("Set-Cookie");
						String content1 = readHttpResponse(response1);
						Document doc = Jsoup.parse(content1);
						Element e = doc.select("#lt").first();
						AppConstants.sLt = e.attr("value");
						Log.v(TAG, "===lt:"+AppConstants.sLt);
						if(null != firstCookie && firstCookie.length>0){
							Log.v(TAG, "====firstCookie:"+firstCookie[0].getValue());
							String JSESSIONID = firstCookie[0].getValue();
							JSESSIONID = JSESSIONID.substring(11, JSESSIONID.indexOf(";"));
							AppConstants.sJessionId = JSESSIONID;
						}	
						mycookie.put("JSESSIONID", AppConstants.sJessionId);
						Log.v(TAG, "====JSESSIONID:"+AppConstants.sJessionId);
						HttpGet get2 = new HttpGet(AppConstants.sUrl_login2);
						request = get2;
						request.setHeader("Cookie", mycookie.getCookieString());
						HttpResponse response2 = client.execute(request);
						picStream =readBytesFromHttpResponse(response2);
						if(null != picStream){
							listener.onValidateCode(picStream);
						}
					}
					
				} catch (ClientProtocolException e) {
					Log.d(TAG, "===ClientProtocolException");
					listener.onNetException();
					e.printStackTrace();
				} catch (IOException e) {
					Log.d(TAG, "===IOException");
					listener.onNetException();
					e.printStackTrace();
				} catch(Exception e){
					if(e.getClass() != IOException.class && e.getClass() != ClientProtocolException.class){
						Log.d(TAG, "===Exception");
						listener.onServerException();
					}
					
				}
			}

		}.start();
	}
	
	/**
	 * 登录 
	 * @param username
	 * @param password
	 * @param validateCode
	 * @param listener
	 */
	public void login(final String username, final String password, final String validateCode, final LoginListener listener) {
		new Thread() {
			@Override
			public void run() {
				DefaultHttpClient client = HttpManager.getNewHttpClient();
				HttpUriRequest request = null;
				client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
						NetStateManager.getAPN());
				MyCookie mycookie = MyCookie.getInstance();
				mycookie.put("P_UUID", "");
				try {
					HttpGet get3 = new HttpGet(AppConstants.sUrl_login3 + validateCode);
					request = get3;
					get3.setHeader("Cookie", mycookie.getCookieString());
					HttpResponse response3 = client.execute(request);
					String validateResult  = readHttpResponse(response3);
					Log.v(TAG, "====validateResult:"+validateResult);
					if(null != validateResult && validateResult.indexOf("result:true")>-1){
						HttpPost post4 = new HttpPost(AppConstants.sUrl_login4 + AppConstants.sJessionId);
					    request = post4;
					    request.setHeader("Content-Type", "application/x-www-form-urlencoded");
					    request.setHeader("Referer", AppConstants.sReq_Referer_login4);
					    request.setHeader("Cookie", mycookie.getCookieString());
					    request.setHeader("Origin", AppConstants.sReq_Origin_login4);
						List<NameValuePair> postparam = new ArrayList<NameValuePair>();
						postparam.add(new BasicNameValuePair("lt",AppConstants.sLt));
						postparam.add(new BasicNameValuePair("_eventId","submit"));
						postparam.add(new BasicNameValuePair("timezone","-480"));
						postparam.add(new BasicNameValuePair("account",username));
						postparam.add(new BasicNameValuePair("cipher",password));
						postparam.add(new BasicNameValuePair("checkCode",validateCode));
						postparam.add(new BasicNameValuePair("checkCodeTemp","4533"));
						HttpEntity httpentity = new UrlEncodedFormEntity(postparam,"utf-8");
						post4.setEntity(httpentity);
					    HttpResponse response4 = client.execute(request);
					    Log.v(TAG, "====response4:"+response4.getStatusLine().getStatusCode());
					    Header[] headers2 = response4.getHeaders("Set-Cookie");
					    String CASTGC = headers2[1].getValue();
					    CASTGC = CASTGC.substring(7, CASTGC.indexOf(";"));
					    mycookie.put("CASTGC", CASTGC);
					    Log.v(TAG, "====CASTGC:"+CASTGC);
					    
					    HttpGet get5 = new HttpGet(AppConstants.sUrl_login5);
						get5.setHeader("Referer", AppConstants.sReq_Referer_login5+AppConstants.sJessionId);
						client.execute(get5);
						AppConstants.sCookieStore = client.getCookieStore();
						
						HttpPost post6 = new HttpPost("http://60.247.77.194/ultrapmo/portal/getUserOnlineCount.action");
						request = post6;
						mycookie.clear();
						HttpResponse response6 = client.execute(request);
						Log.v(TAG, readHttpResponse(response6));
						AppConstants.sLt = "";
						listener.onCompleteLogin();
					}else{
//						AppConstants.sLt = "";
						listener.onValidateCodeError();
					}
				} catch (ClientProtocolException e) {
					listener.onNetException();
					e.printStackTrace();
				} catch (IOException e) {
					listener.onNetException();
					e.printStackTrace();
				} catch(Exception e) {
					if(e.getClass() != IOException.class && e.getClass() != ClientProtocolException.class){
						listener.onServerException();
					}
				}
				
			}

		}.start();
	}
	
	
	
	
//	/**
//	 * 读取验证码图片
//	 * @param client
//	 * @param url
//	 * @param headerParams
//	 * @return
//	 */
//	private static  ByteArrayOutputStream getValidateCode(HttpClient client,String url,Map<String,String> headerParams) throws Exception{
//		ByteArrayOutputStream result = null;
//		HttpGet get = new HttpGet(url);
//		HttpUriRequest request = null;
//		request = get;
//		// 设置请求cookie
//		if(null != headerParams && headerParams.size()>0){
//			int i=0;
//			Set<Entry<String,String>> params = headerParams.entrySet();
//			StringBuffer cookieBuffer = new StringBuffer("");
//			for(Entry<String,String> h:params){
//				cookieBuffer.append(h.getKey());
//				cookieBuffer.append("=");
//				cookieBuffer.append(h.getValue());
//				cookieBuffer.append(";");
//				i++;
//			}
//			cookieBuffer.deleteCharAt(cookieBuffer.lastIndexOf(";"));
//			request.setHeader("Cookie", cookieBuffer.toString());
//		}
//		HttpResponse response = client.execute(request);
////		StatusLine status = response.getStatusLine();
////		int statusCode = status.getStatusCode();
////		if(statusCode != 200){
////			listener.onServerException();
////			return null;
////		}
//		result = readBytesFromHttpResponse(response);
//		return result;
//	}
	
	/**
     * 读取HttpResponse数据
     * 
     * @param response
     * @return
     */
    private static String readHttpResponse(HttpResponse response) {
        String result = "";
        HttpEntity entity = response.getEntity();
        InputStream inputStream;
        try {
            inputStream = entity.getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();

            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null && header.getValue().toLowerCase().indexOf("gzip") > -1) {
                inputStream = new GZIPInputStream(inputStream);
            }

            int readBytes = 0;
            byte[] sBuffer = new byte[512];
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }
            result = new String(content.toByteArray(), "UTF-8");
            return result;
        } catch (IllegalStateException e) {
        } catch (IOException e) {
        }
        return result;
    }
	
	/**
     * 读取HttpResponse 字节流ByteArrayOutputStream
     * 
     * @param response
     * @return
     */
    private static ByteArrayOutputStream readBytesFromHttpResponse(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        InputStream inputStream;
        try {
            inputStream = entity.getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();

            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null && header.getValue().toLowerCase().indexOf("gzip") > -1) {
                inputStream = new GZIPInputStream(inputStream);
            }

            int readBytes = 0;
            byte[] sBuffer = new byte[512];
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }
            return content;
        } catch (IllegalStateException e) {
        } catch (IOException e) {
        	
        }
        return null;
    }

	public interface LoginListener {
		/**
		 * 登录开始
		 */
		public void onStartLogin();

		/**
		 * 验证码下载完成
		 * @param bos 验证码图片数据
		 */
		public void onValidateCode(ByteArrayOutputStream bos);
		
		public void onValidateCodeError();

		/**
		 * 登录完成
		 */
		public void onCompleteLogin();
		
		public void onServerException();
		
		public void onNetException();
	}

}
