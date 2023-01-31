package com.abdulrahman.tvshows.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulrahman.tvshows.R;
import com.abdulrahman.tvshows.databinding.ItemConteinerEpisodeBinding;
import com.abdulrahman.tvshows.models.Episode;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodesViewHolder>{

    private List<Episode> episodeList;
    private LayoutInflater layoutInflater;

    public EpisodesAdapter(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }

    @NonNull
    @Override
    public EpisodesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemConteinerEpisodeBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_conteiner_episode, parent,false );
        return new EpisodesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesViewHolder holder, int position) {
        holder.bindEpisodes(episodeList.get(position));
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    static class EpisodesViewHolder extends RecyclerView.ViewHolder {
        ItemConteinerEpisodeBinding binding;
        public EpisodesViewHolder(@NonNull ItemConteinerEpisodeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindEpisodes(Episode episode){
            String season, episodeNumber, title;
            if (episode.getSeason().length() == 1){
                 season = "S0"+episode.getSeason();
            }else {
                season = "S"+episode.getSeason();
            }

            if (episode.getEpisode().length() == 1){
                episodeNumber = "E0"+episode.getEpisode();
            }else {
                episodeNumber = "E"+episode.getEpisode();
            }
            title = season+ "|" + episodeNumber;
            binding.setTitle(title);
            binding.setName(episode.getName());
            binding.setAirDate(episode.getAir_date());
        }
    }
}
