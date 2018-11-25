package com.rajesh.myplayer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajesh on 12/19/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "songs_manager";
    private static final String TABLE_SONGS = "song_history";

    private static final String KEY_ID = "id";
    private static final String SONG_NAME = "songName";
    private static final String SONG_TYPE = "type";

    DatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SONG_TABLE = "CREATE TABLE " + TABLE_SONGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + SONG_NAME + " TEXT,"
                + SONG_TYPE + " TEXT" + ")";

        sqLiteDatabase.execSQL(CREATE_SONG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);

        onCreate(sqLiteDatabase);
    }

    void addSong(Song song){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SONG_NAME,song.getSongName());
        contentValues.put(SONG_TYPE,song.getType());

        db.insert(TABLE_SONGS,null,contentValues);
        db.close();

    }

    List<Song> getAllSong(){
        List<Song> songList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_SONGS;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do {
                Song song = new Song();
                song.setId(Integer.parseInt(cursor.getString(0)));
                song.setSongName(cursor.getString(1));
                song.setType(Integer.parseInt(cursor.getString(2)));

                songList.add(song);
            }while (cursor.moveToNext());

        }

        return songList;
    }

    void deleteAll(){
        SQLiteDatabase database = this.getWritableDatabase();
        String delQuery = "DELETE FROM " + TABLE_SONGS;

        database.execSQL(delQuery);
        database.close();
    }

}
