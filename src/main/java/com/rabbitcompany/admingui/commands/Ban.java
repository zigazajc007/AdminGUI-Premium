package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.AdminBanSystem;
import com.rabbitcompany.admingui.utils.Message;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Timestamp;

public class Ban implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission("admingui.ban")){
            if(args.length < 3){
                 player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.chat("&cPlease use /ban <player> <time> <reason>"));
            }else{
                String str_player = args[0];
                String str_time = args[1];
                StringBuilder reason = new StringBuilder();
                boolean silence = false;

                for (int i = 2; i < args.length; i++){
                    if(!args[i].equals("-s")){
                        reason.append(args[i]).append(" ");
                    }else{
                        silence = true;
                    }
                }

                if(NumberUtils.isNumber(AdminUI.stripNonDigits(str_time))){
                    int number = Integer.parseInt(AdminUI.stripNonDigits(str_player));
                    if(str_time.contains("min") || str_time.contains("minute") || str_time.contains("minutes")){

                    }
                }

                AdminBanSystem.banPlayer(player.getUniqueId().toString(), player.getName(), "unknown", str_player, reason.toString(), str_time);

            }
        }

        return true;
    }
}
