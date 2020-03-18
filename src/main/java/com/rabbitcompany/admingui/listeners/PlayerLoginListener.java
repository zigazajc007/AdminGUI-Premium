package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.AdminBanSystem;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerLoginListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event){
        if(event.getResult() == PlayerLoginEvent.Result.ALLOWED){

            //Admin Ban System
            if(AdminGUI.conn != null && AdminGUI.getInstance().getConf().getBoolean("admin_ban_system_enabled", false)){
                if(AdminBanSystem.isPlayerBannedUUID(event.getPlayer().getUniqueId().toString())){
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, AdminBanSystem.playerBannedMessage(event.getPlayer().getUniqueId().toString()));
                }
            }

            if(AdminUI.maintenance_mode && !event.getPlayer().hasPermission("admingui.maintenance")){
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Message.getMessage(event.getPlayer().getUniqueId(), "prefix") + Message.getMessage(event.getPlayer().getUniqueId(), "message_maintenance"));
            }
        }
    }

}
