/*
 *  This file (QuickCommand.java) is a part of project MPoints
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
package me.yic.mpoints.utils;

import me.yic.mpoints.CommandHandler;
import me.yic.mpoints.lang.MessagesManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class QuickCommand extends Command {
    private final String name;
    private final String sign;

    public QuickCommand(String name, String sign) {
        super(name);
        this.name = name;
        this.sign = sign;
        this.description = "MPoints.";
        this.usageMessage = "/<command>";
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }
        return CommandHandler.onCommand(sender, 1, sign, name, args[0], args);
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
