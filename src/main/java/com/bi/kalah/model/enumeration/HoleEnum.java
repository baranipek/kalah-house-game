package com.bi.kalah.model.enumeration;

public enum HoleEnum {
    ONE(0),
    TWO(1),
    THREE(2),
    FOUR(3),
    FIVE(4),
    SIX(5);

    private int index;

    HoleEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}