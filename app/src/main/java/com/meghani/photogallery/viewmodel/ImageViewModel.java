package com.meghani.photogallery.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.meghani.photogallery.PhotoGalleryRepository;
import com.meghani.photogallery.models.Image;
import com.meghani.photogallery.models.ImageResponse;
import com.meghani.photogallery.paging.ImagePagingSource;
import com.meghani.photogallery.util.SingleLiveEvent;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class ImageViewModel extends AndroidViewModel {

    public PhotoGalleryRepository photoGalleryRepository;
    public Application application;

    public ImageViewModel(@NonNull Application application) {
        super(application);
        photoGalleryRepository = new PhotoGalleryRepository();
    }

    public SingleLiveEvent<ImageResponse> getImageDetails(long imageId) {
        return photoGalleryRepository.getImageDetails(imageId);
    }

    public LiveData<ImageResponse> getSimilarImages(String keyword) {
        return photoGalleryRepository.getSimilarImages(keyword);
    }


    // Define Flowable for Images
    public Flowable<PagingData<Image>> imagePagingDataFlowable;
    public ImageViewModel(@NonNull Application application, String query, String category, boolean editorChoice) {
        this(application);
        init(query, category, editorChoice);
    }
    // Init ViewModel Data
    public void init(String query, String category, boolean editorChoice) {
        // Define Paging Source
        ImagePagingSource imagePagingSource = new ImagePagingSource(query, category, editorChoice);

        // Create new Pager
        Pager<Integer, Image> pager = new Pager(
                // Create new paging config
                new PagingConfig(10, //  Count of items in one page
                        10, //  Number of items to prefetch
                        true, // Enable placeholders for data which is not yet loaded
                        10, // initialLoadSize - Count of items to be loaded initially
                        10 * 499),// maxSize - Count of total items to be shown in recyclerview
                () -> imagePagingSource); // set paging source

        // inti Flowable
        imagePagingDataFlowable = PagingRx.getFlowable(pager);
        CoroutineScope coroutineScope = ViewModelKt.getViewModelScope(this);
        PagingRx.cachedIn(imagePagingDataFlowable, coroutineScope);

    }


}
