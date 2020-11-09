package com.rabbitcompany.admingui.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminGUIPlaceholders extends PlaceholderExpansion {

    @Override
    public boolean canRegister(){
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
    public String onRequest(OfflinePlayer offlinePlayer, String identifier){

        if (offlinePlayer == null) return "";
        if (!offlinePlayer.isOnline()) return "";

        final Player player = offlinePlayer.getPlayer();

        switch (identifier){
            case "prefix":
                return Permissions.getPrefix(player.getUniqueId());
            case "suffix":
                return Permissions.getSuffix(player.getUniqueId());
            case "rank":
                return Permissions.getRank(player.getUniqueId());
            default:
                return null;
        }
    }
}
