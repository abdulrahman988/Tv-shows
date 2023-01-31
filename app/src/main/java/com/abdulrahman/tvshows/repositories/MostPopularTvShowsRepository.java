package com.abdulrahman.tvshows.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abdulrahman.tvshows.network.ApiClient;
import com.abdulrahman.tvshows.network.ApiService;
import com.abdulrahman.tvshows.responses.TvShowResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTvShowsRepository {

    private ApiService apiService;

    public MostPopularTvShowsRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowResponse> getMostPopularTvShows(int page){
        MutableLiveData<TvShowResponse> data = new MutableLiveData<>();
        apiService.getMostPopularTvShows(page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
