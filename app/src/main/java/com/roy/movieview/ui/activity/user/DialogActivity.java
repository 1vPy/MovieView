package com.roy.movieview.ui.activity.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.roy.movieview.ui.activity.MainActivity;

/**
 * Created by Administrator on 2017/6/30.
 */

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage("您的账号在其他地方登录,如非本人操作，请重新登录并修改密码")
                .setPositiveButton("重新登录", (dialog, which) -> {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    this.finish();
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    this.finish();
                })
                .show();

    }
}