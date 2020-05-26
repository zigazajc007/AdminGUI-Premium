package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerInteractListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.hasItem()){
            if(event.getItem() != null){
                if(event.getItem().hasItemMeta() && event.getItem().getItemMeta() != null){
                    if(event.getItem().getItemMeta().hasLore() && event.getItem().getItemMeta().getLore() != null){
                        if(event.getItem().getItemMeta().getLore().contains(Message.chat(AdminGUI.getInstance().getConf().getString("admin_tools_lore", "&dClick me to open Admin GUI")))){
                            event.getPlayer().performCommand("admin");
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }

    }

}
