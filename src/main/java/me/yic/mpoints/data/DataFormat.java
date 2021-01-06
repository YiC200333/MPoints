package me.yic.mpoints.data;

import me.yic.mpoints.utils.Points;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DataFormat {

	public static BigDecimal formatString(String sign,String am) {
		BigDecimal bigDecimal = new BigDecimal(am);
		if (Points.getintegerbal(sign)) {
			return bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
		} else {
			return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
		}
	}

	public static BigDecimal formatDouble(String sign,Double am) {
		BigDecimal bigDecimal = new BigDecimal(String.valueOf(am));
		if (Points.getintegerbal(sign)) {
			return bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
		} else {
			return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
		}
	}

	public static String shown(String sign, BigDecimal am) {
		if (am.compareTo(BigDecimal.ONE) > 0) {
			return ChatColor.translateAlternateColorCodes('&', Points.getdisplayformat(sign)
					.replace("%balance%", Points.getdecimalFormat(sign).format(am))
					.replace("%currencyname%", Points.getpluralname(sign)));
		}
		return ChatColor.translateAlternateColorCodes('&', Points.getdisplayformat(sign)
				.replace("%balance%", Points.getdecimalFormat(sign).format(am))
				.replace("%currencyname%", Points.getsingularname(sign)));
	}

	public static String shownd(String sign, Double am) {
		if (am > 1) {
			return ChatColor.translateAlternateColorCodes('&', Points.getdisplayformat(sign)
					.replace("%balance%", Points.getdecimalFormat(sign).format(am))
					.replace("%currencyname%", Points.getpluralname(sign)));
		}
		return ChatColor.translateAlternateColorCodes('&', Points.getdisplayformat(sign)
				.replace("%balance%", Points.getdecimalFormat(sign).format(am))
				.replace("%currencyname%", Points.getsingularname(sign)));
	}

	public static boolean isMAX(String sign,BigDecimal am) {
		return am.compareTo(Points.getmaxnumber(sign)) > 0;
	}

	public static DecimalFormat setformat(Boolean isInteger, String gpoint) {
		String format = "###,##0";
		DecimalFormat decimalFormat;
		if (isInteger) {
			decimalFormat = new DecimalFormat(format);
			return decimalFormat;
		}
		format = format + ".00";
		decimalFormat = new DecimalFormat(format);
		DecimalFormatSymbols spoint = new DecimalFormatSymbols();
		spoint.setDecimalSeparator('.');
		if (gpoint.length()==1){
			spoint.setGroupingSeparator(gpoint.charAt(0));
		}
		decimalFormat.setDecimalFormatSymbols(spoint);
		return decimalFormat;
	}


	public static BigDecimal setmaxnumber(String maxn) {
		BigDecimal defaultmaxnumber = new BigDecimal("100000000000000000000000000");
		if (maxn.length()>27){
			return defaultmaxnumber;
		}
		BigDecimal mnumber = new BigDecimal(maxn);
		if (mnumber.compareTo(defaultmaxnumber)>=0){
			return defaultmaxnumber;
		}else{
			return mnumber;
		}
	}
}
