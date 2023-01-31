package com.abdulrahman.tvshows.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.abdulrahman.tvshows.R;
import com.abdulrahman.tvshows.adapters.TvShowsAdapter;
import com.abdulrahman.tvshows.databinding.ActivitySearchBinding;
import com.abdulrahman.tvshows.listeners.TvShowListener;
import com.abdulrahman.tvshows.models.TvShow;
import com.abdulrahman.tvshows.responses.TvShowResponse;
import com.abdulrahman.tvshows.utilities.Constants;
import com.abdulrahman.tvshows.viewmodels.MostPopularTvShowsViewModel;
import com.abdulrahman.tvshows.viewmodels.SearchTvShowViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements TvShowListener {

    private ActivitySearchBinding binding;
    private SearchTvShowViewModel viewModel;
    private List<TvShow> tvShowsList= new ArrayList<>();
    private TvShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        doInitialization();

    }

    private void doInitialization() {

        binding.imageBack.setOnClickListener(view -> onBackPressed());
        binding.imageSearch.setOnClickListener(view -> {
            tvShowsList.clear();
            binding.tvShowRecyclerView.scrollToPosition(0);
            currentPage = 1;
            searchTvShow(binding.searchEt.getText().toString().trim());
        });

        binding.nextBtn.setOnClickListener(view -> {
            if(currentPage <= totalAvailablePages){
                currentPage += 1;
                tvShowsList.clear();
                binding.tvShowRecyclerView.scrollToPosition(0);
                searchTvShow(binding.searchEt.getText().toString().trim());

            }
        });

        binding.previousBtn.setOnClickListener(view -> {
            if(currentPage != 1){
                currentPage -= 1;
                tvShowsList.clear();
                binding.tvShowRecyclerView.scrollToPosition(0);
                searchTvShow(binding.searchEt.getText().toString().trim());
            }
        });

        binding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(SearchTvShowViewModel.class);
        tvShowsAdapter = new TvShowsAdapter(tvShowsList, this);
        binding.tvShowRecyclerView.setAdapter(tvShowsAdapter);

        if (binding.searchEt.toString() == null){
            tvShowsList.clear();
            tvShowsAdapter.notifyDataSetChanged();
        }
    }


    private void searchTvShow(String query){
        viewModel.searchTvShow(query, currentPage).observe(this, tvShowResponse -> {
            if(tvShowResponse != null){
                totalAvailablePages = tvShowResponse.getPages();

                binding.pageNumber.setText("page "+currentPage+" / "+totalAvailablePages);

                if (tvShowResponse.getTv_shows() != null){
                    tvShowsList.addAll(tvShowResponse.getTv_shows());
                    tvShowsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onTvShowClicked(TvShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TvShowDetailsActivity.class);
        intent.putExtra(Constants.TvShow ,tvShow);
        startActivity(intent);

    }
}