package com.gaparmar.mediaflashback;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.People;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.PeopleScopes;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.PhoneNumber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates and handles events relating to the regular mode UI screen
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    private static MusicPlayer musicPlayer;
    private static MusicQueuer musicQueuer;
    private FusedLocationProviderClient mFusedLocationClient;
    private Handler addressHandler;
    private static AddressRetriver addressRetriver;
    private static UINormal tracker;
    private static int[] stoppedInfo = new int[2];
    public static boolean isPlaying;
    private static boolean browsing = false;

    GoogleApiClient mGoogleApiClient;

    final int RC_INTENT = 200;
    final int RC_API_CHECK = 100;

    SignInButton signInButton;
    Toolbar toolbar;
    ProgressBar progressBar;

    public static Map<String, Integer> weekDays;
    public static MusicPlayer getMusicPlayer(){
        return musicPlayer;
    }
    public static MusicQueuer getMusicQueuer() { return musicQueuer; }
    public static AddressRetriver getAddressRetriver() {
        return addressRetriver;
    }
    private UserLocation userLocation;
    public static UINormal getUITracker() {
        return tracker;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, 666);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton = (SignInButton)findViewById(R.id.main_googlesigninbtn);
        signInButton.setOnClickListener(this);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // The serverClientId is an OAuth 2.0 web client ID
                .requestServerAuthCode("292202723687-bfhvb9ntufbr7dti0bnt0a1holr76vu0.apps.googleusercontent.com")
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN),
                        new Scope(PeopleScopes.CONTACTS_READONLY),
                        new Scope(PeopleScopes.USER_EMAILS_READ),
                        new Scope(PeopleScopes.USERINFO_EMAIL),
                        new Scope(PeopleScopes.USER_PHONENUMBERS_READ))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

        userLocation = new UserLocation(this);

        // Stores the days of the week
        weekDays = new HashMap<String, Integer>();
        weekDays.put("Monday", 1);
        weekDays.put("Tuesday", 2);
        weekDays.put("Wednesday", 3);
        weekDays.put("Thursday", 4);
        weekDays.put("Friday", 5);
        weekDays.put("Saturday", 6);
        weekDays.put("Sunday", 7);


        // Initialize UI
        tracker = new UINormal(this);
        tracker.setButtonFunctions();

        // Initializie the song functions
        if( musicQueuer == null ) {
            musicQueuer = new MusicQueuer(this);
            musicQueuer.readSongs();
            musicQueuer.readAlbums();
        }

        // Initialized the player
        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer(this, musicQueuer);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.d("MA:mFusedLocationClient","Got the location" );
                                addressRetriver.setLocation(location);
                            }
                        }
                    });
        } catch( SecurityException e) {
            System.out.println("Security Alert");
        }

        // Initialize the addresss retriver
        if( addressRetriver == null ) {
            addressHandler = new Handler( );
            addressRetriver = new AddressRetriver(this, addressHandler );
        }

        // Unless there is a song playing when we get back to normal mode, hide the button
        if( !musicPlayer.wasPlayingSong()) {
            tracker.setButtonsPausing();
        }
        else {
            tracker.setButtonsPlaying();
        }

        //mPlayer.loadMedia(R.raw.replay);
        Button launchFlashbackActivity = (Button) findViewById(R.id.flashback_button);
        ImageButton playButton = (ImageButton) findViewById(R.id.play_button);
        Button browseBtn = (Button) findViewById(R.id.browse_button);
        browseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick( View view ){
                launchLibrary();
                finish();
            }
        });

        launchFlashbackActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick( View view ){
                isPlaying = musicPlayer.isPlaying();
                StorageHandler.storeLastMode(MainActivity.this, 1);
                launchActivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("MainActivity", "sign in result");
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("MainActivity", "onActivityResult:GET_TOKEN:success:" + result.getStatus().isSuccess());
            // This is what we need to exchange with the server.
            Log.d("MainActivity", "auth Code:" + acct.getServerAuthCode());

            new PeoplesAsync().execute(acct.getServerAuthCode());

        } else {

            Log.d("MainActivity", result.getStatus().toString() + "\nmsg: " + result.getStatus().getStatusMessage());
        }
    }

    public static People setUp(Context context, String serverAuthCode) throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        // Redirect URL for web based applications.
        // Can be empty too.
        String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";


        // Exchange auth code for access token
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                httpTransport,
                jsonFactory,
                "292202723687-bfhvb9ntufbr7dti0bnt0a1holr76vu0.apps.googleusercontent.com",
                "Bjh3-HxFCdujrYkVbnIYfcck",
                serverAuthCode,
                redirectUrl).execute();

        // Then, create a GoogleCredential object using the tokens from GoogleTokenResponse
        GoogleCredential credential = new GoogleCredential.Builder()
                .setClientSecrets("292202723687-bfhvb9ntufbr7dti0bnt0a1holr76vu0.apps.googleusercontent.com",
                        "Bjh3-HxFCdujrYkVbnIYfcck")
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .build();

        credential.setFromTokenResponse(tokenResponse);

        // credential can then be used to access Google services
        return new People.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("MediaFlashback")
                .build();
    }

    class PeoplesAsync extends AsyncTask<String, Void, List<String>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<String> doInBackground(String... params) {

            List<String> nameList = new ArrayList<>();

            try {
                People peopleService = setUp(MainActivity.this, params[0]);

                ListConnectionsResponse response = peopleService.people().connections()
                        .list("people/me").setRequestMaskIncludeField("person.names")
                        .execute();
                List<Person> connections = response.getConnections();

                if(connections != null) {
                    for (Person person : connections) {
                        if (!person.isEmpty()) {
                            List<Name> names = person.getNames();
                            if (names != null)
                                System.out.println("Names");
                                for (Name name : names) {
                                    nameList.add(name.getDisplayName());
                                    System.out.println(name.getDisplayName());
                                }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return nameList;
        }
    }

    private void getIdToken() {
        // Show an account picker to let the user choose a Google account from the device.
        // If the GoogleSignInOptions only asks for IDToken and/or profile and/or email then no
        // consent screen will be shown here.
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_INTENT);
    }


    @Override
    public void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
        if(StorageHandler.getLastMode(this) == 1){
            launchActivity();
        }
    }

    /**
     * Requests location permission from the user
     * @param permission The permission being requested
     * @param requestCode The code for the permissions
     */
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Checks if the user requires permissions
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                // If they do, ask for it
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        }
    }

    /**
     * Launches flashback mode activity
     */
    public void launchActivity(){
        //input = (EditText)findViewById(R.id.in_time) ;
        Intent intent = new Intent(this, FlashbackActivity.class);
        //intent.putExtra("transferred_string", input.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    /**
     * Launches the browse music screen
     */
    public void launchLibrary() {
        Intent intent = new Intent(this, LibraryActivity.class);
        setResult(Activity.RESULT_OK, intent);
        browsing = true;
        startActivity(intent);
    }

    /**
     * Pauses the song when the activity is paused
     */
    @Override
    public void onPause(){
        super.onPause();

        // Checks if the user is not browsing songs, but is playing one
        if(!browsing) {
            if (isPlaying) {
                // If they are, the song pauses
                stoppedInfo = musicPlayer.stopPlaying();
                musicPlayer.pauseSong();
                isPlaying = true;
            } else {
                isPlaying = false;
            }
        }else if (browsing && isPlaying){
            tracker.setButtonsPlaying();
        }
    }

    /**
     * Resumes the song when the activity is resumed
     */
    @Override
    public void onResume() {
        super.onResume();
        // Checks if the user is playing a song, but now browsing
        if (isPlaying) {
            if(!browsing) {
                // Updates buttons accordingly.
                Log.i("Main:onResume","song playing, you are not browsing");
                tracker.setButtonsPausing();
                tracker.updateTrackInfo();
                isPlaying = true;
            }else{
                // Updates the buttons differently if the user is browsing
                Log.i("Main:onResume", "updating the buttons");
                tracker.setButtonsPlaying();
                tracker.updateTrackInfo();
            }
        }
        browsing = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_googlesigninbtn:
                Log.d("MainActivity", "btn click");
                System.out.println("a");
                getIdToken();
                break;

        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
