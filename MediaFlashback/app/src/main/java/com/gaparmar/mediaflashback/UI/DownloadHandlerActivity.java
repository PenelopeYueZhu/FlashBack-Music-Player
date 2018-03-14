package com.gaparmar.mediaflashback.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gaparmar.mediaflashback.DataStorage.LogInstance;
import com.gaparmar.mediaflashback.MusicDownloader;
import com.gaparmar.mediaflashback.R;
import com.gaparmar.mediaflashback.WhereAndWhen.DownloadService;

import java.util.ArrayList;

public class DownloadHandlerActivity extends AppCompatActivity {
    EditText EditText_url;
    EditText inputTitle;
    EditText time;
    private MusicDownloader musicDownloader;
    ArrayList<LogInstance> t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_handler);
        EditText_url = findViewById(R.id.url_entered);
        inputTitle = findViewById(R.id.inputTrack);

        Intent intent = new Intent(this, DownloadService.class);
        getApplicationContext().startService(intent);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);

        musicDownloader = DownloadService.getMusicDownloader();

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
            String filename = inputTitle.getText().toString();
            Toast.makeText(this, "Downloading from " + url, Toast.LENGTH_SHORT).show();
            // if service is lagging behind and has not initialized musicDownloader
            if (musicDownloader == null) {
                musicDownloader = DownloadService.getMusicDownloader();
            }

            if (url.contains("zip") && !filename.equals("")) {
                musicDownloader.downloadData(url, filename, "zip");
            } else {
                musicDownloader.downloadData(url, filename, "mp3");
            }
        }
     }

     public void mockTime(View view){
        MockCalendar mockCal = new MockCalendar();
        mockCal.setHour(Integer.parseInt(time.getText().toString()));
     }



}
