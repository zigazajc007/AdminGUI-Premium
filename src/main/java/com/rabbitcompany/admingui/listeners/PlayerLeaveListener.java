package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.adminbans.AdminBans;
import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Channel;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.SQLException;

public class PlayerLeaveListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerLeaveListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){

        if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)){
            Channel.send(event.getPlayer().getName(),"send", "online_players");
        }else{
            AdminUI.online_players.remove(event.getPlayer().getName());
        }

        AdminUI.skulls_players.remove(event.getPlayer().getName());
    }
}
