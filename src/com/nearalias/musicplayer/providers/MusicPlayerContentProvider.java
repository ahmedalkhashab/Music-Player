package com.nearalias.musicplayer.providers;

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
import android.text.TextUtils;

import com.nearalias.musicplayer.database.MusicPlayerDatabaseHelper;
import com.nearalias.musicplayer.database.PlaylistTable;
import com.nearalias.musicplayer.database.SongInPlaylistTable;
import com.nearalias.musicplayer.database.SongTable;

public class MusicPlayerContentProvider extends ContentProvider {

	private MusicPlayerDatabaseHelper databaseHelper;

	// UriMatchers
	private static final int MUSICPLAYER = 10;
	private static final int MUSICPLAYER_ID = 20;

	private static final String AUTHORITY = "com.nearalias.musicplayer.providers";
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
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = matcher.match(uri);
		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case MUSICPLAYER:
			rowsDeleted = database.delete(SongTable.SONG_TABLE_NAME, selection, selectionArgs);
			break;
		case MUSICPLAYER_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = database.delete(SongTable.SONG_TABLE_NAME, SongTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = database.delete(SongTable.SONG_TABLE_NAME, SongTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = matcher.match(uri);
		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		long id = 0;
		switch (uriType) {
		case MUSICPLAYER:
			id = database.insert(SongTable.SONG_TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int uriType = matcher.match(uri);
		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case MUSICPLAYER:
			rowsUpdated = database.update(SongTable.SONG_TABLE_NAME, values, selection, selectionArgs);
			break;
		case MUSICPLAYER_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = database.update(SongTable.SONG_TABLE_NAME, values, SongTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = database.update(SongTable.SONG_TABLE_NAME, values, SongTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
