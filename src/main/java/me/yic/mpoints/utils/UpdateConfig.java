package me.yic.mpoints.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class UpdateConfig {

	public static boolean update(FileConfiguration config, File cc) {
		boolean update = false;
		FileConfiguration ck = YamlConfiguration.loadConfiguration(cc);
		//if (!ck.contains("MySQL.table_suffix")) {
		//	config.createSection("MySQL.table_suffix");
		//	config.set("MySQL.table_suffix", "");
		//	update = true;
		//}
		return update;
	}


	public static boolean updatepoint(String u, FileConfiguration config, File cc) {
		boolean update = false;
		FileConfiguration ck = YamlConfiguration.loadConfiguration(cc);
		if (!ck.contains(u + ".setting.hide-comannd-message")) {
			config.createSection(u + ".setting.hide-comannd-message");
			config.set(u + ".setting.hide-comannd-message", false);
			update = true;
		}
		return update;
	}
}
