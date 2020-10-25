package me.yic.mpoints.data.caches;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.task.SendMessTaskS;
import me.yic.mpoints.utils.PlayerPoints;
import me.yic.mpoints.utils.RecordData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
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
            }else{
                PlayerPoints pp = new PlayerPoints(u,sign,value);
                playerdata.put(u, pp);
            }
        }
    }

    public static void refreshFromCache(final UUID uuid,String sign) {
        if (uuid != null) {
            DataCon.getBal(uuid,sign);
        }
    }

    public static void cacheUUID(final String u, UUID v) {
        uidcache.put(u, v);
    }

    public static void clearCache() {
        playerdata.clear();
        uidcache.clear();
    }

    public static BigDecimal getBalanceFromCacheOrDB(UUID u, String sign) {
        BigDecimal amount = BigDecimal.ZERO;
        if (playerdata.containsKey(u)) {
            PlayerPoints pp = playerdata.get(u);
            if (pp.containsKey(sign)){
            amount = pp.getpoints(sign);
            }else{
                DataCon.getBal(u,sign);
                if (pp.containsKey(sign)){
                    amount = pp.getpoints(sign);
                }
            }
        } else {
            DataCon.getBal(u,sign);
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
        BigDecimal bal = getBalanceFromCacheOrDB(u,sign);
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
            output.writeUTF(u.toString());
            output.writeUTF(amount.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(MPoints.getInstance(), "mpoints:acb", stream.toByteArray());
    }


    public static void change(UUID u, String sign, BigDecimal amount, Boolean isAdd, String type, String playername, String reason) {
        BigDecimal newvalue = amount;
        BigDecimal bal = getBalanceFromCacheOrDB(u,sign);
        if (isAdd != null) {
            if (isAdd) {
                newvalue = bal.add(amount);
            } else {
                newvalue = bal.subtract(amount);
            }
        }
        insertIntoCache(u, sign, newvalue);
        RecordData x = null;
        if (MPoints.config.getBoolean("Settings.mysql") && MPoints.config.getBoolean("Settings.transaction-record")) {
            x = new RecordData(type, u, playername, sign, newvalue, amount, isAdd, reason);
        }
        if (MPoints.isBungeecord() && PointsCache.getPointFromCache(sign).getenablebc()) {
            sendmessave(u, sign, bal, newvalue, amount, isAdd, x);
        } else {
            DataCon.save(u, sign, bal, amount, isAdd, x);
        }
    }

    public static void changeall(String sign, String targettype, BigDecimal amount, Boolean isAdd, String type, String reason) {
        RecordData x = null;
        playerdata.clear();
        if (MPoints.config.getBoolean("Settings.mysql") && MPoints.config.getBoolean("Settings.transaction-record")) {
            x = new RecordData(type, null, null, sign, BigDecimal.ZERO, amount, isAdd, reason);
        }
        DataCon.saveall(sign, targettype, amount, isAdd, x);
        if (MPoints.isBungeecord() && PointsCache.getPointFromCache(sign).getenablebc()) {
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

    public static UUID translateUUID(String name) {
        if (uidcache.containsKey(name)) {
            return uidcache.get(name);
        } else {
            Player pp = Bukkit.getPlayerExact(name);
            if (pp != null) {
                uidcache.put(name, pp.getUniqueId());
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

    private static void sendmessave(UUID u, String sign, BigDecimal balance, BigDecimal amount, BigDecimal amountc, Boolean isAdd, RecordData x) {
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
        new SendMessTaskS(stream, u, sign, balance, amountc, isAdd, x).runTaskAsynchronously(MPoints.getInstance());
    }

    private static void sendmessaveall(String sign, String targettype, BigDecimal amountc, Boolean isAdd) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF("balanceall");
            output.writeUTF(MPoints.getSign());
            output.writeUTF(sign);
            if (targettype.equals("all")) {
                output.writeUTF("all");
            }else if (targettype.equals("online")) {
                output.writeUTF("online");
            }
            output.writeUTF(amountc.toString());
            if (isAdd) {
                output.writeUTF("add");
            } else {
                output.writeUTF("subtract");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        new SendMessTaskS(stream, null, sign, null, amountc, isAdd, null).runTaskAsynchronously(MPoints.getInstance());
    }

}
