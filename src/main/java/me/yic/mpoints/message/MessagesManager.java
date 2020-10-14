package me.yic.mpoints.message;

import me.yic.mpoints.MPoints;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessagesManager {
	private final MPoints plugin;
	public static FileConfiguration messageFile;

	public MessagesManager(MPoints plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void load() {
		File file = new File(MPoints.getInstance().getDataFolder(), "message.yml");
		boolean translate = false;

		if (!file.exists()) {
			try {
				file.createNewFile();
				translate = true;
				plugin.logger("已创建一个新的语言文件");
			} catch (IOException e) {
				e.printStackTrace();
				plugin.logger("语言文件创建异常");
			}
		}

		messageFile = YamlConfiguration.loadConfiguration(file);
		Languages.compare(plugin.lang(), file);
		if (translate) {
			Languages.translatorName(plugin.lang(), file);
		}
	}
}
