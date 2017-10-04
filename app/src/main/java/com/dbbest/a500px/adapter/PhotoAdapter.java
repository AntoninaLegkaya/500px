package com.dbbest.a500px.adapter;

import android.app.Activity;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.dbbest.a500px.App;
import com.dbbest.a500px.R;
import com.dbbest.a500px.db.model.PhotoModel;
import com.dbbest.a500px.ui.CropSquareTransformation;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

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

    public PhotoAdapter() {
        //Nothing here
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
            //if you need three fix imageview in width
            int devicewidth = displaymetrics.widthPixels / 3;

            int deviceheight = devicewidth;

            holder.photoView.getLayoutParams().width = devicewidth;

            //if you need same height as width you can set devicewidth in holder.image_view.getLayoutParams().height
            holder.photoView.getLayoutParams().height = deviceheight;

            holder.bind(new PhotoModel(cursor));
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

        void bind(PhotoModel photo) {
            onPhotoSet(photo.getImageUrl(), photoView);

        }

        void onAvatarSet(String fullPreviewUrl, final ImageView previewView) {

            Glide.with(previewView.getContext()).load(fullPreviewUrl).asBitmap().centerCrop().placeholder(R.drawable.ic_user_places_holder)
                    .into(new BitmapImageViewTarget(previewView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(App.instance().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            previewView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
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
}

