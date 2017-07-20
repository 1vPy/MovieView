package com.roy.movieview.presenter.impl.movie;

import com.roy.movieview.http.douban.DoubanApi;
import com.roy.movieview.presenter.BasePresenterImpl;
import com.roy.movieview.ui.contract.movie.MovieComeUpContract;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 1vPy(Roy) on 2017/6/21.
 */

public class MovieComeUpPresenterImpl extends BasePresenterImpl<MovieComeUpContract.View> implements MovieComeUpContract.Presenter {

    private DoubanApi mDoubanApi;

    @Inject
    public MovieComeUpPresenterImpl(DoubanApi doubanApi) {
        mDoubanApi = doubanApi;
    }

    @Override
    public void getMovieComeUp(int start, int count) {
        addSubscribe(mDoubanApi.getComingMovie(start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (isAttached())
                        getView().movieComeUp(bean);
                }, throwable -> {
                    if (isAttached()) {
                        getView().showError(throwable.getLocalizedMessage());
                    }
                })
        );
    }
}
