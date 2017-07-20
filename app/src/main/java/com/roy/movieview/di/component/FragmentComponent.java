package com.roy.movieview.di.component;

import android.app.Activity;

import com.roy.movieview.di.module.ApplicationModule;
import com.roy.movieview.di.module.FragmentModule;
import com.roy.movieview.di.scope.FragmentScope;
import com.roy.movieview.ui.fragment.main.AboutFragment;
import com.roy.movieview.ui.fragment.movie.MovieComeUpFragment;
import com.roy.movieview.ui.fragment.movie.MovieHotFragment;

import dagger.Component;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

@FragmentScope
@Component(dependencies = {ApplicationComponent.class}, modules = { FragmentModule.class})
public interface FragmentComponent {
    Activity provideActivity();

    void inject(MovieHotFragment fragment);

    void inject(MovieComeUpFragment fragment);

    void inject(AboutFragment fragment);
}
