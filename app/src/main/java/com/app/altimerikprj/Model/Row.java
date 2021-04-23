package com.app.altimerikprj.Model;

/**
 * Created by Anil on 1/30/2018.
 */
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Row implements Serializable
{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("imageHref")
    @Expose
    private Object imageHref;
    private final static long serialVersionUID = 5017935060823000673L;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getImageHref() {
        return imageHref;
    }

    public void setImageHref(Object imageHref) {
        this.imageHref = imageHref;
    }

}