package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerCommandListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event){
        Player p = event.getPlayer();
        String message = event.getMessage();

        if(adminGUI.getConf().getBoolean("acs_enabled")){
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if(player.hasPermission("admingui.chat.spy") && AdminUI.command_spy.getOrDefault(player.getUniqueId(), false) && !player.getUniqueId().equals(p.getUniqueId())){
                    player.sendMessage(Message.chat(adminGUI.getConf().getString("acs_format").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
                }
            }
        }
    }

}
