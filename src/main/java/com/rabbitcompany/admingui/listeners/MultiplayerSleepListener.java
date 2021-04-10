package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class MultiplayerSleepListener implements Listener {

    private AdminGUI adminGUI;

    public MultiplayerSleepListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        if(event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)){
            Settings.players_sleeping++;
            if(Settings.players_sleeping >= (Bukkit.getOnlinePlayers().size() * adminGUI.getConf().getInt("ms_percentages", 50)) / 100.0){
                event.getPlayer().getWorld().setTime(0L);
                event.getPlayer().getWorld().setStorm(false);
                event.getPlayer().getWorld().setThundering(false);
                Settings.players_sleeping = 0;
            }
        }
    }

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        if(Settings.players_sleeping != 0) Settings.players_sleeping--;
    }

}
