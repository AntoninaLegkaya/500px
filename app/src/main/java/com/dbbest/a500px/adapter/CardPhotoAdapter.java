package com.dbbest.a500px.adapter;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.dbbest.a500px.App;
import com.dbbest.a500px.R;
import com.dbbest.a500px.db.ProviderDefinition;
import com.dbbest.a500px.db.model.AvatarsModel;
import com.dbbest.a500px.db.model.CardModel;
import com.dbbest.a500px.db.model.PhotoModel;
import com.dbbest.a500px.db.model.UserModel;
import com.dbbest.a500px.db.repository.AvatarsColumns;
import com.dbbest.a500px.db.repository.UserColumns;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import timber.log.Timber;

public class CardPhotoAdapter extends BaseRecycleViewCursorAdapter<CardPhotoAdapter.PhotoViewHolder> {


    public CardPhotoAdapter() {
        super();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(PhotoViewHolder holder, Cursor cursor) {
        CardModel cards = getPhotos(cursor);
        holder.bind(cards);
    }


    public CardModel getPhotos(Cursor cursor) {
        try {
            if (cursor != null && cursor.getCount() > 0) {
                return parseCursor(cursor);
            } else {
                Timber.i("Cursor has no data");
            }
        } catch (Exception e) {
            Timber.e(e, "query error");
        }
        return null;

    }

    private CardModel parseCursor(Cursor cursor) {
        CardModel cardModel = new CardModel();
        composeData(cursor, cardModel);

        return cardModel;
    }

    private void composeData(Cursor cursor, CardModel cardModel) {
        PhotoModel model = new PhotoModel(cursor);

        Cursor userCursor = App.instance().getContentResolver().query(ProviderDefinition.UserEntry.URI, null, UserColumns.ID + "=?",
                new String[]{String.valueOf(model.getUserId())}, UserColumns.ID);

        Cursor avatarCursor = App.instance().getContentResolver().query(ProviderDefinition.AvatarsEntry.URI, null, AvatarsColumns.ID + "=?",
                new String[]{String.valueOf(model.getUserId())}, AvatarsColumns.ID);

        if (userCursor != null && userCursor.moveToFirst()) {
            UserModel userModel = new UserModel(userCursor);
            cardModel.setNameUser(userModel.getName());
            userCursor.close();
        }
        if (avatarCursor != null && avatarCursor.moveToFirst()) {
            cardModel.setImageUrl(model.getImageUrl());
            AvatarsModel avatarsModel = new AvatarsModel(avatarCursor);
            cardModel.setAvatars(avatarsModel);
            avatarCursor.close();
        } else {
            Timber.e("Error while compose data");

        }
    }


    @SuppressFBWarnings(value = "SIC_INNER_SHOULD_BE_STATIC_ANON")
    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        ImageView avatar;
        ImageView photo;
        View parentView;

        @SuppressFBWarnings(value = "URF_UNREAD_FIELD")
        PhotoViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            cv = (CardView) itemView.findViewById(R.id.cardView);
            name = (TextView) itemView.findViewById(R.id.text_name);
            avatar = (ImageView) itemView.findViewById(R.id.image_avatar);
            photo = (ImageView) itemView.findViewById(R.id.image_photo);
        }

        void bind(CardModel card) {
            name.setText(card.getNameUser());
            onAvatarSet(card.getAvatars().getDefaultUrl(), avatar);
            onPhotoSet(card.imageUrl, photo);

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
                    .placeholder(R.drawable.ic_empty)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .fitCenter().crossFade()
                    .into(previewView);
        }

    }
}
