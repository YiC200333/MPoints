package me.yic.mpoints.listeners;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.task.Updater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ConnectionListeners implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (Bukkit.getOnlinePlayers().size() == 1) {
			Cache.clearCache();
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player a = event.getPlayer();
		new BukkitRunnable() {
			@Override
			public void run() {
				DataCon.newPlayer(a);
			}
		}.runTaskAsynchronously(MPoints.getInstance());
		Cache.translateUUID(a.getName(), a);

		if (a.isOp()) {
			notifyUpdate(a);
		}
	}


	private void notifyUpdate(Player player) {
		if (!(MPoints.checkup() & Updater.old)) {
			return;
		}

		if (MPoints.getInstance().lang().equalsIgnoreCase("Chinese")
				| MPoints.getInstance().lang().equalsIgnoreCase("ChineseTW")) {
			player.sendMessage("§f[MPoints]§b发现新版本 " + Updater.newVersion);
			player.sendMessage("§f[MPoints]§ahttps://www.mcbbs.net/thread-962904-1-1.html");
		} else {
			player.sendMessage("§f[MPoints]§bDiscover the new version " + Updater.newVersion);
			player.sendMessage("§f[MPoints]§ahttps://www.spigotmc.org/resources/xconomy.75669/");
		}

	}

}
