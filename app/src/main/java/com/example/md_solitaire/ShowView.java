package com.example.md_solitaire;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class ShowView extends AppCompatImageView implements Holder{

    private Stack<Card> cardStack = new Stack<>();

    public ShowView(@NonNull Context context) {
        this(context, null, 0);
    }

    public ShowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    void updateView(){
        Card card = peek();
        setImageResource(card != null ? card.getResId() : R.drawable.bos_kart);
        setTag(card);
        super.setOnTouchListener(card != null ? touchListener : null);
    }

    public void clear() {
        cardStack.clear();
        updateView();
    }

    public void push(Card card) {
        cardStack.push(card);
        updateView();
    }

    public Card pop() {
        Card top = null;
        if(cardStack.size() > 0){
            top = cardStack.pop();
        }
        updateView();
        return top;
    }

    @Override
    public Card peek() {
        if(cardStack.size() > 0){
            return cardStack.peek();
        }
        return null;
    }

    @Override
    public boolean isPark(LEVEL level, Card card) {
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

    public Vector<Card> getCards() {
        return new Vector<>(cardStack);
    }
}
