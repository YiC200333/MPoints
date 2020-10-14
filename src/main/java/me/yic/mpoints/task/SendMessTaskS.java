package me.yic.mpoints.task;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.utils.RecordData;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.UUID;

public class SendMessTaskS extends BukkitRunnable {
	private final ByteArrayOutputStream stream;
	private final UUID u;
	private final String sign;
	private final BigDecimal balance;
	private final BigDecimal amount;
	private final Boolean type;
	private final RecordData x;

	public SendMessTaskS(ByteArrayOutputStream stream, UUID u, String sign, BigDecimal balance, BigDecimal amount, Boolean type, RecordData x) {
		this.stream = stream;
		this.u = u;
		this.sign = sign;
		this.balance = balance;
		this.amount = amount;
		this.type = type;
		this.x = x;
	}

	@Override
	public void run() {
		Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(MPoints.getInstance(), "mpoints:acb", this.stream.toByteArray());
		if (u != null) {
			DataCon.save(u, sign, balance, amount, type, x);
		}
	}
}