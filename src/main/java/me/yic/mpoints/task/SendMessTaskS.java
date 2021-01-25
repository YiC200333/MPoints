/*
 *  This file (SendMessTaskS.java) is a part of project MPoints
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