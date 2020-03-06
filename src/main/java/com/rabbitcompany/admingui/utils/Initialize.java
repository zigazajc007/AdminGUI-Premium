package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

import static org.bukkit.Bukkit.getServer;

public class Initialize {

    public static void GUI(Player player, ItemStack helmet){
        if (player.hasPermission("admingui.admin")) {
            int max_value = AdminUI.skulls.size();
            if(AdminGUI.getInstance().getConf().getBoolean("initialize_reminder", true)){
                player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_initializing_start"));
            }
            int taskID = getServer().getScheduler().scheduleSyncRepeatingTask(AdminGUI.getInstance(), new Runnable() {
                int i = 0;
                @Override
                public void run() {
                    if(player.isOnline()){
                        if (i < max_value) {
                            player.getInventory().setHelmet(AdminUI.skulls.get(AdminUI.skulls.keySet().toArray()[i].toString()));
                        } else {
                            player.getInventory().setHelmet(helmet);
                            if(AdminGUI.getInstance().getConf().getBoolean("initialize_reminder", true)){
                                player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_initializing_finish"));
                            }
                            Bukkit.getServer().getScheduler().cancelTask(AdminUI.task_gui.get(player.getUniqueId()));
                            AdminUI.task_gui.remove(player.getUniqueId());
                        }
                        i++;
                    }else{
                        Bukkit.getServer().getScheduler().cancelTask(AdminUI.task_gui.get(player.getUniqueId()));
                        AdminUI.task_gui.remove(player.getUniqueId());
                    }
                }
            }, 5*20L, AdminGUI.getInstance().getConf().getInt("initialize_delay", 1) * 20L);
            AdminUI.task_gui.put(player.getUniqueId(), taskID);
        }
    }
    
    public static void Players(Player player, ItemStack helmet){
        if (player.hasPermission("admingui.admin")) {
            int max_value = AdminUI.skulls_players.size();
            if(AdminGUI.getInstance().getConf().getBoolean("initialize_reminder", true)){
                player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_initializing_start"));
            }
            int taskID = getServer().getScheduler().scheduleSyncRepeatingTask(AdminGUI.getInstance(), new Runnable() {
                int i = 0;
                @Override
                public void run() {
                    if(player.isOnline()){
                        if (i < max_value) {
                            player.getInventory().setHelmet(AdminUI.skulls_players.get(AdminUI.skulls_players.keySet().toArray()[i].toString()));
                        } else {
                            player.getInventory().setHelmet(helmet);
                            if(AdminGUI.getInstance().getConf().getBoolean("initialize_reminder", true)){
                                player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_initializing_finish"));
                            }
                            Bukkit.getServer().getScheduler().cancelTask(AdminUI.task_players.get(player.getUniqueId()));
                            AdminUI.task_players.remove(player.getUniqueId());
                        }
                        i++;
                    }else{
                        Bukkit.getServer().getScheduler().cancelTask(AdminUI.task_players.get(player.getUniqueId()));
                        AdminUI.task_players.remove(player.getUniqueId());
                    }
                }
            }, 20L, AdminGUI.getInstance().getConf().getInt("initialize_delay", 2) * 20L);
            AdminUI.task_players.put(player.getUniqueId(), taskID);
        }
    }

    public static void Database(){
        getServer().getScheduler().scheduleSyncRepeatingTask(AdminGUI.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    AdminGUI.mySQL.query("SELECT * FROM admingui_players;", results -> {
                        if (results != null) {
                            AdminUI.online_players.clear();
                            AdminUI.skulls_players.clear();
                            while (results.next()){
                                AdminUI.online_players.add(results.getString("username"));
                                AdminUI.skulls_players.put(results.getString("username"), Item.pre_createPlayerHead(results.getString("username")));
                            }
                        }
                    });
                } catch (SQLException ignored) { }
            }
        }, 20L, AdminGUI.getInstance().getConf().getInt("mysql_delay", 5) * 20L);
    }
}
