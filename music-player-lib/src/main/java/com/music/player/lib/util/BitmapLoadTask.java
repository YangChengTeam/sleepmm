package com.music.player.lib.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import com.music.player.lib.R;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * TinyHung@Outlook.com
 * 2018/1/21
 * 加载URL图片
 */

public class BitmapLoadTask extends AsyncTask<String,Void,Bitmap>{

    private static BitmapLoadTask mInstance;
    private final RemoteViews mRemoteViews;

    public BitmapLoadTask(RemoteViews remoteViews) {
        this.mRemoteViews=remoteViews;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        if(null!=strings&&strings.length>0){
            try {
                //从网络读取图片流转换成Bitmap
                URL imageUrl=new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) imageUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(20000);
                urlConnection.setConnectTimeout(20000);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                if(null!=inputStream){
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    return bitmap;
                }
                inputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(null!=bitmap&&null!=mRemoteViews){
            mRemoteViews.setImageViewBitmap(R.id.nc_icon,bitmap);
        }
    }

    public static  synchronized BitmapLoadTask getInstance(RemoteViews remoteViews){
        synchronized (BitmapLoadTask.class){
            if(null==mInstance){
                mInstance=new BitmapLoadTask(remoteViews);
            }
        }
        return mInstance;
    }

    /**
     * 加载网络图片
     * @param musicCover
     */
    public static void loadImage(String musicCover) {
        mInstance.cancel(true);//尝试取消正在进行的任务
        mInstance.execute(musicCover);
    }
}
