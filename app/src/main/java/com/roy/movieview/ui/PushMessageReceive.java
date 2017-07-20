package com.roy.movieview.ui;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.roy.movieview.ui.activity.user.DialogActivity;
import com.roy.movieview.utils.common.JustUtils;
import com.roy.movieview.utils.common.NotificationHelper;
import com.roy.movieview.utils.user.UserPreference;

import java.util.List;

import cn.bmob.push.PushConstants;

/**
 * Created by Administrator on 2017/6/28.
 */

public class PushMessageReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {

            UserPreference userPreference = new UserPreference(context);
            userPreference.clearUserInfo();

            if(JustUtils.isBackground(context)){
                NotificationHelper.createNotification(context);
            }else{
                Intent i = new Intent(context, DialogActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(i);
            }

        }
    }

}
