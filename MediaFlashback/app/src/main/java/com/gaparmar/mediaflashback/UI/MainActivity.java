package com.gaparmar.mediaflashback.UI;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.Toolbar;

import com.gaparmar.mediaflashback.DataStorage.StorageHandler;
import com.gaparmar.mediaflashback.Friend;
import com.gaparmar.mediaflashback.MusicDownloader;
import com.gaparmar.mediaflashback.MusicPlayer;
import com.gaparmar.mediaflashback.MusicQueuer;
import com.gaparmar.mediaflashback.R;
import com.gaparmar.mediaflashback.WhereAndWhen.AddressRetriver;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.PeopleScopes;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates and handles events relating to the regular mode com.gaparmar.mediaflashback.UI screen
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private MusicQueuer musicQueuer;
    private MusicPlayer musicPlayer;

    // Objects for location
    private static MusicDownloader musicDownloader;
    private FusedLocationProviderClient mFusedLocationClient;
    private Handler addressHandler;
    private static AddressRetriver addressRetriver;
    private static UINormal tracker;

    private static ArrayList<String> stoppedInfo = new ArrayList<>();
    public static boolean isPlaying;
    private static boolean browsing = false;
    private static boolean firstTime = true;
    private static boolean inDownloadScreen = false;
    private static boolean viewingTracklist = false;
    private static ArrayList<Friend> friendList;
    public static Friend me;

    GoogleApiClient mGoogleApiClient;

    final int RC_INTENT = 200;
    final int RC_API_CHECK = 100;

    SignInButton signInButton;
    Toolbar toolbar;
    ProgressBar progressBar;

    public static Map<String, Integer> weekDays;

    public static AddressRetriver getAddressRetriver() {
        return addressRetriver;
    }

    public static MusicDownloader getMusicDownloader() {
        return musicDownloader;
    }

    public static UINormal getUITracker() {
        return tracker;
    }

    private BackgroundService backgroundService;
    private boolean isBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, 666);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tracker = new UINormal(this);
        tracker.setButtonFunctions();

        if(firstTime) {
            firstTime = false;
            GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    // The serverClientId is an OAuth 2.0 web client ID
                    .requestServerAuthCode("292202723687-bfhvb9ntufbr7dti0bnt0a1holr76vu0.apps.googleusercontent.com")
                    .requestEmail()
                    .requestScopes(new Scope(Scopes.PLUS_LOGIN),
                            new Scope(PeopleScopes.CONTACTS_READONLY))
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addOnConnectionFailedListener(this)
                    .addConnectionCallbacks(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                    .build();

            getIdToken();
            mGoogleApiClient.connect();
        }

        // Stores the days of the week
        weekDays = new HashMap<String, Integer>();
        weekDays.put("Monday", 1);
        weekDays.put("Tuesday", 2);
        weekDays.put("Wednesday", 3);
        weekDays.put("Thursday", 4);
        weekDays.put("Friday", 5);
        weekDays.put("Saturday", 6);
        weekDays.put("Sunday", 7);

        friendList = new ArrayList<>();

        Intent intent = new Intent(this, BackgroundService.class);
        getApplicationContext().startService(intent);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                MainActivity.getAddressRetriver().setLocation(location);
                Log.d("FBLgetting location", "Setting the location to address retriver");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        /// Register the listener with the Location Manager to receive location updates
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
        } catch( SecurityException e) {

        }

        // Initialize the music downloader
        if (musicDownloader == null) {
            musicDownloader = new MusicDownloader(this);
        }

        // Initialize the addresss retriver
        if (addressRetriver == null) {
            addressHandler = new Handler();
            addressRetriver = new AddressRetriver(this, addressHandler);
        }

        // Unless there is a song playing when we get back to normal mode, hide the button
     /*   if (!musicPlayer.wasPlayingSong()) {
            tracker.setButtonsPausing();
        } else {
            tracker.setButtonsPlaying();
        }*/


        //mPlayer.loadMedia(R.raw.replay);
        Button launchFlashbackActivity = findViewById(R.id.flashback_button);
      //  ImageButton playButton =  findViewById(R.id.play_button);
        Button browseBtn = findViewById(R.id.browse_button);
        ImageButton tracklistBtn = findViewById(R.id.tracklist);

        tracklistBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick( View view ) {
                launchTrackListActivtiy();
            }
        });



        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchLibraryActivity();
                finish();
            }
        });

        launchFlashbackActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlaying = BackgroundService.getMusicPlayer().isPlaying();
                StorageHandler.storeLastMode(MainActivity.this, 1);
                launchFlashbackActivity();
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
            List<String> idList = new ArrayList<String>();

            try {
                People peopleService = setUp(MainActivity.this, params[0]);



                Person profile = peopleService.people().get("people/me").setRequestMaskIncludeField("person.names").execute();

                String name = profile.getNames().get(0).getDisplayName();
                String proxy = name.substring(0,1) + name.substring(2,3) + name.substring(4,5);
                me = new Friend(name, profile.getNames().get(0).getMetadata().getSource().getId(), proxy);

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
                                for (Name n : names) {
                                    if(!idList.contains(n.getMetadata().getSource().getId()))
                                    {
                                        nameList.add(n.getDisplayName());
                                        idList.add(n.getMetadata().getSource().getId());
                                        System.out.println(n.getDisplayName());
                                        System.out.println(n.getMetadata().getSource().getId());
                                        System.out.println(n.getDisplayName().substring(0,1) +n.getDisplayName().substring(2,3)
                                        +n.getDisplayName().substring(4,5));
                                    }
                                }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            for(int i = 0; i < nameList.size(); i++)
            {
                String proxy = nameList.get(i).substring(0,1) + nameList.get(i).substring(2,3) + nameList.get(i).substring(4,5);
                friendList.add(new Friend(nameList.get(i), idList.get(i), proxy));
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

    /**
     * Pass in a Friend object (with the correct name, id, and proxy)
     * @param friend
     * @return the name if it is a friend, otherwise return the proxy name
     */
    public String isFriend(Friend friend)
    {
        for(Friend f : friendList)
        {
            if(f.equals(friend))
            {
                return friend.getName();
            }
        }
        return friend.getProxy();
    }


    @Override
    public void onStart(){
        super.onStart();
        if(StorageHandler.getLastMode(this) == 1){
            //launchFlashbackActivity();
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
    public void launchFlashbackActivity(){
        Log.d("MainActivity", "Launching Flashback mode");
        //input = (EditText)findViewById(R.id.in_time) ;
        Intent intent = new Intent(this, FlashbackActivity.class);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    /**
     * Launches the browse music screen
     */
    public void launchLibraryActivity() {
        Log.d("MainActivity", "Launching library");
        Intent intent = new Intent(this, LibraryActivity.class);
        setResult(Activity.RESULT_OK, intent);
        browsing = true;
        startActivity(intent);
    }

    /**
     * Launches tracklist
     */
    public void launchTrackListActivtiy() {
        Log.d("MainActivity", "Launching tracklist");
        viewingTracklist = true;
        Intent intent = new Intent(this, TracklistActivity.class);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    /**
     * Launches the download activity screen
     */
    public void launchDownloadActivity(){
        Log.d("MainActivity", "Launching downloads");
        inDownloadScreen = true;
        Intent intent = new Intent(this, DownloadHandlerActivity.class);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    /**
     * Launches the vibemode activity
     */
    public void launchVibemodeActivity(){
        Log.d("MainActivity", "Launching vibe mode");
        Intent intent = new Intent(this, VibemodeActivity.class);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
    }

    /**
     * Pauses the song when the activity is paused
     */
    @Override
    public void onPause(){
        super.onPause();

        // Checks if the user is not browsing songs, but is playing one
        if(!browsing && !viewingTracklist && !inDownloadScreen) {
            if (isPlaying) {
                // If they are, the song pauses
                stoppedInfo = BackgroundService.getMusicPlayer().stopPlaying();
                musicPlayer.pauseSong();
                isPlaying = true;
            } else {
                isPlaying = false;
            }
        }else if ((browsing || viewingTracklist || inDownloadScreen) && isPlaying ){
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
            if(!browsing && !inDownloadScreen && !viewingTracklist) {
                // Updates buttons accordingly.
                Log.i("Main:onResume","song playing, you are not browsing");
                tracker.setButtonsPausing();
                tracker.updateUI();
                //tracker.updateTrackInfo();
                isPlaying = true;
            }else{
                // Updates the buttons differently if the user is browsing
                Log.i("Main:onResume", "updating the buttons");
                tracker.updateToggle();
                tracker.setButtonsPlaying();
                tracker.updateUI();
                //tracker.updateTrackInfo();
            }
        }
        browsing = false;
        inDownloadScreen = false;
        viewingTracklist = false;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    /**
     * Starts the download activity
     * @param view
     */
    public void onLaunchDownloadClick(View view)
    {
        launchDownloadActivity();
    }

    /**
     * Starts the vibemode activity
     * @param view
     */
    public void onLaunchVibemodeClick(View view)
    {
        launchVibemodeActivity();
    }
}
