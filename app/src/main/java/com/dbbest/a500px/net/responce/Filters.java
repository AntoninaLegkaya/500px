
package com.dbbest.a500px.net.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Filters {

    @SerializedName("category")
    @Expose
    private Boolean category;
    @SerializedName("exclude")
    @Expose
    private Boolean exclude;

    public Boolean getCategory() {
        return category;
    }

    public void setCategory(Boolean category) {
        this.category = category;
    }

    public Boolean getExclude() {
        return exclude;
    }

    public void setExclude(Boolean exclude) {
        this.exclude = exclude;
    }



}
