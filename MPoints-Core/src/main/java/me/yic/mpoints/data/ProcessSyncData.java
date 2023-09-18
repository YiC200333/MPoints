/*
 *  This file (ProcessSyncData.java) is a part of project MPoints
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

import me.yic.mpoints.MPoints;
import me.yic.mpoints.MPointsLoad;
import me.yic.mpoints.adapter.comp.CPlayer;
import me.yic.mpoints.data.syncdata.SyncData;
import me.yic.mpoints.data.syncdata.SyncMessage;
import me.yic.mpoints.info.SyncInfo;
import me.yic.mpoints.info.SyncType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.UUID;

public class ProcessSyncData {

    public static void process(byte[] message){

        ByteArrayInputStream input = new ByteArrayInputStream(message);
        try {
            ObjectInputStream ios = new ObjectInputStream(input);

            String sv = ios.readUTF();
            if (!sv.equals(MPoints.syncversion)) {
                MPoints.getInstance().logger("收到不同版本插件的数据，无法同步，当前插件版本 ", 1, MPoints.syncversion);
                return;
            }

            SyncData ob = (SyncData) ios.readObject();
            if (ob.getServerKey().equals(SyncInfo.server_key)) {
                return;
            }

            if (!ob.getSign().equals(MPointsLoad.Config.SYNCDATA_SIGN)) {
                return;
            }

            if (ob.getSyncType().equals(SyncType.MESSAGE) || ob.getSyncType().equals(SyncType.MESSAGE_SEMI) ) {
                SyncMessage sd = (SyncMessage) ob;
                UUID muid = sd.getUniqueId();
                if (ob.getSyncType().equals(SyncType.MESSAGE_SEMI)){
                    muid = sd.getRUniqueId();
                }
                CPlayer p = new CPlayer(muid);
                if (p.isOnline()) {
                    p.sendMessage(sd.getMessage());
                }
            }else{
                ob.SyncStart();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
