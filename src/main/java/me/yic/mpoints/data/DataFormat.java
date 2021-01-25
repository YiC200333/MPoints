/*
 *  This file (DataFormat.java) is a part of project MPoints
 *  Copyright (C) YiC and contributors
 *
 *  This program is free software: you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 *  Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package me.yic.mpoints.data;

import me.yic.mpoints.utils.Points;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DataFormat {

    public static BigDecimal formatString(String sign, String am) {
        BigDecimal bigDecimal = new BigDecimal(am);
        if (Points.getintegerbal(sign)) {
            return bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
        } else {
            return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
        }
    }

    public static BigDecimal formatDouble(String sign, Double am) {
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

    public static boolean isMAX(String sign, BigDecimal am) {
        return am.compareTo(Points.getmaxnumber(sign)) > 0;
    }

    public static DecimalFormat setformat(Boolean isInteger, String gpoint) {
        DecimalFormat decimalFormat = new DecimalFormat();

        if (!isInteger) {
            decimalFormat.setMinimumFractionDigits(2);
            decimalFormat.setMaximumFractionDigits(2);
        }

        if (gpoint != null && !gpoint.equals("") && gpoint.length() == 1) {
            DecimalFormatSymbols spoint = new DecimalFormatSymbols();
            spoint.setGroupingSeparator(gpoint.charAt(0));
            decimalFormat.setDecimalFormatSymbols(spoint);
        }
        return decimalFormat;
    }


    public static BigDecimal setmaxnumber(String maxn) {
        BigDecimal defaultmaxnumber = new BigDecimal("100000000000000000000000000");
        if (maxn.length() > 27) {
            return defaultmaxnumber;
        }
        BigDecimal mnumber = new BigDecimal(maxn);
        if (mnumber.compareTo(defaultmaxnumber) >= 0) {
            return defaultmaxnumber;
        } else {
            return mnumber;
        }
    }
}
