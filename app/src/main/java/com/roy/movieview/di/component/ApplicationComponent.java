package com.roy.movieview.di.component;

import android.content.Context;

import com.roy.movieview.di.module.ApplicationModule;
import com.roy.movieview.di.module.HttpModule;
import com.roy.movieview.http.bmob.BmobApi;
import com.roy.movieview.http.douban.DoubanApi;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

@Singleton
@Component(modules = {ApplicationModule.class, HttpModule.class})
public interface ApplicationComponent {
    Context provideContext();

    DoubanApi provideDoubanApi();

    BmobApi provideBmobApi();
}
