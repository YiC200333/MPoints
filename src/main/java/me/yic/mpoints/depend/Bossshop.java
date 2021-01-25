/*
 *  This file (Bossshop.java) is a part of project MPoints
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
package me.yic.mpoints.depend;

import me.yic.mpoints.data.DataFormat;
import me.yic.mpoints.data.caches.Cache;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.prices.BSPriceTypeNumber;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class Bossshop extends BSPriceTypeNumber {
    public Object createObject(Object o, boolean force_final_state) {
        return InputReader.readString(o, true);
    }

    public boolean validityCheck(String item_name, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder().severe("MPoints error");
        return false;
    }

    @Override
    public void enableType() {
    }


    @Override
    public boolean hasPrice(Player p, BSBuy buy, Object price, ClickType clickType, int multiplier, boolean messageOnFailure) {
        String mess = (String) price;
        String sign = mess.substring(mess.indexOf("#") + 1, mess.indexOf("##"));
        String amountx = mess.substring(mess.indexOf("##") + 2, mess.length() - 1);
        double balance = Cache.getBalanceFromCacheOrDB(p.getUniqueId(), sign).doubleValue();
        double amount = ClassManager.manager.getMultiplierHandler().calculatePriceWithMultiplier(p, buy, clickType, DataFormat.formatString(sign, amountx).doubleValue()) * multiplier;
        if (balance < amount) {
            if (messageOnFailure) {
                ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Points", p);
            }
            return false;
        }
        return true;
    }

    @Override
    public String takePrice(Player p, BSBuy buy, Object price, ClickType clickType, int multiplier) {
        String mess = (String) price;
        String sign = mess.substring(mess.indexOf("#") + 1, mess.indexOf("##"));
        String amountx = mess.substring(mess.indexOf("##") + 2, mess.length() - 1);
        Double amount = ClassManager.manager.getMultiplierHandler().calculatePriceWithMultiplier(p, buy, clickType, DataFormat.formatString(sign, amountx).doubleValue()) * multiplier;
        Cache.change(p.getUniqueId(), p.getName(), sign, DataFormat.formatDouble(sign, amount), false, "BOSSSHOP", "N/A");
        return getDisplayBalance(p, buy, price, clickType);
    }

    @Override
    public String getDisplayBalance(Player p, BSBuy buy, Object price, ClickType clickType) {
        String mess = (String) price;
        String sign = mess.substring(mess.indexOf("#") + 1, mess.indexOf("##"));
        return DataFormat.shown(sign, Cache.getBalanceFromCacheOrDB(p.getUniqueId(), sign));
    }

    @Override
    public String getDisplayPrice(Player p, BSBuy buy, Object price, ClickType clickType) {
        String mess = (String) price;
        String sign = mess.substring(mess.indexOf("#") + 1, mess.indexOf("##"));
        String amountx = mess.substring(mess.indexOf("##") + 2, mess.length() - 1);
        return DataFormat.shown(sign, DataFormat.formatString(sign, amountx));
    }


    @Override
    public String[] createNames() {
        return new String[]{"mpoints"};
    }


    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

    @Override
    public boolean isIntegerValue() {
        return true;
    }
}
