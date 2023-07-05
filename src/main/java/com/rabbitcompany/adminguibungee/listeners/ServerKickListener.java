package com.rabbitcompany.adminguibungee.listeners;

import com.rabbitcompany.adminguibungee.AdminGUIBungee;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class ServerKickListener implements Listener {

	public static boolean isServerOnline(String ip, int port) {
		try {
			Socket s = new Socket();
			s.connect(new InetSocketAddress(ip, port));
			s.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@EventHandler
	public void onServerKickEvent(ServerKickEvent e) {

		ServerInfo kickedFrom;

		if (e.getPlayer().getServer() != null) {
			kickedFrom = e.getPlayer().getServer().getInfo();
		} else if (AdminGUIBungee.getInstance().getProxy().getReconnectHandler() != null) {
			kickedFrom = AdminGUIBungee.getInstance().getProxy().getReconnectHandler().getServer(e.getPlayer());
		} else {
			kickedFrom = AbstractReconnectHandler.getForcedHost(e.getPlayer().getPendingConnection());
			if (kickedFrom == null)
				kickedFrom = ProxyServer.getInstance().getServerInfo(e.getPlayer().getPendingConnection().getListener().getDefaultServer());
		}

		List<String> server_list = AdminGUIBungee.config.getStringList("fallback_server_list");

		String fallback_server = AdminGUIBungee.config.getStringList("fallback_server_list").get(0);

		for (String server : server_list) {
			if (!server.equals(kickedFrom.getName())) {
				String host = AdminGUIBungee.bungee_config.getString("servers." + server + ".address");
				String[] host_list = host.split(":");
				if (isServerOnline(host_list[0], Integer.parseInt(host_list[1]))) {
					fallback_server = server;
					break;
				}
			}
		}

		ServerInfo kickTo = AdminGUIBungee.getInstance().getProxy().getServerInfo(fallback_server);

		if (kickedFrom != null && kickedFrom.equals(kickTo)) return;

		e.setCancelled(true);
		e.setCancelServer(kickTo);
	}

}
