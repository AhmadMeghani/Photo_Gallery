package com.meghani.photogallery.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.meghani.photogallery.BuildConfig;
import com.meghani.photogallery.R;
import com.meghani.photogallery.databinding.SingleImageItemBinding;
import com.meghani.photogallery.util.Utils;

import java.io.File;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class FavoritesImagesAdapter extends RecyclerView.Adapter<FavoritesImagesAdapter.FavoritesImagesAdapterVH> {
    File[] imageList;
    int count;

    public FavoritesImagesAdapter(File[] imageList, int count) {
        this.imageList = imageList;
        this.count = count;
    }

    @NonNull
    @Override
    public FavoritesImagesAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoritesImagesAdapterVH(
                SingleImageItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,
                        false)
        );
    }

    @Override
    public void onViewRecycled(@NonNull FavoritesImagesAdapterVH holder) {
        Glide.with(holder.binding.imageViewImage.getContext()).clear(holder.binding.imageViewImage);
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesImagesAdapterVH holder, int position) {

        holder.binding.icSave.setImageDrawable(ContextCompat.getDrawable(holder.binding.getRoot().getContext(), R.drawable.ic_heart_filled));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawableColor());
        requestOptions.error(Utils.getRandomDrawableColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(holder.binding.getRoot().getContext())
                .load(imageList[position])
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.binding.prograssLoadPhoto.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.binding.prograssLoadPhoto.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.imageViewImage);
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public class FavoritesImagesAdapterVH extends RecyclerView.ViewHolder {

        private SingleImageItemBinding binding;

        public FavoritesImagesAdapterVH(@NonNull SingleImageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                Uri path = FileProvider.getUriForFile(v.getContext(), BuildConfig.APPLICATION_ID + ".fileprovider", imageList[getAbsoluteAdapterPosition()]);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "image/*");
                intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION); //must for reading data from directory

                v.getContext().startActivity(intent);
            });
        }

    }


}
