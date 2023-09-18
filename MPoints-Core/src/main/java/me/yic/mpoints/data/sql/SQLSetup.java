/*
 *  This file (SQL.java) is a part of project MPoints
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
package me.yic.mpoints.data.sql;

import me.yic.mpoints.MPoints;
import me.yic.mpoints.MPointsLoad;

import java.io.File;

public class SQLSetup extends SQL{

    public static void setupMySqlTable() {
        if (MPointsLoad.DConfig.gettablesuffix() != null & !MPointsLoad.DConfig.gettablesuffix().equals("")) {
            tableName = "mpointsplayerinfo_" + MPointsLoad.DConfig.gettablesuffix().replace("%sign%", MPointsLoad.Config.SYNCDATA_SIGN);
            tablePoint = "mpointspt_" + MPointsLoad.DConfig.gettablesuffix().replace("%sign%", MPointsLoad.Config.SYNCDATA_SIGN);
            tableRecordName = "mpointsrecord_" + MPointsLoad.DConfig.gettablesuffix().replace("%sign%", MPointsLoad.Config.SYNCDATA_SIGN);
            tableLoginName = "mpointslogin_" + MPointsLoad.DConfig.gettablesuffix().replace("%sign%", MPointsLoad.Config.SYNCDATA_SIGN);
            tableUUIDName = "mpointsuuid_" + MPointsLoad.DConfig.gettablesuffix().replace("%sign%", MPointsLoad.Config.SYNCDATA_SIGN);
        }
    }

    public static void setupSqLiteAddress() {
        if (MPointsLoad.DConfig.gethost().equalsIgnoreCase("Default")) {
            return;
        }

        File folder = new File(MPointsLoad.DConfig.gethost());
        if (folder.exists()) {
            database.userdata = new File(folder, "data.db");
        } else {
            MPoints.getInstance().logger("自定义文件夹路径不存在", 1, null);
        }

    }
}