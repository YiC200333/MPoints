/*
 *  This file (PlayerPoints.java) is a part of project MPoints
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
package me.yic.mpoints.data;

import me.yic.mpoints.data.syncdata.PlayerData;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerPoints {
    private final UUID uid;
    private final String name;
    private final ConcurrentHashMap<String, PlayerData> points = new ConcurrentHashMap<>();

    public PlayerPoints(UUID uid, String name, String sign, PlayerData value) {
        this.uid = uid;
        this.name = name;
        this.points.put(sign, value);
    }

    public UUID getUUID() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public PlayerData getPoints(String sign) {
        return points.get(sign);
    }

    public boolean containsKey(String sign) {
        return points.containsKey(sign);
    }

    public void insert(final String sign, PlayerData value) {
        if (value != null) {
            points.put(sign, value);
        }
    }
}
