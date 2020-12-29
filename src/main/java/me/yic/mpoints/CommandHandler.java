package me.yic.mpoints;

import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.data.DataFormat;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.data.caches.PointsCache;
import me.yic.mpoints.message.Messages;
import me.yic.mpoints.message.MessagesManager;
import me.yic.mpoints.task.SendMessTaskS;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandHandler {

    public static boolean onCommand(CommandSender sender, Integer commandindex, String sign, String commandp, String commandName, String[] args) {
        switch (commandName) {
            case "balancetop":
			case "baltop": {
                if (args.length - commandindex == 0) {

                    if (!(sender.isOp() || sender.hasPermission("mpoints.user.balancetop"))) {
                        sender.sendMessage(sendprefix() + sendMessage(null, "no_permission"));
                        return true;
                    }

                    if (!Cache.baltop_uid.containsKey(sign)) {
                        sender.sendMessage(sendprefix() + sendMessage(null, "points_nosign"));
                        return true;
                    }

                    if (Cache.baltop_uid.get(sign).isEmpty()) {
                        sender.sendMessage(sendprefix() + sendMessage(null, "top_nodata"));
                        return true;
                    }

                    sender.sendMessage(sendMessage(sign, "top_title"));
                    sender.sendMessage(sendMessage(null, "sum_text")
                            .replace("%balance%", DataFormat.shown(sign, Cache.sumbalance.get(sign))));

                    List<UUID> topuids = Cache.baltop_uid.get(sign);
                    int placement = 0;
                    for (UUID uu : topuids) {
                        String topName = Cache.baltop_name.get(sign).get(placement);
                        placement++;
                        sender.sendMessage(sendMessage(null, "top_text")
                                .replace("%index%", String.valueOf(placement))
                                .replace("%player%", topName)
                                .replace("%balance%", DataFormat.shown(sign, Cache.getBalanceFromCacheOrDB(uu, sign))));
                    }

                    if (checkMessage("top_subtitle"))
                        sender.sendMessage(sendMessage(null, "top_subtitle"));

                    break;
                } else if (args.length - commandindex == 2) {
                    if (args[0 + commandindex].equalsIgnoreCase("hide") || args[0 + commandindex].equalsIgnoreCase("display")) {

                        if (!(sender.isOp() || sender.hasPermission("mpoints.admin.balancetop"))) {
                            sendHelpMessage(sender, 1);
                            return true;
                        }

                        UUID targetUUID = Cache.translateUUID(args[1 + commandindex],null);

                        if (targetUUID == null) {
                            sender.sendMessage(sendprefix() + sendMessage(null, "noaccount"));
                            return true;
                        }

                        if (args[0 + commandindex].equalsIgnoreCase("hide")) {
                            DataCon.setTopBalHide(targetUUID, sign, 1);
                            sender.sendMessage(sendprefix() + sendMessage(sign, "top_hidden").replace("%player%", args[1 + commandindex]));
                        } else if (args[0 + commandindex].equalsIgnoreCase("display")) {
                            DataCon.setTopBalHide(targetUUID, sign, 0);
                            sender.sendMessage(sendprefix() + sendMessage(sign, "top_displayed").replace("%player%", args[1 + commandindex]));
                        }

                        break;
                    }
                }
            }

            case "pay": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(sendprefix() + Messages.systemMessage("§6控制台无法使用该指令"));
                    return true;
                }

                if (!(sender.isOp() || PointsCache.getPointFromCache(sign).getallowpay())) {
                    sender.sendMessage(sendprefix() + sendMessage(null, "no_permission"));
                    return true;
                }

                if (args.length - commandindex != 2) {
                    sendHelpMessage(sender, 1);
                    return true;
                }

                if (sender.getName().equalsIgnoreCase(args[0 + commandindex])) {
                    sender.sendMessage(sendprefix() + sendMessage(null, "pay_self"));
                    return true;
                }

                if (!isDouble(sign, args[1 + commandindex])) {
                    sender.sendMessage(sendprefix() + sendMessage(null, "invalid"));
                    return true;
                }

                BigDecimal amount = DataFormat.formatString(sign, args[1 + commandindex]);

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    sender.sendMessage(sendprefix() + sendMessage(null, "invalid"));
                    return true;
                }

                String amountFormatted = DataFormat.shown(sign, amount);
                BigDecimal bal_sender = Cache.getBalanceFromCacheOrDB(((Player) sender).getUniqueId(), sign);

                if (bal_sender.compareTo(amount) < 0) {
                    sender.sendMessage(sendprefix() + sendMessage(sign, "pay_fail")
                            .replace("%amount%", amountFormatted));
                    return true;
                }

                Player target = Cache.getplayer(args[0 + commandindex]);
                UUID targetUUID = Cache.translateUUID(args[0 + commandindex],null);
                if (targetUUID == null) {
                    sender.sendMessage(sendprefix() + sendMessage(null, "noaccount"));
                    return true;
                }

                BigDecimal bal_target = Cache.getBalanceFromCacheOrDB(targetUUID, sign);
                if (DataFormat.isMAX(sign, bal_target.add(amount))) {
                    sender.sendMessage(sendprefix() + sendMessage(null, "over_maxnumber"));
                    return true;
                }

                String com = commandp + " " + commandName + " " + args[0 + commandindex] + " " + args[1 + commandindex];
                Cache.change(((Player) sender).getUniqueId(), sender.getName(), sign, amount, false, "PLAYER_COMMAND", com);
                sender.sendMessage(sendprefix() + sendMessage(sign, "pay")
                        .replace("%player%", args[0 + commandindex])
                        .replace("%amount%", amountFormatted));

                Cache.change(targetUUID, args[0 + commandindex], sign, amount, true, "PLAYER_COMMAND", com);
                String mess = sendprefix() + sendMessage(sign, "pay_receive")
                        .replace("%player%", sender.getName())
                        .replace("%amount%", amountFormatted);

                if (target == null) {
                    broadcastSendMessage(false, sign, targetUUID, mess);
                    return true;
                }

                target.sendMessage(mess);
                break;
            }

			case "balance":
			case "bal": {

                switch (args.length - commandindex) {
                    case 0: {
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(sendprefix() + Messages.systemMessage("§6控制台无法使用该指令"));
                            return true;
                        }

                        if (!(sender.isOp() || sender.hasPermission("mpoints.user.balance"))) {
                            sender.sendMessage(sendprefix() + sendMessage(null, "no_permission"));
                            return true;
                        }

                        Player player = (Player) sender;


                        if (MPoints.config.getBoolean("Settings.cache-correction")) {
                            Cache.refreshFromCache(player.getUniqueId(), sign);
                        }

                        BigDecimal a = Cache.getBalanceFromCacheOrDB(player.getUniqueId(), sign);
                        sender.sendMessage(sendprefix() + sendMessage(sign, "balance")
                                .replace("%balance%", DataFormat.shown(sign, a)));

                        break;
                    }

                    case 1: {
                        if (!(sender.isOp() || sender.hasPermission("xconomy.user.balance.other"))) {
                            sender.sendMessage(sendprefix() + sendMessage(null, "no_permission"));
                            return true;
                        }

                        UUID targetUUID = Cache.translateUUID(args[0 + commandindex],null);
                        if (targetUUID == null) {
                            sender.sendMessage(sendprefix() + sendMessage(null, "noaccount"));
                            return true;
                        }

                        BigDecimal targetBalance = Cache.getBalanceFromCacheOrDB(targetUUID, sign);
                        sender.sendMessage(sendprefix() + sendMessage(sign, "balance_other")
                                .replace("%player%", args[0 + commandindex])
                                .replace("%balance%", DataFormat.shown(sign, targetBalance)));

                        break;
                    }

                    default: {
                        sendHelpMessage(sender, 1);
                        break;
                    }

                }
                break;
            }


            case "give":
            case "take":
            case "set": {

                if (!(sender.isOp() | sender.hasPermission("mpoints.admin.give")
                        | sender.hasPermission("mpoints.admin.take") | sender.hasPermission("mpoints.admin.set"))) {
                    sendHelpMessage(sender, 1);
                    return true;
                }

                if (!check()) {
                    sender.sendMessage(sendprefix() + Messages.systemMessage("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令"));
                    return true;
                }

                switch (args.length - commandindex) {
                    case 2:
                    case 3: {

                        if (!isDouble(sign, args[1 + commandindex])) {
                            sender.sendMessage(sendprefix() + sendMessage(null, "invalid"));
                            return true;
                        }

                        BigDecimal amount = DataFormat.formatString(sign, args[1 + commandindex]);
                        String amountFormatted = DataFormat.shown(sign, amount);
                        Player target = Cache.getplayer(args[0 + commandindex]);
                        UUID targetUUID = Cache.translateUUID(args[0 + commandindex],null);
                        String reason = "";
                        if (args.length - commandindex == 3) {
                            reason = args[2 + commandindex];
                        }

                        if (targetUUID == null) {
                            sender.sendMessage(sendprefix() + sendMessage(null, "noaccount"));
                            return true;
                        }

                        String com = commandp + " " + commandName + " " + args[0 + commandindex] + " " + args[1 + commandindex] + " " + reason;

                        Boolean hidecommandmessage = PointsCache.getPointFromCache(sign).gethidemessage();

                        switch (commandName) {
                            case "give": {
                                if (!(sender.isOp() | sender.hasPermission("mpoints.admin.give"))) {
                                    sendHelpMessage(sender, 1);
                                    return true;
                                }

                                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                                    sender.sendMessage(sendprefix() + sendMessage(null, "invalid"));
                                    return true;
                                }

                                BigDecimal bal = Cache.getBalanceFromCacheOrDB(targetUUID, sign);
                                if (DataFormat.isMAX(sign, bal.add(amount))) {
                                    sender.sendMessage(sendprefix() + sendMessage(null, "over_maxnumber"));
                                    return true;
                                }

                                Cache.change(targetUUID, args[0 + commandindex], sign, amount, true, "ADMIN_COMMAND", com);
                                sender.sendMessage(sendprefix() + sendMessage(sign, "money_give")
                                        .replace("%player%", args[0 + commandindex])
                                        .replace("%amount%", amountFormatted));

                                if ((checkMessage("money_give_receive") & !hidecommandmessage) | args.length - commandindex == 3) {

                                        String message = sendprefix() + sendMessage(sign, "money_give_receive")
                                                .replace("%player%", args[0 + commandindex])
                                                .replace("%amount%", amountFormatted);

                                        if (args.length - commandindex == 3) {
                                            message = sendprefix() + reason;
                                        }

                                        if (target == null) {
                                            broadcastSendMessage(false, sign, targetUUID, message);
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
                                    sender.sendMessage(sendprefix() + sendMessage(null, "invalid"));
                                    return true;
                                }

                                BigDecimal bal = Cache.getBalanceFromCacheOrDB(targetUUID, sign);
                                if (bal.compareTo(amount) < 0) {
                                    sender.sendMessage(sendprefix() + sendMessage(sign, "money_take_fail")
                                            .replace("%player%", args[0 + commandindex])
                                            .replace("%amount%", amountFormatted));

                                    return true;
                                }

                                Cache.change(targetUUID, args[0 + commandindex], sign, amount, false, "ADMIN_COMMAND", com);
                                sender.sendMessage(sendprefix() + sendMessage(sign, "money_take")
                                        .replace("%player%", args[0 + commandindex])
                                        .replace("%amount%", amountFormatted));

                                if ((checkMessage("money_give_receive") & !hidecommandmessage) | args.length - commandindex == 3) {
                                    String mess = sendprefix() + sendMessage(sign, "money_take_receive")
                                            .replace("%player%", args[0 + commandindex]).replace("%amount%", amountFormatted);

                                    if (args.length - commandindex == 3) {
                                        mess = sendprefix() + reason;
                                    }

                                    if (target == null) {
                                        broadcastSendMessage(false, sign, targetUUID, mess);
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

                                Cache.change(targetUUID, args[0 + commandindex], sign, amount, null, "ADMIN_COMMAND", com);
                                sender.sendMessage(sendprefix() + sendMessage(sign, "money_set")
                                        .replace("%player%", args[0 + commandindex])
                                        .replace("%amount%", amountFormatted));

                                if ((checkMessage("money_give_receive") & !hidecommandmessage) | args.length - commandindex == 3) {
                                    String mess = sendprefix() + sendMessage(sign, "money_set_receive")
                                            .replace("%player%", args[0 + commandindex])
                                            .replace("%amount%", amountFormatted);

                                    if (args.length - commandindex == 3) {
                                        mess = sendprefix() + reason;
                                    }

                                    if (target == null) {
                                        broadcastSendMessage(false, sign, targetUUID, mess);
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

                    case 4: {

                        if (!args[0 + commandindex].equals("*")) {
                            sendHelpMessage(sender, 1);
                            return true;
                        }

                        if (!(args[1 + commandindex].equalsIgnoreCase("all") | args[1 + commandindex].equalsIgnoreCase("online"))) {
                            sendHelpMessage(sender, 1);
                            return true;
                        }

                        if (!isDouble(sign, args[2 + commandindex])) {
                            sender.sendMessage(sendprefix() + sendMessage(null, "invalid"));
                            return true;
                        }

                        BigDecimal amount = DataFormat.formatString(sign, args[2 + commandindex]);

                        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                            sender.sendMessage(sendprefix() + sendMessage(null, "invalid"));
                            return true;
                        }

                        String target = "AllPlayer";
                        if (args[1 + commandindex].equalsIgnoreCase("online")) {
                            target = "OnlinePlayer";
                        }

                        String amountFormatted = DataFormat.shown(sign, amount);

                        String com = commandp + " " + commandName + " " + args[0 + commandindex] + " " + args[1 + commandindex] + " " + args[2 + commandindex] + " " + args[3 + commandindex];

                        switch (commandName) {
                            case "give": {
                                if (!(sender.isOp() | sender.hasPermission("mpoints.admin.give"))) {
                                    sendHelpMessage(sender, 1);
                                    return true;
                                }

                                Cache.changeall(sign, args[1 + commandindex], amount, true, "ADMIN_COMMAND", com);
                                sender.sendMessage(sendprefix() + sendMessage(sign, "money_give")
                                        .replace("%player%", target)
                                        .replace("%amount%", amountFormatted));

                                String message = sendprefix() + args[3 + commandindex];
                                Bukkit.broadcastMessage(message);
                                broadcastSendMessage(true, sign, null, message);
                                break;
                            }

                            case "take": {
                                if (!(sender.isOp() | sender.hasPermission("mpoints.admin.take"))) {
                                    sendHelpMessage(sender, 1);
                                    return true;
                                }

                                Cache.changeall(sign, args[1 + commandindex], amount, false, "ADMIN_COMMAND", com);
                                sender.sendMessage(sendprefix() + sendMessage(sign, "money_take")
                                        .replace("%player%", target)
                                        .replace("%amount%", amountFormatted));

                                String message = sendprefix() + args[3 + commandindex];
                                Bukkit.broadcastMessage(message);
                                broadcastSendMessage(true, sign, null, message);

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
                break;
            }
            default: {
                sendHelpMessage(sender, 1);
                break;
            }

        }

        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isDouble(String sign, String s) {
        try {
            BigDecimal value = new BigDecimal(s);

            if (value.compareTo(BigDecimal.ONE) >= 0) {
                return !DataFormat.isMAX(sign, DataFormat.formatString(sign, s));
            }

        } catch (NumberFormatException ignored) {
            return false;
        }

        return true;
    }

    public static boolean check() {
        return !(Bukkit.getOnlinePlayers().isEmpty() && MPoints.isBungeecord());
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean checkMessage(String message) {
        return !MessagesManager.messageFile.getString(message).equals("");
    }

    public static String sendprefix() {
        return sendMessage(null, "prefix");
    }

    @SuppressWarnings("ConstantConditions")
    public static String sendMessage(String sign, String message) {
        if (sign == null) {
            return ChatColor.translateAlternateColorCodes('&', MessagesManager.messageFile.getString(message));
        } else {
            return ChatColor.translateAlternateColorCodes('&', MessagesManager.messageFile.getString(message)).
                    replace("%pointname%", PointsCache.getPointFromCache(sign).getpluralname());
        }
    }

    private static void sendHelpMessage(CommandSender sender, Integer num) {
        List<String> helplist = new ArrayList<>();
        helplist.add(sendMessage(null, "help1"));
        helplist.add(sendMessage(null, "help2"));
        helplist.add(sendMessage(null, "help3"));
        helplist.add(sendMessage(null, "help4"));
        helplist.add(sendMessage(null, "help5"));
        if (sender.isOp() | sender.hasPermission("mpoints.admin.list")) {
            helplist.add(sendMessage(null, "help6"));
        }
        if (sender.isOp() | sender.hasPermission("mpoints.admin.give")) {
            helplist.add(sendMessage(null, "help7"));
            helplist.add(sendMessage(null, "help10"));
        }
        if (sender.isOp() | sender.hasPermission("mpoints.admin.take")) {
            helplist.add(sendMessage(null, "help8"));
            helplist.add(sendMessage(null, "help11"));
        }
        if (sender.isOp() | sender.hasPermission("mpoints.admin.set")) {
            helplist.add(sendMessage(null, "help9"));
        }
        if (sender.isOp() | sender.hasPermission("mpoints.admin.balancetop")) {
            helplist.add(sendMessage(null, "help12"));
        }
        if (sender.isOp()) {
            helplist.add(sendMessage(null, "help13"));
        }
        Integer maxipages = 0;
        if (helplist.size() % 5 == 0){
            maxipages = helplist.size() / 5;
        }else{
            maxipages = helplist.size() / 5 + 1;
        }
        sender.sendMessage(sendMessage(null, "help_title_full").replace("%page%", num.toString() + "/" + maxipages.toString()));
        Integer indexpage = 0;
        while (indexpage < 5) {
            if (helplist.size() > indexpage + (num - 1) * 5) {
                sender.sendMessage(helplist.get(indexpage + (num - 1) * 5));
            }
            indexpage += 1;
        }
    }

    private static void broadcastSendMessage(boolean ispublic, String sign, UUID u, String s1) {
        if (!MPoints.isBungeecord() || !PointsCache.getPointFromCache(sign).getenablebc()) {
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            if (!ispublic) {
                output.writeUTF("message");
                output.writeUTF(MPoints.getSign());
                output.writeUTF(sign);
                output.writeUTF(u.toString());
                output.writeUTF(s1);
            } else {
                output.writeUTF("broadcast");
                output.writeUTF(MPoints.getSign());
                output.writeUTF(sign);
                output.writeUTF(s1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        new SendMessTaskS(stream, null, sign, null, null).runTaskAsynchronously(MPoints.getInstance());

    }

}
