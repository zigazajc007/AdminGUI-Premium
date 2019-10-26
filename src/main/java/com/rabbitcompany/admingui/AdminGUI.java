package com.rabbitcompany.admingui;

import com.rabbitcompany.admingui.commands.Admin;
import com.rabbitcompany.admingui.listeners.InventoryClickListener;
import com.rabbitcompany.admingui.listeners.PlayerDamageListener;
import com.rabbitcompany.admingui.listeners.PlayerJoinListener;
import com.rabbitcompany.admingui.listeners.PlayerLoginListener;
import com.rabbitcompany.admingui.utils.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class AdminGUI extends JavaPlugin {

    private static AdminGUI instance;

    //VaultAPI
    private static Economy econ = null;
    public static boolean vault = false;

    //Language
    private File l = null;
    private YamlConfiguration lang = new YamlConfiguration();

    //Plugins
    private File p = null;
    private YamlConfiguration plug = new YamlConfiguration();

    //Commands
    private File c = null;
    private YamlConfiguration comm = new YamlConfiguration();

    //Commands other
    private File o = null;
    private YamlConfiguration como = new YamlConfiguration();

    @Override
    public void onEnable() {
        instance = this;
        this.l = new File(getDataFolder(), "language.yml");
        this.p = new File(getDataFolder(), "plugins.yml");
        this.c = new File(getDataFolder(), "commands.yml");
        this.o = new File(getDataFolder(), "commands-other.yml");

        mkdir();
        loadYamls();

        Bukkit.getConsoleSender().sendMessage(Message.chat("&7[&cAdmin GUI&7] &aPlugin is enabled!"));

        //bStats
        if(!Bukkit.getVersion().contains("1.8")){
            MetricsLite metricsLite = new MetricsLite(this);
        }

        //VaultAPI
        if(setupEconomy()){
            vault = true;
        }

        //Listeners
        new InventoryClickListener(this);
        new PlayerDamageListener(this);
        new PlayerJoinListener(this);
        new PlayerLoginListener(this);

        //Commands
        this.getCommand("admin").setExecutor((CommandExecutor) new Admin());
        this.getCommand("admin").setTabCompleter(new TabCompletion());
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Message.chat("&7[&cAdmin GUI&7] &4Plugin is disabled!"));
    }

    //VaultAPI
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    public static Economy getEconomy() {
        return econ;
    }

    private void mkdir(){
        if (!this.l.exists()) {
            saveResource("language.yml", false);
        }

        if(!this.p.exists()){
            saveResource("plugins.yml", false);
        }

        if(!this.c.exists()){
            saveResource("commands.yml", false);
        }

        if(!this.o.exists()){
            saveResource("commands-other.yml", false);
        }
    }

    public void loadYamls(){
        try {
            this.lang.load(this.l);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            this.plug.load(this.p);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            this.comm.load(this.c);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            this.como.load(this.o);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getLang() { return this.lang; }

    public YamlConfiguration getPlug() { return this.plug; }

    public YamlConfiguration getComm() { return this.comm; }

    public YamlConfiguration getComo() { return this.como; }

    public void saveLang() {
        try {
            this.lang.save(this.l);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePlug(){
        try {
            this.plug.save(this.p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveComm(){
        try {
            this.comm.save(this.c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveComo(){
        try {
            this.como.save(this.o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AdminGUI getInstance(){
        return instance;
    }
}