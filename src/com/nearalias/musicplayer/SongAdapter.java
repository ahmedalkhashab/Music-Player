package com.nearalias.musicplayer;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nearalias.musicplayer.models.Song;

public class SongAdapter extends ArrayAdapter<Song> {

	private Context context;
	private ArrayList<Song> songs;

	public SongAdapter(Context context, int textViewResourceId, ArrayList<Song> songs) {
		super(context, textViewResourceId, songs);
		this.context = context;
		this.songs = songs;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SongsViewHolder viewHolder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fragment_song, null);
			viewHolder = createSongsViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else viewHolder = (SongsViewHolder) convertView.getTag();

		setUpSongInfo(position, viewHolder);

		return convertView;
	}

	private SongsViewHolder createSongsViewHolder(View convertView) {
		SongsViewHolder viewHolder = new SongsViewHolder();
		viewHolder.name = (TextView) convertView.findViewById(R.id.name);
		viewHolder.artist = (TextView) convertView.findViewById(R.id.artist);
		viewHolder.album = (TextView) convertView.findViewById(R.id.album);
		return viewHolder;
	}

	private void setUpSongInfo(int position, SongsViewHolder viewHolder) {
		Song song = songs.get(position);
		if (song != null){
			viewHolder.name.setText(song.getName());
			viewHolder.artist.setText(song.getArtist());
			viewHolder.album.setText(song.getAlbum());
		}
	}
}
