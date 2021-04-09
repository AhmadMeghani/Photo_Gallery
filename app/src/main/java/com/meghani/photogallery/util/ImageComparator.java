package com.meghani.photogallery.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.meghani.photogallery.models.Image;

/*
    Comparator for comparing Image object to avoid duplicates
 */
public class ImageComparator extends DiffUtil.ItemCallback<Image> {
    @Override
    public boolean areItemsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
        return oldItem.getId() == newItem.getId();
    }
}
