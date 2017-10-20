package com.dbbest.a500px.imageProvider;

import android.support.annotation.StringRes;
import android.view.View;

public final class Loader {

    private String url;
    private
    @StringRes
    int placeholder;
    private View view;

    public Loader(Builder builder) {
        this.url = builder.url;
        this.placeholder = builder.placeholder;
        this.view = builder.view;
    }

    public String getUrl() {
        return url;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public View getView() {
        return view;
    }


    public static class Builder {

        private String url;
        private
        @StringRes
        int placeholder;
        private View view;

        public Builder(String urlImage) {
            this.url = urlImage;
        }

        public Builder addPlaceholder(@StringRes int holder) {
            this.placeholder = holder;
            return this;
        }

        public Builder addView(View imageView) {
            this.view = imageView;
            return this;
        }

        public Loader build() {
            return new Loader(this);
        }

    }
}
