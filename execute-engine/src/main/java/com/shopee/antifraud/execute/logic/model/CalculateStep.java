package com.shopee.antifraud.execute.logic.model;

public class CalculateStep {

    private String var;

    private boolean breakValue;

    private boolean finalValue;


    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public boolean isBreakValue() {
        return breakValue;
    }

    public void setBreakValue(boolean breakValue) {
        this.breakValue = breakValue;
    }

    public boolean isFinalValue() {
        return finalValue;
    }

    public void setFinalValue(boolean finalValue) {
        this.finalValue = finalValue;
    }
}
