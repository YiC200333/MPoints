package me.yic.mpoints.utils;

import me.yic.mpoints.AdapterManager;
import me.yic.mpoints.MPoints;
import me.yic.mpoints.data.syncdata.SyncData;

public class SendPluginMessage {
    public static void SendMessTask(String channel, SyncData sd) {
        if (!AdapterManager.PLUGIN.getOnlinePlayersisEmpty()) {
            AdapterManager.PLUGIN.sendPluginMessage(channel, sd.toByteArray(MPoints.syncversion));
        }
    }
}
