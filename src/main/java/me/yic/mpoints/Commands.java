/*
 *  This file (Commands.java) is a part of project MPoints
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
package me.yic.mpoints;


import me.yic.mpoints.lang.MessagesManager;
import me.yic.mpoints.utils.Points;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        String commandName = cmd.getName().toLowerCase();
        if (args.length <= 1) {
            switch (args.length) {
                case 0: {
                    if (commandName.equalsIgnoreCase("mpoints")) {
                        CommandHandler.sendHelpMessage(sender, 1);
                        return true;
                    }
                    break;
                }

                case 1: {
                    if (args[0].equalsIgnoreCase("list")) {
                        if (sender.isOp()) {
                            for (String sign : Points.pointsigns.keySet()) {
                                sender.sendMessage(sendprefix() + sendMessage(sign, "points_list").replace("%sign%", sign));
                            }
                            return true;
                        }
                    }

                    if (args[0].equalsIgnoreCase("reload")) {
                        if (sender.isOp()) {
                            MPoints.getInstance().reloadMessages();
                            if (!MPoints.getInstance().reloadPoints()) {
                                sender.sendMessage(sendprefix() + MessagesManager.systemMessage("§cpoints.yml重载错误"));
                            }
                            sender.sendMessage(sendprefix() + MessagesManager.systemMessage("§amessage.yml 和 points.yml 重载成功"));
                            return true;
                        }
                    }

                    if (args[0].equalsIgnoreCase("version")) {
                        showVersion(sender);
                        return true;
                    }

                    if (args[0].equalsIgnoreCase("help")) {
                        CommandHandler.sendHelpMessage(sender, 1);
                        return true;
                    }
                    break;
                }
            }
            return true;
        } else if (args.length <= 2 && args[0].equalsIgnoreCase("help")) {
            CommandHandler.sendHelpMessage(sender, Integer.valueOf(args[1]));
            return true;
        } else {

            if (!Points.pointsigns.containsKey(args[0])) {
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
                    replace("%pointname%", Points.getpluralname(sign));
        }
    }

    public static void showVersion(CommandSender sender) {
        sender.sendMessage(sendMessage(null, "prefix") + "§6 MPoints §f(Version: "
                + MPoints.getInstance().getDescription().getVersion() + ") §6|§7 Author: §f" + MessagesManager.getAuthor());
        String trs = MessagesManager.getTranslatorS();
        if (trs != null) {
            sender.sendMessage(sendMessage(null, "prefix") + "§7 Translator (system): §f" + trs);
        }
    }
}
