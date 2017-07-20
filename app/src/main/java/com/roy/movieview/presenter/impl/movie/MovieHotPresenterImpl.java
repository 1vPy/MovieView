package com.roy.movieview.presenter.impl.movie;

import com.roy.movieview.http.douban.DoubanApi;
import com.roy.movieview.presenter.BasePresenterImpl;
import com.roy.movieview.ui.contract.movie.MovieHotContract;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

public class MovieHotPresenterImpl extends BasePresenterImpl<MovieHotContract.View> implements MovieHotContract.Presenter {
    private DoubanApi mDoubanApi;

    @Inject
    public MovieHotPresenterImpl(DoubanApi doubanApi) {
        this.mDoubanApi = doubanApi;
    }

    @Override
    public void getMovieHot(int start, int count, String city) {
        addSubscribe(mDoubanApi.getHotMovie(start, count, city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                            if (isAttached())
                                getView().movieHot(bean);
                        },
                        throwable -> {
                            if (isAttached())
                                getView().showError(throwable.getLocalizedMessage());
                        })
        );
    }
}
