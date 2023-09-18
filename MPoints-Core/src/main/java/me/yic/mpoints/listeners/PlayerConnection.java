/*
 *  This file (PlayerConnection.java) is a part of project MPoints
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
package me.yic.mpoints.listeners;

import me.yic.mpoints.AdapterManager;
import me.yic.mpoints.MPointsLoad;
import me.yic.mpoints.adapter.comp.CPlayer;
import me.yic.mpoints.data.DataCon;
import me.yic.mpoints.data.DataLink;
import me.yic.mpoints.data.syncdata.tab.SyncTabJoin;

public class PlayerConnection{

    public static void onJoin(CPlayer player) {

        if (MPointsLoad.DConfig.canasync) {
            AdapterManager.runTaskAsynchronously(() -> DataLink.newPlayer(player));
        } else {
            DataLink.newPlayer(player);
        }

        if (MPointsLoad.getSyncData_Enable()) {
            DataCon.SendMessTask(new SyncTabJoin(player.getName()));
        }
        if (!AdapterManager.Tab_PlayerList.contains(player.getName())) {
            AdapterManager.Tab_PlayerList.add(player.getName());
        }

    }


}
