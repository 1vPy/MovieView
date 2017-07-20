package com.roy.movieview.ui.adapter.movie;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.roy.movieview.R;
import com.roy.movieview.bean.movie.DirectorActor;
import com.roy.movieview.utils.image.ImageUtils;

import java.util.List;

/**
 * Created by 1vPy(Roy) on 2017/5/12.
 */

public class MovieDetailAdapter extends BaseQuickAdapter<DirectorActor, BaseViewHolder> {
    public MovieDetailAdapter(int layoutResId, List<DirectorActor> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DirectorActor item) {
        if (item.getAvatars() != null) {
            ImageUtils.displayImage(mContext, item.getAvatars().getLarge(), (ImageView) helper.getView(R.id.iv_avatar));
        } else {
            ImageUtils.displayImage(mContext, null, (ImageView) helper.getView(R.id.iv_avatar));
        }
        helper.setText(R.id.tv_name, item.getName());
        switch (item.getRole()) {
            case 0:
                helper.setText(R.id.tv_role, "[导演]");
                break;
            case 1:
                helper.setText(R.id.tv_role, "[演员]");
                break;
        }
    }
}
