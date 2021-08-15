/*
 *  This file (MPoints.java) is a part of project MPoints
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

import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.data.SQL;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.depend.Placeholder;
import me.yic.mpoints.listeners.BSListening;
import me.yic.mpoints.listeners.ConnectionListeners;
import me.yic.mpoints.listeners.SPsync;
import me.yic.mpoints.lang.MessagesManager;
import me.yic.mpoints.task.Baltop;
import me.yic.mpoints.task.Updater;
import me.yic.mpoints.utils.PointsConfig;
import me.yic.mpoints.utils.ServerINFO;
import me.yic.mpoints.utils.UpdateConfig;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.lang.reflect.Field;

public class MPoints extends JavaPlugin {

    private static MPoints instance;
    public static FileConfiguration config;
    private MessagesManager messageManager;
    private PointsConfig pointsconfig;
    private BukkitTask refresherTask = null;
    Metrics metrics = null;
    private Placeholder papiExpansion = null;
    public static Boolean hasbcpoint = false;
    public static CommandMap commandMap = null;

    @SuppressWarnings("ConstantConditions")
    public void onEnable() {
        instance = this;
        load();
        readserverinfo();
        if (checkup()) {
            new Updater().runTaskAsynchronously(this);
        }
        // 检查更新
        messageManager = new MessagesManager(this);
        messageManager.load();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            logger("发现 PlaceholderAPI", null);
            setupPlaceHolderAPI();
        }

        if (Bukkit.getPluginManager().getPlugin("DatabaseDrivers") != null) {
            logger("发现 DatabaseDrivers", null);
            ServerINFO.DDrivers = true;
        }

        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getServer().getPluginManager().registerEvents(new ConnectionListeners(), this);

        metrics = new Metrics(this, 9061);

        Bukkit.getPluginCommand("mpoints").setExecutor(new Commands());

        pointsconfig = new PointsConfig();
        if (!pointsconfig.load()) {
            onDisable();
            return;
        }

        allowHikariConnectionPooling();
        if (!DataCon.create()) {
            onDisable();
            return;
        }

        Cache.baltop();

        loadguidshop();

        if (hasbcpoint) {
            if (isBungeecord()) {
                getServer().getMessenger().registerIncomingPluginChannel(this, "mpoints:aca", new SPsync());
                getServer().getMessenger().registerOutgoingPluginChannel(this, "mpoints:acb");
                logger("已开启BungeeCord同步", null);
            }
        }

        int time = config.getInt("Settings.refresh-time");
        if (time < 30) {
            time = 30;
        }

        refresherTask = new Baltop().runTaskTimerAsynchronously(this, time * 20L, time * 20L);
        logger(null, "===== YiC =====");

    }

    public void onDisable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && papiExpansion != null) {
            try {
                papiExpansion.unregister();
            } catch (NoSuchMethodError ignored) {
            }
        }

        if (isBungeecord()) {
            getServer().getMessenger().unregisterIncomingPluginChannel(this, "mpoints:aca", new SPsync());
            getServer().getMessenger().unregisterOutgoingPluginChannel(this, "mpoints:acb");
        }

        refresherTask.cancel();
        SQL.close();
        logger("MPoints已成功卸载", null);
    }

    public static MPoints getInstance() {
        return instance;
    }

    public void reloadMessages() {
        messageManager.load();
    }

    public boolean reloadPoints() {
        return pointsconfig.load();
    }

    public static void allowHikariConnectionPooling() {
        if (!config.getBoolean("Settings.mysql")) {
            return;
        }
        ServerINFO.EnableConnectionPool = MPoints.config.getBoolean("Pool-Settings.usepool");
    }

    public static String getSign() {
        return config.getString("BungeeCord.sign");
    }

    private void setupPlaceHolderAPI() {
        papiExpansion = new Placeholder(this);
        if (papiExpansion.register()) {
            getLogger().info("PlaceholderAPI successfully hooked");
        } else {
            getLogger().info("PlaceholderAPI unsuccessfully hooked");
        }
    }

    public void readserverinfo() {
        ServerINFO.Lang = config.getString("Settings.language");
        ServerINFO.Sign = config.getString("BungeeCord.sign");

        ServerINFO.RequireAsyncRun = config.getBoolean("Settings.mysql");
    }


    public void logger(String tag, String message) {
        if (tag == null) {
            getLogger().info(message);
        } else {
            if (message == null) {
                getLogger().info(MessagesManager.systemMessage(tag));
            } else {
                if (message.startsWith("<#>")) {
                    getLogger().info(message.substring(3) + MessagesManager.systemMessage(tag));
                } else {
                    getLogger().info(MessagesManager.systemMessage(tag) + message);
                }
            }
        }
    }


    public static boolean isBungeecord() {
        if (!hasbcpoint) {
            return false;
        }

        return config.getBoolean("Settings.mysql");
    }

    public static boolean checkup() {
        return config.getBoolean("Settings.check-update");
    }

    private void load() {
        saveDefaultConfig();
        update_config();
        reloadConfig();
        config = getConfig();
    }

    private void loadguidshop() {
        if (Bukkit.getPluginManager().getPlugin("BossShopPro") != null) {
            getServer().getPluginManager().registerEvents(new BSListening(), this);
        }
    }

    private void update_config() {
        File config = new File(this.getDataFolder(), "config.yml");
        boolean update = UpdateConfig.update(getConfig(), config);
        if (update) {
            saveConfig();
        }
    }
}
