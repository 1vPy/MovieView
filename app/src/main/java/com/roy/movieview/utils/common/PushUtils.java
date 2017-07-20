package com.roy.movieview.utils.common;

import android.content.Context;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;

/**
 * Created by Administrator on 2017/6/29.
 */

public class PushUtils {
    public static void pushLoginMessage(Context context,String InstallationId){
        BmobPushManager bmobPush = new BmobPushManager(context);
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("installationId", InstallationId);
        bmobPush.setQuery(query);
        bmobPush.pushMessage("消息内容");
    }
}
