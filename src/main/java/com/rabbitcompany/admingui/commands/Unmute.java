package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.utils.AdminBanSystem;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Unmute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            if(args.length != 1){
                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cPlease use /unmute <player>"));
            }else{
                String str_player = args[0];
                if(AdminBanSystem.isPlayerMuted(str_player)){
                    AdminBanSystem.unMutePlayerName(str_player);
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_unmute_player").replace("{player}", str_player));
                }else{
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&a" + str_player + " isn't muted."));
                }
            }
            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission("admingui.unmute")){
            if(args.length != 1){
                player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.chat("&cPlease use /unmute <player>"));
            }else{
                String str_player = args[0];
                if(AdminBanSystem.isPlayerMuted(str_player)){
                    AdminBanSystem.unMutePlayerName(str_player);
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_unmute_player").replace("{player}", str_player));
                }else{
                    player.sendMessage(Message.getMessage(player.getUniqueId(),"prefix") + Message.chat("&a" + str_player + " isn't muted."));
                }
            }
        }else{
            player.sendMessage(Message.getMessage(player.getUniqueId(),"prefix") + Message.getMessage(player.getUniqueId(), "permission"));
        }

        return true;
    }
}
