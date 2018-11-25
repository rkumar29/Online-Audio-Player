package com.rajesh.myplayer;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SongResponseInterface,SearchView.OnQueryTextListener {

    RecyclerView mSongsRecycler;
    SongsAdapter mSongsAdapter;
    List<SongsBean> mSongsList = new ArrayList<>();
    SearchView searchView;
    ProgressBar progressBar;
    String songPlayUri;
    TextView mShowHistory;
    String mSongName,mArtistName,mCoverImage;

    Intent myPlayerIntent;
    DatabaseHandler databaseHandler;

    DownloadManager.Request downloadRequest;
    DownloadManager downloadManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHandler = new DatabaseHandler(this);
        getUiObject();

        getListOfSongs();
    }

    private void getUiObject() {
        mSongsRecycler = findViewById(R.id.rv_song_list);
        searchView = findViewById(R.id.sv_songs);
        progressBar = findViewById(R.id.progress);
        mShowHistory = findViewById(R.id.tv_show_history);

        searchView.setOnQueryTextListener(this);
        mShowHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showHistory = new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(showHistory);
            }
        });
    }


    private void getListOfSongs(){

        progressBar.setVisibility(View.VISIBLE);

        String URL_SONGS = "http://starlord.hackerearth.com/studio";

        JsonArrayRequest request = new JsonArrayRequest(URL_SONGS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0;i < response.length();i++){
                    SongsBean songsBean = new SongsBean();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        mSongName = jsonObject.getString("song");
                        songsBean.setSong(mSongName);

                        songPlayUri = jsonObject.getString("url");
                        songsBean.setUrl(songPlayUri);

                        mArtistName = jsonObject.getString("artists");
                        songsBean.setArtists(mArtistName);

                        mCoverImage = jsonObject.getString("cover_image");
                        songsBean.setCover_image(jsonObject.getString("cover_image"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mSongsList.add(songsBean);

                }
                setAdapter(mSongsList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue  = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private void songHistory(String mSongName,int typ) {
      //  Toast.makeText(this, ""+mSongName+" "+typ, Toast.LENGTH_SHORT).show();
        databaseHandler.addSong(new Song(mSongName,typ));

    }


    private void setAdapter(List<SongsBean> mSongsList) {
        mSongsAdapter = new SongsAdapter(mSongsList,getApplicationContext(),this);
        RecyclerView.LayoutManager layoutManager  = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,
                false);
        mSongsRecycler.setLayoutManager(layoutManager);
        mSongsRecycler.setItemAnimator(new DefaultItemAnimator());
        mSongsRecycler.setAdapter(mSongsAdapter);

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void play(int position,String songPlayUri,String songName,String artistName,String imageUri) {

        final int type = 1;

        Toast.makeText(this, "Playing...", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        myPlayerIntent = new Intent(this,PlayerActivity.class);
        myPlayerIntent.putExtra("song_url",songPlayUri);
        myPlayerIntent.putExtra("song_name",songName);
        myPlayerIntent.putExtra("artist_name",artistName);
        myPlayerIntent.putExtra("cover_image",imageUri);

        songHistory(songName,type);

        startActivity(myPlayerIntent);
        progressBar.setVisibility(View.GONE);

    }


    @Override
    public void download(int position,String songPlayUri,String mSongName) {
        final int type = 2;
        Toast.makeText(this, "Downloading...", Toast.LENGTH_LONG).show();
        songHistory(mSongName,type);

        downloadRequest = new DownloadManager.Request(Uri.parse(songPlayUri));

        downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,mSongName+".mp3");
        downloadRequest.allowScanningByMediaScanner();
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(downloadRequest);
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        newText  = newText.toLowerCase();
        ArrayList<SongsBean> songsList = new ArrayList<>();
        for (SongsBean songBean : mSongsList) {
            String song = songBean.getSong().toLowerCase();

            if (song.contains(newText)){
                songsList.add(songBean);
            }
        }
        mSongsAdapter.setFilter(songsList);
        return true;
    }
}
