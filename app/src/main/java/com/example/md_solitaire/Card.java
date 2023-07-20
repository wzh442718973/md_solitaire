package com.example.md_solitaire;

import android.graphics.Color;

import java.io.Serializable;

class Card implements Serializable {

    private CardType type;
    private int value;

    private boolean returned;

    public Card(CardType type, int value) {
        this(type, value, false);
    }

    public Card(CardType type, int value, boolean returned) {
        this.setType(type);
        this.setValue(value);
        this.setReturned(returned);
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public int getColor() {
        switch (this.type) {
            case COEUR:
            case CARREAU:
                return Color.RED;
            default:
                return Color.BLACK;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value < 1 || value > 13) throw new IllegalArgumentException("Value not supported");
        this.value = value;
    }

    public String getName() {
        switch (this.value) {
            case 1:
                return "A";
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "7";
            case 8:
                return "8";
            case 9:
                return "9";
            case 10:
                return "10";
            case 11:
                return "J";
            case 12:
                return "Q";
            case 13:
                return "K";
            default:
                throw new RuntimeException("Bad card value");
        }
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }


    @Override
    public String toString() {
        return type.toString() + " " + value;
    }

    public static enum CardType {
        PIQUE, COEUR, CARREAU, TREFLE;
    }


    public int getResId(){
        return resIds[type.ordinal()][value];
    }

    private static final int resIds[][] = {
            {
                    0,
                    R.drawable.maca_as,
                    R.drawable.maca_2,
                    R.drawable.maca_3,
                    R.drawable.maca_4,
                    R.drawable.maca_5,
                    R.drawable.maca_6,
                    R.drawable.maca_7,
                    R.drawable.maca_8,
                    R.drawable.maca_9,
                    R.drawable.maca_10,
                    R.drawable.maca_vale,
                    R.drawable.maca_kiz,
                    R.drawable.maca_papaz,

            }, {
            0,
            R.drawable.kupa_as,
            R.drawable.kupa_2,
            R.drawable.kupa_3,
            R.drawable.kupa_4,
            R.drawable.kupa_5,
            R.drawable.kupa_6,
            R.drawable.kupa_7,
            R.drawable.kupa_8,
            R.drawable.kupa_9,
            R.drawable.kupa_10,
            R.drawable.kupa_vale,
            R.drawable.kupa_kiz,
            R.drawable.kupa_papaz,
    }, {
            0,
            R.drawable.sinek_as,
            R.drawable.sinek_2,
            R.drawable.sinek_3,
            R.drawable.sinek_4,
            R.drawable.sinek_5,
            R.drawable.sinek_6,
            R.drawable.sinek_7,
            R.drawable.sinek_8,
            R.drawable.sinek_9,
            R.drawable.sinek_10,
            R.drawable.sinek_vale,
            R.drawable.sinek_kiz,
            R.drawable.sinek_papaz,
    }, {
            0,
            R.drawable.karo_as,
            R.drawable.karo_2,
            R.drawable.karo_3,
            R.drawable.karo_4,
            R.drawable.karo_5,
            R.drawable.karo_6,
            R.drawable.karo_7,
            R.drawable.karo_8,
            R.drawable.karo_9,
            R.drawable.karo_10,
            R.drawable.karo_vale,
            R.drawable.karo_kiz,
            R.drawable.karo_papaz,
    }
    };
}