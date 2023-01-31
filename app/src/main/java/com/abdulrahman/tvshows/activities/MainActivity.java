package com.abdulrahman.tvshows.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.abdulrahman.tvshows.R;
import com.abdulrahman.tvshows.adapters.TvShowsAdapter;
import com.abdulrahman.tvshows.databinding.ActivityMainBinding;
import com.abdulrahman.tvshows.listeners.TvShowListener;
import com.abdulrahman.tvshows.models.TvShow;
import com.abdulrahman.tvshows.utilities.Constants;
import com.abdulrahman.tvshows.viewmodels.MostPopularTvShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TvShowListener {

    private MostPopularTvShowsViewModel viewModel;
    private ActivityMainBinding binding;
    private List<TvShow> tvShowsList = new ArrayList<>();
    private TvShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        doInitialization();
    }

    private void doInitialization() {
        binding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTvShowsViewModel.class);
        tvShowsAdapter = new TvShowsAdapter(tvShowsList, this);
        binding.tvShowRecyclerView.setAdapter(tvShowsAdapter);

        getMostPopularTvShow();

        binding.imageWatchList.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), WatchlistActivity.class));
        });

        binding.imageSearch.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        });

        binding.nextBtn.setOnClickListener(view -> {
            if(currentPage <= totalAvailablePages){
                currentPage += 1;
                tvShowsList.clear();
                binding.tvShowRecyclerView.scrollToPosition(0);
                getMostPopularTvShow();
            }
        });

        binding.previousBtn.setOnClickListener(view -> {
            if(currentPage != 1){
                currentPage -= 1;
                tvShowsList.clear();
                binding.tvShowRecyclerView.scrollToPosition(0);
                getMostPopularTvShow();
            }
        });
    }

    private void getMostPopularTvShow() {
        toggleLoading();
        viewModel.getMostPopularTvShows(currentPage).observe(this, mostPopularTvShowsResponse -> {
            binding.setIsLoading(false);
            if (mostPopularTvShowsResponse != null) {
                totalAvailablePages = mostPopularTvShowsResponse.getPages();

                binding.nextBtn.setVisibility(View.VISIBLE);
                binding.pageNumber.setVisibility(View.VISIBLE);
                binding.pageNumber.setText("page "+currentPage+"/"+totalAvailablePages);

                if (currentPage == 1){
                    binding.previousBtn.setVisibility(View.INVISIBLE);
                }else{ binding.previousBtn.setVisibility(View.VISIBLE);}

                if (mostPopularTvShowsResponse.getTv_shows() != null) {
                    tvShowsList.addAll(mostPopularTvShowsResponse.getTv_shows());
                    tvShowsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void toggleLoading() {
         if (binding.getIsLoading() != null && binding.getIsLoading()) {
                binding.setIsLoading(false);
            } else {
                binding.setIsLoading(true);
        }
    }

    @Override
    public void onTvShowClicked(TvShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TvShowDetailsActivity.class);
        intent.putExtra(Constants.TvShow ,tvShow);
        startActivity(intent);
    }
}