package com.gaparmar.mediaflashback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private Song s;

    ImageButton playButton;
    ImageButton pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button launchFlashbackActivity = (Button) findViewById(R.id.flashback_button);

        launchFlashbackActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick( View view ){
                launchActivity();
            }
        });
    }

    public void launchActivity(){
        //input = (EditText)findViewById(R.id.in_time) ;
        Intent intent = new Intent(this, FlashbackActivity.class);
        //intent.putExtra("transferred_string", input.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }
}
