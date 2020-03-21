package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.utils.AdminBanSystem;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Kick implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            if(args.length < 2){
                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cPlease use /kick <player> <reason>"));
            }else{
                Player target = Bukkit.getPlayer(args[0]);
                if(target != null){
                    if(target.isOnline()) {

                        StringBuilder message = new StringBuilder();

                        for (int i = 1; i < args.length; i++){
                            message.append(args[i]).append(" ");
                        }

                        sender.sendMessage(Message.chat(AdminBanSystem.kickPlayer("Console", "Console", target.getUniqueId().toString(), target.getName(), message.toString())));
                        target.kickPlayer(Message.getMessage(target.getUniqueId(), "prefix") + Message.getMessage(target.getUniqueId(), "kick") + Message.chat(message.toString()));
                    }else{
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cPlayer " + target.getName() + " isn't online."));
                    }
                }else{
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "is_not_a_player"));
                }
            }
            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission("admingui.kick.other")){
            if(args.length < 2){
                player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.chat("&cPlease use /kick <player> <reason>"));
            }else{
                Player target = Bukkit.getPlayer(args[0]);
                if(target != null){
                    if(target.isOnline()) {
                        StringBuilder message = new StringBuilder();

                        for (int i = 1; i < args.length; i++){
                            message.append(args[i]).append(" ");
                        }

                        player.sendMessage(Message.chat(AdminBanSystem.kickPlayer(player.getUniqueId().toString(), player.getName(), target.getUniqueId().toString(), target.getName(), message.toString())));
                        target.kickPlayer(Message.getMessage(target.getUniqueId(), "prefix") + Message.getMessage(target.getUniqueId(), "kick") + Message.chat(message.toString()));
                    }else{
                        player.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cPlayer " + target.getName() + " isn't online."));
                    }
                }else{
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "is_not_a_player"));
                }
            }
        }else{
            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(),"permission"));
        }

        return true;
    }
}
