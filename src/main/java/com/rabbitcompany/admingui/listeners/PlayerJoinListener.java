package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Item;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static org.bukkit.Bukkit.getServer;

public class PlayerJoinListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerJoinListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        AdminUI.online_players.add(event.getPlayer().getName());
        AdminUI.skulls.put(event.getPlayer().getName(), Item.pre_createPlayerHead(event.getPlayer().getName()));

        if(event.getPlayer().hasPermission("admingui.admin")){
            int  max_value = AdminUI.skulls.size();
            getServer().getScheduler().scheduleSyncRepeatingTask(AdminGUI.getInstance(), new Runnable() {
                int i = 0;
                @Override
                public void run() {
                    if(i < max_value){
                        event.getPlayer().getInventory().setHelmet(AdminUI.skulls.get(AdminUI.skulls.keySet().toArray()[i].toString()));
                    }else{
                        event.getPlayer().getInventory().setHelmet(null);
                        Bukkit.getServer().getScheduler().cancelTasks(AdminGUI.getInstance());
                    }
                    i++;
                }
            }, 20L, 20L);
        }
    }
}
