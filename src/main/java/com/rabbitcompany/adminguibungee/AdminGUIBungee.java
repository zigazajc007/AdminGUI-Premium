package com.rabbitcompany.adminguibungee;

import com.rabbitcompany.adminguibungee.listeners.PluginMessageListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class AdminGUIBungee extends Plugin {

    private static AdminGUIBungee instance;

    String username = "%%__USERNAME__%%";
    String user_id = "%%__USER__%%";

    @Override
    public void onEnable() {
        instance = this;
        getProxy().registerChannel("my:admingui");
        getProxy().getPluginManager().registerListener(this, new PluginMessageListener());

        info(ChatColor.GREEN + "Enabled");
    }

    @Override
    public void onDisable() {
        info(ChatColor.RED + "Disabled");
    }

    public static AdminGUIBungee getInstance(){
        return instance;
    }

    private void info(String message){
        getLogger().info("");
        getLogger().info(ChatColor.GOLD + "[]======[" + message + ChatColor.RED + " AdminGUI-Bungee" + ChatColor.GOLD + "]======[]");
        getLogger().info(ChatColor.GOLD + "|");
        getLogger().info(ChatColor.GOLD + "| " + ChatColor.RED + "Information:");
        getLogger().info(ChatColor.GOLD + "|");
        getLogger().info(ChatColor.GOLD + "|   " + ChatColor.BLUE +"Name: " + ChatColor.AQUA + "AdminGUI-Bungee");
        getLogger().info(ChatColor.GOLD + "|   " + ChatColor.BLUE + "Developer: " + ChatColor.AQUA + "Black1_TV");
        if(!username.contains("%%__")){
            getLogger().info(ChatColor.GOLD + "|   " + ChatColor.BLUE + "Plugin owner: " + ChatColor.AQUA + username);
        }else if(!user_id.contains("%%__")){
            getLogger().info(ChatColor.GOLD + "|   " + ChatColor.BLUE + "Plugin owner: " + ChatColor.AQUA + user_id);
        }else{
            getLogger().info(ChatColor.GOLD + "|   " + ChatColor.BLUE + "Plugin owner: " + ChatColor.RED + ChatColor.BOLD + "CRACKED");
        }
        getLogger().info(ChatColor.GOLD + "|   " + ChatColor.BLUE + "Version: " + ChatColor.AQUA + "1.0.2");
        getLogger().info(ChatColor.GOLD + "|");
        getLogger().info(ChatColor.GOLD + "| " + ChatColor.RED + "Support:");
        getLogger().info(ChatColor.GOLD + "|");
        getLogger().info(ChatColor.GOLD + "|   " + ChatColor.BLUE + "Discord: " + ChatColor.AQUA + "Crazy Rabbit#0001");
        getLogger().info(ChatColor.GOLD + "|   " + ChatColor.BLUE + "Mail: " + ChatColor.AQUA + "ziga.zajc007@gmail.com");
        getLogger().info(ChatColor.GOLD + "|   " + ChatColor.BLUE + "Discord: " + ChatColor.AQUA + "https://discord.gg/hUNymXX");
        getLogger().info(ChatColor.GOLD + "|");
        getLogger().info(ChatColor.GOLD + "[]=====================================[]");
        getLogger().info("");
    }

}
