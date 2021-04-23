package com.app.altimerikprj.Model;

/**
 * Created by Anil on 1/30/2018.
 */

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wipro implements Serializable
{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("rows")
    @Expose
    private List<Row> rows = null;
    private final static long serialVersionUID = -3555176745398618688L;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

}