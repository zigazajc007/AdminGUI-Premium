package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class PlayerJoinListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerJoinListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();

        if(AdminGUI.getInstance().getPlayers().getString(player.getUniqueId().toString(), null) == null){
            AdminGUI.getInstance().getPlayers().set(player.getUniqueId() + ".name", player.getName());
            if(player.getAddress() != null && player.getAddress().getAddress() != null)
                AdminGUI.getInstance().getPlayers().set(player.getUniqueId() + ".ips", new String[]{player.getAddress().getAddress().toString().replace("/", "")});
            AdminGUI.getInstance().getPlayers().set(player.getUniqueId() + ".firstJoin", System.currentTimeMillis());
        }else{
            List<String> ips = AdminGUI.getInstance().getPlayers().getStringList(player.getUniqueId() + ".ips");
            if(player.getAddress() != null && player.getAddress().getAddress() != null)
                if(!ips.contains(player.getAddress().getAddress().toString().replace("/", ""))) ips.add(player.getAddress().getAddress().toString().replace("/", ""));
            AdminGUI.getInstance().getPlayers().set(player.getUniqueId() + ".ips", ips);
        }
        AdminGUI.getInstance().getPlayers().set(player.getUniqueId() + ".lastJoin", System.currentTimeMillis());
        AdminGUI.getInstance().savePlayers();

        //TODO: Permissions
        if(AdminGUI.getInstance().getConf().getBoolean("mysql", false) && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false) && AdminGUI.getInstance().getConf().getInt("ap_storage_type", 0) == 2){
            if(Database.rankNeedFix(player.getName())) Database.fixRank(player.getUniqueId(), player.getName());
            Database.cacheRank(player.getUniqueId());
        }

        if(AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)){
            String rank = AdminGUI.getInstance().getPlayers().getString(player.getName() + ".rank", null);
            if(rank != null){
                AdminGUI.getInstance().getPlayers().set(player.getName(), null);
                AdminGUI.getInstance().getPlayers().set(player.getUniqueId() + ".rank", rank);
                AdminGUI.getInstance().savePlayers();
            }
            TargetPlayer.refreshPermissions(player);
        }

        if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)){
            Channel.send(player.getName(),"send", "online_players");
        }else{
            AdminUI.online_players.add(player.getName());
        }

        AdminUI.skulls_players.put(player.getName(), Item.pre_createPlayerHead(player.getName()));

        if(AdminGUI.getInstance().getConf().getBoolean("atl_enabled", false)) TargetPlayer.refreshPlayerTabList(player);

        //Update Checker
        if(AdminGUI.getInstance().getConf().getBoolean("uc_enabled", true) && AdminGUI.getInstance().getConf().getInt("uc_send_type", 1) == 1 && AdminGUI.new_version != null && (player.hasPermission("admingui.admin") || player.isOp())){
            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.chat(AdminGUI.getInstance().getConf().getString("uc_notify", "&aNew update is available. Please update me to &b{version}&a.").replace("{version}", AdminGUI.new_version)));
        }

        if(adminGUI.getConf().getInt("initialize_gui",0) == 1) {
            if(!AdminUI.task_gui.containsKey(player.getUniqueId())) Initialize.GUI(player, player.getInventory().getHelmet());
        }
    }
}
