package com.nearalias.musicplayer.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SongTable {

	public static final String SONG_TABLE_NAME = "song";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_ARTIST = "artist";
	public static final String COLUMN_ALBUM = "album";
	public static final String COLUMN_GENRE = "genre";
	public static final String COLUMN_LYRICS = "lyrics";

	// Database creation SQL statement
	private static final String CREATE_SONG_TABLE = "CREATE TABLE IF NOT EXISTS" + SONG_TABLE_NAME + "(" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT NOT NULL, " + COLUMN_ARTIST + " TEXT NOT NULL," + COLUMN_ALBUM
			+ " TEXT NOT NULL," + COLUMN_GENRE + " TEXT NOT NULL," + COLUMN_LYRICS + " TEXT NOT NULL" + ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_SONG_TABLE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(SongTable.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + SONG_TABLE_NAME);
		onCreate(database);
	}

}
