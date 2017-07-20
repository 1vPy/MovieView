package com.roy.movieview.ui.contract.movie;

import com.roy.movieview.bean.movie.MovieBean;
import com.roy.movieview.ui.contract.BaseContract;

/**
 * Created by 1vPy(Roy) on 2017/6/21.
 */

public interface MovieComeUpContract {
    interface View extends BaseContract.View{
        void movieComeUp(MovieBean bean);
    }

    interface Presenter{
        void getMovieComeUp(int start,int count);
    }
}
