package me.yic.mpoints;

import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.data.SQL;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.depend.Placeholder;
import me.yic.mpoints.listeners.BSListening;
import me.yic.mpoints.listeners.ConnectionListeners;
import me.yic.mpoints.listeners.SPsync;
import me.yic.mpoints.message.Messages;
import me.yic.mpoints.message.MessagesManager;
import me.yic.mpoints.task.Baltop;
import me.yic.mpoints.utils.PointsConfig;
import me.yic.mpoints.utils.UpdateConfig;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

public class MPoints extends JavaPlugin {

	private static MPoints instance;
	public static FileConfiguration config;
	private MessagesManager messageManager;
	private PointsConfig pointsconfig;
	private BukkitTask refresherTask = null;
	Metrics metrics = null;
	private Placeholder papiExpansion = null;
	public static Boolean ddrivers = false;
	public static Boolean hasbcpoint = false;
	public static CommandMap commandMap = null;

	public void onEnable() {
		instance = this;
		load();
		if (checkup()) {
			//new Updater().runTaskAsynchronously(this);
		}
		// 检查更新
		messageManager = new MessagesManager(this);
		messageManager.load();

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			logger("发现 PlaceholderAPI");
			setupPlaceHolderAPI();
		}

		if (Bukkit.getPluginManager().getPlugin("DatabaseDrivers") != null) {
			logger("发现 DatabaseDrivers");
			ddrivers = true;
		}

		try {
			final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			commandMap = (CommandMap) bukkitCommandMap.get(getServer());
		} catch (Exception e) {
			e.printStackTrace();
		}

		getServer().getPluginManager().registerEvents(new ConnectionListeners(), this);

		metrics = new Metrics(this, 9061);

		Bukkit.getPluginCommand("mpoints").setExecutor(new Commands());

		pointsconfig = new PointsConfig(this);
		if (!pointsconfig.load()) {
			onDisable();
			return;
		}

		if (!DataCon.create()) {
			onDisable();
			return;
		}

		Cache.baltop();

		loadguidshop();

		if (hasbcpoint) {
			if (isBungeecord()) {
				getServer().getMessenger().registerIncomingPluginChannel(this, "mpoints:aca", new SPsync());
				getServer().getMessenger().registerOutgoingPluginChannel(this, "mpoints:acb");
				logger("已开启BC同步");
			} else if (!config.getBoolean("Settings.mysql")) {
				if (config.getString("SQLite.path").equalsIgnoreCase("Default")) {
					logger("SQLite文件路径设置错误");
					logger("BC同步未开启");
				}
			}
		}

		int time = config.getInt("Settings.refresh-time");
		if (time < 30) {
			time = 30;
		}

		refresherTask = new Baltop().runTaskTimerAsynchronously(this, time * 20, time * 20);
		logger("===== YiC =====");

	}

	public void onDisable() {
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			try {
				papiExpansion.unregister();
			} catch (NoSuchMethodError ignored) {
			}
		}

		if (isBungeecord()) {
			getServer().getMessenger().unregisterIncomingPluginChannel(this, "mpoints:aca", new SPsync());
			getServer().getMessenger().unregisterOutgoingPluginChannel(this, "mpoints:acb");
		}

		refresherTask.cancel();
		SQL.close();
		logger("XConomy已成功卸载");
	}

	public static MPoints getInstance() {
		return instance;
	}

	public void reloadMessages() {
		messageManager.load();
	}

	public static boolean allowHikariConnectionPooling() {
		if (!config.getBoolean("Settings.mysql")) {
			return false;
		}
		return MPoints.config.getBoolean("Pool-Settings.usepool");
	}

	public static String getSign() {
		return config.getString("BungeeCord.sign");
	}

	private void setupPlaceHolderAPI() {
		papiExpansion = new Placeholder(this);
		if (papiExpansion.register()) {
			getLogger().info("PlaceholderAPI successfully hooked");
		} else {
			getLogger().info("PlaceholderAPI unsuccessfully hooked");
		}
	}

	public String lang() {
		return config.getString("Settings.language");
	}

	public void logger(String mess) {
		getLogger().info(Messages.systemMessage(mess));
	}

	public static boolean isBungeecord() {
		if (!hasbcpoint) {
			return false;
		}

		if (config.getBoolean("Settings.mysql")) {
			return true;
		}

		return !config.getBoolean("Settings.mysql") & !config.getString("SQLite.path").equalsIgnoreCase("Default");

	}

	public static boolean checkup() {
		return config.getBoolean("Settings.check-update");
	}

	private void load() {
		saveDefaultConfig();
		update_config();
		reloadConfig();
		config = getConfig();
	}

	private void loadguidshop() {
		if (Bukkit.getPluginManager().getPlugin("BossShopPro") != null) {
			getServer().getPluginManager().registerEvents(new BSListening(), this);
		}
	}

	private void update_config() {
		File config = new File(this.getDataFolder(), "config.yml");
		boolean update = UpdateConfig.update(getConfig(), config);
		if (update) {
			saveConfig();
		}
	}
}
