package com.dbbest.a500px.db.repository;

import com.dbbest.a500px.db.model.AvatarsModel;
import com.dbbest.a500px.db.model.PhotoModel;
import com.dbbest.a500px.db.model.UserModel;

public final class RepositoryProvider {

    public Repository<PhotoModel> photo() {
        return new PhotoRepository();
    }

    public Repository<UserModel> user() {
        return new UserRepository();
    }
    public Repository<AvatarsModel> avatars() {
        return new AvatarsRepository();
    }
}
