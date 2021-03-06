package com.meghani.photogallery.api;

import com.meghani.photogallery.models.ImageResponse;

import io.reactivex.rxjava3.core.Single;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.meghani.photogallery.util.Utils.API_KEY;
import static com.meghani.photogallery.util.Utils.BASE_URL;

public class APIClient {
    // Define APIInterface
    static APIInterface apiInterface;

    // create retrofit instance
    public static APIInterface getAPIInterface() {
        if (apiInterface == null) {
            // Create OkHttp Client
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            // Add interceptor to add API key as query string parameter to each request
            client.addInterceptor(chain -> {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                HttpUrl url = originalHttpUrl.newBuilder()
                        // Add API Key as query string parameter
                        .addQueryParameter("key", API_KEY)
                        .build();
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            });
            // Create retrofit instance
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client.build())
                    // Add Gson converter
                    .addConverterFactory(GsonConverterFactory.create())
                    // Add RxJava sport for Retrofit
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
            // Init APIInterface
            apiInterface = retrofit.create(APIInterface.class);
        }
        return apiInterface;
    }

    //API service interface
    public interface APIInterface {
        // Define Get request with query string parameter as page number
        @GET("api/")
        Single<ImageResponse> getImagesByPage(@Query("page") int page, @Query("q") String query, @Query("category") String category, @Query("editors_choice") boolean editorChoice);

        @GET("api/")
        Call<ImageResponse> getImageById(@Query("id") long id);

        @GET("api/")
        Call<ImageResponse> getSimilarImages(@Query("q") String keyword, @Query("page") int page);

    }
}
