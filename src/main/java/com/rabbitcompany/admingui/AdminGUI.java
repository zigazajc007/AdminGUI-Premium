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

    //Kick
    private File k = null;
    private YamlConfiguration kick = new YamlConfiguration();

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
        this.k = new File(getDataFolder(), "kick.yml");
        this.p = new File(getDataFolder(), "plugins.yml");
        this.c = new File(getDataFolder(), "commands.yml");
        this.o = new File(getDataFolder(), "commands-other.yml");

        mkdir();
        loadYamls();

        info("&aEnabling");

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
        info("&4Disabling");
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

        if(!this.k.exists()){
            saveResource("kick.yml", false);
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
            this.kick.load(this.k);
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

    public YamlConfiguration getKick() { return this.kick; }

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

    public void saveKick(){
        try {
            this.kick.save(this.k);
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

    private void info(String message){
        Bukkit.getConsoleSender().sendMessage(Message.chat(""));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8[]=====[" + message + " &cAdminGUI-Premium&8]=====[]"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8| &cInformation:"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Name: &bAdminGUI-Premium"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Developer: &bBlack1_TV"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Version: &b2.0.5"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8| &cSupport:"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Discord: &bCrazy Rabbit#0001"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Mail: &bziga.zajc007@gmail.com"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Discord: &bhttps://discord.gg/hUNymXX"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8[]=====================================[]"));
        Bukkit.getConsoleSender().sendMessage(Message.chat(""));
    }

    public static AdminGUI getInstance(){
        return instance;
    }
}