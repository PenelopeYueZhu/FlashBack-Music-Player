package com.gaparmar.mediaflashback.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.gaparmar.mediaflashback.R;

/**
 * Represents the Browse Music screen
 */
public class LibraryActivity extends AppCompatActivity {

    private TextView mTextMessage;

    // Represents the nagivation bar at the bottom to see tracks / albums / ret
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            // Checks if the user selected the tracks screen
            switch (item.getItemId()) {
                case R.id.navigation_songs:
                    mTextMessage.setText("Tracks");
                    Log.d("LibraryActivity", "Opening up Tracks tab");
                    selectedFragment = TracksFragment.newInstance();
                    break;
                // Checks if the user selected the albums screen
                case R.id.navigation_albums:
                    mTextMessage.setText("Albums");
                    Log.d("LibraryActivity", "Opening up Albums tab");
                    selectedFragment = AlbumsFragment.newInstance();
                    break;
                // Checks if user selected the Artists tab
                case R.id.navigation_artists:
                    mTextMessage.setText("Artists");
                    Log.d("LibraryActivity", "Opening up Artists tab");
                    selectedFragment = ArtistsFragment.newInstance();
                    break;
                // Checks if user selected Favorites tab
                case R.id.navigation_favorites:
                    mTextMessage.setText("Favorites");
                    Log.d("LibraryActivity", "Opening up Favorites tab");
                    selectedFragment = FavoritesFragment.newInstance();
                    break;
                // Checks if the user selected the back button
                case R.id.navigation_mplayer:
                    startPlayer();
                    return true;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    /**
     * Returns the screen back to the regular mode
     */
    public void startPlayer() {
        startActivity(new Intent(LibraryActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Runs when the activity is created. creates the bottom navigation bar
     * @param savedInstanceState The Bundle that is passed in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library2);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, TracksFragment.newInstance());
        transaction.commit();

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

}
