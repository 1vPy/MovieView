package com.roy.movieview.ui.contract.movie;

import com.roy.movieview.bean.movie.MStarDetail;
import com.roy.movieview.bean.movie.MovieDetail;
import com.roy.movieview.bean.user.UserInfo;
import com.roy.movieview.ui.contract.BaseContract;

/**
 * Created by 1vPy(Roy) on 2017/6/21.
 */

public interface MovieDetailContract {
    interface View extends BaseContract.View {
        void movieDetail(MovieDetail movieDetail);

        void mStarDetail(MStarDetail mStarDetail);

        void praiseSuccess();
    }

    interface Presenter {
        void getMovieDetail(String id);

        void addPraise(String movieId,String username);

        void getMStarDetail(String id);

        boolean isLogin();

        UserInfo getUserInfo();
    }
}
