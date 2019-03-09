package cn.larry.argorithm;

import java.util.*;

/**
 * 动态规划解跳一跳问题 （一个面试题）
 * <p>
 * 有若干个方块，每个方块有体力包，可能加体力也可能减体力，跳消耗的体力等于距离的平方
 * 给定初始体力 s 和体力分布 powers ，写程序求是否能到达终点
 */
public class DynamicPrograming {

    public static void main(String[] args) {
        int[] powers = new int[]{1, -10, 1};
        int start = 5;
        JumpStrategy strategy = jump(powers, start);
        System.out.println(strategy);
    }

    /**
     * @param powers     方块的体力分布
     * @param startPower 初始体力
     * @return 如果无法到达终点则返回null 否则返回跳的方案 JumpStrategy
     */
    public static JumpStrategy jump(int[] powers, int startPower) {
        if (startPower <= 0) {
            return null;
        }

        Map<Integer /*要跳到的点*/, JumpStrategy /*跳到该点的最佳方案 */> pointStrategy = new HashMap<>();

        //特殊点 -1 表示起始点， 0表示跳的第一个点
        pointStrategy.put(-1, new JumpStrategy(new ArrayList<Integer>(), startPower));

        for (int i = 0; i < powers.length; i++) { //求到点i的跳跃方案
            // 动态规划，先跳到点j，再跳到点i,
            for (int j = i - 1; j >= -1; j--) {
                JumpStrategy strategy = pointStrategy.get(j);
                if (strategy != null) { //有方案跳到点j
                    int cost = (i - j) * (i - j);
                    if (strategy.leftPower >= cost) {  //能否直接跳到点i,   为什么是直接从点j跳点i，因为从点j跳到中间点k再到点i的方案已经被包含在j=k时的方案里了
                        List<Integer> points = new ArrayList<>(strategy.getJumpPoints());
                        points.add(i);
                        JumpStrategy strategyi = new JumpStrategy(points, strategy.getLeftPower() - cost + powers[i]);

                        if (pointStrategy.get(i) == null || strategyi.leftPower > pointStrategy.get(i).getLeftPower()) {
                            pointStrategy.put(i, strategyi);
                        }
                    }
                }
            }
        }
        // powers.length - 1 表示终点
        return pointStrategy.get(powers.length - 1);
    }


    /**
     * 跳跃策略
     */
    static class JumpStrategy {

        public JumpStrategy(List<Integer> jumpPoints, int finalPower) {
            this.jumpPoints = jumpPoints;
            this.leftPower = finalPower;
        }


        List<Integer> jumpPoints;
        int leftPower;

        public List<Integer> getJumpPoints() {
            return jumpPoints;
        }

        public void setJumpPoints(List<Integer> jumpPoints) {
            this.jumpPoints = jumpPoints;
        }

        public int getLeftPower() {
            return leftPower;
        }

        public void setLeftPower(int leftPower) {
            this.leftPower = leftPower;
        }
    }
}
