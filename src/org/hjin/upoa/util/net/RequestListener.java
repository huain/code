package org.hjin.upoa.util.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 发起访问请求时所需的回调接口。
 * TODO：（To be design...）
 * 
 * @author SINA
 * @since 2013-11-05
 */
public interface RequestListener {
    
    /**
     * 当获取服务器返回的字符串后，该函数被调用。
     * 
     * @param response 服务器返回的字符串
     * @param flag 请求标识
     */
    public void onComplete(String response,int flag);

    /**
     * 当获取服务器返回的文件流后，该函数被调用。
     * 
     * @param responseOS 服务器返回的文件流
     * @param flag 请求标识
     */
    public void onComplete4binary(ByteArrayOutputStream responseOS,int flag);

    /**
     * 当访问服务器时，发生 I/O 异常时，该函数被调用。
     * 
     * @param e I/O 异常对象
     */
    public void onIOException(IOException e,int flag);

    /**
     * 当访问服务器时，其它异常时，该函数被调用。
     * 
     * @param e 微博自定义异常对象
     */
    public void onError(MyHttpException e,int flag);
}

