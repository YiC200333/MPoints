/*
 *  This file (PointsConfig.java) is a part of project MPoints
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
package me.yic.mpoints.info;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.adapter.comp.CConfig;
import me.yic.mpoints.data.DataFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PointsConfig {
    public static CConfig config;
    public final String cpath;
    public final DataFormat DATAFORMAT;
    public PointsConfig(String cpath) {
        this.cpath = cpath;
        this.INITIAL_BAL = config.getDouble(cpath +".settings.initial-bal");
        this.P_SIGN = config.getString(cpath +".settings.sign").toLowerCase();
        this.ENABLE_BALTOP = config.getBoolean(cpath +".settings.enable-baltop");
        this.ALLOW_PAY = config.getBoolean(cpath +".settings.allow-pay-command");
        this.HIDE_OP_COMMAND = config.getBoolean(cpath +".settings.hide-comannd-message");
        this.ENABLE_BC = config.getBoolean(cpath +".settings.enable-bungeecord");

        this.SINGULAR_NAME = config.getString(cpath +".display.singular-name");
        this.PLURAL_NAME = config.getString(cpath +".display.plural-name");
        this.INTEGER_BAL = config.getBoolean(cpath +".display.integer-bal");
        this.THOUSANDS_SEPARATOR = config.getString(cpath +".display.thousands-separator");
        this.DISPLAY_FORMAT = config.getString(cpath +".display.display-format");
        this.MAX_NUMBER = config.getString(cpath +".display.max-number");
        setformatbalance();
        this.DATAFORMAT = new DataFormat(this);
        this.ENABLE_QUICKCOMMAND = config.getBoolean(cpath +".quick-command.enable");
        this.String_COMMAND = config.getString(cpath +".quick-command.command");
    }
    public final String P_SIGN;
    public final double INITIAL_BAL;
    public final boolean ENABLE_BALTOP;
    public final boolean ALLOW_PAY;
    public BigDecimal PAYMENT_TAX = BigDecimal.ZERO;
    public final boolean HIDE_OP_COMMAND;
    public final boolean ENABLE_BC;
    public final String SINGULAR_NAME;
    public final String PLURAL_NAME;
    public final boolean INTEGER_BAL;
    public final String THOUSANDS_SEPARATOR;
    public final String DISPLAY_FORMAT;
    public final String MAX_NUMBER;
    public List<BigDecimal> FORMAT_BALANCE = null;
    public LinkedHashMap<BigDecimal, String> FORMAT_BALANCE_C = null;
    public final boolean ENABLE_QUICKCOMMAND;
    public final String String_COMMAND;

    private void setformatbalance() {
        FORMAT_BALANCE = new ArrayList<>();
        try {
            FORMAT_BALANCE_C = config.getConfigurationSectionSort(cpath + ".display.format-balance");
            FORMAT_BALANCE.addAll(FORMAT_BALANCE_C.keySet());
        } catch (Exception e) {
            e.printStackTrace();
            FORMAT_BALANCE = null;
            MPoints.getInstance().logger(null, 1, "Error getting balance custom format");
            return;
        }
        if (FORMAT_BALANCE.isEmpty()){
            FORMAT_BALANCE = null;
            MPoints.getInstance().logger(null, 1, "Error getting balance custom format");
        }
    }
}
