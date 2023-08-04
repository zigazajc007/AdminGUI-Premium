package com.rabbitcompany.adminguibungee;

import com.rabbitcompany.admingui.utils.Hash;
import com.rabbitcompany.admingui.utils.Language;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.adminguibungee.listeners.PluginMessageListener;
import com.rabbitcompany.adminguibungee.listeners.ServerKickListener;
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

	public static Configuration config = null;
	public static Configuration bungee_config = null;
	private static AdminGUIBungee instance;
	String username = "%%__USERNAME__%%";
	String user_id = "%%__USER__%%";

	public static AdminGUIBungee getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		getProxy().registerChannel("my:admingui");

		try {
			bungee_config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("config.yml"));
			mkdir();
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		getProxy().getPluginManager().registerListener(this, new PluginMessageListener());
		if (config.getBoolean("fallback_server_enabled", false))
			getProxy().getPluginManager().registerListener(this, new ServerKickListener());

		info(ChatColor.GREEN + "Enabled");
	}

	@Override
	public void onDisable() {
		info(ChatColor.RED + "Disabled");
	}

	void mkdir() {
		if (!getDataFolder().exists()) getDataFolder().mkdir();

		File file = new File(getDataFolder(), "config.yml");

		if (!file.exists()) {
			try (InputStream in = getResourceAsStream("bungee_config.yml")) {
				Files.copy(in, file.toPath());
				config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
				config.set("fallback_server_list", bungee_config.getSection("servers").getKeys().toArray());
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void info(String message) {
		String text = "\n\n";
		text += ChatColor.GRAY + "[]========[" + message + " " + ChatColor.RED + "AdminGUI-Bungee" + ChatColor.GRAY + "]=======[]\n";
		text += ChatColor.GRAY + "|\n";
		text += ChatColor.GRAY + "| " + ChatColor.RED + "Information:\n";
		text += ChatColor.GRAY + "|\n";
		text += ChatColor.GRAY + "|   " + ChatColor.BLUE + "Name: " + ChatColor.AQUA + "AdminGUI-Bungee\n";
		text += ChatColor.GRAY + "|   " + ChatColor.BLUE + "Developer: " + ChatColor.AQUA + "Black1_TV\n";
		if (!username.contains("%%__")) {
			text += ChatColor.GRAY + "|   " + ChatColor.BLUE + "Plugin owner: " + ChatColor.AQUA + username + "\n";
			text += ChatColor.GRAY + "|   " + ChatColor.BLUE + "License key: " + ChatColor.AQUA + new Hash().createLicenseKey(username) + "\n";
		} else if (!user_id.contains("%%__")) {
			text += ChatColor.GRAY + "|   " + ChatColor.BLUE + "Plugin owner: " + ChatColor.AQUA + user_id + "\n";
			text += ChatColor.GRAY + "|   " + ChatColor.BLUE + "License key: " + ChatColor.AQUA + new Hash().createLicenseKey(user_id) + "\n";
		} else {
			text += ChatColor.GRAY + "|   " + ChatColor.BLUE + "Plugin owner: " + ChatColor.DARK_RED + ChatColor.BOLD + "CRACKED\n";
			text += ChatColor.GRAY + "|   " + ChatColor.BLUE + "License key: " + ChatColor.DARK_RED + "00000-00000-00000-00000\n";
		}
		text += ChatColor.GRAY + "|   " + ChatColor.BLUE + "Version: " + ChatColor.AQUA + getDescription().getVersion() + "\n";
		text += ChatColor.GRAY + "|   " + ChatColor.BLUE + "Website: " + ChatColor.AQUA + "https://rabbit-company.com\n";
		text += ChatColor.GRAY + "|\n";
		text += ChatColor.GRAY + "| " + ChatColor.RED + "Sponsors:\n";
		text += ChatColor.GRAY + "|\n";
		text += ChatColor.GRAY + "|   " + ChatColor.BLUE + "- " + ChatColor.YELLOW + "https://rabbitserverlist.com\n";
		text += ChatColor.GRAY + "|\n";
		text += ChatColor.GRAY + "| " + ChatColor.RED + "Support:\n";
		text += ChatColor.GRAY + "|\n";
		text += ChatColor.GRAY + "|   " + ChatColor.BLUE + "Discord: " + ChatColor.AQUA + "ziga.zajc007\n";
		text += ChatColor.GRAY + "|   " + ChatColor.BLUE + "Mail: " + ChatColor.AQUA + "ziga.zajc007@gmail.com\n";
		text += ChatColor.GRAY + "|   " + ChatColor.BLUE + "Discord: " + ChatColor.AQUA + "https://discord.gg/hUNymXX\n";
		text += ChatColor.GRAY + "|\n";
		text += ChatColor.GRAY + "[]=========================================[]\n";

		getLogger().info(text);
	}

}
