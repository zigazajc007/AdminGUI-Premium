package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Item;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Map;

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

        if(AdminUI.freeze.getOrDefault(p.getUniqueId(), false) && AdminGUI.getInstance().getConf().getBoolean("freeze_execute_commands", true)){
            event.setCancelled(true);
            return;
        }

        if(adminGUI.getConf().getBoolean("acs_enabled", false)){
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if(player.hasPermission("admingui.chat.spy") && AdminUI.command_spy.getOrDefault(player.getUniqueId(), false) && !player.getUniqueId().equals(p.getUniqueId())){
                    for (String ignored_command: adminGUI.getConf().getStringList("acs_ignore")) if(message.startsWith(ignored_command)) return;
                    player.sendMessage(Message.chat(adminGUI.getConf().getString("acs_format").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
                }
            }
        }


        for (Map.Entry<String, Object> slots : AdminGUI.getInstance().getConf().getConfigurationSection("ccc").getValues(false).entrySet()) {

            List<String> aliases = adminGUI.getConf().getStringList("ccc." + slots.getKey() + ".aliases");

            int firstSpaceIndex = message.indexOf(" ");
            if(firstSpaceIndex == -1) firstSpaceIndex = message.length();

            if(message.startsWith("/" + slots.getKey()) || aliases.contains(message.substring(0, firstSpaceIndex).replace("/", ""))){
                String name = adminGUI.getConf().getString("ccc." + slots.getKey() + ".name");
                String format = adminGUI.getConf().getString("ccc." + slots.getKey() + ".format");
                String permission = adminGUI.getConf().getString("ccc." + slots.getKey() + ".permission");

                if(name == null) name = slots.getKey();

                if(p.hasPermission(permission)){
                    String current_channel = AdminUI.custom_chat_channel.getOrDefault(p.getUniqueId(), "");

                    if(message.replace("/", "").equals(slots.getKey()) || !message.contains(" ")){
                        if(current_channel.equals(slots.getKey())){
                            AdminUI.custom_chat_channel.remove(p.getUniqueId());
                            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_disabled").replace("{name}", name));
                        }else{
                            AdminUI.custom_chat_channel.put(p.getUniqueId(), slots.getKey());
                            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_enabled").replace("{name}", name));
                        }
                    }else{

                        String message2 = message.substring(message.indexOf(" ") + 1);

                        for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                            if (target.hasPermission(permission)) {
                                target.sendMessage(Message.chat(format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message2)));
                            }
                        }
                        Bukkit.getConsoleSender().sendMessage(Message.chat(format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message2)));

                    }
                }else{
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "permission"));
                }
                event.setCancelled(true);
            }
        }


    }

}
