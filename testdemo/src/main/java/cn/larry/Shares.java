package cn.larry;

public class Shares {

    public static void main(String[] args) {
        int[] prices = new int[]{3,1,5,2,4,7};
        int profit = maxProfit(prices);
        System.out.println(profit);
    }


    private static int maxProfit(int[] prices){
        int total = 0;
        int pre  = prices[0];
        int preMin = prices[0];
        for(int i = 1;i<prices.length;i++){
            int n = prices[i];
            //当前值小于前值，且可买卖
            if(n<pre && pre!=preMin){
                total += pre -preMin;
                preMin = n;
                pre = n;
                continue;
            }
            if(n<preMin) preMin = n;
            pre = n;
            if(i == prices.length-1 && n> preMin){
                total += n-preMin;
            }
        }
        return total;
    }
}
