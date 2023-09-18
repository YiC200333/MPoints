package me.yic.mpoints.adapter.comp;


import me.yic.mpoints.adapter.iPlugin;
import me.yic.mpoints.data.syncdata.PlayerData;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class CPlugin implements iPlugin {
    @Override
    public CPlayer getplayer(PlayerData pd) {
        return null;
    }

    @Override
    public boolean getOnlinePlayersisEmpty() {
        return false;
    }

    @Override
    public List<UUID> getOnlinePlayersUUIDs() {
        return null;
    }

    @Override
    public void broadcastMessage(String message) {

    }

    @Override
    public void runTaskAsynchronously(Runnable ra) {

    }

    @Override
    public void runTaskLaterAsynchronously(Runnable ra, long time) {

    }

    @Override
    public void sendPluginMessage(String channel, ByteArrayOutputStream stream) {

    }

    @Override
    public void registerIncomingPluginChannel(String channel, String classname) {

    }

    @Override
    public void registerOutgoingPluginChannel(String channel) {

    }

    @Override
    public void unregisterIncomingPluginChannel(String channel, String classname) {

    }

    @Override
    public void unregisterOutgoingPluginChannel(String channel) {

    }
}
