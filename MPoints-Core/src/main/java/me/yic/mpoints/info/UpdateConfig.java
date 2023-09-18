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
package me.yic.mpoints.info;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.adapter.comp.CConfig;

import java.io.IOException;

public class UpdateConfig {

    public static void update(CConfig config) {
        boolean update = false;
        if (!config.contains("SyncData")) {
            MPoints.getInstance().logger(null, 1, "==================================================");
            MPoints.getInstance().logger(null, 1, "The configuration file is an older version");
            MPoints.getInstance().logger(null, 1, "The plugin may occur configuration problems");
            MPoints.getInstance().logger(null, 1, "It is recommended to regenerate configuration file");
            MPoints.getInstance().logger(null, 1, "==================================================");
        }
/*        if (!config.contains("Settings.core-poolsize")) {
            config.createSection("Settings.core-poolsize");
            config.set("Settings.core-poolsize", 4);
            update = true;
        }*/
        if (update){
            try {
                config.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
