package com.meghani.photogallery.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
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

import org.jetbrains.annotations.NotNull;

import static com.meghani.photogallery.util.Utils.ifImageExists;

public class ImagesAdapter extends PagingDataAdapter<Image, ImagesAdapter.ImageViewHolder> {
    // Define Loading ViewType
    public static final int LOADING_ITEM = 0;
    // Define Image ViewType
    public static final int Image_ITEM = 1;
    private onImageClickListener onImageClickListener;

    public ImagesAdapter(@NotNull DiffUtil.ItemCallback<Image> diffCallback, onImageClickListener onImageClickListener) {
        super(diffCallback);
        this.onImageClickListener = onImageClickListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Return ImageViewHolder
        return new ImageViewHolder(SingleImageItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // Get current Image
        Image currentImage = getItem(position);
        // Check for null
        if (currentImage != null && currentImage.getWebformatURL() != null && !currentImage.getWebformatURL().trim().equals("")) {
            // Set Image using glide Library
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(Utils.getRandomDrawableColor());
            requestOptions.error(Utils.getRandomDrawableColor());
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(holder.imageItemBinding.getRoot().getContext())
                    .load(currentImage.getWebformatURL())
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.imageItemBinding.prograssLoadPhoto.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.imageItemBinding.prograssLoadPhoto.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imageItemBinding.imageViewImage);

            if (ifImageExists(getItem(position).getId(), holder.imageItemBinding.getRoot().getContext()))
                holder.imageItemBinding.icSave.setImageDrawable(ContextCompat.getDrawable(holder.imageItemBinding.getRoot().getContext(), R.drawable.ic_heart_filled));
            else
                holder.imageItemBinding.icSave.setImageDrawable(ContextCompat.getDrawable(holder.imageItemBinding.getRoot().getContext(), R.drawable.ic_heart));
        }
    }

    @Override
    public int getItemViewType(int position) {
        // set ViewType
        return position == getItemCount() ? Image_ITEM : LOADING_ITEM;
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        // Define image_item layout view binding
        SingleImageItemBinding imageItemBinding;

        public ImageViewHolder(@NonNull SingleImageItemBinding imageItemBinding) {
            super(imageItemBinding.getRoot());
            // init binding
            this.imageItemBinding = imageItemBinding;

            imageItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageClickListener.onImageClick(getItem(getAbsoluteAdapterPosition()).getId());
                }
            });

            imageItemBinding.icSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.downloadImage(v.getContext(), getItem(getAbsoluteAdapterPosition()).getLargeImageURL(), getItem(getAbsoluteAdapterPosition()).getId());
                    imageItemBinding.icSave.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_heart_filled));
                }
            });
        }
    }

    public interface onImageClickListener {
        void onImageClick(int imageId);
    }

}
