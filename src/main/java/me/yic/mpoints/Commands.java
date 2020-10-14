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

            case 2: {
                if (args[0].equalsIgnoreCase("balancetop") | args[0].equalsIgnoreCase("baltop")) {
                    if (!(sender.isOp() || sender.hasPermission("mpoints.user.balancetop"))) {
                        sender.sendMessage(sendprefix() + sendMessage(null, "no_permission"));
                        return true;
                    }

                    if (!Cache.baltop_uid.containsKey(args[1])) {
                        sender.sendMessage(sendprefix() + sendMessage(null, "points_nosign"));
                        return true;
                    }

                    if (Cache.baltop_uid.get(args[1]).isEmpty()) {
                        sender.sendMessage(sendprefix() + sendMessage(null, "top_nodata"));
                        return true;
                    }

                    sender.sendMessage(sendMessage(args[1], "top_title"));
                    sender.sendMessage(sendMessage(null, "sum_text")
                            .replace("%balance%", DataFormat.shown(args[1], Cache.sumbalance.get(args[1]))));

                    List<UUID> topuids = Cache.baltop_uid.get(args[1]);
                    int placement = 0;
                    for (UUID uu : topuids) {
                        String topName = Cache.baltop_name.get(args[1]).get(placement);
                        placement++;
                        sender.sendMessage(sendMessage(null, "top_text")
                                .replace("%index%", String.valueOf(placement))
                                .replace("%player%", topName)
                                .replace("%balance%", DataFormat.shown(args[1], Cache.getBalanceFromCacheOrDB(uu, args[1]))));
                    }

                    if (checkMessage("top_subtitle"))
                        sender.sendMessage(sendMessage(null, "top_subtitle"));

					break;
                }

                if (args[0].equalsIgnoreCase("balance") | args[0].equalsIgnoreCase("bal")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(sendprefix() + Messages.systemMessage("§6控制台无法使用该指令"));
                        return true;
                    }

                    if (PointsCache.getPointFromCache(args[1]) == null) {
                        sender.sendMessage(sendprefix() + sendMessage(null, "points_nosign"));
                        return true;
                    }

                    if (!(sender.isOp() || sender.hasPermission("mpoints.user.balance"))) {
                        sender.sendMessage(sendprefix() + sendMessage(null, "no_permission"));
                        return true;
                    }

                    Player player = (Player) sender;

                    if (MPoints.config.getBoolean("Settings.cache-correction")) {
                        Cache.refreshFromCache(player.getUniqueId(), args[1]);
                    }

                    BigDecimal a = Cache.getBalanceFromCacheOrDB(player.getUniqueId(), args[1]);
                    sender.sendMessage(sendprefix() + sendMessage(args[1], "balance")
                            .replace("%balance%", DataFormat.shown(args[1], a)));


					break;
                }

				sendHelpMessage(sender);
				break;
            }

            case 3: {
                if (args[0].equalsIgnoreCase("balance") | args[0].equalsIgnoreCase("bal")) {

                    if (!(sender.isOp() || sender.hasPermission("mpoints.user.balance.other"))) {
                        sender.sendMessage(sendprefix() + sendMessage(null, "no_permission"));
                        return true;
                    }

                    if (PointsCache.getPointFromCache(args[1]) == null) {
                        sender.sendMessage(sendprefix() + sendMessage(null, "points_nosign"));
                        return true;
                    }

                    UUID targetUUID = Cache.translateUUID(args[2]);
                    if (targetUUID == null) {
                        sender.sendMessage(sendprefix() + sendMessage(null, "noaccount"));
                        return true;
                    }

                    BigDecimal targetBalance = Cache.getBalanceFromCacheOrDB(targetUUID, args[1]);
                    sender.sendMessage(sendprefix() + sendMessage(args[1], "balance_other")
                            .replace("%player%", args[2])
                            .replace("%balance%", DataFormat.shown(args[1], targetBalance)));
					break;
                }
				sendHelpMessage(sender);
				break;
            }

			case 4:
			case 5:  {

				if (args.length==4 && args[0].equalsIgnoreCase("pay")) {
					if (!(sender instanceof Player)) {
						sender.sendMessage(sendprefix() + Messages.systemMessage("§6控制台无法使用该指令"));
						return true;
					}

					if (PointsCache.getPointFromCache(args[1]) == null) {
						sender.sendMessage(sendprefix() + sendMessage(null, "points_nosign"));
						return true;
					}

					if (!(sender.isOp() || PointsCache.getPointFromCache(args[1]).getallowpay())) {
						sender.sendMessage(sendprefix() + sendMessage(null, "no_permission"));
						return true;
					}

					if (sender.getName().equalsIgnoreCase(args[2])) {
						sender.sendMessage(sendprefix() + sendMessage(null, "pay_self"));
						return true;
					}

					if (!isDouble(args[1],args[3])) {
						sender.sendMessage(sendprefix() + sendMessage(null, "invalid"));
						return true;
					}

					BigDecimal amount = DataFormat.formatString(args[1],args[3]);

					if (amount.compareTo(BigDecimal.ZERO)<=0){
						sender.sendMessage(sendprefix() + sendMessage(null, "invalid"));
						return true;
					}

					String amountFormatted = DataFormat.shown(args[1],amount);
					BigDecimal bal_sender = Cache.getBalanceFromCacheOrDB(((Player) sender).getUniqueId(),args[1]);

					if (bal_sender.compareTo(amount) < 0) {
						sender.sendMessage(sendprefix() + sendMessage(args[1],"pay_fail")
								.replace("%amount%", amountFormatted));
						return true;
					}

					Player target = Bukkit.getPlayerExact(args[2]);
					UUID targetUUID = Cache.translateUUID(args[2]);
					if (targetUUID == null) {
						sender.sendMessage(sendprefix() + sendMessage(null,"noaccount"));
						return true;
					}

					BigDecimal bal_target = Cache.getBalanceFromCacheOrDB(targetUUID,args[1]);
					if (DataFormat.isMAX(args[1],bal_target.add(amount))) {
						sender.sendMessage(sendprefix() + sendMessage(null,"over_maxnumber"));
						return true;
					}

					String com = commandName + " " + args[0] + " " + args[1] + " " + args[2] + " " + args[3];
					Cache.change(((Player) sender).getUniqueId(), args[1], amount, false, "PLAYER_COMMAND", sender.getName(), com);
					sender.sendMessage(sendprefix() + sendMessage(args[1],"pay")
							.replace("%player%", args[2])
							.replace("%amount%", amountFormatted));

					Cache.change(targetUUID, args[1], amount, true, "PLAYER_COMMAND", args[2], com);
					String mess = sendprefix() + sendMessage(args[1],"pay_receive")
							.replace("%player%", sender.getName())
							.replace("%amount%", amountFormatted);

					if (target == null) {
						broadcastSendMessage(false, args[1], args[2], mess);
						return true;
					}

					target.sendMessage(mess);
					break;
				}

				if (!(sender.isOp() | sender.hasPermission("mpoints.admin.give")
						| sender.hasPermission("mpoints.admin.take") | sender.hasPermission("mpoints.admin.set"))) {
					sendHelpMessage(sender);
					return true;
				}

				if (PointsCache.getPointFromCache(args[1]) == null) {
					sender.sendMessage(sendprefix() + sendMessage(null, "points_nosign"));
					return true;
				}

				if (!check()) {
					sender.sendMessage(sendprefix() + Messages.systemMessage("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令"));
					return true;
				}

				if (!isDouble(args[1],args[3])) {
					sender.sendMessage(sendprefix() + sendMessage(null,"invalid"));
					return true;
				}

				BigDecimal amount = DataFormat.formatString(args[1],args[3]);
				String amountFormatted = DataFormat.shown(args[1],amount);
				Player target = Bukkit.getPlayerExact(args[2]);
				UUID targetUUID = Cache.translateUUID(args[2]);
				String reason = "";
				if (args.length==5) {
					reason = args[4];
				}

				if (targetUUID == null) {
					sender.sendMessage(sendprefix() + sendMessage(null,"noaccount"));
					return true;
				}

				String com = commandName + " " + args[0] + " " + args[1] + " " + args[2] + " " + args[3] + " " + reason;
				switch (args[0].toLowerCase()) {
					case "give": {
						if (!(sender.isOp() | sender.hasPermission("mpoints.admin.give"))) {
							sendHelpMessage(sender);
							return true;
						}

						if (amount.compareTo(BigDecimal.ZERO)<=0){
							sender.sendMessage(sendprefix() + sendMessage(null,"invalid"));
							return true;
						}

						BigDecimal bal = Cache.getBalanceFromCacheOrDB(targetUUID,args[1]);
						if (DataFormat.isMAX(args[1],bal.add(amount))) {
							sender.sendMessage(sendprefix() + sendMessage(null,"over_maxnumber"));
							return true;
						}

						Cache.change(targetUUID, args[1], amount, true,"ADMIN_COMMAND", args[2], com);
						sender.sendMessage(sendprefix() + sendMessage(args[1],"money_give")
								.replace("%player%", args[2])
								.replace("%amount%", amountFormatted));

						if (checkMessage("money_give_receive") | args.length==4) {

							String message = sendprefix() + sendMessage(args[1],"money_give_receive")
									.replace("%player%", args[2])
									.replace("%amount%", amountFormatted);

							if (args.length==5) {
								message = sendprefix() + reason;
							}

							if (target == null) {
								broadcastSendMessage(false, args[1], args[2], message);
								return true;
							}

							target.sendMessage(message);

						}
						break;
					}

					case "take": {
						if (!(sender.isOp() | sender.hasPermission("mpoints.admin.take"))) {
							sendHelpMessage(sender);
							return true;
						}

						if (amount.compareTo(BigDecimal.ZERO)<=0){
							sender.sendMessage(sendprefix() + sendMessage(null,"invalid"));
							return true;
						}

						BigDecimal bal = Cache.getBalanceFromCacheOrDB(targetUUID,args[1]);
						if (bal.compareTo(amount) < 0) {
							sender.sendMessage(sendprefix() + sendMessage(args[1],"money_take_fail")
									.replace("%player%", args[1])
									.replace("%amount%", amountFormatted));

							return true;
						}

						Cache.change(targetUUID, args[1], amount, false,"ADMIN_COMMAND", args[2], com);
						sender.sendMessage(sendprefix() + sendMessage(args[1],"money_take")
								.replace("%player%", args[2])
								.replace("%amount%", amountFormatted));

						if (checkMessage("money_give_receive") | args.length==4) {
							String mess = sendprefix() + sendMessage(args[1],"money_take_receive")
									.replace("%player%", args[2]).replace("%amount%", amountFormatted);

							if (args.length==5) {
								mess = sendprefix() + reason;
							}

							if (target == null) {
								broadcastSendMessage(false, args[1], args[2], mess);
								return true;
							}

							target.sendMessage(mess);

						}
						break;
					}

					case "set": {
						if (!(sender.isOp() | sender.hasPermission("mpoints.admin.set"))) {
							sendHelpMessage(sender);
							return true;
						}

						Cache.change(targetUUID, args[1], amount, null,"ADMIN_COMMAND", args[2], com);
						sender.sendMessage(sendprefix() + sendMessage(args[1],"money_set")
								.replace("%player%", args[2])
								.replace("%amount%", amountFormatted));

						if (checkMessage("money_give_receive") | args.length==4) {
							String mess = sendprefix() + sendMessage(args[1],"money_set_receive")
									.replace("%player%", args[2])
									.replace("%amount%", amountFormatted);

							if (args.length==5) {
								mess = sendprefix() + reason;
							}

							if (target == null) {
								broadcastSendMessage(false, args[1], args[2], mess);
								return true;
							}

							target.sendMessage(mess);

						}
						break;
					}

					default: {
						sendHelpMessage(sender);
						break;
					}

				}
				break;
			}
            default: {
                sendHelpMessage(sender);
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
				return !DataFormat.isMAX(sign,DataFormat.formatString(sign,s));
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
		if (sender.isOp() | sender.hasPermission("xconomy.admin.list")) {
			sender.sendMessage(sendMessage(null, "help6"));
		}
        if (sender.isOp() | sender.hasPermission("xconomy.admin.give")) {
            sender.sendMessage(sendMessage(null, "help7"));
            //sender.sendMessage(sendMessage(null, "help10"));
        }
        if (sender.isOp() | sender.hasPermission("xconomy.admin.take")) {
            sender.sendMessage(sendMessage(null, "help8"));
            //sender.sendMessage(sendMessage(null, "help11"));
        }
        if (sender.isOp() | sender.hasPermission("xconomy.admin.set")) {
            sender.sendMessage(sendMessage(null, "help9"));
        }
		if (sender.isOp()) {
			sender.sendMessage(sendMessage(null, "help12"));
		}
    }

	private static void broadcastSendMessage(boolean ispublic, String sign, String s, String s1) {
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
				output.writeUTF(s);
				output.writeUTF(s1);
			}else{
				output.writeUTF("broadcast");
				output.writeUTF(MPoints.getSign());
				output.writeUTF(sign);
				output.writeUTF(s1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		new SendMessTaskS(stream, null, sign, null,null, null, null).runTaskAsynchronously(MPoints.getInstance());

	}
}
