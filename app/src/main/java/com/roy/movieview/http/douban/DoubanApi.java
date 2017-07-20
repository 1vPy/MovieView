package com.roy.movieview.http.douban;

import com.roy.movieview.bean.movie.MStarDetail;
import com.roy.movieview.bean.movie.MovieBean;
import com.roy.movieview.bean.movie.MovieDetail;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by 1vPy(Roy) on 2017/6/20.
 */

public interface DoubanApi {
    String API_BASE_URL = "https://api.douban.com/";

    /**
     * 正在上映
     *
     * @param start
     * @param count
     * @param city
     * @return
     */
    @GET("v2/movie/in_theaters")
    Observable<MovieBean> getHotMovie(@Query("start") int start, @Query("count") int count, @Query("city") String city);

    /**
     * 即将上映
     *
     * @param start
     * @param count
     * @return
     */
    @GET("v2/movie/coming_soon")
    Observable<MovieBean> getComingMovie(@Query("start") int start, @Query("count") int count);

    /**
     * Top250
     *
     * @param start
     * @param count
     * @return
     */
    @GET("v2/movie/top250")
    Observable<MovieBean> getTopMovie(@Query("start") int start, @Query("count") int count);

    /**
     * 电影详情
     *
     * @param id
     * @return
     */
    @GET("v2/movie/subject/{id}")
    Observable<MovieDetail> getMovieDetail(@Path("id") String id);

    /**
     * 电影查询
     *
     * @param query
     * @return
     */
    @GET("v2/movie/search")
    Observable<MovieBean> searchMovie(@Query("q") String query);

    /**
     * 演员查询
     *
     * @param id
     * @return
     */
    @GET("/v2/movie/celebrity/{id}")
    Observable<MStarDetail> getStarDetail(@Path("id") String id);
}
