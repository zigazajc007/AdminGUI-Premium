package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AdminChat implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {

            if(args.length != 0){
                StringBuilder message = new StringBuilder();

                for (String arg : args) {
                    message.append(arg).append(" ");
                }

                for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                    if (target.hasPermission("admingui.chat.staff")) {
                        target.sendMessage(Message.chat(AdminGUI.getInstance().getConf().getString("asc_format").replace("{name}", "Console").replace("{display_name}", "Console").replace("{message}", message)));
                    }
                }
                sender.sendMessage(Message.chat(AdminGUI.getInstance().getConf().getString("asc_format").replace("{name}", "Console").replace("{display_name}", "Console").replace("{message}", message)));
            }else{
                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cYou can only use /ac message"));
            }

            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission("admingui.chat.staff")){
            if(AdminGUI.getInstance().getConf().getBoolean("asc_enabled", false)) {
                if (args.length != 0) {

                    StringBuilder message = new StringBuilder();

                    for (String arg : args) {
                        message.append(arg).append(" ");
                    }

                    for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                        if (target.hasPermission("admingui.chat.staff")) {
                            target.sendMessage(Message.chat(AdminGUI.getInstance().getConf().getString("asc_format").replace("{name}", player.getName()).replace("{display_name}", player.getDisplayName()).replace("{message}", message)));
                        }
                    }
                    Bukkit.getConsoleSender().sendMessage(Message.chat(AdminGUI.getInstance().getConf().getString("asc_format").replace("{name}", player.getName()).replace("{display_name}", player.getDisplayName()).replace("{message}", message)));
                } else {
                    if (!AdminUI.admin_staff_chat.getOrDefault(player.getUniqueId(), false)) {
                        AdminUI.admin_staff_chat.put(player.getUniqueId(), true);
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_chat_enabled"));
                    } else {
                        AdminUI.admin_staff_chat.put(player.getUniqueId(), false);
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_chat_disabled"));
                    }
                }
            }else{
                player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_chat_server_disabled"));
            }
        }else{
            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
        }

        return true;
    }
}
