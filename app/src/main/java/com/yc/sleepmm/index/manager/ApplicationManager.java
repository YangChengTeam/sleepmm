package com.yc.sleepmm.index.manager;

import android.os.Environment;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.index.util.ACache;
import com.yc.sleepmm.index.util.FileUtils;
import java.io.File;
import java.lang.ref.WeakReference;

/**
 * TinyHung@Outlook.com
 * 2017/12/12.
 */

public class ApplicationManager {

    private static WeakReference<ApplicationManager> mInstanceWeakReference=null;//自己
    public WeakReference<ACache> mACacheWeakReference=null;//缓存

    public static synchronized ApplicationManager getInstance(){
        synchronized (ApplicationManager.class){
            if(null==mInstanceWeakReference||null==mInstanceWeakReference.get()){
                ApplicationManager applicationManager=new ApplicationManager();
                mInstanceWeakReference=new WeakReference<ApplicationManager>(applicationManager);
            }
        }
        return mInstanceWeakReference.get();
    }

    /**
     * 全局初始化需要传入
     * @param mACache
     */
    public void setCacheExample(ACache mACache) {
        if(null==mACacheWeakReference||null==mACacheWeakReference.get()){
            mACacheWeakReference=new WeakReference<ACache>(mACache);
        }
    }

    /**
     * 缓存的对象实例
     * @return
     */
    public ACache getCacheExample(){
        if(null==mACacheWeakReference||null==mACacheWeakReference.get()){
            ACache aCache=ACache.get(APP.getInstance().getApplicationContext());
            mACacheWeakReference=new WeakReference<ACache>(aCache);
        }
        return mACacheWeakReference.get();
    }





    /**
     * 获取视频缓存的目录
     * @return
     */
    public String getVideoCacheDir() {
        String cachePath = FileUtils.getFileDir(APP.getInstance().getApplicationContext());
        if(null==cachePath){
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File file= new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/.XinQu/Cache/Video/");
                if(!file.exists()){
                    file.mkdirs();
                }
                //使用内部缓存
                cachePath=file.getAbsolutePath();
            }
        }
        return cachePath;
    }





    public void onDestory(){
        if(null!=mACacheWeakReference){
            mACacheWeakReference.clear();
            mACacheWeakReference=null;
        }
        if(null!=mInstanceWeakReference){
            mInstanceWeakReference.clear();
            mInstanceWeakReference=null;
        }
    }
}
