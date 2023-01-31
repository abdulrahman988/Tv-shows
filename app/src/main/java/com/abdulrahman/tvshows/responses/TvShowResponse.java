package com.abdulrahman.tvshows.responses;

import com.abdulrahman.tvshows.models.TvShow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int pages;

    @SerializedName("tv_shows")
    private List<TvShow> tv_shows;

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public List<TvShow> getTv_shows() {
        return tv_shows;
    }
}
