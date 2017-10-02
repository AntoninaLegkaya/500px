
package com.dbbest.a500px.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Avatars {

    @SerializedName("default")
    @Expose
    private Default def;
    @SerializedName("large")
    @Expose
    private Large large;
    @SerializedName("small")
    @Expose
    private Small small;
    @SerializedName("tiny")
    @Expose
    private Tiny tiny;

    public Default getDefault() {
        return def;
    }

    public void setDefault(Default def) {
        this.def = def;
    }

    public Large getLarge() {
        return large;
    }

    public void setLarge(Large large) {
        this.large = large;
    }

    public Small getSmall() {
        return small;
    }

    public void setSmall(Small small) {
        this.small = small;
    }

    public Tiny getTiny() {
        return tiny;
    }

    public void setTiny(Tiny tiny) {
        this.tiny = tiny;
    }


}
