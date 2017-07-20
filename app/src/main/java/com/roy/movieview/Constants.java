package com.roy.movieview;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/5/17.
 */

public class Constants {
    public static final String application_id = "c6dbd18c94e5d67f3047f196165df693";

    public static final String application_key = "0c0633c21b454ebbb447e8cc3065ee33";

    public static final String image_save_path = Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "data" + File.separator + MvKit.getContext().getPackageName();

    public static final String apk_save_path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"MovieView.apk";

    public static final long t_sdk_app_id = 1400035954;

    public static final int t_account_type = 14244;
}
