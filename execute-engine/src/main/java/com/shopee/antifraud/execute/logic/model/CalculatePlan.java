package com.shopee.antifraud.execute.logic.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CalculatePlan  {

    List<CalculateStep> steps;

    int weightCost;


    int truePercent;

    int trueCost;

    int falseCost;

}
