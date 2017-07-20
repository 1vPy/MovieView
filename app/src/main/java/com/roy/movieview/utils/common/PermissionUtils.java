package com.roy.movieview.utils.common;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */

public class PermissionUtils {


    public static String[] checkPermission(Activity context, List<String> permissionList) {
        List<String> requestPermissionList = new ArrayList<>();

        for (String permission : permissionList) {
            if (ContextCompat.checkSelfPermission(context,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }

        return requestPermissionList.toArray(new String[requestPermissionList.size()]);
    }


    private static void requestPermissionsWrapper(Object cxt, String[] permission, int requestCode) {
        if (cxt instanceof Activity) {
            Activity activity = (Activity) cxt;
            ActivityCompat.requestPermissions(activity, permission, requestCode);
        } else if (cxt instanceof Fragment) {
            Fragment fragment = (Fragment) cxt;
            fragment.requestPermissions(permission, requestCode);
        } else {
            throw new RuntimeException("context is not a activity or fragment");
        }
    }

}
