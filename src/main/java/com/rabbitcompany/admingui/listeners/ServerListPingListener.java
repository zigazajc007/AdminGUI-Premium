package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener implements Listener {

    private final AdminGUI adminGUI;

    public ServerListPingListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event){

        if(adminGUI.getConf().getBoolean("motd_changer_enabled", false)){
            event.setMotd(Message.chat(adminGUI.getConf().getString("motd_changer_motd", "&6&lWelcome to the server")));
            event.setMaxPlayers(adminGUI.getConf().getInt("motd_changer_max_players", Bukkit.getMaxPlayers()));
        }

    }

}
