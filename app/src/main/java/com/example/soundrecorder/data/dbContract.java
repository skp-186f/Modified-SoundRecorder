package com.example.soundrecorder.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class dbContract {

    private dbContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.soundrecorder";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RECORDINGS = "recordings";

    public static final class RecordingEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RECORDINGS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECORDINGS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECORDINGS;

        public static final String TABLE_NAME = "saved_recordings";

        public final static String _ID = BaseColumns._ID;

        public static final String COLUMN_RECORDING_NAME = "recording_name";
        public static final String COLUMN_RECORDING_FILE_PATH = "file_path";
        public static final String COLUMN_RECORDING_LENGTH = "length";
        public static final String COLUMN_TIME_ADDED = "time_added";
    }
}
