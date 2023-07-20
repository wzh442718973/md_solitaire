package com.example.md_solitaire;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class StackView extends AppCompatImageView implements Holder {
    private final Stack<Card> cardStack = new Stack<>();

    public StackView(@NonNull Context context) {
        super(context);
    }

    public StackView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StackView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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


    void updateView() {
        Card card = peek();
        super.setOnTouchListener(card != null ? touchListener : null);
        setTag(card);
        setImageResource(card != null ? card.getResId() : R.drawable.bos_as);
    }

    public void clear() {
        cardStack.clear();
        updateView();
    }

    public void push(Card card) {
        cardStack.push(card);
        updateView();
    }

    @Override
    public Card pop() {
        Card card = null;
        if (cardStack.size() > 0) {
            card = cardStack.pop();
        }
        updateView();
        return card;
    }

    @Override
    public Card peek() {
        if (cardStack.size() > 0) {
            return cardStack.peek();
        } else {
            return null;
        }
    }

    @Override
    public boolean isPark(LEVEL level, Card card) {
        Card top = peek();
        if (top == null) {
            return card.getValue() == 1;
        } else {
            if (top.getType() == card.getType()) {
                if (top.getValue() + 1 == card.getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Card> pop(View v) {
        assert (v == this);
        List<Card> list = new ArrayList<>();
        list.add(pop());
        return list;
    }

    @Override
    public boolean isEmpty() {
        return cardStack.isEmpty();
    }
}
