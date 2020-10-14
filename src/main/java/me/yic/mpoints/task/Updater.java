package me.yic.mpoints.task;

import me.yic.mpoints.MPoints;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

public class Updater extends BukkitRunnable {

	public static boolean old = false;
	public static String newVersion = "none";

	@Override
	public void run() {
		try {
			URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=75669");
			URLConnection conn = url.openConnection();

			newVersion = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();

			List<String> versionList = Arrays.asList(newVersion.split("\\."));
			List<String> newVersionList = Arrays.asList(MPoints.getInstance().getDescription().getVersion().split("\\."));

			if (!compare(versionList, newVersionList)) {
				MPoints.getInstance().logger("已是最新版本");
				return;
			}

			if (MPoints.getInstance().lang().equalsIgnoreCase("Chinese")
					| MPoints.getInstance().lang().equalsIgnoreCase("ChineseTW")) {
				MPoints.getInstance().logger("发现新版本 " + newVersion);
				MPoints.getInstance().logger("https://www.mcbbs.net/thread-962904-1-1.html");
				return;
			}

			MPoints.getInstance().logger("Discover the new version " + newVersion);
			MPoints.getInstance().logger("https://www.spigotmc.org/resources/xconomy.75669/");

		} catch (Exception exception) {
			MPoints.getInstance().logger("检查更新失败");
		}
	}

	private static boolean compare(List<String> web, List<String> pl) {
		int v1 = 0;
		int v2 = 0;

		for (int i = 0; i < 5; i++) {
			if (web.size() >= i + 1) {
				v1 = Integer.parseInt(web.get(i));
			}

			if (pl.size() >= i + 1) {
				v2 = Integer.parseInt(pl.get(i));
			}

			if (v1 != (v2)) {
				break;
			}
		}

		int result = Integer.compare(v1 - v2, 0);
		if (result > 0) {
			old = true;
			return true;
		} else {
			return false;
		}

	}

}
