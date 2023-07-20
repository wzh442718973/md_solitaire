package com.example.md_solitaire;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class DeckView extends FrameLayout implements Holder {

    private final Stack<Card> cardStack = new Stack<>();

    public DeckView(@NonNull Context context) {
        this(context, null, 0, 0);
    }

    public DeckView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public DeckView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DeckView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final DisplayMetrics dm = getResources().getDisplayMetrics();
        adc_y = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, dm);
    }

    public void clear(){
        cardStack.clear();
        removeAllViews();
    }

    static int SPACE = 5;
    int card_width = 0;
    int card_heigh = 0;

    final float adc_y;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);

        int heigh = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        if (wMode == MeasureSpec.UNSPECIFIED || hMode == MeasureSpec.UNSPECIFIED) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        super.setMeasuredDimension(width, heigh);
        card_width = width - SPACE * 2;
        card_heigh = (int) (card_width * 1.5f);
        for (int i = 0; i < getChildCount(); ++i) {
            View child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(card_width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(card_heigh, MeasureSpec.EXACTLY));
        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {


        final int childs = getChildCount();
        for (int i = 0; i < childs; ++i) {
            View child = getChildAt(i);

            int heigh = child.getMeasuredHeight();
            int width = child.getMeasuredWidth();

            int x = (right - left - width) / 2;
            child.layout(x, top, x + width, top + heigh);

            top += adc_y;
        }
    }

    public View.DragShadowBuilder getDragView(View view) {

        final int count = getChildCount();
        int index = indexOfChild(view);
        if (index >= 0) {
            int width = card_width;
            int heigh = (int) (card_heigh + adc_y * (count - index));

            Bitmap bitmap = Bitmap.createBitmap(width, heigh, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            int x =0, y = 0;
            while (index < count) {
                canvas.translate(x, y);
                getChildAt(index).draw(canvas);
                y += adc_y;
                index++;
            }

            return new DragShadow(bitmap);

        }
        return null;
    }

    public boolean checkDrag(View view) {
        final int count = getChildCount();
        int index = indexOfChild(view);
        if (index >= 0) {
            Card select = (Card) view.getTag();
            for (++index; index < count; ++index) {
                View child = getChildAt(index);
                Card card = (Card) child.getTag();
                if (select.getValue() == card.getValue() + 1) {
                    select = card;
                    continue;
                }
                return false;

            }
            return true;
        }
        return false;
    }

    private OnTouchListener touchListener;
    private OnDragListener dragListener;

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        touchListener = l;
//        super.setOnTouchListener(l);
    }

    @Override
    public void setOnDragListener(OnDragListener l) {
        dragListener = l;
        super.setOnDragListener(l);
    }

    public void setCards(Vector<Card> cards) {
        cardStack.addAll(cards);

        final Resources res = getResources();
        ImageView last = null;
        for (int i = 0; i < cards.size(); ++i) {
            last = addCard(cards.get(i), false);
        }
        if (last != null) {
            Card card = (Card) last.getTag();
            last.setImageResource(card.getResId());
            last.setOnTouchListener(touchListener);
        }
    }

    public void push(Card card) {
        cardStack.push(card);
        addCard(card, true);
    }

    public List<Card> pop(View view){
        List<Card> list = new ArrayList<>();
        int index = indexOfChild(view);
        while (index < getChildCount()) {
            list.add((Card) getChildAt(index).getTag());
            removeViewAt(index);
            cardStack.remove(index);
        }
        if (cardStack.size() > 0) {
            ImageView img = (ImageView) getChildAt(getChildCount() - 1);
            Card card = (Card) img.getTag();
            img.setImageResource(card.getResId());
            img.setOnTouchListener(touchListener);
        }
        return list;
    }

    @Override
    public boolean isEmpty() {
        return cardStack.isEmpty();
    }

    @Override
    public Card pop() {
        Card card = null;
        if (cardStack.size() > 0) {
            card = cardStack.pop();
            removeViewAt(getChildCount() - 1);
        }
        if (cardStack.size() > 0) {
            ImageView view = (ImageView) getChildAt(getChildCount() - 1);
            card = (Card) view.getTag();
            view.setImageResource(card.getResId());
            view.setOnTouchListener(touchListener);
        }
        return card;
    }

    @Override
    public Card peek() {
        if (cardStack.size() > 0) {
            return cardStack.peek();
        }
        return null;
    }

    @Override
    public boolean isPark(LEVEL level, Card card) {
        Card top = peek();
        if (top == null) {
            return (card.getValue() == 13);
        } else {
            if (level == LEVEL.FOUR) {
                if ((top.getType().ordinal() + card.getType().ordinal()) % 2 == 0) {
                    return (top.getValue() == (card.getValue() + 1));
                }
            } else {
                if (top.getType() == card.getType()) {
                    return (top.getValue() == (card.getValue() + 1));
                }
            }
        }
        return false;
    }

    private ImageView addCard(Card card, boolean show) {
        ImageView view = new AppCompatImageView(getContext());
        view.setImageResource(show ? card.getResId() : R.drawable.bos);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setTag(card);
        view.setOnTouchListener(show ? touchListener : null);
        super.addView(view);
        requestLayout();
        return view;
    }
}
