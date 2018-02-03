/**
 * Created by gauravparmar on 2/2/18.
 */

// -1, NA, represents uninitialized value
public class Song {

    enum state {
        LIKED, DISLIKED, NEITHER
    }

    // Declaring the member variables of the Song object

    //TODO:
    // mp3 file
    // cover art

    private String title;
    private String parentAlbum;
    private state currentState;
    private String location;    // TODO:: temporary way of storing location
    private String artistName;
    private int lengthInSeconds;
    private int yearOfRelease;
    private int timeLastPlayed; // TODO:: temporary way of storing the time stamp
    private double probability; // TODO:: not yet implemented?


    /* the default constructor */
    public Song(){
        title = "";
        parentAlbum = "NA";
        currentState = state.NEITHER;
        location = "NA";
        artistName = "";
        lengthInSeconds = -1;
        yearOfRelease = -1;
        timeLastPlayed = -1;
    }

    public Song(String title, String parentAlbum,
                String artistName, int lengthInSeconds,
                int yearOfRelease){
        this();
        this.title = title;
        this.parentAlbum = parentAlbum;
        this.lengthInSeconds = lengthInSeconds;
        this.artistName = artistName;
        this.yearOfRelease = yearOfRelease;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getParentAlbum(){
        return this.parentAlbum;
    }

    public void setParentAlbul(String parentAlbum){
        this.parentAlbum = parentAlbum;
    }

    public state getCurrentState(){
        return this.currentState;
    }

    public void setCurrentState(state currentState){
        this.currentState = currentState;
    }

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getArtistName(){
        return this.artistName;
    }

    public void setArtistName(String artistName){
        this.artistName = artistName;
    }

    public int getLengthInSeconds(){
        return this.lengthInSeconds;
    }

    public void setLengthInSeconds(int lengthInSeconds){
        this.lengthInSeconds = lengthInSeconds;
    }

    public int getYearOfRelease(){
        return this.yearOfRelease;
    }

    public void setYearOfRelease(int yearOfRelease){
        this.yearOfRelease = yearOfRelease;
    }

    public int getTimeLastPlayed(){
        return this.timeLastPlayed;
    }

    public void setTimeLastPlayed(int timeLastPlayed){
        this.timeLastPlayed = timeLastPlayed;
    }

    public void updateProbability(){
        double prob = 0.0;
        // TODO:: the logic to calculate probability would go here
        this.probability = prob;
    }

    public double getProbability(){
        return this.probability;
    }

}
