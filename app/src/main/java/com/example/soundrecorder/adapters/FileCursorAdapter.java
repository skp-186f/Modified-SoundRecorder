package com.example.soundrecorder.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.soundrecorder.R;
import com.example.soundrecorder.data.dbContract.RecordingEntry;

public class FileCursorAdapter extends CursorAdapter {


    public FileCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int nameClmIdx = cursor.getColumnIndex(RecordingEntry.COLUMN_RECORDING_NAME);

        String fileName = cursor.getString(nameClmIdx);

        TextView nameTxtView = (TextView) view.findViewById(R.id.file_name_text);

        nameTxtView.setText(fileName);
    }
}
