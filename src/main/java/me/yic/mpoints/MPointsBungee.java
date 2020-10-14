package me.yic.mpoints;

import me.yic.mpoints.listeners.BCsync;
import net.md_5.bungee.api.plugin.Plugin;

public class MPointsBungee extends Plugin {
	private static MPointsBungee instance;

	@Override
	public void onEnable() {

		instance = this;

		getProxy().registerChannel("mpoints:aca");
		getProxy().registerChannel("mpoints:acb");
		getProxy().getPluginManager().registerListener(this, new BCsync());

		getLogger().info("MPoints successfully enabled!");
		getLogger().info("===== YiC =====");

	}

	@Override
	public void onDisable() {
		getLogger().info("MPoints successfully disabled!");
	}

	public static MPointsBungee getInstance() {
		return instance;
	}
}
