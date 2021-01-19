package com.rabbitcompany.adminguibungee.listeners;

import com.rabbitcompany.adminguibungee.AdminGUIBungee;
import com.rabbitcompany.adminguibungee.utils.Channel;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

public class PluginMessageListener implements Listener {

    @EventHandler
    public void onPluginMessageEvent(PluginMessageEvent e) {
        if(e.getTag().equalsIgnoreCase("my:admingui")) {
            ByteArrayInputStream arrayInput = new ByteArrayInputStream(e.getData());
            DataInputStream input = new DataInputStream(arrayInput);

            try {
                String sender = input.readUTF();
                String subchannel = input.readUTF();

                switch(subchannel){
                    case "connect":
                        String target = input.readUTF();
                        AdminGUIBungee.getInstance().getProxy().getPlayer(sender).connect(AdminGUIBungee.getInstance().getProxy().getPlayer(target).getServer().getInfo());
                        break;
                    case "chat":
                        String message = input.readUTF();
                        Channel.sendToAllServers(sender, "chat", message);
                        break;
                    case "send":
                        String action = input.readUTF();
                        switch (action){
                            case "online_players":
                                StringBuilder online_players = new StringBuilder();
                                for (ProxiedPlayer proxiedPlayer : AdminGUIBungee.getInstance().getProxy().getPlayers()) {
                                    online_players.append(proxiedPlayer.getName()).append(";");
                                }
                                Channel.send(sender,"online_players", online_players.toString());
                                break;
                        }
                        break;
                    case "rank":
                        String target_uuid = input.readUTF();
                        String target_name = input.readUTF();
                        String rank = input.readUTF();
                        Channel.sendToAllServers(sender, "rank", target_uuid, target_name, rank);
                        break;
                    case "gamemode":
                        String player = input.readUTF();
                        String gamemode = input.readUTF();
                        Channel.send(sender,"gamemode", player, gamemode);
                        break;
                }
                input.close();
            } catch (IOException ignored) { }
        }
    }
}
