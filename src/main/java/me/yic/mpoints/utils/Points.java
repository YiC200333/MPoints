/*
 *  This file (Points.java) is a part of project MPoints
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
package me.yic.mpoints.utils;

import me.yic.mpoints.data.DataFormat;
import org.bukkit.configuration.file.FileConfiguration;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;

public class Points {

    public static String settingstring = ".settings";

    public static FileConfiguration PointsFile;
    public static HashMap<String, String> pointsigns = new HashMap<>();

    private static BigDecimal formatString(Boolean isInteger, String am) {
        BigDecimal bigDecimal = new BigDecimal(am);
        if (isInteger) {
            return bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
        } else {
            return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
        }
    }

    public static BigDecimal getinitialbal(String sign) {
        String initialbal = PointsFile.getString(pointsigns.get(sign) + settingstring + ".initial-bal");
        return formatString(getintegerbal(sign), initialbal);
    }

    public static Boolean getenablebaltop(String sign) {
        return PointsFile.getBoolean(pointsigns.get(sign) + settingstring + ".enable-baltop");
    }

    public static Boolean getallowpay(String sign) {
        return PointsFile.getBoolean(pointsigns.get(sign) + settingstring + ".allow-pay-command");
    }

    public static Boolean gethidemessage(String sign) {
        return PointsFile.getBoolean(pointsigns.get(sign) + settingstring + ".hide-comannd-message");
    }

    public static Boolean getenablebc(String sign) {
        return PointsFile.getBoolean(pointsigns.get(sign) + settingstring + ".enable-bungeecord");
    }

    public static String getsingularname(String sign) {
        return PointsFile.getString(pointsigns.get(sign) + ".display.singular-name");
    }

    public static String getpluralname(String sign) {
        return PointsFile.getString(pointsigns.get(sign) + ".display.plural-name");
    }

    public static Boolean getintegerbal(String sign) {
        return PointsFile.getBoolean(pointsigns.get(sign) + ".display.integer-bal");
    }

    public static DecimalFormat getdecimalFormat(String sign) {
        String separator = PointsFile.getString(pointsigns.get(sign) + ".display.thousands-separator");
        return DataFormat.setformat(getintegerbal(sign), separator);
    }

    public static String getdisplayformat(String sign) {
        return PointsFile.getString(pointsigns.get(sign) + ".display.display-format");
    }

    @SuppressWarnings("ConstantConditions")
    public static BigDecimal getmaxnumber(String sign) {
        return DataFormat.setmaxnumber(PointsFile.getString(pointsigns.get(sign) + ".display.max-number"));
    }

}
