/*
 *  This file (MPointsBungee.java) is a part of project MPoints
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
package me.yic.mpoints;

import me.yic.mpoints.info.SyncInfo;
import me.yic.mpoints.listeners.BCsync;
import net.md_5.bungee.api.plugin.Plugin;

public class MPointsBungee extends Plugin {
    private static MPointsBungee instance;
    public static String syncversion = SyncInfo.syncversion;

    @Override
    public void onEnable() {

        instance = this;

        getProxy().registerChannel("mpoints:aca");
        getProxy().registerChannel("mpoints:acb");
        getProxy().getPluginManager().registerListener(this, new BCsync());

        getLogger().info("MPoints successfully enabled!");
        getLogger().info("===== YiC =====");

    }

    @Override
    public void onDisable() {
        getLogger().info("MPoints successfully disabled!");
    }

    public static MPointsBungee getInstance() {
        return instance;
    }
}
