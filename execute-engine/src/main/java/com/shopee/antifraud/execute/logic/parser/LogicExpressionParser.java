package com.shopee.antifraud.execute.logic.parser;


import com.shopee.antifraud.execute.logic.model.LogicExpression;
import com.shopee.antifraud.execute.logic.model.OpType;
import com.shopee.antifraud.execute.logic.model.Token;
import com.shopee.antifraud.execute.logic.model.TokenType;

import java.util.List;
import java.util.Stack;

public class LogicExpressionParser {


    public static void main(String[] args) {
        //  String expr = "( g6_f2 ||  g6_f3 ||  g6_f4  ||  g6_f1) && (g2_f1 || g2_f2 || g2_f3 ) && g5_f2   && ! g12_f1_C0007 && ! g12_f2_C0007";
        String expr = "a && b && e || c && d";
        LogicExpression le = parse(expr);
        LogicExprOptimizer.plainExpr(le);
        System.out.println(le.getExpression());
        System.out.println("finished");
    }

    public static LogicExpression parse(String exprStr) {
        exprStr = exprStr.trim();
        if (!exprStr.startsWith("(")) {
            exprStr = "(" + exprStr + ")";
        } else if (!exprStr.endsWith(")")) {
            exprStr = "(" + exprStr + ")";
        }
        List<Token> tokenList = LogicExpressionLexer.parse(exprStr);
        Stack<Token> operatorStack = new Stack<>();
        Stack<LogicExpression> operateExprStack = new Stack<>();
        for (Token token : tokenList) {
            if (token.getTokenType() == TokenType.AND
                    || token.getTokenType() == TokenType.OR
                    || token.getTokenType() == TokenType.NOT
                    || token.getTokenType() == TokenType.LEFT_BRACKET
            ) {
                operatorStack.push(token);
            }
            if (token.getTokenType() == TokenType.VAR) {
                LogicExpression logicExpression = new LogicExpression();
                logicExpression.setExpression(token.getText());
                //处理not运算
                if (!operatorStack.isEmpty() && operatorStack.peek().getTokenType() == TokenType.NOT) {
                    LogicExpression logicExpression1 = new LogicExpression();
                    logicExpression1.setOpType(OpType.NOT);
                    logicExpression1.addSub(logicExpression);
                    operatorStack.pop();
                    operateExprStack.push(logicExpression1);
                } else {
                    operateExprStack.push(logicExpression);
                }
            }

            if (token.getTokenType() == TokenType.RIGHT_BRACKET) {
                LogicExpression expression = calculate(operatorStack, operateExprStack, operatorStack.pop(), operateExprStack.pop());
                if (!operatorStack.isEmpty() && operatorStack.peek().getTokenType() == TokenType.NOT) {
                    LogicExpression logicExpression1 = new LogicExpression();
                    logicExpression1.setOpType(OpType.NOT);
                    logicExpression1.addSub(expression);
                    operatorStack.pop();
                    operateExprStack.push(logicExpression1);
                } else {
                    operateExprStack.push(expression);
                }
            }
        }
//        while (operateExprStack.size() > 1) {
//            doCalculate(operatorStack, operateExprStack);
//        }
        return operateExprStack.pop();
    }


    private static LogicExpression calculate(Stack<Token> operatorStack, Stack<LogicExpression> exprStack, Token operator, LogicExpression rightExpr) {
        if (operator.getTokenType() == TokenType.LEFT_BRACKET) {
            return rightExpr;
        }
        LogicExpression le = new LogicExpression();
        if (operator.getTokenType() == TokenType.AND) {
            LogicExpression leftExpr = exprStack.pop();
            le.setOpType(OpType.parse(operator.getText()));
            le.addSub(leftExpr);
            le.addSub(rightExpr);

            if (operatorStack.isEmpty()) {
                return le;
            }
            return calculate(operatorStack, exprStack, operatorStack.pop(), le);
        }
        if (operator.getTokenType() == TokenType.OR) {
            LogicExpression leftExpr;
            if (!operatorStack.isEmpty()) {
                leftExpr = calculate(operatorStack, exprStack, operatorStack.pop(), exprStack.pop());
            } else {
                leftExpr = exprStack.pop();
            }
            le.setOpType(OpType.parse(operator.getText()));
            le.addSub(rightExpr);
            le.addSub(leftExpr);
            return le;
        }
        throw new IllegalArgumentException("invalid operator" + operator.getText());
    }

    public static void doCalculate(Stack<Token> operatorStack, Stack<LogicExpression> exprStack) {
        if (operatorStack.isEmpty() || exprStack.isEmpty()) {
            return;
        }
        if (operatorStack.peek().getTokenType() == TokenType.RIGHT_BRACKET) {
            operatorStack.pop();
        }

        System.out.println("docalculate:" + operatorStack.peek().getText());
        LogicExpression logicExpression = new LogicExpression();
        Token token = operatorStack.pop();
        if (token.getTokenType() == TokenType.LEFT_BRACKET) {
            //空括号
            return;
        }
        LogicExpression expression1 = exprStack.pop();
        logicExpression.addSub(expression1);
        while (!exprStack.isEmpty()) {
            Token token1 = operatorStack.peek();
            if (token1.getTokenType() == TokenType.LEFT_BRACKET) {
                operatorStack.pop();
                break;
            }
            System.out.println("token1: " + token1.getText());
            OpType newOp = OpType.parse(token1.getText());

            if (logicExpression.getOpType() != null) {
                OpType cur = logicExpression.getOpType();
                if (newOp == cur) {
                    operatorStack.pop();
                    LogicExpression expr = exprStack.pop();
                    logicExpression.addSub(expr);
                } else if (newOp == OpType.AND) {

                } else {

                }
                //后面是and运算，遇到或运算，应该算作遇到左括号，终止
                if (cur == OpType.AND && newOp == OpType.OR) {
                    break;
                }
                //后面是or运算，遇到前面是And运算，需要等前面And运算完成
                if (cur == OpType.OR && newOp == OpType.AND) {
                    doCalculate(operatorStack, exprStack);
                    LogicExpression expr = exprStack.pop();
                    operatorStack.pop();
                    logicExpression.addSub(expr);
                }
            } else {
                operatorStack.pop();
                LogicExpression expr = exprStack.pop();
                logicExpression.addSub(expr);
                logicExpression.setOpType(newOp);
            }
        }
        //处理not运算
        if (!operatorStack.isEmpty() && operatorStack.peek().getTokenType() == TokenType.NOT) {
            LogicExpression logicExpression1 = new LogicExpression();
            logicExpression1.setOpType(OpType.NOT);
            logicExpression1.addSub(logicExpression);
            exprStack.push(logicExpression1);
            operatorStack.pop();
        } else {
            exprStack.push(logicExpression);
        }
    }

}
