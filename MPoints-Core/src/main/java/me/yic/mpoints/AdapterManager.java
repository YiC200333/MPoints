/*
 *  This file (AdapterManager.java) is a part of project MPoints
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

import me.yic.mpoints.adapter.comp.CChat;
import me.yic.mpoints.adapter.comp.CPlugin;
import me.yic.mpoints.info.MessageConfig;
import me.yic.mpoints.info.SyncChannalType;
import me.yic.mpoints.lang.MessagesManager;

import java.util.ArrayList;
import java.util.List;

public class AdapterManager {

    public static List<String> Tab_PlayerList = new ArrayList<>();
    public static boolean foundvaultpe = false;
    public static boolean foundvaultOfflinePermManager = false;

    public final static CPlugin PLUGIN = new CPlugin();

    //public static ScheduledExecutorService ScheduledThreadPool;
    //public static ExecutorService FixedThreadPool;

    public static String translateColorCodes(MessageConfig message) {
        return CChat.translateAlternateColorCodes('&', MessagesManager.messageFile.getString(message.toString()));
    }

    public static String translateColorCodes(String message) {
        return CChat.translateAlternateColorCodes('&', MessagesManager.messageFile.getString(message));
    }


    public static boolean BanModiftyBalance() {
        if (!MPointsLoad.getSyncData_Enable()){
            return false;
        }
        if (!PLUGIN.getOnlinePlayersisEmpty()){
            return false;
        }
        if (MPointsLoad.Config.DISABLE_CACHE){
            return false;
        }
        return !MPointsLoad.Config.SYNCDATA_TYPE.equals(SyncChannalType.REDIS);
    }
    public static boolean checkisMainThread(){
        return Thread.currentThread().getName().equalsIgnoreCase("Server thread") ||
                Thread.currentThread().getName().contains("Region Scheduler Thread #");
    }

    public static void runTaskAsynchronously(Runnable runnable){
        PLUGIN.runTaskAsynchronously(runnable);
    }

    public static void runTaskLaterAsynchronously(Runnable runnable, long seconds){
        PLUGIN.runTaskLaterAsynchronously(runnable, seconds * 20L);
    }

/*    public static ScheduledFuture<?> runTaskTimerAsynchronously(Runnable runnable, long seconds){
        return ScheduledThreadPool.scheduleAtFixedRate(runnable, seconds, seconds, TimeUnit.SECONDS);
    }*/
}
