package com.shopee.antifraud.execute.logic.parser;

import com.shopee.antifraud.execute.logic.model.LogicExpression;
import com.shopee.antifraud.execute.logic.model.OpType;

import java.util.ArrayList;
import java.util.List;

public class LogicExprOptimizer {


    public static LogicExpression plainExpr(LogicExpression root) {
        for(LogicExpression le:root.getSubExpr()){
            plaintExpr(le,root);
        }
        return root;
    }

    private static void plaintExpr(LogicExpression node, LogicExpression parent) {
        if(node.getSubExpr()!=null && ! node.getSubExpr().isEmpty()){
            List<LogicExpression> les = new ArrayList<>(node.getSubExpr());
            for (LogicExpression le : les) {
                plaintExpr(le, node);
            }
        }

        if (node.getOpType() == parent.getOpType()) {
            parent.removeSub(node);
            parent.addAllSub(node.getSubExpr());
        }
    }
}
