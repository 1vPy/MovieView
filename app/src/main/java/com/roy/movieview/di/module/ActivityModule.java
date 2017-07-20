package com.roy.movieview.di.module;

import android.app.Activity;

import com.roy.movieview.di.scope.ActivityScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

@Module
public class ActivityModule {
    public Activity mActivity;

    public ActivityModule(Activity activity){
        this.mActivity = activity;
    }

    @ActivityScope
    @Provides
    Activity provideActivity(){
        return mActivity;
    }

}
