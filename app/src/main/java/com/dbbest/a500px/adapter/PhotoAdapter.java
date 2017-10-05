package com.dbbest.a500px.adapter;

import android.app.Activity;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dbbest.a500px.R;
import com.dbbest.a500px.db.model.PhotoModel;
import com.dbbest.a500px.ui.CropSquareTransformation;

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

    public PhotoAdapter(PreviewCallback previewCallback) {
        this.previewCallback = previewCallback;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
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
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) holder.photoView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int deviceWidth = displaymetrics.widthPixels / 3;
            holder.photoView.getLayoutParams().width = deviceWidth;
            holder.photoView.getLayoutParams().height = deviceWidth;
            final PhotoModel photo = new PhotoModel(cursor);
            holder.bind(photo);
            holder.photoView.setOnClickListener(new View.OnClickListener() {
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
        if (oldCursor != null && dataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(dataSetObserver);
        }
        cursor = newCursor;
        if (newCursor == null) {
            rowIDColumn = -1;
            dataValid = false;
            notifyDataSetChanged();

        } else {
            newCursor.registerDataSetObserver(dataSetObserver);
            rowIDColumn = newCursor.getColumnIndexOrThrow("_id");
            dataValid = true;
            notifyDataSetChanged();
        }
        return oldCursor;
    }


    @SuppressFBWarnings(value = "SIC_INNER_SHOULD_BE_STATIC_ANON")
    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        View cv;
        ImageView photoView;

        @SuppressFBWarnings(value = "URF_UNREAD_FIELD")
        PhotoViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card_view);
            photoView = (ImageView) itemView.findViewById(R.id.image_photo);

        }

        void bind(final PhotoModel photo) {
            onPhotoSet(photo.getPreviewUrl(), photoView);

        }

        void onPhotoSet(String fullPreviewUrl, ImageView previewView) {

            Glide.with(previewView.getContext())
                    .load(fullPreviewUrl)
                    .bitmapTransform(new CropSquareTransformation(previewView.getContext()))
                    .placeholder(R.drawable.ic_empty)
                    .fitCenter()
                    .into(previewView);
        }

    }

  public  interface PreviewCallback {

        void photoSelected(String name, String photoUrl);

    }
}

