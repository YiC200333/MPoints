package me.yic.mpoints.task;

import me.yic.mpoints.data.SQL;
import me.yic.mpoints.data.caches.Cache;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Baltop extends BukkitRunnable {

	@Override
	public void run() {
		Cache.baltop_uid.clear();
		Cache.baltop_name.clear();
		SQL.getBaltop();
		if (Bukkit.getOnlinePlayers().size() == 0) {
			Cache.clearCache();
		}
	}
}
