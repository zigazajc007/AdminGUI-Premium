package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.*;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;

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

        //TODO: Permissions
        if(AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)){
            PermissionAttachment player_attachment = player.addAttachment(AdminGUI.getInstance());
            String rank = AdminGUI.getInstance().getPermissions().getString("ranks."+player.getUniqueId().toString(), null);

            List<?> permissions;
            if(rank == null){
                permissions = AdminGUI.getInstance().getPermissions().getList("groups.default.permissions");
            }else{
                permissions = AdminGUI.getInstance().getPermissions().getList("groups." + rank + ".permissions");
            }

            for (Object permission: permissions) {
                player_attachment.setPermission(permission.toString(), true);
            }
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
