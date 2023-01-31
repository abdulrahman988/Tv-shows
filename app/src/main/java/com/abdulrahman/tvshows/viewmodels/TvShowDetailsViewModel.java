package com.abdulrahman.tvshows.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.abdulrahman.tvshows.database.TvShowDatabase;
import com.abdulrahman.tvshows.models.TvShow;
import com.abdulrahman.tvshows.repositories.TvShowDetailsRepository;
import com.abdulrahman.tvshows.responses.TvShowDetailsResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TvShowDetailsViewModel extends AndroidViewModel {
    private TvShowDetailsRepository tvShowDetailsRepository;
    private TvShowDatabase tvShowDatabase;

    public TvShowDetailsViewModel(Application application) {
        super(application);
        tvShowDetailsRepository = new TvShowDetailsRepository();
        tvShowDatabase = TvShowDatabase.getTvShowDatabase(application);
    }
    public LiveData<TvShowDetailsResponse> getTvShowDetails(String tvShowId){
        return tvShowDetailsRepository.getTvShowsDetails(tvShowId);
    }
    public Completable addToWatchList(TvShow tvShow){
        return tvShowDatabase.tvShowDao().addToWatchList(tvShow);

    }

    public Flowable<TvShow> getTvShowFromWatchList(String tvShowId){
        return tvShowDatabase.tvShowDao().getTvShowFromWatchList(tvShowId);
    }

    public Completable removeTvShowFromWatchList(TvShow tvShow){
        return tvShowDatabase.tvShowDao().removeFromWatchList(tvShow);
    }

}
