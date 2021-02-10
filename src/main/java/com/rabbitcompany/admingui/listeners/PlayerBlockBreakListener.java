package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBlockBreakListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerBlockBreakListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerBreakBlock(BlockBreakEvent event){
        Player p = event.getPlayer();

        if(Settings.freeze.getOrDefault(p.getUniqueId(), false) && AdminGUI.getInstance().getConf().getBoolean("freeze_break_blocks", true)) event.setCancelled(true);
    }

}
