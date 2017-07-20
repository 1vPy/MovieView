package com.roy.movieview.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.roy.movieview.R;
import com.roy.movieview.bean.movie.MStarDetail;
import com.roy.movieview.bean.movie.Work;
import com.roy.movieview.utils.common.JustUtils;
import com.roy.movieview.utils.common.ScreenUtils;
import com.roy.movieview.utils.image.ImageUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */

public class ActionDetailDialog extends Dialog{

    public ActionDetailDialog(@NonNull Context context) {
        super(context);
    }

    public ActionDetailDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected ActionDetailDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public static class Builder {
        private Context mContext;
        private MStarDetail mMStarDetail;
        private String positiveButtonText;
        private String negativeButtonText;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        private boolean isBottomShow = true;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setActionInfo(MStarDetail mStarDetail) {
            mMStarDetail = mStarDetail;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) mContext
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) mContext
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public ActionDetailDialog create() {
            ActionDetailDialog dialog = new ActionDetailDialog(mContext, R.style.Dialog);
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_action_detail, null);
            dialog.addContentView(view, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ImageUtils.displayImage(mContext, mMStarDetail.getAvatars().getLarge(), (ImageView) view.findViewById(R.id.iv_action_avatar));
            ((TextView) view.findViewById(R.id.tv_action_name)).setText(mMStarDetail.getName());
            ((TextView) view.findViewById(R.id.tv_action_name_en)).setText(mMStarDetail.getNameEn());
            ((TextView) view.findViewById(R.id.tv_action_gender)).setText(mMStarDetail.getGender());
            ((TextView) view.findViewById(R.id.tv_action_bornplace)).setText(mMStarDetail.getBornPlace());
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.ryv_main_subject);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            recyclerView.setAdapter(new MainSubjectAdapter(R.layout.item_main_subject, mMStarDetail.getWorks()));
            recyclerView.setFocusable(false);
            LinearLayout detail_bottom = (LinearLayout) view.findViewById(R.id.ll_bottom_layout);

            ((NestedScrollView)view.findViewById(R.id.nsv_action)).setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY - oldScrollY > 0 && isBottomShow) {  //下移隐藏
                        isBottomShow = false;
                        detail_bottom.animate().translationY(detail_bottom.getHeight());
                    } else if (scrollY - oldScrollY < 0 && !isBottomShow) {    //上移出现
                        isBottomShow = true;
                        detail_bottom.animate().translationY(0);
                    }
                }
            });

            if (positiveButtonText != null) {
                ((Button) view.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) view.findViewById(R.id.positiveButton))
                            .setOnClickListener(v -> positiveButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE));
                }
            } else {
                view.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            if (negativeButtonText != null) {
                ((Button) view.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) view.findViewById(R.id.negativeButton))
                            .setOnClickListener(v -> negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE));
                }
            } else {
                view.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = (int) (ScreenUtils.getScreenWidthPixels(mContext) * 0.8);//宽高可设置具体大小
            //lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = (int) (ScreenUtils.getScreenHeightPixels(mContext) * 0.6);
            dialog.getWindow().setAttributes(lp);
            //dialog.setContentView(view);
            return dialog;
        }
    }

    public static class MainSubjectAdapter extends BaseQuickAdapter<Work, BaseViewHolder> {

        public MainSubjectAdapter(int layoutResId, List<Work> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Work item) {
            ImageUtils.displayImage(mContext, item.getSubject().getImages().getLarge(), helper.getView(R.id.iv_subject_img));
            helper.setText(R.id.tv_subject_name, item.getSubject().getTitle());
            helper.setText(R.id.tv_subject_rating, item.getSubject().getRating().getAverage().toString());
            helper.setText(R.id.tv_subject_type, JustUtils.elements2String(item.getSubject().getGenres()));
            helper.setText(R.id.tv_subject_role, item.getRoles().get(0));
        }
    }
}
