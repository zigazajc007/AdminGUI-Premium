package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.utils.Channel;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.admingui.utils.Settings;
import com.rabbitcompany.admingui.utils.TargetPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Map;

public class PlayerPlaceholderCommandListener implements Listener {

	private final AdminGUI adminGUI;

	public PlayerPlaceholderCommandListener(AdminGUI plugin) {
		adminGUI = plugin;

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		Player p = event.getPlayer();
		String message = event.getMessage();

		if (adminGUI.getConf().getBoolean("command_disabler_enabled", false) && !TargetPlayer.hasPermission(p, "admingui.commanddisabler.bypass")) {
			if (adminGUI.getConf().getConfigurationSection("disabled_commands") != null) {
				for (String command : adminGUI.getConf().getConfigurationSection("disabled_commands").getValues(false).keySet()) {
					if (command.equals(message.split(" ")[0])) {
						p.sendMessage(Message.chat(adminGUI.getConf().getString("disabled_commands." + command, Message.getMessage(p.getUniqueId(), "permission"))));
						event.setCancelled(true);
						return;
					}
				}
			}
		}

		if (Settings.freeze.getOrDefault(p.getUniqueId(), false) && AdminGUI.getInstance().getConf().getBoolean("freeze_execute_commands", true)) {
			event.setCancelled(true);
			return;
		}

		//RTP Command
		if ((message.equals("/rtp") || message.equals("/wild")) && adminGUI.getConf().getBoolean("rtp_enabled", false)) {
			if (AdminGUI.getInstance().getConf().getDouble("rtp_delay", 10) > 0 && !TargetPlayer.hasPermission(p, "admingui.rtp.delay.bypass")) {
				long last_rtp_send = Settings.rtp_delay.getOrDefault(p.getUniqueId(), 0L);
				if (last_rtp_send != 0L) {
					if (last_rtp_send + (AdminGUI.getInstance().getConf().getDouble("rtp_delay", 10) * 1000) >= System.currentTimeMillis()) {
						p.sendMessage(Message.chat(AdminGUI.getInstance().getConf().getString("rtp_delay_message", "&cYou need to wait {seconds} seconds, before you can execute /rtp command again!").replace("{seconds}", AdminGUI.getInstance().getConf().getString("rtp_delay", "10"))));
						event.setCancelled(true);
						return;
					} else {
						Settings.rtp_delay.put(p.getUniqueId(), System.currentTimeMillis());
					}
				} else {
					Settings.rtp_delay.put(p.getUniqueId(), System.currentTimeMillis());
				}
			}

			p.sendMessage(Message.chat(AdminGUI.getInstance().getConf().getString("rtp_begin", "&aFinding safe location...")));

			if (TargetPlayer.safeTeleport(p)) {
				p.sendMessage(Message.chat(AdminGUI.getInstance().getConf().getString("rtp_success", "&aYou have been teleported to safe location.")));
			} else {
				p.sendMessage(Message.chat(AdminGUI.getInstance().getConf().getString("rtp_failed", "&cSafe location to teleport can't be found. Please try again later.")));
			}
			event.setCancelled(true);
		}

		if (adminGUI.getConf().getBoolean("acs_enabled", false)) {
			String chat_command_spy_format = PlaceholderAPI.setPlaceholders(p, adminGUI.getConf().getString("acs_format", "&7[Command Spy] {name} &7> {message}"));
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				if (TargetPlayer.hasPermission(player, "admingui.chat.spy") && Settings.command_spy.getOrDefault(player.getUniqueId(), false) && !player.getUniqueId().equals(p.getUniqueId())) {
					for (String ignored_command : adminGUI.getConf().getStringList("acs_ignore"))
						if (message.startsWith(ignored_command)) return;
					player.sendMessage(Message.chat(chat_command_spy_format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message)));
				}
			}
		}

		for (Map.Entry<String, Object> slots : AdminGUI.getInstance().getConf().getConfigurationSection("ccc").getValues(false).entrySet()) {

			List<String> aliases = adminGUI.getConf().getStringList("ccc." + slots.getKey() + ".aliases");

			int firstSpaceIndex = message.indexOf(" ");
			if (firstSpaceIndex == -1) firstSpaceIndex = message.length();

			if (message.startsWith("/" + slots.getKey()) || aliases.contains(message.substring(0, firstSpaceIndex).replace("/", ""))) {
				String name = adminGUI.getConf().getString("ccc." + slots.getKey() + ".name");
				String format = adminGUI.getConf().getString("ccc." + slots.getKey() + ".format");
				String permission = adminGUI.getConf().getString("ccc." + slots.getKey() + ".permission");

				if (name == null) name = slots.getKey();

				if (TargetPlayer.hasPermission(p, permission)) {
					String current_channel = Settings.custom_chat_channel.getOrDefault(p.getUniqueId(), "");

					if (message.replace("/", "").equals(slots.getKey()) || !message.contains(" ")) {
						if (current_channel.equals(slots.getKey())) {
							Settings.custom_chat_channel.remove(p.getUniqueId());
							p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_disabled").replace("{name}", name));
						} else {
							Settings.custom_chat_channel.put(p.getUniqueId(), slots.getKey());
							p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_enabled").replace("{name}", name));
						}
					} else {

						String message2 = message.substring(message.indexOf(" ") + 1);

						for (Player target : Bukkit.getServer().getOnlinePlayers()) {
							if (TargetPlayer.hasPermission(target, permission)) {
								target.sendMessage(Message.chat(format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message2)));
							}
						}
						Bukkit.getConsoleSender().sendMessage(Message.chat(format.replace("{name}", p.getName()).replace("{display_name}", p.getDisplayName()).replace("{message}", message2)));

						if (adminGUI.getConf().getBoolean("bungeecord_enabled", false) && adminGUI.getConf().getBoolean("bungeecord_custom_chat_channels", false)) {
							String serverName = adminGUI.getConf().getString("server_name", "Default");
							Channel.send(p.getUniqueId().toString(), "custom_chat_channels", slots.getKey(), serverName, p.getName(), message2);
						}
					}
				} else {
					p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "permission"));
				}
				event.setCancelled(true);
			}
		}
	}

}
