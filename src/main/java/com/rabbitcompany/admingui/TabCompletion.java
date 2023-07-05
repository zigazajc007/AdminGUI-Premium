package com.rabbitcompany.admingui;

import com.rabbitcompany.admingui.utils.Channel;
import com.rabbitcompany.admingui.utils.Language;
import com.rabbitcompany.admingui.utils.Settings;
import com.rabbitcompany.admingui.utils.TargetPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TabCompletion implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		if (!(sender instanceof Player)) {
			if (command.getName().equalsIgnoreCase("admin")) {
				List<String> completions = new ArrayList<>();

				if (args.length == 1) {
					completions.add("reload");
					completions.add("check");
					completions.add("maintenance");
					completions.add("language");
					if(AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) completions.add("rank");
					if(AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) completions.add("permission");
					if (AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)) {
						Channel.send(sender.getName(), "send", "online_players");
						completions.addAll(Settings.online_players);
					} else {
						for (Player all : Bukkit.getServer().getOnlinePlayers()) {
							completions.add(all.getName());
						}
					}
				} else if (args.length == 2) {
					if (args[0].equals("rank") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						completions.add("set");
						completions.add("up");
						completions.add("down");
					} else if (args[0].equals("permission") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)){
						completions.add("add");
						completions.add("remove");
					} else if (args[0].equals("language")) {
						completions.add("fix");
						completions.add("download");
					} else if (args[0].equals("maintenance")) {
						completions.add("on");
						completions.add("off");
					} else if (args[0].equals("check")) {
						Set<String> con_sec = AdminGUI.getInstance().getPlayers().getConfigurationSection("").getKeys(false);
						for (String uuid_name : con_sec) {
							completions.add(AdminGUI.getInstance().getPlayers().getString(uuid_name + ".name"));
						}
					}
				} else if (args.length == 3) {
					if(args[0].equals("permission") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false) && (args[1].equals("add") || args[1].equals("remove"))) {
						for (Player all : Bukkit.getServer().getOnlinePlayers()) completions.add(all.getName());
					} else if (args[0].equals("rank") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false) && (args[1].equals("set") || args[1].equals("up") || args[1].equals("down"))) {
						if (AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)) {
							Channel.send(sender.getName(), "send", "online_players");
							completions.addAll(Settings.online_players);
						} else {
							for (Player all : Bukkit.getServer().getOnlinePlayers()) completions.add(all.getName());
						}
					} else if (args[0].equals("language") && args[1].equals("download")) {
						completions.addAll(Language.default_languages);
					} else if (args[0].equals("language") && args[1].equals("fix")) {
						completions.addAll(Language.enabled_languages);
					}
				} else if (args.length == 4) {
					if(args[0].equals("permission") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false) && args[1].equals("remove")) {
						Player target_player = Bukkit.getServer().getPlayer(args[2]);
						if(target_player != null) {
							List<String> playerPermissions = AdminGUI.getInstance().getPlayers().getStringList(target_player.getUniqueId() + ".permissions");
							completions.addAll(playerPermissions);
						}
					} else if (args[0].equals("rank") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false) && args[1].equals("set")) {
						for (Map.Entry<String, Object> rank : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
							completions.add(rank.getKey());
						}
					}
				}
				return completions;
			} else if (command.getName().equalsIgnoreCase("adminchat")) {
				List<String> completions = new ArrayList<>();

				if (args.length == 1) {
					completions.add("mute");
					completions.add("clear");
				}

				return completions;
			}

			return null;
		}

		Player player = (Player) sender;

		if (command.getName().equalsIgnoreCase("admin")) {
			List<String> completions = new ArrayList<>();

			if (args.length == 1) {

				if (TargetPlayer.hasPermission(player, "admingui.admin")){
					if (AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)) {
						Channel.send(sender.getName(), "send", "online_players");
						completions.addAll(Settings.online_players);
					} else {
						for (Player all : Bukkit.getServer().getOnlinePlayers()) {
							completions.add(all.getName());
						}
					}
				}

				if (TargetPlayer.hasPermission(player, "admingui.reload")) completions.add("reload");

				if (TargetPlayer.hasPermission(player, "admingui.rank") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false))
					completions.add("rank");

				if(TargetPlayer.hasPermission(player, "admingui.permission.manage") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false))
					completions.add("permission");

				if (TargetPlayer.hasPermission(player, "admingui.check")) completions.add("check");

				if (TargetPlayer.hasPermission(player, "admingui.maintenance.manage")) completions.add("maintenance");

				if (TargetPlayer.hasPermission(player, "admingui.admin")) completions.add("tools");

				if (TargetPlayer.hasPermission(player, "admingui.admin")) completions.add("initialize");

			} else if (args.length == 2) {
				if (args[0].equals("initialize")) {
					if (TargetPlayer.hasPermission(player, "admingui.admin")) completions.add("gui");
					if (TargetPlayer.hasPermission(player, "admingui.admin")) completions.add("players");
				} else if (args[0].equals("rank") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
					if (TargetPlayer.hasPermission(player, "admingui.rank.set")) completions.add("set");
					if (TargetPlayer.hasPermission(player, "admingui.rank.up")) completions.add("up");
					if (TargetPlayer.hasPermission(player, "admingui.rank.down")) completions.add("down");
				} else if (args[0].equals("permission") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)){
					if (TargetPlayer.hasPermission(player, "admingui.permission.manage")){
						completions.add("add");
						completions.add("remove");
					}
				} else if (args[0].equals("maintenance")) {
					if (TargetPlayer.hasPermission(player, "admingui.maintenance.manage")){
						completions.add("on");
						completions.add("off");
					}
				} else if (args[0].equals("check") && TargetPlayer.hasPermission(player, "admingui.check")) {
					Set<String> con_sec = AdminGUI.getInstance().getPlayers().getConfigurationSection("").getKeys(false);
					for (String uuid_name : con_sec) {
						completions.add(AdminGUI.getInstance().getPlayers().getString(uuid_name + ".name"));
					}
				}
			} else if (args.length == 3) {
				if(args[0].equals("permission") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false) && (args[1].equals("add") || args[1].equals("remove"))) {
					for (Player all : Bukkit.getServer().getOnlinePlayers()) completions.add(all.getName());
				} else if (args[0].equals("rank") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false) && (args[1].equals("set") || args[1].equals("up") || args[1].equals("down"))) {
					if (AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)) {
						Channel.send(sender.getName(), "send", "online_players");
						completions.addAll(Settings.online_players);
					} else {
						for (Player all : Bukkit.getServer().getOnlinePlayers()) completions.add(all.getName());
					}
				}
			} else if (args.length == 4) {
				if(args[0].equals("permission") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false) && args[1].equals("remove") && TargetPlayer.hasPermission(player, "admingui.permission.manage")) {
					Player target_player = Bukkit.getServer().getPlayer(args[2]);
					if(target_player != null) {
						List<String> playerPermissions = AdminGUI.getInstance().getPlayers().getStringList(target_player.getUniqueId() + ".permissions");
						completions.addAll(playerPermissions);
					}
				}else if (args[0].equals("rank") && AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false) && args[1].equals("set")) {
					for (Map.Entry<String, Object> rank : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
						completions.add(rank.getKey());
					}
				}
			}
			return completions;
		} else if (command.getName().equalsIgnoreCase("adminchat")) {
			List<String> completions = new ArrayList<>();

			if (args.length == 1) {
				if(TargetPlayer.hasPermission(player, "admingui.chat.mute")) completions.add("mute");
				if(TargetPlayer.hasPermission(player, "admingui.chat.clear")) completions.add("clear");
			}

			return completions;
		}

		return null;
	}
}
