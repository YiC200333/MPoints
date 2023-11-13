/*
 *  This file (MPointsAPI.java) is a part of project MPoints
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
package me.yic.mpoints.api;

import me.yic.mpoints.AdapterManager;
import me.yic.mpoints.MPoints;
import me.yic.mpoints.MPointsLoad;
import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.data.DataLink;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.data.caches.PointsCache;
import me.yic.mpoints.data.syncdata.PlayerData;
import me.yic.mpoints.info.SyncChannalType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@SuppressWarnings("unused")
public class MPointsAPI {

    private final String psign;

    public MPointsAPI(String psign){
        this.psign = psign;
    }

    public static String getversion() {
        return MPoints.PVersion;
    }

    public static SyncChannalType getSyncChannalType() {
        return MPointsLoad.Config.SYNCDATA_TYPE;
    }

    public static Boolean isExistsign(String sign) {
        return PointsCache.CacheContainsKey(sign);
    }

    public static Set<String> getPointsList() {
        return PointsCache.getPointsList();
    }

    public BigDecimal formatdouble(String amount) {
        return PointsCache.getDataFormat(psign).formatString(amount);
    }

    public String getdisplay(BigDecimal balance) {
        return PointsCache.getDataFormat(psign).shown(balance);
    }

    public boolean createPlayerData(UUID uid, String name) {
        return DataLink.newPlayer(uid, name);
    }

    public PlayerData getPlayerData(UUID uid) {
        return DataCon.getPlayerData(psign, uid);
    }

    public PlayerData getPlayerData(String name) {
        return DataCon.getPlayerData(psign, name);
    }

    public boolean ismaxnumber(BigDecimal amount) {
        return PointsCache.getDataFormat(psign).isMAX(amount);
    }


    public int changePlayerBalance(UUID u, String playername, BigDecimal amount, Boolean isadd) {
        return changePlayerBalance(u, playername, amount, isadd, null);
    }

    public int changePlayerBalance(UUID u, String playername, BigDecimal amount, Boolean isadd, String pluginname) {
        if (MPointsLoad.getSyncData_Enable() & AdapterManager.BanModiftyBalance()) {
            return 1;
        }
        BigDecimal bal = getPlayerData(u).getBalance();
        if (isadd != null) {
            if (isadd) {
                if (ismaxnumber(bal.add(amount))) {
                    return 3;
                }
            } else {
                if (bal.compareTo(amount) < 0) {
                    return 2;
                }
            }
        }
        DataCon.changeplayerdata(psign, "PLUGIN_API", u, amount, isadd, pluginname, null);
        return 0;
    }

    public List<String> getbalancetop() {
        return Cache.baltop_papi.get(psign);
    }

    public BigDecimal getsumbalance() {
        return Cache.sumbalance.get(psign);
    }

}
