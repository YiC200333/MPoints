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

import me.yic.mpoints.adapter.comp.CChat;
import me.yic.mpoints.info.DefaultConfig;
import me.yic.mpoints.info.PointsConfig;
import net.md_5.bungee.api.ChatColor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DataFormat {
    DecimalFormat decimalFormat;
    DecimalFormat decimalFormatX;
    BigDecimal maxNumber;
    final PointsConfig pconfig;
    public DataFormat(PointsConfig pconfig){
        this.pconfig = pconfig;
        load();
    }

    public BigDecimal formatString(String am) {
        BigDecimal bigDecimal = new BigDecimal(am);
        if (pconfig.INTEGER_BAL) {
            return bigDecimal.setScale(0, RoundingMode.DOWN);
        } else {
            return bigDecimal.setScale(2, RoundingMode.DOWN);
        }
    }

    public BigDecimal formatdouble(double am) {
        BigDecimal bigDecimal = BigDecimal.valueOf(am);
        if (pconfig.INTEGER_BAL) {
            return bigDecimal.setScale(0, RoundingMode.DOWN);
        } else {
            return bigDecimal.setScale(2, RoundingMode.DOWN);
        }
    }

    public BigDecimal formatBigDecimal(BigDecimal am) {
        if (pconfig.INTEGER_BAL) {
            return am.setScale(0, RoundingMode.DOWN);
        } else {
            return am.setScale(2, RoundingMode.DOWN);
        }
    }

    public String shown(BigDecimal am) {
        if (am.compareTo(BigDecimal.ONE) == 0) {
            return CChat.translateAlternateColorCodes('&', pconfig.DISPLAY_FORMAT
                    .replace("%balance%", decimalFormat.format(am))
                    .replace("%format_balance%", getformatbalance(am))
                    .replace("%currencyname%", pconfig.SINGULAR_NAME));
        }
        return CChat.translateAlternateColorCodes('&', pconfig.DISPLAY_FORMAT
                .replace("%balance%", decimalFormat.format(am))
                .replace("%format_balance%", getformatbalance(am))
                .replace("%currencyname%", pconfig.PLURAL_NAME));
    }


    public String shown(double am) {
        return shown(BigDecimal.valueOf(am));
    }

    public String PEshownf(BigDecimal am) {
        if (am.compareTo(BigDecimal.ONE) == 0) {
            return ChatColor.translateAlternateColorCodes('&', pconfig.DISPLAY_FORMAT
                    .replace("%balance%", getformatbalance(am))
                    .replace("%format_balance%", getformatbalance(am))
                    .replace("%currencyname%", pconfig.SINGULAR_NAME));
        }
        return ChatColor.translateAlternateColorCodes('&', pconfig.DISPLAY_FORMAT
                .replace("%balance%", getformatbalance(am))
                .replace("%format_balance%", getformatbalance(am))
                .replace("%currencyname%", pconfig.PLURAL_NAME));
    }

    public boolean isMAX(BigDecimal am) {
        return am.compareTo(maxNumber) > 0;
    }

    public void load() {
        maxNumber = setmaxnumber();
        String gpoint = pconfig.THOUSANDS_SEPARATOR;
        decimalFormat = new DecimalFormat();
        decimalFormatX = new DecimalFormat();

        decimalFormatX.setMinimumFractionDigits(2);
        decimalFormatX.setMaximumFractionDigits(2);

        if (pconfig.INTEGER_BAL) {
            decimalFormat.setMinimumFractionDigits(0);
            decimalFormat.setMaximumFractionDigits(0);
        }else{
            decimalFormat.setMinimumFractionDigits(2);
            decimalFormat.setMaximumFractionDigits(2);
        }

        if (gpoint != null && gpoint.length() == 1) {
            DecimalFormatSymbols spoint = new DecimalFormatSymbols();
            spoint.setGroupingSeparator(gpoint.charAt(0));
            decimalFormat.setDecimalFormatSymbols(spoint);
            decimalFormatX.setDecimalFormatSymbols(spoint);
        }

        pconfig.PAYMENT_TAX = setpaymenttax();
    }


    private BigDecimal setmaxnumber() {
        String maxn = pconfig.MAX_NUMBER;
        BigDecimal defaultmaxnumber = new BigDecimal("10000000000000000");
        if (maxn == null) {
            return defaultmaxnumber;
        }
        if (maxn.length() > 17) {
            return defaultmaxnumber;
        }
        BigDecimal mnumber = new BigDecimal(maxn);
        if (mnumber.compareTo(defaultmaxnumber) >= 0) {
            return defaultmaxnumber;
        } else {
            return mnumber;
        }
    }


    private static BigDecimal setpaymenttax() {
        double pt = DefaultConfig.config.getDouble("Settings.payment-tax");
        if (pt < 0.0) {
            pt = 0.0;
        }
        return BigDecimal.valueOf(pt).add(BigDecimal.ONE);
    }

    private String getformatbalance(BigDecimal bal) {
        if (pconfig.FORMAT_BALANCE != null) {
            if (bal.compareTo(pconfig.FORMAT_BALANCE.get(0)) < 0) {
                return decimalFormat.format(bal);
            }
            BigDecimal x = BigDecimal.ZERO;
            for (BigDecimal b : pconfig.FORMAT_BALANCE) {
                if (bal.compareTo(b) >= 0) {
                    x = b;
                } else {
                    break;
                }
            }
            BigDecimal aa = bal.divide(x, 3, RoundingMode.DOWN);
            return decimalFormatX.format(aa) + pconfig.FORMAT_BALANCE_C.get(x);
        } else {
            return decimalFormat.format(bal);
        }
    }
}
