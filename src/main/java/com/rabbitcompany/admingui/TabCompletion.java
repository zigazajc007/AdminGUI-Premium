package com.rabbitcompany.admingui;

import com.rabbitcompany.admingui.ui.AdminUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if(command.getName().equalsIgnoreCase("admin")){
            List<String> completions = new ArrayList<>();

           if(args.length == 1){

               //SQL
                if(AdminGUI.conn != null){
                    completions.addAll(AdminUI.online_players);
                }else{
                    for(Player all : Bukkit.getServer().getOnlinePlayers()) {
                        completions.add(all.getName());
                    }
                }

                if(sender.hasPermission("admingui.reload")){
                    completions.add("reload");
                }

                if(sender.hasPermission("admingui.chat")){
                    completions.add("chat");
                }

                completions.add("initialize");

           }else if(args.length == 2 && args[0].equals("initialize")){
               completions.add("gui");
               completions.add("players");
           }
            return completions;
        }else if(command.getName().equalsIgnoreCase("unban")){
            List<String> completions = new ArrayList<>();

            if(args.length == 1){
                //SQL
                if(AdminGUI.conn != null){
                    completions.addAll(AdminUI.online_players);
                }else{
                    for(Player all : Bukkit.getServer().getOnlinePlayers()) {
                        completions.add(all.getName());
                    }
                }
            }
            return completions;
        }else if(command.getName().equalsIgnoreCase("ban")){
            List<String> completions = new ArrayList<>();

            if(args.length == 1){
                //SQL
                if(AdminGUI.conn != null){
                    completions.addAll(AdminUI.online_players);
                }else{
                    for(Player all : Bukkit.getServer().getOnlinePlayers()) {
                        completions.add(all.getName());
                    }
                }
            }else if(args.length == 2){

                //Minutes
                completions.add("15min");
                completions.add("30min");

                //Hours
                completions.add("1h");
                completions.add("3h");
                completions.add("5h");

                //Days
                completions.add("1d");
                completions.add("5d");
                completions.add("10d");

                //Months
                completions.add("1m");
                completions.add("3m");
                completions.add("10m");

                //Years
                completions.add("1y");
                completions.add("3y");
                completions.add("10y");

            }else if(args.length == 3){
                completions.add("Advertising");
                completions.add("Hacking");
                completions.add("Swearing");
                completions.add("Griefing");
                completions.add("Spamming");
            }

            return completions;
        }

        return null;
    }
}
