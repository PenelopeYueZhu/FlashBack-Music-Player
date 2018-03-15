package com.gaparmar.mediaflashback.UI;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gaparmar.mediaflashback.Album;
import com.gaparmar.mediaflashback.MusicPlayer;
import com.gaparmar.mediaflashback.MusicQueuer;
import com.gaparmar.mediaflashback.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlbumsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlbumsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView mListView;

    private MusicPlayer mp;
    private MusicQueuer mq;

    /**
     * The required empty constructor
     */
    public AlbumsFragment() {}

    public static AlbumsFragment newInstance() {
        AlbumsFragment fragment = new AlbumsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    /**
     * Runs when the activity is created
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mq = BackgroundService.getMusicQueuer();
        mp = BackgroundService.getMusicPlayer();
    }

    /**
     * Runs when the view is created
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return The view created by the activity
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_albums, container, false);
    }

    /**
     * Creates the buttons that are displayed and causes them to play
     * the selected album
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        final ArrayList<String> albums = mq.getEntireAlbumList();
        mListView = (ListView)getView().findViewById(R.id.album_list);
        String[] titles = new String[albums.size()];

        // Loads in the album titles
        for(int i = 0; i < titles.length; ++i){
            titles[i] = albums.get(i);
        }

        ArrayAdapter adapter = new ArrayAdapter(this.getContext(),
                android.R.layout.simple_list_item_1, titles);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Album a = BackgroundService.getMusicQueuer().getAlbum(albums.get(position));
                Log.d("loading album", "Album: " + a.getAlbumTitle() + "has " + a.getNumSongs());
               // BackgroundService.getMusicQueuer().readThroughAllAlbums();
                mp.loadAlbum(a);
                MainActivity.isPlaying = true;
                onDetach();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into com.gaparmar.mediaflashback.UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
