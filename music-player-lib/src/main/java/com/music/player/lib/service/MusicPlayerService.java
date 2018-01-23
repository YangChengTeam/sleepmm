package com.music.player.lib.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.music.player.lib.R;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.constants.Constants;
import com.music.player.lib.listener.OnPlayerEventListener;
import com.music.player.lib.manager.AudioFocusManager;
import com.music.player.lib.manager.MusicPlayerManager;
import com.music.player.lib.mode.MusicPlayerAction;
import com.music.player.lib.mode.PlayerAlarmModel;
import com.music.player.lib.mode.PlayerModel;
import com.music.player.lib.util.Logger;
import com.music.player.lib.util.SharedPreferencesUtil;
import com.music.player.lib.util.ToastUtils;
import com.music.player.lib.util.MusicPlayerUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TinyHung@Outlook.com
 * 2018/1/18.
 * 音乐播放服务
 */

public class MusicPlayerService extends Service implements IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnCompletionListener, IMediaPlayer.OnBufferingUpdateListener,
        IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnInfoListener{

    private static final String TAG = MusicPlayerService.class.getSimpleName();
    private static KSYMediaPlayer mMediaPlayer;
    private static List<MusicInfo> mMusicInfos=null;//播放任务集合
    private MusicActionReceiver mMusicActionReceiver;
    private static AudioFocusManager mAudioFocusManager;
    private static OnPlayerEventListener mOnPlayerEventListener;//播放的状态回调
    private static int mPlayModel= PlayerModel.PLAY_MODEL_SEQUENCE_FOR;//默认播放播放模式(列表顺序播放)
    private static int mPlayAlarmModel= PlayerAlarmModel.PLAYER_ALARM_NORMAL;//默认定时模式(不限时)
    private static int mPlayPosition=0;//当前正在播放的位置
    private static long TIMER_DURTION= PlayerAlarmModel.TIME.DEFAULT_TIME;//默认的定时时长30分钟
    private static long currentDurtion=0;//当前已经执行完了多少毫秒任务
    private PlayTimerTask mPlayTimerTask;
    private Timer mTimer;
    private Handler mHandler;
    private NotificationManager mManager;
    private int NOTIFACTION_ID=0x1;

    /**
     * 设置播放模式
     * @param playMode
     */
    private void setPlayMode(int playMode) {
        this.mPlayModel=playMode;
    }

    /**
     * 返回播放模式
     * @return
     */
    private int getPlayMode() {
        return mPlayModel;
    }

    /**
     * 设置闹钟定时模式
     * @param alarmMode
     */
    private void setPlayAlarmMode(int alarmMode) {
        this.mPlayAlarmModel=alarmMode;
        //无奈之举，重复这段代码，防止同时回调changeAlarmModelResult(),单独再写一遍
        switch (mPlayAlarmModel) {
            //十分钟
            case PlayerAlarmModel.PLAYER_ALARM_10:
                TIMER_DURTION=10*60;
                break;
            //二十分钟
            case PlayerAlarmModel.PLAYER_ALARM_20:
                TIMER_DURTION=20*60;
                break;
            //30分钟档次
            case PlayerAlarmModel.PLAYER_ALARM_30:
                TIMER_DURTION=30*60;
                break;
            //一个小时
            case PlayerAlarmModel.PLAYER_ALARM_ONE_HOUR:
                TIMER_DURTION=60*60;
                break;
            //不限时
            case PlayerAlarmModel.PLAYER_ALARM_NORMAL:
                TIMER_DURTION=-1;
                break;
        }
        Logger.d(TAG,"用户配置中心设置了"+(TIMER_DURTION/60)+"分钟");
        currentDurtion=0;
        //为防止界面刷新不及时
        if(null!=mOnPlayerEventListener){
            mOnPlayerEventListener.taskRemmainTime(TIMER_DURTION-currentDurtion);
        }
    }

    /**
     * 设置定时关闭闹钟模式
     * @param fiexdTimerAlarmModel
     */
    private void setAralmFiexdTimer(int fiexdTimerAlarmModel) {
        Logger.d(TAG,"设置中心，定时关闭档次"+fiexdTimerAlarmModel);
        this.mPlayAlarmModel=fiexdTimerAlarmModel;
        changeAlarmDurtion(mPlayAlarmModel);
    }


    /**
     * 返回定时模式
     * @return
     */
    public int getPlayAlarmModel() {
        return mPlayAlarmModel;
    }

    /**
     * 返回定时时长
     * @return
     */
    private long getPlayerAlarmDurtion() {
        return TIMER_DURTION;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicPlayerServiceBunder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG,"onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d(TAG,"onStartCommand");
        //注册广播
        mMusicActionReceiver = new MusicActionReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(MusicPlayerAction.MUSIC_ACTION_START_PAUSE);
        intentFilter.addAction(MusicPlayerAction.MUSIC_ACTION_STOP);
        registerReceiver(mMusicActionReceiver,intentFilter);
        //场景捕获
        mAudioFocusManager = new AudioFocusManager(this);//当耳机拔出，或者其他外界干扰时捕获
        //初始化播放列表池
        mMusicInfos=new ArrayList<>();
        mHandler = new Handler();
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Logger.d(TAG,"onDestroy");
        stop();
        mOnPlayerEventListener=null;
        if(null!=mAudioFocusManager){
            mAudioFocusManager.abandonAudioFocus();
        }
        unregisterReceiver(mMusicActionReceiver);

        if(null!=mManager){
            mManager.cancelAll();
        }
        super.onDestroy();
    }

    private boolean mediaPlayerNoEmpty() {
        return null!=mMediaPlayer;
    }

    /**
     * 初始化播放器
     */
    private void initMediaPlayer() {
        mMediaPlayer = new KSYMediaPlayer.Builder(MusicPlayerManager.getInstance().getContext()).build();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setScreenOnWhilePlaying(true);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnInfoListener(this);
    }

    /**
     * 设置监听
     * @param onPlayerEventListener
     */
    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        mOnPlayerEventListener = onPlayerEventListener;
    }

    /**
     * 移除监听
     */
    private void removeEventListener() {
        mOnPlayerEventListener=null;
    }

    /**
     * 返回正在播放的任务列表
     * @return
     */
    public List<MusicInfo> getMusicList() {
        return mMusicInfos;
    }

    /**
     * 开始、暂停播放,返回是否 播放/暂停 成功状态
     * 开始和暂停定时任务计时器
     * @return
     */
    public boolean playPause() {
        Logger.d(TAG,"playPause");
        if(null!=mMusicInfos&&mMusicInfos.size()>0){
            if(mediaPlayerNoEmpty()){
                if(mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                    stopTimer();
                    if(null!=mOnPlayerEventListener){
                        mOnPlayerEventListener.pauseResult(mMusicInfos.get(mPlayPosition));
                    }
                    return true;
                }else{
                    mMediaPlayer.start();
                    startTimer();
                    if(null!=mOnPlayerEventListener){
                        mOnPlayerEventListener.startResult(mMusicInfos.get(mPlayPosition));
                    }
                    return true;
                }
            }else{
                //初始化播放
                playMusic(0);
                startTimer();
                return true;
            }
        }else{
            return false;
        }
    }

    /**
     * 当播放任务开始时 开始计时
     */
    private void startTimer() {
        stopTimer();
        mTimer = new Timer();
        mPlayTimerTask = new PlayTimerTask();
        //立即执行，1000毫秒循环一次
        mTimer.schedule(mPlayTimerTask,0,1000);
    }

    /**
     * 当播放任务结束时 结束计时
     */
    private void stopTimer() {
        if(null!=mTimer){
            mTimer.cancel();
        }
        if(null!=mPlayTimerTask){
            mPlayTimerTask.cancel();
        }
    }

    /**
     * 倒计时 计时器
     */
    private class PlayTimerTask extends TimerTask{
        @Override
        public void run() {
            //时间到了，结束播放任务,结束定时计时器任务
            if(-1!=TIMER_DURTION&&currentDurtion>=TIMER_DURTION){
                Logger.d(TAG,"时间到了，任务结束。停止音频播放");
                if(null!=mOnPlayerEventListener){
                    mOnPlayerEventListener.taskRemmainTime(TIMER_DURTION-currentDurtion);
                }
                if(null!=mHandler){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            stop();
                        }
                    });
                }
                currentDurtion=0;
            }else{
                currentDurtion=currentDurtion+1;
                long mutin = (TIMER_DURTION - currentDurtion) / 60;
                Logger.d(TAG,"倒计时："+mutin+"分"+(TIMER_DURTION - currentDurtion)%60+"秒");
                if(null!=mOnPlayerEventListener){
                    mOnPlayerEventListener.taskRemmainTime(TIMER_DURTION-currentDurtion);
                }
            }
        }
    }


    /**
     * 设置定时关闭的临界时间，每一次设置后定时器时间重新生效
     * @param durtion
     */
    private void setPlayerDurtion(long durtion) {
        TIMER_DURTION=durtion;
        currentDurtion=0;
        //若用户滑动进度条改变了时长，默认重新设置为30分钟
        SharedPreferencesUtil.getInstance().putInt(Constants.SP_MUSIC_PLAY_ALARM,PlayerAlarmModel.PLAYER_ALARM_30);
        //为防止界面刷新不及时
        if(null!=mOnPlayerEventListener){
            mOnPlayerEventListener.taskRemmainTime(TIMER_DURTION-currentDurtion);
        }
    }



    /**
     * 改变定时器定时时长模式，每次设置定时器设置重新生效
     */
    private void changeAlarmModel() {
        if(mPlayAlarmModel>=PlayerAlarmModel.PLAYER_ALARM_NORMAL){
            mPlayAlarmModel=PlayerAlarmModel.PLAYER_ALARM_10;
        }else{
            mPlayAlarmModel++;
        }
        Logger.d(TAG,"changeAlarmModel:新设定的定时档次"+mPlayAlarmModel);
        changeAlarmDurtion(mPlayAlarmModel);
    }

    /**
     * 定时归零
     */
    private void changeAlarmDurtion(int playAlarmModel) {
        switch (playAlarmModel) {
            //十分钟
            case PlayerAlarmModel.PLAYER_ALARM_10:
                TIMER_DURTION=10*60;
                break;
            //二十分钟
            case PlayerAlarmModel.PLAYER_ALARM_20:
                TIMER_DURTION=20*60;
                break;
            //30分钟档次
            case PlayerAlarmModel.PLAYER_ALARM_30:
                TIMER_DURTION=30*60;
                break;
            //一个小时
            case PlayerAlarmModel.PLAYER_ALARM_ONE_HOUR:
                TIMER_DURTION=60*60;
                break;
            //不限时
            case PlayerAlarmModel.PLAYER_ALARM_NORMAL:
                TIMER_DURTION=-1;
                break;
        }
        SharedPreferencesUtil.getInstance().putInt(Constants.SP_MUSIC_PLAY_ALARM,playAlarmModel);
        if(null!=mOnPlayerEventListener){
            mOnPlayerEventListener.changeAlarmModelResult(playAlarmModel);
        }
        Logger.d(TAG,"用户设置了"+(TIMER_DURTION/60)+"分钟");
        currentDurtion=0;
        //为防止界面刷新不及时
        if(null!=mOnPlayerEventListener){
            mOnPlayerEventListener.taskRemmainTime(TIMER_DURTION-currentDurtion);
        }
    }

    /**
     * 改变播放器播放模式
     */
    private void changePlayModel() {
        //最后一个
        if(mPlayModel>=PlayerModel.PLAY_MODEL_SINGER){
            mPlayModel=PlayerModel.PLAY_MODEL_SEQUENCE_FOR;//还原到第一个
        }else{
            mPlayModel++;
        }
        Logger.d(TAG,"changePlayModel:mPlayModel="+mPlayModel);
        SharedPreferencesUtil.getInstance().putInt(Constants.SP_MUSIC_PLAY_MODEL,mPlayModel);
        if(null!=mOnPlayerEventListener){
            mOnPlayerEventListener.changePlayModelResult(mPlayModel);
        }
    }


    public boolean isPlaying() {
        return null==mMediaPlayer?false:mMediaPlayer.isPlaying();
    }

    public boolean isPreparing() {
        return null==mMediaPlayer?false:mMediaPlayer.isPlayable();
    }

    public long getPlayerCurrentPosition() {
        return null==mMediaPlayer?0:mMediaPlayer.getCurrentPosition();
    }


    /**
     * 播放全新的音乐,调用方法后直接清空现有的播放列表
     * @param musicInfo
     */
    private void playMusic(MusicInfo musicInfo) {
        if(null==musicInfo) return;
        if(null!=mMusicInfos){
            mMusicInfos.clear();
            mMusicInfos.add(musicInfo);
            startPlay(0);
        }
    }

    /**
     * 播放指定位置的音乐
     * @param position
     */
    private void playMusic(int position){
        startPlay(position);
    }

    /**
     * 播放全新的音乐列表
     * @param musicInfos
     * @param position
     */
    private void playMusic(List<MusicInfo> musicInfos,int position){
        if(null==mMusicInfos) mMusicInfos=new ArrayList<>();
        if(null!=musicInfos&&musicInfos.size()>0){
            mMusicInfos.clear();
            mMusicInfos.addAll(musicInfos);
            startPlay(position);
        }
    }

    //这个方法外界不要手动调用
    private void startPlay(int poistion) {
        if(null!=mMusicInfos&&mMusicInfos.size()>0){
            this.mPlayPosition=poistion;
            MusicInfo musicInfo = mMusicInfos.get(poistion);
            startPlay(musicInfo);
        }
    }
    //这个方法外界不要手动调用
    private void startPlay(MusicInfo musicInfo) {
        if(null==musicInfo) return;
        Logger.d(TAG,"播放音乐标题："+musicInfo.getMusicTitle()+",MusicID="+musicInfo.getMusicID()+",mPlayPosition="+mPlayPosition);
        try {
            //初始化音频媒体
            stop();
            startTimer();
            initMediaPlayer();
            mMediaPlayer.setDataSource(musicInfo.getMusicPath());
            mMediaPlayer.prepareAsync();
            if (null!=mOnPlayerEventListener) {
                mOnPlayerEventListener.onMusicChange(musicInfo);//回调切换了播放任务
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d(TAG,"播放异常："+e.getMessage());
        }
    }

    /**
     * 开始播放
     */
    public void  start(){
        Logger.d(TAG,"start");
        if(mediaPlayerNoEmpty()){
            mMediaPlayer.start();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        Logger.d(TAG,"pause");
        if(mediaPlayerNoEmpty()){
            mMediaPlayer.pause();
        }
    }


    /**
     * 下一首
     */
    public void next() {
        if(-1!=TIMER_DURTION&&currentDurtion>=TIMER_DURTION){
            stop();
            return;
        }
        Logger.d(TAG,"next 当前循环模式"+getPlayMode());
        if(null!=mMusicInfos&&mMusicInfos.size()>0){
            switch (getPlayMode()) {
                //列表顺序播放，不循环
//                case PlayerModel.PLAY_MODEL_SEQUENCE:
//                    //已是最后一首,不用管，用户需要重新点击按钮才能播放
//                    if(mPlayPosition==mMusicInfos.size()-1){
//                        return;
//                    }
//                    playMusic(mPlayPosition+1);
//                    break;
                //列表循环
                case PlayerModel.PLAY_MODEL_SEQUENCE_FOR:
                    //如果是最后一首，从第一首开始播放
                    if(mPlayPosition==mMusicInfos.size()-1){
                        playMusic(0);
                    }else{
                        playMusic(mPlayPosition+1);
                    }
                    break;
                //随机播放
                case PlayerModel.PLAY_MODEL_RANDOM:
                    int randomNum = MusicPlayerUtils.getRandomNum(0, mMusicInfos.size() - 1);
                    Logger.d(TAG,"随机播放："+randomNum);
                    playMusic(randomNum);
                    break;
                //单曲循环
                case PlayerModel.PLAY_MODEL_SINGER:
                    playMusic(mPlayPosition);
                    break;
            }
        }else{
            ToastUtils.showCenterToast("请先开始播放任务~");
        }
    }

    /**
     * 上一首
     */
    public void  last(){
        if(-1!=TIMER_DURTION&&currentDurtion>=TIMER_DURTION){
            stop();
            return;
        }
        Logger.d(TAG,"last 当前循环模式"+getPlayMode());
        if(null!=mMusicInfos&&mMusicInfos.size()>0){
            switch (getPlayMode()) {
                //列表顺序播放，不循环
//                case PlayerModel.PLAY_MODEL_SEQUENCE:
//                    //已经是第0首歌，再次播放
//                    if(mPlayPosition<=0){
//                        playMusic(mPlayPosition);
//                    }else{
//                        playMusic(mPlayPosition-1);
//                    }
//                    break;
                //列表循环
                case PlayerModel.PLAY_MODEL_SEQUENCE_FOR:
                    //如果是第0首，再次播放
                    if(mPlayPosition<=0){
                        playMusic(mPlayPosition);
                    }else{
                        playMusic(mPlayPosition-1);
                    }
                    break;
                //随机播放
                case PlayerModel.PLAY_MODEL_RANDOM:
                    int randomNum = MusicPlayerUtils.getRandomNum(0, mMusicInfos.size() - 1);
                    playMusic(randomNum);
                    break;
                //单曲循环
                case PlayerModel.PLAY_MODEL_SINGER:
                    //继续播放自己
                    playMusic(mPlayPosition);
                    break;
            }
        }else{
            ToastUtils.showCenterToast("请先开始播放任务~");
        }
    }

    /**
     * 检查当前正在播放的任务
     */
    private void checkedPlayTask() {
        Logger.d(TAG,"checkedPlayTask--检查播放任务");
        if(null!=mMediaPlayer&&null!=mMusicInfos&&mMusicInfos.size()>0){
            Logger.d(TAG,"播放服务已初始化且播放器列表不为空");
            if(null!=mOnPlayerEventListener){
                mOnPlayerEventListener.checkedPlayTaskResult(mMusicInfos.get(mPlayPosition),mMediaPlayer);
            }
        }
    }

    /**
     * 当前音频播放完成了，根据模式继续播放
     */
    private void onCompletionPlay() {
        if(-1!=TIMER_DURTION&&currentDurtion>=TIMER_DURTION){
            stop();
            return;
        }
        Logger.d(TAG,"onCompletionPlay=当前循环模式:"+getPlayMode());
        if(null!=mMusicInfos&&mMusicInfos.size()>0){
            switch (getPlayMode()) {
                //列表顺序播放，不循环
//                case PlayerModel.PLAY_MODEL_SEQUENCE:
//                    //还没有到最后一首,继续播放，否则停止播放
//                    if(mPlayPosition<mMusicInfos.size()-1){
//                        playMusic(mPlayPosition+1);
//                    }else{
//                        //到了最后一首，停止播放
//                        stop();
//                    }
//                    break;
                //列表循环
                case PlayerModel.PLAY_MODEL_SEQUENCE_FOR:
                    //如果还未到最后一首
                    if(mPlayPosition<mMusicInfos.size()-1){
                        playMusic(mPlayPosition+1);
                    //已经是最后一首了
                    }else{
                        //从第0首开始播放
                        playMusic(0);
                    }
                    break;
                //随机播放
                case PlayerModel.PLAY_MODEL_RANDOM:
                    int randomNum = MusicPlayerUtils.getRandomNum(0, mMusicInfos.size() - 1);
                    playMusic(randomNum);
                    break;
                //单曲循环
                case PlayerModel.PLAY_MODEL_SINGER:
                    //继续播放自己
                    playMusic(mPlayPosition);
                    break;
            }
        }
    }

    /**
     * 设置跳转进度
     * @param durtion
     */
    public void seekTo(int durtion) {
        Logger.d(TAG,"seekTo:"+durtion);
        if(mediaPlayerNoEmpty()){
            mMediaPlayer.seekTo(durtion);
        }
    }

    public void prev() {
        Logger.d(TAG,"prev:");
    }


    /**
     * 重置释放播放器，停止计时器
     */
    private void release(){

        if(mediaPlayerNoEmpty()){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer.reset();
            mMediaPlayer.resetListeners();
            mMediaPlayer=null;
        }
        stopTimer();
        if(null!=mManager){
            mManager.cancel(NOTIFACTION_ID);
            mManager.cancelAll();
        }
    }

    public void stop() {
        Logger.d(TAG,"stop:");
        if(mediaPlayerNoEmpty()){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer.reset();
            mMediaPlayer.resetListeners();
            mMediaPlayer=null;
        }
        stopTimer();
        if(null!=mManager){
            mManager.cancel(NOTIFACTION_ID);
            mManager.cancelAll();
        }
        if(null!=mOnPlayerEventListener){
            mOnPlayerEventListener.stopPlayer(null!=mMusicInfos&&mMusicInfos.size()>0?mMusicInfos.get(mPlayPosition):null);
        }
    }



    private void setLoop(boolean flag) {
        if(mediaPlayerNoEmpty()){
            mMediaPlayer.setLooping(flag);
        }
    }

    private int getPlayPosition() {
        if(mediaPlayerNoEmpty()){
            return 0;
        }
        return -1;
    }


    private Notification getNotification(MusicInfo musicInfo) {
//        RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.remote_music_player_layout);
        NotificationCompat.Builder  notificationCompat=new NotificationCompat.Builder(this);
        notificationCompat.setSmallIcon(R.mipmap.ic_launcher);
        notificationCompat.setContentTitle("睡眠咩咩");
        notificationCompat.setContentText("正在播放:"+musicInfo.getMusicTitle());
        Intent intent = new Intent("com.music.player.action");//响应Action的Activity需要配置此Action
        PendingIntent mPendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationCompat.setContentIntent(mPendingIntent);
        notificationCompat.setWhen(System.currentTimeMillis());
        notificationCompat.setOngoing(true);
        notificationCompat.setPriority(NotificationCompat.PRIORITY_MAX);
        return notificationCompat.build();
    }


    private boolean isOpenNotificationPermissions(){
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        return manager.areNotificationsEnabled();
    }


    /**
     * 接收外界的命令状态
     */
    public class MusicActionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.d(TAG,"MusicActionReceiver:action=="+action);
            switch (action) {
                //开始,暂停
                case MusicPlayerAction.MUSIC_ACTION_START_PAUSE:
                    Logger.d(TAG,"收到播放、暂停广播");
                    break;
                //停止
                case MusicPlayerAction.MUSIC_ACTION_STOP:
                    Logger.d(TAG,"收到播放、暂停广播");
                    break;
                //点击了去主页意图
                case MusicPlayerAction.MUSIC_ACTION_START_MAIN:
                    Logger.d(TAG,"收到了去主页事件");
                    break;
            }
        }
    }


    //===========================================播放回调============================================

    /**
     * 缓冲完成后调用
     * @param iMediaPlayer
     */
    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        Logger.d(TAG,"onPrepared");
        if(null!=iMediaPlayer) iMediaPlayer.start();
        if(null!=mAudioFocusManager){
            mAudioFocusManager.requestAudioFocus();
        }
        //为防止超过定时还在播放
        if(-1!=TIMER_DURTION&&currentDurtion>=TIMER_DURTION){
            stop();
            return;
        }
        if(null!=mOnPlayerEventListener){
            mOnPlayerEventListener.onPrepared(iMediaPlayer);
        }
        //如果有权限，直接发通知
        if(isOpenNotificationPermissions()){
            //创建自定义控制器通知栏
            if(null!=mManager&&null!=mMusicInfos&&mMusicInfos.size()>0){
                mManager.notify(NOTIFACTION_ID,getNotification(mMusicInfos.get(mPlayPosition)));
            }
        }else{
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    /**
     * 播放完成调用
     * @param iMediaPlayer
     */
    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        Logger.d(TAG,"onCompletion");
        if(null!=mManager){
            mManager.cancel(NOTIFACTION_ID);
        }
        if(null!=mOnPlayerEventListener){
            mOnPlayerEventListener.onCompletion();
        }
        //播放完成，根据用户设置的播放模式来自动播放下一首
        onCompletionPlay();
    }

    /**
     * 缓冲进度
     * @param iMediaPlayer
     * @param i
     */
    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
        if(null!=mOnPlayerEventListener){
            mOnPlayerEventListener.onBufferingUpdate(i);
        }
    }

    /**
     * 设置进度完成调用
     * @param iMediaPlayer
     */
    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        if(null!=mOnPlayerEventListener){
            mOnPlayerEventListener.onSeekComplete();
        }
    }

    /**
     * 播放失败
     * @param iMediaPlayer
     * @param i
     * @param i1
     * @return
     */
    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        release();
        if(null!=mOnPlayerEventListener){
            mOnPlayerEventListener.onError(i,i1);
        }
        return false;
    }

    /**
     * 获取音频信息
     * @param iMediaPlayer
     * @param i
     * @param i1
     * @return
     */
    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        Logger.d(TAG,"播放器发生了变化:i=="+i+",i1=="+i1);
        if(null!=mOnPlayerEventListener){
            mOnPlayerEventListener.onInfo(i,i1);
        }
        return false;
    }


    /**
     * 中间人
     */
    public class MusicPlayerServiceBunder extends Binder{
        //返回MusicPlayerService实例
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
        //注册播放监听
        public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
            MusicPlayerService.this.setOnPlayerEventListener(onPlayerEventListener);
        }
        //移除播放监听
        public void removePlayerEventListener() {
            MusicPlayerService.this.removeEventListener();
        }
        //播放新的歌曲
        public void playMusic(MusicInfo musicInfo) {
            MusicPlayerService.this.playMusic(musicInfo);
        }
        //播放指定位置的音乐
        public void playMusic(int position){
            MusicPlayerService.this.playMusic(position);
        }
        //播放全新的列表
        public void playMusic(List<MusicInfo> musicInfos,int position){
            MusicPlayerService.this.playMusic(musicInfos,position);
        }
        //开始播放
        public void start() {
            MusicPlayerService.this.start();
        }
        //暂停播放
        public void pause() {
            MusicPlayerService.this. pause();
        }
        //停止播放
        public void stop() {
            MusicPlayerService.this.stop();
        }
        //返回当前正在播放的位置
        public long getPlayerCurrentPosition() {
            return MusicPlayerService.this.getPlayerCurrentPosition();
        }
        //返回列表模式下面正在播放的角标
        public int getPlayingPosition() {
            return MusicPlayerService.this.getPlayPosition();
        }
        //设置循环模式
        public void setLoop(boolean flag) {
            MusicPlayerService.this.setLoop(flag);
        }
        //改变播放模式
        public  void changePlayModel() {
            MusicPlayerService.this.changePlayModel();
        }
        //设置播放器播放模式
        public void setPlayMode(int playMode) {
            MusicPlayerService.this.setPlayMode(playMode);
        }
        //返回播放模式
        public int getPlayModel() {
            return MusicPlayerService.this.getPlayMode();
        }
        //改变定时模式
        public void changeAlarmModel() {
            MusicPlayerService.this.changeAlarmModel();
        }
        //设置播放器定时关闭模式
        public void setPlayAlarmMode(int mode) {
            MusicPlayerService.this.setPlayAlarmMode(mode);
        }
        //设置定时关闭时长
        public void setPlayerDurtion(long durtion) {
            MusicPlayerService.this.setPlayerDurtion(durtion);
        }
        //设置闹钟档次，需要回调结果
        public void setAralmFiexdTimer(int fiexdTimerAlarmModel) {
            MusicPlayerService.this.setAralmFiexdTimer(fiexdTimerAlarmModel);
        }
        //返回用户设置的闹钟定时时长
        public long getPlayerAlarmDurtion() {
            return MusicPlayerService.this.getPlayerAlarmDurtion();
        }
        //返回定时关闭模式
        public int getPlayAlarmModel() {
            return MusicPlayerService.this.getPlayAlarmModel();
        }
        //检查当前正在播放的任务
        public void checkedPlayTask() {
            MusicPlayerService.this.checkedPlayTask();
        }
        //开始、暂停播放
        public boolean onPlayPause() {
            return MusicPlayerService.this.playPause();
        }
        //播放上一首
        public void playLast() {
            MusicPlayerService.this.last();
        }
        //播放下一首
        public void playNext() {
            MusicPlayerService.this.next();
        }
    }
}
