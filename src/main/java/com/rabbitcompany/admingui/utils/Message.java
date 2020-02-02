package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import org.bukkit.ChatColor;

import java.util.UUID;

public class Message {

    public static String chat(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getMessage(UUID uuid, String config){
        String mess;
        String lang = AdminUI.language.getOrDefault(uuid, AdminGUI.getInstance().getConf().getString("default_language"));
        switch (lang){
            case "Germany":
                mess = AdminGUI.getInstance().getGerm().getString(config);
                break;
            case "Chinese":
                mess = AdminGUI.getInstance().getChin().getString(config);
                break;
            case "Italian":
                mess = AdminGUI.getInstance().getItal().getString(config);
                break;
            case "Russian":
                mess = AdminGUI.getInstance().getRuss().getString(config);
                break;
            case "Bulgarian":
                mess = AdminGUI.getInstance().getBulg().getString(config);
                break;
            case "Spanish":
                mess = AdminGUI.getInstance().getSpan().getString(config);
                break;
            default:
                mess = AdminGUI.getInstance().getEngl().getString(config);
                break;
        }
        if(mess != null){
            return chat(mess);
        }else{
            return chat("&cValue: &6" + config + "&c is missing in " + lang + ".yml file! Please add it or delete " + lang + ".yml file.");
        }
    }
}
