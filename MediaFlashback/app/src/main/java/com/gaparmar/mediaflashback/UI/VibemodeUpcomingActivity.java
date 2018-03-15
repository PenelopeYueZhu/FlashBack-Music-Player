package com.gaparmar.mediaflashback.UI;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaparmar.mediaflashback.R;

public class VibemodeUpcomingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibemode);
        appendUpcomingSong("SSSSSS");
    }


    /**
     * Adds the given song to the Upcoming Songs in the vibe mode
     * @param song the song name to be added
     */
    public void appendUpcomingSong(String song){
        LinearLayout upcoming = findViewById(R.id.upcoming_songs);

        LinearLayout.LayoutParams temp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        temp.setMargins(100,0,0,0);
        TextView t = new TextView(this);
        t.setText(song);
        t.setLayoutParams(temp);
        t.setTextColor(Color.parseColor("#c4d7f2"));
        t.setTextSize(20);
        upcoming.addView(t);
    }
}