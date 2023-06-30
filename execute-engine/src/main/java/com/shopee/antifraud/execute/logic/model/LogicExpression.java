package com.shopee.antifraud.execute.logic.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


@Data
public class LogicExpression {


    private int weight;

    boolean isLeaf;

    private String expression;

    private OpType opType;

    private LinkedHashSet<LogicExpression> subExpr = new LinkedHashSet<>();


    public void  addSub(LogicExpression logicExpression){
        subExpr.add(logicExpression);
    }

    public void  removeSub(LogicExpression logicExpression){
        subExpr.remove(logicExpression);
    }

}
