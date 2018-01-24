package com.music.player.lib.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.music.player.lib.R;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicPlayerConfig;
import com.music.player.lib.constants.Constants;
import com.music.player.lib.listener.OnUserPlayerEventListener;
import com.music.player.lib.manager.MusicPlayerManager;
import com.music.player.lib.mode.GlideCircleTransform;
import com.music.player.lib.mode.PlayerAlarmModel;
import com.music.player.lib.mode.PlayerModel;
import com.music.player.lib.mode.PlayerSetyle;
import com.music.player.lib.mode.PlayerStatus;
import com.music.player.lib.util.Logger;
import com.music.player.lib.util.MusicPlayerUtils;
import com.music.player.lib.util.ToastUtils;
import java.util.Observable;
import java.util.Observer;

/**
 * TinyHung@Outlook.com
 * 2018/1/18
 * 音乐播放器控制器,这个自定义控制器实现了Observer接口，内部自己负责刷新正在播放的音乐，调用者需要注册EventListener事件来处理其他由播放器回调的功能
 */

public class MusicPlayerController extends FrameLayout implements Observer, OnUserPlayerEventListener {

    private  String TAG = "MusicPlayerController";
    private ImageView mIcPlayerCover;
    private ImageView mIcPlayMode;
    private TextView mTvMusicTitle;
    private MusicPlayerSeekBar mMusicPlayerSeekbar;
    private ImageView mIcAlarm;
    private ImageView mIcCollect;
    private Handler mHandler;
    private ImageView mBtnLast;
    private ImageView mBtnNext;
    private int mPlayerStyle=PlayerSetyle.PLAYER_STYLE_DEFAULT;//默认样式
    private ImageView mIcRandomPlay;
    private ImageView mBtnBack;
    private TextView mTvRandomPlay;
    private static int UI_COMPONENT_TYPE=Constants.UI_TYPE_HOME;
    private RequestOptions mOptions;
    private boolean isCollect;//是否已收藏，全局应该同步


