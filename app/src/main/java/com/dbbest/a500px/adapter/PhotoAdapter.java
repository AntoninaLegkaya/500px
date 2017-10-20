package com.dbbest.a500px.adapter;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dbbest.a500px.R;
import com.dbbest.a500px.data.PhotoEntry;
import com.dbbest.a500px.loaders.DataLoadProvider;
import com.dbbest.a500px.loaders.ProviderManager;
import com.dbbest.a500px.model.PhotoModel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private final PreviewCallback previewCallback;
    private Cursor cursor;
    private boolean dataValid;

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
    private int rowIDColumn;

    public PhotoAdapter(PreviewCallback callback) {
        this.previewCallback = callback;
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
        ImageView previewView;

        @SuppressFBWarnings(value = "URF_UNREAD_FIELD")
        PhotoViewHolder(View view) {
            super(view);
            previewView = (ImageView) view.findViewById(R.id.image_photo);
        }

        void bind(final PhotoModel photo) {
            DataLoadProvider dataLoadProvider = new DataLoadProvider.Builder(photo.getPreviewUrl())
                    .addPlaceholder(R.drawable.ic_empty)
                    .addView(previewView)
                    .build();
            ProviderManager manager = new ProviderManager();
            manager.makeGlideProvider(dataLoadProvider).loadImage();

        }
    }
}

