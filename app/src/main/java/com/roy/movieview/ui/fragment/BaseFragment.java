package com.roy.movieview.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import solid.ren.skinlibrary.base.SkinBaseFragment;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

public abstract class BaseFragment extends SkinBaseFragment {
    protected Unbinder unbinder;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);//ButterKnife绑定
        initViewAndEvent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife解绑
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    protected abstract void initViewAndEvent();
}
