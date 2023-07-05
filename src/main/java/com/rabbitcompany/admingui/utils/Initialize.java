package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getServer;

public class Initialize {

	public static void GUI(Player player, ItemStack helmet) {
		if (TargetPlayer.hasPermission(player, "admingui.admin")) {
			int max_value = Settings.skulls.size();
			if (AdminGUI.getInstance().getConf().getBoolean("initialize_reminder", true)) {
				player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_initializing_start"));
			}
			int taskID = getServer().getScheduler().scheduleSyncRepeatingTask(AdminGUI.getInstance(), new Runnable() {
				int i = 0;

				@Override
				public void run() {
					if (player.isOnline()) {
						if (i < max_value) {
							player.getInventory().setHelmet(Settings.skulls.get(Settings.skulls.keySet().toArray()[i].toString()));
						} else {
							player.getInventory().setHelmet(helmet);
							if (AdminGUI.getInstance().getConf().getBoolean("initialize_reminder", true)) {
								player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_initializing_finish"));
							}
							Bukkit.getServer().getScheduler().cancelTask(Settings.task_gui.get(player.getUniqueId()));
							Settings.task_gui.remove(player.getUniqueId());
						}
						i++;
					} else {
						Bukkit.getServer().getScheduler().cancelTask(Settings.task_gui.get(player.getUniqueId()));
						Settings.task_gui.remove(player.getUniqueId());
					}
				}
			}, 5 * 20L, AdminGUI.getInstance().getConf().getInt("initialize_delay", 1) * 20L);
			Settings.task_gui.put(player.getUniqueId(), taskID);
		}
	}

	public static void Players(Player player, ItemStack helmet) {
		if (TargetPlayer.hasPermission(player, "admingui.admin")) {
			int max_value = Settings.skulls_players.size();
			if (AdminGUI.getInstance().getConf().getBoolean("initialize_reminder", true)) {
				player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_initializing_start"));
			}
			int taskID = getServer().getScheduler().scheduleSyncRepeatingTask(AdminGUI.getInstance(), new Runnable() {
				int i = 0;

				@Override
				public void run() {
					if (player.isOnline()) {
						if (i < max_value) {
							player.getInventory().setHelmet(Settings.skulls_players.get(Settings.skulls_players.keySet().toArray()[i].toString()));
						} else {
							player.getInventory().setHelmet(helmet);
							if (AdminGUI.getInstance().getConf().getBoolean("initialize_reminder", true)) {
								player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_initializing_finish"));
							}
							Bukkit.getServer().getScheduler().cancelTask(Settings.task_players.get(player.getUniqueId()));
							Settings.task_players.remove(player.getUniqueId());
						}
						i++;
					} else {
						Bukkit.getServer().getScheduler().cancelTask(Settings.task_players.get(player.getUniqueId()));
						Settings.task_players.remove(player.getUniqueId());
					}
				}
			}, 20L, AdminGUI.getInstance().getConf().getInt("initialize_delay", 2) * 20L);
			Settings.task_players.put(player.getUniqueId(), taskID);
		}
	}
}
