package com.dbbest.a500px.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dbbest.a500px.R;
import com.dbbest.a500px.data.PhotoEntry;
import com.dbbest.a500px.loader.LoaderType;
import com.dbbest.a500px.loader.PictureLoaderManager;
import com.dbbest.a500px.loader.custom.PictureView;
import com.dbbest.a500px.model.PhotoModel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressWarnings("PMD.AccessorMethodGeneration")
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {


    private static final String SETTINGS = "settings";
    private final PreviewCallback previewCallback;
    private final SharedPreferences preferences;
    //PMD check
    boolean dataValid;
    private final DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            dataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            dataValid = false;
            notifyDataSetChanged();
        }
    };
    private Cursor cursor;
    private int rowIDColumn;

    public PhotoAdapter(PreviewCallback callback) {
        this.previewCallback = callback;
        preferences = ((Activity) callback).getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {

        if (!dataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (cursor != null) {
            if (!cursor.moveToPosition(position)) {
                throw new IllegalStateException("couldn't move cursor to position " + position);
            }
            final PhotoModel photo = new PhotoModel(cursor);
            holder.bind(photo);
            holder.previewView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previewCallback.photoSelected(photo.getName(), photo.getPhotoUrl());
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        if (dataValid && cursor != null && cursor.moveToPosition(position)) {
            return cursor.getLong(rowIDColumn);
        }
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemCount() {
        if (dataValid && cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor) {
            return null;
        }
        Cursor oldCursor = cursor;
        if (oldCursor != null) {
            oldCursor.unregisterDataSetObserver(dataSetObserver);
        }
        cursor = newCursor;
        if (newCursor == null) {
            rowIDColumn = -1;
            dataValid = false;
            notifyDataSetChanged();

        } else {
            String typeLoader = (preferences.getString("name", LoaderType.GLIDE
                    .geType()));
            PictureLoaderManager.getInstance((Activity) previewCallback).setLoaderType(typeLoader);
            newCursor.registerDataSetObserver(dataSetObserver);
            rowIDColumn = newCursor.getColumnIndexOrThrow(PhotoEntry._ID);
            dataValid = true;
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    public interface PreviewCallback {
        void photoSelected(String name, String photoUrl);
    }

    @SuppressFBWarnings(value = "SIC_INNER_SHOULD_BE_STATIC_ANON")
    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        PictureView previewView;

        @SuppressFBWarnings(value = "URF_UNREAD_FIELD")
        PhotoViewHolder(View view) {
            super(view);
            previewView = view.findViewById(R.id.image_photo);
        }

        void bind(final PhotoModel photo) {
            PictureLoaderManager.getInstance(previewView.getContext())
                    .createPictureLoader(previewView, R.drawable.ic_empty, photo.getPreviewUrl()).loadBitmap();
        }
    }
}

