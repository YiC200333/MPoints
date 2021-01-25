/*
 *  This file (UpdateConfig.java) is a part of project MPoints
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
package me.yic.mpoints.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class UpdateConfig {

    public static boolean update(FileConfiguration config, File cc) {
        boolean update = false;
        //FileConfiguration ck = YamlConfiguration.loadConfiguration(cc);
        //if (!ck.contains("MySQL.table_suffix")) {
        //	config.createSection("MySQL.table_suffix");
        //	config.set("MySQL.table_suffix", "");
        //	update = true;
        //}
        return update;
    }


    public static boolean updatepoint(String u, FileConfiguration config, File cc) {
        boolean update = false;
        FileConfiguration ck = YamlConfiguration.loadConfiguration(cc);
        if (!ck.contains(u + Points.settingstring + ".hide-comannd-message")) {
            config.createSection(u + Points.settingstring + ".hide-comannd-message");
            config.set(u + Points.settingstring + ".hide-comannd-message", false);
            update = true;
        }
        return update;
    }
}
