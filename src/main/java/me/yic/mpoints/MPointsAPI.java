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
package me.yic.mpoints;

import me.yic.mpoints.data.DataFormat;
import me.yic.mpoints.data.caches.Cache;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class MPointsAPI {

    public String getversion() {
        return MPoints.getInstance().getDescription().getVersion();
    }

    public Boolean isbungeecordmode() {
        return MPoints.isBungeecord();
    }

    public UUID translateUUID(String playername) {
        return Cache.translateUUID(playername, null);
    }

    public BigDecimal formatdouble(String sign, String amount) {
        return DataFormat.formatString(sign, amount);
    }

    public String getdisplay(String sign, BigDecimal balance) {
        return DataFormat.shown(sign, balance);
    }

    public BigDecimal getbalance(String sign, UUID uid) {
        return Cache.getBalanceFromCacheOrDB(uid, sign);
    }

    public Boolean ismaxnumber(String sign, BigDecimal amount) {
        return DataFormat.isMAX(sign, amount);
    }

    public Integer changebalance(String sign, UUID u, String playername, BigDecimal amount, Boolean isadd) {
        if (MPoints.isBungeecord() & Bukkit.getOnlinePlayers().isEmpty()) {
            return 1;
        }
        BigDecimal bal = getbalance(sign, u);
        if (isadd) {
            if (ismaxnumber(sign, bal.add(amount))) {
                return 3;
            }
        } else {
            if (bal.compareTo(amount) < 0) {
                return 2;
            }
        }
        Cache.change(u, playername, sign, amount, isadd, "PLUGIN_API", "N/A");
        return 0;
    }

    public List<String> getbalancetop(String sign) {
        return Cache.baltop_name.get(sign);
    }

    public BigDecimal getsumbalance(String sign) {
        return Cache.sumbal(sign);
    }
}
