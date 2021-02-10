package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Channel;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.admingui.utils.Settings;
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

        if(adminGUI.getConf().getBoolean("cjlm_enabled", true)){
            if(adminGUI.getConf().getString("leave_message", "&7[&4-&7] &6{display_name}") != null){
                event.setQuitMessage(Message.chat(adminGUI.getConf().getString("leave_message", "&7[&4-&7] &6{display_name}").replace("{name}", event.getPlayer().getName()).replace("{display_name}", event.getPlayer().getDisplayName())));
            }else{
                event.setQuitMessage(null);
            }
        }

        if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)){
            Channel.send(event.getPlayer().getName(),"send", "online_players");
        }else{
            Settings.online_players.remove(event.getPlayer().getName());
        }

        if(adminGUI.getConf().getBoolean("ac_enabled", false) && adminGUI.getConf().getDouble("ac_delay", 0) > 0)
            Settings.admin_chat_delay.remove(event.getPlayer().getUniqueId());

        Settings.skulls_players.remove(event.getPlayer().getName());
    }
}
