/*
 *  This file (ConnectionListeners.java) is a part of project MPoints
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
package me.yic.mpoints.listeners;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.lang.MessagesManager;
import me.yic.mpoints.task.Updater;
import me.yic.mpoints.utils.ServerINFO;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ConnectionListeners implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (Bukkit.getOnlinePlayers().size() == 1) {
            Cache.clearCache();
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player a = event.getPlayer();

        if (ServerINFO.RequireAsyncRun) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    DataCon.newPlayer(a);
                }
            }.runTaskAsynchronously(MPoints.getInstance());
            Cache.translateUUID(a.getName(), a);
        } else {
            DataCon.newPlayer(a);
        }

        if (a.isOp()) {
            notifyUpdate(a);
        }
    }


    private void notifyUpdate(Player player) {
        if (!(MPoints.checkup() & Updater.old)) {
            return;
        }
        player.sendMessage("§f[MPoints]§b" + MessagesManager.systemMessage("发现新版本 ") + Updater.newVersion);
        player.sendMessage("§f[MPoints]§ahttps://www.spigotmc.org/resources/mpoints.85184/");

        if (ServerINFO.Lang.equalsIgnoreCase("Chinese")
                | ServerINFO.Lang.equalsIgnoreCase("ChineseTW")) {
            player.sendMessage("§f[MPoints]§ahttps://www.mcbbs.net/thread-1130411-1-1.html");
        }

    }

}
