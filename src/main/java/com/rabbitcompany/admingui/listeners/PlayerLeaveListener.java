package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class PlayerLeaveListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerLeaveListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        //SQL
        if(AdminGUI.conn != null){
            try {
                AdminGUI.mySQL.update("DELETE FROM admingui_players WHERE username = '" + event.getPlayer().getName() + "';");
            } catch (SQLException ignored) { }
        }else{
            AdminUI.online_players.remove(event.getPlayer().getName());
            AdminUI.skulls_players.remove(event.getPlayer().getName());
        }

    }
}
