/*
 *  This file (CommandPay.java) is a part of project MPoints
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
import me.yic.mpoints.adapter.comp.CPlayer;
import me.yic.mpoints.adapter.comp.CSender;
import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.data.caches.PointsCache;
import me.yic.mpoints.data.syncdata.PlayerData;
import me.yic.mpoints.info.MessageConfig;
import me.yic.mpoints.lang.MessagesManager;
import me.yic.mpoints.task.ReceivePerCheck;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandPay extends CommandCore{
    public static boolean onCommandpay(CSender sender, String[] args) {
        if (args.length < 2) {
            sendHelpMessage(sender, 1);
            return true;
        }
        String psign = args[0];
        if (!PointsCache.CacheContainsKey(psign) || !PointsCache.getPoint(psign).ALLOW_PAY) {
            sendMessages(sender, PREFIX + translateColorCodes("points_nosign"));
            return true;
        }
        String commandName = args[1];
        List<String> commandargs = new ArrayList<>();
        int arcount = 0;
        for (String arg : args){
            if (arcount > 1) {
                commandargs.add(arg);
            }
            arcount ++;
        }
        args = commandargs.toArray(new String[0]);

        if (!sender.isPlayer()) {
            sendMessages(sender, PREFIX + MessagesManager.systemMessage("§6控制台无法使用该指令"));
            return true;
        }

        if (!sender.isOp()) {
            if (!(sender.hasPermission("xconomy.user.pay"))) {
                sendMessages(sender, PREFIX + translateColorCodes("no_permission"));
                return true;
            }
        }

        if (args.length != 2) {
            sendHelpMessage(sender, 1);
            return true;
        }

        if (sender.getName().equalsIgnoreCase(args[0])) {
            sendMessages(sender, PREFIX + translateColorCodes("pay_self"));
            return true;
        }

        if (!isDouble(psign, args[1])) {
            sendMessages(sender, PREFIX + translateColorCodes("invalid_amount"));
            return true;
        }

        BigDecimal amount = PointsCache.getDataFormat(psign).formatString(args[1]);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            sendMessages(sender, PREFIX + translateColorCodes("invalid_amount"));
            return true;
        }

        BigDecimal taxamount = amount.multiply(PointsCache.getPoint(psign).PAYMENT_TAX);

        //Cache.refreshFromCache(sender.toPlayer().getUniqueId());

        String amountFormatted = PointsCache.getDataFormat(psign).shown(amount);
        String taxamountFormatted = PointsCache.getDataFormat(psign).shown(taxamount);
        BigDecimal bal_sender = DataCon.getPlayerData(psign, sender.toPlayer().getUniqueId()).getBalance();

        if (bal_sender.compareTo(taxamount) < 0) {
            sendMessages(sender, PREFIX + translateColorCodes(psign, "pay_fail")
                    .replace("%amount%", taxamountFormatted));
            return true;
        }

        PlayerData pd = DataCon.getPlayerData(psign, args[0]);
        if (pd == null) {
            sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
            return true;
        }

        CPlayer target = AdapterManager.PLUGIN.getplayer(pd);
        UUID targetUUID = pd.getUniqueId();
        String realname = pd.getName();
        if (AdapterManager.foundvaultOfflinePermManager) {
            if (!ReceivePerCheck.hasreceivepermission(target, targetUUID)) {
                sendMessages(sender, PREFIX + translateColorCodes("no_receive_permission"));
                return true;
            }
        }

        //Cache.refreshFromCache(targetUUID);

        BigDecimal bal_target = pd.getBalance();
        if (PointsCache.getDataFormat(psign).isMAX(bal_target.add(amount))) {
            sendMessages(sender, PREFIX + translateColorCodes("over_maxnumber"));
            return true;
        }

        String com = commandName + " " + args[0] + " " + amount;
        DataCon.changeplayerdata(psign, "PLAYER_COMMAND", sender.toPlayer().getUniqueId(), taxamount, false, com, null);
        sendMessages(sender, PREFIX + translateColorCodes(psign, "pay")
                .replace("%player%", realname)
                .replace("%amount%", amountFormatted));

        DataCon.changeplayerdata(psign, "PLAYER_COMMAND", targetUUID, amount, true, com, null);
        String mess = PREFIX + translateColorCodes(psign, "pay_receive")
                .replace("%player%", sender.getName())
                .replace("%amount%", amountFormatted);

        if (!target.isOnline()) {
            broadcastSendMessage(false, pd, mess);
            return true;
        }

        target.sendMessage(mess);
        return true;
    }

}
