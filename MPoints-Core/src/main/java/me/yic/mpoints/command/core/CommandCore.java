/*
 *  This file (CommandCore.java) is a part of project MPoints
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
import me.yic.mpoints.MPoints;
import me.yic.mpoints.MPointsLoad;
import me.yic.mpoints.adapter.comp.CPlayer;
import me.yic.mpoints.adapter.comp.CSender;
import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.data.caches.PointsCache;
import me.yic.mpoints.data.syncdata.PlayerData;
import me.yic.mpoints.data.syncdata.SyncMessage;
import me.yic.mpoints.info.MessageConfig;
import me.yic.mpoints.info.SyncType;
import me.yic.mpoints.lang.MessagesManager;
import me.yic.mpoints.utils.UUIDMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandCore {

    protected static String PREFIX = translateColorCodes("prefix");
    protected static String[] systemcommond = new String[]{"reload", "deldata", "reload", "help"};

    public static boolean onCommand(CSender sender, String[] args) {
        if (args.length == 0){
            showVersion(sender);
            return true;
        }
        if (Arrays.stream(systemcommond).anyMatch(element -> element.equals(args[0]))) {
            if (sender.isOp()) {
                if (args.length == 1){
                    if (args[0].equalsIgnoreCase("reload")) {
                        MessagesManager.loadlangmess();
                        PREFIX = translateColorCodes("prefix");
                        sendMessages(sender, PREFIX + MessagesManager.systemMessage("§amessage.yml重载成功"));
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("list")) {
                        for (String sign : PointsCache.getPointsList()) {
                            sendMessages(sender, PREFIX + translateColorCodes(sign, "points_list").replace("%sign%", sign));
                        }
                        return true;
                    }
                }
                if (args.length == 3 && args[0].equalsIgnoreCase("deldata")) {

                    if (check()) {
                        sendMessages(sender, PREFIX + MessagesManager.systemMessage("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令"));
                        return true;
                    }

                    PlayerData pd = DataCon.getPlayerData(args[1], args[2]);
                    if (pd == null) {
                        sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
                        return true;
                    }

                    DataCon.deletePlayerData(args[1], pd);

                    sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.DELETE_DATA_ADMIN).replace("%player%", pd.getName()));

                    return true;
                }
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                sendHelpMessage(sender, 1);
                return true;
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("help")) {
                if (isInt(args[1])) {
                    if (Integer.parseInt(args[1]) > 0) {
                        sendHelpMessage(sender, Integer.valueOf(args[1]));
                    } else {
                        sendHelpMessage(sender, 1);
                    }
                } else {
                    sendHelpMessage(sender, 1);
                }
                return true;
            }
            showVersion(sender);
            return true;
        }

        if (args.length >= 2){
            String commandName = args[1];
            switch (commandName) {
                case "balancetop":
                case "baltop": {
                    return CommandBalancetop.onCommand(sender, args);
                }

                case "pay": {
                    return CommandPay.onCommandpay(sender, args);
                }

                case "money":
                case "balance":
                case "bal":
                case "economy":
                case "eco":
                case "give":
                case "take":
                case "set": {
                    return CommandBalance.onCommandbal(sender, args);
                }

                default: {
                    sendHelpMessage(sender, 1);
                    break;
                }

            }
        }
        sendHelpMessage(sender, 1);
        return true;
    }
    protected static boolean isInt(String s) {
        return Pattern.matches("^[1-9]\\d*$", s);
    }

    protected static boolean isDouble(String psign, String s) {
        if (s.length() > 20){
            return false;
        }
        if (s.matches(".*[a-zA-Z].*")) {
            return false;
        }

        BigDecimal value;
        if (PointsCache.getPoint(psign).INTEGER_BAL){
            try {
                Integer.parseInt(s);
                value = new BigDecimal(s);
            } catch (NumberFormatException ignored) {
                return false;
            }
        }else {
            try {
                Double.parseDouble(s);
                Pattern pattern = Pattern.compile("\\.\\d+");
                Matcher matcher = pattern.matcher(s);

                if (matcher.find()) {
                    String decimalPart = matcher.group();
                    int decimalPlaces = decimalPart.length() - 1;
                    if (decimalPlaces > 2){
                        return false;
                    }
                }
                value = new BigDecimal(s);
            } catch (NumberFormatException ignored) {
                return false;
            }
        }

        if (value.compareTo(BigDecimal.ZERO) > 0) {
            return !PointsCache.getDataFormat(psign).isMAX(PointsCache.getDataFormat(psign).formatString(s));
        }

        return true;
    }

    public static boolean check() {
        return AdapterManager.BanModiftyBalance();
    }

    public static boolean checkMessage(String message) {
        return !MessagesManager.messageFile.getString(message).equals("");
    }

    protected static void sendMessages(CPlayer sender, String message) {
        if (!message.replace(PREFIX, "").equalsIgnoreCase("")) {
            if (message.contains("\\n")) {
                String[] messs = message.split("\\\\n");
                sender.sendMessage(messs);
            } else {
                sender.sendMessage(message);
            }
        }
    }

    protected static void sendMessages(CSender sender, String message) {
        if (!message.replace(PREFIX, "").equalsIgnoreCase("")) {
            if (message.contains("\\n")) {
                String[] messs = message.split("\\\\n");
                sender.sendMessage(messs);
            } else {
                sender.sendMessage(message);
            }
        }
    }

    protected static String translateColorCodes(MessageConfig message) {
        return AdapterManager.translateColorCodes(message);
    }

    protected static String translateColorCodes(String message) {
        return AdapterManager.translateColorCodes(message);
    }

    protected static String translateColorCodes(String psign, String message) {
        return AdapterManager.translateColorCodes(message).replace("%pointname%", PointsCache.getPoint(psign).SINGULAR_NAME);
    }

    public static void showVersion(CSender sender) {
        sender.sendMessage(PREFIX + "§6MPoints §f(Version: "
                + MPoints.PVersion + ") §6|§7 Author: §f" + MessagesManager.getAuthor());
        String trs = MessagesManager.getTranslatorS();
        if (trs != null) {
            sender.sendMessage(PREFIX + "§7Translator (system): §f" + trs);
        }
    }

    protected static void sendHelpMessage(CSender sender, Integer num) {
        List<String> helplist = new ArrayList<>();
        helplist.add(translateColorCodes("help1"));
        helplist.add(translateColorCodes("help2"));
        helplist.add(translateColorCodes("help3"));
        helplist.add(translateColorCodes("help4"));
        helplist.add(translateColorCodes("help5"));
        if (sender.isOp() | sender.hasPermission("mpoints.admin.list")) {
            helplist.add(translateColorCodes("help6"));
        }
        if (sender.isOp() | sender.hasPermission("mpoints.admin.give")) {
            helplist.add(translateColorCodes("help7"));
            helplist.add(translateColorCodes("help10"));
        }
        if (sender.isOp() | sender.hasPermission("mpoints.admin.take")) {
            helplist.add(translateColorCodes("help8"));
            helplist.add(translateColorCodes("help11"));
        }
        if (sender.isOp() | sender.hasPermission("mpoints.admin.set")) {
            helplist.add(translateColorCodes("help9"));
        }
        if (sender.isOp() | sender.hasPermission("mpoints.admin.balancetop")) {
            helplist.add(translateColorCodes("help12"));
        }
        if (sender.isOp()) {
            helplist.add(translateColorCodes("help13"));
        }
        Integer maxipages;
        if (helplist.size() % MPointsLoad.Config.LINES_PER_PAGE == 0) {
            maxipages = helplist.size() / MPointsLoad.Config.LINES_PER_PAGE;
        } else {
            maxipages = helplist.size() / MPointsLoad.Config.LINES_PER_PAGE + 1;
        }
        if (num > maxipages) {
            num = maxipages;
        }
        sendMessages(sender, translateColorCodes("help_title_full").replace("%page%", num + "/" + maxipages));
        int indexpage = 0;
        while (indexpage < MPointsLoad.Config.LINES_PER_PAGE) {
            if (helplist.size() > indexpage + (num - 1) * MPointsLoad.Config.LINES_PER_PAGE) {
                sender.sendMessage(helplist.get(indexpage + (num - 1) * MPointsLoad.Config.LINES_PER_PAGE));
            }
            indexpage += 1;
        }
    }

    protected static void sendRankingMessage(String psign, CSender sender, Integer num) {
        Integer maxipages;
        int listsize = Cache.baltop_papi.get(psign).size();
        if (listsize % MPointsLoad.Config.LINES_PER_PAGE == 0) {
            maxipages = listsize / MPointsLoad.Config.LINES_PER_PAGE;
        } else {
            maxipages = listsize / MPointsLoad.Config.LINES_PER_PAGE + 1;
        }
        if (num > maxipages) {
            num = maxipages;
        }
        int endindex = num * MPointsLoad.Config.LINES_PER_PAGE;
        if (endindex >= listsize) {
            endindex = listsize;
        }
        List<String> topNames = Cache.baltop_papi.get(psign).subList(num * MPointsLoad.Config.LINES_PER_PAGE - MPointsLoad.Config.LINES_PER_PAGE, endindex);

        sendMessages(sender, translateColorCodes(psign, "top_title").replace("%page%", num + "/" + maxipages));
        sendMessages(sender, translateColorCodes("sum_text")
                .replace("%balance%", PointsCache.getDataFormat(psign).shown(Cache.sumbalance.get(psign))));
        int placement = 0;
        for (String topName : topNames) {
            placement++;
            sendMessages(sender, translateColorCodes("top_text")
                    .replace("%index%", String.valueOf(num * MPointsLoad.Config.LINES_PER_PAGE - MPointsLoad.Config.LINES_PER_PAGE + placement))
                    .replace("%player%", topName)
                    .replace("%balance%", PointsCache.getDataFormat(psign).shown(Cache.baltop.get(psign).get(topName))));
        }

        sendMessages(sender, translateColorCodes("top_subtitle"));

    }


    protected static void broadcastSendMessage(boolean ispublic, PlayerData pd, String message) {
        if (!MPointsLoad.getSyncData_Enable()) {
            return;
        }
        if (check() && AdapterManager.PLUGIN.getOnlinePlayersisEmpty()) {
            return;
        }

        SyncMessage sm;
        if (!ispublic) {
            if (MPointsLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                sm = new SyncMessage(SyncType.MESSAGE_SEMI, pd.getName(), message);
            } else {
                sm = new SyncMessage(SyncType.MESSAGE, pd.getUniqueId(), message);
            }
        } else {
            sm = new SyncMessage(SyncType.BROADCAST, "", message);
        }

       DataCon.SendMessTask(sm);
    }


}
