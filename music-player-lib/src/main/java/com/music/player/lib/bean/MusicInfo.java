package com.music.player.lib.bean;



/**
 * TinyHung@Outlook.com
 * 2018/1/18.
 */

public class MusicInfo {

    private String musicID;//音乐ID
    private String musicTitle;//音乐标题
    private String musicPath;//音乐路径
    private String musicCover;//音乐封面
    private String musicAuthor;//音乐作者
    private String musicDurtion;//音乐时长
    private String upTime;//更新时间
    private String musicAlbum;//专辑
    private String musicAlbumTitle;//专辑名称
    private String msuicAlbumIcon;//专辑封面
    private String msuicAlbumAuthor;//专辑作者
    private int playModel;//播放模式
    private boolean isPlaying;//是否正在播放

    public String getMusicID() {
        return musicID;
    }

    public void setMusicID(String musicID) {
        this.musicID = musicID;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public String getMusicCover() {
        return musicCover;
    }

    public void setMusicCover(String musicCover) {
        this.musicCover = musicCover;
    }

    public String getMusicAuthor() {
        return musicAuthor;
    }

    public void setMusicAuthor(String musicAuthor) {
        this.musicAuthor = musicAuthor;
    }

    public String getMusicDurtion() {
        return musicDurtion;
    }

    public void setMusicDurtion(String musicDurtion) {
        this.musicDurtion = musicDurtion;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getMusicAlbum() {
        return musicAlbum;
    }

    public void setMusicAlbum(String musicAlbum) {
        this.musicAlbum = musicAlbum;
    }

    public String getMusicAlbumTitle() {
        return musicAlbumTitle;
    }

    public void setMusicAlbumTitle(String musicAlbumTitle) {
        this.musicAlbumTitle = musicAlbumTitle;
    }

    public String getMsuicAlbumIcon() {
        return msuicAlbumIcon;
    }

    public void setMsuicAlbumIcon(String msuicAlbumIcon) {
        this.msuicAlbumIcon = msuicAlbumIcon;
    }

    public String getMsuicAlbumAuthor() {
        return msuicAlbumAuthor;
    }

    public void setMsuicAlbumAuthor(String msuicAlbumAuthor) {
        this.msuicAlbumAuthor = msuicAlbumAuthor;
    }

    public int getPlayModel() {
        return playModel;
    }

    public void setPlayModel(int playModel) {
        this.playModel = playModel;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
