package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class Permissions {

    public static String getPrefix(UUID uuid){
        String rank = AdminGUI.getInstance().getPermissions().getString("ranks." + uuid + ".rank", null);
        if(rank == null){
            return AdminGUI.getInstance().getPermissions().getString("groups.default.prefix", "");
        }else{
            return AdminGUI.getInstance().getPermissions().getString("groups." + rank + ".prefix", "");
        }
    }

    public static String getSuffix(UUID uuid){
        String rank = AdminGUI.getInstance().getPermissions().getString("ranks." + uuid + ".rank", null);
        if(rank == null){
            return AdminGUI.getInstance().getPermissions().getString("groups.default.suffix", "");
        }else{
            return AdminGUI.getInstance().getPermissions().getString("groups." + rank + ".suffix", "");
        }
    }

    public static String getRank(UUID uuid, String name){
        String rank = null;
        if(uuid != null){
            rank = AdminGUI.getInstance().getPermissions().getString("ranks." + uuid + ".rank", null);
        }else{
            Set<String> con_sec = AdminGUI.getInstance().getPermissions().getConfigurationSection("ranks").getKeys(false);
            for (String uuid_name : con_sec){
                if(AdminGUI.getInstance().getPermissions().getString("ranks." + uuid_name + ".name").equals(name)){
                    rank = AdminGUI.getInstance().getPermissions().getString("ranks." + uuid_name + ".rank", null);
                    break;
                }
            }
        }
        if(rank == null){
            return "default";
        }else{
            return rank;
        }
    }

    public static boolean setRank(UUID uuid, String name, String rank){
        if (AdminGUI.getInstance().getPermissions().getString("groups." + rank + ".prefix") != null) {

            if(uuid != null){
                if(rank.equals("default")){
                    AdminGUI.getInstance().getPermissions().set("ranks." + uuid, null);
                }else{
                    AdminGUI.getInstance().getPermissions().set("ranks." + uuid + ".name", name);
                    AdminGUI.getInstance().getPermissions().set("ranks." + uuid + ".rank", rank);
                }

                if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false) && AdminGUI.getInstance().getConf().getBoolean("bungeecord_rank_sync", true)){
                    Channel.send("Console", "rank", uuid.toString(), name, rank);
                }
            }else{
                boolean changed = false;
                Set<String> con_sec = AdminGUI.getInstance().getPermissions().getConfigurationSection("ranks").getKeys(false);
                for (String uuid_name : con_sec){
                    if(AdminGUI.getInstance().getPermissions().getString("ranks." + uuid_name + ".name").equals(name)){
                        AdminGUI.getInstance().getPermissions().set("ranks." + uuid_name + ".name", name);
                        AdminGUI.getInstance().getPermissions().set("ranks." + uuid_name + ".rank", rank);
                        changed = true;
                        break;
                    }
                }
                if(!changed){
                    AdminGUI.getInstance().getPermissions().set("ranks." + name + ".name", name);
                    AdminGUI.getInstance().getPermissions().set("ranks." + name + ".rank", rank);
                }

                if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false) && AdminGUI.getInstance().getConf().getBoolean("bungeecord_rank_sync", true)){
                    Channel.send("Console", "rank", "null", name, rank);
                }
            }
            AdminGUI.getInstance().savePermissions();
            return true;
        }
        return false;
    }

    public static boolean saveRank(UUID uuid, String name, String rank){
        if (AdminGUI.getInstance().getPermissions().getString("groups." + rank + ".prefix") != null) {
            Player target_player;
            if(uuid != null){
                if(rank.equals("default")){
                    AdminGUI.getInstance().getPermissions().set("ranks." + uuid, null);
                }else{
                    AdminGUI.getInstance().getPermissions().set("ranks." + uuid + ".name", name);
                    AdminGUI.getInstance().getPermissions().set("ranks." + uuid + ".rank", rank);
                }
                target_player = Bukkit.getPlayer(uuid);
            }else{
                target_player = Bukkit.getPlayer(name);
                if(target_player != null && target_player.isOnline()){
                    if(rank.equals("default")){
                        AdminGUI.getInstance().getPermissions().set("ranks." + target_player.getUniqueId(), null);
                    }else{
                        AdminGUI.getInstance().getPermissions().set("ranks." + target_player.getUniqueId() + ".name", name);
                        AdminGUI.getInstance().getPermissions().set("ranks." + target_player.getUniqueId() + ".rank", rank);
                    }
                }else{
                    AdminGUI.getInstance().getPermissions().set("ranks." + name + ".name", name);
                    AdminGUI.getInstance().getPermissions().set("ranks." + name + ".rank", rank);
                }
            }
            AdminGUI.getInstance().savePermissions();

            if(target_player != null && target_player.isOnline()){
                TargetPlayer.refreshPermissions(target_player);
                TargetPlayer.refreshPlayerTabList(target_player);
            }

            return true;
        }
        return false;
    }
}
