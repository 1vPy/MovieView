package com.roy.movieview.ui.custom;

import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;

import solid.ren.skinlibrary.attr.base.SkinAttr;
import solid.ren.skinlibrary.utils.SkinResourcesUtils;

/**
 * Created by Administrator on 2017/7/19.
 */

public class CollapsingToolbarLayoutAttr extends SkinAttr{
    @Override
    public void apply(View view) {
        if (view instanceof CollapsingToolbarLayout) {
            CollapsingToolbarLayout tl = (CollapsingToolbarLayout) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinResourcesUtils.getColor(attrValueRefId);
                tl.setContentScrimColor(color);
                tl.setStatusBarScrimColor(color);
            }
        }
    }
}