    public MusicPlayerController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_music_player_controller, this);
        mHandler = new Handler();
        mBtnLast = (ImageView) findViewById(R.id.btn_last);
        mBtnNext = (ImageView) findViewById(R.id.btn_next);
        mIcPlayerCover = (ImageView) findViewById(R.id.ic_player_cover);
        mIcAlarm = (ImageView) findViewById(R.id.ic_alarm);
        mIcCollect = (ImageView) findViewById(R.id.ic_collect);
        mIcRandomPlay = (ImageView) findViewById(R.id.ic_random_play);
        mBtnBack = (ImageView) findViewById(R.id.btn_back);//返回按钮
        mTvRandomPlay = (TextView) findViewById(R.id.tv_random_play);
        //播放模式
        mIcPlayMode = (ImageView) findViewById(R.id.ic_play_mode);
        //随便听听
        LinearLayout btnRandomPlay = (LinearLayout) findViewById(R.id.btn_random_play);
        //标题
        mTvMusicTitle = (TextView) findViewById(R.id.tv_music_title);
        RelativeLayout btnPlayMode=(RelativeLayout) findViewById(R.id.btn_play_mode);
        RelativeLayout btn_alarm =(RelativeLayout)  findViewById(R.id.btn_alarm);
        RelativeLayout btn_player_collect = (RelativeLayout) findViewById(R.id.btn_player_collect);
        //进度条控制器
        mMusicPlayerSeekbar = (MusicPlayerSeekBar) findViewById(R.id.music_player_seekbar);
        //处理进度条Bar点击事件
        mMusicPlayerSeekbar.setOnClickListener(new MusicPlayerSeekBar.OnClickListener() {
            @Override
            public void onClickView() {
                Logger.d(TAG,"onClickView");
                boolean flag = MusicPlayerManager.getInstance().playPause();//暂停和播放
                if(!flag){
                    //通知所有UI组件，自动开始新的播放，如果UI组件愿意的话
                    MusicPlayerManager.getInstance().autoStartNewPlayTasks(UI_COMPONENT_TYPE,0);//首页播放器控件发出播放命令，所有注册OnUserPlayerEventListener监听的UI，需要根据业务需求来决定是否播放
                }
            }
        });
        //处理进度条seekTo事件
        mMusicPlayerSeekbar.setOnSeekbarChangeListene(new MusicPlayerSeekBar.OnSeekbarChangeListene() {
            @Override
            public void onSeekBarChange(long progress) {
                Logger.d(TAG,"seekProgress："+progress+"秒");
                ToastUtils.showCenterToast("已设定"+ MusicPlayerUtils.stringHoursForTime(progress)+"后停止");
                MusicPlayerManager.getInstance().setPlayerDurtion(progress);//设置定时结束播放的临界时间
            }
        });
        //默认闹钟最大2个小时
        mMusicPlayerSeekbar.setProgressMax(PlayerAlarmModel.TIME.MAX_TWO_HOUR);
        mMusicPlayerSeekbar.setProgress(MusicPlayerManager.getInstance().getPlayerAlarmDurtion());//使用用户最近设置的定时关闭时间

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.btn_last) {
                    MusicPlayerManager.getInstance().playLast();
                    //下一首
                } else if (i == R.id.btn_next) {
                    MusicPlayerManager.getInstance().playNext();

                    //改变播放模式
                } else if (i == R.id.btn_play_mode) {
                    MusicPlayerManager.getInstance().changePlayModel();

                    //改变闹钟定时关闭时长
                } else if (i == R.id.btn_alarm) {
                    MusicPlayerManager.getInstance().changeAlarmModel();

                    //收藏
                } else if (i == R.id.btn_player_collect) {
                    if (null != mOnClickEventListener) {
                        mOnClickEventListener.onEventCollect();
                    }

                    //随便听听
                } else if (i == R.id.btn_random_play) {
                    if (null != mOnClickEventListener) {
                        mOnClickEventListener.onEventRandomPlay();
                    }
                    //返回
                }else if(i==R.id.btn_back){
                    if(null!=mOnClickEventListener){
                        mOnClickEventListener.onBack();
                    }
                }
            }
        };
        mBtnLast.setOnClickListener(onClickListener);
        mBtnNext.setOnClickListener(onClickListener);
        mIcPlayMode.setOnClickListener(onClickListener);
        btnPlayMode.setOnClickListener(onClickListener);
        btn_alarm.setOnClickListener(onClickListener);
        btn_player_collect.setOnClickListener(onClickListener);
        btnRandomPlay.setOnClickListener(onClickListener);
        mBtnBack.setOnClickListener(onClickListener);
        MusicPlayerManager.getInstance().addPlayerStateListener(this);
        MusicPlayerManager.getInstance().checkedPlayerConfig();//检查播放器配置需要在注册监听之后进行,播放器的配置初始化是服务绑定成功后才会初始化的
    }

    /**
     * 观察者刷新，最好不要和onMusicPlayerState()同时处理
     * @param o 哪个被观察者发出的通知
     * @param arg 更新的内容
     */
    @Override
    public void update(Observable o, Object arg) {
        //为空无需处理
        if(null!=arg){
            MusicInfo musicInfo= (MusicInfo) arg;
            switch (musicInfo.getPlauStatus()) {
                //播放任务为空
                case PlayerStatus.PLAYER_STATUS_EMPOTY:
                    Logger.d(TAG,"播放为空");
                    break;
                //异步缓冲中
                case PlayerStatus.PLAYER_STATUS_ASYNCPREPARE:
                    Logger.d(TAG,"异步缓冲中");
                    if(null!=mTvMusicTitle) mTvMusicTitle.setText(musicInfo.getMusicTitle());
                    //封面
                    if(null!=mIcPlayerCover){
                        if(null==mOptions){
                            mOptions = new RequestOptions();
                            mOptions.error(R.drawable.ic_player_cover_default);
                            mOptions.diskCacheStrategy(DiskCacheStrategy.ALL);//缓存源资源和转换后的资源
                            mOptions.skipMemoryCache(true);//跳过内存缓存
                            mOptions.centerCrop();
                            mOptions.transform(new GlideCircleTransform(getContext()));
                        }
                        Glide.with(getContext()).load(musicInfo.getMusicCover()).apply(mOptions).thumbnail(0.1f).into(mIcPlayerCover);//音标
                    }
                    break;
                //开始播放中
                case PlayerStatus.PLAYER_STATUS_PLAYING:
                    Logger.d(TAG,"开始播放中");
                    if(null!=mMusicPlayerSeekbar){
                        mMusicPlayerSeekbar.setPlaying(true);
                    }
                    break;
                //暂停了播放
                case PlayerStatus.PLAYER_STATUS_PAUSE:
                    Logger.d(TAG,"暂停了播放");
                    if(null!=mMusicPlayerSeekbar){
                        mMusicPlayerSeekbar.setPlaying(false);
                    }
                    break;
                //结束、强行停止播放
                case PlayerStatus.PLAYER_STATUS_STOP:
                    Logger.d(TAG,"结束、强行停止播放");
                    if(null!=mMusicPlayerSeekbar)mMusicPlayerSeekbar.setPlaying(false);
                    if(null!=mTvMusicTitle) mTvMusicTitle.setText("");
                    //封面
                    if(null!=mIcPlayerCover)mIcPlayerCover.setImageResource(R.drawable.ic_player_cover_default);
                //播放失败
                case PlayerStatus.PLAYER_STATUS_ERROR:
                    Logger.d(TAG,"播放失败");
                    if(null!=mMusicPlayerSeekbar){
                        mMusicPlayerSeekbar.setPlaying(false);
                    }
                    break;
            }
        }
    }

    /**
     * 改变播放器模式
     * @param playModel
     * @param tips 是否土司提示用户
     */
    private void changePlayerModel(int playModel,boolean tips) {
        Logger.d(TAG,"播放器设置发生了变化：播放器随机模式："+playModel+",播放器样式："+mPlayerStyle+",是否弹窗：="+tips);
        int btnPlayModelIcon=R.drawable.ic_player_mode_sequence_for;
        String msg="列表循环";
        switch (playModel) {
            //列表顺序播放
//            case PlayerModel.PLAY_MODEL_SEQUENCE:
//                Logger.d(TAG,"列表顺序播放");
//                btnPlayModelIcon=R.drawable.ic_player_mode_sequence_for;
//                msg="列表顺序播放";
//                break;
            //列表循环播放
            case PlayerModel.PLAY_MODEL_SEQUENCE_FOR:
                msg="列表循环";
                btnPlayModelIcon=R.drawable.ic_player_mode_sequence_for;
                break;
            //列表随机播放
//            case PlayerModel.PLAY_MODEL_RANDOM:
//                msg="随机";
//                btnPlayModelIcon=R.drawable.ic_player_mode_sequence_for;
//                break;
            //单曲循环
            case PlayerModel.PLAY_MODEL_SINGER:
                msg="单曲循环";
                btnPlayModelIcon=R.drawable.ic_player_mode_singer;
                break;
        }
        //根据当前设置的样式设置播放器对应的主题色
        if(null!=mIcPlayMode){
            mIcPlayMode.setImageResource(btnPlayModelIcon);
            setImageColorFilter(mIcPlayMode,mPlayerStyle);
            if(tips) {
                ToastUtils.showCenterToast("已设定"+msg+"播放模式");
            }
        }
    }

    /**
     * 改变播放器闹钟定时
     * @param model
     * @param tips 是否土司提示
     */
    private void changePlayerAlarmModel(int model,boolean tips) {
        Logger.d(TAG,"播放器设置发生了变化：播放器闹钟模式："+model+",播放器样式："+mPlayerStyle+",是否弹窗：="+tips);
        int btnAlarmModelIcon=R.drawable.ic_player_alarm_clock_30;
        String msg="30分钟";
        switch (model){
            //10分钟
            case PlayerAlarmModel.PLAYER_ALARM_10:
                msg="10分钟";
                btnAlarmModelIcon=R.drawable.ic_player_alarm_clock_10;
                break;
            //20分钟
            case PlayerAlarmModel.PLAYER_ALARM_20:
                msg="20分钟";
                btnAlarmModelIcon=R.drawable.ic_player_alarm_clock_20;
                break;
            //0分钟
            case PlayerAlarmModel.PLAYER_ALARM_30:
                msg="30分钟";
                btnAlarmModelIcon=R.drawable.ic_player_alarm_clock_30;
                break;
            //一个小时
            case PlayerAlarmModel.PLAYER_ALARM_ONE_HOUR:
                msg="一个小时";
                btnAlarmModelIcon=R.drawable.ic_player_alarm_clock_30;
                break;
            //无限制分钟
            case PlayerAlarmModel.PLAYER_ALARM_NORMAL:
                msg="不限时长";
                btnAlarmModelIcon=R.drawable.ic_player_alarm_clock_0;
                break;
        }
        //根据当前设置的样式设置播放器对应的主题色
        if(null!=mIcAlarm){
            mIcAlarm.setImageResource(btnAlarmModelIcon);
            setImageColorFilter(mIcAlarm,mPlayerStyle);
            if(tips){
                ToastUtils.showCenterToast("已设定"+msg+"后停止播放");
            }
        }
    }


    /**
     * 设置收藏ICON
     * @param icon 收藏资源图标
     * @param isCollect 是否收藏
     */
    public void setCollectIcon(int icon,boolean isCollect){
        //这里的业务逻辑的播放歌曲所有的控制器列表都是同步的，不需要校验MusicID,如果需要，加入MusicInfo即可
        MusicPlayerManager.getInstance().changeCollectResult(icon,isCollect);
    }

    /**
     * 是否显示返回按钮，默认不显示
     * @param flag
     */
    public void setBackButtonVisibility(boolean flag){
        if(null!=mBtnBack) mBtnBack.setVisibility(flag?VISIBLE:GONE);
    }

    /**
     * 改变图片原有的颜色
     * @param icCollect
     * @param playerStyle
     */
    private void setImageColorFilter(ImageView icCollect, int playerStyle) {
        if(null==icCollect) return;
        int color=Color.rgb(168,177,204);
        switch (playerStyle) {
            //默认的
            case PlayerSetyle.PLAYER_STYLE_DEFAULT:
                color=Color.rgb(168,177,204);
                break;
            //黑色
            case PlayerSetyle.PLAYER_STYLE_BLACK:
                color=Color.rgb(105,105,105);
                break;
            //亮白色
            case PlayerSetyle.PLAYER_STYLE_WHITE:
                color=Color.rgb(168,177,204);
                break;
            //蓝色
            case PlayerSetyle.PLAYER_STYLE_BLUE:
                color=Color.rgb(18,148,246);
                break;
            //红色
            case PlayerSetyle.PLAYER_STYLE_RED:
                color=Color.rgb(255,78,92);
                break;
            //紫色
            case PlayerSetyle.PLAYER_STYLE_PURPLE:
                color=Color.rgb(47,47,99);
                break;
            //绿色
            case PlayerSetyle.PLAYER_STYLE_GREEN:
                color=Color.rgb(13,220,94);
                break;
        }
        icCollect.setColorFilter(color);
    }

    /**
     * 设置播放器样式
     * @param style PlayerSetyle
     * 见PlayerSetyle类中提供的样式
     */
    public void setPlayerStyle(int style) {
        this.mPlayerStyle=style;
        //默认主题
        int btnControllerColor;
        int btnFunctionColor;
        int titleColor;
        int progressBarBackgroundColor;
        int btnBackColor;
        switch (style) {
            //默认
            case PlayerSetyle.PLAYER_STYLE_DEFAULT:
                btnControllerColor=Color.rgb(168,177,204);//上一首，下一首
                btnFunctionColor=Color.rgb(168,177,204);//播放模式、闹钟、收藏
                titleColor=Color.rgb(255,255,255);//标题
                progressBarBackgroundColor=Color.rgb(255,255,255);//#FFFFFFFF进度条背景背景颜色
                btnBackColor=Color.rgb(255,255,255);//#FFFFFFFF返回按钮颜色
                break;
            //黑色
            case PlayerSetyle.PLAYER_STYLE_BLACK:
                btnControllerColor=Color.rgb(204,204,204);//#FFCCCCCC
                btnFunctionColor=Color.rgb(105,105,105);//#FF696969
                titleColor=Color.rgb(0,0,0);//#00000000
                progressBarBackgroundColor=Color.rgb(204,204,204);//#FFCCCCCC进度条背景背景颜色
                btnBackColor=Color.rgb(0,0,0);
                break;
            //白色
            case PlayerSetyle.PLAYER_STYLE_WHITE:
                btnControllerColor=Color.rgb(255,255,255);
                btnFunctionColor=Color.rgb(255,255,255);
                titleColor=Color.rgb(255,255,255);
                progressBarBackgroundColor=Color.rgb(255,255,255);
                btnBackColor=Color.rgb(255,255,255);
                break;
            //蓝色
            case PlayerSetyle.PLAYER_STYLE_BLUE:
                btnControllerColor=Color.rgb(18,148,246);//#FF1294F6
                btnFunctionColor=Color.rgb(40,159,249);//#FF289FF9
                titleColor=Color.rgb(18,148,246);
                progressBarBackgroundColor=Color.rgb(255,255,255);
                btnBackColor=Color.rgb(18,148,246);
                break;
            //红色
            case PlayerSetyle.PLAYER_STYLE_RED:
                btnControllerColor=Color.rgb(255,78,92);//#FFFF4E5C
                btnFunctionColor=Color.rgb(255,78,92);
                titleColor=Color.rgb(255,78,92);
                progressBarBackgroundColor=Color.rgb(255,255,255);
                btnBackColor=Color.rgb(255,78,92);
                break;
            //紫色
            case PlayerSetyle.PLAYER_STYLE_PURPLE:
                btnControllerColor=Color.rgb(47,47,99);//#FF2F2F63
                btnFunctionColor=Color.rgb(47,47,99);
                titleColor=Color.rgb(47,47,99);
                progressBarBackgroundColor=Color.rgb(255,255,255);
                btnBackColor=Color.rgb(47,47,99);
                break;
            //绿色
            case PlayerSetyle.PLAYER_STYLE_GREEN:
                btnControllerColor=Color.rgb(13,220,94);//#FF0DDC5E
                btnFunctionColor=Color.rgb(13,220,94);
                titleColor=Color.rgb(13,220,94);
                progressBarBackgroundColor=Color.rgb(255,255,255);
                btnBackColor=Color.rgb(13,220,94);
                break;
                default:
                    btnControllerColor=Color.rgb(168,177,204);//#FFA8B1CC
                    btnFunctionColor=Color.rgb(168,177,204);//#FFA8B1CC
                    titleColor=Color.rgb(255,255,255);//#FFFFFFFF
                    progressBarBackgroundColor=Color.rgb(255,255,255);//#FFFFFFFF
                    btnBackColor=Color.rgb(255,255,255);
        }
        if(null!=mBtnLast) mBtnLast.setColorFilter(btnControllerColor);
        if(null!=mBtnNext) mBtnNext.setColorFilter(btnControllerColor);
        if(null!=mIcCollect) mIcCollect.setColorFilter(btnFunctionColor);
        if(null!=mIcPlayMode) mIcPlayMode.setColorFilter(btnFunctionColor);
        if(null!=mIcAlarm) mIcAlarm.setColorFilter(btnFunctionColor);
        if(null!=mTvMusicTitle) mTvMusicTitle.setTextColor(titleColor);
        if(null!=mBtnBack) mBtnBack.setColorFilter(btnBackColor);
        //随便听听
        if(null!=mIcRandomPlay) mIcRandomPlay.setColorFilter(titleColor);
        if(null!=mTvRandomPlay) mTvRandomPlay.setTextColor(titleColor);
        if(null!=mMusicPlayerSeekbar) mMusicPlayerSeekbar.setProgressBackgroundColor(progressBarBackgroundColor);
    }

    /**
     * 持有此控制器的载体必须在所在的界面onDestroy()中调用此onDestroy();
     */
    public void onDestroy() {
        MusicPlayerManager.getInstance().detelePlayerStateListener(this);
        if(null!=mHandler){
            mHandler.removeMessages(0);
        }
        UI_COMPONENT_TYPE=0;
    }

    /**
     * 设置持有播放器控件的UI组件
     * @param uiTypeHome
     * 见：Constants
     */
    public void setUIComponentType(int uiTypeHome) {
        this.UI_COMPONENT_TYPE=uiTypeHome;
    }


    //=====================================监听来自播放器的回调=======================================

    /**
     * 播放器状态回调,建议不要和update()同时处理
     * @param musicInfo 当前播放的任务，未播放为空
     * @param stateCode 类别Code: 0：未播放 1：准备中 2：正在播放 3：暂停播放, 4：停止播放, 5：播放失败,详见：PlayerStatus类
     */
    @Override
    public void onMusicPlayerState(MusicInfo musicInfo, int stateCode) {

    }

    /**
     * 检查播放器播放任务回调
     * @param musicInfo
     * @param mediaPlayer
     * 每次检查播放器正在播放的任务，控制器都需要实时刷新
     */
    @Override
    public void checkedPlayTaskResult(MusicInfo musicInfo, KSYMediaPlayer mediaPlayer) {
        if(null!=musicInfo) Logger.d(TAG,"播放控制器收到任务检查回调,状态"+musicInfo.getPlauStatus());
        if(null!=mTvMusicTitle) mTvMusicTitle.setText(musicInfo.getMusicTitle());
        //封面
        if(null!=mIcPlayerCover) {
            if (null == mOptions) {
                mOptions = new RequestOptions();
                mOptions.error(R.drawable.ic_player_cover_default);
                mOptions.diskCacheStrategy(DiskCacheStrategy.ALL);//缓存源资源和转换后的资源
                mOptions.skipMemoryCache(true);//跳过内存缓存
                mOptions.centerCrop();
                mOptions.transform(new GlideCircleTransform(getContext()));
            }
            Glide.with(getContext()).load(musicInfo.getMusicCover()).apply(mOptions).thumbnail(0.1f).into(mIcPlayerCover);//音标
            if(null!=mMusicPlayerSeekbar) mMusicPlayerSeekbar.setPlaying(2==musicInfo.getPlauStatus()?true:false);//是否正在播放
        }
    }

    /**
     * 用户改变了播放模式
     * @param playModel
     */
    @Override
    public void changePlayModelResult(int playModel) {
        Logger.d(TAG,"changePlayModelResult==playModel:"+playModel);
        if(null!=mIcPlayMode){
            changePlayerModel(playModel,true);
        }
    }

    /**
     * 用户改变了闹钟定时模式
     * @param model
     */
    @Override
    public void changeAlarmModelResult(int model) {
        Logger.d(TAG,"changeAlarmModelResult==playModel:"+model);
        if(null!=mIcAlarm){
            changePlayerAlarmModel(model,true);
        }
    }

    /**
     * 音乐播放器配置结果
     * @param musicPlayerConfig
     */
    @Override
    public void onMusicPlayerConfig(MusicPlayerConfig musicPlayerConfig) {
        if(null!=musicPlayerConfig) Logger.d(TAG,"检查用户设置的播放模式回调"+musicPlayerConfig.getPlayModel()+",闹钟模式："+musicPlayerConfig.getAlarmModel());
        if(null!=musicPlayerConfig&&null!=mIcPlayMode&&null!=mIcAlarm){
            changePlayerModel(musicPlayerConfig.getPlayModel(),false);
            changePlayerAlarmModel(musicPlayerConfig.getAlarmModel(),false);
        }
    }

    /**
     * 缓冲进度
     * @param percent
     */
    @Override
    public void onBufferingUpdate(int percent) {

    }

    /**
     * 播放器准备完成了
     * @param mediaPlayer
     */
    @Override
    public void onPrepared(IMediaPlayer mediaPlayer) {
        Logger.d(TAG,"onPrepared");
        if(null!=mMusicPlayerSeekbar){
            mMusicPlayerSeekbar.setPlaying(true);
        }
    }

    /**
     * 请求自动开启播放任务
     * @param viewTupe
     * @param position
     */
    @Override
    public void autoStartNewPlayTasks(int viewTupe, int position) {
        Logger.d(TAG,"autoStartNewPlayTasks：viewTupe="+viewTupe+",position="+position);
    }

    /**
     * 闹钟定时的剩余时间
     * @param durtion
     */
    @Override
    public void taskRemmainTime(final long durtion) {
        Logger.d(TAG,"taskRemmainTime：durtion="+durtion);
        if(null!=mHandler){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mMusicPlayerSeekbar.setProgress(durtion);
                }
            });
        }
    }

    /**
     * 一处点赞，所有实例化的播放控制器都需要同步
     * @param icon
     * @param isCollect
     */
    @Override
    public void changeCollectResult(int icon, boolean isCollect) {
        if(null!=mIcCollect){
            this.isCollect=isCollect;
            mIcCollect.setImageResource(icon);
            if(isCollect){
                mIcCollect.setColorFilter(Color.rgb(255,91,59));//#FFFF5B3B
            }else{
                setImageColorFilter(mIcCollect,mPlayerStyle);
            }
        }
    }


    //对持有者提供回调
    public interface OnClickEventListener{
        void onEventCollect();//收藏
        void onEventRandomPlay();//随机播放
        void onBack();//返回事件
    }
    private OnClickEventListener mOnClickEventListener;
    public void setOnClickEventListener(OnClickEventListener onClickEventListener) {
        mOnClickEventListener = onClickEventListener;
    }
}
