package com.meghani.photogallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.meghani.photogallery.adapter.ImagesAdapter;
import com.meghani.photogallery.adapter.ImagesLoadStateAdapter;
import com.meghani.photogallery.databinding.FragmentImageListingBinding;
import com.meghani.photogallery.models.Image;
import com.meghani.photogallery.util.ImageComparator;
import com.meghani.photogallery.viewmodel.ImageViewModel;

import io.reactivex.rxjava3.functions.Consumer;


public class ImageListingFragment extends Fragment implements ImagesAdapter.onImageClickListener {
    FragmentImageListingBinding binding;
    ImageViewModel imageViewModel;
    ImagesAdapter imagesAdapter;
    private static final String ARG_CATEGORY = "param1";
    private String category;

    public ImageListingFragment() {
        // Required empty public constructor
    }

    public static ImageListingFragment newInstance(String category) {
        ImageListingFragment fragment = new ImageListingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentImageListingBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create new ImagesAdapter object and provide
        imagesAdapter = new ImagesAdapter(new ImageComparator(), this);
        // Create ViewModel
        boolean editorChoice = false;
        if (category.contains("editor")) {
            category = "";
            editorChoice = true;
        }
        imageViewModel = new ImageViewModel(getActivity().getApplication(), "", category, editorChoice);

        //set recyclerview and adapter
        initRecyclerviewAndAdapter();

        // Subscribe to to paging data
        imageViewModel.imagePagingDataFlowable.subscribe(new Consumer<PagingData<Image>>() {
            @Override
            public void accept(PagingData<Image> imagePagingData) throws Throwable {
                // submit new data to recyclerview adapter
                imagesAdapter.submitData(ImageListingFragment.this.getLifecycle(), imagePagingData);
            }
        });

    }



    private void initRecyclerviewAndAdapter() {
        // Create GridlayoutManger with span of count of 2
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        // Finally set LayoutManger to recyclerview
        binding.recyclerViewImages.setLayoutManager(staggeredGridLayoutManager);

        // set adapter
        binding.recyclerViewImages.setAdapter(
                // This will show end user a progress bar while pages are being requested from server
                imagesAdapter.withLoadStateFooter(
                        // When we will scroll down and next page request will be sent
                        // while we get response form server Progress bar will show to end user
                        new ImagesLoadStateAdapter(v -> {
                            imagesAdapter.retry();
                        }))
        );

    }

    @Override
    public void onImageClick(int imageId) {
        MainFragmentDirections.ActionMainFragmentToImageDetailFragment action =
                MainFragmentDirections.actionMainFragmentToImageDetailFragment();
        action.setImageId(imageId);
        Navigation.findNavController(getActivity().findViewById(R.id.fragment)).navigate(action);


        getActivity().findViewById(R.id.profilePicture).setVisibility(View.GONE);
        LinearLayout headerLayout = getActivity().findViewById(R.id.headerLayout);
        headerLayout.setVisibility(View.GONE);
    }
}

