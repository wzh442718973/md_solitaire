package com.example.md_solitaire;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

public class DragShadow extends View.DragShadowBuilder{

    private Bitmap bitmap;
    DragShadow(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    @Override
    public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
        outShadowSize.set(this.bitmap.getWidth(), this.bitmap.getHeight());
        outShadowTouchPoint.set(outShadowSize.x / 2, outShadowSize.y / 2);
    }

    @Override
    public void onDrawShadow(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }
}
