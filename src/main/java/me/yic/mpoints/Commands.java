package me.yic.mpoints;

import me.yic.mpoints.data.DataFormat;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.data.caches.PointsCache;
import me.yic.mpoints.message.Messages;
import me.yic.mpoints.message.MessagesManager;
import me.yic.mpoints.task.SendMessTaskS;
import me.yic.mpoints.utils.Points;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String commandName = cmd.getName().toLowerCase();
        if (args.length<=1) {
			switch (args.length) {
				case 0: {
					if (commandName.equalsIgnoreCase("mpoints")) {
						sendHelpMessage(sender);
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
							sender.sendMessage(sendprefix() + Messages.systemMessage("§amessage.yml重载成功"));
							return true;
						}
					}

					if (args[0].equalsIgnoreCase("version")) {
						showVersion(sender);
						return true;
					}

					if (args[0].equalsIgnoreCase("help")) {
						sendHelpMessage(sender);
						return true;
					}
					break;
				}
			}
			return true;
		}else{

			if (PointsCache.getPointFromCache(args[0]) == null) {
				sender.sendMessage(sendprefix() + sendMessage(null, "points_nosign"));
				return true;
			}
			String commandp = commandName + " " + args[0];
			return CommandHandler.onCommand(sender,2,args[0],commandp,args[1],args);
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

    private static void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(sendMessage(null, "help_title_full"));
        sender.sendMessage(sendMessage(null, "help1"));
        sender.sendMessage(sendMessage(null, "help2"));
        sender.sendMessage(sendMessage(null, "help3"));
        sender.sendMessage(sendMessage(null, "help4"));
		sender.sendMessage(sendMessage(null, "help5"));
		if (sender.isOp() | sender.hasPermission("mpoints.admin.list")) {
			sender.sendMessage(sendMessage(null, "help6"));
		}
        if (sender.isOp() | sender.hasPermission("mpoints.admin.give")) {
            sender.sendMessage(sendMessage(null, "help7"));
            sender.sendMessage(sendMessage(null, "help10"));
        }
        if (sender.isOp() | sender.hasPermission("mpoints.admin.take")) {
            sender.sendMessage(sendMessage(null, "help8"));
            sender.sendMessage(sendMessage(null, "help11"));
        }
        if (sender.isOp() | sender.hasPermission("mpoints.admin.set")) {
            sender.sendMessage(sendMessage(null, "help9"));
        }
		if (sender.isOp() | sender.hasPermission("mpoints.admin.balancetop")) {
			sender.sendMessage(sendMessage(null, "help12"));
		}
		if (sender.isOp()) {
			sender.sendMessage(sendMessage(null, "help13"));
		}
    }
}
