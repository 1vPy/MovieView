package com.roy.movieview.utils.image;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;
import com.roy.movieview.MvKit;

/**
 * Created by 1vPy(Roy) on 2017/3/29.
 */

public class GlideModuleSetting implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取系统分配给应用的总内存大小
        int memoryCacheSize = maxMemory / 8;//设置图片内存缓存占用八分之一
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));//设置内存缓存大小
        int diskCacheSize = 1024 * 1024 * 30;//最多可以缓存多少字节的数据
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, MvKit.getContext().getPackageName(), diskCacheSize));//设置磁盘缓存大小
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
