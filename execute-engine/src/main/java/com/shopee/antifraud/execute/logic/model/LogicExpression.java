package com.shopee.antifraud.execute.logic.model;


import lombok.Data;

import java.util.*;


@Data
public class LogicExpression {


    private String expression;

    private OpType opType;

    private List<LogicExpression> subExpr = new ArrayList<>();

    //加权计算成本，单位为微秒
    private int weightCost;

    //为true的百分概率
    private int truePercent;

    public void generatePlan(Map<String, Integer> costMap, Map<String, Integer> probMap) {
        if (isLeaf()) {
            if (costMap.get(getExpression()) != null) {
                this.weightCost = costMap.get(getExpression());
            } else {
                this.weightCost = 1000; //默认成本为1ms
            }
            if (probMap.get(getExpression()) != null) {
                this.truePercent = probMap.get(getExpression());
            } else {
                this.truePercent = 50; // 默认概率为50%
            }
            return;
        }
        for (LogicExpression le : subExpr) {
            le.generatePlan(costMap, probMap);
        }
        if (opType == OpType.NOT) {
            LogicExpression sub = subExpr.get(0);
            this.weightCost = sub.getWeightCost();
            this.truePercent = 100 - sub.truePercent;
        }
        if (opType == OpType.AND) {
            // 确定顺序
            int totalCost = subExpr.stream().map(e -> e.weightCost).reduce((i1, i2) -> i1 + i2).get();
            Collections.sort(subExpr, (s1, s2) -> {
                int s1c = s1.weightCost + s1.truePercent / 100 * totalCost / 2;
                int s2c = s2.weightCost + s2.truePercent / 100 * totalCost / 2;
                return s1c - s2c;
            });
            int weightCost = 0;
            double nextPer = 1;
            int falsePercent = 0;
            for (LogicExpression le : subExpr) {
                int truePer = le.getTruePercent();
                int falsePer = 100 - truePer;
                // 以 left的概率走到这一步
                weightCost += nextPer * le.getWeightCost();

                // 当前表达式为false时整体为false
                falsePercent += nextPer * falsePer;
                //走到下一步的概率
                nextPer = nextPer * (100 - falsePer) / 100.0;
            }
            this.weightCost = weightCost;
            this.truePercent = 100 - falsePercent;
        }
        if (opType == OpType.OR) {

            int totalCost = subExpr.stream().map(e -> e.weightCost).reduce((i1, i2) -> i1 + i2).get();
            Collections.sort(subExpr, (s1, s2) -> {
                int s1c = s1.weightCost + (100 - s1.truePercent) / 100 * totalCost / 2;
                int s2c = s2.weightCost + (100 - s2.truePercent) / 100 * totalCost / 2;
                return s1c - s2c;
            });

            int weightCost = 0;
            double nextPer = 1;
            int truePercent = 0;
            for (LogicExpression le : subExpr) {
                int truePer = le.getTruePercent();
                // 以 nextPer的概率走到这一步
                weightCost += nextPer * le.getWeightCost();
                // 当前表达式为true时整体true
                truePercent += nextPer * truePer;
                //走到下一步的概率
                nextPer = nextPer * (100 - truePer) / 100.0;
            }
            this.weightCost = weightCost;
            this.truePercent = truePercent;
        }
    }


    public boolean calculate(FeatureCalculator calculator) {
        if (isLeaf()) {
            return calculator.calculate(getExpression());
        }

        if (opType == OpType.NOT) {
            return !subExpr.get(0).calculate(calculator);
        }

        if (opType == OpType.AND) {
            for (LogicExpression le : getSubExpr()) {
                boolean value = le.calculate(calculator);
                if (value == false) {
                    return false;
                }
            }
            return true;
        }

        if (opType == OpType.OR) {
            for (LogicExpression le : getSubExpr()) {
                boolean value = le.calculate(calculator);
                if (value) {
                    return true;
                }
            }
            return false;
        }
        throw new IllegalArgumentException("optype illegal");
    }

    boolean isLeaf() {
        return subExpr == null || subExpr.isEmpty();
    }

    public int getWeightCost() {
        return weightCost;
    }


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
