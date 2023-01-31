package com.abdulrahman.tvshows.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulrahman.tvshows.R;
import com.abdulrahman.tvshows.databinding.ItemContainerTvShowBinding;
import com.abdulrahman.tvshows.listeners.TvShowListener;
import com.abdulrahman.tvshows.models.TvShow;

import java.util.List;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowViewHolder> {

    private List<TvShow> tvShows;
    private LayoutInflater layoutInflater;
    private TvShowListener tvShowListener;

    //Constructor
    public TvShowsAdapter(List<TvShow> tvShows, TvShowListener tvShowListener) {
        this.tvShows = tvShows;
        this.tvShowListener = tvShowListener;
    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_container_tv_show,
                parent, false);
        return new TvShowViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder holder, int position) {
            holder.bindTvShow(tvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    //ViewHolder
    public class TvShowViewHolder extends RecyclerView.ViewHolder{

        private ItemContainerTvShowBinding binding;

        public TvShowViewHolder(ItemContainerTvShowBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTvShow(TvShow tvShow){
            binding.setTvShow(tvShow);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvShowListener.onTvShowClicked(tvShow);
                }
            });
        }

    }

}
