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
        switch (AdminUI.language.getOrDefault(uuid, 0)){
            case 1:
                mess = AdminGUI.getInstance().getGerm().getString(config);
                break;
            case 2:
                mess = AdminGUI.getInstance().getChin().getString(config);
                break;
            case 3:
                mess = AdminGUI.getInstance().getItal().getString(config);
                break;
            case 4:
                mess = AdminGUI.getInstance().getRuss().getString(config);
                break;
            default:
                mess = AdminGUI.getInstance().getEngl().getString(config);
                break;
        }
        if(mess != null){
            return chat(mess);
        }else{
            return chat("&cValue: &6" + config + "&c is missing in language.yml file! Please add it or delete language.yml file.");
        }
    }
}
