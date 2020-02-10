package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Initialize;
import com.rabbitcompany.admingui.utils.Item;
import com.rabbitcompany.admingui.utils.Message;
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
        AdminUI.skulls_players.put(event.getPlayer().getName(), Item.pre_createPlayerHead(event.getPlayer().getName()));

        if(adminGUI.getConf().getInt("initialize_gui",1) == 1) {
            if(!AdminUI.task_gui.containsKey(event.getPlayer().getUniqueId())){
                Initialize.GUI(event.getPlayer(), event.getPlayer().getInventory().getHelmet());
            }
        }
    }
}
