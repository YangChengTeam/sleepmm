package com.music.player.lib.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicPlayerConfig;
import com.music.player.lib.constants.Constants;
import com.music.player.lib.listener.MusicPlayerServiceConnectionCallback;
import com.music.player.lib.listener.OnPlayerEventListener;
import com.music.player.lib.listener.OnUserPlayerEventListener;
import com.music.player.lib.mode.PlayerAlarmModel;
import com.music.player.lib.mode.PlayerModel;
import com.music.player.lib.service.MusicPlayerService;
import com.music.player.lib.util.Logger;
import com.music.player.lib.util.SharedPreferencesUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * TinyHung@Outlook.com
 * 2018/1/18.
 * 统一注册和调度中心
 */

public class MusicPlayerManager implements OnPlayerEventListener {

    private static final String TAG = MusicPlayerManager.class.getSimpleName();
    private static MusicPlayerManager mInstance;
    private static Context mContext;
    private static MusicPlayerServiceConnectionCallback mConnectionCallback;
    private static List<OnUserPlayerEventListener> mUserCallBackListenerList=null;//方便多界面注册这个监听
    private static MusicPlayerServiceConnection mMusicPlayerServiceConnection;
    private static SubjectObservable mSubjectObservable;
    private static MusicPlayerService.MusicPlayerServiceBunder mMusicPlayerServiceBunder;

    public static synchronized MusicPlayerManager getInstance(){
        synchronized (MusicPlayerManager.class){
            if(null==mInstance){
                mInstance=new MusicPlayerManager();
            }
        }
        return mInstance;
    }


    public MusicPlayerManager(){
        mMusicPlayerServiceConnection = new MusicPlayerServiceConnection();
        mSubjectObservable = new SubjectObservable();
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context){
        this.mContext=context.getApplicationContext();
        SharedPreferencesUtil.init(context,context.getPackageName() + "music_play_config", Context.MODE_MULTI_PROCESS);
        if(1!=SharedPreferencesUtil.getInstance().getInt(Constants.SP_FIRST_START,0)){
            SharedPreferencesUtil.getInstance().putInt(Constants.SP_MUSIC_PLAY_ALARM,PlayerAlarmModel.PLAYER_ALARM_30);
            SharedPreferencesUtil.getInstance().putInt(Constants.SP_FIRST_START,1);
        }
    }

    /**
     * 必须在init()之后调用
     * @return
     */
    public Context getContext() {
        if(null==mContext){
            throw new IllegalStateException("MusicPlayerManager：调用getContext()之前请先调用init()");
        }
        return mContext;
    }

    /**
     * 添加观察者
     * @param o
     * 列表播放最好添加
     */
    public void addObservable(Observer o) {
        if (null!=mSubjectObservable) {
            mSubjectObservable.addObserver(o);
        }
    }

    /**
     * 移除指定观察者
     * @param o
     */
    public void deleteObserver(Observer o){
        if (null!=mSubjectObservable) {
            mSubjectObservable.deleteObserver(o);
        }
    }

    /**
     * 移除所有观察者
     */
    public void deleteObservers(){
        if (null!=mSubjectObservable) {
            mSubjectObservable.deleteObservers();
        }
    }

    /**
     * UI组件需要实现的播放器变化监听
     * @param listener
     */
    public void addPlayerStateListener(OnUserPlayerEventListener listener){
        if(null==mUserCallBackListenerList){
            mUserCallBackListenerList=new ArrayList<>();
        }
        mUserCallBackListenerList.add(listener);
    }

