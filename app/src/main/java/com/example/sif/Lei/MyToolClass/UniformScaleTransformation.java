package com.example.sif.Lei.MyToolClass;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.ImageViewTarget;

public class UniformScaleTransformation extends ImageViewTarget<Bitmap> {

    private ImageView target;

    public UniformScaleTransformation(ImageView view) {
        super(view);
        this.target = view;
    }

    @Override
    protected void setResource(@Nullable Bitmap resource) {
        if (resource == null){
            return;
        }
        view.setImageBitmap(resource);
        int width = resource.getWidth();
        int height = resource.getHeight();

        int imageViewWidth = target.getWidth();

        float sy = (float)(imageViewWidth * 0.1) / (float)(width/0.1);

        int imageViewHeight = (int)(height * sy);
        ViewGroup.LayoutParams params = target.getLayoutParams();
        params.height = imageViewHeight;
        target.setLayoutParams(params);

    }
}
