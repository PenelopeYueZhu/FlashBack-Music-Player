package com.gaparmar.mediaflashback.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gaparmar.mediaflashback.DataStorage.LogInstance;
import com.gaparmar.mediaflashback.MusicDownloader;
import com.gaparmar.mediaflashback.R;

import java.util.ArrayList;

public class DownloadHandlerActivity extends AppCompatActivity {
    EditText EditText_url;
    EditText time;
    private MusicDownloader musicDownloader;
    ArrayList<LogInstance> t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_handler);
        EditText_url = findViewById(R.id.url_entered);
        musicDownloader = new MusicDownloader(this);
        time = findViewById(R.id.timeMock);
//        t = new ArrayList<>();
//        FirebaseHandler.getLogList("dank Gaurav 2.0", t);
    }

    /**
     * Downloads the song from the given url
     * @param view
     */
    public void downloadSong(View view){
        Log.d("DownloadHandlerActivity", "downloadButton clicked");
        Log.d("DownloadHandlerActivity", EditText_url.getText().toString());
        // Check if the url field is empty

        if (EditText_url.getText() == null || EditText_url.getText().toString().equals("Enter URL here")){
//            System.out.println(t.size());
            Toast.makeText(this, t.toString(), Toast.LENGTH_SHORT).show();



        } else{
            String url = EditText_url.getText().toString();
            Toast.makeText(this, "Downloading from " + url, Toast.LENGTH_SHORT).show();
            musicDownloader.downloadData(url, "Song name", "mp3");
        }
     }

     public void mockTime(View view){
        MockCalendar mockCal = new MockCalendar();
        mockCal.setHour(Integer.parseInt(time.getText().toString()));
     }
}
