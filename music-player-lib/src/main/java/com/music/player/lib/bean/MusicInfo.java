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
    private String musicAlbumTitle;//专辑名称

    private int plauStatus;//0：未播放 1：准备中 2：正在播放 3：暂停播放, 4：停止播放

    public int getPlauStatus() {
        return plauStatus;
    }

    public void setPlauStatus(int plauStatus) {
        this.plauStatus = plauStatus;
    }

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
    public String getMusicAlbumTitle() {
        return musicAlbumTitle;
    }
    public void setMusicAlbumTitle(String musicAlbumTitle) {
        this.musicAlbumTitle = musicAlbumTitle;
    }

}
