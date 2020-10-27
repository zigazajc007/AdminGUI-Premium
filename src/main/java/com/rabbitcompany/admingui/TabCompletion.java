package com.rabbitcompany.admingui;

import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Channel;
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

                if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)){
                    Channel.send(sender.getName(),"send", "online_players");
                    completions.addAll(AdminUI.online_players);
                }else{
                    for(Player all : Bukkit.getServer().getOnlinePlayers()) {
                        completions.add(all.getName());
                    }
                }

                if(sender.hasPermission("admingui.reload")){
                    completions.add("reload");
                }

                completions.add("tools");

                completions.add("initialize");

           }else if(args.length == 2 && args[0].equals("initialize")){
               completions.add("gui");
               completions.add("players");
           }
            return completions;
        }else if(command.getName().equalsIgnoreCase("adminchat")){
            List<String> completions = new ArrayList<>();

            if(args.length == 1){
                completions.add("mute");
                completions.add("clear");
            }

            return completions;
        }

        return null;
    }
}
