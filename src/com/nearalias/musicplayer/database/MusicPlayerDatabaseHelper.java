package com.nearalias.musicplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicPlayerDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "nearaliasMusicPlayer.db";
	private static final int DATABASE_VERSION = 1;

	public MusicPlayerDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		SongTable.onCreate(db);
		PlaylistTable.onCreate(db);
		SongInPlaylistTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < newVersion) {
			SongTable.onUpgrade(db, oldVersion, newVersion);
			PlaylistTable.onUpgrade(db, oldVersion, newVersion);
			SongInPlaylistTable.onUpgrade(db, oldVersion, newVersion);
		}
	}

}
