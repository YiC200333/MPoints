package me.yic.mpoints.depend;/*
 *  This file (LoadEconomy.java) is a part of project MPoints
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

import me.yic.mpoints.MPoints;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    public static Permission vaultPerm = null;

    @SuppressWarnings("ConstantConditions")
    public static void load() {
        RegisteredServiceProvider<Permission> rsp = MPoints.getInstance().getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp != null) {
            vaultPerm = rsp.getProvider();
        }
    }


}
