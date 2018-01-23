package com.yc.sleepmm.base.ui.fragment;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.ui.dialog.LoadingProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TinyHung@Outlook.com
 * 2018/1/22.
 * 音乐列表统一父类，需要加载数据失败时候 重试功能，请实现onRefresh()方法
 */

public abstract class MusicBaseFragment extends Fragment {

    protected AppCompatActivity context;
    //数据加载失败界面
    @BindView(R.id.ll_error_view)
    LinearLayout mLl_error_view;
    //数据加载中界面
    @BindView(R.id.ll_loading_view)
    LinearLayout mLl_loading_view;
    //加载中动画
    private AnimationDrawable mAnimationDrawable;
    private View mChildView;
    private LoadingProgressView mLoadingProgressedView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context= (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music_base, null);
        mChildView = View.inflate(getActivity(), getLayoutId(), null);
        if(null!= mChildView){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mChildView.setLayoutParams(params);
            RelativeLayout contentView = (RelativeLayout) rootView.findViewById(R.id.re_conttent_view);
            contentView.addView(mChildView);
        }
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView iv_loading_icon = getView(R.id.iv_loading_icon);
        mAnimationDrawable = (AnimationDrawable) iv_loading_icon.getDrawable();
        mLl_error_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        //默认显示加载中的状态
        showLoadingView();
        initViews();
    }


    /**
     * 显示加载中
     */
    protected void showLoadingView(){
        if(null==mChildView) return;
        if(null!=mChildView&&mChildView.getVisibility()!=View.GONE){
            mChildView.setVisibility(View.GONE);
        }
        if(null!=mLl_error_view&&mLl_error_view.getVisibility()!=View.GONE){
            mLl_error_view.setVisibility(View.GONE);
        }
        if(null!=mLl_loading_view&&mLl_loading_view.getVisibility()!=View.VISIBLE){
            mLl_loading_view.setVisibility(View.VISIBLE);
        }
        if(null!=mAnimationDrawable&&!getActivity().isFinishing()&&!mAnimationDrawable.isRunning()){
            mAnimationDrawable.start();
        }
    }


    /**
     * 显示界面内容
     */
    protected void showContentView() {
        if(null==mChildView) return;
        if(null!=mAnimationDrawable&&null!=context&&!context.isFinishing()&&mAnimationDrawable.isRunning()){
            mAnimationDrawable.stop();
        }
        if(null!=mLl_loading_view&&mLl_loading_view.getVisibility()!=View.GONE){
            mLl_loading_view.setVisibility(View.GONE);
        }
        if(null!=mLl_error_view&&mLl_error_view.getVisibility()!=View.GONE){
            mLl_error_view.setVisibility(View.GONE);
        }
        if(null!=mChildView&&null!=mChildView&&mChildView.getVisibility()!=View.VISIBLE){
            mChildView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 显示加载失败
     */
    protected void showLoadingErrorView() {
        if(null==mChildView) return;
        if(null!=mAnimationDrawable&&null!=context&&!context.isFinishing()&&mAnimationDrawable.isRunning()){
            mAnimationDrawable.stop();
        }
        if(null!=mLl_loading_view&&mLl_loading_view.getVisibility()!=View.GONE){
            mLl_loading_view.setVisibility(View.GONE);
        }
        if(null!=mChildView&&null!=mChildView&&mChildView.getVisibility()!=View.GONE){
            mChildView.setVisibility(View.GONE);
        }
        if(null!=mLl_error_view&&mLl_error_view.getVisibility()!=View.VISIBLE){
            mLl_error_view.setVisibility(View.VISIBLE);
        }
    }



    protected abstract void initViews();

    /**
     * 在这里实现Fragment数据的缓加载.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    protected void onInvisible() {

    }

    protected void onVisible() {

    }


    /**
     * 子类可实现此登录方法
     */
    protected void onLogin() {

    }

    protected <T extends View> T getView(int id) {
        if(null==getView()) return null;
        return (T) getView().findViewById(id);
    }

    /**
     * 布局
     */
    public abstract int getLayoutId();

    /**
     * 加载失败后点击后的操作
     */
    protected void onRefresh() {

    }

    /**
     * 显示进度框
     * @param message
     * @param isProgress
     */
    protected void showProgressDialog(String message,boolean isProgress){
        if(null==mLoadingProgressedView){
            mLoadingProgressedView = new LoadingProgressView(getActivity(),isProgress);
        }
        if(!getActivity().isFinishing()){
            mLoadingProgressedView.setMessage(message);
            mLoadingProgressedView.show();
        }
    }

    /**
     * 关闭进度框
     */
    protected void closeProgressDialog(){
        if(null!=mLoadingProgressedView&&mLoadingProgressedView.isShowing()&&!getActivity().isFinishing()){
            mLoadingProgressedView.dismiss();
            mLoadingProgressedView=null;
        }
    }


    protected boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        context=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }
}
