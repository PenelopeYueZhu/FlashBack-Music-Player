package com.gaparmar.mediaflashback.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.gaparmar.mediaflashback.Artist;
import com.gaparmar.mediaflashback.MusicPlayer;
import com.gaparmar.mediaflashback.MusicQueuer;
import com.gaparmar.mediaflashback.R;

import java.util.ArrayList;

public class TracklistActivity extends AppCompatActivity {
    MusicPlayer mp;
    MusicQueuer mq;
    ListView mListView;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracklist);
        mp = BackgroundService.getMusicPlayer();

        backBtn = (ImageButton) findViewById(R.id.back);

        final ArrayList<String> tracklist = mp.getTrackList();
        mListView = (ListView) findViewById(R.id.tracklist);
        String[] titles = new String[tracklist.size()];

        // Loads in the album titles
        for(int i = 0; i < titles.length; ++i){
            titles[i] = tracklist.get(i);
        }

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, titles);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String fileName = mq.getSong( tracklist.get(position)).getFileName();
                System.out.println( "Song is clicked " + mq.getSong(fileName).getTitle());
                mp.loadNewSong(fileName);
                MainActivity.isPlaying = true;

            }
        });


    }

    public void back(View v) {
        finish();
    }


}
