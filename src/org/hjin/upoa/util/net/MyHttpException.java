package org.hjin.upoa.util.net;

public class MyHttpException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/** HTTP�������ʱ�����������صĴ���״̬�� */
	private final int mStatusCode;

	/**
	 * ���캯����
	 * 
	 * @param message
	 *            HTTP�������ʱ�����������ص��ַ���
	 * @param statusCode
	 *            HTTP�������ʱ�����������صĴ���״̬��
	 */
	public MyHttpException(String message, int statusCode) {
		super(message);
		mStatusCode = statusCode;
	}

	/**
	 * HTTP�������ʱ�����������صĴ���״̬�롣
	 * 
	 * @return ���������صĴ���״̬��
	 */
	public int getStatusCode() {
		return mStatusCode;
	}

}
