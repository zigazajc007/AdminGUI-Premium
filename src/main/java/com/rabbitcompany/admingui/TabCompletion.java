package com.rabbitcompany.admingui;

import com.rabbitcompany.admingui.utils.Channel;
import com.rabbitcompany.admingui.utils.Language;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.admingui.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if(command.getName().equalsIgnoreCase("admin")){
            List<String> completions = new ArrayList<>();

           if(args.length == 1){

                if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)){
                    Channel.send(sender.getName(),"send", "online_players");
                    completions.addAll(Settings.online_players);
                }else{
                    for(Player all : Bukkit.getServer().getOnlinePlayers()) {
                        completions.add(all.getName());
                    }
                }

                if(sender.hasPermission("admingui.reload")) completions.add("reload");

                if(sender.hasPermission("admingui.rank") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) completions.add("rank");

                if(sender.hasPermission("admingui.check")) completions.add("check");

                if(sender.hasPermission("admingui.maintenance.manage")) completions.add("maintenance");

                if(!(sender instanceof Player)) completions.add("language");

                completions.add("tools");

                completions.add("initialize");

           }else if(args.length == 2){
               if(args[0].equals("initialize")){
                   completions.add("gui");
                   completions.add("players");
               }else if(args[0].equals("rank") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)){
                   if(sender.hasPermission("admingui.rank.set")) completions.add("set");
                   if(sender.hasPermission("admingui.rank.up")) completions.add("up");
                   if(sender.hasPermission("admingui.rank.down")) completions.add("down");
               }else if(args[0].equals("language")){
                   if(!(sender instanceof Player)) completions.add("fix");
                   if(!(sender instanceof Player)) completions.add("download");
               }else if(args[0].equals("maintenance")){
                   if(sender.hasPermission("admingui.maintenance.manage")) completions.add("on");
                   if(sender.hasPermission("admingui.maintenance.manage")) completions.add("off");
               }else if(args[0].equals("check") && sender.hasPermission("admingui.check")){
                   Set<String> con_sec = AdminGUI.getInstance().getPlayers().getConfigurationSection("").getKeys(false);
                   for (String uuid_name : con_sec){
                       completions.add(AdminGUI.getInstance().getPlayers().getString(uuid_name+".name"));
                   }
               }
           }else if(args.length == 3){
               if(args[0].equals("rank") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false) && (args[1].equals("set") || args[1].equals("up") || args[1].equals("down"))) {
                   if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)){
                       Channel.send(sender.getName(),"send", "online_players");
                       completions.addAll(Settings.online_players);
                   }else{
                       for(Player all : Bukkit.getServer().getOnlinePlayers()) completions.add(all.getName());
                   }
               }else if(args[0].equals("language") && args[1].equals("download") && !(sender instanceof Player)){
                   completions.addAll(Language.default_languages);
               }else if(args[0].equals("language") && args[1].equals("fix") && !(sender instanceof Player)){
                   completions.addAll(Language.enabled_languages);
               }
           }else if(args.length == 4){
               if(args[0].equals("rank") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false) && args[1].equals("set")) {
                   for (Map.Entry<String, Object> rank : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
                       completions.add(rank.getKey());
                   }
               }
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
