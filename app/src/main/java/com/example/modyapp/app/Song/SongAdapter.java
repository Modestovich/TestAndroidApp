package com.example.modyapp.app.Song;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.modyapp.app.MusicPlayer;
import com.example.modyapp.app.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mody on 7/1/2015.
 */
public class SongAdapter extends BaseAdapter implements Filterable{ //ArrayAdapter<Song> implements Filterable {

    private ArrayList<Song> fullListOfSongs;
    private ArrayList<Song> temporaryData;
    private Context context;
    static Integer previous = -1;

    public SongAdapter(Context context,int resId,  ArrayList<Song> songs){
        fullListOfSongs = songs;
        temporaryData = songs;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.list_view_music,
                    parent,false);
        }
        Song current_song = temporaryData.get(position);
        ((TextView) view.findViewById(R.id.song_name))
                .setText(current_song.getName());
        ((TextView) view.findViewById(R.id.artist_name))
                .setText(current_song.getSong().artist);
        ((TextView) view.findViewById(R.id.song_duration))
                .setText(current_song.getDuration());
        //detecting current playing song
        if(MusicPlayer.getCurrentSong()!=null &&
                current_song.getSong().id == MusicPlayer.getCurrentSong().id) {
            view.findViewById(R.id.player_playing)
                    .setVisibility(View.VISIBLE);
        }else{
            (view.findViewById(R.id.player_playing))
                    .setVisibility(View.INVISIBLE);
        }


        return view;
    }

    @Override
    public int getCount() {
        return temporaryData!=null?temporaryData.size():0;
    }

    @Override
    public Object getItem(int position) {
        return  temporaryData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  temporaryData.get(position).getSong().id;
    }

    @Override
    public Filter getFilter() {
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.length()>0? constraint.toString().toLowerCase():"";
                FilterResults result = new FilterResults();
                if (constraint.toString().length() > 0) {
                    ArrayList<Song> filterList = new ArrayList<Song>();
                    for(Song song: fullListOfSongs){
                        String textForFilter = song.getNameToFilter();
                        if(textForFilter.toLowerCase().contains(constraint)){
                            filterList.add(song);
                        }
                    }
                    result.values = filterList;
                    result.count = filterList.size();
                }else {
                    result.values = fullListOfSongs;
                    result.count = fullListOfSongs.size();
                }
                return result;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                temporaryData = (ArrayList<Song>) results.values;
                MusicPlayer.populateMusicPlayer(temporaryData);
                notifyDataSetChanged();
            }
        };
    }
}