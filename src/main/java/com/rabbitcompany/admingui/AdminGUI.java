package com.rabbitcompany.admingui;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.rabbitcompany.admingui.commands.*;
import com.rabbitcompany.admingui.listeners.*;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.*;
import com.zaxxer.hikari.HikariDataSource;
import github.scarsz.discordsrv.DiscordSRV;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class AdminGUI extends JavaPlugin implements PluginMessageListener {

    private static AdminGUI instance;

    String username = "%%__USERNAME__%%";
    String user_id = "%%__USER__%%";

    //Database
    public static HikariDataSource hikari;
    private static Connection conn = null;

    public static int gui_type = 0;

    //Update Checker
    public static String new_version = null;

    //VaultAPI
    private static Economy econ = null;
    public static boolean vault = false;
    private static Permission perms = null;
    private static Chat chat = null;

    //DiscordSRV
    private DiscordSRVListener discordsrvListener;

    //Config
    private File co = null;
    private final YamlConfiguration conf = new YamlConfiguration();

    //Permissions
    private File pe = null;
    private final YamlConfiguration perm = new YamlConfiguration();

    //Players
    private File pl = null;
    private final YamlConfiguration play = new YamlConfiguration();

    //Kick
    private File k = null;
    private final YamlConfiguration kick = new YamlConfiguration();

    //Plugins
    private File p = null;
    private final YamlConfiguration plug = new YamlConfiguration();

    //Commands
    private File c = null;
    private final YamlConfiguration comm = new YamlConfiguration();

    //Commands other
    private File o = null;
    private final YamlConfiguration como = new YamlConfiguration();

    @Override
    public void onEnable() {
        instance = this;
        this.co = new File(getDataFolder(), "config.yml");
        this.pe = new File(getDataFolder(), "permissions.yml");
        this.pl = new File(getDataFolder(), "players.yml");
        this.k = new File(getDataFolder(), "kick.yml");
        this.p = new File(getDataFolder(), "Custom Commands/plugins.yml");
        this.c = new File(getDataFolder(), "Custom Commands/commands.yml");
        this.o = new File(getDataFolder(), "Custom Commands/commands-other.yml");

        mkdir();
        loadYamls();

        Language.downloadLanguage(getConf().getString("default_language", "English"));

        //Database connection
        if(getConf().getBoolean("mysql", false)) {
            setupMySQL();
        }

        //bStats
        if(!Bukkit.getVersion().contains("1.8")){
            Metrics metrics = new Metrics(this, 5815);
            metrics.addCustomChart(new Metrics.SimplePie("default_language", () -> getConf().getString("default_language", "English")));
            metrics.addCustomChart(new Metrics.SimplePie("admin_tools", () -> getConf().getString("admin_tools_enabled", "true")));
            metrics.addCustomChart(new Metrics.SimplePie("admin_permissions", () -> getConf().getString("ap_enabled", "false")));
            metrics.addCustomChart(new Metrics.SimplePie("admin_chat", () -> getConf().getString("ac_enabled", "false")));
            metrics.addCustomChart(new Metrics.SimplePie("admin_tablist", () -> getConf().getString("atl_enabled", "false")));
            metrics.addCustomChart(new Metrics.SimplePie("admin_command_spy", () -> getConf().getString("acs_enabled", "false")));
            metrics.addCustomChart(new Metrics.SimplePie("bungeecord", () -> getConf().getString("bungeecord_enabled", "false")));
            metrics.addCustomChart(new Metrics.SimplePie("mysql", () -> getConf().getString("mysql", "false")));
        }

        //Update Checker
        if(getConf().getBoolean("uc_enabled", true)){
            new UpdateChecker(this, 49).getVersion(updater_version -> {
                if (!getDescription().getVersion().equalsIgnoreCase(updater_version)) new_version = updater_version;
                info("&aEnabling");
            });
        }else{
            info("&aEnabling");
        }

        //DiscordSRV
        if(getServer().getPluginManager().getPlugin("DiscordSRV") != null){
            discordsrvListener = new DiscordSRVListener(this);
            DiscordSRV.api.subscribe(discordsrvListener);
        }

        //VaultAPI
        if (getServer().getPluginManager().getPlugin("Vault") != null){
            if(setupEconomy() && setupChat() && setupPermissions()) vault = true;
        }

        gui_type = getConf().getInt("gui_type", 0);

        //TODO: Bungee
        if(getConf().getBoolean("bungeecord_enabled", false)){
            getServer().getMessenger().registerOutgoingPluginChannel(this, "my:admingui");
            getServer().getMessenger().registerIncomingPluginChannel(this, "my:admingui", this);
        }

        //Listeners
        new InventoryClickListener(this);
        if(Bukkit.getVersion().contains("1.8")) {
            new PlayerDamageListener(this);
        }
        new PlayerJoinListener(this);
        new PlayerLeaveListener(this);
        new PlayerLoginListener(this);

        if(getConf().getBoolean("admin_tools_enabled", true)){
            new PlayerInteractListener(this);
            new PlayerEntityInteractListener(this);
        }

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PlayerPlaceholderMessageListener(this);
            new PlayerPlaceholderCommandListener(this);
            new AdminGUIPlaceholders().register();
        }else{
            new PlayerMessageListener(this);
            new PlayerCommandListener(this);
        }

        //Freeze
        new PlayerMoveListener(this);
        new PlayerBlockBreakListener(this);
        new PlayerBlockPlaceListener(this);
        new PlayersDroppingItemsListener(this);

        //Commands
        this.getCommand("admin").setExecutor(new Admin());
        this.getCommand("admin").setTabCompleter(new TabCompletion());
        this.getCommand("adminchat").setExecutor(new AdminChat());
        this.getCommand("adminchat").setTabCompleter(new TabCompletion());
        this.getCommand("admincommandspy").setExecutor(new CommandSpy());

        //Skulls
        switch (gui_type){
            case 1:
                AdminUI.skulls.put("0qt", Item.pre_createPlayerHead("0qt"));
                AdminUI.skulls.put("Black1_TV", Item.pre_createPlayerHead("Black1_TV"));
                AdminUI.skulls.put("mattijs", Item.pre_createPlayerHead("mattijs"));
                AdminUI.skulls.put("BKing2012", Item.pre_createPlayerHead("BKing2012"));
                AdminUI.skulls.put("AverageJoe", Item.pre_createPlayerHead("AverageJoe"));
                AdminUI.skulls.put("LobbyPlugin", Item.pre_createPlayerHead("LobbyPlugin"));
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
                break;
            default:
                AdminUI.skulls.put("Black1_TV", Item.pre_createPlayerHead("Black1_TV"));
                break;
        }

    }

    @Override
    public void onDisable() {

        info("&4Disabling");

        //DiscordSRV
        if(getServer().getPluginManager().getPlugin("DiscordSRV") != null){
            DiscordSRV.api.unsubscribe(discordsrvListener);
        }

        if(conn != null){
            try {
                conn.close();
            } catch (SQLException ignored) { }
        }
    }

    //MySql
    private void setupMySQL(){
        try {
            hikari = new HikariDataSource();
            hikari.setMaximumPoolSize(10);
            hikari.setJdbcUrl("jdbc:mysql://" + getConf().getString("mysql_host") + ":" + getConf().getString("mysql_port") + "/" + getConf().getString("mysql_database"));
            hikari.setUsername(getConf().getString("mysql_user"));
            hikari.setPassword(getConf().getString("mysql_password"));
            hikari.addDataSourceProperty("useSSL", getConf().getString("mysql_useSSL"));
            hikari.addDataSourceProperty("cachePrepStmts", "true");
            hikari.addDataSourceProperty("prepStmtCacheSize", "250");
            hikari.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            conn = hikari.getConnection();
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS admingui_ranks(uuid CHAR(36) NOT NULL PRIMARY KEY, username varchar(25) NOT NULL, rank varchar(25) NOT NULL, created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            conn.close();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }
    }

    //VaultAPI
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return true;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if(rsp == null) return false;
        chat = rsp.getProvider();
        return true;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if(rsp == null) return false;
        perms = rsp.getProvider();
        return true;
    }

    public static Economy getEconomy(){ return econ; }

    public static Permission getVaultPermissions() { return perms; }

    public static Chat getVaultChat() { return chat; }

    public void mkdir() {

        if(!this.co.exists()){
            saveResource("config.yml", false);
        }

        if(!this.pe.exists()){
            saveResource("permissions.yml", false);
        }

        if(!this.pl.exists()){
            saveResource("players.yml", false);
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
            this.perm.load(this.pe);
            this.play.load(this.pl);
            this.kick.load(this.k);
            this.plug.load(this.p);
            this.comm.load(this.c);
            this.como.load(this.o);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getConf() { return this.conf; }

    public YamlConfiguration getPermissions() { return this.perm; }

    public YamlConfiguration getPlayers(){ return this.play; }

    public YamlConfiguration getKick() { return this.kick; }

    public YamlConfiguration getPlug() { return this.plug; }

    public YamlConfiguration getComm() { return this.comm; }

    public YamlConfiguration getComo() { return this.como; }

    public void savePermissions() {
        try {
            this.perm.save(this.pe);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePlayers(){
        try {
            this.play.save(this.pl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void info(String message){
        Bukkit.getConsoleSender().sendMessage(Message.chat(""));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6[]=====[" + message + " &cAdminGUI-Premium&6]=====[]"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6| &cInformation:"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Name: &bAdminGUI-Premium"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Developer: &bBlack1_TV"));
        if(!username.contains("%%__")){
            Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Plugin owner: &b" + username));
        }else if(!user_id.contains("%%__")){
            Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Plugin owner: &b" + user_id));
        }else{
            Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Plugin owner: &4&lCRACKED"));
        }
        if(new_version != null){
            Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Version: &b" + getDescription().getVersion() + " (&6update available&b)"));
        }else{
            Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Version: &b" + getDescription().getVersion()));
        }
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6| &cLanguages:"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
        for (String language: Language.getLanguages()) {
            if(getConf().getString("default_language", "English").equals(language)){
                Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &b- " + language + " (default)"));
            }else{
                Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &b- " + language));
            }
        }
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6| &cSupport:"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Discord: &bCrazy Rabbit#0001"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Mail: &bziga.zajc007@gmail.com"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Discord: &bhttps://discord.gg/hUNymXX"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6[]=====================================[]"));
        Bukkit.getConsoleSender().sendMessage(Message.chat(""));
    }

    public static AdminGUI getInstance(){
        return instance;
    }

    //TODO: Bungee
    @Override
    public void onPluginMessageReceived(String channel, Player pla, byte[] message) {

        if (!channel.equalsIgnoreCase("my:admingui")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String sender = in.readUTF();
        String subchannel = in.readUTF();

        switch (subchannel){
            case "chat":
                if(getConf().getBoolean("bungeecord_enabled", false) && getConf().getBoolean("bungeecord_admin_chat", false)){
                    Player player = Bukkit.getServer().getPlayer(UUID.fromString(sender));
                    if(player == null || !player.isOnline())
                        Bukkit.getServer().broadcastMessage(in.readUTF());
                }
                break;
            case "online_players":
                String online_players = in.readUTF();
                String[] op = online_players.split(";");
                AdminUI.online_players.clear();
                for (String on: op) {
                    AdminUI.online_players.add(on);
                    AdminUI.skulls_players.put(on, Item.pre_createPlayerHead(on));
                }
                break;
            case "rank":
                String target_uuid = in.readUTF();
                String name = in.readUTF();
                String rank = in.readUTF();
                if(target_uuid.equals("null")){
                    Permissions.saveRank(null, name, rank);
                }else{
                    Permissions.saveRank(UUID.fromString(target_uuid), name, rank);
                }
                break;
            case "gamemode":
                String player = in.readUTF();
                String gamemode = in.readLine();

                Player target = Bukkit.getServer().getPlayer(player);

                if(target != null){
                    if(target.isOnline()){
                        switch (gamemode){
                            case "spectator":
                                target.setGameMode(GameMode.SPECTATOR);
                                break;
                            case "creative":
                                target.setGameMode(GameMode.CREATIVE);
                                break;
                            case "adventure":
                                target.setGameMode(GameMode.ADVENTURE);
                                break;
                            default:
                                target.setGameMode(GameMode.SURVIVAL);
                                break;
                        }
                    }
                }
                break;
        }

    }
}