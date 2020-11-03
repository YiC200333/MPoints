package me.yic.mpoints.utils;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.data.caches.PointsCache;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class PointsConfig {

	private final MPoints plugin;
	private static FileConfiguration PointsFile;

	public PointsConfig(MPoints plugin) {
		this.plugin = plugin;
	}

	public boolean load() {
		File file = new File(MPoints.getInstance().getDataFolder(), "points.yml");
		if (!file.exists()) {
			MPoints.getInstance().saveResource("points.yml", false);
		}
		PointsFile = YamlConfiguration.loadConfiguration(file);
		return getPointstoCache(file);
	}

	private boolean getPointstoCache(File path) {
		PointsCache.pointsigns.clear();
		PointsCache.points.clear();
		Boolean update = false;
		ConfigurationSection section = PointsFile.getConfigurationSection("");
		for (String u : section.getKeys(false)) {
			if (update) {
				UpdateConfig.updatepoint(u,PointsFile,path);
			}else{
				update = UpdateConfig.updatepoint(u,PointsFile,path);
			}
			String sign = section.getString(u + ".setting.sign");
			String initialbal = section.getString(u + ".setting.initial-bal");
			Boolean enablebaltop = section.getBoolean(u + ".setting.enable-baltop");
			Boolean allowpay = section.getBoolean(u + ".setting.allow-pay-command");
			Boolean hidemessage = section.getBoolean(u + ".setting.hide-comannd-message");
			Boolean enablebc = section.getBoolean(u + ".setting.enable-bungeecord");
			if (enablebc){
				MPoints.hasbcpoint = true;
			}
			String singularname = section.getString(u + ".display.singular-name");
			String pluralname = section.getString(u + ".display.plural-name");
			Boolean integerbal = section.getBoolean(u + ".display.integer-bal");
			String separator = section.getString(u + ".display.thousands-separator");
			String displayformat = section.getString(u + ".display.display-format");
			String maxnumber = section.getString(u + ".display.max-number");
			if (section.getBoolean(u + ".quick-command.enable")){
				Command qcommand  = new QuickCommand(section.getString(u + ".quick-command.command"),sign);
				MPoints.commandMap.register(section.getString(u + ".quick-command.command"),qcommand);
			}
			if (PointsCache.pointsigns.contains(sign)){
				MPoints.getInstance().logger("Exist the same sign of point");
				return false;
			}
			PointsCache.pointsigns.add(sign);
			Points x = new Points(sign,initialbal,enablebaltop,allowpay,hidemessage,enablebc,
					singularname,pluralname,integerbal,separator,displayformat,maxnumber);
			PointsCache.insertIntoCache(sign,x);
		}
		if (update) {
			try {
				PointsFile.save(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

}
