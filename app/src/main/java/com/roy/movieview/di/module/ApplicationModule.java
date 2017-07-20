package com.roy.movieview.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

@Module
public class ApplicationModule {
    private Application mApplication;

    public ApplicationModule(Application application){
        this.mApplication = application;
    }

    @Singleton
    @Provides
    public Context provideApplicationContext(){
        return mApplication.getApplicationContext();
    }
}
