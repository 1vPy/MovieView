package com.roy.movieview.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/7/14.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "MvCollection";
    private static final int DB_VERSION = 1;

    private static final String SQL_CREATE = "create table if not exists tb_collection(" +
            "" +
            "" +
            ")";



    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
