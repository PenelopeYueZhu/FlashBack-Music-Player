package com.gaparmar.mediaflashback.UI;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gaparmar.mediaflashback.MusicPlayer;
import com.gaparmar.mediaflashback.MusicQueuer;
import com.gaparmar.mediaflashback.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TracksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TracksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TracksFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private MusicQueuer mq;
    private MusicPlayer mp;

    public TracksFragment() {
        // Required empty public constructor
    }

    public static TracksFragment newInstance() {
        TracksFragment fragment = new TracksFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //mq = MainActivity.getMusicQueuer();
        //mp = MainActivity.getMusicPlayer();

        mq = BackgroundService.getMusicQueuer();
        mp = BackgroundService.getMusicPlayer();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tracks, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        final ArrayList<String> songs = mq.getEntireSongList();
        mListView = (ListView)getView().findViewById(R.id.album_list);
        String[] titles = new String[songs.size()];

        for(int i = 0; i < titles.length; ++i){
            titles[i] = mq.getSong(songs.get(i)).getTitle();
        }

        ArrayAdapter adapter = new ArrayAdapter(this.getContext(),
                android.R.layout.simple_list_item_1, titles);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String fileName = mq.getSong( songs.get(position)).getFileName();
                System.out.println( "Song is clicked " + mq.getSong(fileName).getTitle());
                mp.loadNewSong(fileName);
                MainActivity.isPlaying = true;
                onDetach();
            }
        });
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
