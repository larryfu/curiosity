package com.shopee.antifraud.execute.logic.model;

public enum OpType {
    AND("&&"), OR("||"), NOT("!");
    private String symbol;

    OpType(String symbol) {
        this.symbol = symbol;
    }

    public static OpType parse(String s) {
        for (OpType opType : values()) {
            if (opType.symbol.equals(s)) {
                return opType;
            }
        }
        return null;
    }

}
