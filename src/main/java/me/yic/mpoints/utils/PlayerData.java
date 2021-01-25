/*
 *  This file (PlayerData.java) is a part of project MPoints
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class PlayerData {
    private final String type;
    private final String uid;
    private final String player;
    private final String sign;
    private final BigDecimal balance;
    private final BigDecimal amount;
    private final BigDecimal newbalance;
    private final String operation;
    private final String date;
    private String command;

    public PlayerData(String type, UUID uid, String player, String sign, BigDecimal balance, BigDecimal amount, BigDecimal newbalance, Boolean isAdd, String command) {
        this.type = type;
        if (uid == null) {
            this.uid = "N/A";
        } else {
            this.uid = uid.toString();
        }
        if (player == null) {
            this.player = "N/A";
        } else {
            this.player = player;
        }
        this.sign = sign;
        this.balance = balance;
        this.amount = amount;
        this.newbalance = newbalance;
        String operation = "SET";
        if (isAdd != null) {
            if (isAdd) {
                operation = "DEPOSIT";
            } else {
                operation = "WITHDRAW";
            }
        }
        this.operation = operation;
        this.date = (new Date()).toString();
        this.command = command;
    }

    public String gettype() {
        return type;
    }

    public String getuid() {
        return uid;
    }

    public String getsign() {
        return sign;
    }

    public String getplayer() {
        return player;
    }

    public BigDecimal getbalance() {
        return balance;
    }

    public BigDecimal getamount() {
        return amount;
    }

    public BigDecimal getnewbalance() {
        return newbalance;
    }

    public String getoperation() {
        return operation;
    }

    public String getdate() {
        return date;
    }

    public String getcommand() {
        return command;
    }

    public void addcachecorrection() {
        command = command + "   (Cache Correction)";
    }
}
