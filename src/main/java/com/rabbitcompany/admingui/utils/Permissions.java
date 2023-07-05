package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Permissions {

	public static HashMap<UUID, String> cache_ranks = new HashMap<>();

	public static String getPrefix(UUID uuid) {
		String rank;

		if (AdminGUI.getInstance().getConf().getBoolean("mysql", false) && AdminGUI.getInstance().getConf().getInt("ap_storage_type", 0) == 2) {
			if (cache_ranks.getOrDefault(uuid, null) != null) {
				rank = cache_ranks.get(uuid);
			} else {
				rank = Database.cacheRank(uuid);
			}
		} else {
			rank = AdminGUI.getInstance().getPlayers().getString(uuid + ".rank", null);
		}

		if (rank == null) {
			return AdminGUI.getInstance().getPermissions().getString("groups.default.prefix", "");
		} else {
			return AdminGUI.getInstance().getPermissions().getString("groups." + rank + ".prefix", "");
		}
	}

	public static String getSuffix(UUID uuid) {
		String rank;

		if (AdminGUI.getInstance().getConf().getBoolean("mysql", false) && AdminGUI.getInstance().getConf().getInt("ap_storage_type", 0) == 2) {
			if (cache_ranks.getOrDefault(uuid, null) != null) {
				rank = cache_ranks.get(uuid);
			} else {
				rank = Database.cacheRank(uuid);
			}
		} else {
			rank = AdminGUI.getInstance().getPlayers().getString(uuid + ".rank", null);
		}

		if (rank == null) {
			return AdminGUI.getInstance().getPermissions().getString("groups.default.suffix", "");
		} else {
			return AdminGUI.getInstance().getPermissions().getString("groups." + rank + ".suffix", "");
		}
	}

	public static String getRank(UUID uuid, String name) {
		String rank = null;

		if (AdminGUI.getInstance().getConf().getBoolean("mysql", false) && AdminGUI.getInstance().getConf().getInt("ap_storage_type", 0) == 2) {

			if (cache_ranks.getOrDefault(uuid, null) != null) {
				rank = cache_ranks.get(uuid);
			} else {
				rank = Database.cacheRank(uuid);
			}

		} else {
			if (uuid != null) {
				rank = AdminGUI.getInstance().getPlayers().getString(uuid + ".rank", null);
			} else {
				Set<String> con_sec = AdminGUI.getInstance().getPlayers().getConfigurationSection("").getKeys(false);
				for (String uuid_name : con_sec) {
					if (AdminGUI.getInstance().getPlayers().getString(uuid_name + ".name").equals(name)) {
						rank = AdminGUI.getInstance().getPlayers().getString(uuid_name + ".rank", null);
						break;
					}
				}
			}
		}

		if (rank == null) {
			return "default";
		} else {
			return rank;
		}
	}

	public static boolean setPermission(UUID uuid, String name, String permission){
		if(uuid != null){
			List<String> permissions = AdminGUI.getInstance().getPlayers().getStringList(uuid + ".permissions");
			if(permissions.contains(permission)) return true;
			permissions.add(permission);
			AdminGUI.getInstance().getPlayers().set(uuid + ".permissions", permissions);
			AdminGUI.getInstance().getPlayers().set(uuid + ".name", name);
			AdminGUI.getInstance().savePlayers();

			Player target_player = Bukkit.getPlayer(uuid);
			if (target_player != null && target_player.isOnline()) {
				TargetPlayer.refreshPermissions(target_player);
			}
			return true;
		}

		boolean changed = false;
		Set<String> con_sec = AdminGUI.getInstance().getPlayers().getConfigurationSection("").getKeys(false);
		for (String uuid_name : con_sec) {
			if (AdminGUI.getInstance().getPlayers().getString(uuid_name + ".name").equals(name)) {
				List<String> permissions = AdminGUI.getInstance().getPlayers().getStringList(uuid_name + ".permissions");
				if(permissions.contains(permission)) return true;
				permissions.add(permission);
				AdminGUI.getInstance().getPlayers().set(uuid_name + ".permissions", permissions);
				AdminGUI.getInstance().getPlayers().set(uuid_name + ".name", name);
				changed = true;
				break;
			}
		}

		if(!changed){
			List<String> permissions = AdminGUI.getInstance().getPlayers().getStringList(name + ".permissions");
			if(permissions.contains(permission)) return true;
			permissions.add(permission);
			AdminGUI.getInstance().getPlayers().set(name + ".name", name);
			AdminGUI.getInstance().getPlayers().set(name + ".permissions", permissions);
		}

		AdminGUI.getInstance().savePlayers();

		Player target_player = Bukkit.getPlayer(name);
		if (target_player != null && target_player.isOnline()) {
			TargetPlayer.refreshPermissions(target_player);
		}
		return true;
	}

	public static boolean removePermission(UUID uuid, String name, String permission){
		if(uuid != null){
			List<String> permissions = AdminGUI.getInstance().getPlayers().getStringList(uuid + ".permissions");
			if(!permissions.contains(permission)) return true;
			permissions.remove(permission);
			AdminGUI.getInstance().getPlayers().set(uuid + ".permissions", permissions);
			AdminGUI.getInstance().getPlayers().set(uuid + ".name", name);
			AdminGUI.getInstance().savePlayers();

			Player target_player = Bukkit.getPlayer(uuid);
			if (target_player != null && target_player.isOnline()) {
				TargetPlayer.refreshPermissions(target_player);
			}
			return true;
		}

		boolean changed = false;
		Set<String> con_sec = AdminGUI.getInstance().getPlayers().getConfigurationSection("").getKeys(false);
		for (String uuid_name : con_sec) {
			if (AdminGUI.getInstance().getPlayers().getString(uuid_name + ".name").equals(name)) {
				List<String> permissions = AdminGUI.getInstance().getPlayers().getStringList(uuid_name + ".permissions");
				if(!permissions.contains(permission)) return true;
				permissions.remove(permission);
				AdminGUI.getInstance().getPlayers().set(uuid_name + ".permissions", permissions);
				AdminGUI.getInstance().getPlayers().set(uuid_name + ".name", name);
				changed = true;
				break;
			}
		}

		if(!changed){
			List<String> permissions = AdminGUI.getInstance().getPlayers().getStringList(name + ".permissions");
			if(!permissions.contains(permission)) return true;
			permissions.remove(permission);
			AdminGUI.getInstance().getPlayers().set(name + ".name", name);
			AdminGUI.getInstance().getPlayers().set(name + ".permissions", permissions);
		}

		AdminGUI.getInstance().savePlayers();

		Player target_player = Bukkit.getPlayer(name);
		if (target_player != null && target_player.isOnline()) {
			TargetPlayer.refreshPermissions(target_player);
		}
		return true;
	}

	public static boolean setRank(UUID uuid, String name, String rank) {
		if (AdminGUI.getInstance().getPermissions().getString("groups." + rank + ".prefix") != null) {
			if (AdminGUI.getInstance().getConf().getBoolean("mysql", false) && AdminGUI.getInstance().getConf().getInt("ap_storage_type", 0) == 2) {
				Database.setRank(uuid, name, rank);
				if (uuid != null) {
					Database.cacheRank(uuid);
					//Vault
					if (AdminGUI.getVaultChat() != null) {
						AdminGUI.getVaultChat().setPlayerPrefix(Bukkit.getPlayer(uuid), getPrefix(uuid));
						AdminGUI.getVaultChat().setPlayerSuffix(Bukkit.getPlayer(uuid), getSuffix(uuid));
					}
				}
			} else {
				if (uuid != null) {
					if (rank.equals("default")) {
						AdminGUI.getInstance().getPlayers().set(uuid + ".rank", null);
					} else {
						AdminGUI.getInstance().getPlayers().set(uuid + ".name", name);
						AdminGUI.getInstance().getPlayers().set(uuid + ".rank", rank);
					}

					if (AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false) && AdminGUI.getInstance().getConf().getInt("ap_storage_type", 0) == 1) {
						Channel.send("Console", "rank", uuid.toString(), name, rank);
					}

					//Vault
					if (AdminGUI.getVaultChat() != null) {
						AdminGUI.getVaultChat().setPlayerPrefix(Bukkit.getPlayer(uuid), getPrefix(uuid));
						AdminGUI.getVaultChat().setPlayerSuffix(Bukkit.getPlayer(uuid), getSuffix(uuid));
					}

				} else {
					boolean changed = false;
					Set<String> con_sec = AdminGUI.getInstance().getPlayers().getConfigurationSection("").getKeys(false);
					for (String uuid_name : con_sec) {
						if (AdminGUI.getInstance().getPlayers().getString(uuid_name + ".name").equals(name)) {
							AdminGUI.getInstance().getPlayers().set(uuid_name + ".name", name);
							AdminGUI.getInstance().getPlayers().set(uuid_name + ".rank", rank);
							changed = true;
							break;
						}
					}
					if (!changed) {
						AdminGUI.getInstance().getPlayers().set(name + ".name", name);
						AdminGUI.getInstance().getPlayers().set(name + ".rank", rank);
					}

					if (AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false) && AdminGUI.getInstance().getConf().getInt("ap_storage_type", 0) == 1) {
						Channel.send("Console", "rank", "null", name, rank);
					}
				}
				AdminGUI.getInstance().savePlayers();
			}
			return true;
		}
		return false;
	}

	public static boolean saveRank(UUID uuid, String name, String rank) {
		if (AdminGUI.getInstance().getPermissions().getString("groups." + rank + ".prefix") != null) {
			Player target_player;
			if (uuid != null) {
				if (rank.equals("default")) {
					AdminGUI.getInstance().getPlayers().set(uuid + ".rank", null);
				} else {
					AdminGUI.getInstance().getPlayers().set(uuid + ".name", name);
					AdminGUI.getInstance().getPlayers().set(uuid + ".rank", rank);
				}
				target_player = Bukkit.getPlayer(uuid);
			} else {
				target_player = Bukkit.getPlayer(name);
				if (target_player != null && target_player.isOnline()) {
					if (rank.equals("default")) {
						AdminGUI.getInstance().getPlayers().set(target_player.getUniqueId() + ".rank", null);
					} else {
						AdminGUI.getInstance().getPlayers().set(target_player.getUniqueId() + ".name", name);
						AdminGUI.getInstance().getPlayers().set(target_player.getUniqueId() + ".rank", rank);
					}
				} else {
					AdminGUI.getInstance().getPlayers().set(name + ".name", name);
					AdminGUI.getInstance().getPlayers().set(name + ".rank", rank);
				}
			}
			AdminGUI.getInstance().savePlayers();

			if (target_player != null && target_player.isOnline()) {
				TargetPlayer.refreshPermissions(target_player);
				TargetPlayer.refreshPlayerTabList(target_player);
			}

			return true;
		}
		return false;
	}
}
