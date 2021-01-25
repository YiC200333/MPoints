/*
 *  This file (Languages.java) is a part of project MPoints
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
package me.yic.mpoints.lang;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Languages {


    public static List<String> index() {
        List<String> ll = new ArrayList<>();
        ll.add("prefix");
        ll.add("points_list");
        ll.add("balance");
        ll.add("balance_other");
        ll.add("points_nosign");
        ll.add("top_title");
        ll.add("sum_text");
        ll.add("top_text");
        ll.add("top_subtitle");
        ll.add("top_nodata");
        ll.add("top_hidden");
        ll.add("top_displayed");
        ll.add("pay");
        ll.add("pay_receive");
        ll.add("pay_fail");
        ll.add("pay_self");
        ll.add("noaccount");
        ll.add("invalid");
        ll.add("over_maxnumber");
        ll.add("money_give");
        ll.add("money_give_receive");
        ll.add("money_take");
        ll.add("money_take_fail");
        ll.add("money_take_receive");
        ll.add("money_set");
        ll.add("money_set_receive");
        ll.add("no_permission");
        ll.add("help_title_full");
        ll.add("help1");
        ll.add("help2");
        ll.add("help3");
        ll.add("help4");
        ll.add("help5");
        ll.add("help6");
        ll.add("help7");
        ll.add("help8");
        ll.add("help9");
        ll.add("help10");
        ll.add("help11");
        ll.add("help12");
        ll.add("help13");
        return ll;

    }

    static void english(HashMap<String, String> mess) {
        mess.put("prefix", "&6[MPoints]");
        mess.put("balance", "&a%pointname% Balance: %balance%");
        mess.put("balance_other", "&a%player%'s %pointname% balance: %balance%");
        mess.put("points_list", "&a%sign% -- %pointname%");
        mess.put("points_nosign", "&cThis sign doesn't exist");
        mess.put("top_title", "&e========= %pointname% TOP10 =========");
        mess.put("sum_text", "&fServer Total - %balance%");
        mess.put("top_text", "&e%index%: %player% - %balance%");
        mess.put("top_subtitle", "&7TOP10 refreshed every 5 minutes");
        mess.put("top_nodata", "&cNo TOP10 data");
        mess.put("top_hidden", "&a%player% is hidden from %pointname% data");
        mess.put("top_displayed", "&a%player% is displayed from %pointname% data");
        mess.put("pay", "&cYou pay %player% %amount% %pointname%");
        mess.put("pay_receive", "&aYou receive %amount% %pointname% from %player%");
        mess.put("pay_fail", "&cYour %pointname% is less than %amount%");
        mess.put("pay_self", "&cYou can't pay yourself");
        mess.put("noaccount", "&cTarget account does not exist");
        mess.put("invalid", "&cInvalid amount");
        mess.put("over_maxnumber", "&cThe target account amount over the maximum amount");
        mess.put("money_give", "&cYou give %player% %amount%");
        mess.put("money_give_receive", "&aAdministrator give you %amount%");
        mess.put("money_take", "&cYou take %amount% from %player%");
        mess.put("money_take_fail", "&c%player%'s %pointname% is less than %amount%");
        mess.put("money_take_receive", "&cAdministrator take %amount% from your account");
        mess.put("money_set", "&cYou set %player%'s %pointname% to %amount%");
        mess.put("money_set_receive", "&cAdministrator set your %pointname% to %amount%");
        mess.put("no_permission", "&cYou don't have permission to use this command");
        mess.put("help_title_full", "&6=============== [MPoints] HELP <Page %page%> ===============");
        mess.put("help1", "&6mpoints <pointname> balance  -  Displays your balance of point");
        mess.put("help2", "&6mpoints <pointname> balance <player>  -  Displays <player>'s balance of point");
        mess.put("help3", "&6mpoints <pointname> pay <player> <amount>  -  Pay <player> <amount> point");
        mess.put("help4", "&6mpoints <pointname> balancetop  -  Displays TOP10");
        mess.put("help5", "&6mpoints version  -  Display plugin version information");
        mess.put("help6", "&6mpoints list  -  List all points");
        mess.put("help7", "&6mpoints <pointname> give <player> <amount>  -  give <player> <amount> point");
        mess.put("help8", "&6mpoints <pointname> take <player> <amount>  -  take <amount> point from <player>");
        mess.put("help9", "&6mpoints <pointname> set <player> <amount>  -  set <player>'s point to <amount>");
        mess.put("help10", "&6mpoints <pointname> give * <all/online> <amount> <reason>  -  give <all/online player> <amount> point");
        mess.put("help11", "&6mpoints <pointname> take * <all/online> <amount> <reason>  -  take <amount> point from <all/online player>");
        mess.put("help12", "&6mpoints <pointname> balancetop hide/display <player>  -  Hide or display a <player>'s data from TOP10");
        mess.put("help13", "&6mpoints reload -  Reload message.yml");
    }

    static void spanish(HashMap<String, String> mess) {
        mess.put("prefix", "&6[MPoints]");
        mess.put("balance", "&aTu balance en %pointname% es de: %balance%");
        mess.put("balance_other", "&aEl balance de %player% en %pointname% es de: %balance%");
        mess.put("points_list", "&a%sign% -- %pointname%");
        mess.put("points_nosign", "&cThis sign doesn''t exist");
        mess.put("top_title", "&e========= %pointname% TOP10 =========");
        mess.put("sum_text", "&fTotal de dinero en el server - %balance%");
        mess.put("top_text", "&e%index%: %player% - %balance%");
        mess.put("top_subtitle", "&7El TOP10 se actualiza cada 5 minutos");
        mess.put("top_nodata", "&cNo hay datos aun para un TOP10");
        mess.put("top_hidden", "&aEl jugador %player% esconde su cantidad de %pointname%");
        mess.put("top_displayed", "&aEl jugador %player% ahora muestra su cantidad de %pointname%");
        mess.put("pay", "&cTransferiste a %player% %amount% %pointname%");
        mess.put("pay_receive", "&aRecibiste %amount% %pointname% de %player%");
        mess.put("pay_fail", "&cTus %pointname% no alcanzan para %amount%");
        mess.put("pay_self", "&cNo puedes transferir esta moneda a ti mismo");
        mess.put("noaccount", "&cLa cuenta escrita no existe");
        mess.put("invalid", "&cCantidad Invalida");
        mess.put("over_maxnumber", "&cLa cantidad de %pointname% de la cuenta escrita sobrepasa la cantidad limite");
        mess.put("money_give", "&cLe diste a %player% %amount%");
        mess.put("money_give_receive", "&aEl Administrador te dio %amount%");
        mess.put("money_take", "&cTu tomaste %amount% %pointname% de %player%");
        mess.put("money_take_fail", "&cLa cantiidad de %pointname% de %player% es menor que %amount%");
        mess.put("money_take_receive", "&cEl Administrador tomo %amount% %pointname% de tu cuenta");
        mess.put("money_set", "&cEstableciste la cantidad de %pointname% de %player% en  %amount%");
        mess.put("money_set_receive", "&cEl Administrador establecio tu cantidad de %pointname% en %amount%");
        mess.put("no_permission", "&cNo tienes permiso para usar este comando");
        mess.put("help_title_full", "&6=============== [MPoints] HELP <Page %page%> ===============");
        mess.put("help1", "&6mpoints <moneda> balance  -  Muestra tu balance en esta moneda");
        mess.put("help2", "&6mpoints <moneda> balance <jugador>  -  Muestra el balance del <jugador> enesta moneda");
        mess.put("help3", "&6mpoints <moneda> pay <jugador> <cantidad>  -  Transfiere al jugador cierta cantidad de esta moneda");
        mess.put("help4", "&6mpoints <moneda> balancetop  -  Muestra el TOP10");
        mess.put("help5", "&6mpoints version  -  Muestra a informacion del Plugin");
        mess.put("help6", "&6mpoints list  -  Enlista todas las moneda");
        mess.put("help7", "&6mpoints <moneda> give <jugador> <cantidad>  -  Da al jugador cierta cantidad de esta moneda");
        mess.put("help8", "&6mpoints <moneda> take <jugador> <cantidad>  -  Toma del jugador cierta cantidad de esta moneda");
        mess.put("help9", "&6mpoints <moneda> set <jugador> <cantidad>  -  Etablece al jugador cierta cantidad de esta moneda");
        mess.put("help10", "&6mpoints <moneda> give * <all/online> <cantidad> <razon>  -  Da a todos/jugadores online cierta cantidad de esta moneda");
        mess.put("help11", "&6mpoints <moneda> take * <all/online> <cantidad> <razon>  -  Toma cierta cantidad de todos/jugadores online");
        mess.put("help12", "&6mpoints <moneda> balancetop hide/display <jugador>  -  Muestra o esconde los datos del <jugador> del TOP10");
        mess.put("help13", "&6mpoints reload -  Recarga el archivo message.yml");
    }

    static void chinese(HashMap<String, String> mess) {
        mess.put("prefix", "&6[MPoints]");
        mess.put("balance", "&a%pointname% 余额: %balance%");
        mess.put("balance_other", "&a%player% 的 %pointname% 余额: %balance%");
        mess.put("points_list", "&a%sign% -- %pointname%");
        mess.put("points_nosign", "&c这个标志不存在");
        mess.put("top_title", "&e========= %pointname% TOP10 =========");
        mess.put("sum_text", "&f服务器总金额 - %balance%");
        mess.put("top_text", "&e%index%: %player% - %balance%");
        mess.put("top_subtitle", "&7TOP10每5分钟刷新一次");
        mess.put("top_nodata", "&c无玩家经济数据");
        mess.put("top_hidden", "&a%player% 已从 %pointname% 数据中隐藏");
        mess.put("top_displayed", "&a%player% 已从 %pointname% 数据中显示");
        mess.put("pay", "&c你转账给%player% %amount% %pointname%");
        mess.put("pay_receive", "&a你从 %player% 收到 %amount% %pointname%");
        mess.put("pay_fail", "&c你的 %pointname% 不足  %amount%");
        mess.put("pay_self", "&c你不能向自己转账");
        mess.put("noaccount", "&c目标帐号不存在");
        mess.put("invalid", "&c输入的金额无效");
        mess.put("over_maxnumber", "&c目标帐号金额超出最大值");
        mess.put("money_give", "&c你给予了 %player% %amount%");
        mess.put("money_give_receive", "&a管理员给予你 %amount%");
        mess.put("money_take", "&c你从  %player% 收取了 %amount%");
        mess.put("money_take_fail", "&c%player% 的 %pointname% 不足  %amount%");
        mess.put("money_take_receive", "&c管理员扣除了  %amount%");
        mess.put("money_set", "&c你将  %player% 的 %pointname% 设置为 %amount%");
        mess.put("money_set_receive", "&c管理员设置你的 %pointname% 为  %amount%");
        mess.put("no_permission", "&c你没有权限使用这个指令");
        mess.put("help_title_full", "&6=============== [MPoints] 帮助 <第 %page% 页> ===============");
        mess.put("help1", "&6mpoints <pointname> balance  -  查询点数余额");
        mess.put("help2", "&6mpoints <pointname> balance <玩家>  -  查询<玩家>点数余额");
        mess.put("help3", "&6mpoints <pointname> pay <玩家> <金额>  -  转账给<玩家><金额>点数");
        mess.put("help4", "&6mpoints <pointname> balancetop  -  查询点数排行榜");
        mess.put("help5", "&6mpoints version  -  显示插件版本信息");
        mess.put("help6", "&6mpoints list  -  列出所有的点数");
        mess.put("help7", "&6mpoints <pointname> give <玩家> <金额>  -  给与<玩家><金额>点数");
        mess.put("help8", "&6mpoints <pointname> take <玩家> <金额>  -  从<玩家>取走<金额>点数");
        mess.put("help9", "&6mpoints <pointname> set <玩家> <金额>  -  设置<玩家>点数为<金额>");
        mess.put("help10", "&6mpoints <pointname> give * <all/online> <金额> <理由>  -  给与<所有/在线玩家><金额>点数");
        mess.put("help11", "&6mpoints <pointname> take * <all/online> <金额> <理由>  -  从<所有/在线玩家>取走<金额>点数");
        mess.put("help12", "&6mpoints <pointname> balancetop hide/display <玩家>  -  将<玩家>的数据从Top10上隐藏或显示");
        mess.put("help13", "&6mpoints reload 重新加载 message.yml");
    }

    static void chinesetw(HashMap<String, String> mess) {
        mess.put("prefix", "&6[MPoints]");
        mess.put("balance", "&a%pointname% 餘額： %balance%");
        mess.put("balance_other", "&a%player% 的 %pointname%： %balance%");
        mess.put("points_list", "&a%sign% -- %pointname%");
        mess.put("points_nosign", "&c這個標志不存在");
        mess.put("top_title", "&e========= %pointname% TOP10 =========");
        mess.put("sum_text", "&f伺服器總金額 - %balance%");
        mess.put("top_text", "&e%index%： %player% - %balance%");
        mess.put("top_subtitle", "&7TOP10每五分鐘刷新一次");
        mess.put("top_nodata", "&c無玩家經濟資料");
        mess.put("top_hidden", "&a%player% 已從 %pointname% 數據中隱藏");
        mess.put("top_displayed", "&a%player% 已從 %pointname% 數據中顯示");
        mess.put("pay", "&c你轉帳給 %player% %amount% %pointname%");
        mess.put("pay_receive", "&a你從 %player% 收到轉帳 %amount% %pointname%");
        mess.put("pay_fail", "&c你的 %pointname% 不足 %amount%");
        mess.put("pay_self", "&c你不能向自己轉帳");
        mess.put("noaccount", "&c目標帳號不存在");
        mess.put("invalid", "&c輸入的金額無效");
        mess.put("over_maxnumber", "&c目標賬號金額超出最大值");
        mess.put("money_give", "&c你給予了 %player% %amount%");
        mess.put("money_give_receive", "&a管理員給予你 %amount%");
        mess.put("money_take", "&c你從 %player% 收取了 %amount%");
        mess.put("money_take_fail", "&c%player% 的 %pointname% 不足 %amount%");
        mess.put("money_take_receive", "&c管理員扣除了 %amount%");
        mess.put("money_set", "&c你將 %player% 的 %pointname% 設定為 %amount%");
        mess.put("money_set_receive", "&c管理員設定你的 %pointname% 為 %amount%");
        mess.put("no_permission", "&c你沒有許可權使用這個指令");
        mess.put("help_title_full", "&6=============== [MPoints]幫助 <第 %page% 頁> ===============");
        mess.put("help1", "&6mpoints <pointname> balance  -  查詢點數餘額");
        mess.put("help2", "&6mpoints <pointname> balance <玩家>  -  査詢<玩家>點數餘額");
        mess.put("help3", "&6mpoints <pointname> pay <玩家> <金額>  -  轉帳給<玩家><金額>點數");
        mess.put("help4", "&6mpoints <pointname> balancetop  -  查詢點數排行榜");
        mess.put("help5", "&6mpoints version  -  顯示插件版本信息");
        mess.put("help6", "&6mpoints list  -  列出所有的點數");
        mess.put("help7", "&6mpoints <pointname> give <玩家> <金額>  -  給與<玩家><金額>點數");
        mess.put("help8", "&6mpoints <pointname> take <玩家> <金額>  -  從<玩家>取走<金額>點數");
        mess.put("help9", "&6mpoints <pointname> set <玩家> <金額>  -  設定<玩家>點數為<金額>");
        mess.put("help10", "&6mpoints <pointname> give * <all/online> <金額> <理由>  -  給與<所有/在綫玩家><金额>點數");
        mess.put("help11", "&6mpoints <pointname> take * <all/online> <金額> <理由>  -  從<所有/在綫玩家>取走<金額>點數");
        mess.put("help12", "&6mpoints <pointname> balancetop hide/display <玩家>  -  將<玩家>的數據從Top10上隱藏或顯示");
        mess.put("help13", "&6mpoints reload 重新加載 message.yml");
    }


    public static void translateFile(String string, File file) {
        try {
            FileOutputStream f = new FileOutputStream(file, true);
            f.write(string.getBytes());
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void translatorName(String lang, File file) {
        if (lang.equalsIgnoreCase("French")) {
            translateFile("#============================== Translator - none ==============================", file);
        } else if (lang.equalsIgnoreCase("Spanish")) {
            translateFile("#============================== Translator - SYukishiro ==============================", file);
        } else if (lang.equalsIgnoreCase("Russian")) {
            translateFile("#============================== Translator - none ==============================", file);
        } else if (lang.equalsIgnoreCase("Turkish")) {
            translateFile("#============================== Translator - none ==============================", file);
        } else if (lang.equalsIgnoreCase("Japanese")) {
            translateFile("#============================== Translator - none ==============================", file);
        }
    }
}
