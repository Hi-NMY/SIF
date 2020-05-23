package com.example.sif.Lei.ShiPeiQi;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewItemJianJu extends RecyclerView.ItemDecoration {
    private int space;

    public ViewItemJianJu(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = space;
    }
}
