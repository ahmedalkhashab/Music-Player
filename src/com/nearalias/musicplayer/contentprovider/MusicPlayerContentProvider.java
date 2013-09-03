package com.nearalias.musicplayer.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.nearalias.musicplayer.database.MusicPlayerDatabaseHelper;
import com.nearalias.musicplayer.database.PlaylistTable;
import com.nearalias.musicplayer.database.SongInPlaylistTable;
import com.nearalias.musicplayer.database.SongTable;

public class MusicPlayerContentProvider extends ContentProvider {

	private MusicPlayerDatabaseHelper databaseHelper;

	// UriMatchers
	private static final int MUSICPLAYER = 10;
	private static final int MUSICPLAYER_ID = 20;

	private static final String AUTHORITY = "com.nearalias.musicplayer.contentprovider";
	private static final String BASE_PATH = "musicplayer";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BASE_PATH;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH;

	private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		matcher.addURI(AUTHORITY, BASE_PATH, MUSICPLAYER);
		matcher.addURI(AUTHORITY, BASE_PATH + "/#", MUSICPLAYER_ID);
	}

	@Override
	public boolean onCreate() {
		databaseHelper = new MusicPlayerDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		verifyColumnsAreValid(projection);

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(SongTable.SONG_TABLE_NAME);

		int uriType = matcher.match(uri);

		switch (uriType) {
		case MUSICPLAYER:
			break;
		case MUSICPLAYER_ID:
			queryBuilder.appendWhere(SongTable.COLUMN_ID + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	private void verifyColumnsAreValid(String[] projection) {
		String[] validColumnsArray = { SongTable.COLUMN_ALBUM, SongTable.COLUMN_ARTIST, SongTable.COLUMN_GENRE, SongTable.COLUMN_ID,
				SongTable.COLUMN_LYRICS, SongTable.COLUMN_NAME, PlaylistTable.COLUMN_ID, PlaylistTable.COLUMN_NAME,
				SongInPlaylistTable.COLUMN_ID, SongInPlaylistTable.COLUMN_PLAYLIST_ID, SongInPlaylistTable.COLUMN_SONG_ID };

		if (projection != null) {
			HashSet<String> queriedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> validColumns = new HashSet<String>(Arrays.asList(validColumnsArray));
			if (!validColumns.containsAll(queriedColumns)) {
				throw new IllegalArgumentException("Invalid column in projection");
			}
		}
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}

}
