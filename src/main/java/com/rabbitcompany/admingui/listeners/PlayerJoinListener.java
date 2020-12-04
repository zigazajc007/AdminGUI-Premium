package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerJoinListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();

        //TODO: Permissions
        if(AdminGUI.getInstance().getConf().getBoolean("mysql", false) && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false) && AdminGUI.getInstance().getConf().getInt("ap_storage_type", 0) == 2){
            if(Database.rankNeedFix(event.getPlayer().getName())) Database.fixRank(event.getPlayer().getUniqueId(), event.getPlayer().getName());
            Database.cacheRank(event.getPlayer().getUniqueId());
        }

        if(AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)){
            String rank = AdminGUI.getInstance().getPermissions().getString("ranks." + player.getName() + ".rank", null);
            if(rank != null){
                AdminGUI.getInstance().getPermissions().set("ranks." + player.getName(), null);
                AdminGUI.getInstance().getPermissions().set("ranks." + player.getUniqueId() + ".name", player.getName());
                AdminGUI.getInstance().getPermissions().set("ranks." + player.getUniqueId() + ".rank", rank);
                AdminGUI.getInstance().savePermissions();
            }
            TargetPlayer.refreshPermissions(player);
        }

        if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)){
            Channel.send(player.getName(),"send", "online_players");
        }else{
            AdminUI.online_players.add(player.getName());
        }

        AdminUI.skulls_players.put(player.getName(), Item.pre_createPlayerHead(player.getName()));

        if(AdminGUI.getInstance().getConf().getBoolean("atl_enabled", false)){
            TargetPlayer.refreshPlayerTabList(player);
        }

        if(adminGUI.getConf().getInt("initialize_gui",0) == 1) {
            if(!AdminUI.task_gui.containsKey(player.getUniqueId())){
                Initialize.GUI(player, player.getInventory().getHelmet());
            }
        }
    }
}
