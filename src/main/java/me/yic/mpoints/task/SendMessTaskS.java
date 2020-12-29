package me.yic.mpoints.task;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.utils.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class SendMessTaskS extends BukkitRunnable {
	private final ByteArrayOutputStream stream;
	private final UUID u;
	private final String sign;
	private final Boolean isAdd;
	private final PlayerData x;

	public SendMessTaskS(ByteArrayOutputStream stream, UUID u, String sign, Boolean isAdd, PlayerData x) {
		this.stream = stream;
		this.u = u;
		this.sign = sign;
		this.isAdd = isAdd;
		this.x = x;
	}

	@Override
	public void run() {
		Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(MPoints.getInstance(), "mpoints:acb", this.stream.toByteArray());
		if (u != null) {
			DataCon.save(u, sign, isAdd, x);
		}
	}
}