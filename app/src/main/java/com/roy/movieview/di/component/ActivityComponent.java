package com.roy.movieview.di.component;

import android.app.Activity;

import com.roy.movieview.di.module.ActivityModule;
import com.roy.movieview.di.scope.ActivityScope;
import com.roy.movieview.ui.activity.MainActivity;
import com.roy.movieview.ui.activity.SplashActivity;
import com.roy.movieview.ui.activity.movie.MovieDetailActivity;
import com.roy.movieview.ui.activity.user.LoginRegisterActivity;
import com.roy.movieview.ui.activity.user.UserCenterActivity;

import dagger.Component;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {ActivityModule.class})
public interface ActivityComponent {

    Activity provideActivity();

    void inject(MainActivity activity);

    void inject(MovieDetailActivity activity);

    void inject(LoginRegisterActivity activity);

    void inject(UserCenterActivity activity);

    void inject(SplashActivity activity);

}
