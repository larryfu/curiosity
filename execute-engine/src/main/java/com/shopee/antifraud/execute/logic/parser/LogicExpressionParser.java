package com.shopee.antifraud.execute.logic.parser;

import com.shopee.antifraud.execute.logic.model.LogicExpression;
import com.shopee.antifraud.execute.logic.model.Token;

import java.util.List;
import java.util.Stack;

public class LogicExpressionParser {


    public LogicExpression parse(String exprStr) {
        List<Token> tokenList = LogicExpressionLexer.parse(exprStr);
        Stack<Token> stack = new Stack<>();
        for (Token token : tokenList) {

        }
        return null;
    }

}
