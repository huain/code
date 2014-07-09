package org.hjin.upoa.util.net;

public class MyHttpException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/** HTTP请求出错时，服务器返回的错误状态码 */
	private final int mStatusCode;

	/**
	 * 构造函数。
	 * 
	 * @param message
	 *            HTTP请求出错时，服务器返回的字符串
	 * @param statusCode
	 *            HTTP请求出错时，服务器返回的错误状态码
	 */
	public MyHttpException(String message, int statusCode) {
		super(message);
		mStatusCode = statusCode;
	}

	/**
	 * HTTP请求出错时，服务器返回的错误状态码。
	 * 
	 * @return 服务器返回的错误状态码
	 */
	public int getStatusCode() {
		return mStatusCode;
	}

}
