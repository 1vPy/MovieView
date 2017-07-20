package com.roy.movieview.ui.fragment.movie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.roy.movieview.R;
import com.roy.movieview.bean.movie.MovieBean;
import com.roy.movieview.bean.movie.Subject;
import com.roy.movieview.presenter.impl.movie.MovieHotPresenterImpl;
import com.roy.movieview.ui.activity.movie.MovieDetailActivity;
import com.roy.movieview.ui.adapter.movie.MovieHotAdapter;
import com.roy.movieview.ui.contract.movie.MovieHotContract;
import com.roy.movieview.ui.custom.CustomLoadMoreView;
import com.roy.movieview.ui.fragment.BaseDataFragment;
import com.roy.movieview.utils.common.LogUtils;
import com.roy.movieview.utils.tip.SnackBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 正在热映电影Fragment
 * <p>
 * Created by 1vPy(Roy) on 2017/5/11.
 */

public class MovieHotFragment extends BaseDataFragment<MovieHotContract.View, MovieHotPresenterImpl> implements MovieHotContract.View
        , BaseQuickAdapter.RequestLoadMoreListener
        , SwipeRefreshLayout.OnRefreshListener
        , View.OnClickListener
        , BaseQuickAdapter.OnItemClickListener
        , BaseQuickAdapter.OnItemLongClickListener {
    private static final String TAG = MovieHotFragment.class.getSimpleName();

    @BindView(R.id.cdl_root)
    CoordinatorLayout cdl_root;

    @BindView(R.id.fab_back_top)
    FloatingActionButton fab_back_top;

    @BindView(R.id.srl_movie)
    SwipeRefreshLayout srl_movie;

    @BindView(R.id.ryv_movie)
    RecyclerView ryv_movie;

    private View mError;

    private View mLoading;

    private MovieHotAdapter mMovieHotAdapter;

    private List<Subject> mSubjectsList = new ArrayList<>();

    private int pageCount = 0;

    private static final int PAGE_SIZE = 20;

    private int mNowPosition = 0;

    public static MovieHotFragment newInstance() {
        Bundle args = new Bundle();
        MovieHotFragment fragment = new MovieHotFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_hot, container, false);
        return view;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        mError = LayoutInflater.from(getActivity()).inflate(R.layout.view_error, (ViewGroup) ryv_movie.getParent(), false);
        mLoading = LayoutInflater.from(getActivity()).inflate(R.layout.view_loading, (ViewGroup) ryv_movie.getParent(), false);

        ryv_movie.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMovieHotAdapter = new MovieHotAdapter(R.layout.item_movie_hot, mSubjectsList);
        ryv_movie.setAdapter(mMovieHotAdapter);
        mMovieHotAdapter.setEmptyView(mLoading);//加载时的loading界面
        mMovieHotAdapter.setLoadMoreView(new CustomLoadMoreView());//设置自定义的效果
        mMovieHotAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);//设置item加载动画


        mError.setOnClickListener(v -> {
            mMovieHotAdapter.setEmptyView(mLoading);
            pageCount = 0;
            mPresenterImpl.getMovieHot(pageCount * PAGE_SIZE, PAGE_SIZE, "武汉");
        });

        initData();
        initListener();
    }

    private void initData() {
        mPresenterImpl.getMovieHot(pageCount * PAGE_SIZE, PAGE_SIZE, "武汉");
    }

    private void initListener() {
        mMovieHotAdapter.setOnLoadMoreListener(this, ryv_movie);//设置上拉加载更多监听
        mMovieHotAdapter.setOnItemClickListener(this);
        srl_movie.setOnRefreshListener(this);//下拉刷新监听
        fab_back_top.setOnClickListener(this);

        //RecyclerView滑动监听(当第一个item消失时显示返回顶部按钮，反之显示)
        ryv_movie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) ryv_movie.getLayoutManager();
                mNowPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (mNowPosition > 0) {
                    if (!fab_back_top.isShown())
                        fab_back_top.show();
                } else {
                    if (fab_back_top.isShown())
                        fab_back_top.hide();
                }
            }
        });
    }

    @Override
    public void movieHot(MovieBean bean) {
        LogUtils.log(TAG, bean.getTitle(), LogUtils.DEBUG);

        isRefreshingSuccess();

        isLoadingSuccess(bean);

        mSubjectsList.addAll(bean.getSubjects());
        mMovieHotAdapter.notifyDataSetChanged();
    }

    private void isRefreshingSuccess() {
        if (srl_movie.isRefreshing()) {
            srl_movie.setRefreshing(false);
            mSubjectsList.clear();
            mMovieHotAdapter.setNewData(mSubjectsList);
            SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.refresh_success), SnackBarUtils.Info).show();
        }
    }

    private void isLoadingSuccess(MovieBean bean) {
        if (mMovieHotAdapter.isLoading()) {
            mMovieHotAdapter.loadMoreComplete();
            isLoadEnd(bean);
        }

    }

    private void isLoadEnd(MovieBean bean) {
        LogUtils.log(TAG, "total:" + bean.getTotal() + "/current:" + (mSubjectsList.size() + bean.getSubjects().size()), LogUtils.DEBUG);

        //判断是否还有更多数据
        if (bean.getTotal() <= (mSubjectsList.size() + bean.getSubjects().size())) {
            mMovieHotAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showError(String message) {
        isRefreshingFailed();

        isLoadingFailed();

        isLoadingError();

    }

    private void isRefreshingFailed() {
        if (srl_movie.isRefreshing()) {
            srl_movie.setRefreshing(false);
            SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.refresh_failed), SnackBarUtils.Warning).show();
        }
    }

    private void isLoadingFailed() {
        if (mMovieHotAdapter.isLoading()) {
            pageCount--;
            mMovieHotAdapter.loadMoreFail();
        }
    }

    private void isLoadingError() {
        if (mSubjectsList == null || mSubjectsList.size() <= 0) {
            mMovieHotAdapter.setEmptyView(mError);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        loadMore();
    }

    private void loadMore(){
        pageCount++;
        mPresenterImpl.getMovieHot(pageCount * PAGE_SIZE, PAGE_SIZE, "武汉");
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    private void refresh(){
        pageCount = 0;
        mPresenterImpl.getMovieHot(pageCount * PAGE_SIZE, PAGE_SIZE, "武汉");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_back_top:
                if (mNowPosition < 10) {
                    ryv_movie.smoothScrollToPosition(0);
                } else {
                    ryv_movie.scrollToPosition(0);
                }
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MovieDetailActivity.start(getActivity(), mSubjectsList.get(position).getId());

    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        return false;
    }
}
