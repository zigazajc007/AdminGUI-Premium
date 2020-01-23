package com.rabbitcompany.admingui;

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

                for(Player all : Bukkit.getServer().getOnlinePlayers()) {
                    completions.add(all.getName());
                }

                if(sender.hasPermission("admingui.reload")){
                    completions.add("reload");
                }

                completions.add("initialize");

           }else if(args.length == 2 && args[0].equals("initialize")){
               completions.add("gui");
               completions.add("players");
           }
            return completions;
        }

        return null;
    }
}
