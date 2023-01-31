package com.abdulrahman.tvshows.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.abdulrahman.tvshows.R;
import com.abdulrahman.tvshows.adapters.EpisodesAdapter;
import com.abdulrahman.tvshows.adapters.ImageSliderAdapter;
import com.abdulrahman.tvshows.databinding.ActivityTvShowDetailsActivityBinding;
import com.abdulrahman.tvshows.databinding.LayoutEpisodeBottomSheetBinding;
import com.abdulrahman.tvshows.models.Episode;
import com.abdulrahman.tvshows.models.TvShow;
import com.abdulrahman.tvshows.utilities.Constants;
import com.abdulrahman.tvshows.viewmodels.TvShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TvShowDetailsActivity extends AppCompatActivity {

    private ActivityTvShowDetailsActivityBinding binding;
    private TvShowDetailsViewModel tvShowDetailsViewModel;
    private LayoutEpisodeBottomSheetBinding layoutEpisodeBottomSheetBinding;
    private BottomSheetDialog bottomSheetDialog;
    private TvShow tvShow;
    private Boolean isTvShowInWatchList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tv_show_details_activity);

        doInitialization();


    }

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TvShowDetailsViewModel.class);
        binding.imageBack.setOnClickListener(view -> {
            onBackPressed();
        });
        tvShow = (TvShow) getIntent().getSerializableExtra(Constants.TvShow);

        getTvShowDetails();
        checkTvShowInWatchLit();
    }

    private void getTvShowDetails(){

        binding.setIsLoading(true);
        String showId = String.valueOf(tvShow.getId());

        tvShowDetailsViewModel.getTvShowDetails(showId).observe(
                this, tvShowDetailsResponse -> {
                binding.setIsLoading(false);
                if (tvShowDetailsResponse.getTvShowDetails() != null){
                    if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null){
                        loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                    }
                    if (tvShowDetailsResponse.getTvShowDetails().getImage_path() != null){
                        binding.setImageUrl(tvShowDetailsResponse.getTvShowDetails().getImage_path());
                        binding.imageTvShow.setVisibility(View.VISIBLE);
                    }
                }
                if (tvShowDetailsResponse.getTvShowDetails().getDescription() != null){
                        binding.setDescription(
                                String.valueOf(HtmlCompat.fromHtml(tvShowDetailsResponse.getTvShowDetails().getDescription()
                                , HtmlCompat.FROM_HTML_MODE_LEGACY))
                        );
                        binding.tvShowDescription.setEllipsize(TextUtils.TruncateAt.END);
                        binding.tvShowDescription.setVisibility(View.VISIBLE);
                }

                    binding.setRating(
                            String.format(
                                    Locale.getDefault(),
                                    "%.2f",
                                    Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())
                            )
                    );
                binding.tvShowRating.setVisibility(View.VISIBLE);

                if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null){
                    binding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                }else {
                    binding.setGenre("N/A");
                }

                binding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime()+" Min");

                binding.buttonWebsite.setOnClickListener(view -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                    startActivity(intent);
                });

                binding.buttonEpisode.setOnClickListener(view -> {
                    showBottomSheetDialog(tvShowDetailsResponse.getTvShowDetails().getEpisodes());
                });
        });

        //ركز هنا على isTvShowInWatchList وطريقه عمله
                binding.watchList.setOnClickListener(view -> {
                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    if (isTvShowInWatchList){
                        isTvShowInWatchList = false;
                       compositeDisposable.add(tvShowDetailsViewModel.removeTvShowFromWatchList(tvShow)
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(() -> {
                                   binding.watchList.setImageResource(R.drawable.ic_round_remove_red_eye_24);
                                   Toast.makeText(getApplicationContext(), "removed from watchlist", Toast.LENGTH_SHORT).show();
                               })
                       );
                   }else{
                       compositeDisposable.add(tvShowDetailsViewModel.addToWatchList(tvShow)
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(() -> {
                                   binding.watchList.setImageResource(R.drawable.ic_baseline_check_24);
                                   Toast.makeText(getApplicationContext(), "added to watchlist", Toast.LENGTH_SHORT).show();
                               })
                       );
                   }
                });
                binding.watchList.setVisibility(View.VISIBLE);
    }

    private void checkTvShowInWatchLit(){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTvShowFromWatchList(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(( tvShow -> {
                    isTvShowInWatchList = true;
                    binding.watchList.setImageResource(R.drawable.ic_baseline_check_24);
                    compositeDisposable.dispose();
                })));
    }

    private void showBottomSheetDialog(List<Episode> episodes) {
        bottomSheetDialog = new BottomSheetDialog(TvShowDetailsActivity.this);
        layoutEpisodeBottomSheetBinding = DataBindingUtil.inflate(
                LayoutInflater.from(TvShowDetailsActivity.this),
                R.layout.layout_episode_bottom_sheet,null,
                false);

        bottomSheetDialog.setContentView(layoutEpisodeBottomSheetBinding.getRoot());


        layoutEpisodeBottomSheetBinding.episodesRecyclerView.setAdapter(new EpisodesAdapter(episodes));
        layoutEpisodeBottomSheetBinding.tvShowName.setText("Episodes | "+tvShow.getName());
        bottomSheetDialog.show();
        layoutEpisodeBottomSheetBinding.imageClose.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
        });




    }

    private void loadImageSlider(String[] sliderImages){
        binding.slideViewPager.setOffscreenPageLimit(1);
        binding.slideViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        binding.slideViewPager.setVisibility(View.VISIBLE);
        binding.viewFadingEdge.setVisibility(View.VISIBLE);
        loadTvShowDetails();
    }

    private void loadTvShowDetails(){
        binding.setName(tvShow.getName());
        binding.setNetworkCountry(tvShow.getNetwork()
                + " (" + tvShow.getCountry() + ")" );
        binding.setStatus(tvShow.getStatus());
        binding.setStartDate(tvShow.getStart_date());

        binding.textName.setVisibility(View.VISIBLE);
        binding.textNetwork.setVisibility(View.VISIBLE);
        binding.textStatus.setVisibility(View.VISIBLE);
        binding.textStarted.setVisibility(View.VISIBLE);
        binding.buttonWebsite.setVisibility(View.VISIBLE);
        binding.buttonEpisode.setVisibility(View.VISIBLE);
    }
}
