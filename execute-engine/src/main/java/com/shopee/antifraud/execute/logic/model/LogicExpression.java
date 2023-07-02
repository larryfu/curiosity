package com.shopee.antifraud.execute.logic.model;


import lombok.Data;

import java.util.*;


@Data
public class LogicExpression {


    private int weight;

    boolean isLeaf;

    private String expression;

    private OpType opType;


    //计算成本
    private int cost;

    //为true的百分概率
    private int truePercent;



    List<CalculateStep> generate(boolean target){
        if(getSubExpr() == null || getSubExpr().isEmpty()){
            CalculateStep step =  new CalculateStep();
            step.setVar(getExpression());
            step.setBreakValue(target);
            step.setFinalValue(target);
        }
        if(opType == OpType.AND){

        }

        for(LogicExpression logicExpression:getSubExpr()){

        }
    }

    public int calculateCost(Map<String, Integer> costMap) {
        int cost = doCalculateCost(costMap);
        this.cost = cost;
        return cost;
    }

    public int doCalculateCost(Map<String, Integer> costMap) {
        if (getSubExpr() == null || getSubExpr().isEmpty()) {
            return costMap.get(getExpression());
        } else {
            int cost = 0;
            for (LogicExpression le : getSubExpr()) {
                cost += le.calculateCost(costMap);
            }
            return cost;
        }
    }

    public int calculateTruePercent(Map<String, Integer> probMap) {
        int per = doCalculateTruePercent(probMap);
        this.truePercent = per;
        return per;
    }

    private int doCalculateTruePercent(Map<String, Integer> probMap) {
        if (getSubExpr() == null || getSubExpr().isEmpty()) {
            return probMap.get(getExpression());
        } else {
            int per = 100;
            if (opType == OpType.AND) {
                for (LogicExpression le : getSubExpr()) {
                    per *= le.calculateTruePercent(probMap);
                    per = per / 100;
                }
            } else if (opType == OpType.NOT) {
                for (LogicExpression le : getSubExpr()) {
                    per *= le.calculateTruePercent(probMap);
                    per = per / 100;
                }
                return 100 - per;
            } else {
                for (LogicExpression le : getSubExpr()) {
                    //乘以为false的概率
                    per *= (100 - le.calculateTruePercent(probMap));
                    per = per / 100;
                }
                return 100 - per;
            }
            return per;
        }
    }

    public int getCost() {
        return cost;
    }

    private LinkedHashSet<LogicExpression> subExpr = new LinkedHashSet<>();


    public void addSub(LogicExpression logicExpression) {
        subExpr.add(logicExpression);
    }

    public void addAllSub(Collection<LogicExpression> logicExpression) {
        subExpr.addAll(logicExpression);
    }


    public void removeSub(LogicExpression logicExpression) {
        subExpr.remove(logicExpression);
    }

}
