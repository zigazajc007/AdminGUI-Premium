package com.rabbitcompany.admingui;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.rabbitcompany.admingui.commands.Admin;
import com.rabbitcompany.admingui.commands.AdminChat;
import com.rabbitcompany.admingui.commands.CommandSpy;
import com.rabbitcompany.admingui.listeners.*;
import com.rabbitcompany.admingui.utils.*;
import com.rabbitcompany.admingui.utils.vault.VaultChatConnector;
import com.rabbitcompany.admingui.utils.vault.VaultPermissionConnector;
import com.zaxxer.hikari.HikariDataSource;
import github.scarsz.discordsrv.DiscordSRV;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class AdminGUI extends JavaPlugin implements PluginMessageListener {

	//Database
	public static HikariDataSource hikari;
	public static int gui_type = 0;
	//Update Checker
	public static String new_version = null;
	private static AdminGUI instance;
	private static Connection conn = null;
	//VaultAPI
	private static Economy econ = null;
	private static Permission perms = null;
	private static Chat chat = null;
	private final YamlConfiguration conf = new YamlConfiguration();
	private final YamlConfiguration sett = new YamlConfiguration();
	private final YamlConfiguration perm = new YamlConfiguration();
	private final YamlConfiguration play = new YamlConfiguration();
	private final YamlConfiguration kick = new YamlConfiguration();
	private final YamlConfiguration plug = new YamlConfiguration();
	private final YamlConfiguration comm = new YamlConfiguration();
	private final YamlConfiguration como = new YamlConfiguration();
	String username = "%%__USERNAME__%%";
	String user_id = "%%__USER__%%";
	//DiscordSRV
	private DiscordSRVListener discordsrvListener;
	//Config
	private File co = null;
	//Settings
	private File se = null;
	//Permissions
	private File pe = null;
	//Players
	private File pl = null;
	//Kick
	private File k = null;
	//Plugins
	private File p = null;
	//Commands
	private File c = null;
	//Commands other
	private File o = null;

	public static Economy getEconomy() {
		return econ;
	}

	public static Permission getVaultPermissions() {
		return perms;
	}

	public static Chat getVaultChat() {
		return chat;
	}

	public static AdminGUI getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		this.co = new File(getDataFolder(), "config.yml");
		this.se = new File(getDataFolder(), "settings.yml");
		this.pe = new File(getDataFolder(), "permissions.yml");
		this.pl = new File(getDataFolder(), "players.yml");
		this.k = new File(getDataFolder(), "kick.yml");
		this.p = new File(getDataFolder(), "Custom Commands/plugins.yml");
		this.c = new File(getDataFolder(), "Custom Commands/commands.yml");
		this.o = new File(getDataFolder(), "Custom Commands/commands-other.yml");

		mkdir();
		loadYamls();

		Language.downloadLanguage(getConf().getString("default_language", "English"));

		if (getSett().getBoolean("maintenance", false)) Settings.maintenance_mode = true;

		//Database connection
		if (getConf().getBoolean("mysql", false)) setupMySQL();

		//bStats
		Metrics metrics = new Metrics(this, 5815);
		metrics.addCustomChart(new Metrics.SimplePie("default_language", () -> getConf().getString("default_language", "English")));
		metrics.addCustomChart(new Metrics.SimplePie("admin_tools", () -> getConf().getString("admin_tools_enabled", "true")));
		metrics.addCustomChart(new Metrics.SimplePie("admin_random_teleport", () -> getConf().getString("rtp_enabled", "false")));
		metrics.addCustomChart(new Metrics.SimplePie("admin_permissions", () -> getConf().getString("ap_enabled", "false")));
		metrics.addCustomChart(new Metrics.SimplePie("admin_chat", () -> getConf().getString("ac_enabled", "false")));
		metrics.addCustomChart(new Metrics.SimplePie("admin_tablist", () -> getConf().getString("atl_enabled", "false")));
		metrics.addCustomChart(new Metrics.SimplePie("admin_command_spy", () -> getConf().getString("acs_enabled", "false")));
		metrics.addCustomChart(new Metrics.SimplePie("bungeecord", () -> getConf().getString("bungeecord_enabled", "false")));
		metrics.addCustomChart(new Metrics.SimplePie("mysql", () -> getConf().getString("mysql", "false")));

		//Update Checker
		if (getConf().getBoolean("uc_enabled", true)) {
			new UpdateChecker(this, 49).getVersion(updater_version -> {
				if (!getDescription().getVersion().equalsIgnoreCase(updater_version)) new_version = updater_version;
				info("&aEnabling");
			});
		} else {
			info("&aEnabling");
		}

		//DiscordSRV
		if (getServer().getPluginManager().getPlugin("DiscordSRV") != null) {
			discordsrvListener = new DiscordSRVListener(this);
			DiscordSRV.api.subscribe(discordsrvListener);
		}

		//VaultAPI
		if (getServer().getPluginManager().getPlugin("Vault") != null) {
			setupEconomy();
			setupPermissions();
			if (getConf().getBoolean("ap_enabled", false)) {
				final ServicesManager sm = getServer().getServicesManager();
				sm.register(Permission.class, new VaultPermissionConnector(), this, ServicePriority.Highest);
				setupPermissions();
				sm.register(Chat.class, new VaultChatConnector(perms), this, ServicePriority.Highest);
			}
			setupChat();
		}

		gui_type = getConf().getInt("gui_type", 0);

		//TODO: Bungee
		if (getConf().getBoolean("bungeecord_enabled", false)) {
			getServer().getMessenger().registerOutgoingPluginChannel(this, "my:admingui");
			getServer().getMessenger().registerIncomingPluginChannel(this, "my:admingui", this);
		}

		//Listeners
		new InventoryClickListener(this);
		if (Bukkit.getVersion().contains("1.8")) new PlayerDamageListener(this);

		new PlayerJoinListener(this);
		new PlayerLeaveListener(this);
		new PlayerLoginListener(this);

		if (getConf().getBoolean("admin_tools_enabled", true)) {
			new PlayerInteractListener(this);
			new PlayerEntityInteractListener(this);
		}

		if (getConf().getBoolean("ms_enabled", false)) new MultiplayerSleepListener(this);

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PlayerPlaceholderMessageListener(this);
			new PlayerPlaceholderCommandListener(this);
			new AdminGUIPlaceholders().register();
		} else {
			new PlayerMessageListener(this);
			new PlayerCommandListener(this);
		}

		if (getConf().getBoolean("motd_changer_enabled", false)) new ServerListPingListener(this);

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
		if (gui_type == 1) {
			Settings.skulls.put("0qt", Item.pre_createPlayerHead("0qt"));
			Settings.skulls.put("Black1_TV", Item.pre_createPlayerHead("Black1_TV"));
			Settings.skulls.put("mattijs", Item.pre_createPlayerHead("mattijs"));
			Settings.skulls.put("BKing2012", Item.pre_createPlayerHead("BKing2012"));
			Settings.skulls.put("AverageJoe", Item.pre_createPlayerHead("AverageJoe"));
			Settings.skulls.put("LobbyPlugin", Item.pre_createPlayerHead("LobbyPlugin"));
			Settings.skulls.put("MHF_Redstone", Item.pre_createPlayerHead("MHF_Redstone"));
			Settings.skulls.put("Ground15", Item.pre_createPlayerHead("Ground15"));
			Settings.skulls.put("EDDxample", Item.pre_createPlayerHead("EDDxample"));
			Settings.skulls.put("LapisBlock", Item.pre_createPlayerHead("LapisBlock"));
			Settings.skulls.put("emack0714", Item.pre_createPlayerHead("emack0714"));
			Settings.skulls.put("Super_Sniper", Item.pre_createPlayerHead("Super_Sniper"));
			Settings.skulls.put("IM_", Item.pre_createPlayerHead("IM_"));
			Settings.skulls.put("Burger_guy", Item.pre_createPlayerHead("Burger_guy"));
			Settings.skulls.put("MFH_Spawner", Item.pre_createPlayerHead("MFH_Spawner"));
			Settings.skulls.put("MrSnowDK", Item.pre_createPlayerHead("MrSnowDK"));
			Settings.skulls.put("ZeeFear", Item.pre_createPlayerHead("ZeeFear"));
			Settings.skulls.put("Opp", Item.pre_createPlayerHead("Opp"));
			Settings.skulls.put("haohanklliu", Item.pre_createPlayerHead("haohanklliu"));
			Settings.skulls.put("raichuthink", Item.pre_createPlayerHead("raichuthink"));
			Settings.skulls.put("ThaBrick", Item.pre_createPlayerHead("ThaBrick"));
			Settings.skulls.put("Mannahara", Item.pre_createPlayerHead("Mannahara"));
			Settings.skulls.put("Zyne", Item.pre_createPlayerHead("Zyne"));
			Settings.skulls.put("3i5g00d", Item.pre_createPlayerHead("3i5g00d"));
			Settings.skulls.put("MHF_ArrowLeft", Item.pre_createPlayerHead("MHF_ArrowLeft"));
			Settings.skulls.put("MHF_Question", Item.pre_createPlayerHead("MHF_Question"));
			Settings.skulls.put("MHF_ArrowRight", Item.pre_createPlayerHead("MHF_ArrowRight"));
			Settings.skulls.put("ZiGmUnDo", Item.pre_createPlayerHead("ZiGmUnDo"));
			Settings.skulls.put("Push_red_button", Item.pre_createPlayerHead("Push_red_button"));
			Settings.skulls.put("ElMarcosFTW", Item.pre_createPlayerHead("ElMarcosFTW"));
			Settings.skulls.put("DavidGriffiths", Item.pre_createPlayerHead("DavidGriffiths"));
		} else {
			Settings.skulls.put("Black1_TV", Item.pre_createPlayerHead("Black1_TV"));
		}

	}

	@Override
	public void onDisable() {

		info("&4Disabling");

		//DiscordSRV
		if (getServer().getPluginManager().getPlugin("DiscordSRV") != null) DiscordSRV.api.unsubscribe(discordsrvListener);

		//Admin Permissions
		if (getConf().getBoolean("ap_enabled", false)) {
			for (Player player : Bukkit.getOnlinePlayers()) TargetPlayer.removePermissions(player);
			Settings.permissions.clear();
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException ignored) {
			}
		}
	}

	//MySql
	private void setupMySQL() {
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
		if (rsp == null) return false;
		chat = rsp.getProvider();
		return true;
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		if (rsp == null) return false;
		perms = rsp.getProvider();
		return true;
	}

	public void mkdir() {
		if (!this.co.exists()) saveResource("config.yml", false);
		if (!this.se.exists()) saveResource("settings.yml", false);
		if (!this.pe.exists()) saveResource("permissions.yml", false);
		if (!this.pl.exists()) saveResource("players.yml", false);
		if (!this.k.exists()) saveResource("kick.yml", false);
		if (!this.p.exists()) saveResource("Custom Commands/plugins.yml", false);
		if (!this.c.exists()) saveResource("Custom Commands/commands.yml", false);
		if (!this.o.exists()) saveResource("Custom Commands/commands-other.yml", false);
	}

	public void loadYamls() {
		try {
			this.conf.load(this.co);
			this.sett.load(this.se);
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

	public YamlConfiguration getConf() {
		return this.conf;
	}

	public YamlConfiguration getSett() {
		return this.sett;
	}

	public YamlConfiguration getPermissions() {
		return this.perm;
	}

	public YamlConfiguration getPlayers() {
		return this.play;
	}

	public YamlConfiguration getKick() {
		return this.kick;
	}

	public YamlConfiguration getPlug() {
		return this.plug;
	}

	public YamlConfiguration getComm() {
		return this.comm;
	}

	public YamlConfiguration getComo() {
		return this.como;
	}

	public void savePlayers() {
		try {
			this.play.save(this.pl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveSettings() {
		try {
			this.sett.save(this.se);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void info(String message) {
		Bukkit.getConsoleSender().sendMessage(Message.chat(""));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6[]=====[" + message + " &cAdminGUI-Premium&6]=====[]"));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6| &cInformation:"));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Name: &bAdminGUI-Premium"));
		if (new_version != null) {
			Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Version: &b" + getDescription().getVersion() + " (&6Update available&b)"));
		} else {
			Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Version: &b" + getDescription().getVersion()));
		}
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Developer: &bBlack1_TV"));
		if (!username.contains("%%__")) {
			Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Plugin owner: &b" + username));
			Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9License key: &b" + new Hash().createLicenseKey(username)));
		} else if (!user_id.contains("%%__")) {
			Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Plugin owner: &b" + user_id));
			Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9License key: &b" + new Hash().createLicenseKey(user_id)));
		} else {
			Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Plugin owner: &4&lCRACKED"));
			Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9License key: &400000-00000-00000-00000"));
		}
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6| &cLanguages:"));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
		for (String language : Language.getLanguages()) {
			if (getConf().getString("default_language", "English").equals(language)) {
				Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &b- " + language + " (default)"));
			} else {
				Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &b- " + language));
			}
		}
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6| &cSupport:"));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Website: &6https://rabbit-company.com"));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Discord: &bziga.zajc007"));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Mail: &bziga.zajc007@gmail.com"));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Discord: &bhttps://discord.gg/hUNymXX"));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
		Bukkit.getConsoleSender().sendMessage(Message.chat("&6[]=====================================[]"));
		Bukkit.getConsoleSender().sendMessage(Message.chat(""));
	}

	//TODO: Bungee
	@Override
	public void onPluginMessageReceived(String channel, Player pla, byte[] message) {

		if (!channel.equalsIgnoreCase("my:admingui")) return;

		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String sender = in.readUTF();
		String subchannel = in.readUTF();

		switch (subchannel) {
			case "chat":
				if (getConf().getBoolean("bungeecord_enabled", false) && getConf().getBoolean("bungeecord_admin_chat", false)) {
					Player player = Bukkit.getServer().getPlayer(UUID.fromString(sender));
					if (player == null || !player.isOnline())
						Bukkit.getServer().broadcastMessage(in.readUTF());
				}
				break;
			case "custom_chat_channels":
				String ccChannel = in.readUTF();
				String ccServerName = in.readUTF();
				String ccPlayerName = in.readUTF();
				String ccMessage = in.readUTF();

				if(!getConf().getBoolean("bungeecord_enabled", false) || !getConf().getBoolean("bungeecord_custom_chat_channels", false)) break;
				if(!getConf().contains("ccc." + ccChannel)) break;
				Player ccPlayer = Bukkit.getServer().getPlayer(UUID.fromString(sender));
				if (ccPlayer != null && ccPlayer.isOnline()) break;
				String bbFormat = getConf().getString("bungeecord_custom_chat_channels_format", "&7[{server_name}] {format}");
				String ccFormat = getConf().getString("ccc." + ccChannel + ".format", "&2[&cStaff Chat&2] &5{name} &f> {message}");
				String ccPermission = getConf().getString("ccc." + ccChannel + ".permission");

				ccFormat = bbFormat.replace("{server_name}", ccServerName).replace("{format}", ccFormat);

				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					if (TargetPlayer.hasPermission(player, ccPermission)) {
						player.sendMessage(Message.chat(ccFormat.replace("{name}", ccPlayerName).replace("{display_name}", ccPlayerName).replace("{server_name}", ccServerName).replace("{message}", ccMessage)));
					}
				}
				Bukkit.getConsoleSender().sendMessage(Message.chat(ccFormat.replace("{name}", ccPlayerName).replace("{display_name}", ccPlayerName).replace("{server_name}", ccServerName).replace("{message}", ccMessage)));
				break;
			case "online_players":
				String online_players = in.readUTF();
				String[] op = online_players.split(";");
				Settings.online_players.clear();
				for (String on : op) {
					Settings.online_players.add(on);
					Settings.skulls_players.put(on, Item.pre_createPlayerHead(on));
				}
				break;
			case "rank":
				String target_uuid = in.readUTF();
				String name = in.readUTF();
				String rank = in.readUTF();
				if (target_uuid.equals("null")) {
					Permissions.saveRank(null, name, rank);
				} else {
					Permissions.saveRank(UUID.fromString(target_uuid), name, rank);
				}
				break;
			case "gamemode":
				String player = in.readUTF();
				String gamemode = in.readLine();

				Player target = Bukkit.getServer().getPlayer(player);

				if (target != null) {
					if (target.isOnline()) {
						switch (gamemode) {
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