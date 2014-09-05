package org.hjin.upoa.util.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AsyncRunner {
	
	/**
     * 根据 URL 异步请求数据，并在获取到数据后通过 {@link RequestListener} 
     * 接口进行回调。请注意：该回调函数是运行在后台线程的。
     * 另外，在调用该方法时，成功时，会调用 {@link RequestListener#onComplete}，
     * {@link RequestListener#onComplete4binary} 并不会被回调，请区分 {@link #request4Binary}。
     * 
     * @param url        服务器地址
     * @param params     存放参数的容器
     * @param httpMethod "GET" or "POST"
     * @param listener   回调对象
     */
    public static void request(
    		final int flag,
            final String url, 
            final MyParameters params, 
            final String httpMethod, 
            final RequestListener listener) {
        
        new Thread() {
            @Override
            public void run() {
            	
            	try {
					String resp = HttpManager.openUrl(
					        url, httpMethod, params, params.getValue("pic"));
					if (listener != null) {
					    listener.onComplete(resp,flag);
					}
				} catch (MyHttpException e) {
					if (listener != null) {
						listener.onError(e,flag);
	                }
					e.printStackTrace();
				} catch (IOException e) {
					if (listener != null) {
	                	listener.onIOException(e, flag);
	                }
					e.printStackTrace();
				}
            }
        }.start();

    }
    
    /**
     * 根据 URL 异步请求数据，并在获取到数据后通过 {@link RequestListener} 
     * 接口进行回调。请注意：该回调函数是运行在后台线程的。
     * 另外，在调用该方法时，成功时，会调用 {@link RequestListener#onComplete4binary}，
     * {@link RequestListener#onComplete} 并不会被回调，请区分 {@link #request}。
     * 
     * @param url        服务器地址
     * @param params     存放参数的容器
     * @param httpMethod "GET" or "POST"
     * @param listener   回调对象
     */
    public static void request4Binary(
    		final int flag,
            final String url,
            final MyParameters params, 
            final String httpMethod, 
            final RequestListener listener) {
        
        new Thread() {
            @Override
            public void run() {
            	try {
					ByteArrayOutputStream resp = HttpManager.openUrl4Binary(
					        url, httpMethod, params, params.getValue("pic"));
					if (listener != null) {
					    listener.onComplete4binary(resp,flag);
					}
				} catch (MyHttpException e) {
					if (listener != null) {
                        listener.onError(e,flag);
                    }
				} catch (IOException e) {
					if (listener != null) {
                        listener.onIOException(e,flag);
                    }
				}
            }
        }.start();
    }
    

}
