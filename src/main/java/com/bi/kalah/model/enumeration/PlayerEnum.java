package com.bi.kalah.model.enumeration;


public enum PlayerEnum {
    NORTH("Player North"),
    SOUTH("Player South");

    private String value;

    PlayerEnum(String value) {
        this.value = value;
    }

    public String getValue() {return value;}

}
