/*
 *  This file (BSListening.java) is a part of project MPoints
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

import me.yic.mpoints.depend.Bossshop;
import org.black_ixx.bossshop.events.BSRegisterTypesEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class BSListening implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onRegister(BSRegisterTypesEvent event) {
        new Bossshop().register();
    }

}
