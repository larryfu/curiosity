package com.shopee.antifraud.execute.logic.model;

import com.shopee.antifraud.execute.logic.parser.LogicExpressionParser;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LogicExpr {

    private LogicExpression root;

    private Set<String> vars;


    private List<CalculateStep> calculateSequence;


    public LogicExpr(String expr){
        this.root = LogicExpressionParser.parse(expr);

    }

    public void generateCalculateStep(Map<String,Integer> costMap,Map<String,Integer> probMap){
        root.calculateCost(costMap);
        root.calculateTruePercent(probMap);
    }
}
