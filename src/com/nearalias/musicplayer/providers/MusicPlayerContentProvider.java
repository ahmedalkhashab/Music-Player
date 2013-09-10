package com.nearalias.musicplayer.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
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

	private static final String AUTHORITY = "com.nearalias.musicplayer.providers";

	private static final class Paths {
		private static final String SONG_PATH = "song_uri";
		private static final String PLAYLIST_PATH = "playlist_uri";
		private static final String SONG_IN_PLAYLIST_PATH = "song_in_playlist_uri";
	}

	public static final Uri SONG_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + Paths.SONG_PATH);
	public static final Uri PLAYLIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + Paths.PLAYLIST_PATH);
	public static final Uri SONG_IN_PLAYLIST_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + Paths.SONG_IN_PLAYLIST_PATH);
	public static final String SONG_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.nearalias.musicplayer.song";
	public static final String SONG_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.nearalias.musicplayer.song";
	public static final String PLAYLIST_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.nearalias.musicplayer.playlist";
	public static final String PLAYLIST_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.nearalias.musicplayer.playlist";
	public static final String SONG_IN_PLAYLIST_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/vnd.nearalias.musicplayer.song_in_playlist";
	public static final String SONG_IN_PLAYLIST_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/vnd.nearalias.musicplayer.song_in_playlist";

	private static final class UriMatches {
		private static final int SONG = 0;
		private static final int SONG_ID = 1;
		private static final int PLAYLIST = 2;
		private static final int PLAYLIST_ID = 3;
		private static final int SONG_IN_PLAYLIST = 4;
		private static final int SONG_IN_PLAYLIST_ID = 5;
	}

	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sUriMatcher.addURI(AUTHORITY, Paths.SONG_PATH, UriMatches.SONG);
		sUriMatcher.addURI(AUTHORITY, Paths.SONG_PATH + "/#", UriMatches.SONG_ID);
		sUriMatcher.addURI(AUTHORITY, Paths.PLAYLIST_PATH, UriMatches.PLAYLIST);
		sUriMatcher.addURI(AUTHORITY, Paths.PLAYLIST_PATH + "/#", UriMatches.PLAYLIST_ID);
		sUriMatcher.addURI(AUTHORITY, Paths.SONG_IN_PLAYLIST_PATH, UriMatches.SONG_IN_PLAYLIST);
		sUriMatcher.addURI(AUTHORITY, Paths.SONG_IN_PLAYLIST_PATH + "/#", UriMatches.SONG_IN_PLAYLIST_ID);
	}

	@Override
	public boolean onCreate() {
		databaseHelper = new MusicPlayerDatabaseHelper(getContext());
		return true;
	}

	@Override
	public String getType(final Uri uri) {
		switch (sUriMatcher.match(uri)) {

		case UriMatches.SONG:
			return SONG_CONTENT_TYPE;
		case UriMatches.SONG_ID:
			return SONG_CONTENT_ITEM_TYPE;

		case UriMatches.PLAYLIST:
			return PLAYLIST_CONTENT_TYPE;
		case UriMatches.PLAYLIST_ID:
			return PLAYLIST_CONTENT_ITEM_TYPE;

		case UriMatches.SONG_IN_PLAYLIST:
			return SONG_IN_PLAYLIST_CONTENT_TYPE;
		case UriMatches.SONG_IN_PLAYLIST_ID:
			return SONG_IN_PLAYLIST_CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri resultUri = null;
		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		long id = 0;
		switch (sUriMatcher.match(uri)) {
		case UriMatches.SONG:
			id = database.insert(SongTable.SONG_TABLE_NAME, null, values);
			if (id > 0) {
				resultUri = ContentUris.withAppendedId(SONG_CONTENT_URI, id);
			}
			break;
		case UriMatches.PLAYLIST:
			id = database.insert(PlaylistTable.PLAYLIST_TABLE_NAME, null, values);
			if (id > 0) {
				resultUri = ContentUris.withAppendedId(PLAYLIST_CONTENT_URI, id);
			}
			break;
		case UriMatches.SONG_IN_PLAYLIST:
			id = database.insert(SongInPlaylistTable.SONG_IN_PLAYLIST_TABLE_NAME, null, values);
			if (id > 0) {
				resultUri = ContentUris.withAppendedId(SONG_IN_PLAYLIST_CONTENT_URI, id);
			}
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		if (id > 0) {
			getContext().getContentResolver().notifyChange(resultUri, null);
			return resultUri;
		}
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// verifyColumnsAreValid(projection);

		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		case UriMatches.SONG:
			break;
		case UriMatches.SONG_ID:
			queryBuilder.setTables(SongTable.SONG_TABLE_NAME);
			queryBuilder.appendWhere(SongTable.COLUMN_ID + "=" + uri.getLastPathSegment());
			break;
		case UriMatches.PLAYLIST:
			break;
		case UriMatches.PLAYLIST_ID:
			queryBuilder.setTables(PlaylistTable.PLAYLIST_TABLE_NAME);
			queryBuilder.appendWhere(PlaylistTable.COLUMN_ID + "=" + uri.getLastPathSegment());
			break;
		case UriMatches.SONG_IN_PLAYLIST:
			break;
		case UriMatches.SONG_IN_PLAYLIST_ID:
			queryBuilder.setTables(SongInPlaylistTable.SONG_IN_PLAYLIST_TABLE_NAME);
			queryBuilder.appendWhere(SongInPlaylistTable.COLUMN_ID + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		int rowsDeleted = 0;
		String id;
		switch (sUriMatcher.match(uri)) {
		case UriMatches.SONG:
			rowsDeleted = database.delete(SongTable.SONG_TABLE_NAME, selection, selectionArgs);
			break;
		case UriMatches.SONG_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = database.delete(SongTable.SONG_TABLE_NAME, SongTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = database.delete(SongTable.SONG_TABLE_NAME, SongTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		case UriMatches.PLAYLIST:
			rowsDeleted = database.delete(PlaylistTable.PLAYLIST_TABLE_NAME, selection, selectionArgs);
			break;
		case UriMatches.PLAYLIST_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = database.delete(PlaylistTable.PLAYLIST_TABLE_NAME, PlaylistTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = database.delete(PlaylistTable.PLAYLIST_TABLE_NAME, PlaylistTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		case UriMatches.SONG_IN_PLAYLIST:
			rowsDeleted = database.delete(SongInPlaylistTable.SONG_IN_PLAYLIST_TABLE_NAME, selection, selectionArgs);
			break;
		case UriMatches.SONG_IN_PLAYLIST_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = database.delete(SongInPlaylistTable.SONG_IN_PLAYLIST_TABLE_NAME, SongInPlaylistTable.COLUMN_ID + "=" + id,
						null);
			} else {
				rowsDeleted = database.delete(SongInPlaylistTable.SONG_IN_PLAYLIST_TABLE_NAME, SongInPlaylistTable.COLUMN_ID + "=" + id
						+ " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		int rowsUpdated = 0;
		String id;
		switch (sUriMatcher.match(uri)) {
		case UriMatches.SONG:
			rowsUpdated = database.update(SongTable.SONG_TABLE_NAME, values, selection, selectionArgs);
			break;
		case UriMatches.SONG_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = database.update(SongTable.SONG_TABLE_NAME, values, SongTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = database.update(SongTable.SONG_TABLE_NAME, values, SongTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		case UriMatches.PLAYLIST:
			rowsUpdated = database.update(PlaylistTable.PLAYLIST_TABLE_NAME, values, selection, selectionArgs);
			break;
		case UriMatches.PLAYLIST_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = database.update(PlaylistTable.PLAYLIST_TABLE_NAME, values, PlaylistTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = database.update(PlaylistTable.PLAYLIST_TABLE_NAME, values, PlaylistTable.COLUMN_ID + "=" + id + " and "
						+ selection, selectionArgs);
			}
			break;
		case UriMatches.SONG_IN_PLAYLIST:
			rowsUpdated = database.update(SongInPlaylistTable.SONG_IN_PLAYLIST_TABLE_NAME, values, selection, selectionArgs);
			break;
		case UriMatches.SONG_IN_PLAYLIST_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = database.update(SongInPlaylistTable.SONG_IN_PLAYLIST_TABLE_NAME, values, SongInPlaylistTable.COLUMN_ID + "="
						+ id, null);
			} else {
				rowsUpdated = database.update(SongInPlaylistTable.SONG_IN_PLAYLIST_TABLE_NAME, values, SongInPlaylistTable.COLUMN_ID + "="
						+ id + " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
