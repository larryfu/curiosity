package com.shopee.antifraud.execute.logic.model;


import lombok.Data;

@Data
public class Token {

    private TokenType tokenType;

    private String text;


}
