package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.adminbans.AdminBans;
import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Channel;
import com.rabbitcompany.admingui.utils.Initialize;
import com.rabbitcompany.admingui.utils.Item;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.SQLException;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;

public class PlayerJoinListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerJoinListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)){
            Channel.send(event.getPlayer().getName(),"send", "online_players");
        }else{
            AdminUI.online_players.add(event.getPlayer().getName());
        }

        AdminUI.skulls_players.put(event.getPlayer().getName(), Item.pre_createPlayerHead(event.getPlayer().getName()));

        if(adminGUI.getConf().getInt("initialize_gui",0) == 1) {
            if(!AdminUI.task_gui.containsKey(event.getPlayer().getUniqueId())){
                Initialize.GUI(event.getPlayer(), event.getPlayer().getInventory().getHelmet());
            }
        }
    }
}
