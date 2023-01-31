package com.abdulrahman.tvshows.listeners;

import com.abdulrahman.tvshows.models.TvShow;

public interface WatchlistListener {
    void onTvShowClicked(TvShow tvShow);

    void removeTvShowFromWatchList(TvShow tvShow, int position);

}
