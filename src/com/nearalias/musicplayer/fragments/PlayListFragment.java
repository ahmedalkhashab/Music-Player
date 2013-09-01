package com.nearalias.musicplayer.fragments;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ListFragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
	private MediaPlayer mediaPlayer;

	private void printDirectoriesToLog() {
		File path = new File(Environment.getExternalStorageDirectory() + "/nearaliasMusicPlayer/");
		Log.i("BLAH", path.toString() + "");
		if (path.exists())
			Log.i("BLAH", "path exists");
		if (path.isDirectory())
			Log.i("BLAH", "path is a folder");
		File[] folders = path.listFiles();
		for (File folder : folders) {
			Log.i("BLAH", "folder: " + folder);
			if (folder.isFile())
				continue;
			File[] files = folder.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String fileName) {
					return fileName.endsWith(".mp3") || fileName.endsWith(".mp4") || fileName.endsWith(".mkv") || fileName.endsWith(".wav")
							|| fileName.endsWith(".ogg") || fileName.endsWith(".m4a");
				}
			});
			if (files.length == 0)
				continue;
			mediaPlayer = new MediaPlayer();
			for (File file : files) {
				try {
					FileInputStream fis = new FileInputStream(file);
					FileDescriptor fileDescriptor = fis.getFD();
					mediaPlayer.setDataSource(fileDescriptor);
					mediaPlayer.prepare();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					Log.i("BLAH", "file name: " + file);
					e.printStackTrace();
				}
				Log.i("BLAH", "||| file:" + file);
				Log.i("BLAH", "|||||| duration: " + mediaPlayer.getDuration());
				Log.i("BLAH", "|||||| current location: " + mediaPlayer.getCurrentPosition());
				Log.i("BLAH", "|||||| isPlaying: " + mediaPlayer.isPlaying());
//				if (!file.equals(files[2]))
//					mediaPlayer.reset();
				if (file.equals(files[0]))
					break;
			}
//			mediaPlayer.reset();
//			mediaPlayer.release();
		}
	}

	public PlayListFragment() {
		printDirectoriesToLog();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Toast.makeText(getActivity(), names.get(position), Toast.LENGTH_SHORT).show();
		startOrStopPlayingSong();
	}

	private void startOrStopPlayingSong() {
		if (mediaPlayer.isPlaying())
			mediaPlayer.pause();
		else
			mediaPlayer.start();
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
