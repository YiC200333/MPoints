package me.yic.mpoints.utils;

import me.yic.mpoints.MPoints;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PointsConfig extends Points{

	private final MPoints plugin;

	public PointsConfig(MPoints plugin) {
		this.plugin = plugin;
	}

	public boolean load() {
		File file = new File(MPoints.getInstance().getDataFolder(), "points.yml");
		if (!file.exists()) {
			MPoints.getInstance().saveResource("points.yml", false);
		}
		PointsFile = YamlConfiguration.loadConfiguration(file);
		return LoadPoints(file);
	}

	private boolean LoadPoints(File path) {
		pointsigns.clear();
		Boolean update = false;
		ConfigurationSection section = PointsFile.getConfigurationSection("");
		for (String u : section.getKeys(false)) {
			if (section.contains(u + ".setting")){
				settingstring = ".setting";
			}
			if (update) {
				UpdateConfig.updatepoint(u,PointsFile,path);
			}else{
				update = UpdateConfig.updatepoint(u,PointsFile,path);
			}
			String sign = section.getString(u + settingstring + ".sign");
			Boolean enablebc = section.getBoolean(u + settingstring + ".enable-bungeecord");
			if (enablebc){
				MPoints.hasbcpoint = true;
			}
			if (section.getBoolean(u + ".quick-command.enable")){
				Command qcommand  = new QuickCommand(section.getString(u + ".quick-command.command"),sign);
				MPoints.commandMap.register(section.getString(u + ".quick-command.command"),qcommand);
			}
			if (pointsigns.containsKey(sign)){
				MPoints.getInstance().logger("Exist the same sign of point");
				return false;
			}
			pointsigns.put(sign, u);
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
