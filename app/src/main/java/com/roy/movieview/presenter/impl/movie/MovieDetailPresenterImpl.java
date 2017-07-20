package com.roy.movieview.presenter.impl.movie;

import com.roy.movieview.bean.movie.MovieDetail;
import com.roy.movieview.bean.user.ErrorBean;
import com.roy.movieview.bean.user.UserInfo;
import com.roy.movieview.bean.user.movie.comment.CommentMovie;
import com.roy.movieview.bean.user.movie.comment.CommentResult;
import com.roy.movieview.bean.user.movie.praise.PraiseMovie;
import com.roy.movieview.bean.user.movie.praise.PraiseResult;
import com.roy.movieview.http.bmob.BmobApi;
import com.roy.movieview.http.douban.DoubanApi;
import com.roy.movieview.presenter.BasePresenterImpl;
import com.roy.movieview.ui.contract.movie.MovieDetailContract;
import com.roy.movieview.utils.common.LogUtils;
import com.roy.movieview.utils.json.JsonUtils;
import com.roy.movieview.utils.user.UserPreference;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by 1vPy(Roy) on 2017/6/21.
 */

public class MovieDetailPresenterImpl extends BasePresenterImpl<MovieDetailContract.View> implements MovieDetailContract.Presenter {

    private static final String TAG = MovieDetailPresenterImpl.class.getSimpleName();

    private DoubanApi mDoubanApi;

    private BmobApi mBombApi;

    private UserPreference mUserPreference;

    @Inject
    public MovieDetailPresenterImpl(DoubanApi doubanApi, BmobApi bmobApi, UserPreference userPreference) {
        mDoubanApi = doubanApi;
        mBombApi = bmobApi;
        mUserPreference = userPreference;
    }

    @Override
    public void getMovieDetail(String id) {

        Observable<MovieDetail> o1 = mDoubanApi.getMovieDetail(id);


        PraiseResult praiseNum = new PraiseResult();
        praiseNum.setMovieId(id);
        String requestNum = JsonUtils.JavaBean2Json(praiseNum);
        Observable<Response<ResponseBody>> o2 = mBombApi.praiseQuery(requestNum);


        CommentResult result = new CommentResult();
        result.setMovieId(id);
        String commentJson = JsonUtils.JavaBean2Json(result);
        Observable<Response<ResponseBody>> o3 = mBombApi.commentQuery(commentJson);


        if (isLogin()) {
            PraiseResult isPraise = new PraiseResult();
            isPraise.setMovieId(id);
            isPraise.setUsername(mUserPreference.readUserInfo().getUsername());
            String requestIs = JsonUtils.JavaBean2Json(isPraise);
            Observable<Response<ResponseBody>> o4 = mBombApi.praiseQuery(requestIs);

            addSubscribe(Observable.zip(o1, o2, o3, o4, (movieDetail, responseBodyResponse, responseBodyResponse2, responseBodyResponse3) -> {
                        movieDetail.setPraiseNum(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), PraiseMovie.class).getResults().size());
                        movieDetail.setCommentNum(JsonUtils.Json2JavaBean(responseBodyResponse2.body().string(), CommentMovie.class).getResults().size());
                        movieDetail.setPraise(JsonUtils.Json2JavaBean(responseBodyResponse3.body().string(), PraiseMovie.class).getResults().size() > 0);
                        return movieDetail;
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(movieDetail -> {
                                if (isAttached())
                                    getView().movieDetail(movieDetail);
                            }, throwable -> {
                                if (isAttached()) {
                                    getView().showError(throwable.getLocalizedMessage());
                                }
                            })
            );
        } else {
            addSubscribe(Observable.zip(o1, o2, o3, (movieDetail, responseBodyResponse, responseBodyResponse2) -> {

                        movieDetail.setPraiseNum(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), PraiseMovie.class).getResults().size());
                        movieDetail.setPraise(false);
                        movieDetail.setCommentNum(JsonUtils.Json2JavaBean(responseBodyResponse2.body().string(), CommentMovie.class).getResults().size());
                        return movieDetail;
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(movieDetail -> {
                                if (isAttached())
                                    getView().movieDetail(movieDetail);
                            }, throwable -> {
                                if (isAttached()) {
                                    getView().showError(throwable.getLocalizedMessage());
                                }
                            })
            );
        }


    }


    @Override
    public void addPraise(String movieId, String username) {
        PraiseResult result = new PraiseResult();
        result.setMovieId(movieId);
        result.setUsername(username);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(result));
        addSubscribe(mBombApi.addPraise(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()) {
                        if (isAttached())
                            getView().praiseSuccess();

                    } else {
                        String error = "";
                        switch (responseBodyResponse.code()) {
                            case 404:
                                error = JsonUtils.Json2JavaBean(responseBodyResponse.errorBody().string(), ErrorBean.class).getError();
                                break;
                            default:
                                error = "未知错误";
                                break;
                        }

                        if (isAttached())
                            getView().showError(error);
                    }

                }, throwable -> {
                    if (isAttached())
                        getView().showError(throwable.getLocalizedMessage());
                })
        );

    }

    @Override
    public void getMStarDetail(String id) {
        addSubscribe(mDoubanApi.getStarDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mStarDetail -> {
                    if (isAttached())
                        getView().mStarDetail(mStarDetail);
                }, throwable -> {
                    if (isAttached())
                        getView().showError(throwable.getLocalizedMessage());
                })
        );
    }

    @Override
    public boolean isLogin() {
        return mUserPreference.readUserInfo() != null;
    }

    @Override
    public UserInfo getUserInfo() {
        return mUserPreference.readUserInfo();
    }
}
