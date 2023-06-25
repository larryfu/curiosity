package com.shopee.antifraud.execute.logic.parser;

import com.shopee.antifraud.execute.logic.model.OpType;
import com.shopee.antifraud.execute.logic.model.Token;
import com.shopee.antifraud.execute.logic.model.TokenType;

import java.util.ArrayList;
import java.util.List;

public class LogicExpressionLexer {


    private static boolean isVarCharStart(char c) {
        return c > 'a' && c < 'z' || c > 'A' && c < 'Z' || c == '_';
    }

    private static boolean isVarChar(char c) {
        return isVarCharStart(c) || c > '0' && c < '9';
    }


    public static List<Token> parse(String exprStr) throws IllegalArgumentException {
        List<Token> tokenList = new ArrayList<>();
        for (int i = 0; i < exprStr.length(); i++) {
            char c = exprStr.charAt(i);
            if (c == ' ' || c == '\n' || c == '\r' || c == '\t') {
                continue;
            }
            if (c == '(') {
                Token token = new Token();
                token.setText("(");
                token.setTokenType(TokenType.LEFT_BRACKET);
                tokenList.add(token);
                continue;
            }
            if (c == ')') {
                Token token = new Token();
                token.setText(")");
                token.setTokenType(TokenType.RIGHT_BRACKET);
                tokenList.add(token);
                continue;
            }
            if (c == '!') {
                Token token = new Token();
                token.setText("!");
                token.setTokenType(TokenType.NOT);
                tokenList.add(token);
                continue;
            }
            if (c == '&') {
                if (i + 1 >= exprStr.length() || exprStr.charAt(i + 1) != '&') {
                    throw new IllegalArgumentException("expect & ");
                }
                Token token = new Token();
                token.setTokenType(TokenType.AND);
                token.setText("&&");
                tokenList.add(token);
                i++;
                continue;
            }
            if (c == '|') {
                if (i + 1 >= exprStr.length() || exprStr.charAt(i + 1) != '|') {
                    throw new IllegalArgumentException("expect & ");
                }
                Token token = new Token();
                token.setTokenType(TokenType.OR);
                token.setText("||");
                tokenList.add(token);
                i++;
                continue;
            }
            if (isVarCharStart(c)) {
                int j = i + 1;
                for (; j < exprStr.length(); j++) {
                    if (!isVarChar(exprStr.charAt(j))) {
                        break;
                    }
                }
                String var = exprStr.substring(i, j);
                Token token = new Token();
                token.setTokenType(TokenType.VAR);
                token.setText(var);
                tokenList.add(token);
                i = j - 1;
                continue;
            }
            throw new IllegalArgumentException("unexpect char " + c);
        }
        return tokenList;
    }


}
