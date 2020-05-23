package com.example.sif.Lei.MyToolClass;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

public class GlideRoundTransform extends BitmapTransformation {
    private static float radius = 0f;

    public GlideRoundTransform() {

    }

    public GlideRoundTransform(int dp){
        super();
        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = TransformationUtils.centerCrop(pool,toTransform,outWidth,outHeight);
        return roundCrop(pool, bitmap);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap bitmap) {
        if (bitmap == null){
            return null;
        }
        Bitmap resule = pool.get(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.ARGB_8888);
        if (resule == null){
            resule = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(resule);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(bitmap,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f,0f,bitmap.getWidth(),bitmap.getHeight());
        canvas.drawRoundRect(rectF,radius,radius,paint);

        return resule;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
