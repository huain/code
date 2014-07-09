package org.hjin.upoa.ui.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hjin.upoa.R;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.util.Utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.widget.TextView;

public class MyImageGetter implements ImageGetter {
	
	private final String TAG = "MyImageGetter";
	
	private final int GETIMAGE = 1;
	
	private Context mContext;
	
	private TextView mTv;
	
//	private URLDrawable mDrawable;
	
	private String mSavePath;
	
	private boolean mIsGetNetPic;
	
	public MyImageGetter(Context context, TextView tv, boolean isGetNetPic){
		this.mContext = context;
		this.mTv = tv;
		this.mIsGetNetPic = isGetNetPic;
	}
	
//	private Handler mHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			switch(msg.what){
//			case 1:{
//				String savepath = msg.getData().get("pic").toString();
//				Drawable drawable = Drawable.createFromPath(savepath);
//				//Drawable drawable = (Drawable)msg.getData().get("pic");
//				if(null != drawable){
//					mDrawable.setDrawable(drawable);
//					mTv.setText(mTv.getText());
//				}
//			}break;
//			default:break;
//			}
//		}
//	};

	@Override
	public Drawable getDrawable(String source) {
        // TODO Auto-generated method stub
        // 将source进行MD5加密并保存至本地
        String imageName = Utility.md5(source);//Common.md5(source);
		Log.v(TAG, "===source:"+source);
		Log.v(TAG, "===imageName:"+imageName);
        String sdcardPath = Environment.getExternalStorageDirectory().toString(); // 获取SDCARD的路径
        // 获取图片后缀名
        String[] ss = source.split("\\.");
        String ext = ss[ss.length - 1];
 
        // 最终图片保持的地址
        mSavePath = sdcardPath + "/" + mContext.getPackageName() + "/"
                + imageName + "." + ext;
 
        File file = new File(mSavePath);
        if (file.exists()) {
            // 如果文件已经存在，直接返回
            Drawable drawable = Drawable.createFromPath(mSavePath);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            return drawable;
        }
 
        // 不存在文件时返回默认图片，并异步加载网络图片
        Resources res = mContext.getResources();
        URLDrawable mDrawable = new URLDrawable(res.getDrawable(R.drawable.pic_default));
        //new ImageAsync(drawable).execute(savePath, source);
//        ImageAsync imageAsync = new ImageAsync(mSavePath,source,mHandler);
//        if(AppConstants.isnonepic && mContext.g){
//        	
//        }
        if(mIsGetNetPic){
        	 new ImageAsync(mDrawable).execute(mSavePath, source);
        }
        return mDrawable;
	}
	
//	public class ImageAsync implements RequestListener{
//		
//		private Handler mHandler;
//		
//		private TextView mTv;
//		
//		private URLDrawable mURLDrawable;
//		
//		private String mSavepath;
//		
//		public ImageAsync(String savepath,String source,Handler handler){
//			this.mHandler = handler;
//			
////			this.mTv = tv;
////			
////			this.mURLDrawable = urlDrawable;
//			
//			this.mSavepath = savepath;
//			
//			if (source.startsWith("/ultrapmo")) {
//				source = AppConstants.sHost_pmo + source;
//			}
//			MyParameters params = new MyParameters();
//			AsyncRunner.request4Binary(GETIMAGE, source, params, "GET", this);
//		}
//		
//		@Override
//		public void onComplete(String response, int flag) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void onComplete4binary(ByteArrayOutputStream responseOS, int flag) {
//			switch (flag){
//			case GETIMAGE:{
//				if(null != responseOS){
//					File file = new File(mSavePath);
//		            String basePath = file.getParent();
//		            File basePathFile = new File(basePath);
//		            if (!basePathFile.exists()) {
//		                basePathFile.mkdirs();
//		            }
//		            FileOutputStream fileout;
//		            try {
//						file.createNewFile();
//						fileout = new FileOutputStream(file);
//						fileout.write(responseOS.toByteArray());
//						fileout.flush();
//						responseOS.close();
//						fileout.close();
//					} catch (FileNotFoundException e) {
//						Log.d(TAG, "===FileNotFoundException");
//						e.printStackTrace();
//					} catch (IOException e) {
//						Log.d(TAG, "===IOException");
//						e.printStackTrace();
//					} 
//
////		            Drawable drawable = Drawable.createFromPath(mSavepath);
//		            Log.d(TAG, "===mSavePath:"+mSavePath);
//		            Message msg = mHandler.obtainMessage();
//		            msg.what = 1;
//		            Bundle data = new Bundle();
//		            data.putString("pic", mSavepath);
//		            msg.setData(data);
//		            msg.sendToTarget();
////		            mURLDrawable.setDrawable(drawable);
////		            mTv.refreshDrawableState();
//					//mTv.setText(mTv.getText());
//				}
//			};break;
//			default:break;
//			}
//		}
//
//		@Override
//		public void onIOException(IOException e) {
//			// TODO Auto-generated method stub
//			Log.d(TAG, "===onIOException");
//		}
//
//		@Override
//		public void onError(Exception e) {
//			// TODO Auto-generated method stub
//			Log.d(TAG, "===onError");
//			e.printStackTrace();
//		}
//	}
	
    private class ImageAsync extends AsyncTask<String, Integer, Drawable> {
    	 
        private URLDrawable drawable;
 
        public ImageAsync(URLDrawable drawable) {
            this.drawable = drawable;
        }
 
        @Override
        protected Drawable doInBackground(String... params) {
            // TODO Auto-generated method stub
            String savePath = params[0];
            String url = params[1];
			if (url.startsWith("/ultrapmo")) {
				url = AppConstants.sHost_pmo + url;
			}
 
            InputStream in = null;
            try {
                // 获取网络图片
                HttpGet http = new HttpGet(url);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) client.execute(http);
                BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(
                        response.getEntity());
                in = bufferedHttpEntity.getContent();
 
            } catch (Exception e) {
                try {
                    if (in != null)
                        in.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
 
            if (in == null)
                return drawable;
 
            try {
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
 
                Drawable mDrawable = Drawable.createFromPath(savePath);
                return mDrawable;
            } catch (Exception e) {
                // TODO: handle exception
            }
            return drawable;
        }
 
        @Override
        protected void onPostExecute(Drawable result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                drawable.setDrawable(result);
                //CharSequence a = mTv.getText();
                mTv.setText(mTv.getText()); // 通过这里的重新设置 TextView 的文字来更新UI
            }
        }
 
    }
	
	
	public class URLDrawable extends BitmapDrawable {
    	 
        private Drawable drawable;
 
        public URLDrawable(Drawable defaultDraw) {
            setDrawable(defaultDraw);
        }
 
        public void setDrawable(Drawable nDrawable) {
            drawable = nDrawable;
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            setBounds(0, 0, drawable.getIntrinsicWidth(),
                  drawable.getIntrinsicHeight());
        }
 
        @Override
        public void draw(Canvas canvas) {
            // TODO Auto-generated method stub
            drawable.draw(canvas);
        }
 
    }

}
