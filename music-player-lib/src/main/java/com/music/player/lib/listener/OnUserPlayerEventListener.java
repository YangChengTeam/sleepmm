package com.music.player.lib.listener;

import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicPlayerConfig;

/**
 * UI界面组件需要注册的播放Event事件
 * TinyHung@Outlook.com
 * 2018/1/18.
 */

public interface OnUserPlayerEventListener {
    /**
     * 检查当前正在播放的任务，建议在界面的onResume()中调用
     * @param musicInfo
     */
    void checkedPlayTaskResult(MusicInfo musicInfo, KSYMediaPlayer mediaPlayer);
    /**
     * 改变了播放器播放模式回调
     * @param playModel
     */
    void changePlayModelResult(int playModel);
    /**
     * 改变了闹钟模式回调
     * @param model
     */
    void changeAlarmModelResult(int model);
    /**
     * 播放器配置
     * @param musicPlayerConfig
     */
    void onMusicPlayerConfig(MusicPlayerConfig musicPlayerConfig);
    /**
     * 切换了歌曲
     * @param music
     */
    void onMusicChange(MusicInfo music);
    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);
    /**
     * 播放器准备好了
     */
    void onPrepared(IMediaPlayer mediaPlayer);

    /**
     * 开始播放了
     */
    void startResult(MusicInfo musicInfo);

    /**
     * 暂停了播放
     */
    void pauseResult(MusicInfo musicInfo);

    /**
     * 播放完成了
     */
    void onCompletion();


    /**
     * 播放停止了
     * @param musicInfo
     */
    void stopPlayer(MusicInfo musicInfo);

    /**
     * 设置进度成功回调
     */
    void onSeekComplete();
    /**
     * 更新定时停止播放时间
     */
    void onTimer();
    /**
     * 音频的基本信息
     * @param i
     * @param i1
     */
    void onInfo(int i, int i1);
    /**
     * 播放失败
     * @param what
     * @param extra
     */
    void onError(int what, int extra);

    /**
     * 自动创建播放任务
     * @param viewTupe
     * @param position
     */
    void autoStartNewPlayTasks(int viewTupe, int position);

    /**
     * 播放时间倒计时，注意：该方法在子线程回调
     * @param durtion
     */
    void taskRemmainTime(long durtion);

}
