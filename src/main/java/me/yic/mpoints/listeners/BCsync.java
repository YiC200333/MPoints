/*
 *  This file (BCsync.java) is a part of project MPoints
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
import me.yic.mpoints.MPointsBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class BCsync implements Listener {

    @SuppressWarnings({"UnstableApiUsage", "unused"})
    @EventHandler
    public void on(PluginMessageEvent event) {
        if (!(event.getSender() instanceof Server)) {
            return;
        }

        if (!event.getTag().equalsIgnoreCase("mpoints:acb")) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
        Server senderServer = (Server) event.getSender();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        String type = input.readUTF();
        try {
            if (type.equalsIgnoreCase("balance")) {
                output.writeUTF("balance");
                output.writeUTF(input.readUTF());
                output.writeUTF(input.readUTF());
                output.writeUTF(input.readUTF());
                output.writeUTF(input.readUTF());
            } else if (type.equalsIgnoreCase("message")) {
                output.writeUTF("message");
                output.writeUTF(input.readUTF());
                output.writeUTF(input.readUTF());
                String uid = input.readUTF();
                ProxiedPlayer p = ProxyServer.getInstance().getPlayer(UUID.fromString(uid));
                if (p != null) {
                    output.writeUTF(uid);
                    output.writeUTF(input.readUTF());
                } else {
                    return;
                }
            } else if (type.equalsIgnoreCase("balanceall")) {
                output.writeUTF("balanceall");
                output.writeUTF(input.readUTF());
                output.writeUTF(input.readUTF());
                output.writeUTF(input.readUTF());
                output.writeUTF(input.readUTF());
                output.writeUTF(input.readUTF());
            } else if (type.equalsIgnoreCase("broadcast")) {
                output.writeUTF("broadcast");
                output.writeUTF(input.readUTF());
                output.writeUTF(input.readUTF());
                output.writeUTF(input.readUTF());
            }
        } catch (IOException e) {
            ProxyServer.getInstance().getLogger().severe("An I/O error occurred!");
        }

        for (ServerInfo s : ProxyServer.getInstance().getServers().values()) {
            if (!s.getName().equals(senderServer.getInfo().getName()) && s.getPlayers().size() > 0) {
                ProxyServer.getInstance().getScheduler().runAsync(MPointsBungee.getInstance(), () -> SendMessTaskB(s, stream));
            }
        }

    }


    public static void SendMessTaskB(ServerInfo s, ByteArrayOutputStream stream) {
        s.sendData("mpoints:aca", stream.toByteArray());
    }

}
