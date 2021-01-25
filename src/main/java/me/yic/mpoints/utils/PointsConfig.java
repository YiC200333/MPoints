/*
 *  This file (PointsConfig.java) is a part of project MPoints
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

import me.yic.mpoints.MPoints;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PointsConfig extends Points {

    public boolean load() {
        File file = new File(MPoints.getInstance().getDataFolder(), "points.yml");
        if (!file.exists()) {
            MPoints.getInstance().saveResource("points.yml", false);
        }
        PointsFile = YamlConfiguration.loadConfiguration(file);
        return LoadPoints(file);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean LoadPoints(File path) {
        pointsigns.clear();
        boolean update = false;
        ConfigurationSection section = PointsFile.getConfigurationSection("");
        for (String u : section.getKeys(false)) {
            if (section.contains(u + ".setting")) {
                settingstring = ".setting";
            }
            if (update) {
                UpdateConfig.updatepoint(u, PointsFile, path);
            } else {
                update = UpdateConfig.updatepoint(u, PointsFile, path);
            }
            String sign = section.getString(u + settingstring + ".sign");
            boolean enablebc = section.getBoolean(u + settingstring + ".enable-bungeecord");
            if (enablebc) {
                MPoints.hasbcpoint = true;
            }
            if (section.getBoolean(u + ".quick-command.enable")) {
                Command qcommand = new QuickCommand(section.getString(u + ".quick-command.command"), sign);
                MPoints.commandMap.register(section.getString(u + ".quick-command.command"), qcommand);
            }
            if (pointsigns.containsKey(sign)) {
                MPoints.getInstance().logger(null, "Exist the same sign of point");
                return false;
            }
            pointsigns.put(sign, u);
        }
        if (update) {
            try {
                PointsFile.save(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
