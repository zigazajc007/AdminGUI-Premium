package com.rabbitcompany.admingui;

import com.rabbitcompany.admingui.commands.Admin;
import com.rabbitcompany.admingui.listeners.*;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Item;
import com.rabbitcompany.admingui.utils.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;


public class AdminGUI extends JavaPlugin {

    private static AdminGUI instance;

    //VaultAPI
    private static Economy econ = null;
    public static boolean vault = false;

    //Config
    private File co = null;
    private YamlConfiguration conf = new YamlConfiguration();

    //English
    private File en = null;
    private YamlConfiguration engl = new YamlConfiguration();

    //German
    private File ge = null;
    private YamlConfiguration germ = new YamlConfiguration();

    //Chinese
    private File ch = null;
    private YamlConfiguration chin = new YamlConfiguration();

    //Russian
    private File ru = null;
    private YamlConfiguration russ = new YamlConfiguration();

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
        this.co = new File(getDataFolder(), "config.yml");
        this.en = new File(getDataFolder(), "Languages/English.yml");
        this.ge = new File(getDataFolder(), "Languages/German.yml");
        this.ch = new File(getDataFolder(), "Languages/Chinese.yml");
        this.ru = new File(getDataFolder(), "Languages/Russian.yml");
        this.k = new File(getDataFolder(), "kick.yml");
        this.p = new File(getDataFolder(), "Custom Commands/plugins.yml");
        this.c = new File(getDataFolder(), "Custom Commands/commands.yml");
        this.o = new File(getDataFolder(), "Custom Commands/commands-other.yml");

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
        new PlayerLeaveListener(this);
        new PlayerLoginListener(this);

        //Commands
        this.getCommand("admin").setExecutor((CommandExecutor) new Admin());
        this.getCommand("admin").setTabCompleter(new TabCompletion());

        //Skulls
        AdminUI.skulls.put("0qt", Item.pre_createPlayerHead("0qt"));
        AdminUI.skulls.put("Black1_TV", Item.pre_createPlayerHead("Black1_TV"));
        AdminUI.skulls.put("mattijs", Item.pre_createPlayerHead("mattijs"));
        AdminUI.skulls.put("BKing2012", Item.pre_createPlayerHead("BKing2012"));
        AdminUI.skulls.put("AverageJoe", Item.pre_createPlayerHead("AverageJoe"));
        AdminUI.skulls.put("Chaochris", Item.pre_createPlayerHead("Chaochris"));
        AdminUI.skulls.put("MHF_Redstone", Item.pre_createPlayerHead("MHF_Redstone"));
        AdminUI.skulls.put("Ground15", Item.pre_createPlayerHead("Ground15"));
        AdminUI.skulls.put("EDDxample", Item.pre_createPlayerHead("EDDxample"));
        AdminUI.skulls.put("LapisBlock", Item.pre_createPlayerHead("LapisBlock"));
        AdminUI.skulls.put("emack0714", Item.pre_createPlayerHead("emack0714"));
        AdminUI.skulls.put("Super_Sniper", Item.pre_createPlayerHead("Super_Sniper"));
        AdminUI.skulls.put("IM_", Item.pre_createPlayerHead("IM_"));
        AdminUI.skulls.put("Burger_guy", Item.pre_createPlayerHead("Burger_guy"));
        AdminUI.skulls.put("MFH_Spawner", Item.pre_createPlayerHead("MFH_Spawner"));
        AdminUI.skulls.put("MrSnowDK", Item.pre_createPlayerHead("MrSnowDK"));
        AdminUI.skulls.put("ZeeFear", Item.pre_createPlayerHead("ZeeFear"));
        AdminUI.skulls.put("Opp", Item.pre_createPlayerHead("Opp"));
        AdminUI.skulls.put("haohanklliu", Item.pre_createPlayerHead("haohanklliu"));
        AdminUI.skulls.put("raichuthink", Item.pre_createPlayerHead("raichuthink"));
        AdminUI.skulls.put("ThaBrick", Item.pre_createPlayerHead("ThaBrick"));
        AdminUI.skulls.put("Mannahara", Item.pre_createPlayerHead("Mannahara"));
        AdminUI.skulls.put("Zyne", Item.pre_createPlayerHead("Zyne"));
        AdminUI.skulls.put("3i5g00d", Item.pre_createPlayerHead("3i5g00d"));
        AdminUI.skulls.put("MHF_ArrowLeft", Item.pre_createPlayerHead("MHF_ArrowLeft"));
        AdminUI.skulls.put("MHF_Question", Item.pre_createPlayerHead("MHF_Question"));
        AdminUI.skulls.put("MHF_ArrowRight", Item.pre_createPlayerHead("MHF_ArrowRight"));
        AdminUI.skulls.put("ZiGmUnDo", Item.pre_createPlayerHead("ZiGmUnDo"));
        AdminUI.skulls.put("Push_red_button", Item.pre_createPlayerHead("Push_red_button"));
        AdminUI.skulls.put("ElMarcosFTW", Item.pre_createPlayerHead("ElMarcosFTW"));
        AdminUI.skulls.put("DavidGriffiths", Item.pre_createPlayerHead("DavidGriffiths"));

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

    private void mkdir() {

        if(!this.co.exists()){
            saveResource("config.yml", false);
        }

        if (!this.en.exists()) {
            saveResource("Languages/English.yml", false);
        }

        if (!this.ge.exists()) {
            saveResource("Languages/German.yml", false);
        }

        if (!this.ch.exists()) {
            saveResource("Languages/Chinese.yml", false);
        }

        if (!this.ru.exists()) {
            saveResource("Languages/Russian.yml", false);
        }

        if(!this.k.exists()){
            saveResource("kick.yml", false);
        }

        if(!this.p.exists()){
            saveResource("Custom Commands/plugins.yml", false);
        }

        if(!this.c.exists()){
            saveResource("Custom Commands/commands.yml", false);
        }

        if(!this.o.exists()){
            saveResource("Custom Commands/commands-other.yml", false);
        }
    }

    public void loadYamls(){

        try{
            this.conf.load(this.co);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        try {
            this.engl.load(this.en);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            this.germ.load(this.ge);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            this.chin.load(this.ch);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            this.russ.load(this.ru);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            this.kick.load(this.k);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            this.plug.load(this.p);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            this.comm.load(this.c);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            this.como.load(this.o);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getConf() { return this.conf; }

    public YamlConfiguration getEngl() { return this.engl; }

    public YamlConfiguration getGerm() { return this.germ; }

    public YamlConfiguration getChin() { return this.chin; }

    public YamlConfiguration getRuss() { return this.russ; }

    public YamlConfiguration getKick() { return this.kick; }

    public YamlConfiguration getPlug() { return this.plug; }

    public YamlConfiguration getComm() { return this.comm; }

    public YamlConfiguration getComo() { return this.como; }

    /*

    //Saving files...

    public void saveEngl() {
        try {
            this.engl.save(this.en);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGerm() {
        try {
            this.germ.save(this.ge);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveChin() {
        try {
            this.chin.save(this.ch);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveRuss() {
        try {
            this.russ.save(this.ru);
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
    } */

    private void info(String message){
        Bukkit.getConsoleSender().sendMessage(Message.chat(""));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8[]=====[" + message + " &cAdminGUI-Premium&8]=====[]"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8| &cInformation:"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Name: &bAdminGUI-Premium"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Developer: &bBlack1_TV"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Version: &b3.0.1"));
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