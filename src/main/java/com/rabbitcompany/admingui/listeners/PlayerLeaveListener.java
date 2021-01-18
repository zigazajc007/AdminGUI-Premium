package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Channel;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

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

        if(adminGUI.getConf().getBoolean("ac_enabled", false) && adminGUI.getConf().getDouble("ac_delay", 0) > 0 ){
            AdminUI.admin_chat_delay.remove(event.getPlayer().getUniqueId());
        }

        AdminUI.skulls_players.remove(event.getPlayer().getName());
    }
}
