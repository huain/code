package org.hjin.upoa.util.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * �����������ʱ����Ļص��ӿڡ�
 * TODO����To be design...��
 * 
 * @author SINA
 * @since 2013-11-05
 */
public interface RequestListener {
    
    /**
     * ����ȡ���������ص��ַ����󣬸ú��������á�
     * 
     * @param response ���������ص��ַ���
     * @param flag �����ʶ
     */
    public void onComplete(String response,int flag);

    /**
     * ����ȡ���������ص��ļ����󣬸ú��������á�
     * 
     * @param responseOS ���������ص��ļ���
     * @param flag �����ʶ
     */
    public void onComplete4binary(ByteArrayOutputStream responseOS,int flag);

    /**
     * �����ʷ�����ʱ������ I/O �쳣ʱ���ú��������á�
     * 
     * @param e I/O �쳣����
     */
    public void onIOException(IOException e,int flag);

    /**
     * �����ʷ�����ʱ�������쳣ʱ���ú��������á�
     * 
     * @param e ΢���Զ����쳣����
     */
    public void onError(MyHttpException e,int flag);
}

