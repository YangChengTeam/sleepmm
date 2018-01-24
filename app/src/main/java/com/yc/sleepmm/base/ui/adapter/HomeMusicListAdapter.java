package com.yc.sleepmm.base.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.mode.PlayerStatus;
import com.music.player.lib.util.DateUtil;
import com.music.player.lib.util.Logger;
import com.yc.sleepmm.R;
import com.yc.sleepmm.sleep.ui.CommonUtils;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * TinyHung@Outlook.com
 * 2018/1/21.
 * 首页音乐列表
 */

public class HomeMusicListAdapter extends BaseQuickAdapter<MusicInfo,BaseViewHolder> implements Observer{

    private static final String TAG = HomeMusicListAdapter.class.getSimpleName();

    public HomeMusicListAdapter(@Nullable List<MusicInfo> data) {
        super(R.layout.item_re_home_music_list,data);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final MusicInfo item) {
        if(null!=item){
            String seconds = item.getMusicDurtion();
            if(TextUtils.isEmpty(seconds)){
                seconds="1.0";
            }
            float second = Float.parseFloat(seconds);
            helper.setText(R.id.tv_item_name,item.getMusicTitle())
                    .setText(R.id.tv_item_play_count,(58465+helper.getPosition())+"")
                    .setText(R.id.tv_item_num,(helper.getPosition()+1)+"")
                    .setText(R.id.tv_item_durtion,(DateUtil.getTimeLengthString((int) (second))));
            switch (item.getPlauStatus()) {
                //缓冲中
                case PlayerStatus.PLAYER_STATUS_ASYNCPREPARE:

                    break;
                //播放中
                case PlayerStatus.PLAYER_STATUS_PLAYING:
                    ImageView ic_play_anim = (ImageView) helper.getView(R.id.ic_play_anim);
                    ic_play_anim.setImageResource(R.drawable.play_anim);
                    AnimationDrawable drawable = (AnimationDrawable) ic_play_anim.getDrawable();
                    if(null!=drawable){
                        if(drawable.isRunning()) drawable.stop();
                        drawable.start();
                    }
                    helper.setVisible(R.id.tv_item_num,false);
                    helper.setVisible(R.id.ic_play_anim,true);
                    helper.setImageResource(R.id.ic_item_play,R.drawable.ic_play_pause);
                    helper.setTextColor(R.id.tv_item_name, CommonUtils.getColor(R.color.app_style));
                    break;
                //暂停中
                case PlayerStatus.PLAYER_STATUS_PAUSE:
                    ImageView icPlayAnim = (ImageView) helper.getView(R.id.ic_play_anim);
                    icPlayAnim.setImageResource(R.drawable.play_anim);
                    AnimationDrawable drawableAnimation = (AnimationDrawable) icPlayAnim.getDrawable();
                    if(null!=drawableAnimation){
                        drawableAnimation.stop();
                    }
                    helper.setVisible(R.id.tv_item_num,false);
                    helper.setVisible(R.id.ic_play_anim,true);
                    helper.setImageResource(R.id.ic_item_play,R.drawable.ic_item_play_true);
                    helper.setTextColor(R.id.tv_item_name, CommonUtils.getColor(R.color.app_style));
                    break;
                //播放完成或已停止
                case PlayerStatus.PLAYER_STATUS_STOP:
                //播放失败
                case PlayerStatus.PLAYER_STATUS_ERROR:
                //为空
                case PlayerStatus.PLAYER_STATUS_EMPOTY:
                    helper.setVisible(R.id.tv_item_num,true);
                    helper.setVisible(R.id.ic_play_anim,false);
                    helper.setImageResource(R.id.ic_play_anim,0);
                    helper.setImageResource(R.id.ic_item_play,R.drawable.ic_item_play);
                    helper.setTextColor(R.id.tv_item_name, Color.WHITE);
                    break;

            }

            //播放\暂停
            helper.setOnClickListener(R.id.btn_play, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null!=mOnItemClickListener){
                        mOnItemClickListener.onPlayMusic(helper.getAdapterPosition(),v);
                    }
                }
            });
            //条目
            helper.setOnClickListener(R.id.ll_item_view, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null!=mOnItemClickListener){
                        mOnItemClickListener.onDetails(item.getMusicID());
                    }
                }
            });
        }
    }

    /**
     * 提供手动刷新，当检查播放器任务正在进行的时候，最终会回调至此
     * @param musicInfo
     */
    public void notifyDataSetChanged(MusicInfo musicInfo){
        if(null!=musicInfo&&!TextUtils.isEmpty(musicInfo.getMusicID())){
            if(null!=mData&&mData.size()>0){
                for (int i = 0; i < mData.size(); i++) {
                    MusicInfo info = mData.get(i);
                    if(null!=musicInfo&&musicInfo.getMusicID().equals(info.getMusicID())){
                        info.setPlaying(true);
                    }else{
                        info.setPlaying(false);
                    }
                }
                this.notifyDataSetChanged();
            }
        }
    }


    /**
     * 刷新条目,这里刷新单个条目
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        if(null!=arg){
            MusicInfo musicInfo= (MusicInfo) arg;
            if(null!=musicInfo&&null!=mData&&mData.size()>0){
                int position=0;
                for (int i = 0; i < mData.size(); i++) {
                    MusicInfo info = mData.get(i);
                    if(musicInfo.getMusicID().equals(info.getMusicID())){
                        info.setPlauStatus(musicInfo.getPlauStatus());
                    }else{
                        info.setPlauStatus(0);
                    }
                }
                Logger.d(TAG,"列表收到观察者刷新,类型："+musicInfo.getPlauStatus()+"位置："+position+",musicID："+musicInfo.getMusicID());
                this.notifyDataSetChanged();
            }
        }
    }

    public interface OnItemClickListener{
        void onPlayMusic(int position, View view);
        void onDetails(String musicID);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
