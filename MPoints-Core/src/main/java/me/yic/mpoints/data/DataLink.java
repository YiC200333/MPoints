/*
 *  This file (DataLink.java) is a part of project MPoints
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

import me.yic.mpoints.AdapterManager;
import me.yic.mpoints.MPoints;
import me.yic.mpoints.MPointsLoad;
import me.yic.mpoints.adapter.comp.CPlayer;
import me.yic.mpoints.data.redis.RedisThread;
import me.yic.mpoints.data.sql.SQL;
import me.yic.mpoints.data.sql.SQLCreateNewAccount;
import me.yic.mpoints.data.sql.SQLSetup;
import me.yic.mpoints.data.syncdata.PlayerData;
import me.yic.mpoints.info.RecordInfo;
import me.yic.mpoints.info.SyncChannalType;
import me.yic.mpoints.utils.RedisConnection;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("unused")
public class DataLink{
    public static boolean hasnonplayerplugin = false;

    public static boolean create() {
        switch (MPointsLoad.DConfig.getStorageType()) {
            case 1:
/*                MPoints.getInstance().logger("数据保存方式", 0, " - SQLite");
                SQLSetup.setupSqLiteAddress();

                File dataFolder = MPoints.getInstance().getPDataFolder();
                if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                    MPoints.getInstance().logger("文件夹创建异常", 1, null);
                    return false;
                }
                break;*/
                MPoints.getInstance().logger(null, 1, "Please complete the database.yml configuration");
                return false;

            case 2:
                MPoints.getInstance().logger("数据保存方式", 0, " - MySQL");
                SQLSetup.setupMySqlTable();
                break;

            case 3:
                MPoints.getInstance().logger("数据保存方式", 0, " - MariaDB");
                SQLSetup.setupMySqlTable();
                break;

        }

        if (SQL.con()) {
            if (MPointsLoad.DConfig.getStorageType() == 2 || MPointsLoad.DConfig.getStorageType() == 3) {
                SQL.getwaittimeout();
            }
            SQL.createTable();
            MPointsLoad.DConfig.loggersysmess("连接正常");
        } else {
            MPointsLoad.DConfig.loggersysmess("连接异常");
            return false;
        }


        if (MPointsLoad.Config.SYNCDATA_TYPE.equals(SyncChannalType.REDIS)) {
            if (RedisConnection.connectredis()) {
                RedisThread rThread = new RedisThread();
                rThread.start();
            } else {
                return false;
            }
        }

        MPoints.getInstance().logger("MPoints加载成功", 0, null);
        return true;
    }


    public static void deletePlayerData(UUID u, String psign) {
        SQL.deletePlayerData(u.toString(), psign);
    }

    public static void getTopBal() {
        SQL.getBaltop();
    }

    public static void setTopBalHide(String psign, UUID u, int type) {
        SQL.hidetop(psign, u, type);
    }

    public static String getBalSum(String psign) {
        if (SQL.sumBal(psign) == null) {
            return "0.0";
        }
        return SQL.sumBal(psign);
    }

    public static void save(String psign, PlayerData pd, Boolean isAdd, BigDecimal amount, RecordInfo ri) {
        SQL.save(psign, pd, isAdd, amount, ri);
    }

    public static void newPlayer(CPlayer a) {
        SQLCreateNewAccount.newPlayer(a);
    }

    public static boolean newPlayer(UUID uid, String name) {
        return SQLCreateNewAccount.newPlayer(uid, name, null);
    }

    public static <T> PlayerData getPlayerData(String psign, T key) {
        if (AdapterManager.checkisMainThread()) {
            try {
                return CompletableFuture.supplyAsync(() -> exgetPlayerData(psign, key)).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }else{
            return exgetPlayerData(psign, key);
        }
    }

    private static <T> PlayerData exgetPlayerData(String psign, T key) {
        if (key instanceof UUID) {
            return SQL.getPlayerData((UUID) key, psign);
        } else if (key instanceof String) {
            return SQL.getPlayerData((String) key, psign);
        }
        return null;
    }

    public static void saveall(String psign, String targettype, BigDecimal amount, Boolean isAdd, RecordInfo ri) {
        AdapterManager.runTaskAsynchronously(() -> {
            if (targettype.equalsIgnoreCase("all")) {
                SQL.saveall(psign, targettype, null, amount, isAdd, ri);
            } else if (targettype.equalsIgnoreCase("online")) {
                List<UUID> ol = AdapterManager.PLUGIN.getOnlinePlayersUUIDs();
                SQL.saveall(psign, targettype, ol, amount, isAdd, ri);
            }
        });
    }

}
