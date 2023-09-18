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

import me.yic.mpoints.MPointsLoad;
import me.yic.mpoints.data.GetUUID;
import me.yic.mpoints.data.PlayerPoints;
import me.yic.mpoints.data.syncdata.PlayerData;
import me.yic.mpoints.utils.UUIDMode;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

    public static ConcurrentHashMap<UUID, PlayerPoints> pds = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, UUID> uuids = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<UUID, UUID> m_uuids = new ConcurrentHashMap<>();

    public static HashMap<String, LinkedHashMap<String, BigDecimal>> baltop = new HashMap<>();
    public static HashMap<String, List<String>> baltop_papi = new HashMap<>();
    public static ConcurrentHashMap<String, BigDecimal> sumbalance = new ConcurrentHashMap<>();

    public static void insertIntoCache(final UUID uuid, final String psign, final PlayerData pd) {
        if (pd != null) {
            if (pd.getName() != null && pd.getBalance() != null) {
                PlayerPoints pp;
                if (pds.containsKey(uuid)){
                    pp = pds.get(uuid);
                    pp.insert(psign, pd);
                }else{
                    pp = new PlayerPoints(uuid, pd.getName(), psign, pd);
                }
                pds.put(uuid, pp);
                if (MPointsLoad.Config.USERNAME_IGNORE_CASE) {
                    uuids.put(pp.getName().toLowerCase(), uuid);
                } else {
                    uuids.put(pp.getName(), uuid);
                }
            }
        }
    }

    public static void insertIntoMultiUUIDCache(final UUID uuid, final UUID luuid) {
        if (uuid != null && luuid != null && !uuid.toString().equals(luuid.toString())) {
            m_uuids.put(luuid, uuid);
        }
    }

    public static <T> boolean CacheContainsKey(final T key) {
        if (key instanceof UUID) {
            if (pds.containsKey((UUID) key)){
                return true;
            }
            if (MPointsLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                UUID luuid = getMultiUUIDCache((UUID) key);
                if (luuid == null) {
                    return false;
                }else {
                    return pds.containsKey(luuid);
                }
            }
            return false;
        }
        if (MPointsLoad.Config.USERNAME_IGNORE_CASE) {
            return uuids.containsKey(((String) key).toLowerCase());
        }
        return uuids.containsKey((String) key);
    }


    public static <T> PlayerPoints getDataFromCache(final T key) {
        UUID u;
        if (key instanceof UUID) {
            u = (UUID) key;
            if (MPointsLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                if (!pds.containsKey((UUID) key) && getMultiUUIDCache((UUID) key) != null){
                    u = getMultiUUIDCache((UUID) key);
                }
            }
        } else {
            if (MPointsLoad.Config.USERNAME_IGNORE_CASE) {
                u = uuids.get(((String) key).toLowerCase());
            } else {
                u = uuids.get(((String) key));
            }
        }
        return pds.get(u);
    }

    public static void deleteDataFromCache(final UUID key) {
        if (CacheContainsKey(key)) {
            pds.remove(key);
        }
    }

    public static UUID getMultiUUIDCache(final UUID luuid) {
        if (m_uuids.containsKey(luuid)){
            return m_uuids.get(luuid);
        }
        return null;
    }

    public static void updateIntoCache(final UUID uuid, final PlayerData pd, final BigDecimal newbalance, final BigDecimal vbalance) {
        pd.setBalance(newbalance);
        pd.setVerifyBalance(vbalance);
        PlayerPoints pps;
        if (pds.containsKey(uuid)) {
            pps = new PlayerPoints(uuid, pd.getName(), pd.getPsign(), pd);
        }else{
            pps = pds.get(uuid);
            pps.insert(pd.getPsign(), pd);
        }
        pds.put(uuid, pps);
    }

    @SuppressWarnings("all")
    public static void removefromCache(final UUID uuid) {
        if (pds.containsKey(uuid)) {
            String name = pds.get(uuid).getName();
            pds.remove(uuid);
            uuids.remove(name);
        }
    }


    @SuppressWarnings("all")
    public static void syncOnlineUUIDCache(final String oldname, final String newname, final UUID uuid) {
        if (uuids.containsKey(newname)) {
            UUID u = uuids.get(newname);
            pds.remove(u);
            uuids.remove(newname);
        }
        GetUUID.removeUUIDFromCache(oldname);
        GetUUID.removeUUIDFromCache(newname);
        removefromCache(uuid);
    }


    public static void clearCache() {
        pds.clear();
        uuids.clear();
        m_uuids.clear();
    }


}