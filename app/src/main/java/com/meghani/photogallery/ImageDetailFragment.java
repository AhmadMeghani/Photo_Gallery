package com.meghani.photogallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.meghani.photogallery.adapter.UnpagedImagesAdapter;
import com.meghani.photogallery.databinding.FragmentImageDetailBinding;
import com.meghani.photogallery.models.Image;
import com.meghani.photogallery.models.ImageResponse;
import com.meghani.photogallery.util.Utils;
import com.meghani.photogallery.viewmodel.ImageViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.meghani.photogallery.util.Utils.ifImageExists;


public class ImageDetailFragment extends Fragment implements UnpagedImagesAdapter.onImageClickListener {

    FragmentImageDetailBinding binding;
    long imageId;
    ImageViewModel imageViewModel;
    UnpagedImagesAdapter unpagedImagesAdapter;
    String imageLink;
    Menu collapsedMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentImageDetailBinding.inflate(getLayoutInflater());
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.animToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.animToolbar.setNavigationIcon(R.drawable.ic_back);
        binding.animToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return binding.getRoot();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        collapsedMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageId = ImageDetailFragmentArgs.fromBundle(getArguments()).getImageId();
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        getImageDetails(imageId);


        if (ifImageExists((int) imageId, getContext()))
            binding.fabSave.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_heart_filled_bug));
        else
            binding.fabSave.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_heart_big));

        binding.fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.downloadImage(v.getContext(), imageLink, (int) imageId);
                binding.fabSave.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_heart_filled_bug));
            }
        });

        setupViews();
    }

    private void setupViews() {
        getActivity().findViewById(R.id.headerLayout).setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(Intent.ACTION_SEND,
                Uri.parse(imageLink)).setType("text/plain"));
        return super.onOptionsItemSelected(item);
    }

    private void getImageDetails(long imageId) {
        imageViewModel.getImageDetails(imageId).observe(getViewLifecycleOwner(), new Observer<ImageResponse>() {
            @Override
            public void onChanged(ImageResponse imageResponse) {
                binding.setImageDetails(imageResponse.getImageList().get(0));
                imageLink = imageResponse.getImageList().get(0).getLargeImageURL();

                //set recyclerview and adapter
                List<String> tagsList = Arrays.asList(imageResponse.getImageList().get(0).getTags().split("\\s*,\\s*"));
                String tvTags = "";
                for (String tag : tagsList) {
                    tvTags += "#" + tag + " ";
                }
                binding.tags.setText(tvTags);
//                initRecyclerviewAndAdapter(imageResponse.getImageList().get(0).getTags().substring(0,imageResponse.getImageList().get(0).getTags().indexOf(",")));
                initRecyclerviewAndAdapter(imageResponse.getImageList().get(0).getTags().replace(", ", "+"));

            }
        });
    }


    public void initRecyclerviewAndAdapter(String keywords) {
        List<Image> imageList = new ArrayList<>();
        unpagedImagesAdapter = new UnpagedImagesAdapter(imageList, this);
        imageViewModel.getSimilarImages(keywords).observe(getViewLifecycleOwner(), new Observer<ImageResponse>() {
            @Override
            public void onChanged(ImageResponse imageResponse) {
                imageList.clear();
                imageList.addAll(imageResponse.getImageList());

                // Create GridlayoutManger with span of count of 2
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

                // Finally set LayoutManger to recyclerview
                binding.scrollableview.setLayoutManager(staggeredGridLayoutManager);
                binding.scrollableview.setAdapter(unpagedImagesAdapter);
                unpagedImagesAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onImageClick(int imageDetailId) {
        if (imageId != imageDetailId) {
            ImageDetailFragmentDirections.ActionImageDetailFragmentToImageDetailFragment action =
                    ImageDetailFragmentDirections.actionImageDetailFragmentToImageDetailFragment();
            action.setImageId(imageDetailId);
            Navigation.findNavController(getActivity().findViewById(R.id.fragment)).navigate(action);


            LinearLayout headerLayout = getActivity().findViewById(R.id.headerLayout);
            headerLayout.setVisibility(View.GONE);
        }
    }
}