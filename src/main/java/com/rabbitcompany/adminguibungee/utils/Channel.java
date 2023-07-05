package com.rabbitcompany.adminguibungee.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.rabbitcompany.adminguibungee.AdminGUIBungee;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Map;

public class Channel {

	public static void send(String player, String subchannel, String... data) {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		output.writeUTF(player);
		output.writeUTF(subchannel);
		for (String da : data) output.writeUTF(da);

		try {
			if (AdminGUIBungee.getInstance().getProxy().getPlayer(player).isConnected())
				AdminGUIBungee.getInstance().getProxy().getPlayer(player).getServer().getInfo().sendData("my:admingui", output.toByteArray());
		} catch (Exception ignored) {
		}

	}

	public static void sendToAllServers(String player, String subchannel, String... data) {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		output.writeUTF(player);
		output.writeUTF(subchannel);
		for (String da : data) output.writeUTF(da);

		Map<String, ServerInfo> servers = AdminGUIBungee.getInstance().getProxy().getServers();
		for (Map.Entry<String, ServerInfo> en : servers.entrySet()) {
			String name = en.getKey();
			AdminGUIBungee.getInstance().getProxy().getServerInfo(name).sendData("my:admingui", output.toByteArray());
		}
	}

}
