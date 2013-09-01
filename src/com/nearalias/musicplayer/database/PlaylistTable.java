package com.nearalias.musicplayer.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PlaylistTable {

	public static final String PLAYLIST_TABLE_NAME = "playlist";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";

	// Database creation SQL statement
	private static final String CREATE_PLAYLIST_TABLE = "CREATE TABLE IF NOT EXISTS" + PLAYLIST_TABLE_NAME + "(" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT NOT NULL" + ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_PLAYLIST_TABLE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(PlaylistTable.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + PLAYLIST_TABLE_NAME);
		onCreate(database);
	}

}
