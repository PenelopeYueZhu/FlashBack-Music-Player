package com.gaparmar.mediaflashback;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DownloadHandlerActivity extends AppCompatActivity {
    EditText EditText_url;
    private MusicDownloader musicDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_handler);
        EditText_url = findViewById(R.id.url_entered);
        musicDownloader = new MusicDownloader(this);
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

        //TODO remove this
        Song s1 = new Song();
        s1.setSongTitle("dank Gaurav");


//        DatabaseReference temp = ref.child("new_song_list").push();
//        temp.setValue(s1);
          FirebaseHandler.saveSongToSongList(s1);
     }
}
