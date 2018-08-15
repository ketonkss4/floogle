package com.pmd.floogle.network;


import com.pmd.floogle.models.RequestResult;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainService {

    private NetworkService networkService;

    private OkHttpClient getAuthClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Interceptor authInterceptor = chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Authorization", "Client-ID ceb06e455ea5d99")
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        };
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(authInterceptor);
        return httpClient.build();
    }

    public MainService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.API_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getAuthClient())
                .build();

        networkService = retrofit.create(NetworkService.class);
    }

    private interface NetworkService {
        @GET("services/rest/?method=flickr.photos.search&api_key=1508443e49213ff84d566777dc211f2a&per_page=25&format=json&nojsoncallback=1")
        Observable<RequestResult> getPhotosBySearchTags(@Query("tags") String searchTags, @Query("page") String page);
    }

    public Observable<RequestResult> getPhotosBySearchTags(String searchTags, String page) {
        return networkService.getPhotosBySearchTags(searchTags, page);
    }

}
