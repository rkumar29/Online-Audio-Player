package com.rajesh.myplayer;

/**
 * Created by Rajesh on 12/17/2017.
 */

public interface SongResponseInterface {
    public void play(int position,String url,String name,String artist,String image);
    public void download(int position,String url,String songName);
}
