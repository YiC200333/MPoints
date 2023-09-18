/*
 *  This file (PointsCache.java) is a part of project MPoints
 *  Copyright (C) YiC and contributors
 *
 *  This program is free software: you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 *  Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package me.yic.mpoints.data.caches;

import me.yic.mpoints.data.DataFormat;
import me.yic.mpoints.info.PointsConfig;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PointsCache {

    public static Map<String, PointsConfig> pcs = new ConcurrentHashMap<>();

    public static void insertIntoCache(final String psign, final PointsConfig pc) {
        pcs.put(psign, pc);
    }

    public static boolean CacheContainsKey(final String psign) {
        return pcs.containsKey(psign);
    }
    public static PointsConfig getPoint(final String psign) {
        return pcs.get(psign);
    }

    public static Set<String> getPointsList() {
        return pcs.keySet();
    }

    public static void ClearCache() {
        pcs.clear();
    }

    public static DataFormat getDataFormat(String psign) {
        return pcs.get(psign).DATAFORMAT;
    }

}