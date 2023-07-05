package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.admingui.utils.Settings;
import com.rabbitcompany.admingui.utils.TargetPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommandSpy implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cYou can only use Command Spy in game."));
		} else {
			Player player = (Player) sender;
			if (AdminGUI.getInstance().getConf().getBoolean("acs_enabled", false)) {
				if (TargetPlayer.hasPermission(player, "admingui.chat.spy")) {
					if (Settings.command_spy.getOrDefault(player.getUniqueId(), false)) {
						Settings.command_spy.put(player.getUniqueId(), false);
						player.sendMessage(Message.chat(Message.getMessage(player.getUniqueId(), "prefix") + "&aCommand Spy is disabled!"));
					} else {
						Settings.command_spy.put(player.getUniqueId(), true);
						player.sendMessage(Message.chat(Message.getMessage(player.getUniqueId(), "prefix") + "&aCommand Spy is enabled!"));
					}
				} else {
					player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
				}
			} else {
				player.sendMessage(Message.chat(Message.getMessage(player.getUniqueId(), "prefix") + "&cCommand Spy is disabled on this server!"));
			}
		}
		return true;
	}
}
