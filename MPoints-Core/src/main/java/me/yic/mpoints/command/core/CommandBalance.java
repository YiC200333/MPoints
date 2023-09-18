/*
 *  This file (CommandBalance.java) is a part of project MPoints
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

import me.yic.mpoints.AdapterManager;
import me.yic.mpoints.MPointsLoad;
import me.yic.mpoints.adapter.comp.CPlayer;
import me.yic.mpoints.adapter.comp.CSender;
import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.data.caches.PointsCache;
import me.yic.mpoints.data.syncdata.PlayerData;
import me.yic.mpoints.info.MessageConfig;
import me.yic.mpoints.lang.MessagesManager;
import me.yic.mpoints.utils.UUIDMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandBalance extends CommandCore{
    public static boolean onCommandbal(CSender sender, String[] args) {
        if (args.length < 2) {
            sendHelpMessage(sender, 1);
            return true;
        }
        String psign = args[0];
        if (!PointsCache.CacheContainsKey(psign)) {
            sendMessages(sender, PREFIX + translateColorCodes("points_nosign"));
            return true;
        }
        String commandName = args[1];
        List<String> commandargs = new ArrayList<>();
        if (commandName.equalsIgnoreCase("give") || commandName.equalsIgnoreCase("take") || commandName.equalsIgnoreCase("set")){
            commandargs.add(commandName);
            commandName = "money";
        }
        int arcount = 0;
        for (String arg : args){
            if (arcount > 1) {
                commandargs.add(arg);
            }
            arcount ++;
        }
        args = commandargs.toArray(new String[0]);
        int commndlength = args.length;

        StringBuilder reasonmessages = null;
        if (sender.isOp() | sender.hasPermission("mpoints.admin.give")
                | sender.hasPermission("mpoints.admin.take") | sender.hasPermission("mpoints.admin.set")) {
            if (args.length >= 4) {
                if (args.length == 4) {
                    reasonmessages = new StringBuilder(args[3]);
                } else {
                    reasonmessages = new StringBuilder();
                    if (isDouble(psign, args[2])) {
                        commndlength = 4;
                        int count = 3;
                        while (count < args.length) {
                            reasonmessages.append(args[count]).append(" ");
                            count += 1;
                        }
                    } else {
                        if (args.length == 5) {
                            reasonmessages = new StringBuilder(args[4]);
                        } else {
                            commndlength = 5;
                            int count = 4;
                            while (count < args.length) {
                                reasonmessages.append(args[count]).append(" ");
                                count += 1;
                            }
                        }
                    }
                }
            }
        }

        switch (commndlength) {
            case 0: {
                if (!sender.isPlayer()) {
                    sendMessages(sender, PREFIX + MessagesManager.systemMessage("§6控制台无法使用该指令"));
                    return true;
                }

                if (!(sender.isOp() || sender.hasPermission("mpoints.user.balance"))) {
                    sendMessages(sender, PREFIX + translateColorCodes("no_permission"));
                    return true;
                }

                CPlayer player = sender.toPlayer();

                //Cache.refreshFromCache(player.getUniqueId());

                BigDecimal a = DataCon.getPlayerData(psign, player.getUniqueId()).getBalance();
                sendMessages(sender, PREFIX + translateColorCodes(psign, "balance")
                        .replace("%balance%", PointsCache.getDataFormat(psign).shown((a))));

                break;
            }

            case 1: {
                if (!(sender.isOp() || sender.hasPermission("mpoints.user.balance.other"))) {
                    sendMessages(sender, PREFIX + translateColorCodes("no_permission"));
                    return true;
                }

                PlayerData pd = DataCon.getPlayerData(psign, args[0]);
                if (pd == null) {
                    sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
                    return true;
                }
                String realname = pd.getName();

                BigDecimal targetBalance = pd.getBalance();
                sendMessages(sender, PREFIX + translateColorCodes(psign, "balance_other")
                        .replace("%player%", realname)
                        .replace("%balance%", PointsCache.getDataFormat(psign).shown((targetBalance))));

                break;
            }

            case 3:
            case 4: {
                if (!(sender.isOp() | sender.hasPermission("mpoints.admin.give")
                        | sender.hasPermission("mpoints.admin.take") | sender.hasPermission("mpoints.admin.set"))) {
                    sendHelpMessage(sender, 1);
                    return true;
                }

                if (check()) {
                    sendMessages(sender, PREFIX + MessagesManager.systemMessage("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令"));
                    return true;
                }

                if (!isDouble(psign, args[2])) {
                    sendMessages(sender, PREFIX + translateColorCodes("invalid_amount"));
                    return true;
                }

                BigDecimal amount = PointsCache.getDataFormat(psign).formatString(args[2]);
                String amountFormatted = PointsCache.getDataFormat(psign).shown(amount);
                PlayerData pd = DataCon.getPlayerData(psign, args[1]);

                if (pd == null) {
                    sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
                    return true;
                }

                CPlayer target = AdapterManager.PLUGIN.getplayer(pd);
                UUID targetUUID = pd.getUniqueId();
                String realname = pd.getName();

                String com = commandName + " " + args[0] + " " + args[1] + " " + amount;
                if (commndlength == 4) {
                    com += " " + reasonmessages;
                }
                switch (args[0].toLowerCase()) {
                    case "give": {
                        if (!(sender.isOp() | sender.hasPermission("mpoints.admin.give"))) {
                            sendHelpMessage(sender, 1);
                            return true;
                        }

                        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                            sendMessages(sender, PREFIX + translateColorCodes("invalid_amount"));
                            return true;
                        }

                        //Cache.refreshFromCache(targetUUID);

                        BigDecimal bal = pd.getBalance();
                        if (PointsCache.getDataFormat(psign).isMAX(bal.add(amount))) {
                            sendMessages(sender, PREFIX + translateColorCodes("over_maxnumber"));
                            if (target != null) {
                                sendMessages(target, PREFIX + translateColorCodes("over_maxnumber_receive"));
                            }
                            return true;
                        }

                        DataCon.changeplayerdata(psign, "ADMIN_COMMAND", targetUUID, amount, true, com, reasonmessages);
                        sendMessages(sender, PREFIX + translateColorCodes(psign, "money_give")
                                .replace("%player%", realname)
                                .replace("%amount%", amountFormatted));

                        if (checkMessage("money_give_receive") | commndlength == 4) {

                            String message = PREFIX + translateColorCodes(psign, "money_give_receive")
                                    .replace("%player%", realname)
                                    .replace("%amount%", amountFormatted);

                            if (commndlength == 4) {
                                message = PREFIX + reasonmessages;
                            }

                            if (!target.isOnline()) {
                                broadcastSendMessage(false, pd, message);
                                return true;
                            }

                            target.sendMessage(message);

                        }
                        break;
                    }

                    case "take": {
                        if (!(sender.isOp() | sender.hasPermission("mpoints.admin.take"))) {
                            sendHelpMessage(sender, 1);
                            return true;
                        }

                        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                            sendMessages(sender, PREFIX + translateColorCodes("invalid_amount"));
                            return true;
                        }

                        //Cache.refreshFromCache(targetUUID);
                        BigDecimal bal = pd.getBalance();
                        if (bal.compareTo(amount) < 0) {
                            sendMessages(sender, PREFIX + translateColorCodes(psign, "money_take_fail")
                                    .replace("%player%", realname)
                                    .replace("%amount%", amountFormatted));

                            return true;
                        }

                        DataCon.changeplayerdata(psign, "ADMIN_COMMAND", targetUUID, amount, false, com, reasonmessages);
                        sendMessages(sender, PREFIX + translateColorCodes(psign, "money_take")
                                .replace("%player%", realname)
                                .replace("%amount%", amountFormatted));

                        if (checkMessage("money_take_receive") | commndlength == 4) {
                            String mess = PREFIX + translateColorCodes(psign, "money_take_receive")
                                    .replace("%player%", realname).replace("%amount%", amountFormatted);

                            if (commndlength == 4) {
                                mess = PREFIX + reasonmessages;
                            }

                            if (!target.isOnline()) {
                                broadcastSendMessage(false, pd, mess);
                                return true;
                            }

                            target.sendMessage(mess);

                        }
                        break;
                    }

                    case "set": {
                        if (!(sender.isOp() | sender.hasPermission("mpoints.admin.set"))) {
                            sendHelpMessage(sender, 1);
                            return true;
                        }

                        DataCon.changeplayerdata(psign, "ADMIN_COMMAND", targetUUID, amount, null, com, reasonmessages);
                        sendMessages(sender, PREFIX + translateColorCodes(psign, "money_set")
                                .replace("%player%", realname)
                                .replace("%amount%", amountFormatted));

                        if (checkMessage("money_set_receive") | commndlength == 4) {
                            String mess = PREFIX + translateColorCodes(psign, "money_set_receive")
                                    .replace("%player%", realname)
                                    .replace("%amount%", amountFormatted);

                            if (commndlength == 4) {
                                mess = PREFIX + reasonmessages;
                            }

                            if (!target.isOnline()) {
                                broadcastSendMessage(false, pd, mess);
                                return true;
                            }

                            target.sendMessage(mess);

                        }
                        break;
                    }

                    default: {
                        sendHelpMessage(sender, 1);
                        break;
                    }

                }
                break;
            }

            case 5: {
                if (!(sender.isOp() | sender.hasPermission("mpoints.admin.give")
                        | sender.hasPermission("mpoints.admin.take") | sender.hasPermission("mpoints.admin.set"))) {
                    sendHelpMessage(sender, 1);
                    return true;
                }

                if (!args[1].equals("*")) {
                    sendHelpMessage(sender, 1);
                    return true;
                }

                if (!(args[2].equalsIgnoreCase("all") | args[2].equalsIgnoreCase("online"))) {
                    sendHelpMessage(sender, 1);
                    return true;
                }

                if (MPointsLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE) && args[2].equalsIgnoreCase("online")) {
                    sendMessages(sender, PREFIX + MessagesManager.systemMessage("§c该指令不支持在半正版模式中使用"));
                    return true;
                }

                if (check()) {
                    sendMessages(sender, PREFIX + MessagesManager.systemMessage("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令"));
                    return true;
                }

                if (!isDouble(psign, args[3])) {
                    sendMessages(sender, PREFIX + translateColorCodes("invalid_amount"));
                    return true;
                }

                BigDecimal amount = PointsCache.getDataFormat(psign).formatString(args[3]);

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    sendMessages(sender, PREFIX + translateColorCodes("invalid_amount"));
                    return true;
                }

                String target = "AllPlayer";
                if (args[2].equalsIgnoreCase("online")) {
                    target = "OnlinePlayer";
                }

                String amountFormatted = PointsCache.getDataFormat(psign).shown(amount);

                String com = commandName + " " + args[0] + " " + args[1] + " " + args[2] + " " + amount + " " + reasonmessages;

                switch (args[0].toLowerCase()) {
                    case "give": {
                        if (!(sender.isOp() | sender.hasPermission("mpoints.admin.give"))) {
                            sendHelpMessage(sender, 1);
                            return true;
                        }

                        DataCon.changeallplayerdata(psign, args[2], "ADMIN_COMMAND", amount, true, com, reasonmessages);
                        sendMessages(sender, PREFIX + translateColorCodes(psign, "money_give")
                                .replace("%player%", target)
                                .replace("%amount%", amountFormatted));

                        String message = PREFIX + reasonmessages;
                        AdapterManager.PLUGIN.broadcastMessage(message);
                        broadcastSendMessage(true, null, message);
                        break;
                    }

                    case "take": {
                        if (!(sender.isOp() | sender.hasPermission("mpoints.admin.take"))) {
                            sendHelpMessage(sender, 1);
                            return true;
                        }

                        DataCon.changeallplayerdata(psign, args[2], "ADMIN_COMMAND", amount, false, com, reasonmessages);
                        sendMessages(sender, PREFIX + translateColorCodes(psign, "money_take")
                                .replace("%player%", target)
                                .replace("%amount%", amountFormatted));

                        String message = PREFIX + reasonmessages;
                        AdapterManager.PLUGIN.broadcastMessage(message);
                        broadcastSendMessage(true, null, message);

                        break;
                    }

                    default: {
                        sendHelpMessage(sender, 1);
                        break;
                    }

                }

                break;
            }

            default: {
                sendHelpMessage(sender, 1);
                break;
            }

        }
        return true;
    }

}
