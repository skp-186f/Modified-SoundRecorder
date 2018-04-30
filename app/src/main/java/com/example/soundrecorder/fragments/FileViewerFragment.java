package com.example.soundrecorder.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.soundrecorder.R;
import com.example.soundrecorder.adapters.FileCursorAdapter;
import com.example.soundrecorder.data.dbContract;

import java.io.File;

/**
 * Created by Daniel on 12/23/2014.
 */
public class FileViewerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ARG_POSITION = "position";
    private static final String LOG_TAG = "FileViewerFragment";

    private static final int FILE_LOADER = 0;

    private FileCursorAdapter mFileCursorAdapter;

    public static FileViewerFragment newInstance(int position) {
        FileViewerFragment f = new FileViewerFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_file_viewer, container, false);

        ListView listView = (ListView) v.findViewById(R.id.listView);

        mFileCursorAdapter = new FileCursorAdapter(getActivity().getApplicationContext(), null);

        listView.setAdapter(mFileCursorAdapter);

        getLoaderManager().initLoader(FILE_LOADER, null, this);

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                dbContract.RecordingEntry._ID,
                dbContract.RecordingEntry.COLUMN_RECORDING_NAME};


        return new CursorLoader(getActivity().getApplicationContext(),
                dbContract.RecordingEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mFileCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFileCursorAdapter.swapCursor(null);
    }
}




