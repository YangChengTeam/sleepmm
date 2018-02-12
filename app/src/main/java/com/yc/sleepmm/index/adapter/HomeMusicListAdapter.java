package com.yc.sleepmm.index.adapter;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.mode.PlayerStatus;
import com.music.player.lib.util.DateUtil;
import com.yc.sleepmm.R;
import com.yc.sleepmm.index.util.CommonUtils;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * TinyHung@Outlook.com
 * 2018/1/21.
 * 首页音乐列表
 */

public class HomeMusicListAdapter extends BaseQuickAdapter<MusicInfo, BaseViewHolder> implements Observer {

    private static final String TAG = HomeMusicListAdapter.class.getSimpleName();

    public HomeMusicListAdapter( List<MusicInfo> data) {
        super(R.layout.item_re_home_music_list, data);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final MusicInfo item) {
        if (null != item) {
            String seconds = item.getTime();
            if (TextUtils.isEmpty(seconds)) {
                seconds = "1.0";
            }
            float second = Float.parseFloat(seconds);
            helper.setText(R.id.tv_item_name, item.getTitle())
                    .setText(R.id.tv_item_play_count, item.getPlay_num())
                    .setText(R.id.tv_item_num, (helper.getAdapterPosition() + 1) + "")
                    .setText(R.id.tv_item_durtion, (DateUtil.getTimeLengthString((int) (second))));
            switch (item.getPlauStatus()) {
                //缓冲中
                case PlayerStatus.PLAYER_STATUS_ASYNCPREPARE:
                    helper.setVisible(R.id.tv_item_num, true);
                    helper.setVisible(R.id.ic_play_anim, false);
                    helper.setImageResource(R.id.ic_item_play, R.drawable.ic_play_pause);
                    helper.setTextColor(R.id.tv_item_name, CommonUtils.getColor(R.color.app_style));
                    break;
                //播放中
                case PlayerStatus.PLAYER_STATUS_PLAYING:
                    ImageView ic_play_anim =  helper.getView(R.id.ic_play_anim);
                    ic_play_anim.setImageResource(R.drawable.play_anim);
                    AnimationDrawable drawable = (AnimationDrawable) ic_play_anim.getDrawable();
                    if (null != drawable) {
                        if (drawable.isRunning()) drawable.stop();
                        drawable.start();
                    }
                    helper.setVisible(R.id.tv_item_num, false);
                    helper.setVisible(R.id.ic_play_anim, true);
                    helper.setImageResource(R.id.ic_item_play, R.drawable.ic_play_pause);
                    helper.setTextColor(R.id.tv_item_name, CommonUtils.getColor(R.color.app_style));
                    break;
                //暂停中
                case PlayerStatus.PLAYER_STATUS_PAUSE:
                    ImageView icPlayAnim = (ImageView) helper.getView(R.id.ic_play_anim);
                    icPlayAnim.setImageResource(R.drawable.play_anim);
                    AnimationDrawable drawableAnimation = (AnimationDrawable) icPlayAnim.getDrawable();
                    if (null != drawableAnimation) {
                        drawableAnimation.stop();
                    }
                    helper.setVisible(R.id.tv_item_num, false);
                    helper.setVisible(R.id.ic_play_anim, true);
                    helper.setImageResource(R.id.ic_item_play, R.drawable.ic_item_play_true);
                    helper.setTextColor(R.id.tv_item_name, CommonUtils.getColor(R.color.app_style));
                    break;
                //播放完成或已停止
                case PlayerStatus.PLAYER_STATUS_STOP:
                    //播放失败
                case PlayerStatus.PLAYER_STATUS_ERROR:
                    //为空
                case PlayerStatus.PLAYER_STATUS_EMPOTY:
                    helper.setVisible(R.id.tv_item_num, true);
                    helper.setVisible(R.id.ic_play_anim, false);
                    helper.setImageResource(R.id.ic_play_anim, 0);
                    helper.setImageResource(R.id.ic_item_play, R.drawable.ic_item_play);
                    helper.setTextColor(R.id.tv_item_name, Color.WHITE);
                    break;

            }
        }
    }

    /**
     * 刷新条目,这里刷新单个条目
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        if (null != arg) {
            MusicInfo musicInfo = (MusicInfo) arg;
            if (null != musicInfo && null != mData && mData.size() > 0) {

                int position = 0;
                for (int i = 0; i < mData.size(); i++) {
                    MusicInfo info = mData.get(i);
                    if (musicInfo.getId().equals(info.getId())) {
                        info.setPlauStatus(musicInfo.getPlauStatus());
                        position = i;
                        break;
                    } else {
                        info.setPlauStatus(0);
                    }
                }
                notifyItemChanged(position);
//                this.notifyDataSetChanged();
            }
        }
    }

}