    /**
     * 注销播放器变化监听
     * @param listener
     */
    public void detelePlayerStateListener(OnUserPlayerEventListener listener){
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            if(mUserCallBackListenerList.contains(listener)){
                mUserCallBackListenerList.remove(listener);
            }
        }
    }

    /**
     * 移除所有的播放器变化监听
     */
    public void deteleAllPlayerStateListener(){
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            mUserCallBackListenerList.clear();
            mUserCallBackListenerList=null;
        }
    }


    /**
     * 检查播放器配置
     */
    public void checkedPlayerConfig() {
        Logger.d(TAG,"checkedPlayerConfig=检查用户播放器设置,闹钟:"+SharedPreferencesUtil.getInstance().getInt(Constants.SP_MUSIC_PLAY_ALARM,PlayerAlarmModel.PLAYER_ALARM_30));
        MusicPlayerConfig musicPlayerConfig=new MusicPlayerConfig();
        musicPlayerConfig.setPlayModel(SharedPreferencesUtil.getInstance().getInt(Constants.SP_MUSIC_PLAY_MODEL,PlayerModel.PLAY_MODEL_SEQUENCE_FOR));
        musicPlayerConfig.setAlarmModel(SharedPreferencesUtil.getInstance().getInt(Constants.SP_MUSIC_PLAY_ALARM,PlayerAlarmModel.PLAYER_ALARM_30));
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onUserPlayerEventListener : mUserCallBackListenerList) {
                onUserPlayerEventListener.onMusicPlayerConfig(musicPlayerConfig);
            }
        }
    }

    /**
     * 检查当前正在播放的任务，建议在RecyclerView适配器或者播放控制器初始化后调用
     */
    public void onResumeChecked() {
        if(serviceIsNoEmpty()){
            mMusicPlayerServiceBunder.checkedPlayTask();
        }
    }



    /**
     * 播放新的音乐
     * @param musicInfo
     */
    public void  playMusic(MusicInfo musicInfo){
        if(serviceIsNoEmpty()){
            mMusicPlayerServiceBunder.playMusic(musicInfo);
        }
    }

    /**
     * 播放指定位置音乐
     * @param pistion
     */
    public void playMusic(int pistion){
        if(serviceIsNoEmpty()){
            mMusicPlayerServiceBunder.playMusic(pistion);
        }
    }

    /**
     * 播放一个全新的列表,并指定位置
     * @param pistion 指定播放的位置
     * @param musicInfos 任务列表
     */
    public void playMusic(List<MusicInfo> musicInfos,int pistion){
        if(serviceIsNoEmpty()){
            mMusicPlayerServiceBunder.playMusic(musicInfos,pistion);
        }
    }


    /**
     * 开始\暂停播放
     * @return
     */
    public boolean playPause(){
        if(serviceIsNoEmpty()){
            return mMusicPlayerServiceBunder.onPlayPause();
        }
        return false;
    }

    /**
     * 结束播放
     */
    public void stop(){
        if(serviceIsNoEmpty()){
            mMusicPlayerServiceBunder.stop();
        }
    }

    /**
     * 返回播放器正在播放的位置
     * @return
     */
    public long getPlayDurtion(){
        if(serviceIsNoEmpty()){
            return mMusicPlayerServiceBunder.getPlayerCurrentPosition();
        }
        return 0;
    }

    /**
     * 播放上一首
     */
    public void playLast() {
        if(serviceIsNoEmpty()){
            mMusicPlayerServiceBunder.playLast();
        }
    }

    /**
     * 播放下一首
     */
    public void playNext() {
        if(serviceIsNoEmpty()){
            mMusicPlayerServiceBunder.playNext();
        }
    }


    /**
     * 获取正在播放的角标位置
     * @return
     */
    public int getPlayingPosition(){
        if(serviceIsNoEmpty()){
            return mMusicPlayerServiceBunder.getPlayingPosition();
        }
        return -1;
    }

    /**
     * 设置是否重复播放
     * @param flag
     */
    public void setLoop(boolean flag){
        if(serviceIsNoEmpty()){
            mMusicPlayerServiceBunder.setLoop(flag);
        }
    }


    /**
     * 改变播放模式
     * @return
     */
    public void changePlayModel(){
        if(serviceIsNoEmpty()){
            mMusicPlayerServiceBunder.changePlayModel();
        }
    }

    /**
     * 改变闹钟定时模式
     */
    public void changeAlarmModel() {
        if(serviceIsNoEmpty()){
            mMusicPlayerServiceBunder.changeAlarmModel();
        }
    }

    /**
     * 获取当前设置的播放模式
     * @return
     */
    public int getPlayModel(){
        if(serviceIsNoEmpty()){
            return mMusicPlayerServiceBunder.getPlayModel();
        }
        return PlayerModel.PLAY_MODEL_SEQUENCE_FOR;//默认列表循环播放
    }



    /**
     * 获取当前设置的闹钟定时时长
     * @return 固定档次，不是时间
     */
    public int getPlayerAlarmModel() {
        if(serviceIsNoEmpty()){
            return mMusicPlayerServiceBunder.getPlayAlarmModel();
        }
        return PlayerAlarmModel.PLAYER_ALARM_NORMAL;//默认不限制时间播放
    }

    /**
     * 获取当前设置的闹钟定时时长
     * @return 具体的时间，单位秒
     */
    public long getPlayerAlarmDurtion(){
        if(serviceIsNoEmpty()){
            return mMusicPlayerServiceBunder.getPlayerAlarmDurtion();
        }
        return 0;
    }

    /**
     * 设置定时关闭播放器 具体时间，不在PlayerAlarmModel提供的档次内
     * @param durtion 具体的时间，单位秒
     */
    public void setPlayerDurtion(long durtion) {
        if(serviceIsNoEmpty()){
            mMusicPlayerServiceBunder.setPlayerDurtion(durtion);
        }
    }

    /**
     * 直接设置定时关闭播放器的闹钟档次
     * 参考：PlayerAlarmModel定义的档次
     */
    public void setAralmFiexdTimer(int fiexdTimerAlarmModel) {
        if(serviceIsNoEmpty()){
            mMusicPlayerServiceBunder.setAralmFiexdTimer(fiexdTimerAlarmModel);
        }
    }

    /**
     * 自动开始播放任务
     * 调用这个方法后会回调给所有已实现OnUserPlayerEventListener 接口的UI组建，用来创建播放任务
     * @param viewTupe 需要执行回调的View类型，默认定义了两个，主页和详情界面
     * @param position 播放器要求从哪个位置开始播放
     */
    public void autoStartNewPlayTasks(int viewTupe,int position) {
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onUserPlayerEventListener : mUserCallBackListenerList) {
                onUserPlayerEventListener.autoStartNewPlayTasks(viewTupe,position);
            }
        }
    }

    /**
     * 返回mMusicPlayerServiceBunder的实例是否为空
     * @return
     */
    private boolean serviceIsNoEmpty() {
        return null!=mMusicPlayerServiceBunder;
    }

    /**
     * 是否开启调试模式,默认不开启
     * @param flag
     */
    public void setDebug(boolean flag) {
        Logger.IS_DEBUG=flag;
    }


    /**
     * bindService()必需
     */
    private class MusicPlayerServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(null!=service){
                mMusicPlayerServiceBunder = (MusicPlayerService.MusicPlayerServiceBunder) service;
                mMusicPlayerServiceBunder.setOnPlayerEventListener(MusicPlayerManager.this);//注册播放状态Event状态监听
                mMusicPlayerServiceBunder.setPlayMode(SharedPreferencesUtil.getInstance().getInt(Constants.SP_MUSIC_PLAY_MODEL,PlayerModel.PLAY_MODEL_SEQUENCE_FOR));
                mMusicPlayerServiceBunder.setPlayAlarmMode(SharedPreferencesUtil.getInstance().getInt(Constants.SP_MUSIC_PLAY_ALARM, PlayerAlarmModel.PLAYER_ALARM_NORMAL));
                if (null!=mConnectionCallback) {
                    mConnectionCallback.onServiceConnected(mMusicPlayerServiceBunder.getService());
                }
            }else{
                Logger.d(TAG,"绑定服务失败");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d(TAG,"服务已断开");
            //注销监听
            if(null!=mMusicPlayerServiceBunder){
                mMusicPlayerServiceBunder.removePlayerEventListener();
            }
            mMusicPlayerServiceBunder=null;
            if (null!=mConnectionCallback) {
                mConnectionCallback.onServiceDisconnected();
            }
        }
    }

    /**
     * 绑定服务
     * @param context
     * @param serviceConnectionCallBack
     */
    public void binService(Context context, MusicPlayerServiceConnectionCallback serviceConnectionCallBack){
        if(null==mContext){
            throw new IllegalStateException("请先在Application中调用init()方法");
        }
        if(null!=mMusicPlayerServiceConnection){
            Logger.d(TAG,"binService");
            if(null!=serviceConnectionCallBack)this.mConnectionCallback = serviceConnectionCallBack;
            Intent intent = new Intent(context, MusicPlayerService.class);
            context.startService(intent);
            context.bindService(intent, mMusicPlayerServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    public void binService(final Context context) {
        binService(context, null);
    }

    /**
     * 解绑服务
     * @param context
     */
    public void  unBindService(Context context){
        if(null!=mMusicPlayerServiceBunder){
            if(null==mMusicPlayerServiceConnection) return;
            context.unbindService(mMusicPlayerServiceConnection);
        }
        mConnectionCallback=null;
    }

    public void onDestroy(){
        if(serviceIsNoEmpty()){
            mMusicPlayerServiceBunder.onDestroy();
        }
    }


    //=================================播放状态回调，回调至调用者=====================================

    @Override
    public void onMusicChange(MusicInfo music) {
        if(null!=mSubjectObservable){
            mSubjectObservable.updataSubjectObserivce(music);
        }
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.onMusicChange(music);
            }
        }
    }

    @Override
    public void onCompletion() {
        if(null!=mSubjectObservable){
            mSubjectObservable.updataSubjectObserivce(null);
        }
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.onCompletion();
            }
        }
    }

    @Override
    public void stopPlayer(MusicInfo musicInfo) {
        if(null!=mSubjectObservable){
            mSubjectObservable.updataSubjectObserivce(null);
        }
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.stopPlayer(musicInfo);
            }
        }
    }

    @Override
    public void onPrepared(IMediaPlayer mediaPlayer) {
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.onPrepared(mediaPlayer);
            }
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.onBufferingUpdate(percent);
            }
        }
    }

    @Override
    public void onSeekComplete() {
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.onSeekComplete();
            }
        }
    }

    @Override
    public void onTimer() {
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.onTimer();
            }
        }
    }

    @Override
    public void onError(int what, int extra) {
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.onError(what,extra);
            }
        }
    }

    @Override
    public void onInfo(int i, int i1) {
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.onInfo(i,i1);
            }
        }
    }

    @Override
    public void checkedPlayTaskResult(MusicInfo musicInfo, KSYMediaPlayer mediaPlayer) {
        if(null!=mSubjectObservable){
            mSubjectObservable.updataSubjectObserivce(musicInfo);
        }
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.checkedPlayTaskResult(musicInfo,mediaPlayer);
            }
        }
    }


    @Override
    public void pauseResult(MusicInfo musicInfo) {
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.pauseResult(musicInfo);
            }
        }
    }

    @Override
    public void startResult(MusicInfo musicInfo) {
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.startResult(musicInfo);
            }
        }
    }

    @Override
    public void changePlayModelResult(int playModel) {
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.changePlayModelResult(playModel);
            }
        }
    }

    @Override
    public void changeAlarmModelResult(int playAlarmModel) {
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.changeAlarmModelResult(playAlarmModel);
            }
        }
    }

    @Override
    public void taskRemmainTime(long durtion) {
        if(null!=mUserCallBackListenerList&&mUserCallBackListenerList.size()>0){
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.taskRemmainTime(durtion);
            }
        }
    }
}
