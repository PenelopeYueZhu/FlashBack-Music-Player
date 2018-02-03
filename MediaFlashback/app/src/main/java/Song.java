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
    private String parent_album;
    private state current_state;
    private String location;    // TODO:: temporary way of storing location
    private String artist_name;
    private int length_in_seconds;
    private int year_of_release;
    private int time_last_played; // TODO:: temporary way of storing the time stamp
    private double probability; // TODO:: not yet implemented?


    /* the default constructor */
    public Song(){
        title = "";
        parent_album = "NA";
        current_state = state.NEITHER;
        location = "NA";
        artist_name = "";
        length_in_seconds = -1;
        year_of_release = -1;
        time_last_played = -1;
    }

    public Song(String title, String parent_album,
                String artist_name, int length_in_seconds,
                int year_of_release){
        this();
        this.title = title;
        this.parent_album = parent_album;
        this.length_in_seconds = length_in_seconds;
        this.artist_name = artist_name;
        this.year_of_release = year_of_release;
    }
}
