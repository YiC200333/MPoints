package me.yic.mpoints;

import me.yic.mpoints.data.DataFormat;
import me.yic.mpoints.data.caches.Cache;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class MPointsAPI {

	public static String getversion() {
		return MPoints.getInstance().getDescription().getVersion();
	}

	public static Boolean isbungeecordmode() {
		return MPoints.isBungeecord();
	}

	public static UUID translateUUID(String playername) {
		return Cache.translateUUID(playername);
	}

	public static BigDecimal formatdouble(String sign, String amount) {
		return DataFormat.formatString(sign, amount);
	}

	public static String getdisplay(String sign, BigDecimal balance) {
		return DataFormat.shown(sign, balance);
	}

	public static BigDecimal getbalance(String sign, UUID uid) {
		return Cache.getBalanceFromCacheOrDB(uid,sign);
	}

	public static Boolean ismaxnumber(String sign, BigDecimal amount) {
		return DataFormat.isMAX(sign, amount);
	}

	public static Integer changebalance(String sign, UUID u, String playername, BigDecimal amount, Boolean isadd) {
		if (MPoints.isBungeecord() & Bukkit.getOnlinePlayers().isEmpty()) {
			return 1;
		}
		BigDecimal bal = getbalance(sign, Cache.translateUUID(playername));
		if (isadd){
			if (ismaxnumber(sign, bal.add(amount))){
				return 3;
			}
		}else {
			if (bal.compareTo(amount) < 0) {
				return 2;
			}
		}
		Cache.change(u ,sign, amount ,isadd ,"PLUGIN_API" , playername, "N/A");
		return 0;
	}

	public static List<String> getbalancetop(String sign) {
		return Cache.baltop_name.get(sign);
	}

	public static BigDecimal getsumbalance(String sign) {
		return Cache.sumbal(sign);
	}
}
