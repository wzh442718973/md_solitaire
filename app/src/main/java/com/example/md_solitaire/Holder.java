package com.example.md_solitaire;

import android.view.View;

import java.util.List;

public interface Holder {
    void push(Card card);
    Card pop();

    Card peek();

    boolean isPark(LEVEL level, Card card);

    List<Card> pop(View v);

    boolean isEmpty();
}
