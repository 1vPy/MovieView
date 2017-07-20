package com.roy.movieview.di.module;

import android.content.Context;

import com.roy.movieview.db.DBHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/7/14.
 */

@Module
public class DBModule {
    @Singleton
    @Provides
    DBHelper provideDBHelper(Context context){
       return new DBHelper(context);
    }
}
