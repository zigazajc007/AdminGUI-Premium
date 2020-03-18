package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Message;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerPlaceholderMessageListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerPlaceholderMessageListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPlaceholderChat(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        String message = event.getMessage();

        String chat_format = PlaceholderAPI.setPlaceholders(event.getPlayer(), adminGUI.getConf().getString("ac_format"));
        String chat_staff_format = PlaceholderAPI.setPlaceholders(event.getPlayer(), adminGUI.getConf().getString("asc_format"));

        if(p.hasPermission("admingui.chat.staff") && AdminUI.admin_staff_chat.getOrDefault(p.getUniqueId(), false)){
            event.setCancelled(true);

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if(player.hasPermission("admingui.chat.staff")){
                    player.sendMessage(Message.chat(chat_staff_format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
                }
            }
        }else{
            if(adminGUI.getConf().getBoolean("ac_enabled", false)){
                event.setCancelled(true);

                if(p.hasPermission("admingui.chat")){
                    if(p.hasPermission("admingui.chat.color") || p.hasPermission("admingui.chat.colors")){
                        Bukkit.broadcastMessage(Message.chat(chat_format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
                    }else{
                        Bukkit.broadcastMessage(chat_format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message));
                    }
                }else{
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "permission"));
                }
            }
        }
    }

}
