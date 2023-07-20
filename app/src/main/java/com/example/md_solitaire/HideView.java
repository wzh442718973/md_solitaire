package com.example.md_solitaire;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.Vector;

public class HideView extends AppCompatImageView {


    private final Vector<Card> cards = new Vector<>();

    public interface IFlipListener {
        void onFinish();

        void onFlip(Card card);
    }

    private final View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            IFlipListener lis = listener;
            if (lis == null) {
                return;
            }
            if (cards.isEmpty()) {
                lis.onFinish();
            } else {
                Card card = cards.remove(0);
                lis.onFlip(card);
                updateView();
            }
        }
    };

    public void setCards(Vector<Card> vector) {
        cards.clear();
        cards.addAll(vector);
        updateView();
    }

    public void clear() {
        cards.clear();
        updateView();
    }

    void updateView() {
        if (cards.isEmpty()) {
//            setImageResource(R.drawable.bos_kart);
            setImageResource(0);
        } else {
            setImageResource(R.drawable.bos);
        }
    }

    private IFlipListener listener;

    public void setFlipListener(IFlipListener listener) {
        this.listener = listener;
    }

    public HideView(@NonNull Context context) {
        this(context, null, 0);
    }

    public HideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HideView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setFocusable(true);
        setClickable(true);
        setOnClickListener(clickListener);
    }
}
