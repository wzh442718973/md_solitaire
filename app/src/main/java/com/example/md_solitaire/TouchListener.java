package com.example.md_solitaire;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

public class TouchListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            //view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }
}
