package com.rajesh.myplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajesh on 12/17/2017.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyViewHolder> {

    private List<SongsBean> songsBeansList;
    private Context context;
    private SongResponseInterface songResponseInterface;

    SongsAdapter(List<SongsBean> songsBeansList, Context context, SongResponseInterface songResponseInterface) {
        this.songsBeansList = songsBeansList;
        this.context = context;
        this.songResponseInterface = songResponseInterface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        SongsBean songsBean = songsBeansList.get(holder.getAdapterPosition());
        holder.mSongName.setText(songsBean.getSong());
        holder.mArtists.setText(songsBean.getArtists());
        final String song_name = songsBeansList.get(holder.getAdapterPosition()).getSong();
        final String artist_name = songsBeansList.get(holder.getAdapterPosition()).getArtists();
        final String song_url = songsBeansList.get(holder.getAdapterPosition()).getUrl();
        final String image_url = songsBeansList.get(holder.getAdapterPosition()).getCover_image();

        Glide.with(context).load(image_url).into(holder.mCoverImage);

        holder.mSongPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songResponseInterface.play(holder.getAdapterPosition(), song_url, song_name, artist_name,image_url);
            }
        });
        holder.mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songResponseInterface.download(holder.getAdapterPosition(),song_url,song_name);
            }
        });

    }

    @Override
    public int getItemCount() {
        return songsBeansList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mSongName, mArtists;
        ImageView mCoverImage, mPlay, mDownload;
        LinearLayout mSongPlay;

        MyViewHolder(View itemView) {
            super(itemView);
            mSongName = itemView.findViewById(R.id.tv_song_name);
            mArtists = itemView.findViewById(R.id.tv_artist_name);
            mCoverImage = itemView.findViewById(R.id.iv_cover_image);
            mPlay = itemView.findViewById(R.id.iv_play);
            mDownload = itemView.findViewById(R.id.iv_download);
            mSongPlay = itemView.findViewById(R.id.ll_song);
        }
    }

    public void setFilter(List<SongsBean> newList) {
        songsBeansList = new ArrayList<>();
        songsBeansList.addAll(newList);
        notifyDataSetChanged();
    }

}
