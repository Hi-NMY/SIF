package com.example.sif.Lei.MyToolClass;

import android.app.Activity;

import androidx.core.content.ContextCompat;

import com.example.sif.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;

public class SelectImage {

    private Activity activity;
    public SelectImage(Activity activity1) {
        this.activity = activity1;
        ImageTheme();
    }

    public void showSelectImage(int i,String imageName){
        switch (i){
            case 1:
                PictureSelector.create(activity)
                        .openGallery(PictureMimeType.ofImage())
                        .loadImageEngine(GlideEngine.createGlideEngine())
                        .setPictureStyle(mPictureParameterStyle)
                        .setPictureCropStyle(mCropParameterStyle)
                        .enableCrop(true)
                        .compress(true)
                        .renameCropFileName("a"+imageName)
                        .renameCompressFile(imageName)
                        .setLanguage(-1)
                        .isUseCustomCamera(false)
                        .maxVideoSelectNum(1)
                        .isReturnEmpty(false)
                        .selectionMode(PictureConfig.SINGLE)
                        .previewImage(false)
                        .showCropGrid(false)
                        .withAspectRatio(1,1)
                        .hideBottomControls(true)
                        .showCropFrame(true)
                        .isDragFrame(true)
                        .rotateEnabled(false)
                        .scaleEnabled(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;

            case 2:
                PictureSelector.create(activity)
                        .openCamera(PictureMimeType.ofImage())
                        .loadImageEngine(GlideEngine.createGlideEngine())
                        .setPictureStyle(mPictureParameterStyle)
                        .setPictureCropStyle(mCropParameterStyle)
                        .enableCrop(true)
                        .compress(true)
                        .renameCropFileName("a"+imageName)
                        .renameCompressFile(imageName)
                        .setLanguage(-1)
                        .isUseCustomCamera(false)
                        .maxVideoSelectNum(1)
                        .isReturnEmpty(false)
                        .selectionMode(PictureConfig.SINGLE)
                        .previewImage(true)
                        .imageFormat(PictureMimeType.JPEG)
                        .showCropGrid(false)
                        .withAspectRatio(1,1)
                        .hideBottomControls(true)
                        .showCropFrame(true)
                        .isDragFrame(true)
                        .rotateEnabled(false)
                        .scaleEnabled(true)
                        .forResult(PictureConfig.REQUEST_CAMERA);
                break;
            case 3:
                PictureSelector.create(activity)
                        .openGallery(PictureMimeType.ofImage())
                        .loadImageEngine(GlideEngine.createGlideEngine())
                        .setPictureStyle(mPictureParameterStyle)
                        .setPictureCropStyle(mCropParameterStyle)
                        .setLanguage(-1)
                        .isUseCustomCamera(false)
                        .maxVideoSelectNum(1)
                        .isReturnEmpty(false)
                        .selectionMode(PictureConfig.SINGLE)
                        .previewImage(false)
                        .showCropGrid(false)
                        .withAspectRatio(1,1)
                        .hideBottomControls(true)
                        .showCropFrame(true)
                        .isDragFrame(true)
                        .rotateEnabled(false)
                        .scaleEnabled(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
        }

    }

    private PictureParameterStyle mPictureParameterStyle;
    private PictureCropParameterStyle mCropParameterStyle;
    private void ImageTheme(){
        mPictureParameterStyle = new PictureParameterStyle();

        mCropParameterStyle = new PictureCropParameterStyle(
                ContextCompat.getColor(activity, R.color.beijing),
                ContextCompat.getColor(activity, R.color.beijing),
                ContextCompat.getColor(activity, R.color.ziti),
                mPictureParameterStyle.isChangeStatusBarFontColor);
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle.isChangeStatusBarFontColor = false;
// 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle.isOpenCompletedNumStyle = false;
// 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle.isOpenCheckNumStyle = false;
// 相册状态栏背景色
        mPictureParameterStyle.pictureStatusBarColor = ContextCompat.getColor(activity, R.color.beijing);
// 相册列表标题栏背景色
        mPictureParameterStyle.pictureTitleBarBackgroundColor = ContextCompat.getColor(activity, R.color.beijing);
// 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_arrow_up;
// 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_arrow_down;
// 相册文件夹列表选中圆点
        mPictureParameterStyle.pictureFolderCheckedDotStyle = 0;
// 相册返回箭头
        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.left_xiao;
// 标题栏字体颜色
        mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(activity, R.color.ziti);
// 相册右侧取消按钮字体颜色
        mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(activity, R.color.ziti);
// 相册列表勾选图片样式
        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_selector;
// 相册列表底部背景色
        mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(activity, R.color.beijing);
// 已选数量圆点背景样式
        mPictureParameterStyle.pictureCheckNumBgStyle = 0;
// 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(activity, R.color.ziti);
// 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(activity, R.color.picture_color_9b);
// 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(activity, R.color.weilan);
// 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(activity, R.color.picture_color_9b);
// 预览界面底部背景色
        mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(activity, R.color.beijing);
// 外部预览界面删除按钮样式
        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete;
// 外部预览界面是否显示删除按钮
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
    }
}
