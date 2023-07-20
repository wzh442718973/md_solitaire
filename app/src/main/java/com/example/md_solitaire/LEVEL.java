package com.example.md_solitaire;

public enum LEVEL {

    SINGLE("single"),
    DOUBLE("double"),
    FOUR("four");

    private LEVEL(String name){
        this.name = name;
    }
    public final String name;
}
