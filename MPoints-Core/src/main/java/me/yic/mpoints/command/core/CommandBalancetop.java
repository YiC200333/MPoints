/*
 *  This file (CommandBalancetop.java) is a part of project MPoints
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
package me.yic.mpoints.command.core;

import me.yic.mpoints.adapter.comp.CSender;
import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.data.DataLink;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.data.syncdata.PlayerData;
import me.yic.mpoints.info.MessageConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandBalancetop extends CommandCore{
    public static boolean onCommand(CSender sender, String[] args) {
        if (args.length < 2) {
            sendHelpMessage(sender, 1);
            return true;
        }
        String psign = args[0];
        if (!Cache.baltop_papi.containsKey(psign)) {
            sendMessages(sender, PREFIX + translateColorCodes("points_nosign"));
            return true;
        }
        List<String> commandargs = new ArrayList<>();
        int arcount = 0;
        for (String arg : args){
            if (arcount > 1) {
                commandargs.add(arg);
            }
            arcount ++;
        }
        args = commandargs.toArray(new String[0]);

        if (args.length == 0 || args.length == 1) {

            if (!(sender.isOp() || sender.hasPermission("mpoints.user.balancetop"))) {
                sendMessages(sender, PREFIX + translateColorCodes("no_permission"));
                return true;
            }

            if (Cache.baltop.get(psign).isEmpty()) {
                sendMessages(sender, PREFIX + translateColorCodes("top_nodata"));
                return true;
            }

            if (args.length == 0) {
                sendRankingMessage(psign, sender, 1);
            } else {
                if (isDouble(psign, args[0])) {
                    if (Integer.parseInt(args[0]) > 0) {
                        sendRankingMessage(psign, sender, Integer.valueOf(args[0]));
                    } else {
                        sendRankingMessage(psign, sender, 1);
                    }
                } else {
                    sendRankingMessage(psign, sender, 1);
                }
            }

        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("hide") || args[0].equalsIgnoreCase("display")) {

                if (!(sender.isOp() || sender.hasPermission("mpoints.admin.balancetop"))) {
                    sendHelpMessage(sender, 1);
                    return true;
                }

                PlayerData pd = DataCon.getPlayerData(psign, args[1]);

                if (pd == null) {
                    sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
                    return true;
                }

                UUID targetUUID = pd.getUniqueId();
                if (args[0].equalsIgnoreCase("hide")) {
                    DataLink.setTopBalHide(psign, targetUUID, 1);
                    sendMessages(sender, PREFIX + translateColorCodes(psign, "top_hidden").replace("%player%", args[1]));
                } else if (args[0].equalsIgnoreCase("display")) {
                    DataLink.setTopBalHide(psign, targetUUID, 0);
                    sendMessages(sender, PREFIX + translateColorCodes(psign, "top_displayed").replace("%player%", args[1]));
                }

            }
        }
        return true;
    }

}
