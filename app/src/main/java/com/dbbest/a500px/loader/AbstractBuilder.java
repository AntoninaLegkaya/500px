package com.dbbest.a500px.loader;

import android.support.annotation.DrawableRes;

abstract class AbstractBuilder {

    abstract AbstractBuilder build();

    abstract AbstractBuilder urlLoad(String url);

    abstract AbstractBuilder placeholder(@DrawableRes int placeholder);

//    abstract AbstractBuilder requestBuilder();
//
//    abstract AbstractBuilder holder();
//
//    abstract AbstractBuilder into();
//
//    abstract AbstractBuilder applyCropCenter();



}
