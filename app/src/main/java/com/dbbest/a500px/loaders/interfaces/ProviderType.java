package com.dbbest.a500px.loaders.interfaces;

public enum ProviderType {

    GLIDE("glide"),
    PICASSO("picasso");

    private String name;

    ProviderType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
