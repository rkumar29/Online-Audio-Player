package com.rajesh.myplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rajesh on 12/20/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<Song> historyBeanList;
    private Context context;

    HistoryAdapter(List<Song> historyBeanList, Context context) {
        this.historyBeanList = historyBeanList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = historyBeanList.get(holder.getAdapterPosition());
        holder.mSerial.setText(Integer.toString(song.getId()));
        holder.mSong.setText(song.getSongName());
      //  holder.mType.setText(Integer.toString(song.getType()));

        if (Integer.toString(song.getType()).equals("1")){
            holder.mType.setText("Stream");
        }else {
            holder.mType.setText("Download");
        }
    }

    @Override
    public int getItemCount() {
        return historyBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mSerial,mSong,mType;
        ViewHolder(View itemView) {
            super(itemView);
            mSerial = itemView.findViewById(R.id.tv_serial_no);
            mSong = itemView.findViewById(R.id.tv_songName);
            mType = itemView.findViewById(R.id.tv_type);

        }
    }
}
