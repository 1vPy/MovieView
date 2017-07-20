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
import com.roy.movieview.presenter.impl.movie.MovieComeUpPresenterImpl;
import com.roy.movieview.ui.activity.movie.MovieDetailActivity;
import com.roy.movieview.ui.adapter.movie.MovieComeUpAdapter;
import com.roy.movieview.ui.contract.movie.MovieComeUpContract;
import com.roy.movieview.ui.custom.CustomLoadMoreView;
import com.roy.movieview.ui.fragment.BaseDataFragment;
import com.roy.movieview.utils.common.LogUtils;
import com.roy.movieview.utils.tip.SnackBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 即将上映电影Fragment
 * 代码与MovieHotFragment类似
 * Created by Administrator on 2017/5/11.
 */

public class MovieComeUpFragment extends BaseDataFragment<MovieComeUpContract.View,MovieComeUpPresenterImpl> implements MovieComeUpContract.View
        , BaseQuickAdapter.RequestLoadMoreListener
        , SwipeRefreshLayout.OnRefreshListener
        , View.OnClickListener
        , BaseQuickAdapter.OnItemClickListener{

    private static final String TAG = MovieHotFragment.class.getSimpleName();

    @BindView(R.id.cdl_root)
    CoordinatorLayout cdl_root;

    @BindView(R.id.fab_back_top)
    FloatingActionButton fab_back_top;

    @BindView(R.id.srl_movie)
    SwipeRefreshLayout srl_movie;

    @BindView(R.id.ryv_movie)
    RecyclerView ryv_movie;


    private MovieComeUpAdapter mMovieComeUpAdapter;

    private List<Subject> mSubjectsList = new ArrayList<>();

    private int pageCount = 0;

    private static final int PAGE_SIZE = 20;

    private int mNowPosition = 0;

    public static MovieComeUpFragment newInstance() {
        Bundle args = new Bundle();
        MovieComeUpFragment fragment = new MovieComeUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_comeup, container, false);
        return view;
    }


    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        ryv_movie.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMovieComeUpAdapter = new MovieComeUpAdapter(R.layout.item_movie_comeup, mSubjectsList);
        ryv_movie.setAdapter(mMovieComeUpAdapter);
        mMovieComeUpAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) ryv_movie.getParent());
        mMovieComeUpAdapter.setLoadMoreView(new CustomLoadMoreView());
        mMovieComeUpAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        initData();
        initListener();
    }

    private void initData() {
        mPresenterImpl.getMovieComeUp(pageCount * PAGE_SIZE, PAGE_SIZE);
    }

    private void initListener() {
        mMovieComeUpAdapter.setOnLoadMoreListener(this, ryv_movie);
        mMovieComeUpAdapter.setOnItemClickListener(this);
        srl_movie.setOnRefreshListener(this);
        fab_back_top.setOnClickListener(this);
        ryv_movie.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    public void onRefresh() {
        pageCount = 0;
        mPresenterImpl.getMovieComeUp(pageCount * PAGE_SIZE, PAGE_SIZE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_back_top:
                if(mNowPosition < 10){
                    ryv_movie.smoothScrollToPosition(0);
                }else{
                    ryv_movie.scrollToPosition(0);
                }
                fab_back_top.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        super.onHidden(fab);
                        SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.is_top), SnackBarUtils.Info).show();
                    }
                });
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        pageCount++;
        mPresenterImpl.getMovieComeUp(pageCount * PAGE_SIZE, PAGE_SIZE);
    }

    @Override
    public void movieComeUp(MovieBean bean) {
        LogUtils.log(TAG, bean.getTitle(), LogUtils.DEBUG);
        if (srl_movie.isRefreshing()) {
            srl_movie.setRefreshing(false);
            mSubjectsList.clear();
            mMovieComeUpAdapter.setNewData(mSubjectsList);
            SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.refresh_success), SnackBarUtils.Info).show();
        }
        if (mMovieComeUpAdapter.isLoading()) {
            mMovieComeUpAdapter.loadMoreComplete();
            LogUtils.log(TAG, "total:" + bean.getTotal() + "/current:" + (mSubjectsList.size() + bean.getSubjects().size()), LogUtils.DEBUG);
            if (bean.getTotal() <= (mSubjectsList.size() + bean.getSubjects().size())) {
                mMovieComeUpAdapter.loadMoreEnd();
            }
        }
        mSubjectsList.addAll(bean.getSubjects());
        mMovieComeUpAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        if (srl_movie.isRefreshing()) {
            srl_movie.setRefreshing(false);
            SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.refresh_success), SnackBarUtils.Warning).show();
        } else {
            SnackBarUtils.LongSnackbar(cdl_root, message, SnackBarUtils.Warning).show();
        }
        if (mMovieComeUpAdapter.isLoading()) {
            pageCount--;
            mMovieComeUpAdapter.loadMoreFail();
        }
        if (mSubjectsList == null || mSubjectsList.size() <= 0) {
            View error = LayoutInflater.from(getActivity()).inflate(R.layout.view_error, (ViewGroup) ryv_movie.getParent(), false);
            mMovieComeUpAdapter.setEmptyView(error);
            error.setOnClickListener(v -> {
                mMovieComeUpAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) ryv_movie.getParent());
                pageCount = 0;
                mPresenterImpl.getMovieComeUp(pageCount * PAGE_SIZE, PAGE_SIZE);
            });
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MovieDetailActivity.start(getActivity(),mSubjectsList.get(position).getId());
    }
}
