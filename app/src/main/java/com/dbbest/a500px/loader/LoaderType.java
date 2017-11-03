package com.dbbest.a500px.loader;

public enum LoaderType {

    GLIDE("glide"),
    PICASSO("picasso"),
    LOADER("loader");
    private String type;

      LoaderType(String t) {
        this.type = t;
    }

    public String geType() {
        return type;
    }

    public static LoaderType fromString(String value) {
        if (value.equals(GLIDE.type)) {
            return GLIDE;
        } else if (value.equals(PICASSO.type)) {
            return PICASSO;
        } else if (value.equals(LOADER.type)) {
            return LOADER;
        }

        throw new IllegalArgumentException("No Such Loader Type: " + value);
    }

}
