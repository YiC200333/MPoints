/*
 *  This file (DefaultConfig.java) is a part of project MPoints
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
package me.yic.mpoints.info;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.adapter.comp.CConfig;
import me.yic.mpoints.utils.UUIDMode;

public class DefaultConfig {
    public static CConfig config;

    public DefaultConfig() {
        setUUIDMode();
    }
    public boolean ISOLDCONFIG = false;

    public UUIDMode UUIDMODE = UUIDMode.DEFAULT;
    public String LANGUAGE = config.getString("Settings.language");
    public boolean CHECK_UPDATE = config.getBoolean("Settings.check-update");
    public int REFRESH_TIME = Math.max(config.getInt("Settings.refresh-time"), 30);
    public int RANKING_SIZE = getrankingsize();
    public int LINES_PER_PAGE = config.getInt("Settings.lines-per-page");
    public boolean DISABLE_CACHE = config.getBoolean("Settings.disable-cache");

    public boolean TRANSACTION_RECORD = config.getBoolean("Settings.transaction-record");
    public boolean USERNAME_IGNORE_CASE = config.getBoolean("Settings.username-ignore-case");

    public SyncChannalType SYNCDATA_TYPE = SyncChannalType.OFF;

    public String SYNCDATA_SIGN = config.getString("SyncData.sign");

    //==================================================
    public String RE_WORLD = config.getString("Region-Thread.world");
    public int RE_X = config.getInt("Region-Thread.range-x");
    public int RE_Y = config.getInt("Region-Thread.range-y");

    private int getrankingsize() {
        return Math.min(config.getInt("Settings.ranking-size"), 100);
    }


    private void setUUIDMode() {
        if (config.getString("UUID-mode").equalsIgnoreCase("Online")) {
            UUIDMODE = UUIDMode.ONLINE;
        } else if (config.getString("UUID-mode").equalsIgnoreCase("Offline")) {
            UUIDMODE = UUIDMode.OFFLINE;
            USERNAME_IGNORE_CASE = false;
        } else if (config.getString("UUID-mode").equalsIgnoreCase("SemiOnline")) {
            UUIDMODE = UUIDMode.SEMIONLINE;
        }
        MPoints.getInstance().logger(null, 0, UUIDMODE.toString());
    }


    public void setSyncData() {
        if (!config.contains("SyncData.enable")) {
            ISOLDCONFIG = true;
            return;
        }

        if (config.getBoolean("SyncData.enable")) {
            String channeltype = config.getString("SyncData.channel-type");
            if (channeltype.equalsIgnoreCase("BungeeCord")) {
                SYNCDATA_TYPE = SyncChannalType.BUNGEECORD;
            }else if (channeltype.equalsIgnoreCase("Redis")) {
                SYNCDATA_TYPE = SyncChannalType.REDIS;
            }
        }
    }

}
