package com.roy.movieview.di.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

@Module
public class FragmentModule {
    private Fragment mFragment;

    public FragmentModule(Fragment fragment){
        mFragment = fragment;
    }

    @Provides
    public Activity provideActivity(){
        return mFragment.getActivity();
    }
}
