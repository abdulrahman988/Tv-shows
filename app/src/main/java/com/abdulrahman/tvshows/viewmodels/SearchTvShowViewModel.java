package com.abdulrahman.tvshows.viewmodels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.abdulrahman.tvshows.repositories.SearchTvShowRepository;
import com.abdulrahman.tvshows.responses.TvShowResponse;

public class SearchTvShowViewModel extends ViewModel {
    SearchTvShowRepository searchTvShowRepository;

    public SearchTvShowViewModel() {
        searchTvShowRepository = new SearchTvShowRepository();
    }

    public LiveData<TvShowResponse> searchTvShow(String query, int page){
        return searchTvShowRepository.searchTvShow(query, page);
    }
}
