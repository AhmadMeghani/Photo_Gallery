package com.meghani.photogallery.paging;

import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.meghani.photogallery.api.APIClient;
import com.meghani.photogallery.models.Image;
import com.meghani.photogallery.models.ImageResponse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ImagePagingSource extends RxPagingSource<Integer, Image> {
    private final String category;
    private final String query;
    private final boolean editorChoice;

    public ImagePagingSource(String query, String category, boolean editorChoice) {
        this.category = category;
        this.editorChoice = editorChoice;
        this.query = query;
    }

    @NotNull
    @Override
    public Single<LoadResult<Integer, Image>> loadSingle(@NotNull LoadParams<Integer> loadParams) {
        try {
            // If page number is already there then init page variable with it otherwise we are loading fist page
            int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
            // Send request to server with page number
            return APIClient.getAPIInterface()
                    .getImagesByPage(page, query, category, editorChoice)
                    // Subscribe the result
                    .subscribeOn(Schedulers.io())
                    // Map result top List of Images
                    .map(ImageResponse::getImageList)
                    // Map result to LoadResult Object
                    .map(imageList -> toLoadResult(imageList, page))
                    // when error is there return error
                    .onErrorReturn(LoadResult.Error::new);
        } catch (Exception e) {
            // Request ran into error return error
            return Single.just(new LoadResult.Error(e));
        }
    }

    // Method to map Images to LoadResult object
    private LoadResult<Integer, Image> toLoadResult(List<Image> Images, int page) {
        return new LoadResult.Page(Images, page == 1 ? null : page - 1, page + 1);
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NotNull PagingState<Integer, Image> pagingState) {
        return null;
    }
}
