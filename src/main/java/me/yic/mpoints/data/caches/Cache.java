/*
 *  This file (Cache.java) is a part of project MPoints
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

import me.yic.mpoints.MPoints;
import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.task.SendMessTaskS;
import me.yic.mpoints.utils.PlayerData;
import me.yic.mpoints.utils.PlayerPoints;
import me.yic.mpoints.utils.Points;
import me.yic.mpoints.utils.ServerINFO;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    public static Map<UUID, PlayerPoints> playerdata = new ConcurrentHashMap<>();
    public static Map<String, UUID> uidcache = new HashMap<>();
    public static Map<String, List<UUID>> baltop_uid = new HashMap<>();
    public static Map<String, List<String>> baltop_name = new HashMap<>();
    public static Map<String, BigDecimal> sumbalance = new HashMap<>();

    public static void insertIntoCache(final UUID u, String sign, BigDecimal value) {
        if (value != null) {
            if (playerdata.containsKey(u)) {
                PlayerPoints pp = playerdata.get(u);
                pp.insert(sign, value);
                playerdata.put(u, pp);
            } else {
                PlayerPoints pp = new PlayerPoints(u, sign, value);
                playerdata.put(u, pp);
            }
        }
    }

    public static void insertIntoUUIDCache(final String u, UUID v) {
        uidcache.put(u, v);
    }

    public static void refreshFromCache(final UUID uuid, String sign) {
        if (uuid != null) {
            DataCon.getBal(uuid, sign);
        }
    }

    public static void clearCache() {
        playerdata.clear();
        uidcache.clear();
    }

    public static BigDecimal getBalanceFromCacheOrDB(UUID u, String sign) {
        BigDecimal amount = BigDecimal.ZERO;
        if (playerdata.containsKey(u)) {
            PlayerPoints pp = playerdata.get(u);
            if (pp.containsKey(sign)) {
                amount = pp.getpoints(sign);
            } else {
                DataCon.getBal(u, sign);
                if (pp.containsKey(sign)) {
                    amount = pp.getpoints(sign);
                }
            }
        } else {
            DataCon.getBal(u, sign);
            if (playerdata.containsKey(u)) {
                PlayerPoints pp = playerdata.get(u);
                amount = pp.getpoints(sign);
            }
        }
        if (Bukkit.getOnlinePlayers().size() == 0) {
            clearCache();
        }
        return amount;
    }

    public static void cachecorrection(UUID u, String sign, BigDecimal amount, Boolean isAdd) {
        BigDecimal newvalue;
        BigDecimal bal = getBalanceFromCacheOrDB(u, sign);
        if (isAdd) {
            newvalue = bal.add(amount);
        } else {
            newvalue = bal.subtract(amount);
        }
        insertIntoCache(u, sign, newvalue);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF("balance");
            output.writeUTF(MPoints.getSign());
            output.writeUTF(sign);
            output.writeUTF(u.toString());
            output.writeUTF(amount.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (MPoints.isBungeecord() && Points.getenablebc(sign)) {
            Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(MPoints.getInstance(), "mpoints:acb", stream.toByteArray());
        }
    }


    public static void change(UUID u, String playername, String sign, BigDecimal amount, Boolean isAdd, String type, String reason) {
        BigDecimal newvalue = amount;
        BigDecimal bal = getBalanceFromCacheOrDB(u, sign);
        if (isAdd != null) {
            if (isAdd) {
                newvalue = bal.add(amount);
            } else {
                newvalue = bal.subtract(amount);
            }
        }
        insertIntoCache(u, sign, newvalue);
        PlayerData pd = new PlayerData(type, u, playername, sign, bal, amount, newvalue, isAdd, reason);
        presavedata(u, sign, isAdd, pd);
    }

    private static void presavedata(UUID u, String sign, Boolean isAdd, PlayerData pd) {

        if (!MPoints.isBungeecord() || !Points.getenablebc(sign)) {
            sendmessave(u, sign, isAdd, pd);
        } else {
            if (ServerINFO.RequireAsyncRun) {
                Bukkit.getScheduler().runTaskAsynchronously(MPoints.getInstance(), () ->
                        DataCon.save(u, sign, isAdd, pd));
            } else {
                DataCon.save(u, sign, isAdd, pd);
            }
        }
    }

    public static void changeall(String sign, String targettype, BigDecimal amount, Boolean isAdd, String type, String reason) {
        playerdata.clear();
        PlayerData pd = new PlayerData(type, null, null, sign, null, amount, BigDecimal.ZERO, isAdd, reason);
        DataCon.saveall(sign, targettype, amount, isAdd, pd);
        if (MPoints.isBungeecord() && Points.getenablebc(sign)) {
            sendmessaveall(sign, targettype, amount, isAdd);
        }
    }

    public static void baltop() {
        baltop_uid.clear();
        baltop_name.clear();
        sumbalance.clear();
        DataCon.getTopBal();
    }


    public static BigDecimal sumbal(String sign) {
        return sumbalance.get(sign);
    }

    @SuppressWarnings("ConstantConditions")
    public static Player getplayer(String name) {
        return Bukkit.getPlayer(translateUUID(name, null));
    }

    public static UUID translateUUID(String name, Player pp) {
        if (uidcache.containsKey(name)) {
            return uidcache.get(name);
        } else {
            if (pp != null) {
                insertIntoUUIDCache(name, pp.getUniqueId());
                return uidcache.get(name);
            } else {
                DataCon.getUid(name);
                if (uidcache.containsKey(name)) {
                    return uidcache.get(name);
                }
            }
        }
        return null;
    }

    private static void sendmessave(UUID u, String sign, Boolean isAdd, PlayerData pd) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF("balance");
            output.writeUTF(MPoints.getSign());
            output.writeUTF(sign);
            output.writeUTF(u.toString());
            output.writeUTF(pd.getnewbalance().toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SendMessTask(stream, u, sign, isAdd, pd);
    }

    private static void sendmessaveall(String sign, String targettype, BigDecimal amount, Boolean isAdd) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF("balanceall");
            output.writeUTF(MPoints.getSign());
            output.writeUTF(sign);
            if (targettype.equals("all")) {
                output.writeUTF("all");
            } else if (targettype.equals("online")) {
                output.writeUTF("online");
            }
            output.writeUTF(amount.toString());
            if (isAdd) {
                output.writeUTF("add");
            } else {
                output.writeUTF("subtract");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SendMessTask(stream, null, sign, isAdd, null);
    }



    private static void SendMessTask(ByteArrayOutputStream stream, UUID u, String sign, Boolean isAdd, PlayerData pd) {
        if (ServerINFO.RequireAsyncRun) {
            new SendMessTaskS(stream, u, sign, isAdd, pd).runTaskAsynchronously(MPoints.getInstance());
        } else {
            Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(MPoints.getInstance(), "mpoints:acb", stream.toByteArray());
            if (u != null) {
                DataCon.save(u, sign, isAdd, pd);
            }
        }
    }
}
