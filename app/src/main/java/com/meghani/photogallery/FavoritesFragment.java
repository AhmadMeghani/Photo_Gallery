package com.meghani.photogallery;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.meghani.photogallery.adapter.FavoritesImagesAdapter;
import com.meghani.photogallery.databinding.FragmentFavoritesBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoritesFragment extends Fragment {
    FragmentFavoritesBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showImageList();
        setupViews();

    }

    private void setupViews() {
        getActivity().findViewById(R.id.headerLayout).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.profilePicture).setVisibility(View.GONE);
        EditText search_bar = getActivity().findViewById(R.id.search_bar);
        search_bar.setVisibility(View.GONE);
        getActivity().findViewById(R.id.backBtn).setVisibility(View.VISIBLE);

    }

    private void showImageList() {
        File file = new
                File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                + "/PhotoGallery/");
        if (file.exists()) {
            File[] imagesList = file.listFiles();
            List<String> images = new ArrayList<>();
            Collections.addAll(images, file.list());

            binding.totalCount.setText(images.size() + " Photos");

            FavoritesImagesAdapter favoritesImagesAdapter = new FavoritesImagesAdapter(imagesList, images.size());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            binding.recyclerViewImages.setLayoutManager(linearLayoutManager);
            binding.recyclerViewImages.setAdapter(favoritesImagesAdapter);

        }
    }
}