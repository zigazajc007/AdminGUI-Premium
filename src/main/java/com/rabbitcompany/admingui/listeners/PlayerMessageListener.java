package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Colors;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class PlayerMessageListener implements Listener {

    private AdminGUI adminGUI;

    public PlayerMessageListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        String message = event.getMessage();

        if(AdminUI.freeze.getOrDefault(p.getUniqueId(), false) && AdminGUI.getInstance().getConf().getBoolean("freeze_send_message", true)){
            event.setCancelled(true);

            if(AdminGUI.getInstance().getConf().getBoolean("freeze_admin_chat", true) && AdminUI.admin_staff_chat.getOrDefault(p.getUniqueId(), false)) {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if(player.hasPermission("admingui.chat.staff") || AdminUI.freeze.getOrDefault(player.getUniqueId(), false)){
                        player.sendMessage(Message.chat(adminGUI.getConf().getString("asc_format").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
                    }
                }
                Bukkit.getConsoleSender().sendMessage(Message.chat(adminGUI.getConf().getString("asc_format").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
            }

            return;
        }

        if(p.hasPermission("admingui.chat.staff") && AdminUI.admin_staff_chat.getOrDefault(p.getUniqueId(), false)){
            event.setCancelled(true);

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if(player.hasPermission("admingui.chat.staff")){
                    player.sendMessage(Message.chat(adminGUI.getConf().getString("asc_format").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
                }
            }
            Bukkit.getConsoleSender().sendMessage(Message.chat(adminGUI.getConf().getString("asc_format").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
        }else{
            if(adminGUI.getConf().getBoolean("ac_enabled", false)){

                if(Bukkit.getServer().getPluginManager().getPlugin("AdminBans") != null){
                    if(AdminBansAPI.isPlayerMuted(event.getPlayer().getUniqueId(), AdminBansAPI.server_name)){
                        return;
                    }
                }

                if (!p.hasPermission("admingui.chat.color") && !p.hasPermission("admingui.chat.colors")) {
                    message = message.replace("&", "");
                }

                if(!p.hasPermission("admingui.chat.advertisement.bypass")){
                    if(!message.endsWith(".")){
                        message = message.replace("."," ");
                    }
                }

                if(adminGUI.getConf().getBoolean("ac_beautifier", true)){
                    message = message.toLowerCase();
                    if(!message.endsWith(".") && !message.endsWith("!") && !message.endsWith("?")){
                        message = message + ".";
                    }
                }

                List<String> filters = adminGUI.getConf().getStringList("ac_filter");

                if(!p.hasPermission("admingui.chat.filter.bypass")){
                    for (String filter : filters) {
                        message = message.replace(filter, "****");
                    }
                }

                if(adminGUI.getConf().getBoolean("ac_beautifier", true)){
                    message = Character.toUpperCase(message.charAt(0)) + message.substring(1);
                }

                switch (AdminUI.chat_color.getOrDefault(p.getUniqueId(), "LIGHT_GRAY_WOOL")){
                    case "WHITE_WOOL":
                        message = Message.chat("&f" + message);
                        break;
                    case "ORANGE_WOOL":
                        message = Message.chat("&6" + message);
                        break;
                    case "MAGENTA_WOOL":
                        message = Message.chat("&d" + message);
                        break;
                    case "LIGHT_BLUE_WOOL":
                        message = Message.chat("&b" + message);
                        break;
                    case "YELLOW_WOOL":
                        message = Message.chat("&e" + message);
                        break;
                    case "LIME_WOOL":
                        message = Message.chat("&a" + message);
                        break;
                    case "GRAY_WOOL":
                        message = Message.chat("&8" + message);
                        break;
                    case "LIGHT_GRAY_WOOL":
                        message = Message.chat("&7" + message);
                        break;
                    case "CYAN_WOOL":
                        message = Message.chat("&3" + message);
                        break;
                    case "PURPLE_WOOL":
                        message = Message.chat("&5" + message);
                        break;
                    case "BLUE_WOOL":
                        message = Message.chat("&9" + message);
                        break;
                    case "GREEN_WOOL":
                        message = Message.chat("&2" + message);
                        break;
                    case "RED_WOOL":
                        message = Message.chat("&4" + message);
                        break;
                    case "BLACK_WOOL":
                        message = Message.chat("&0" + message);
                        break;
                    case "CLOCK":
                        message = Message.chat(Colors.rainbowText(message));
                        break;
                    case "EXPERIENCE_BOTTLE":
                        message = Colors.gradientText(message);
                        break;
                }

                if(p.hasPermission("admingui.chat.color") || p.hasPermission("admingui.chat.colors")){
                    if(Bukkit.getVersion().contains("1.16")){
                        message = Colors.toHex(message);
                    }
                    event.setFormat(Message.chat(adminGUI.getConf().getString("ac_format").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
                }else{
                    event.setFormat(adminGUI.getConf().getString("ac_format").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message));
                }
            }
        }
    }
}
