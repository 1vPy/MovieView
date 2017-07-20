package com.roy.movieview.ui.contract;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

public interface BaseContract {
    interface View {
        void showError(String msg);
    }

    interface Presenter<T> {
        void attachView(T view);

        boolean isAttached();

        T getView();

        void detachView();
    }
}
