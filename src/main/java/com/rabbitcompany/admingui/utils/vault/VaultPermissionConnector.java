package com.rabbitcompany.admingui.utils.vault;

import com.rabbitcompany.admingui.utils.Permissions;
import com.rabbitcompany.admingui.utils.TargetPlayer;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VaultPermissionConnector extends Permission {

	@Override
	public String getName() {
		return "AdminGUI-Premium";
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean hasSuperPermsCompat() {
		return false;
	}

	@Override
	public boolean playerHas(String world, String player, String permission) {
		Player p = Bukkit.getPlayer(player);
		if (p != null) return TargetPlayer.hasPermission(p, permission);
		return false;
	}

	@Override
	public boolean playerAdd(String world, String player, String permission) {
		return false;
	}

	@Override
	public boolean playerRemove(String world, String player, String permission) {
		return false;
	}

	@Override
	public boolean groupHas(String world, String group, String permission) {
		return false;
	}

	@Override
	public boolean groupAdd(String world, String group, String permission) {
		return false;
	}

	@Override
	public boolean groupRemove(String world, String group, String permission) {
		return false;
	}

	@Override
	public boolean playerInGroup(String world, String player, String group) {
		Player p = Bukkit.getPlayer(player);
		if (p != null) return Permissions.getRank(p.getUniqueId(), p.getName()).equals(group);
		return false;
	}

	@Override
	public boolean playerAddGroup(String world, String player, String group) {
		Player p = Bukkit.getPlayer(player);
		if (p != null) return Permissions.setRank(p.getUniqueId(), p.getName(), group);
		return false;
	}

	@Override
	public boolean playerRemoveGroup(String world, String player, String group) {
		Player p = Bukkit.getPlayer(player);
		if (p != null) return Permissions.setRank(p.getUniqueId(), p.getName(), "default");
		return false;
	}

	@Override
	public String[] getPlayerGroups(String world, String player) {
		Player p = Bukkit.getPlayer(player);
		if (p != null) return new String[]{Permissions.getRank(p.getUniqueId(), p.getName())};
		return new String[0];
	}

	@Override
	public String getPrimaryGroup(String world, String player) {
		Player p = Bukkit.getPlayer(player);
		if (p != null) return Permissions.getRank(p.getUniqueId(), p.getName());
		return null;
	}

	@Override
	public String[] getGroups() {
		return new String[0];
	}

	@Override
	public boolean hasGroupSupport() {
		return false;
	}
}
