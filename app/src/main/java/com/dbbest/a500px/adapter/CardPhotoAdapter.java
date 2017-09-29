package com.dbbest.a500px.adapter;

import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dbbest.a500px.App;
import com.dbbest.a500px.R;
import com.dbbest.a500px.db.ProviderDefinition;
import com.dbbest.a500px.db.model.AvatarsModel;
import com.dbbest.a500px.db.model.CardModel;
import com.dbbest.a500px.db.model.PhotoModel;
import com.dbbest.a500px.db.model.UserModel;
import com.dbbest.a500px.db.repository.AvatarsColumns;
import com.dbbest.a500px.db.repository.UserColumns;

import timber.log.Timber;

public class CardPhotoAdapter extends BaseRecycleViewCursorAdapter<CardPhotoAdapter.PhotoViewHolder> {


    public CardPhotoAdapter(Cursor cursor) {
        super(cursor);
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
            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    Timber.i("Cursor count = 0");
                } else {
                    Timber.i("Cursor has data");
                    return parseCursor(cursor);
                }
            } else {
                Timber.i("Cursor has no data");
            }
        } catch (Exception e) {
            Timber.e(e, "query error");
        }
        return null;

    }

    private CardModel parseCursor(Cursor cursor) {
        if (cursor != null) {
            PhotoModel model = new PhotoModel(cursor);
            CardModel cardModel = new CardModel();
            Timber.i("Data from Cursor: user_id: %d ", model.getUserId());
            Cursor userCursor = App.instance().getContentResolver().query(ProviderDefinition.UserEntry.URI, null, UserColumns.ID + "=?",
                    new String[]{String.valueOf(model.getUserId())}, UserColumns.ID);
            userCursor.moveToFirst();
            UserModel userModel = new UserModel(userCursor);
            Timber.i("Data from Cursor: user_name: %s ", userModel.getName());
            cardModel.setNameUser(userModel.getName());


            cardModel.setImageUrl(model.getImageUrl());

            Cursor avatarCursor = App.instance().getContentResolver().query(ProviderDefinition.AvatarsEntry.URI, null, AvatarsColumns.ID + "=?",
                    new String[]{String.valueOf(model.getUserId())}, AvatarsColumns.ID);
            avatarCursor.moveToFirst();
            AvatarsModel avatarsModel = new AvatarsModel(avatarCursor);
            cardModel.setAvatars(avatarsModel);
            return cardModel;
        } else {
            Timber.i("Cursor Empty");
            return null;
        }


    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        ImageView avatar;
        ImageView photo;

        PhotoViewHolder(View itemView) {
            super(itemView);
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

        void onAvatarSet(String fullPreviewUrl, ImageView previewView) {

            Glide.with(previewView.getContext())
                    .load(fullPreviewUrl)
                    .placeholder(R.drawable.ic_user_places_holder)
                    .into(previewView);
        }

        void onPhotoSet(String fullPreviewUrl, ImageView previewView) {

            Glide.with(previewView.getContext())
                    .load(fullPreviewUrl)
                    .placeholder(R.drawable.ic_empty)
                    .into(previewView);
        }

    }
}
