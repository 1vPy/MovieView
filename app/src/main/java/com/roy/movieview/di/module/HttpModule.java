package com.roy.movieview.di.module;

import android.content.Context;

import com.roy.movieview.http.bmob.BmobApi;
import com.roy.movieview.http.douban.DoubanApi;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

@Module
public class HttpModule {

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    DoubanApi provideDoubanApi(Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, DoubanApi.API_BASE_URL).create(DoubanApi.class);
    }

    @Singleton
    @Provides
    BmobApi provideBmobApi(Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, BmobApi.API_BASE_URL).create(BmobApi.class);
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpClientBuilder() {
        return new OkHttpClient.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(OkHttpClient.Builder builder, Context context) {
        int cacheSize = 10 * 1024 * 1024;
        File dir = new File(context.getCacheDir(), "MvCache");
        Cache cache = new Cache(dir, cacheSize);
        return builder
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    private Retrofit createRetrofit(Retrofit.Builder builder, OkHttpClient client, String url) {
        return builder
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }


}
