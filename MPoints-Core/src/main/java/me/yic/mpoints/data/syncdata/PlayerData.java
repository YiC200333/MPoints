/*
 *  This file (PlayerData.java) is a part of project MPoints
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
package me.yic.mpoints.data.syncdata;

import me.yic.mpoints.MPointsLoad;
import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.info.SyncType;

import java.math.BigDecimal;
import java.util.UUID;

public class PlayerData extends SyncData {
    private final String name;
    public final String psign;
    private BigDecimal balance;
    private BigDecimal vbalance;

    public PlayerData(String psign, UUID uuid, String name, BigDecimal balance) {
        super(SyncType.UPDATEPLAYER, uuid);
        this.name = name;
        this.psign = psign;
        this.balance = balance;
    }

    protected PlayerData(SyncType sycn, String psign, UUID uuid, String name, BigDecimal balance) {
        super(sycn, uuid);
        this.name = name;
        this.psign = psign;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }
    public String getPsign() {
        return psign;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setVerifyBalance(BigDecimal balance) {
        this.vbalance = balance;
    }

    @Override
    public void SyncStart() {
        if (!MPointsLoad.Config.DISABLE_CACHE) {
            if (Cache.CacheContainsKey(getUniqueId()) && Cache.getDataFromCache(getUniqueId()).getPoints(this.psign).balance.compareTo(vbalance) != 0) {
                DataCon.deletedatafromcache(getUniqueId());
                return;
            }
        }
        Cache.insertIntoCache(getUniqueId(), psign, this);
    }
}