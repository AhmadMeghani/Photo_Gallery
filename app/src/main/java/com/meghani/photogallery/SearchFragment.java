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
import com.meghani.photogallery.databinding.FragmentSearchBinding;
import com.meghani.photogallery.models.Image;
import com.meghani.photogallery.util.ImageComparator;
import com.meghani.photogallery.viewmodel.ImageViewModel;

import io.reactivex.rxjava3.functions.Consumer;

public class SearchFragment extends Fragment implements ImagesAdapter.onImageClickListener {

    FragmentSearchBinding binding;
    String searchQuery;
    ImageViewModel imageViewModel;
    ImagesAdapter imagesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        searchQuery = SearchFragmentArgs.fromBundle(getArguments()).getSearchQuery();
        //set recyclerview and adapter
        initRecyclerviewAndAdapter(searchQuery);
    }


    public void initRecyclerviewAndAdapter(String searchQuery) {
        binding.searchQuery.setText(searchQuery);

        // Create new ImagesAdapter object and provide
        imagesAdapter = new ImagesAdapter(new ImageComparator(), this);

        // Create ViewModel
        imageViewModel = new ImageViewModel(getActivity().getApplication(), searchQuery, "", false);

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

        // Subscribe to to paging data
        imageViewModel.imagePagingDataFlowable.subscribe(new Consumer<PagingData<Image>>() {
            @Override
            public void accept(PagingData<Image> imagePagingData) throws Throwable {
                // submit new data to recyclerview adapter
                imagesAdapter.submitData(SearchFragment.this.getLifecycle(), imagePagingData);
            }
        });

    }

    @Override
    public void onImageClick(int imageId) {
        SearchFragmentDirections.ActionSearchFragmentToImageDetailFragment action =
                SearchFragmentDirections.actionSearchFragmentToImageDetailFragment();
        action.setImageId(imageId);
        Navigation.findNavController(getActivity().findViewById(R.id.fragment)).navigate(action);

        LinearLayout headerLayout = getActivity().findViewById(R.id.headerLayout);
        headerLayout.setVisibility(View.GONE);
    }

    private void setupViews() {
        EditText search_bar = getActivity().findViewById(R.id.search_bar);
        getActivity().findViewById(R.id.headerLayout).setVisibility(View.VISIBLE);
        search_bar.clearFocus();
        search_bar.setText("");
        getActivity().findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
    }
}