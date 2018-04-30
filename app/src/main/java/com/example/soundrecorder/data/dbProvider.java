package com.example.soundrecorder.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.example.soundrecorder.data.dbContract.RecordingEntry;

public class dbProvider extends FileProvider{

    public static final String LOG_TAG = dbProvider.class.getSimpleName();

    private static final int RECORDINGS = 100;

    private static final int RECORDING_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(dbContract.CONTENT_AUTHORITY, dbContract.PATH_RECORDINGS, RECORDINGS);

        sUriMatcher.addURI(dbContract.CONTENT_AUTHORITY, dbContract.PATH_RECORDINGS + "/#", RECORDING_ID);
    }

    private dbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new dbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        final int match = sUriMatcher.match(uri);
        switch(match) {
            case RECORDINGS:
                cursor = database.query(RecordingEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case RECORDING_ID:
                selection = RecordingEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(RecordingEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case RECORDINGS:
                return insertRecording(uri, contentValues);
            default:
                throw new IllegalArgumentException("Cannot insert at " + uri);
        }
    }

    private Uri insertRecording(Uri uri, ContentValues values) {

        String name = values.getAsString(RecordingEntry.COLUMN_RECORDING_NAME);

        if (name == null) {
            throw new IllegalArgumentException("Recording must have a name");
        }

        String filepath = values.getAsString(RecordingEntry.COLUMN_RECORDING_FILE_PATH);

        if (filepath == null) {
            throw new IllegalArgumentException("Recording must have a filepath");
        }

        Integer length = values.getAsInteger(RecordingEntry.COLUMN_RECORDING_LENGTH);

        if (length == null || length < 0) {
            throw new IllegalArgumentException("Recording must have a length");
        }

        Integer timeAdded = values.getAsInteger(RecordingEntry.COLUMN_TIME_ADDED);

        if (timeAdded == null || length < 0) {
            throw new IllegalArgumentException("Recording must have a time added value");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(RecordingEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Could not insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {

        int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDINGS:
                return updateRecording(uri, contentValues, selection, selectionArgs);

            case RECORDING_ID:
                selection = RecordingEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateRecording(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateRecording(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(RecordingEntry.COLUMN_RECORDING_NAME)) {
            String name = values.getAsString(RecordingEntry.COLUMN_RECORDING_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Recording must have a name");
            }
        }

        if (values.containsKey(RecordingEntry.COLUMN_RECORDING_FILE_PATH)) {
            String path = values.getAsString(RecordingEntry.COLUMN_RECORDING_FILE_PATH);
            if (path == null) {
                throw new IllegalArgumentException("Recording must have a filepath");
            }
        }

        if (values.containsKey(RecordingEntry.COLUMN_TIME_ADDED)) {
            Integer timeAdded = values.getAsInteger(RecordingEntry.COLUMN_TIME_ADDED);
            if (timeAdded == null || timeAdded < 0) {
                throw new IllegalArgumentException("Recording must have a time added value");
            }
        }

        if (values.containsKey(RecordingEntry.COLUMN_TIME_ADDED)) {
            Integer length = values.getAsInteger(RecordingEntry.COLUMN_RECORDING_LENGTH);
            if (length == null || length < 0) {
                throw new IllegalArgumentException("Recording must have a length");
            }
        }

        // If there are no values to update, then don't update the database
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(RecordingEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDINGS:
                rowsDeleted = database.delete(RecordingEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case RECORDING_ID:
                selection = RecordingEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(RecordingEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot delete at this " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDINGS:
                return RecordingEntry.CONTENT_LIST_TYPE;
            case RECORDING_ID:
                return RecordingEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Invalid URI " + uri + " with match " + match);
        }
    }
}
