package com.dbbest.a500px.loaders;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;

public final class DataLoadProvider {

    private final String url;
    private final
    @DrawableRes
    int placeholder;
    private final View view;

     DataLoadProvider(Builder builder) {
        this.url = builder.url;
        this.placeholder = builder.placeholder;
        this.view = builder.view;
    }

    String getUrl() {
        return url;
    }

    int getPlaceholder() {
        return placeholder;
    }

    View getView() {
        return view;
    }


    public static class Builder {

        private final String url;
        private
        @StringRes
        int placeholder;
        private View view;

        public Builder(String urlImage) {
            this.url = urlImage;
        }

        public Builder addPlaceholder(@DrawableRes  int holder) {
            this.placeholder = holder;
            return this;
        }

        public Builder addView(View imageView) {
            this.view = imageView;
            return this;
        }

        public DataLoadProvider build() {
            return new DataLoadProvider(this);
        }

    }
}
