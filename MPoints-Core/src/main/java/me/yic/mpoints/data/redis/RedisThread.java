/*
 *  This file (RedisThread.java) is a part of project MPoints
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
package me.yic.mpoints.data.redis;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.utils.RedisConnection;
import redis.clients.jedis.Jedis;

public class RedisThread extends Thread {

    public RedisThread() {
        super("MPointsRedisSub");
    }

    @Override
    public void run() {
        MPoints.getInstance().logger("Redis监听线程创建中", 0, null);
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getResource();
            jedis.subscribe(RedisConnection.subscriber, RedisConnection.channelname);
        } catch (Exception e) {
            MPoints.getInstance().logger(null, 1, "Error during creation");
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                RedisConnection.returnResource(jedis);
            }
        }
    }
}
