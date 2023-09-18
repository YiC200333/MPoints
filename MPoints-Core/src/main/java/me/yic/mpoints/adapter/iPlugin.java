package me.yic.mpoints.adapter;


import me.yic.mpoints.adapter.comp.CPlayer;
import me.yic.mpoints.data.syncdata.PlayerData;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public interface iPlugin {
    CPlayer getplayer(PlayerData pd);
    boolean getOnlinePlayersisEmpty();

    List<UUID> getOnlinePlayersUUIDs();

    void broadcastMessage(String message);

    void runTaskAsynchronously(Runnable ra);

    void runTaskLaterAsynchronously(Runnable ra, long time);

    void sendPluginMessage(String channel, ByteArrayOutputStream stream);

    void registerIncomingPluginChannel(String channel, String classname);

    void registerOutgoingPluginChannel(String channel);

    void unregisterIncomingPluginChannel(String channel, String classname);

    void unregisterOutgoingPluginChannel(String channel);
}
