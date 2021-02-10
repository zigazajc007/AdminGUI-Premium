package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayersDroppingItemsListener implements Listener {

    private AdminGUI adminGUI;

    public PlayersDroppingItemsListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDroppingItems(PlayerDropItemEvent event){
        Player p = event.getPlayer();

        if(Settings.freeze.getOrDefault(p.getUniqueId(), false) && AdminGUI.getInstance().getConf().getBoolean("freeze_drop_items", true)) event.setCancelled(true);
    }

}
