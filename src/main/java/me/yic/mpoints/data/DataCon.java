package me.yic.mpoints.data;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.utils.DatabaseConnection;
import me.yic.mpoints.utils.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class DataCon extends MPoints {

	public static boolean create() {
		if (config.getBoolean("Settings.mysql")) {
			getInstance().logger("数据保存方式 - MySQL");
			setupMySqlTable();

			if (SQL.con()) {
				SQL.getwaittimeout();
				SQL.createTable();
				getInstance().logger("MySQL连接正常");
			} else {
				getInstance().logger("MySQL连接异常");
				return false;
			}

		} else {
			getInstance().logger("数据保存方式 - SQLite");
			setupSqLiteAddress();

			File dataFolder = new File(getInstance().getDataFolder(), "playerdata");
			dataFolder.mkdirs();
			if (SQL.con()) {
				SQL.createTable();
				getInstance().logger("SQLite连接正常");
			} else {
				getInstance().logger("SQLite连接异常");
				return false;
			}

		}
		getInstance().logger("MPoints加载成功");
		return true;
	}

	public static void newPlayer(Player a) {
		SQL.newPlayer(a);
	}

	public static void getBal(UUID u,String sign) {
		SQL.select(u,sign);
	}

	public static void getUid(String name) {
		SQL.selectUID(name);
	}

	public static void getTopBal() {
		SQL.getBaltop();
	}

	public static void setTopBalHide(UUID u,String sign, Integer type) {
		SQL.hidetop(u,sign,type);
	}

	public static void save(UUID UID, String sign, Boolean isAdd, PlayerData pd) {
		SQL.save(UID, sign, isAdd, pd);
	}

	public static void saveall(String sign, String targettype, BigDecimal amount, Boolean isAdd, PlayerData x) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (targettype.equalsIgnoreCase("all")) {
					SQL.saveall(sign, targettype, null, amount, isAdd, x);
				} else if (targettype.equalsIgnoreCase("online")) {
					List<UUID> ol = new ArrayList<UUID>();
					for (Player pp : Bukkit.getOnlinePlayers()) {
						ol.add(pp.getUniqueId());
					}
					SQL.saveall(sign, targettype, ol, amount, isAdd, x);
				}
			}
		}.runTaskAsynchronously(MPoints.getInstance());
	}

	private static void setupMySqlTable() {
		if (config.getString("MySQL.table-suffix") != null & !config.getString("MySQL.table-suffix").equals("")) {
			SQL.suffix = config.getString("MySQL.table-suffix").replace("%sign%", getSign()) + "_";
		}
	}

	private static void setupSqLiteAddress() {
		if (config.getString("SQLite.path").equalsIgnoreCase("Default")) {
			return;
		}

		File folder = new File(config.getString("SQLite.path"));
		if (folder.exists()) {
			DatabaseConnection.userdata = new File(folder, "data.db");
		} else {
			getInstance().logger("自定义文件夹路径不存在");
		}

	}
}
