/*
 *  This file (DataCon.java) is a part of project MPoints
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
import me.yic.mpoints.adapter.comp.CallAPI;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.data.redis.RedisPublisher;
import me.yic.mpoints.data.syncdata.PlayerData;
import me.yic.mpoints.data.syncdata.SyncBalanceAll;
import me.yic.mpoints.data.syncdata.SyncData;
import me.yic.mpoints.data.syncdata.SyncDelData;
import me.yic.mpoints.info.MessageConfig;
import me.yic.mpoints.info.RecordInfo;
import me.yic.mpoints.info.SyncChannalType;
import me.yic.mpoints.utils.SendPluginMessage;

import java.math.BigDecimal;
import java.util.UUID;

public class DataCon {

    public static PlayerData getPlayerData(String psign, UUID uuid) {
        return getPlayerDatai(psign, uuid);
    }

    public static PlayerData getPlayerData(String psign, String username) {
        return getPlayerDatai(psign, username);
    }

    private static <T> PlayerData getPlayerDatai(String psign, T u) {
        PlayerData pd = null;

        if (MPointsLoad.Config.DISABLE_CACHE) {
            return DataLink.getPlayerData(psign, u);
        }

        if (Cache.CacheContainsKey(u)) {
            pd = Cache.getDataFromCache(u).getPoints(psign);
        }
        if (pd == null){
            pd = DataLink.getPlayerData(psign, u);
        }
        if (AdapterManager.PLUGIN.getOnlinePlayersisEmpty()) {
            Cache.clearCache();
        }
        return pd;
    }

    public static void deletePlayerData(String psign, PlayerData pd) {
        DataLink.deletePlayerData(pd.getUniqueId(), psign);
        Cache.removefromCache(pd.getUniqueId());

        if (!(pd instanceof SyncDelData) && MPointsLoad.getSyncData_Enable()) {
            SendMessTask(new SyncDelData(psign, pd));
        }

        CPlayer cp = AdapterManager.PLUGIN.getplayer(pd);
        if(cp.isOnline()){
            cp.kickPlayer("[MPoints] " + AdapterManager.translateColorCodes(MessageConfig.DELETE_DATA));
        }
    }

    public static void deletedatafromcache(UUID u) {
        Cache.deleteDataFromCache(u);
    }

    public static void changeplayerdata(final String psign, final String type, final UUID uid, final BigDecimal amount, final Boolean isAdd, final String command, final Object comment) {
        PlayerData pd = getPlayerData(psign, uid);
        UUID u = pd.getUniqueId();
        BigDecimal newvalue = amount;
        BigDecimal bal = pd.getBalance();

        RecordInfo ri = new RecordInfo(type, command, comment);

        CallAPI.CallPlayerAccountEvent(psign, u, pd.getName(), bal, amount, isAdd, ri);

        if (isAdd != null) {
            if (isAdd) {
                newvalue = bal.add(amount);
            } else {
                newvalue = bal.subtract(amount);
            }
        }

        Cache.updateIntoCache(u, pd, newvalue, bal);

        if (MPointsLoad.DConfig.canasync && AdapterManager.checkisMainThread()) {
            AdapterManager.runTaskAsynchronously(() -> {
                DataLink.save(psign, pd, isAdd, amount, ri);
                if (MPointsLoad.getSyncData_Enable()) {
                    SendMessTask(pd);
                }
            });
        } else {
            DataLink.save(psign, pd, isAdd, amount, ri);
            if (MPointsLoad.getSyncData_Enable()) {
                SendMessTask(pd);
            }
        }

    }


    public static void changeallplayerdata(String psign, String targettype, String type, BigDecimal amount, Boolean isAdd, String command, StringBuilder comment) {
        Cache.clearCache();

        RecordInfo ri = new RecordInfo(type, command, comment);

        if (MPointsLoad.DConfig.canasync && AdapterManager.checkisMainThread()) {
            AdapterManager.runTaskAsynchronously(() -> DataLink.saveall(psign, targettype, amount, isAdd, ri));
        } else {
            DataLink.saveall(psign, targettype, amount, isAdd, ri);
        }

        boolean isallbool = targettype.equals("all");
        //if (targettype.equals("all")) {
        //} else

        if (MPointsLoad.getSyncData_Enable()) {
            SendMessTask(new SyncBalanceAll(psign, isallbool, isAdd, amount));
        }
    }

    public static void baltop() {
        Cache.baltop.clear();
        Cache.baltop_papi.clear();
        DataLink.getTopBal();
    }


    public static void sumbal(String psign) {
        Cache.sumbalance.get(psign);
    }


    public static void SendMessTask(SyncData pd) {
        if (MPointsLoad.Config.SYNCDATA_TYPE.equals(SyncChannalType.REDIS)) {
            RedisPublisher.publishmessage(pd.toByteArray(MPoints.syncversion).toByteArray());
        }else if (MPointsLoad.Config.SYNCDATA_TYPE.equals(SyncChannalType.BUNGEECORD)) {
            SendPluginMessage.SendMessTask("mpoints:acb", pd);
        }
    }

}
