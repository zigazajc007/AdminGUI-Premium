package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.admingui.utils.Settings;
import com.rabbitcompany.admingui.utils.TargetPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AdminChat implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

		if (!(sender instanceof Player)) {

			if (AdminGUI.getInstance().getConf().getBoolean("ac_enabled", false)) {
				if (args.length == 1) {
					if (args[0].equals("clear")) {
						for (int i = 0; i < 100; i++) Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage(Message.getMessage(UUID.randomUUID(), "message_admin_chat_clear").replace("{player}", "Console"));
					} else if (args[0].equals("mute")) {
						if (Settings.muted_chat) {
							Settings.muted_chat = false;
							Bukkit.broadcastMessage(Message.getMessage(UUID.randomUUID(), "message_admin_chat_unmute"));
						} else {
							Settings.muted_chat = true;
							Bukkit.broadcastMessage(Message.getMessage(UUID.randomUUID(), "message_admin_chat_mute"));
						}
					} else {
						sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_chat_arguments"));
					}
				} else {
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_chat_arguments"));
				}
			} else {
				sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_chat_server_disabled"));
			}
			return true;
		}

		Player player = (Player) sender;

		if (TargetPlayer.hasPermission(player, "admingui.chat")) {
			if (AdminGUI.getInstance().getConf().getBoolean("ac_enabled", false)) {
				if (args.length == 1) {
					if (args[0].equals("clear")) {
						if (TargetPlayer.hasPermission(player, "admingui.chat.clear")) {
							for (int i = 0; i < 100; i++) Bukkit.broadcastMessage("");
							Bukkit.broadcastMessage(Message.getMessage(UUID.randomUUID(), "message_admin_chat_clear").replace("{player}", player.getName()));
						} else {
							player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
						}
					} else if (args[0].equals("mute")) {
						if (TargetPlayer.hasPermission(player, "admingui.chat.mute")) {
							if (Settings.muted_chat) {
								Settings.muted_chat = false;
								Bukkit.broadcastMessage(Message.getMessage(UUID.randomUUID(), "message_admin_chat_unmute"));
							} else {
								Settings.muted_chat = true;
								Bukkit.broadcastMessage(Message.getMessage(UUID.randomUUID(), "message_admin_chat_mute"));
							}
						} else {
							player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
						}
					} else {
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_chat_arguments"));
					}
				} else {
					player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_chat_arguments"));
				}
			} else {
				player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_chat_server_disabled"));
			}
		} else {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
		}

		return true;
	}
}
