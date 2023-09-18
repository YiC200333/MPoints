/*
 *  This file (SyncTabJoin.java) is a part of project MPoints
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
package me.yic.mpoints.data.syncdata.tab;

import me.yic.mpoints.AdapterManager;
import me.yic.mpoints.data.syncdata.SyncData;
import me.yic.mpoints.info.SyncType;

import java.util.List;

public class SyncTabJoin extends SyncData {

    private final String name;
    List<String> allname;

    public SyncTabJoin(String name){
        super(SyncType.TAB_JOIN, null);
        this.name = name;
        this.allname = null;
    }

    public String getName(){
        return name;
    }

    public void setallPlayers(List<String> allname){
        this.allname = allname;
    }

    @Override
    public void SyncStart() {
        if (allname == null) {
            if (!AdapterManager.Tab_PlayerList.contains(name)) {
                AdapterManager.Tab_PlayerList.add(name);
            }
        }else{
            AdapterManager.Tab_PlayerList.clear();
            AdapterManager.Tab_PlayerList.addAll(allname);
        }
    }
}
