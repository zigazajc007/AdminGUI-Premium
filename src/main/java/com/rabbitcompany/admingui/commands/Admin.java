package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class Admin implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

		AdminUI adminUI = new AdminUI();

		if (!(sender instanceof Player)) {

			if (args.length == 1) {
				if (args[0].equals("reload")) {
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_reload_start"));
					AdminGUI.getInstance().mkdir();
					AdminGUI.getInstance().loadYamls();
					Language.getLanguages();
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_reload_finish"));
				} else if (args[0].equals("language")) {
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cYou can only use /admin language download <language> or /admin language fix <language>"));
				} else if (args[0].equals("check")) {
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_check_arguments"));
				}else if (args[0].equals("rank")) {
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						sender.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_rank_arguments"));
				} else if (args[0].equals("permission")) {
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						sender.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_permission_arguments"));
				} else if (args[0].equals("maintenance")) {
					if (Settings.maintenance_mode) {
						Settings.maintenance_mode = false;
						AdminGUI.getInstance().getSett().set("maintenance", false);
						AdminGUI.getInstance().saveSettings();
						sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance_disabled"));
					} else {
						Settings.maintenance_mode = true;
						AdminGUI.getInstance().getSett().set("maintenance", true);
						AdminGUI.getInstance().saveSettings();
						sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance_enabled"));
						for (Player pl : getServer().getOnlinePlayers()) {
							if (!pl.isOp() && !TargetPlayer.hasPermission(pl, "admingui.maintenance")) {
								pl.kickPlayer(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance"));
							}
						}
					}
				}
			} else if (args.length == 2) {
				if (args[0].equals("rank")) {
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						sender.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_rank_arguments"));
				} else if (args[0].equals("permission")) {
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						sender.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_permission_arguments"));
				} else if (args[0].equals("language")) {
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cYou can only use /admin language download <language> or /admin language fix <language>"));
				} else if (args[0].equals("maintenance")) {
					String enabled = ChatColor.stripColor(args[1]);
					if (enabled.equals("on") || enabled.equals("enable")) {
						if (!Settings.maintenance_mode) {
							Settings.maintenance_mode = true;
							AdminGUI.getInstance().getSett().set("maintenance", true);
							AdminGUI.getInstance().saveSettings();
							for (Player pl : getServer().getOnlinePlayers()) {
								if (!pl.isOp() && !TargetPlayer.hasPermission(pl, "admingui.maintenance")) {
									pl.kickPlayer(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance"));
								}
							}
						}
						sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance_enabled"));
					} else if (enabled.equals("off") || enabled.equals("disable")) {
						Settings.maintenance_mode = false;
						AdminGUI.getInstance().getSett().set("maintenance", false);
						AdminGUI.getInstance().saveSettings();
						sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance_disabled"));
					} else {
						sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cYou can only use /admin maintenance <on/off>"));
					}
				} else if (args[0].equals("check")) {
					String name = ChatColor.stripColor(args[1]);
					Set<String> con_sec = AdminGUI.getInstance().getPlayers().getConfigurationSection("").getKeys(false);
					for (String uuid_name : con_sec) {
						if (AdminGUI.getInstance().getPlayers().getString(uuid_name + ".name", "").equals(name)) {
							sender.sendMessage(Message.chat("&9" + name + " stats:"));
							sender.sendMessage(Message.chat("  &9UUID: &b" + uuid_name));
							if (AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
								sender.sendMessage(Message.chat("  &9Rank: &b" + AdminGUI.getInstance().getPlayers().getString(uuid_name + ".rank", "default")));
							}
							StringBuilder ips = new StringBuilder();
							for (String ip : AdminGUI.getInstance().getPlayers().getStringList(uuid_name + ".ips")) {
								ips.append(ip).append(", ");
							}
							if (ips.length() >= 3) {
								ips.delete(ips.length() - 2, ips.length());
								sender.sendMessage(Message.chat("  &9IPs: &b" + ips));
							} else {
								sender.sendMessage(Message.chat("  &9IPs:&b none"));
							}
							StringBuilder alts = new StringBuilder();
							for (String uuid_name2 : con_sec) {
								String alt_name = AdminGUI.getInstance().getPlayers().getString(uuid_name2 + ".name");
								for (String ip : AdminGUI.getInstance().getPlayers().getStringList(uuid_name2 + ".ips")) {
									if (ips.toString().contains(ip) && !name.equals(alt_name) && !alts.toString().contains(alt_name)) {
										alts.append(alt_name).append(", ");
									}
								}
							}
							if (alts.length() >= 3) {
								alts.delete(alts.length() - 2, alts.length());
								sender.sendMessage(Message.chat("  &9ALTs: &b" + alts));
							} else {
								sender.sendMessage(Message.chat("  &9ALTs:&b none"));
							}
							sender.sendMessage(Message.chat("  &9Last Join: &b" + Database.date_format.format(new Date(AdminGUI.getInstance().getPlayers().getLong(uuid_name + ".lastJoin")))));
							sender.sendMessage(Message.chat("  &9First Join: &b" + Database.date_format.format(new Date(AdminGUI.getInstance().getPlayers().getLong(uuid_name + ".firstJoin")))));
							break;
						}
					}
				}
			} else if (args.length == 3) {
				if (args[0].equals("permission")){
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						sender.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_permission_arguments"));
				} else if (args[0].equals("rank")) {
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						sender.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}
					if (args[1].equals("up")) {
						Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[2]));
						if (target_player != null) {
							String cur_rank = Permissions.getRank(target_player.getUniqueId(), target_player.getName());
							int cur_priority = AdminGUI.getInstance().getPermissions().getInt("groups." + cur_rank + ".priority", AdminGUI.getInstance().getPermissions().getInt("groups.default.priority"));
							ArrayList<Integer> priorities = new ArrayList<>();
							for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
								int pri = AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority");
								if (cur_priority > pri) priorities.add(pri);
							}
							if (priorities.size() > 0) {
								int max_pri = Collections.max(priorities);
								for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
									if (AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority") == max_pri) {
										Permissions.setRank(target_player.getUniqueId(), target_player.getName(), priority.getKey());
										TargetPlayer.refreshPlayerTabList(target_player);
										TargetPlayer.refreshPermissions(target_player);
										sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", priority.getKey()));
									}
								}
							} else {
								sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", cur_rank));
							}
						} else {
							String cur_rank = Permissions.getRank(null, ChatColor.stripColor(args[2]));
							int cur_priority = AdminGUI.getInstance().getPermissions().getInt("groups." + cur_rank + ".priority", AdminGUI.getInstance().getPermissions().getInt("groups.default.priority"));
							ArrayList<Integer> priorities = new ArrayList<>();
							for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
								int pri = AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority");
								if (cur_priority > pri) priorities.add(pri);
							}
							if (priorities.size() > 0) {
								int max_pri = Collections.max(priorities);
								for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
									if (AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority") == max_pri) {
										Permissions.setRank(null, ChatColor.stripColor(args[2]), priority.getKey());
										sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", ChatColor.stripColor(args[2])).replace("{rank}", priority.getKey()));
									}
								}
							} else {
								sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", ChatColor.stripColor(args[2])).replace("{rank}", cur_rank));
							}
						}
					} else if (args[1].equals("down")) {
						Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[2]));
						if (target_player != null) {
							String cur_rank = Permissions.getRank(target_player.getUniqueId(), target_player.getName());
							int cur_priority = AdminGUI.getInstance().getPermissions().getInt("groups." + cur_rank + ".priority", AdminGUI.getInstance().getPermissions().getInt("groups.default.priority"));
							ArrayList<Integer> priorities = new ArrayList<>();
							for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
								int pri = AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority");
								if (cur_priority < pri) priorities.add(pri);
							}
							if (priorities.size() > 0) {
								int min_pri = Collections.min(priorities);
								for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
									if (AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority") == min_pri) {
										Permissions.setRank(target_player.getUniqueId(), target_player.getName(), priority.getKey());
										TargetPlayer.refreshPlayerTabList(target_player);
										TargetPlayer.refreshPermissions(target_player);
										sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", priority.getKey()));
										break;
									}
								}
							} else {
								sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", cur_rank));
							}
						} else {
							String cur_rank = Permissions.getRank(null, ChatColor.stripColor(args[2]));
							int cur_priority = AdminGUI.getInstance().getPermissions().getInt("groups." + cur_rank + ".priority", AdminGUI.getInstance().getPermissions().getInt("groups.default.priority"));
							ArrayList<Integer> priorities = new ArrayList<>();
							for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
								int pri = AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority");
								if (cur_priority < pri) priorities.add(pri);
							}
							if (priorities.size() > 0) {
								int min_pri = Collections.min(priorities);
								for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
									if (AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority") == min_pri) {
										Permissions.setRank(null, ChatColor.stripColor(args[2]), priority.getKey());
										sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", ChatColor.stripColor(args[2])).replace("{rank}", priority.getKey()));
										break;
									}
								}
							} else {
								sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", ChatColor.stripColor(args[2])).replace("{rank}", cur_rank));
							}
						}
					} else {
						sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_rank_arguments"));
					}
				} else if (args[0].equals("language")) {
					if (args[1].equals("download")) {
						if (Language.downloadLanguage(args[2])) {
							sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&a" + args[2] + " language has been downloaded."));
						} else {
							sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&c" + args[2] + " language don't exists. Please check list of available languages."));
						}
					} else if (args[1].equals("fix")) {
						if (Language.fixLanguage(args[2])) {
							sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&a" + args[2] + " language has been fixed."));
						} else {
							sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&c" + args[2] + ".yml don't exists. Please download it first."));
						}
					} else {
						sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cYou can only use /admin language download <language> or /admin language fix <language>"));
					}
				} else {
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_arguments"));
				}
			} else if (args.length == 4) {
				if (args[0].equals("permission")){
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						sender.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}

					if(!args[1].equals("add") && !args[1].equals("remove")){
						sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_permission_arguments"));
						return true;
					}

					String name = args[2];
					String permission = args[3];

					Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(name));
					if(target_player != null){
						if(args[1].equals("add")){
							Permissions.setPermission(target_player.getUniqueId(), name, permission);
							sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_permission_added").replace("{player}", name).replace("{permission}", permission));
						}else{
							Permissions.removePermission(target_player.getUniqueId(), name, permission);
							sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_permission_removed").replace("{player}", name).replace("{permission}", permission));
						}
						return true;
					}

					if(args[1].equals("add")){
						Permissions.setPermission(null, name, permission);
						sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_permission_added").replace("{player}", name).replace("{permission}", permission));
					}else{
						Permissions.removePermission(null, name, permission);
						sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_permission_removed").replace("{player}", name).replace("{permission}", permission));
					}
				}else if (args[0].equals("rank")) {
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						sender.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}
					if (args[1].equals("set")) {
						Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[2]));
						String rank = args[3];
						if (target_player != null) {
							if (Permissions.setRank(target_player.getUniqueId(), target_player.getName(), rank)) {
								TargetPlayer.refreshPlayerTabList(target_player);
								TargetPlayer.refreshPermissions(target_player);
								sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", rank));
							} else {
								sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_rank_arguments"));
							}
						} else {
							if (Permissions.setRank(null, ChatColor.stripColor(args[2]), rank)) {
								sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", ChatColor.stripColor(args[2])).replace("{rank}", rank));
							} else {
								sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_rank_arguments"));
							}
						}
					} else {
						sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_rank_arguments"));
					}
				} else {
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_arguments"));
				}
			} else {
				sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cYou can only use admin GUI in game."));
			}

			return true;
		}

		Player player = (Player) sender;

		if (TargetPlayer.hasPermission(player, "admingui.admin")) {

			//TODO: Bungee
			Channel.send(player.getName(), "send", "online_players");

			if (args.length == 0) {
				Settings.target_player.put(player.getUniqueId(), player);
				player.openInventory(adminUI.GUI_Main(player));
			} else if (args.length == 1) {
				if (args[0].equals("reload")) {
					if (TargetPlayer.hasPermission(player, "admingui.reload")) {
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_reload_start"));
						AdminGUI.getInstance().mkdir();
						AdminGUI.getInstance().loadYamls();
						Language.getLanguages();
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_reload_finish"));
					} else {
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
					}
				} else if (args[0].equals("initialize")) {
					player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_initialize"));
				} else if (args[0].equals("rank")) {
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						player.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}
					player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
				} else if (args[0].equals("permission")){
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						player.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}
					player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_permission_arguments"));
				} else if (args[0].equals("check")) {
					player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_check_arguments"));
				} else if (args[0].equals("maintenance")) {
					if (TargetPlayer.hasPermission(player, "admingui.maintenance.manage")) {
						if (Settings.maintenance_mode) {
							Settings.maintenance_mode = false;
							AdminGUI.getInstance().getSett().set("maintenance", false);
							AdminGUI.getInstance().saveSettings();
							player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance_disabled"));
						} else {
							Settings.maintenance_mode = true;
							AdminGUI.getInstance().getSett().set("maintenance", true);
							AdminGUI.getInstance().saveSettings();
							player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance_enabled"));
							for (Player pl : getServer().getOnlinePlayers()) {
								if (!pl.isOp() && !TargetPlayer.hasPermission(pl, "admingui.maintenance")) {
									pl.kickPlayer(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance"));
								}
							}
						}
					} else {
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
					}
				} else if (args[0].equals("tools") || args[0].equals("tool")) {
					if (TargetPlayer.hasPermission(player, "admingui.admin") && AdminGUI.getInstance().getConf().getBoolean("admin_tools_enabled", true)) {
						TargetPlayer.giveAdminTools(player, 0);
					} else {
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
					}
				} else {
					Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[0]));
					if (target_player != null) {
						Settings.target_player.put(player.getUniqueId(), target_player);
						if (player.getName().equals(target_player.getName())) {
							player.openInventory(adminUI.GUI_Player(player));
						} else {
							player.openInventory(adminUI.GUI_Players_Settings(player, target_player, target_player.getName()));
						}
					} else if (AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false) && Settings.online_players.contains(ChatColor.stripColor(args[0]))) {
						//TODO: Bungee
						switch (AdminGUI.getInstance().getConf().getInt("control_type", 0)) {
							case 0:
								Channel.send(player.getName(), "connect", ChatColor.stripColor(args[0]));
								break;
							case 1:
								Settings.target_player.put(player.getUniqueId(), null);
								player.openInventory(adminUI.GUI_Players_Settings(player, null, ChatColor.stripColor(args[0])));
								break;
							default:
								player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.chat("&cPlayer " + ChatColor.stripColor(args[0]) + " is not located in the same server as you."));
								break;
						}
					} else {
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "is_not_a_player").replace("{player}", args[0]));
					}
				}
			} else if (args.length == 2) {
				if (args[0].equals("initialize")) {
					if (args[1].equals("gui")) {
						if (!Settings.task_gui.containsKey(player.getUniqueId()) && !Settings.task_players.containsKey(player.getUniqueId())) {
							Initialize.GUI(player, player.getInventory().getHelmet());
						} else {
							player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_already_initializing"));
						}
					} else if (args[1].equals("players")) {
						if (!Settings.task_gui.containsKey(player.getUniqueId()) && !Settings.task_players.containsKey(player.getUniqueId())) {
							Initialize.Players(player, player.getInventory().getHelmet());
						} else {
							player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_already_initializing"));
						}
					} else {
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_initialize"));
					}
				} else if (args[0].equals("rank")) {
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						player.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}
					player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
				} else if (args[0].equals("permission")) {
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						player.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}
					player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_permission_arguments"));
				} else if (args[0].equals("maintenance")) {
					if (TargetPlayer.hasPermission(player, "admingui.maintenance.manage")) {
						String enabled = ChatColor.stripColor(args[1]);
						if (enabled.equals("on") || enabled.equals("enable")) {
							if (!Settings.maintenance_mode) {
								Settings.maintenance_mode = true;
								AdminGUI.getInstance().getSett().set("maintenance", true);
								AdminGUI.getInstance().saveSettings();
								for (Player pl : getServer().getOnlinePlayers()) {
									if (!pl.isOp() && !TargetPlayer.hasPermission(pl, "admingui.maintenance")) {
										pl.kickPlayer(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance"));
									}
								}
							}
							player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_maintenance_enabled"));
						} else if (enabled.equals("off") || enabled.equals("disable")) {
							Settings.maintenance_mode = false;
							AdminGUI.getInstance().getSett().set("maintenance", false);
							AdminGUI.getInstance().saveSettings();
							player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_maintenance_disabled"));
						} else {
							player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.chat("&cYou can only use /admin maintenance <on/off>"));
						}
					} else {
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
					}
				} else if (args[0].equals("check")) {
					if (TargetPlayer.hasPermission(player, "admingui.check")) {
						String name = ChatColor.stripColor(args[1]);
						Set<String> con_sec = AdminGUI.getInstance().getPlayers().getConfigurationSection("").getKeys(false);
						for (String uuid_name : con_sec) {
							if (AdminGUI.getInstance().getPlayers().getString(uuid_name + ".name").equals(name)) {
								player.sendMessage(Message.chat("&9" + name + " stats:"));
								player.sendMessage(Message.chat("  &9UUID: &b" + uuid_name));
								if (AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
									player.sendMessage(Message.chat("  &9Rank: &b" + AdminGUI.getInstance().getPlayers().getString(uuid_name + ".rank", "default")));
								}
								StringBuilder ips = new StringBuilder();
								for (String ip : AdminGUI.getInstance().getPlayers().getStringList(uuid_name + ".ips")) {
									ips.append(ip).append(", ");
								}
								if (ips.length() >= 3) {
									ips.delete(ips.length() - 2, ips.length());
									player.sendMessage(Message.chat("  &9IPs: &b" + ips));
								} else {
									player.sendMessage(Message.chat("  &9IPs:&b none"));
								}
								StringBuilder alts = new StringBuilder();
								for (String uuid_name2 : con_sec) {
									String alt_name = AdminGUI.getInstance().getPlayers().getString(uuid_name2 + ".name");
									for (String ip : AdminGUI.getInstance().getPlayers().getStringList(uuid_name2 + ".ips")) {
										if (ips.toString().contains(ip) && !name.equals(alt_name) && !alts.toString().contains(alt_name)) {
											alts.append(alt_name).append(", ");
										}
									}
								}
								if (alts.length() >= 3) {
									alts.delete(alts.length() - 2, alts.length());
									player.sendMessage(Message.chat("  &9ALTs: &b" + alts));
								} else {
									player.sendMessage(Message.chat("  &9ALTs:&b none"));
								}
								player.sendMessage(Message.chat("  &9Last Join: &b" + Database.date_format.format(new Date(AdminGUI.getInstance().getPlayers().getLong(uuid_name + ".lastJoin")))));
								player.sendMessage(Message.chat("  &9First Join: &b" + Database.date_format.format(new Date(AdminGUI.getInstance().getPlayers().getLong(uuid_name + ".firstJoin")))));
								break;
							}
						}
					} else {
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
					}
				} else {
					player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_arguments"));
				}
			} else if (args.length == 3) {
				if (args[0].equals("permission")) {
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						player.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}
					player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_permission_arguments"));
				} else if (args[0].equals("rank")) {
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						player.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}
					if (args[1].equals("up")) {
						if (TargetPlayer.hasPermission(player, "admingui.rank.up")) {
							Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[2]));
							if (target_player != null) {
								String cur_rank = Permissions.getRank(target_player.getUniqueId(), target_player.getName());
								int cur_priority = AdminGUI.getInstance().getPermissions().getInt("groups." + cur_rank + ".priority", AdminGUI.getInstance().getPermissions().getInt("groups.default.priority"));
								ArrayList<Integer> priorities = new ArrayList<>();
								for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
									int pri = AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority");
									if (cur_priority > pri) priorities.add(pri);
								}
								if (priorities.size() > 0) {
									int max_pri = Collections.max(priorities);
									for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
										if (AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority") == max_pri) {
											Permissions.setRank(target_player.getUniqueId(), target_player.getName(), priority.getKey());
											TargetPlayer.refreshPlayerTabList(target_player);
											TargetPlayer.refreshPermissions(target_player);
											player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", priority.getKey()));
										}
									}
								} else {
									player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", cur_rank));
								}
							} else {
								String target_player_name = ChatColor.stripColor(args[2]);
								String cur_rank = Permissions.getRank(null, target_player_name);
								int cur_priority = AdminGUI.getInstance().getPermissions().getInt("groups." + cur_rank + ".priority", AdminGUI.getInstance().getPermissions().getInt("groups.default.priority"));
								ArrayList<Integer> priorities = new ArrayList<>();
								for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
									int pri = AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority");
									if (cur_priority > pri) priorities.add(pri);
								}
								if (priorities.size() > 0) {
									int max_pri = Collections.max(priorities);
									for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
										if (AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority") == max_pri) {
											Permissions.setRank(null, target_player_name, priority.getKey());
											player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", target_player_name).replace("{rank}", priority.getKey()));
										}
									}
								} else {
									player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", target_player_name).replace("{rank}", cur_rank));
								}
							}
						} else {
							player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
						}
					} else if (args[1].equals("down")) {
						if (TargetPlayer.hasPermission(player, "admingui.rank.down")) {
							Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[2]));
							if (target_player != null) {
								String cur_rank = Permissions.getRank(target_player.getUniqueId(), target_player.getName());
								int cur_priority = AdminGUI.getInstance().getPermissions().getInt("groups." + cur_rank + ".priority", AdminGUI.getInstance().getPermissions().getInt("groups.default.priority"));
								ArrayList<Integer> priorities = new ArrayList<>();
								for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
									int pri = AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority");
									if (cur_priority < pri) priorities.add(pri);
								}
								if (priorities.size() > 0) {
									int min_pri = Collections.min(priorities);
									for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
										if (AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority") == min_pri) {
											Permissions.setRank(target_player.getUniqueId(), target_player.getName(), priority.getKey());
											TargetPlayer.refreshPlayerTabList(target_player);
											TargetPlayer.refreshPermissions(target_player);
											player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", priority.getKey()));
											break;
										}
									}
								} else {
									player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", cur_rank));
								}
							} else {
								String cur_rank = Permissions.getRank(null, ChatColor.stripColor(args[2]));
								int cur_priority = AdminGUI.getInstance().getPermissions().getInt("groups." + cur_rank + ".priority", AdminGUI.getInstance().getPermissions().getInt("groups.default.priority"));
								ArrayList<Integer> priorities = new ArrayList<>();
								for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
									int pri = AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority");
									if (cur_priority < pri) priorities.add(pri);
								}
								if (priorities.size() > 0) {
									int min_pri = Collections.min(priorities);
									for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
										if (AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority") == min_pri) {
											Permissions.setRank(null, ChatColor.stripColor(args[2]), priority.getKey());
											player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", ChatColor.stripColor(args[2])).replace("{rank}", priority.getKey()));
											break;
										}
									}
								} else {
									player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", ChatColor.stripColor(args[2])).replace("{rank}", cur_rank));
								}
							}
						} else {
							player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
						}
					} else {
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
					}
				} else {
					player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_arguments"));
				}
			} else if (args.length == 4) {
				if (args[0].equals("permission")){
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						player.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}

					if(!args[1].equals("add") && !args[1].equals("remove")){
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_permission_arguments"));
						return true;
					}

					if (!TargetPlayer.hasPermission(player, "admingui.permission.manage")) {
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
						return true;
					}

					String name = args[2];
					String permission = args[3];

					Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(name));
					if(target_player != null){
						if(args[1].equals("add")){
							Permissions.setPermission(target_player.getUniqueId(), name, permission);
							player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_permission_added").replace("{player}", name).replace("{permission}", permission));
						}else{
							Permissions.removePermission(target_player.getUniqueId(), name, permission);
							player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_permission_removed").replace("{player}", name).replace("{permission}", permission));
						}
						return true;
					}

					if(args[1].equals("add")){
						Permissions.setPermission(null, name, permission);
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_permission_added").replace("{player}", name).replace("{permission}", permission));
					}else{
						Permissions.removePermission(null, name, permission);
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_permission_removed").replace("{player}", name).replace("{permission}", permission));
					}
				} else if (args[0].equals("rank")) {
					if (!AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)) {
						player.sendMessage(Message.chat("&cAdmin Permissions are disabled on this server!"));
						return true;
					}
					if (args[1].equals("set")) {
						if (TargetPlayer.hasPermission(player, "admingui.rank.set")) {
							Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[2]));
							String rank = args[3];
							if (target_player != null) {
								if (Permissions.setRank(target_player.getUniqueId(), target_player.getName(), rank)) {
									TargetPlayer.refreshPlayerTabList(target_player);
									TargetPlayer.refreshPermissions(target_player);
									player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", rank));
								} else {
									player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
								}
							} else {
								if (Permissions.setRank(null, ChatColor.stripColor(args[2]), rank)) {
									player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", ChatColor.stripColor(args[2])).replace("{rank}", rank));
								} else {
									player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
								}
							}
						} else {
							player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
						}
					} else {
						player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
					}
				} else {
					player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_arguments"));
				}
			} else {
				player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_arguments"));
			}
		} else {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
		}
		return true;
	}
}
