/*
 *  This file (SPsync.java) is a part of project MPoints
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

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.yic.mpoints.MPoints;
import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.data.DataFormat;
import me.yic.mpoints.data.caches.Cache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SPsync implements PluginMessageListener {

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player arg1, byte[] message) {
        if (!channel.equals("mpoints:aca")) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        String type = input.readUTF();
        String serversign = input.readUTF();
        String pointsign = input.readUTF();
        if (!serversign.equals(MPoints.getSign())) {
            return;
        }

        if (type.equalsIgnoreCase("balance")) {
            UUID u = UUID.fromString(input.readUTF());
            String bal = input.readUTF();
            Cache.insertIntoCache(u, pointsign, DataFormat.formatString(pointsign, bal));
        } else if (type.equalsIgnoreCase("message")) {
            Player p = Bukkit.getPlayer(UUID.fromString(input.readUTF()));
            String mess = input.readUTF();
            if (p != null) {
                p.sendMessage(mess);
            }
        } else if (type.equalsIgnoreCase("balanceall")) {
            String targettype = input.readUTF();
            String amount = input.readUTF();
            String isadds = input.readUTF();
            if (targettype.equalsIgnoreCase("all")) {
                Cache.playerdata.clear();
            } else if (targettype.equalsIgnoreCase("online")) {
                Cache.playerdata.clear();
                Boolean isadd = null;
                if (isadds.equalsIgnoreCase("add")) {
                    isadd = true;
                } else if (isadds.equalsIgnoreCase("subtract")) {
                    isadd = false;
                } else if (isadds.equalsIgnoreCase("set")) {
                    isadd = null;
                }
                DataCon.saveall("online", pointsign, DataFormat.formatString(pointsign, amount), isadd, null);
            }
        } else if (type.equalsIgnoreCase("broadcast")) {
            String mess = input.readUTF();
            Bukkit.broadcastMessage(mess);
        }
    }

}
