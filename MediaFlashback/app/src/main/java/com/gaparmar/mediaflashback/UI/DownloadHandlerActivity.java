package com.gaparmar.mediaflashback.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.gaparmar.mediaflashback.DataStorage.LogInstance;
import com.gaparmar.mediaflashback.MusicDownloader;
import com.gaparmar.mediaflashback.R;
import com.gaparmar.mediaflashback.WhereAndWhen.DownloadService;

import java.util.ArrayList;
import java.util.Calendar;

public class DownloadHandlerActivity extends AppCompatActivity {
    EditText EditText_url;
    EditText inputTitle;
    TimePicker time;
    private MusicDownloader musicDownloader;
    ArrayList<LogInstance> t;
    public static MockCalendar mockedTime;
    public static boolean isToggled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_handler);
        EditText_url = findViewById(R.id.url_entered);
        inputTitle = findViewById(R.id.inputTrack);
        time = findViewById(R.id.timePicker);

        Intent intent = new Intent(this, DownloadService.class);
        getApplicationContext().startService(intent);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);

        musicDownloader = DownloadService.getMusicDownloader();
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
            } else if (url.contains("mp3") && !filename.equals("")){
                musicDownloader.downloadData(url, filename, "mp3");
            } else {
                Toast.makeText(this, "Input filename", Toast.LENGTH_LONG).show();
            }
        }
     }

     public void mockTime(View view){
         mockedTime = new MockCalendar();
         mockedTime.setHour(time.getHour());
     }

     public void toggleTimeMock(View view){
         isToggled = ((ToggleButton)findViewById(R.id.toggle_time)).isChecked();
         System.out.println("TOGGLING MOCKED TIME, now: "+isToggled);
     }


    /**
     * Gets the time to be used for calculating probability
     * @return
     */
     public static Calendar getTime(){
        if(isToggled){
            return mockedTime;
        }else{
            return Calendar.getInstance();
        }

     }



}
