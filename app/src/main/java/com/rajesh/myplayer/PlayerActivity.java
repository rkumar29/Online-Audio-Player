package com.rajesh.myplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    TextView mSongName,mArtistName,mStartTime,mTotalTime;
    ImageView mCoverImage,mForward,mRewind,mPlay,mClose,mPause;
    SeekBar mSeekBar;
    String mSongUri;
    MediaPlayer mediaPlayer;
    ProgressBar progressBar;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getUiObject();

        getDataFromIntent();
        setMediaSource();

    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        finish();
    }

    private void setMediaSource() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(mSongUri));
            mediaPlayer.prepare();
            mTotalTime.setText(setTime(mediaPlayer.getDuration()));


        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(this);

    }

    private void getUiObject() {

        mSongName = findViewById(R.id.tv_song);
        mArtistName = findViewById(R.id.textView_artist_name);
        mCoverImage = findViewById(R.id.iv_music_cover);
        mForward = findViewById(R.id.iv_forward);
        mRewind = findViewById(R.id.iv_rewind);
        mPlay = findViewById(R.id.iv_play_song);
        mSeekBar = findViewById(R.id.seekBar_player);
        mStartTime = findViewById(R.id.tv_start_time);
        mTotalTime = findViewById(R.id.tv_total_time);
        mClose = findViewById(R.id.iv_close_player);
        mPause = findViewById(R.id.iv_pause_song);
        progressBar = findViewById(R.id.progress_player);

        progressBar.setVisibility(View.VISIBLE);


        mClose.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        mPause.setOnClickListener(this);
        mForward.setOnClickListener(this);
        mRewind.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(this);

    }


    private void getDataFromIntent(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){

            mSongName.setText(extras.getString("song_name"));
            mArtistName.setText(extras.getString("artist_name"));
            Glide.with(getApplicationContext()).load(extras.getString("cover_image")).into(mCoverImage);
            mSongUri = extras.getString("song_url");


        }
        progressBar.setVisibility(View.GONE);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.iv_close_player:
                finish();
                mediaPlayer.stop();
                break;

            case R.id.iv_play_song:

                if (!mediaPlayer.isPlaying()){
                    mPause.setVisibility(View.VISIBLE);
                    mPlay.setVisibility(View.GONE);
                    mediaPlayer.start();
                    Toast.makeText(this, "Please Wait...", Toast.LENGTH_LONG).show();
                    handler = new Handler();
                    mSeekBar.setMax(mediaPlayer.getDuration());
                    mSeekBar.getProgress();

                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            mStartTime.setText(setTime(mediaPlayer.getCurrentPosition()));
                            mSeekBar.setProgress(mediaPlayer.getCurrentPosition());

                            handler.postDelayed(this,1000);
                        }
                    };

                    handler.postDelayed(runnable,1000);

                }else{
                    mPause.setVisibility(View.GONE);
                    mPlay.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.iv_forward:

                int currentPosition = mediaPlayer.getCurrentPosition()+10000;
                mediaPlayer.seekTo(currentPosition);
                mSeekBar.setProgress(currentPosition);
                mStartTime.setText(setTime(currentPosition));


                break;

            case R.id.iv_rewind:

                int currentPos = mediaPlayer.getCurrentPosition()-10000;
                mediaPlayer.seekTo(currentPos);
                mSeekBar.setProgress(currentPos);
                mStartTime.setText(setTime(currentPos));

                break;

            case R.id.iv_pause_song:

                mPause.setVisibility(View.GONE);
                mPlay.setVisibility(View.VISIBLE);

                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;

        }
    }

    private String setTime(int duration) {
        long duRation = (long) duration / 1000;
        long hours = duRation / 3600;
        long minutes = (duRation - hours * 3600) / 60;
        long seconds = duRation - (hours * 3600 + minutes * 60);

       return  "0"+hours+":"+minutes+":"+seconds;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        //seekBar.setProgress(progress);
        if (fromUser){
           // Toast.makeText(this, ""+progress, Toast.LENGTH_SHORT).show();
            mSeekBar.setProgress(progress);
            mStartTime.setText(setTime(progress));
            mediaPlayer.seekTo(progress);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

            mPlay.setVisibility(View.VISIBLE);
            mPause.setVisibility(View.GONE);
    }

   /* private long getTotalTimeInSec(int duration){
        long timeInmillisec = duration;
        long duRation = timeInmillisec / 1000;
        long hours = duRation / 3600;
        long minutes = (duRation - hours * 3600) / 60;
        return duRation - (hours * 3600 + minutes * 60);
    }*/


}
