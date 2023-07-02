package com.shopee.antifraud.execute.logic.model;

public enum TokenType {
    VAR(null),
    AND("&&"),
    OR("||"),
    NOT("!"),
    LEFT_BRACKET("("),
    RIGHT_BRACKET(")");

    TokenType(String text) {
        this.text = text;
    }

    private String text;

    public String getText() {
        return text;
    }
}
