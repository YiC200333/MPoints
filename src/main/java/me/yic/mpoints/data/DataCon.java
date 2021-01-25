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

import me.yic.mpoints.MPoints;
import me.yic.mpoints.utils.DatabaseConnection;
import me.yic.mpoints.utils.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class DataCon extends MPoints {

    public static boolean create() {
        if (config.getBoolean("Settings.mysql")) {
            getInstance().logger("数据保存方式", " - MySQL");
            setupMySqlTable();

            if (SQL.con()) {
                SQL.getwaittimeout();
                SQL.createTable();
                getInstance().logger("MySQL连接正常", null);
            } else {
                getInstance().logger("MySQL连接异常", null);
                return false;
            }

        } else {
            getInstance().logger("数据保存方式", " - SQLite");
            setupSqLiteAddress();

            File dataFolder = new File(getInstance().getDataFolder(), "playerdata");
            if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                getInstance().logger("文件夹创建异常", null);
                return false;
            }
            if (SQL.con()) {
                SQL.createTable();
                getInstance().logger("SQLite连接正常", null);
            } else {
                getInstance().logger("SQLite连接异常", null);
                return false;
            }

        }
        getInstance().logger("MPoints加载成功", null);
        return true;
    }

    public static void newPlayer(Player a) {
        SQL.newPlayer(a);
    }

    public static void getBal(UUID u, String sign) {
        SQL.select(u, sign);
    }

    public static void getUid(String name) {
        SQL.selectUID(name);
    }

    public static void getTopBal() {
        SQL.getBaltop();
    }

    public static void setTopBalHide(UUID u, String sign, Integer type) {
        SQL.hidetop(u, sign, type);
    }

    public static void save(UUID UID, String sign, Boolean isAdd, PlayerData pd) {
        SQL.save(UID, sign, isAdd, pd);
    }

    public static void saveall(String sign, String targettype, BigDecimal amount, Boolean isAdd, PlayerData x) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (targettype.equalsIgnoreCase("all")) {
                    SQL.saveall(sign, targettype, null, amount, isAdd, x);
                } else if (targettype.equalsIgnoreCase("online")) {
                    List<UUID> ol = new ArrayList<>();
                    for (Player pp : Bukkit.getOnlinePlayers()) {
                        ol.add(pp.getUniqueId());
                    }
                    SQL.saveall(sign, targettype, ol, amount, isAdd, x);
                }
            }
        }.runTaskAsynchronously(MPoints.getInstance());
    }

    @SuppressWarnings("ConstantConditions")
    private static void setupMySqlTable() {
        if (config.getString("MySQL.table-suffix") != null & !config.getString("MySQL.table-suffix").equals("")) {
            SQL.suffix = config.getString("MySQL.table-suffix").replace("%sign%", getSign()) + "_";
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static void setupSqLiteAddress() {
        if (config.getString("SQLite.path").equalsIgnoreCase("Default")) {
            return;
        }

        File folder = new File(config.getString("SQLite.path"));
        if (folder.exists()) {
            DatabaseConnection.userdata = new File(folder, "data.db");
        } else {
            getInstance().logger("自定义文件夹路径不存在", null);
        }

    }
}
