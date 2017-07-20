package com.roy.movieview.ui.contract.movie;

import com.roy.movieview.bean.movie.MovieBean;
import com.roy.movieview.ui.contract.BaseContract;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

public interface MovieHotContract {
    interface View extends BaseContract.View {
        void movieHot(MovieBean bean);
    }

    interface Presenter {
        void getMovieHot(int start, int count, String city);
    }
}
