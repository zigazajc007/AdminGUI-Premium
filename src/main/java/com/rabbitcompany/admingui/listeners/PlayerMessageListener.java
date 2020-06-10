package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class PlayerMessageListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerMessageListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        String message = event.getMessage();

        if(p.hasPermission("admingui.chat.staff") && AdminUI.admin_staff_chat.getOrDefault(p.getUniqueId(), false)){
            event.setCancelled(true);

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if(player.hasPermission("admingui.chat.staff")){
                    player.sendMessage(Message.chat(adminGUI.getConf().getString("asc_format").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
                }
            }
            Bukkit.getConsoleSender().sendMessage(Message.chat(adminGUI.getConf().getString("asc_format").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
        }else{
            if(adminGUI.getConf().getBoolean("ac_enabled", false)){

                if(Bukkit.getServer().getPluginManager().getPlugin("AdminBans") != null){
                    if(AdminBansAPI.isPlayerMuted(event.getPlayer().getUniqueId(), AdminBansAPI.server_name)){
                        return;
                    }
                }

                if(!p.hasPermission("admingui.chat.advertisement.bypass")){
                    if(!message.endsWith(".")){
                        message = message.replace("."," ");
                    }
                }

                if(adminGUI.getConf().getBoolean("ac_beautifier", true)){
                    message = message.toLowerCase();
                    if(!message.endsWith(".") && !message.endsWith("!") && !message.endsWith("?")){
                        message = message + ".";
                    }
                }

                List<String> filters = adminGUI.getConf().getStringList("ac_filter");

                if(!p.hasPermission("admingui.chat.filter.bypass")){
                    for (String filter : filters) {
                        message = message.replace(filter, "****");
                    }
                }

                if(adminGUI.getConf().getBoolean("ac_beautifier", true)){
                    message = Character.toUpperCase(message.charAt(0)) + message.substring(1);
                }

                if(p.hasPermission("admingui.chat.color") || p.hasPermission("admingui.chat.colors")){
                    event.setFormat(Message.chat(adminGUI.getConf().getString("ac_format").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
                }else{
                    event.setFormat(adminGUI.getConf().getString("ac_format").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message));
                }
            }
        }
    }
}
