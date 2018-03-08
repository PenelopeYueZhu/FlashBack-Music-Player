package com.gaparmar.mediaflashback;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class DownloadHandlerActivity extends AppCompatActivity {
    EditText EditText_url;
    EditText time;
    private MusicDownloader musicDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_handler);
        EditText_url = findViewById(R.id.url_entered);
        musicDownloader = new MusicDownloader(this);
        time = findViewById(R.id.timeMock);
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
            Toast.makeText(this, "Please enter URL", Toast.LENGTH_SHORT).show();
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
