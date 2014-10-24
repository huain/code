package org.hjin.upoa.util.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AsyncRunner {
	
	/**
     * ���� URL �첽�������ݣ����ڻ�ȡ�����ݺ�ͨ�� {@link RequestListener} 
     * �ӿڽ��лص�����ע�⣺�ûص������������ں�̨�̵߳ġ�
     * ���⣬�ڵ��ø÷���ʱ���ɹ�ʱ������� {@link RequestListener#onComplete}��
     * {@link RequestListener#onComplete4binary} �����ᱻ�ص��������� {@link #request4Binary}��
     * 
     * @param url        ��������ַ
     * @param params     ��Ų���������
     * @param httpMethod "GET" or "POST"
     * @param listener   �ص�����
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
     * ���� URL �첽�������ݣ����ڻ�ȡ�����ݺ�ͨ�� {@link RequestListener} 
     * �ӿڽ��лص�����ע�⣺�ûص������������ں�̨�̵߳ġ�
     * ���⣬�ڵ��ø÷���ʱ���ɹ�ʱ������� {@link RequestListener#onComplete4binary}��
     * {@link RequestListener#onComplete} �����ᱻ�ص��������� {@link #request}��
     * 
     * @param url        ��������ַ
     * @param params     ��Ų���������
     * @param httpMethod "GET" or "POST"
     * @param listener   �ص�����
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
