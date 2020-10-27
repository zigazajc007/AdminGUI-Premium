package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Channel;
import com.rabbitcompany.admingui.utils.Initialize;
import com.rabbitcompany.admingui.utils.Item;
import com.rabbitcompany.admingui.utils.Message;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
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

        if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)){
            Channel.send(event.getPlayer().getName(),"send", "online_players");
        }else{
            AdminUI.online_players.add(event.getPlayer().getName());
        }

        AdminUI.skulls_players.put(event.getPlayer().getName(), Item.pre_createPlayerHead(event.getPlayer().getName()));

        if(AdminGUI.getInstance().getConf().getBoolean("atl_enabled", false)){
            if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
                event.getPlayer().setPlayerListHeader(PlaceholderAPI.setPlaceholders(event.getPlayer(), Message.chat(AdminGUI.getInstance().getConf().getString("atl_header", "&a{display_name}").replace("{name}", event.getPlayer().getName()).replace("{display_name}", event.getPlayer().getDisplayName()))));
                event.getPlayer().setPlayerListName(PlaceholderAPI.setPlaceholders(event.getPlayer(), Message.chat(AdminGUI.getInstance().getConf().getString("atl_format", "&a{display_name}").replace("{name}", event.getPlayer().getName()).replace("{display_name}", event.getPlayer().getDisplayName()))));
                event.getPlayer().setPlayerListFooter(PlaceholderAPI.setPlaceholders(event.getPlayer(), Message.chat(AdminGUI.getInstance().getConf().getString("atl_footer", "&a{display_name}").replace("{name}", event.getPlayer().getName()).replace("{display_name}", event.getPlayer().getDisplayName()))));
            }else{
                event.getPlayer().setPlayerListHeader(Message.chat(AdminGUI.getInstance().getConf().getString("atl_header", "&a{display_name}").replace("{name}", event.getPlayer().getName()).replace("{display_name}", event.getPlayer().getDisplayName())));
                event.getPlayer().setPlayerListName(Message.chat(AdminGUI.getInstance().getConf().getString("atl_format", "&a{display_name}").replace("{name}", event.getPlayer().getName()).replace("{display_name}", event.getPlayer().getDisplayName())));
                event.getPlayer().setPlayerListFooter(Message.chat(AdminGUI.getInstance().getConf().getString("atl_footer", "&a{display_name}").replace("{name}", event.getPlayer().getName()).replace("{display_name}", event.getPlayer().getDisplayName())));
            }
        }

        if(adminGUI.getConf().getInt("initialize_gui",0) == 1) {
            if(!AdminUI.task_gui.containsKey(event.getPlayer().getUniqueId())){
                Initialize.GUI(event.getPlayer(), event.getPlayer().getInventory().getHelmet());
            }
        }
    }
}
