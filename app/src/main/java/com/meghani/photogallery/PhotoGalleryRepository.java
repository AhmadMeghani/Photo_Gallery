package com.meghani.photogallery;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.meghani.photogallery.api.APIClient;
import com.meghani.photogallery.models.ImageResponse;
import com.meghani.photogallery.util.SingleLiveEvent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoGalleryRepository {
    private final SingleLiveEvent<ImageResponse> imageResponseMutableLiveData = new SingleLiveEvent<>();

    private final SingleLiveEvent<ImageResponse> similarImagesLiveData = new SingleLiveEvent<>();

    public SingleLiveEvent<ImageResponse> getImageDetails(long imageId) {

        APIClient.APIInterface apiInterface = APIClient.getAPIInterface();
        Call<ImageResponse> call = apiInterface.getImageById(imageId);

        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if(response.isSuccessful())
                    imageResponseMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {

            }
        });

        return imageResponseMutableLiveData;
    }

    public LiveData<ImageResponse> getSimilarImages(String keyword) {
        APIClient.APIInterface apiInterface = APIClient.getAPIInterface();
        Call<ImageResponse> call = apiInterface.getSimilarImages(keyword, 1);

        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if(response.isSuccessful())
                    similarImagesLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {

            }
        });

        return similarImagesLiveData;
    }
}
