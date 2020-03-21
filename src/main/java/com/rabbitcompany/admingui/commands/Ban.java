package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.AdminBanSystem;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.admingui.utils.TargetPlayer;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.util.UUID;

public class Ban implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            if(args.length < 3){
                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cPlease use /ban <player> <time> <reason>"));
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

                long mil_year = 31556952000L;
                long mil_month = 2592000000L;
                long mil_day = 86400000L;
                long mil_hour = 3600000L;
                long mil_minute = 60000L;

                if(NumberUtils.isNumber(AdminUI.stripNonDigits(str_time))){
                    int number = Integer.parseInt(AdminUI.stripNonDigits(str_time));
                    long time = 0L;

                    if(str_time.contains("min") || str_time.contains("minute") || str_time.contains("minutes")){
                        time = mil_minute;
                    }else if(str_time.contains("h") || str_time.contains("hour") || str_time.contains("hours")){
                        time = mil_hour;
                    }else if(str_time.contains("d") || str_time.contains("day") || str_time.contains("days")){
                        time = mil_day;
                    }else if(str_time.contains("m") || str_time.contains("month") || str_time.contains("months")){
                        time = mil_month;
                    }else if(str_time.contains("y") || str_time.contains("year") || str_time.contains("years")){
                        time = mil_year;
                    }

                    Date until = new Date(System.currentTimeMillis() + (number * time));

                    if(silence){
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + AdminBanSystem.banPlayer("Console", "Console", Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, reason.toString(), AdminBanSystem.sdf.format(until)));
                    }else{
                        Bukkit.broadcastMessage(Message.getMessage(UUID.randomUUID(), "prefix") + AdminBanSystem.banPlayer("Console", "Console", Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, reason.toString(), AdminBanSystem.sdf.format(until)));
                    }

                    Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(str_player));
                    if(target_player != null){
                        if(target_player.isOnline()){
                            target_player.kickPlayer(""+TargetPlayer.banCustomReason(target_player.getUniqueId(), reason.toString(), AdminBanSystem.sdf.format(until)));
                        }
                    }

                }else{
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "is_not_a_number").replace("{number}", str_time));
                }
            }
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

                long mil_year = 31556952000L;
                long mil_month = 2592000000L;
                long mil_day = 86400000L;
                long mil_hour = 3600000L;
                long mil_minute = 60000L;

                if(NumberUtils.isNumber(AdminUI.stripNonDigits(str_time))){
                    int number = Integer.parseInt(AdminUI.stripNonDigits(str_time));
                    long time = 0L;

                    if(str_time.contains("min") || str_time.contains("minute") || str_time.contains("minutes")){
                        time = mil_minute;
                    }else if(str_time.contains("h") || str_time.contains("hour") || str_time.contains("hours")){
                        time = mil_hour;
                    }else if(str_time.contains("d") || str_time.contains("day") || str_time.contains("days")){
                        time = mil_day;
                    }else if(str_time.contains("m") || str_time.contains("month") || str_time.contains("months")){
                        time = mil_month;
                    }else if(str_time.contains("y") || str_time.contains("year") || str_time.contains("years")){
                        time = mil_year;
                    }

                    Date until = new Date(System.currentTimeMillis() + (number * time));

                    if(silence){
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + AdminBanSystem.banPlayer(player.getUniqueId().toString(), player.getName(), Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, reason.toString(), AdminBanSystem.sdf.format(until)));
                    }else{
                        Bukkit.broadcastMessage(Message.getMessage(player.getUniqueId(), "prefix") + AdminBanSystem.banPlayer(player.getUniqueId().toString(), player.getName(), Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, reason.toString(), AdminBanSystem.sdf.format(until)));
                    }
                    Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(str_player));
                    if(target_player != null){
                        if(target_player.isOnline()){
                            target_player.kickPlayer(""+TargetPlayer.banCustomReason(target_player.getUniqueId(), reason.toString(), AdminBanSystem.sdf.format(until)));
                        }
                    }

                }else{
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "is_not_a_number").replace("{number}", str_time));
                }
            }
        }else{
            player.sendMessage(Message.getMessage(player.getUniqueId(),"prefix") + Message.getMessage(player.getUniqueId(), "permission"));
        }

        return true;
    }
}
