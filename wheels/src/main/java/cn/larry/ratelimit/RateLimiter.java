package cn.larry.ratelimit;

import redis.clients.jedis.Jedis;

import java.util.List;

public class RateLimiter {

    private Jedis jedis;
    private String limitName;
    private int maxQps;

    private int curLimit;
    private int minQps;
    private int timeoutMs;

    private long qpsLastUpdateTime;

    private int period_length = 10_000;

    public RateLimiter(Jedis jedis, String limitName, int maxQps, int minQps, int timeoutMs) {
        this.jedis = jedis;
        this.limitName = limitName;
        this.maxQps = maxQps;
        this.minQps = minQps;
        this.curLimit = maxQps;
        this.timeoutMs = timeoutMs;
    }

    public String getQpsLimitKey() {
        return String.format("qps_limit_{%s}", limitName);
    }

    public String getTimeoutKey(long period) {
        return String.format("timeout_record_{%s}_%d", limitName, period);
    }

    public String getCurLimitKey() {
        return String.format("cur_limit_{%s}", limitName);
    }

    public String getLimitUpdateKey() {
        return String.format("cur_limit_{%s}", limitName);
    }

    private String getRandomString(int len) {
        return null;
    }

    private long getPeriod(long time) {
        return (time / period_length);
    }

    public boolean getToken() {
        long time = System.currentTimeMillis();
        String key = getQpsLimitKey();
        long start = (time / 1000) * 1000;
        long end = start + 1000;
        long count = jedis.zcount(key, start, end);
        if (count > curLimit) {
            return false;
        }
        String member = getRandomString(10);
        jedis.zadd(key, time, member);
        return true;
    }

    //记录请求耗时
    public void recordTime(long start) {
        try {
            long cur = System.currentTimeMillis();
            long cost = cur - start;
            if (cost >= timeoutMs) {
                long timeoutValue = cost - timeoutMs;
                //超时记录到发起请求时的那个区间
                long period = getPeriod(start);
                String key = getTimeoutKey(period);
                jedis.incrBy(key, timeoutValue);
            }
            updateQpsLimit(cost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateQpsLimit(long time) {
        long cur = System.currentTimeMillis();
        long pos = (cur / 1000) % 10;
        //10s一个计算周期，后半周期才计算
        if (pos <= 5) {
            return;
        }
        //本地10s内更新过，不再重复计算
        if (cur - qpsLastUpdateTime < 5000) {
            return;
        }
        String curlimitKey = getCurLimitKey();
        String updateKey = getLimitUpdateKey();
        List<String> result = jedis.mget(curlimitKey, updateKey);
        if (result.size() == 2) {
            String remoteCurLimitStr = result.get(0);
            String remoteUpdate = result.get(1);
            if (remoteCurLimitStr != null && remoteUpdate != null) {
                int remoteCurLimit = Integer.parseInt(result.get(0));
                long lastUpdate = Long.parseLong(result.get(1));
                //远程最近5s内更新过,不再重复计算
                if (cur - lastUpdate < 5000) {
                    this.curLimit = remoteCurLimit;
                    this.qpsLastUpdateTime = lastUpdate;
                    return;
                }
            }
        }
        if (time > timeoutMs) {
            decraseQpsLimit();
        }
        if (time < timeoutMs / 2) {
            increaseQpsLimit();
        }
    }

    private void decraseQpsLimit() {
        if (curLimit == minQps) {
            return;
        }
        long cur = System.currentTimeMillis();
        long prePeriod = getPeriod(cur) - 1;
        //取前两个区间计算超时程度，当前区间超时请求可能还没有返回
        String preKey = getTimeoutKey(prePeriod);
        String prePreKey = getTimeoutKey(prePeriod - 1);
        List<String> totalTimeout = jedis.mget(preKey, prePreKey);
        String preTotalTimeout = totalTimeout.get(0);
        String prePreTotalTimeout = totalTimeout.get(1);
        long preTotal = preTotalTimeout == null ? 0 : Long.parseLong(preTotalTimeout);
        long prePreTotal = prePreTotalTimeout == null ? 0 : Long.parseLong(prePreTotalTimeout);
        if (preTotal + prePreTotal == 0) {
            return;
        }
        String qpsKey = getQpsLimitKey();
        long totalReq = jedis.zcount(qpsKey, (prePeriod - 1) * 10_000, prePeriod * 10_000 + 10000);
        if (totalReq <= 0) {
            return;
        }
        long timeoutAvg = (preTotal + prePreTotal) / totalReq;
        if (timeoutAvg <= 0) {
            return;
        }
        double overloadRate = timeoutAvg / (double) timeoutMs;
        int newLimit = (int) (curLimit / (curLimit + curLimit * overloadRate));
        if (newLimit < minQps) {
            newLimit = minQps;
        }
        setNewLimit(newLimit, cur);
    }

    private void increaseQpsLimit() {
        if (curLimit == maxQps) {
            return;
        }
        long cur = System.currentTimeMillis();
        long period = getPeriod(cur);
        String curTimeoutSumKey = getTimeoutKey(period);
        String preTimeoutSumKey = getTimeoutKey(period - 1);
        String curTimeoutSum = jedis.get(curTimeoutSumKey);
        String preTimeoutSum = jedis.get(preTimeoutSumKey);
        long curSum = 0;
        long preSum = 0;
        if (curTimeoutSum != null) {
            curSum = Long.parseLong(curTimeoutSum);
        }
        if (preTimeoutSum != null) {
            preSum = Long.parseLong(preTimeoutSum);
        }
        if (curSum + preSum > 0) {
            return;
        }
        int increase = maxQps / 10;
        int newLimit = Math.max(maxQps, curLimit + increase);
        setNewLimit(newLimit, cur);
    }

    private void setNewLimit(int newLimit, long time) {
        String curlimitKey = getCurLimitKey();
        String updateKey = getLimitUpdateKey();
        this.curLimit = newLimit;
        this.qpsLastUpdateTime = time;
        jedis.mset(curlimitKey, String.valueOf(newLimit), updateKey, String.valueOf(time));
    }
}
