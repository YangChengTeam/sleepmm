package com.music.player.lib.listener;

import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicPlayerConfig;

/**
 * 播放进度监听器
 * TinyHung@Outlook.com
 * 2018/1/18.
 */

public interface OnPlayerEventListener {
    /**
     * 切换歌曲
     * @param music
     */
    void onMusicChange(MusicInfo music);

    /**
     * 播放完成
     */
    void onCompletion();

    /**
     * 播放停止了
     * @param musicInfo
     */
    void stopPlayer(MusicInfo musicInfo);

    /**
     * 播放器准备好了
     */
    void onPrepared(IMediaPlayer mediaPlayer);

    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);

    /**
     * 设置进度完成回调
     */
    void onSeekComplete();
    /**
     * 更新定时停止播放时间
     */
    void onTimer();

    /**
     * 播放失败
     * @param what
     * @param extra
     */
    void onError(int what, int extra);

    /**
     * 音频的基本信息
     * @param i
     * @param i1
     */
    void onInfo(int i, int i1);

    /**
     * 检查当前正在播放的任务，建议在界面的onResume()中调用
     * @param musicInfo
     */
    void checkedPlayTaskResult(MusicInfo musicInfo, KSYMediaPlayer mediaPlayer);

    /**
     * 暂停了播放
     */
    void pauseResult(MusicInfo musicInfo);

    /**
     * 开始播放了
     */
    void startResult(MusicInfo musicInfo);

    /**
     * 改变了播放器播放模式回调
     * @param playModel
     */
    void changePlayModelResult(int playModel);

    /**
     * 设定闹钟回调
     * @param playAlarmModel
     */
    void changeAlarmModelResult(int playAlarmModel);

    /**
     * 计时器剩余的时间，回调给播放控制器
     * @param durtion
     */
    void taskRemmainTime(long durtion);

}
