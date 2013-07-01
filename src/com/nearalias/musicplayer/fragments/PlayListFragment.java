package com.nearalias.musicplayer.fragments;

import java.util.ArrayList;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.nearalias.musicplayer.R;
import com.nearalias.musicplayer.SongAdapter;
import com.nearalias.musicplayer.models.Song;

/**
 * Fragment that appears in the "content_frame"
 */
public class PlayListFragment extends ListFragment {

	public static final String ARG_PLANET_NUMBER = "planet_number";
	public static final String ARG_SONG_NAME = "name";
	public static final String ARG_SONG_ARTIST = "artist";
	public static final String ARG_SONG_ALBUM = "album";
	public static final String ARG_SONG_GENRE = "genre";
	public static final String ARG_SONG_LYRIC = "lyric";

	private ArrayList<Song> songs;
	private ArrayList<String> names, artists, albums, genres, lyrics;

	public PlayListFragment() {
		// empty stub
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Toast.makeText(getActivity(), names.get(position), Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int i = getArguments().getInt(ARG_PLANET_NUMBER);
		names = getArguments().getStringArrayList(ARG_SONG_NAME);
		artists = getArguments().getStringArrayList(ARG_SONG_ARTIST);
		albums = getArguments().getStringArrayList(ARG_SONG_ALBUM);
		genres = getArguments().getStringArrayList(ARG_SONG_GENRE);
		lyrics = getArguments().getStringArrayList(ARG_SONG_LYRIC);
		setUpSongs();

		String planet = getResources().getStringArray(R.array.planets_array)[i];
		getActivity().setTitle(planet);

		setListAdapter(new SongAdapter(getActivity(), R.layout.fragment_song, songs));
	}

	private void setUpSongs() {
		songs = new ArrayList<Song>();
		for (int i = 0; i < names.size(); i++)
			songs.add(new Song(names.get(i), artists.get(i), albums.get(i), genres.get(i), lyrics.get(i)));
	}

}
