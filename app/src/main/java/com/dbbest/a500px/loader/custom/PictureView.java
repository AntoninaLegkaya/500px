package com.dbbest.a500px.loader.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import java.net.URL;

public class PictureView extends AppCompatImageView {

    private URL pictureUrl;
    private PictureTask pictureLoadTask;
    private boolean isCache;
    // Status flag that indicates if onDraw has completed
    private boolean isDrawn;
    @DrawableRes
    private int placeHolder;

    public PictureView(@NonNull Context context) {
        super(context);
    }

    public PictureView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PictureView(@NonNull Context context, @Nullable AttributeSet attrs,
                       @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {

        // Clears out the image drawable, turns off the cache, disconnects the view from a URL
        setPictureUrl(null, -1, false);

        // Gets the current Drawable, or null if no Drawable is attached
        Drawable localDrawable = getDrawable();

        // if the Drawable is null, unbind it from this VIew
        if (localDrawable != null) {
            localDrawable.setCallback(null);
        }
        // Sets the downloader thread to null
        this.pictureLoadTask = null;

        // Always call the super method last
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if ((!isDrawn) && (pictureUrl != null)) {
            pictureLoadTask = PictureManager.startDownload(this, isCache);
            isDrawn = true;
        }

        super.onDraw(canvas);
    }


    public URL getPictureViewUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(URL url, @DrawableRes int holder, boolean cache) {
        placeHolder = holder;
        if (pictureUrl != null && url != null) {
            if (pictureUrl.toString().equals(url.toString())) {
                return;
            } else {
                PictureManager.removeLoader(pictureLoadTask, pictureUrl);
            }
        }
        pictureUrl = url;
        if ((isDrawn) && (url != null)) {
            isCache = cache;
            pictureLoadTask = PictureManager.startDownload(this, cache);
        }
    }

    @Override
    public void setImageBitmap(Bitmap paramBitmap) {
        super.setImageBitmap(paramBitmap);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

}
