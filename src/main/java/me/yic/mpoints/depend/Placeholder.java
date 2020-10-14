package me.yic.mpoints.depend;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.data.DataFormat;
import me.yic.mpoints.data.caches.Cache;
import me.yic.mpoints.data.caches.PointsCache;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import java.math.BigDecimal;
import java.util.UUID;

public class Placeholder extends PlaceholderExpansion{

	private final MPoints plugin;

	public Placeholder(MPoints plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getAuthor() {
		return "YiC";
	}

	@Override
	public String getIdentifier() {
		return "mpoints";
	}

	@Override
	public String onRequest(OfflinePlayer player, String identifier) {
		if (!identifier.contains("_sign_")){
			return "[MPoints]NOT EXIST SIGN";
		}
		String sign = identifier.substring(identifier.indexOf("_sign_") + 6);
		if (PointsCache.getPointFromCache(sign) == null){
			return "[MPoints]NOT EXIST SIGN";
		}
		if (identifier.contains("balance_sign_")) {
			if (player == null) {
			return "0.0";
		    }
			BigDecimal a = Cache.getBalanceFromCacheOrDB(player.getUniqueId(),sign);
			return DataFormat.shown(sign,a);
		} else if (identifier.contains("balance_value_sign_")) {
			if (player == null) {
				return "0.0";
			}
			BigDecimal bal = Cache.getBalanceFromCacheOrDB(player.getUniqueId(),sign);
			return bal.toString();
		} else if (identifier.contains("top_player_")) {
			String index = identifier.substring(identifier.indexOf("top_player_") + 11,identifier.indexOf("_sign_"));
			if (isNumber(index)) {
				if (outindex(sign,index)) {
					return "NO DATA";
				}
				int ii = Integer.parseInt(index) - 1;
				return Cache.baltop_name.get(sign).get(ii);
			} else {
				return "[MPoints]Invalid index";
			}
		} else if (identifier.contains("top_balance_")) {
			String index = identifier.substring(identifier.indexOf("top_balance_") + 12,identifier.indexOf("_sign_"));
			if (identifier.contains("top_balance_value")) {
				index = identifier.substring(identifier.indexOf("top_balance_value_") + 18,identifier.indexOf("_sign_"));
			}
			if (isNumber(index)) {
				if (outindex(sign,index)) {
					return "NO DATA";
				}
				int indexInt = Integer.parseInt(index) - 1;
				UUID uu = Cache.baltop_uid.get(sign).get(indexInt);
				BigDecimal bal = Cache.getBalanceFromCacheOrDB(uu,sign);
				if (identifier.contains("top_balance_value")) {
					return bal.toString();
				}
				return DataFormat.shown(sign,bal);
			} else {
				return "[MPoints]Invalid index";
			}
		} else if (identifier.contains("sum_balance")) {
			if (Cache.sumbalance == null) {
				return "0.0";
			}
			BigDecimal bal = Cache.sumbalance.get(sign);
			if (identifier.contains("sum_balance_value")) {
				return bal.toString();
			}
			return DataFormat.shown(sign,bal);
		}
		return null;
	}

	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	private boolean isNumber(String str) {
		if (str.equals("10")) {
			return true;
		}
		if (str.equals("0")) {
			return false;
		}
		return str.matches("[0-9]");
	}

	private boolean outindex(String sign, String str) {
		return Integer.parseInt(str) > Cache.baltop_uid.get(sign).size();
	}
}
