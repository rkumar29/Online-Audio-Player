package com.rajesh.myplayer;

/**
 * Created by Rajesh on 12/19/2017.
 */

public class Song {

    private int id;
    private String songName;
    private int type;

    Song() {
    }

    public Song(int id, String songName, int type) {
        this.id = id;
        this.songName = songName;
        this.type = type;
    }

    Song(String songName, int type) {
        this.songName = songName;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
