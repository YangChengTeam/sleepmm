package com.yc.sleepmm.index.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.mode.PlayerStatus;
import com.music.player.lib.util.DateUtil;
import com.music.player.lib.util.Logger;
import com.yc.sleepmm.R;
import com.yc.sleepmm.index.util.CommonUtils;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * TinyHung@Outlook.com
 * 2018/1/18.
 */

public class MusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Observer{

    private static final String TAG = "MusicListAdapter";
    private final LayoutInflater mLayoutInflater;
    private List<MusicInfo> mData;

    public MusicListAdapter(Context context, List<MusicInfo> musicInfos) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mData=musicInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_re_simple_music_list,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        MusicInfo musicInfo = mData.get(position);
        if(null!=musicInfo){
            String seconds = musicInfo.getMusicDurtion();
            if(TextUtils.isEmpty(seconds)){
                seconds="1.0";
            }
            float second = Float.parseFloat(seconds);
            viewHolder.tv_item_name.setText(musicInfo.getMusicTitle());
            viewHolder.tv_item_author.setText(musicInfo.getMusicAuthor());
            viewHolder.tv_item_drutaion.setText(DateUtil.getTimeLengthString((int) (second)));
            viewHolder.tv_item_num.setText((position+1)+"");

            switch (musicInfo.getPlauStatus()) {
                //缓冲中
                case PlayerStatus.PLAYER_STATUS_ASYNCPREPARE:
                    viewHolder.tv_item_num.setVisibility(View.VISIBLE);
                    viewHolder.ic_play_anim.setVisibility(View.GONE);
                    viewHolder.tv_item_name.setTextColor(CommonUtils.getColor(R.color.app_style));
                    break;
                //播放中
                case PlayerStatus.PLAYER_STATUS_PLAYING:
                    viewHolder.tv_item_num.setVisibility(View.GONE);
                    viewHolder.ic_play_anim.setVisibility(View.VISIBLE);
                    viewHolder.ic_play_anim.setImageResource(R.drawable.play_anim);
                    viewHolder.tv_item_name.setTextColor(CommonUtils.getColor(R.color.app_style));
                    AnimationDrawable drawable = (AnimationDrawable) viewHolder.ic_play_anim.getDrawable();
                    if(null!=drawable){
                        if(drawable.isRunning()) drawable.stop();
                        drawable.start();
                    }
                    break;
                //暂停中
                case PlayerStatus.PLAYER_STATUS_PAUSE:
                    viewHolder.ic_play_anim.setVisibility(View.VISIBLE);
                    viewHolder.tv_item_num.setVisibility(View.GONE);
                    viewHolder.ic_play_anim.setImageResource(R.drawable.play_anim);
                    viewHolder.tv_item_name.setTextColor(CommonUtils.getColor(R.color.app_style));
                    AnimationDrawable drawableAnimation = (AnimationDrawable) viewHolder.ic_play_anim.getDrawable();
                    if(null!=drawableAnimation){
                        drawableAnimation.stop();
                    }
                    break;
                //播放完成或已停止
                case PlayerStatus.PLAYER_STATUS_STOP:
                    //播放失败
                case PlayerStatus.PLAYER_STATUS_ERROR:
                    //为空
                case PlayerStatus.PLAYER_STATUS_EMPOTY:
                    viewHolder.ic_play_anim.setImageResource(0);
                    viewHolder.ic_play_anim.setVisibility(View.GONE);
                    viewHolder.tv_item_num.setVisibility(View.VISIBLE);
                    viewHolder.tv_item_name.setTextColor(CommonUtils.getColor(R.color.coment_color));
                    break;

            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null!=mOnItemClickListener){
                        mOnItemClickListener.onItemClick(position,v);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null==mData?0:mData.size();
    }

    public void setNewData(List<MusicInfo> musicInfos) {
        this.mData=musicInfos;
        this.notifyDataSetChanged();
    }

    /**
     * 刷新列表状态
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
                Logger.d(TAG,"播放器示例界面列表收到观察者刷新,类型："+musicInfo.getPlauStatus()+"，位置："+position+",musicID："+musicInfo.getMusicID());
                this.notifyDataSetChanged();
            }
        }
    }

    public List<MusicInfo> getData() {
        return mData;
    }

    public interface OnItemClickListener{
        void onItemClick(int position, View view);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_item_name;
        private TextView tv_item_author;
        private TextView tv_item_drutaion;
        private TextView tv_item_num;
        private ImageView ic_play_anim;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_item_name=(TextView)itemView.findViewById(R.id.tv_item_name);
            tv_item_author=(TextView)itemView.findViewById(R.id.tv_item_author);
            tv_item_drutaion=(TextView)itemView.findViewById(R.id.tv_item_drutaion);
            tv_item_num=(TextView) itemView.findViewById(R.id.tv_item_num);
            ic_play_anim=(ImageView)itemView.findViewById(R.id.ic_play_anim);
        }
    }
}
