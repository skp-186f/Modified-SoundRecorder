package com.example.soundrecorder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.soundrecorder.data.dbContract.RecordingEntry;

public class dbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = dbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "saved_recordings.db";

    private static final int DATABASE_VERSION = 1;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String TEXT_TYPE = " TEXT";
        String COMMA_SEP = ",";

        String SQL_CREATE_RECORDINGS_TABLE =
                "CREATE TABLE " + RecordingEntry.TABLE_NAME + " (" +
                        RecordingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                        RecordingEntry.COLUMN_RECORDING_NAME + TEXT_TYPE + " NOT NULL " + COMMA_SEP +
                        RecordingEntry.COLUMN_RECORDING_FILE_PATH + TEXT_TYPE + " NOT NULL " + COMMA_SEP +
                        RecordingEntry.COLUMN_RECORDING_LENGTH + " INTEGER NOT NULL " + COMMA_SEP +
                        RecordingEntry.COLUMN_TIME_ADDED + " INTEGER NOT NULL" + ")";

        db.execSQL(SQL_CREATE_RECORDINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
