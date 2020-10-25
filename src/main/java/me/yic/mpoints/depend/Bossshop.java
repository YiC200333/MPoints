package me.yic.mpoints.depend;

import me.yic.mpoints.MPointsAPI;
import me.yic.mpoints.data.DataFormat;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.prices.BSPriceTypeNumber;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class Bossshop extends BSPriceTypeNumber{
    public Object createObject(Object o, boolean force_final_state) {
        return InputReader.readString(o, true);
    }

    public boolean validityCheck(String item_name, Object o) {
        if ((String) o != null) {
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
        String sign = mess.substring(mess.indexOf("#") + 1,mess.indexOf("##"));
        String amountx = mess.substring(mess.indexOf("##") + 2,mess.length()-1);
        Double balance = MPointsAPI.getbalance(sign,p.getUniqueId()).doubleValue();
        Double amount = (double) ClassManager.manager.getMultiplierHandler().calculatePriceWithMultiplier(p, buy, clickType, DataFormat.formatString(sign,amountx).doubleValue()) * multiplier;
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
        String sign = mess.substring(mess.indexOf("#") + 1,mess.indexOf("##"));
        String amountx = mess.substring(mess.indexOf("##") + 2,mess.length()-1);
        Double amount = (double) ClassManager.manager.getMultiplierHandler().calculatePriceWithMultiplier(p, buy, clickType, DataFormat.formatString(sign,amountx).doubleValue()) * multiplier;
        MPointsAPI.changebalance(sign,p.getUniqueId(),p.getName(),DataFormat.formatDouble(sign,amount),false);
        return getDisplayBalance(p, buy, price, clickType);
    }

    @Override
    public String getDisplayBalance(Player p, BSBuy buy, Object price, ClickType clickType) {
        String mess = (String) price;
        String sign = mess.substring(mess.indexOf("#") + 1,mess.indexOf("##"));
        return MPointsAPI.getdisplay(sign,MPointsAPI.getbalance(sign,p.getUniqueId()));
    }

    @Override
    public String getDisplayPrice(Player p, BSBuy buy, Object price, ClickType clickType) {
        String mess = (String) price;
        String sign = mess.substring(mess.indexOf("#") + 1,mess.indexOf("##"));
        String amountx = mess.substring(mess.indexOf("##") + 2,mess.length()-1);
        return MPointsAPI.getdisplay(sign,DataFormat.formatString(sign,amountx));
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
