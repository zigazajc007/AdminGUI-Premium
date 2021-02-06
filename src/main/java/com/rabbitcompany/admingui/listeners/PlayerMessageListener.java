package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Channel;
import com.rabbitcompany.admingui.utils.Colors;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.admingui.utils.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

            String freeze_channel = AdminGUI.getInstance().getConf().getString("freeze_admin_chat", null);

            if(freeze_channel != null) {
                String format = adminGUI.getConf().getString("ccc." + freeze_channel + ".format");
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if(player.hasPermission(adminGUI.getConf().getString("ccc." + freeze_channel + ".permission")) || AdminUI.freeze.getOrDefault(player.getUniqueId(), false)){
                        player.sendMessage(Message.chat(format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
                    }
                }
                Bukkit.getConsoleSender().sendMessage(Message.chat(format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
            }
            return;
        }

        String command = AdminUI.custom_chat_channel.getOrDefault(p.getUniqueId(), "");
        if(!command.equals("")){
            event.setCancelled(true);

            String format = adminGUI.getConf().getString("ccc." + command + ".format");
            String permission = adminGUI.getConf().getString("ccc." + command + ".permission");

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if(player.hasPermission(permission)){
                    player.sendMessage(Message.chat(format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
                }
            }
            Bukkit.getConsoleSender().sendMessage(Message.chat(format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));

        }else{
            if(adminGUI.getConf().getBoolean("ac_enabled", false)){

                if(AdminUI.muted_chat && !p.hasPermission("admingui.chat.mute.bypass")){
                    event.setCancelled(true);
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "message_admin_chat_muted"));
                    return;
                }

                if(Bukkit.getServer().getPluginManager().getPlugin("AdminBans") != null){
                    if(AdminBansAPI.isPlayerMuted(event.getPlayer().getUniqueId(), AdminBansAPI.server_name)) return;
                }

                if(adminGUI.getConf().getDouble("ac_delay", 0) > 0 && !p.hasPermission("admingui.chat.delay.bypass")){
                    long last_message_send = AdminUI.admin_chat_delay.getOrDefault(p.getUniqueId(), 0L);
                    if(last_message_send != 0L){
                        if(last_message_send + (adminGUI.getConf().getDouble("ac_delay", 0) * 1000) >= System.currentTimeMillis()){
                            event.setCancelled(true);
                            p.sendMessage(Message.chat(adminGUI.getConf().getString("ac_delay_message", "&cYou need to wait {seconds} seconds to sent another message!").replace("{seconds}", ""+adminGUI.getConf().getString("ac_delay", "0"))));
                            return;
                        }else{
                            AdminUI.admin_chat_delay.put(p.getUniqueId(), System.currentTimeMillis());
                        }
                    }else{
                        AdminUI.admin_chat_delay.put(p.getUniqueId(), System.currentTimeMillis());
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

                if(adminGUI.getConf().getConfigurationSection("ac_emojis") != null){
                    for(String emoji : adminGUI.getConf().getConfigurationSection("ac_emojis").getValues(false).keySet())
                        message = message.replace(emoji, adminGUI.getConf().getString("ac_emojis."+emoji, emoji));
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

                //TODO: Permissions
                String prefix = Permissions.getPrefix(p.getUniqueId());
                String suffix = Permissions.getSuffix(p.getUniqueId());

                //VaultAPI
                String vault_prefix = "";
                String vault_suffix = "";
                if(AdminGUI.vault){
                    vault_prefix = AdminGUI.getVaultChat().getPlayerPrefix(p);
                    vault_suffix = AdminGUI.getVaultChat().getPlayerSuffix(p);
                }

                message = Colors.toHex(message);

                String format = Colors.toHex(Message.chat(adminGUI.getConf().getString("ac_format", "&7{display_name} &7> {message}").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{server_name}", adminGUI.getConf().getString("server_name", "Default")).replace("{prefix}", prefix).replace("{suffix}", suffix).replace("{vault_prefix}", vault_prefix).replace("{vault_suffix}", vault_suffix)));

                if (!p.hasPermission("admingui.chat.color") && !p.hasPermission("admingui.chat.colors")) message = ChatColor.stripColor(message);

                event.setMessage(message);
                event.setFormat(format.replace("{message}", message));

                if(adminGUI.getConf().getBoolean("bungeecord_enabled", false) && adminGUI.getConf().getBoolean("bungeecord_admin_chat", false)) {
                    String bungee_format = Colors.toHex(Message.chat(adminGUI.getConf().getString("bungeecord_admin_chat_format", "&7[{server_name}] &7{display_name} &7> {message}").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{server_name}", adminGUI.getConf().getString("server_name", "Default")).replace("{prefix}", prefix).replace("{suffix}", suffix).replace("{vault_prefix}", vault_prefix).replace("{vault_suffix}", vault_suffix)));
                    Channel.send(p.getUniqueId().toString(), "chat", bungee_format.replace("{message}", message));
                }
            }
        }
    }
}
