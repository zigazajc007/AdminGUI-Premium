package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;

public class Message {

    public static String chat(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getMessage(UUID uuid, String config){
        String lang = Settings.language.getOrDefault(uuid, AdminGUI.getInstance().getConf().getString("default_language"));
        String mess = Language.getMessages(uuid, config);

        if(mess != null){
            if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
                mess = PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(uuid), mess);
            }
            return chat(mess);
        }else{
            return chat("&cValue: &6" + config + "&c is missing in " + lang + ".yml file! Please add it or delete " + lang + ".yml file.");
        }
    }
}
