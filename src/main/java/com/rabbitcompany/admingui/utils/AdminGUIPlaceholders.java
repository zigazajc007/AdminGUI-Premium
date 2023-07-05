package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class AdminGUIPlaceholders extends PlaceholderExpansion {

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "admingui";
	}

	@Override
	public @NotNull String getAuthor() {
		return "Black1_TV";
	}

	@Override
	public @NotNull String getVersion() {
		return "1.0.0";
	}

	@Override
	public String onRequest(OfflinePlayer offlinePlayer, String identifier) {

		if (offlinePlayer == null) return "";
		if (!offlinePlayer.isOnline()) return "";

		final Player player = offlinePlayer.getPlayer();

		switch (identifier) {
			case "player_prefix":
				return Permissions.getPrefix(player.getUniqueId());
			case "player_suffix":
				return Permissions.getSuffix(player.getUniqueId());
			case "player_rank":
				return Permissions.getRank(player.getUniqueId(), player.getName());
			case "player_chat_channel":
				return Settings.custom_chat_channel.getOrDefault(player.getUniqueId(), "default");
			case "is_chat_muted":
				return String.valueOf(Settings.muted_chat);
			case "is_player_god":
				if (Bukkit.getVersion().contains("1.8"))
					return String.valueOf(Settings.god.getOrDefault(player.getUniqueId(), false));
				return String.valueOf(player.isInvulnerable());
			case "is_player_frozen":
				return String.valueOf(Settings.freeze.getOrDefault(player.getUniqueId(), false));
			case "is_maintenance_mode":
				return String.valueOf(Settings.maintenance_mode);
			case "player_first_join":
				return Database.date_format.format(new Date(AdminGUI.getInstance().getPlayers().getLong(player.getUniqueId() + ".firstJoin")));
			case "player_last_join":
				return Database.date_format.format(new Date(AdminGUI.getInstance().getPlayers().getLong(player.getUniqueId() + ".lastJoin")));
			case "player_ips":
				StringBuilder ips = new StringBuilder();
				for (String ip : AdminGUI.getInstance().getPlayers().getStringList(player.getUniqueId() + ".ips")) {
					ips.append(ip).append(", ");
				}
				ips.delete(ips.length() - 2, ips.length());
				return ips.toString();
			default:
				return null;
		}
	}
}
