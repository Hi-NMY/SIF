package com.example.sif.Lei.NiceImageView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.sif.R;

public class CircleProgressBar extends View {
    //圆环画笔
    private Paint paint;
    //圆环颜色
    private int ringcolor;
    //圆环半径
    private float radius;
    //圆环宽度
    private float strokeWidth;
    //总进度
    private float maxProgress = 100;
    //当前进度
    private float newProgress;


    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initVariable();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressbar,0,0);
        radius = typedArray.getDimension(R.styleable.CircleProgressbar_radius,80);
        strokeWidth = typedArray.getDimension(R.styleable.CircleProgressbar_strokeWidth,10);
        ringcolor = typedArray.getColor(R.styleable.CircleProgressbar_ringColor,0xFF0000);
    }


    private void initVariable() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(ringcolor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (newProgress > 0){
            RectF rectF = new RectF(getWidth() / 2 - radius,getHeight() / 2 - radius, getWidth() / 2 + radius, getHeight() / 2 + radius);
            canvas.drawArc(rectF,0,0,false,paint);
            canvas.drawArc(rectF,-90,((float)newProgress / maxProgress) * 360,false,paint);
        }
    }

    public void setProgress(float progress){
        this.newProgress = progress;
        postInvalidate();
    }

}
