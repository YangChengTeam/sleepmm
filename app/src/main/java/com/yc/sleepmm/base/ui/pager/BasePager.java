package com.yc.sleepmm.base.ui.pager;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.yc.sleepmm.R;
import java.security.InvalidParameterException;

/**
 * TinyHung@Outlook.com
 * 2017/9/11
 * 视频播放界面的Pager基类
 */

public abstract class BasePager{

    protected final Activity mContext;
    private View mChildView;

    public BasePager(Activity context){
        this.mContext=context;
        boolean flag=context instanceof Activity;
        if(!flag){
            throw new InvalidParameterException("请传入Activity类型的上下文");
        }
    }

    /**
     * 设置LayoutID
     * @param layoutID
     */
    public void setContentView(int layoutID){
        //父View
        View groupView = mContext.getLayoutInflater().inflate(R.layout.base_pager, null);
        //子View
        mChildView = View.inflate(mContext, R.layout.base_pager, (ViewGroup) groupView.getParent());
        //父内容容器
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mChildView.setLayoutParams(params);
        ((RelativeLayout) groupView.findViewById(R.id.view_content)).addView(mChildView);//添加至父容器
        initViews();
        initData();
    }

    public View getView() {
        return mChildView;
    }
    public abstract void initViews();
    public abstract void initData();
    protected void onDestroy(){}
    protected void onResume(){}
    protected void onPause(){}
}
