package com.abdulrahman.tvshows.adapters;

import android.database.DatabaseUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulrahman.tvshows.R;
import com.abdulrahman.tvshows.databinding.ItemContainerSlideImageBinding;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder> {

    private String[] sliderImage;
    private LayoutInflater layoutInflater;

    public ImageSliderAdapter(String[] sliderImage) {
        this.sliderImage = sliderImage;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerSlideImageBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_container_slide_image,
                parent,false);
        return new ImageSliderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        holder.bindSliderImage(sliderImage[position]);
    }

    @Override
    public int getItemCount() {
        return sliderImage.length;
    }


    static class ImageSliderViewHolder extends RecyclerView.ViewHolder{
        private ItemContainerSlideImageBinding binding;
        public ImageSliderViewHolder(@NonNull ItemContainerSlideImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bindSliderImage(String imageUrl){
            binding.setImageUrl(imageUrl);
        }
    }
}
