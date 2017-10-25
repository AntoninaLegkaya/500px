package com.dbbest.a500px.loader;

public class LoaderImp implements Loader {

    private final LoaderBuilder builder;


    LoaderImp(LoaderBuilder loaderBuilder) {
        this.builder = loaderBuilder;
    }

    @Override
    public void loadImage() {
        builder.build();
    }

}
