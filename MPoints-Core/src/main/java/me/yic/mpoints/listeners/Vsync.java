/*
 *  This file (Vsync.java) is a part of project MPoints
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

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.yic.mpoints.MPointsVelocity;
import me.yic.mpoints.data.syncdata.SyncMessage;
import me.yic.mpoints.data.syncdata.tab.SyncTabJoin;
import me.yic.mpoints.info.SyncType;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Vsync {
    public static final HashMap<String, List<String>> allservername = new HashMap<>();

    @SuppressWarnings(value = {"unused"})
    @Subscribe
    public void on(PluginMessageEvent event) {
        if (event.getSource() instanceof Player) {
            return;
        }

        if (!event.getIdentifier().getId().equals(MPointsVelocity.acb.getId())) {
            return;
        }


        ByteArrayInputStream input = new ByteArrayInputStream(event.getData());
        try {
            ObjectInputStream ios = new ObjectInputStream(input);
            ServerConnection senderServer = (ServerConnection) event.getSource();

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(output);

            String sv = ios.readUTF();
            oos.writeUTF(sv);
            String svv = sv;
            if (svv.contains(".")) {
                svv = "versions §f" + svv;
            } else {
                svv = "§fold versions";
            }
            if (!sv.equals(MPointsVelocity.syncversion)) {
                MPointsVelocity.getInstance().logger.warn("§cReceived data from " + svv + ", §cunable to synchronize, Current plugin version §f" + MPointsVelocity.syncversion);
                return;
            }

            Object ob = ios.readObject();
            if (ob instanceof SyncMessage) {
                SyncMessage sd = (SyncMessage) ob;
                if (sd.getSyncType().equals(SyncType.MESSAGE)) {
                    Optional<Player> p = MPointsVelocity.getInstance().server.getPlayer(sd.getUniqueId());
                    if (!p.isPresent()) {
                        return;
                    }
                }else if(sd.getSyncType().equals(SyncType.MESSAGE_SEMI)) {
                    Optional<Player> p = MPointsVelocity.getInstance().server.getPlayer(sd.getName());
                    if (!p.isPresent()) {
                        return;
                    }else{
                        sd.setRUniqueId(p.get().getUniqueId());
                    }
                }
            }else if (ob instanceof SyncTabJoin) {
                SyncTabJoin sj = (SyncTabJoin) ob;
                String sign = sj.getSign();
                List<String> allname;
                if (allservername.containsKey(sign)){
                    allname = allservername.get(sign);
                }else{
                    allname = new ArrayList<>();
                }
                if (!allname.contains(sj.getName())) {
                    allname.add(sj.getName());
                }
                allservername.put(sign, allname);
                sj.setallPlayers(allname);
            }

            oos.writeObject(ob);
            oos.flush();
            for (RegisteredServer s : MPointsVelocity.getInstance().server.getAllServers()) {
                if (!s.equals(senderServer.getServer()) && s.getPlayersConnected().size() > 0) {
                    MPointsVelocity.getInstance().server.getScheduler().buildTask(MPointsVelocity.getInstance(), () -> SendMessTaskB(s, output)).schedule();
                }
            }


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void SendMessTaskB(RegisteredServer s, ByteArrayOutputStream stream) {
        s.sendPluginMessage(MPointsVelocity.aca, stream.toByteArray());
    }
}
