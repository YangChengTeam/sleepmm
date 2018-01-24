package com.yc.sleepmm.base.ui.adapter;

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
import com.yc.sleepmm.R;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * TinyHung@Outlook.com
 * 2018/1/18.
 */

public class MusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Observer{


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
        final MusicInfo musicInfo = mData.get(position);
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

                    break;
                //播放中
                case PlayerStatus.PLAYER_STATUS_PLAYING:
                    viewHolder.ic_play_anim.setImageResource(R.drawable.play_anim);
                    AnimationDrawable drawable = (AnimationDrawable) viewHolder.ic_play_anim.getDrawable();
                    if(null!=drawable){
                        drawable.start();
                    }
                    viewHolder.ic_play_anim.setVisibility(View.VISIBLE);
                    viewHolder.tv_item_num.setVisibility(View.GONE);
                    break;
                    //这里未做详细处理
                //暂停中
                case PlayerStatus.PLAYER_STATUS_PAUSE:
                //播放完成或已停止
                case PlayerStatus.PLAYER_STATUS_STOP:
                    //播放失败
                case PlayerStatus.PLAYER_STATUS_ERROR:
                    viewHolder.ic_play_anim.setImageResource(0);
                    viewHolder.ic_play_anim.setVisibility(View.GONE);
                    viewHolder.tv_item_num.setVisibility(View.VISIBLE);
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
     * 提供手动刷新，当检查播放器任务正在进行的时候，最终会回调至此
     * @param musicInfo
     */
    public void notifyDataSetChanged(MusicInfo musicInfo){
        if(null!=musicInfo&&null!=mData&&mData.size()>0){
            int position=0;
            for (int i = 0; i < mData.size(); i++) {
                MusicInfo info = mData.get(i);
                if(musicInfo.getMusicID().equals(info.getMusicID())){
                    info.setPlauStatus(2);
                    position=i;
                    break;
                }
            }
            this.notifyItemChanged(position);
        }
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
                        position=i;
                        break;
                    }
                }
                this.notifyItemChanged(position);
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
