package me.yic.mpoints;

import me.yic.mpoints.data.caches.PointsCache;
import me.yic.mpoints.message.Messages;
import me.yic.mpoints.message.MessagesManager;
import me.yic.mpoints.utils.Points;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String commandName = cmd.getName().toLowerCase();
        if (args.length <= 1) {
            switch (args.length) {
                case 0: {
                    if (commandName.equalsIgnoreCase("mpoints")) {
                        sendHelpMessage(sender, 1);
                        return true;
                    }
                    break;
                }

                case 1: {
                    if (args[0].equalsIgnoreCase("list")) {
                        if (sender.isOp()) {
                            for (String sign : PointsCache.pointsigns) {
                                Points x = PointsCache.getPointFromCache(sign);
                                sender.sendMessage(sendprefix() + sendMessage(sign, "points_list").replace("%sign%", sign));
                            }
                            return true;
                        }
                    }

                    if (args[0].equalsIgnoreCase("reload")) {
                        if (sender.isOp()) {
                            MPoints.getInstance().reloadMessages();
                            if (!MPoints.getInstance().reloadPoints()) {
                                sender.sendMessage(sendprefix() + Messages.systemMessage("§cpoints.yml重载错误"));
                            }
                            sender.sendMessage(sendprefix() + Messages.systemMessage("§amessage.yml 和 points.yml 重载成功"));
                            return true;
                        }
                    }

                    if (args[0].equalsIgnoreCase("version")) {
                        showVersion(sender);
                        return true;
                    }

                    if (args[0].equalsIgnoreCase("help")) {
                        sendHelpMessage(sender, 1);
                        return true;
                    }
                    break;
                }
            }
            return true;
        } else if (args.length <= 2 && args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(sender, Integer.valueOf(args[1]));
            return true;
        } else {

            if (PointsCache.getPointFromCache(args[0]) == null) {
                sender.sendMessage(sendprefix() + sendMessage(null, "points_nosign"));
                return true;
            }
            String commandp = commandName + " " + args[0];
            return CommandHandler.onCommand(sender, 2, args[0], commandp, args[1], args);
        }
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

    public static void showVersion(CommandSender sender) {
        sender.sendMessage(sendMessage(null, "prefix") + "§6 MPoints §f(Version: "
                + MPoints.getInstance().getDescription().getVersion() + ") §6|§7 Author: §f" + Messages.getAuthor());
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
}
