package me.yic.mpoints.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.yic.mpoints.MPointsBungee;
import me.yic.mpoints.task.SendMessTaskB;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BCsync implements Listener {

	@SuppressWarnings("UnstableApiUsage")
	@EventHandler
	public void on(PluginMessageEvent event) {
		if (!(event.getSender() instanceof Server)) {
			return;
		}

		if (!event.getTag().equalsIgnoreCase("mpoints:acb")) {
			return;
		}

		ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
		Server senderServer = (Server) event.getSender();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		String type = input.readUTF();
		try {
			if (type.equalsIgnoreCase("balance")) {
				output.writeUTF("balance");
				output.writeUTF(input.readUTF());
				output.writeUTF(input.readUTF());
				output.writeUTF(input.readUTF());
				output.writeUTF(input.readUTF());
			} else if (type.equalsIgnoreCase("message")) {
				output.writeUTF("message");
				output.writeUTF(input.readUTF());
				output.writeUTF(input.readUTF());
				String name = input.readUTF();
				ProxiedPlayer p = ProxyServer.getInstance().getPlayer(name);
				if (p != null) {
					output.writeUTF(name);
					output.writeUTF(input.readUTF());
				} else {
					return;
				}
			} else if (type.equalsIgnoreCase("balanceall")) {
				output.writeUTF("balanceall");
				output.writeUTF(input.readUTF());
				output.writeUTF(input.readUTF());
				output.writeUTF(input.readUTF());
				output.writeUTF(input.readUTF());
				output.writeUTF(input.readUTF());
			} else if (type.equalsIgnoreCase("broadcast")) {
				output.writeUTF("broadcast");
				output.writeUTF(input.readUTF());
				output.writeUTF(input.readUTF());
				output.writeUTF(input.readUTF());
			}
		} catch (IOException e) {
			ProxyServer.getInstance().getLogger().severe("An I/O error occurred!");
		}

		for (ServerInfo s : ProxyServer.getInstance().getServers().values()) {
			if (!s.getName().equals(senderServer.getInfo().getName()) && s.getPlayers().size() > 0) {
				ProxyServer.getInstance().getScheduler().runAsync(MPointsBungee.getInstance(), new SendMessTaskB(s, stream));
			}
		}

	}

}
