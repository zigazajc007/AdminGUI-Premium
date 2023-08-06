package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.utils.*;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class PlayerPlaceholderMessageListener implements Listener {

	private final AdminGUI adminGUI;

	public PlayerPlaceholderMessageListener(AdminGUI plugin) {
		adminGUI = plugin;

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPlaceholderChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		String message = event.getMessage();

		if (Settings.freeze.getOrDefault(p.getUniqueId(), false) && AdminGUI.getInstance().getConf().getBoolean("freeze_send_message", true)) {
			event.setCancelled(true);

			String freeze_channel = AdminGUI.getInstance().getConf().getString("freeze_admin_chat", null);

			if (freeze_channel != null) {
				String format = PlaceholderAPI.setPlaceholders(p, adminGUI.getConf().getString("ccc." + freeze_channel + ".format"));

				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					if (TargetPlayer.hasPermission(player, adminGUI.getConf().getString("ccc." + freeze_channel + ".permission")) || Settings.freeze.getOrDefault(player.getUniqueId(), false)) {
						player.sendMessage(Message.chat(format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
					}
				}
				Bukkit.getConsoleSender().sendMessage(Message.chat(format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
			}

			return;
		}

		String command = Settings.custom_chat_channel.getOrDefault(p.getUniqueId(), "");
		if (!command.equals("")) {
			event.setCancelled(true);

			String format = PlaceholderAPI.setPlaceholders(p, adminGUI.getConf().getString("ccc." + command + ".format"));
			String permission = adminGUI.getConf().getString("ccc." + command + ".permission");

			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				if (TargetPlayer.hasPermission(player, permission)) {
					player.sendMessage(Message.chat(format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
				}
			}
			Bukkit.getConsoleSender().sendMessage(Message.chat(format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));

			if (adminGUI.getConf().getBoolean("bungeecord_enabled", false) && adminGUI.getConf().getBoolean("bungeecord_custom_chat_channels", false)) {
				String serverName = adminGUI.getConf().getString("server_name", "Default");
				Channel.send(p.getUniqueId().toString(), "custom_chat_channels", command, serverName, p.getName(), message);
			}

		} else {

			if (adminGUI.getConf().getBoolean("ac_enabled", false)) {

				if (Settings.muted_chat && !TargetPlayer.hasPermission(p, "admingui.chat.mute.bypass")) {
					event.setCancelled(true);
					p.sendMessage(Message.getMessage(p.getUniqueId(), "message_admin_chat_muted"));
					return;
				}

				if (Bukkit.getServer().getPluginManager().getPlugin("AdminBans") != null) {
					if (AdminBansAPI.isPlayerMuted(event.getPlayer().getUniqueId(), AdminBansAPI.server_name)) return;
				}

				if (adminGUI.getConf().getDouble("ac_delay", 0) > 0 && !TargetPlayer.hasPermission(p, "admingui.chat.delay.bypass")) {
					long last_message_send = Settings.admin_chat_delay.getOrDefault(p.getUniqueId(), 0L);
					if (last_message_send != 0L) {
						if (last_message_send + (adminGUI.getConf().getDouble("ac_delay", 0) * 1000) >= System.currentTimeMillis()) {
							event.setCancelled(true);
							p.sendMessage(Message.chat(adminGUI.getConf().getString("ac_delay_message", "&cYou need to wait {seconds} seconds to sent another message!").replace("{seconds}", adminGUI.getConf().getString("ac_delay", "0"))));
							return;
						} else {
							Settings.admin_chat_delay.put(p.getUniqueId(), System.currentTimeMillis());
						}
					} else {
						Settings.admin_chat_delay.put(p.getUniqueId(), System.currentTimeMillis());
					}
				}

				message = Message.chat(message);
				if (!TargetPlayer.hasPermission(p, "admingui.chat.color") && !TargetPlayer.hasPermission(p, "admingui.chat.colors")) {
					message = ChatColor.stripColor(message);
				} else {
					message = Colors.toHex(message);
				}

				message = message.replaceAll("%", "‰");

				if (!TargetPlayer.hasPermission(p, "admingui.chat.advertisement.bypass")) {
					if (!message.endsWith(".")) {
						message = message.replace(".", " ");
					}
				}

				if (adminGUI.getConf().getBoolean("ac_beautifier", true)) {
					message = message.toLowerCase();
					if (!message.endsWith(".") && !message.endsWith("!") && !message.endsWith("?")) {
						message = message + ".";
					}
				}

				List<String> filters = adminGUI.getConf().getStringList("ac_filter");

				if (!TargetPlayer.hasPermission(p, "admingui.chat.filter.bypass")) {
					for (String filter : filters) {
						message = message.replace(filter, "****");
					}

					if (!adminGUI.getConf().getBoolean("ac_beautifier", true)) {
						String tempMessage = message.toLowerCase();

						for (String filter : filters) {
							tempMessage = tempMessage.replace(filter, "****");
						}

						int index = tempMessage.indexOf("*");
						while (index != -1) {
							message = message.substring(0, index) + "*" + message.substring(index + 1);
							index = tempMessage.indexOf("*", index + 1);
						}
					}
				}

				if (adminGUI.getConf().getConfigurationSection("ac_emojis") != null) {
					for (String emoji : adminGUI.getConf().getConfigurationSection("ac_emojis").getValues(false).keySet())
						message = message.replace(emoji, adminGUI.getConf().getString("ac_emojis." + emoji, emoji));
				}

				if (adminGUI.getConf().getBoolean("ac_beautifier", true))
					message = Character.toUpperCase(message.charAt(0)) + message.substring(1);

				switch (Settings.chat_color.getOrDefault(p.getUniqueId(), adminGUI.getConf().getString("ac_default_color", "LIGHT_GRAY_WOOL"))) {
					case "WHITE_WOOL":
						message = Message.chat("&f" + message);
						break;
					case "ORANGE_WOOL":
						message = Message.chat("&6" + message);
						break;
					case "MAGENTA_WOOL":
						message = Message.chat("&d" + message);
						break;
					case "LIGHT_BLUE_WOOL":
						message = Message.chat("&b" + message);
						break;
					case "YELLOW_WOOL":
						message = Message.chat("&e" + message);
						break;
					case "LIME_WOOL":
						message = Message.chat("&a" + message);
						break;
					case "PINK_WOOL":
						message = Message.chat("&c" + message);
						break;
					case "GRAY_WOOL":
						message = Message.chat("&8" + message);
						break;
					case "LIGHT_GRAY_WOOL":
						message = Message.chat("&7" + message);
						break;
					case "CYAN_WOOL":
						message = Message.chat("&3" + message);
						break;
					case "PURPLE_WOOL":
						message = Message.chat("&5" + message);
						break;
					case "BLUE_WOOL":
						message = Message.chat("&9" + message);
						break;
					case "GREEN_WOOL":
						message = Message.chat("&2" + message);
						break;
					case "RED_WOOL":
						message = Message.chat("&4" + message);
						break;
					case "BLACK_WOOL":
						message = Message.chat("&0" + message);
						break;
					case "CLOCK":
						message = Message.chat(Colors.rainbowText(message));
						break;
					case "EXPERIENCE_BOTTLE":
						message = Colors.gradientText(message);
						break;
					default:
						message = Message.chat(message);
						break;
				}

				//TODO: Permissions
				String prefix = Permissions.getPrefix(p.getUniqueId());
				String suffix = Permissions.getSuffix(p.getUniqueId());

				//VaultAPI
				String vault_prefix = "";
				String vault_suffix = "";
				if (AdminGUI.getVaultChat() != null) {
					vault_prefix = AdminGUI.getVaultChat().getPlayerPrefix(p);
					vault_suffix = AdminGUI.getVaultChat().getPlayerSuffix(p);
				}

				String format = PlaceholderAPI.setPlaceholders(p, Colors.toHex(Message.chat(adminGUI.getConf().getString("ac_format", "&7{display_name} &7> {message}").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{server_name}", adminGUI.getConf().getString("server_name", "Default")).replace("{prefix}", prefix).replace("{suffix}", suffix).replace("{vault_prefix}", vault_prefix).replace("{vault_suffix}", vault_suffix))));

				int noChatReport = adminGUI.getConf().getInt("ac_no_chat_reports", 0);
				if(noChatReport == 1){
					message = message + " ";
				}else if(noChatReport == 2 || noChatReport == 3){
					event.setCancelled(true);
					for(Player player : Bukkit.getOnlinePlayers()){
						player.sendMessage(format.replace("{message}", message));
					}
					if(noChatReport == 3) Bukkit.getConsoleSender().sendMessage(format.replace("{message}", message));
				}

				event.setMessage(message);
				event.setFormat(format.replace("{message}", message));

				if (adminGUI.getConf().getBoolean("bungeecord_enabled", false) && adminGUI.getConf().getBoolean("bungeecord_admin_chat", false)) {
					String bungee_format = PlaceholderAPI.setPlaceholders(p, Colors.toHex(Message.chat(adminGUI.getConf().getString("bungeecord_admin_chat_format", "&7[{server_name}] &7{display_name} &7> {message}").replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{server_name}", adminGUI.getConf().getString("server_name", "Default")).replace("{prefix}", prefix).replace("{suffix}", suffix).replace("{vault_prefix}", vault_prefix).replace("{vault_suffix}", vault_suffix))));
					Channel.send(p.getUniqueId().toString(), "chat", bungee_format.replace("{message}", message));
				}
			}

		}
	}

}
