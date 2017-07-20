package com.roy.movieview.utils.common;

import android.app.ActivityManager;
import android.content.Context;

import com.roy.movieview.bean.movie.Cast;
import com.roy.movieview.bean.movie.Director;
import com.roy.movieview.bean.movie.DirectorActor;
import com.roy.movieview.bean.movie.MovieDetail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1vPy(Roy) on 2017/6/21.
 */

public class JustUtils {
    public static <T>String elements2String(List<T> list){
        if (list != null && list.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (T s : list) {
                stringBuilder.append(s + "/");
            }
            return stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
        }
        return null;
    }

    public static List<DirectorActor> two2One(MovieDetail movieDetail){
        List<DirectorActor> mDirectorActors = new ArrayList<>();
        for (Director directors : movieDetail.getDirectors()) {
            DirectorActor directorActor = new DirectorActor();
            directorActor.setAvatars(directors.getAvatars());
            directorActor.setName(directors.getName());
            directorActor.setId(directors.getId());
            directorActor.setRole(0);
            mDirectorActors.add(directorActor);
        }

        for (Cast casts : movieDetail.getCasts()) {
            DirectorActor directorActor = new DirectorActor();
            directorActor.setAvatars(casts.getAvatars());
            directorActor.setName(casts.getName());
            directorActor.setId(casts.getId());
            directorActor.setRole(1);
            mDirectorActors.add(directorActor);
        }
        return mDirectorActors;
    }

    public static byte[] file2byte(String path) {
        byte[] data = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

    public static byte[] inputStream2byte(InputStream inputStream) {
        byte[] data = null;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = inputStream.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            inputStream.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }


    /**
     * 读取txt文件的内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String txt2String(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                LogUtils.log("JustUtils","packageName:"+context.getPackageName(),LogUtils.DEBUG);
                LogUtils.log("JustUtils","importance:"+appProcess.importance,LogUtils.DEBUG);
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return false;
                }else{
                    return true;
                }
            }
            LogUtils.log("JustUtils","other:"+appProcess.processName,LogUtils.DEBUG);
        }
        return true;
    }
}
