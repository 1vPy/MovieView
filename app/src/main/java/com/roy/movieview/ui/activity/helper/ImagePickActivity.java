package com.roy.movieview.ui.activity.helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;


import com.roy.movieview.BuildConfig;
import com.roy.movieview.Constants;
import com.roy.movieview.utils.common.LogUtils;
import com.roy.movieview.utils.image.PhotoToCircle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * Created by 1vPy(Roy) on 2017/6/8.
 */

public class ImagePickActivity extends AppCompatActivity {
    private static final String TAG = ImagePickActivity.class.getSimpleName();

    protected static final int FROM_ALBUM = 0;
    protected static final int FROM_CAMERA = 1;

    protected static final int REQUEST_ALBUM = 2;
    protected static final int REQUEST_ALBUM_N = 3;
    protected static final int REQUEST_CAMERA = 4;
    protected static final int REQUEST_CAMERA_N = 5;

    private static final int CROP_PICTURE = 6;

    private Uri mImageUri;
    private Bitmap mAvatar;
    private File mCameraFile = new File(Constants.image_save_path, "Camera.jpg");//照相机的File对象
    private File mCropFile = new File(Constants.image_save_path, "Crop.jpg");//裁剪后的File对象
    private File mGalleryFile = new File(Constants.image_save_path, "Callery.jpg");//相册的File对象

    public static void start(Activity activity, int requestCode, int from) {
        Intent intent = new Intent(activity, ImagePickActivity.class);
        intent.putExtra("from", from);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (getIntent().getIntExtra("from",0)){
            case FROM_ALBUM:
                fromAlbum();
                break;
            case FROM_CAMERA:
                fromCamera();
                break;
            default:
                break;
        }
    }

    private void fromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mGalleryFile));
        startActivityForResult(intent, REQUEST_ALBUM);
    }

    private void fromCamera() {
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
            mImageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", mCameraFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_CAMERA_N);

        } else {
            mImageUri = Uri.fromFile(mCameraFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            startActivityForResult(intent, REQUEST_CAMERA);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case REQUEST_ALBUM:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case REQUEST_ALBUM_N:
                    startPhotoZoom(data.getData());
                    break;
                case REQUEST_CAMERA:
                    startPhotoZoom(Uri.fromFile(mCameraFile)); // 开始对图片进行裁剪处理
                    break;
                case REQUEST_CAMERA_N:
                    startPhotoZoom(FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", mCameraFile));
                    break;
                case CROP_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }else{
            setResult(resultCode);
            this.finish();
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            LogUtils.log(TAG, "The uri is not exist.", LogUtils.DEBUG);
        }
        Intent intent = new Intent("com.android.camera.action.CROP");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri outPutUri = Uri.fromFile(mCropFile);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);

        } else {
            Uri outPutUri = Uri.fromFile(mCropFile);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     * @param data
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mAvatar = extras.getParcelable("data");
            mAvatar = PhotoToCircle.toRoundBitmap(mAvatar); // 这个时候的图片已经被处理成圆形的了
            saveBitmap(mAvatar);
            setResult(RESULT_OK);
            this.finish();
        }
    }

    public void saveBitmap(Bitmap bm) {
        if (mCropFile.exists()) {
            mCropFile.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(mCropFile);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
