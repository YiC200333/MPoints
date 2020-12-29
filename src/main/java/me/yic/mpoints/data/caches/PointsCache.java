package me.yic.mpoints.data.caches;

import me.yic.mpoints.utils.Points;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PointsCache {
    public static Map<String, Points> points = new ConcurrentHashMap<>();
    public static List<String> pointsigns = new ArrayList<>();

    public static void insertIntoCache(final String sign, Points p) {
        if (p != null) {
            points.put(sign, p);
        }
    }
    
    public static Points getPointFromCache(String sign) {
        if (points.containsKey(sign)) {
            return points.get(sign);
        }
        return null;
    }

    public static void CleanCache() {
        points.clear();
        pointsigns.clear();
    }
}
