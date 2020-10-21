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

            if(AdminGUI.getInstance().getConf().getBoolean("ac_enabled", false)){
                if(args.length == 1) {
                    if (args[0].equals("mute")) {
                        if(AdminUI.muted_chat){
                            AdminUI.muted_chat = false;
                            Bukkit.broadcastMessage(Message.getMessage(UUID.randomUUID(), "message_admin_chat_unmute"));
                        }else{
                            AdminUI.muted_chat = true;
                            Bukkit.broadcastMessage(Message.getMessage(UUID.randomUUID(), "message_admin_chat_mute"));
                        }
                    }else{
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_chat_arguments"));
                    }
                }else{
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_chat_arguments"));
                }
            }else{
                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_chat_server_disabled"));
            }
            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission("admingui.chat")){
            if(AdminGUI.getInstance().getConf().getBoolean("ac_enabled", false)){
                if(args.length == 1){
                    if(args[0].equals("mute")){
                        if(player.hasPermission("admingui.chat.mute")){
                            if(AdminUI.muted_chat){
                                AdminUI.muted_chat = false;
                                Bukkit.broadcastMessage(Message.getMessage(UUID.randomUUID(), "message_admin_chat_unmute"));
                            }else{
                                AdminUI.muted_chat = true;
                                Bukkit.broadcastMessage(Message.getMessage(UUID.randomUUID(), "message_admin_chat_mute"));
                            }
                        }else{
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
                        }
                    }else{
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_chat_arguments"));
                    }
                }else{
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_chat_arguments"));
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
