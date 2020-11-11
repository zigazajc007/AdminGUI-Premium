package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.ui.AdminUI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminGUIPlaceholders extends PlaceholderExpansion {

    @Override
    public boolean canRegister(){ return true; }

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
    public String onRequest(OfflinePlayer offlinePlayer, String identifier){

        if (offlinePlayer == null) return "";
        if (!offlinePlayer.isOnline()) return "";

        final Player player = offlinePlayer.getPlayer();

        switch (identifier){
            case "player_prefix":
                return Permissions.getPrefix(player.getUniqueId());
            case "player_suffix":
                return Permissions.getSuffix(player.getUniqueId());
            case "player_rank":
                return Permissions.getRank(player.getUniqueId(), player.getName());
            case "player_chat_channel":
                return AdminUI.custom_chat_channel.getOrDefault(player.getUniqueId(), "default");
            case "is_chat_muted":
                return String.valueOf(AdminUI.muted_chat);
            case "is_player_god":
                if(Bukkit.getVersion().contains("1.8")) return String.valueOf(AdminUI.god.getOrDefault(player.getUniqueId(), false));
                return String.valueOf(player.isInvulnerable());
            case "is_player_frozen":
                return String.valueOf(AdminUI.freeze.getOrDefault(player.getUniqueId(), false));
            case "is_maintenance_mode":
                return String.valueOf(AdminUI.maintenance_mode);
            default:
                return null;
        }
    }
}
