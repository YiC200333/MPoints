package me.yic.mpoints.message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Languages {

	public static HashMap<String, String> mess = new HashMap<>();

	public static void compare(String lang, File f) {
		if (lang.equalsIgnoreCase("English")) {
			english();
		} else if (lang.equalsIgnoreCase("ChineseTW")) {
			chinesetw();
		} else {
			chinese();
		}

		List<String> messages = index();
		for (String message : messages) {
			boolean renew = false;
			if (!MessagesManager.messageFile.contains(message)) {
				renew = true;
				MessagesManager.messageFile.createSection(message);
				MessagesManager.messageFile.set(message, mess.get(message));
			}

			try {
				if (renew) {
					MessagesManager.messageFile.save(f);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

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
		//ll.add("help10");
		//ll.add("help11");
		ll.add("help12");
		return ll;

	}

	private static void english() {
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
		mess.put("pay", "&cYou pay %player% %amount% %pointname%");
		mess.put("pay_receive", "&aYou receive %amount% %pointname% from %player%");
		mess.put("pay_fail", "&cYour %pointname% is less than %amount%");
		mess.put("pay_self", "&cYou can't pay yourself");
		mess.put("noaccount", "&cTarget account does not exist");
		mess.put("invalid", "&cInvalid amount");
		mess.put("over_maxnumber", "&cThe target account amount over the maximum amount");
		mess.put("money_give", "&cYou give %player% %amount% %pointname%");
		mess.put("money_give_receive", "&aAdministrator give you %amount% %pointname%");
		mess.put("money_take", "&cYou take %amount% %pointname% from %player%");
		mess.put("money_take_fail", "&c%player%'s %pointname% is less than %amount%");
		mess.put("money_take_receive", "&cAdministrator take %amount% %pointname% from your account");
		mess.put("money_set", "&cYou set %player%'s %pointname% to %amount%");
		mess.put("money_set_receive", "&cAdministrator set your %pointname% to %amount%");
		mess.put("no_permission", "&cYou don't have permission to use this command");
		mess.put("help_title_full", "&6=============== [MPoints] HELP ===============");
		mess.put("help1", "&6mpoints balance <pointname>  -  Displays your balance of point");
		mess.put("help2", "&6mpoints balance <pointname> <player>  -  Displays <player>'s balance of point");
		mess.put("help3", "&6mpoints pay <pointname> <player> <amount>  -  Pay <player> <amount> point");
		mess.put("help4", "&6mpoints balancetop <pointname>  -  Displays TOP10");
		mess.put("help5", "&6mpoints version  -  Display plugin version information");
		mess.put("help6", "&6mpoints list  -  List all points");
		mess.put("help7", "&6mpoints give <pointname> <player> <amount>  -  give <player> <amount> point");
		mess.put("help8", "&6mpoints take <pointname> <player> <amount>  -  take <amount> point from <player>");
		mess.put("help9", "&6mpoints set <pointname> <player> <amount>  -  set <player>'s point to <amount>");
		mess.put("help10", "&6mpoints give <pointname> * <all/online> <amount> <reason>  -  give <all/online player> <amount> point");
		mess.put("help11", "&6mpoints take <pointname> * <all/online> <amount> <reason>  -  take <amount> point from <all/online player>");
		mess.put("help12", "&6mpoints reload -  Reload message.yml");
	}

	private static void chinese() {
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
		mess.put("pay", "&c你转账给%player% %amount% %pointname%");
		mess.put("pay_receive", "&a你从 %player% 收到 %amount% %pointname%");
		mess.put("pay_fail", "&c你的 %pointname% 不足  %amount%");
		mess.put("pay_self", "&c你不能向自己转账");
		mess.put("noaccount", "&c目标帐号不存在");
		mess.put("invalid", "&c输入的金额无效");
		mess.put("over_maxnumber", "&c目标帐号金额超出最大值");
		mess.put("money_give", "&c你给予了 %player% %amount% %pointname%");
		mess.put("money_give_receive", "&a管理员给予你 %amount% %pointname%");
		mess.put("money_take", "&c你从  %player% 收取了 %amount% %pointname%");
		mess.put("money_take_fail", "&c%player% 的 %pointname% 不足  %amount%");
		mess.put("money_take_receive", "&c管理员扣除了  %amount% %pointname%");
		mess.put("money_set", "&c你将  %player% 的 %pointname% 设置为 %amount%");
		mess.put("money_set_receive", "&c管理员设置你的 %pointname% 为  %amount%");
		mess.put("no_permission", "&c你没有权限使用这个指令");
		mess.put("help_title_full", "&6=============== [MPoints] 帮助  ===============");
		mess.put("help1", "&6mpoints balance <pointname>  -  查询点数余额");
		mess.put("help2", "&6mpoints balance <pointname> <玩家>  -  查询<玩家>点数余额");
		mess.put("help3", "&6mpoints pay <pointname> <玩家> <金额>  -  转账给<玩家><金额>点数");
		mess.put("help4", "&6mpoints balancetop <pointname>  -  查询点数排行榜");
		mess.put("help5", "&6mpoints version  -  显示插件版本信息");
		mess.put("help6", "&6mpoints list  -  列出所有的点数");
		mess.put("help7", "&6mpoints give <pointname> <玩家> <金额>  -  给与<玩家><金额>点数");
		mess.put("help8", "&6mpoints take <pointname> <玩家> <金额>  -  从<玩家>取走<金额>点数");
		mess.put("help9", "&6mpoints set <pointname> <玩家> <金额>  -  设置<玩家>点数为<金额>");
		mess.put("help10", "&6mpoints give <pointname> * <all/online> <amount> <reason>  -  给与<所有/在线玩家><金额>点数");
		mess.put("help11", "&6mpoints take <pointname> * <all/online> <amount> <reason>  -  从<所有/在线玩家>取走<金额>点数");
		mess.put("help12", "&6mpoints reload 重新加载 message.yml");
	}

	private static void chinesetw() {
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
		mess.put("pay", "&c你轉帳給 %player% %amount% %pointname%");
		mess.put("pay_receive", "&a你從 %player% 收到轉帳 %amount% %pointname%");
		mess.put("pay_fail", "&c你的 %pointname% 不足 %amount%");
		mess.put("pay_self", "&c你不能向自己轉帳");
		mess.put("noaccount", "&c目標帳號不存在");
		mess.put("invalid", "&c輸入的金額無效");
		mess.put("over_maxnumber", "&c目標賬號金額超出最大值");
		mess.put("money_give", "&c你給予了 %player% %amount% %pointname%");
		mess.put("money_give_receive", "&a管理員給予你 %amount% %pointname%");
		mess.put("money_take", "&c你從 %player% 收取了 %amount% %pointname%");
		mess.put("money_take_fail", "&c%player% 的 %pointname% 不足 %amount%");
		mess.put("money_take_receive", "&c管理員扣除了 %amount% %pointname%");
		mess.put("money_set", "&c你將 %player% 的 %pointname% 設定為 %amount%");
		mess.put("money_set_receive", "&c管理員設定你的 %pointname% 為 %amount%");
		mess.put("no_permission", "&c你沒有許可權使用這個指令");
		mess.put("help_title_full", "&6=============== [MPoints]幫助 ===============");
		mess.put("help1", "&6mpoints balance <pointname>  -  查詢點數餘額");
		mess.put("help2", "&6mpoints balance <玩家> <pointname>  -  査詢<玩家>點數餘額");
		mess.put("help3", "&6mpoints pay <pointname> <玩家> <金額>  -  轉帳給<玩家><金額>點數");
		mess.put("help4", "&6mpoints balancetop <pointname>  -  查詢點數排行榜");
		mess.put("help5", "&6mpoints version  -  顯示插件版本信息");
		mess.put("help6", "&6mpoints list  -  列出所有的點數");
		mess.put("help7", "&6mpoints give <pointname> <玩家> <金額>  -  給與<玩家><金額>點數");
		mess.put("help8", "&6mpoints take <pointname> <玩家> <金額>  -  從<玩家>取走<金額>點數");
		mess.put("help9", "&6mpoints set <pointname> <玩家> <金額>  -  設定<玩家>點數為<金額>");
		mess.put("help10", "&6mpoints give <pointname> * <all/online> <amount> <reason>  -  給與<所有/在綫玩家><金额>點數");
		mess.put("help11", "&6mpoints take <pointname> * <all/online> <amount> <reason>  -  從<所有/在綫玩家>取走<金額>點數");
		mess.put("help12", "&6mpoints reload 重新加載 message.yml");
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
			translateFile("#============================== Translator - none ==============================", file);
		} else if (lang.equalsIgnoreCase("Russian")) {
			translateFile("#============================== Translator - none ==============================", file);
		} else if (lang.equalsIgnoreCase("Turkish")) {
			translateFile("#============================== Translator - none ==============================", file);
		} else if (lang.equalsIgnoreCase("Japanese")) {
			translateFile("#============================== Translator - none ==============================", file);
		}
	}
}
