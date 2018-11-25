package com.rajesh.myplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mGoBack,mClear;
    RecyclerView mHistoryRecycler;
    List<Song> mHistoryList;
    HistoryAdapter historyAdapter;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setHistoryRecycler();

        mGoBack = findViewById(R.id.tv_go_back);
        mClear = findViewById(R.id.tv_clear_history);
        mGoBack.setOnClickListener(this);
        mClear.setOnClickListener(this);

    }

    private void setHistoryRecycler() {
        databaseHandler = new DatabaseHandler(this);
        mHistoryList = new ArrayList<>();
        mHistoryList = databaseHandler.getAllSong();
        mHistoryRecycler = findViewById(R.id.rv_song_history);
        historyAdapter = new HistoryAdapter(mHistoryList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mHistoryRecycler.setLayoutManager(layoutManager);
        mHistoryRecycler.setItemAnimator(new DefaultItemAnimator());
        mHistoryRecycler.setAdapter(historyAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_go_back:
                finish();
                break;

            case R.id.tv_clear_history:
                databaseHandler.deleteAll();
                for (int i = 0;i < mHistoryList.size();i++) {
                    mHistoryList.remove(i);
                }
                historyAdapter.notifyDataSetChanged();
                break;
        }
    }
}
