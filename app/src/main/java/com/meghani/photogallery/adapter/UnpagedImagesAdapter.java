package com.meghani.photogallery.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.meghani.photogallery.R;
import com.meghani.photogallery.databinding.SingleImageItemBinding;
import com.meghani.photogallery.models.Image;
import com.meghani.photogallery.util.Utils;

import java.util.List;

import static com.meghani.photogallery.util.Utils.ifImageExists;

public class UnpagedImagesAdapter extends RecyclerView.Adapter<UnpagedImagesAdapter.UnpagedImagesAdapterVH> {
    List<Image> imageList;
    public onImageClickListener onImageClickListener;

    public UnpagedImagesAdapter(List<Image> imageList, UnpagedImagesAdapter.onImageClickListener onImageClickListener) {
        this.imageList = imageList;
        this.onImageClickListener = onImageClickListener;
    }

    @NonNull
    @Override
    public UnpagedImagesAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UnpagedImagesAdapterVH(
                SingleImageItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,
                        false)
        );
    }

    @Override
    public void onViewRecycled(@NonNull UnpagedImagesAdapterVH holder) {
        Glide.with(holder.binding.imageViewImage.getContext()).clear(holder.binding.imageViewImage);
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull UnpagedImagesAdapterVH holder, int position) {
        String imageURL = imageList.get(position).getWebformatURL();

        if (imageURL != null && !imageURL.trim().equals("")) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(Utils.getRandomDrawableColor());
            requestOptions.error(Utils.getRandomDrawableColor());
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(holder.binding.getRoot().getContext())
                    .load(imageURL)
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

            if (ifImageExists(imageList.get(position).getId(), holder.binding.getRoot().getContext()))
                holder.binding.icSave.setImageDrawable(ContextCompat.getDrawable(holder.binding.getRoot().getContext(), R.drawable.ic_heart_filled));
            else
                holder.binding.icSave.setImageDrawable(ContextCompat.getDrawable(holder.binding.getRoot().getContext(), R.drawable.ic_heart));

        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class UnpagedImagesAdapterVH extends RecyclerView.ViewHolder {

        private SingleImageItemBinding binding;

        public UnpagedImagesAdapterVH(@NonNull SingleImageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageClickListener.onImageClick(imageList.get(getAbsoluteAdapterPosition()).getId());
                }
            });

            binding.icSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.downloadImage(v.getContext(), imageList.get(getAbsoluteAdapterPosition()).getLargeImageURL(), imageList.get(getAbsoluteAdapterPosition()).getId());
                    binding.icSave.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_heart_filled));
                }
            });
        }

    }

    public interface onImageClickListener {
        void onImageClick(int imageId);

    }


}
