package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;

public class Initialize {
    
    public static void GUI(Player player){
        if (player.hasPermission("admingui.admin")) {
            int max_value = AdminUI.skulls.size();
            if(AdminGUI.getInstance().getConf().getBoolean("initialize_reminder", true)){
                player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_initializing_start"));
            }
            getServer().getScheduler().scheduleSyncRepeatingTask(AdminGUI.getInstance(), new Runnable() {
                int i = 0;
                @Override
                public void run() {
                    if (i < max_value) {
                        player.getInventory().setHelmet(AdminUI.skulls.get(AdminUI.skulls.keySet().toArray()[i].toString()));
                    } else {
                        player.getInventory().setHelmet(null);
                        if(AdminGUI.getInstance().getConf().getBoolean("initialize_reminder", true)){
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_initializing_finish"));
                        }
                        Bukkit.getServer().getScheduler().cancelTasks(AdminGUI.getInstance());
                    }
                    i++;
                }
            }, 20L, AdminGUI.getInstance().getConf().getInt("initialize_delay", 1) * 20L);
        }
    }
    
    public static void Players(Player player){
        if (player.hasPermission("admingui.admin")) {
            int max_value = AdminUI.skulls_players.size();
            if(AdminGUI.getInstance().getConf().getBoolean("initialize_reminder", true)){
                player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_initializing_start"));
            }
            getServer().getScheduler().scheduleSyncRepeatingTask(AdminGUI.getInstance(), new Runnable() {
                int i = 0;
                @Override
                public void run() {
                    if (i < max_value) {
                        player.getInventory().setHelmet(AdminUI.skulls_players.get(AdminUI.skulls_players.keySet().toArray()[i].toString()));
                    } else {
                        player.getInventory().setHelmet(null);
                        if(AdminGUI.getInstance().getConf().getBoolean("initialize_reminder", true)){
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_initializing_finish"));
                        }
                        Bukkit.getServer().getScheduler().cancelTasks(AdminGUI.getInstance());
                    }
                    i++;
                }
            }, 20L, AdminGUI.getInstance().getConf().getInt("initialize_delay", 1) * 20L);
        }
    }

    public static void StopTask(int taskID){
        Bukkit.getServer().getScheduler().cancelTask(taskID);
    }
    
}
