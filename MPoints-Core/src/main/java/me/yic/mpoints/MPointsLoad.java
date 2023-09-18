/*
 *  This file (MPointsLoad.java) is a part of project MPoints
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
package me.yic.mpoints;

import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.data.caches.PointsCache;
import me.yic.mpoints.data.sql.SQL;
import me.yic.mpoints.info.DataBaseConfig;
import me.yic.mpoints.info.DefaultConfig;
import me.yic.mpoints.info.PointsConfig;
import me.yic.mpoints.info.SyncChannalType;
import me.yic.mpoints.lang.MessagesManager;
import me.yic.mpoints.utils.RedisConnection;

public class MPointsLoad{
    public static boolean DDrivers = false;

    public static DataBaseConfig DConfig;
    public static DefaultConfig Config;

    public static void LoadConfig(){

        Config = new DefaultConfig();
        DConfig = new DataBaseConfig();

        MessagesManager.loadsysmess();
        MessagesManager.loadlangmess();

        DConfig.Initialization();
        Config.setSyncData();

        for (String kk : PointsConfig.config.getConfigurationSection("")){
            PointsCache.insertIntoCache(kk, new PointsConfig(kk));
        }
    }

    public static void Initial(){
        DataCon.baltop();

        if (Config.SYNCDATA_TYPE.equals(SyncChannalType.BUNGEECORD)) {
            if ((DConfig.getStorageType() == 0 || DConfig.getStorageType() == 1)
                    && (DConfig.gethost().equalsIgnoreCase("Default"))) {
                MPoints.getInstance().logger("SQLite文件路径设置错误", 1, null);
                MPoints.getInstance().logger("BungeeCord同步未开启", 1, null);
                Config.SYNCDATA_TYPE = SyncChannalType.OFF;
            } else {
                AdapterManager.PLUGIN.registerIncomingPluginChannel("mpoints:aca", "me.yic.mpoints.listeners.SPsync");
                AdapterManager.PLUGIN.registerOutgoingPluginChannel("mpoints:acb");
                MPoints.getInstance().logger("已开启BungeeCord同步", 0, null);
            }
        }

        //DataFormat.load();
    }

    public static void Unload() {
        if (Config.SYNCDATA_TYPE.equals(SyncChannalType.BUNGEECORD)) {
            AdapterManager.PLUGIN.unregisterIncomingPluginChannel("mpoints:aca", "me.yic.mpoints.listeners.SPsync");
            AdapterManager.PLUGIN.unregisterOutgoingPluginChannel("mpoints:acb");
        }else if(Config.SYNCDATA_TYPE.equals(SyncChannalType.REDIS)) {
            RedisConnection.close();
        }
        SQL.close();
    }


    public static boolean getSyncData_Enable(){
        return !Config.SYNCDATA_TYPE.equals(SyncChannalType.OFF);
    }

}
