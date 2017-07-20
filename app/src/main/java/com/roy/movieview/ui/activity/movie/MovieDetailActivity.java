package com.roy.movieview.ui.activity.movie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.roy.movieview.R;
import com.roy.movieview.bean.movie.DirectorActor;
import com.roy.movieview.bean.movie.MStarDetail;
import com.roy.movieview.bean.movie.MovieDetail;
import com.roy.movieview.presenter.impl.movie.MovieDetailPresenterImpl;
import com.roy.movieview.ui.activity.BaseDataActivity;
import com.roy.movieview.ui.adapter.movie.MovieDetailAdapter;
import com.roy.movieview.ui.contract.movie.MovieDetailContract;
import com.roy.movieview.ui.custom.ActionDetailDialog;
import com.roy.movieview.utils.common.JustUtils;
import com.roy.movieview.utils.common.ScreenUtils;
import com.roy.movieview.utils.image.ImageUtils;
import com.roy.movieview.utils.tip.SnackBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 1vPy(Roy) on 2017/5/12.
 */

public class MovieDetailActivity extends BaseDataActivity<MovieDetailContract.View, MovieDetailPresenterImpl>
        implements MovieDetailContract.View
        , View.OnClickListener
        , NestedScrollView.OnScrollChangeListener
        , BaseQuickAdapter.OnItemClickListener {
    private static final String TAG = MovieDetailActivity.class.getSimpleName();


    @BindView(R.id.clp_toolbar)
    CollapsingToolbarLayout clp_toolbar;
    @BindView(R.id.cdl_root)
    CoordinatorLayout cdl_root;
    @BindView(R.id.iv_movie_detail_image)
    ImageView iv_movie_detail_image;
    @BindView(R.id.iv_movie)
    ImageView iv_movie;
    @BindView(R.id.tv_rating)
    TextView tv_rating;
    @BindView(R.id.tv_rating_num)
    TextView tv_rating_num;
    @BindView(R.id.tv_year)
    TextView tv_year;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tv_country)
    TextView tv_country;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_original_name)
    TextView tv_original_name;
    @BindView(R.id.tv_summary)
    TextView tv_summary;
    @BindView(R.id.ryv_movie)
    RecyclerView ryv_movie;
    @BindView(R.id.tv_more_details)
    TextView tv_more_details;
    @BindView(R.id.tv_buy_tickets)
    TextView tv_buy_tickets;
    @BindView(R.id.fab_collection)
    FloatingActionButton fab_collection;
    @BindView(R.id.nsv_scroller)
    NestedScrollView nsv_scroller;
    @BindView(R.id.detail_bottom)
    FrameLayout detail_bottom;
    @BindView(R.id.tv_detail_bottom_share)
    TextView tv_detail_bottom_share;
    @BindView(R.id.tv_detail_bottom_comment)
    TextView tv_detail_bottom_comment;
    @BindView(R.id.tv_detail_bottom_praise)
    TextView tv_detail_bottom_praise;

    private ProgressDialog mProgressDialog;

    private MovieDetailAdapter mMovieDetailAdapter;

    private String mId;
    private MovieDetail mMovieDetail;

    private List<DirectorActor> mDirectorActorList = new ArrayList<>();

    private boolean isBottomShow = false;

    private boolean isPraise = false;

    private int praiseNum = 0;

    public static void start(Context context, String id) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra("movieId", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        dynamicAddView(clp_toolbar, "collapsingToolbarLayout", R.color.toolbar_bg);
    }

    @Override
    protected View rootView() {
        return nsv_scroller;
    }

    @Override
    protected void retryClicked() {
        mPresenterImpl.getMovieDetail(mId);
        showLoading();
    }

    @Override
    protected boolean canSwipeBack() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        mToolbar.setNavigationOnClickListener(v -> MovieDetailActivity.this.finish());
        ryv_movie.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        nsv_scroller.setOnScrollChangeListener(this);
        mMovieDetailAdapter = new MovieDetailAdapter(R.layout.item_movie_detail, mDirectorActorList);
        ryv_movie.setAdapter(mMovieDetailAdapter);
        if (getIntent() != null) {
            mId = getIntent().getStringExtra("movieId");
            if (mId != null) {
                mPresenterImpl.getMovieDetail(mId);
            }
        }

        fab_collection.setOnClickListener(this);
        tv_detail_bottom_share.setOnClickListener(this);
        tv_detail_bottom_comment.setOnClickListener(this);
        tv_detail_bottom_praise.setOnClickListener(this);
        tv_more_details.setOnClickListener(this);
        tv_buy_tickets.setOnClickListener(this);
        mMovieDetailAdapter.setOnItemClickListener(this);

        showLoading();

        detail_bottom.setY(ScreenUtils.dipToPx(this, 56));

        fab_collection.hide();

    }


    @Override
    public void movieDetail(MovieDetail movieDetail) {
        initData(movieDetail);
        isBottomShow = true;
        detail_bottom.animate().translationY(0);
        fab_collection.show();
    }

    @Override
    public void mStarDetail(MStarDetail mStarDetail) {
        mProgressDialog.dismiss();
        new ActionDetailDialog.Builder(this)
                .setActionInfo(mStarDetail)
                .setPositiveButton("查看详情", (dialog, which) -> {

                })
                .setNegativeButton("关闭", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }


    private void initData(MovieDetail movieDetail) {
        mMovieDetail = movieDetail;

        if (!TextUtils.isEmpty(movieDetail.getTitle())) {
            mToolbar.setTitle(movieDetail.getTitle());
            tv_name.setText(movieDetail.getTitle());
        }

        if (movieDetail.getImages() != null) {
            ImageUtils.displayImage(this, movieDetail.getImages().getLarge(), iv_movie_detail_image);
            ImageUtils.displayImage(mContext, movieDetail.getImages().getLarge(), iv_movie);
        }

        if (movieDetail.getRating() != null) {
            tv_rating.setText("评分" + movieDetail.getRating().getAverage());
        }

        tv_rating_num.setText(movieDetail.getRatingsCount() + "人 评分");

        tv_year.setText(movieDetail.getYear() + " 上映");

        tv_type.setText("类型：" + JustUtils.elements2String(movieDetail.getGenres()));

        tv_country.setText("国家：" + JustUtils.elements2String(movieDetail.getCountries()));

        tv_original_name.setText(movieDetail.getOriginalTitle() + " [原名]");

        tv_summary.setText(movieDetail.getSummary());

        initDirectorActorList(movieDetail);

        praiseNum = movieDetail.getPraiseNum();

        tv_detail_bottom_praise.setText("点赞(" + movieDetail.getPraiseNum() + ")");

        tv_detail_bottom_comment.setText("评论(" + movieDetail.getCommentNum() + ")");

        if (movieDetail.isPraise()) {
            Drawable drawable = getResources().getDrawable(R.drawable.icon_collect_n);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_detail_bottom_praise.setCompoundDrawables(null, drawable, null, null);
            isPraise = true;
        }

        hideLoading();
    }

    /**
     * 导演、演员合并为一个list
     *
     * @param movieDetail
     */
    private void initDirectorActorList(MovieDetail movieDetail) {
        mDirectorActorList.addAll(JustUtils.two2One(movieDetail));
        mMovieDetailAdapter.notifyDataSetChanged();

    }

    @Override
    public void praiseSuccess() {
        isPraise = true;
        Drawable drawable = getResources().getDrawable(R.drawable.icon_collect_n);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_detail_bottom_praise.setCompoundDrawables(null, drawable, null, null);
        tv_detail_bottom_praise.setText("点赞(" + (praiseNum + 1) + ")");
        SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.praise_success), SnackBarUtils.Info).show();
    }


    @Override
    public void showError(String message) {
        showError();
        fab_collection.hide();
        SnackBarUtils.LongSnackbar(cdl_root, message, SnackBarUtils.Warning).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_more_details:
                openWebView(mMovieDetail.getAlt());
                break;
            case R.id.tv_buy_tickets:
                openWebView(mMovieDetail.getScheduleUrl());
                break;
            case R.id.fab_collection:
                collection();
                break;
            case R.id.tv_detail_bottom_praise:
                praise();
                break;
            case R.id.tv_detail_bottom_comment:
                break;
        }
    }

    private void collection() {

    }

    private void praise() {
        if (!mPresenterImpl.isLogin()) {
            SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.login_praise), SnackBarUtils.Warning).show();
            return;
        }
        if (isPraise) {
            SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.not_praise_again), SnackBarUtils.Info).show();
            return;
        }
        mPresenterImpl.addPraise(mId, mPresenterImpl.getUserInfo().getUsername());
        isPraise = true;
    }

    private void openWebView(String url) {
       /* Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.setClass(MovieDetailActivity.this, WebViewActivity.class);
        startActivity(intent);*/
    }


    //控制底部菜单栏的显示与隐藏
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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在获取演员数据..");
        mProgressDialog.show();
        mPresenterImpl.getMStarDetail(mDirectorActorList.get(position).getId());
    }
}
