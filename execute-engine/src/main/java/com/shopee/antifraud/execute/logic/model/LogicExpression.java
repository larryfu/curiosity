package com.shopee.antifraud.execute.logic.model;


import lombok.Data;

import java.util.List;


@Data
public class LogicExpression {


    private int weight;

    boolean isLeaf;

    private String expression;

    private OpType opType;

    private List<LogicExpression> subExpr;

}
