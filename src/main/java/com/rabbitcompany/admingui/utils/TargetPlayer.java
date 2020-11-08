package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class TargetPlayer {

    public void setPotionEffect(org.bukkit.entity.Player p, org.bukkit.entity.Player target_player, PotionEffectType potion, String getPotionConfigName, int duration, int level){
        if(target_player.hasPotionEffect(potion)){
            target_player.removePotionEffect(potion);
        }
        target_player.addPotionEffect(new PotionEffect(potion, duration*1200, level-1));
        if(duration == 1000000){
            if(p.getName().equals(target_player.getName())){
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_potions").replace("{potion}", Message.getMessage(p.getUniqueId(), getPotionConfigName)).replace("{time}", "∞"));
            }else{
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_potions").replace("{player}", target_player.getName()).replace("{potion}", Message.getMessage(p.getUniqueId(), getPotionConfigName)).replace("{time}", "∞"));
                target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_potions").replace("{player}", p.getName()).replace("{potion}", Message.getMessage(target_player.getUniqueId(), getPotionConfigName)).replace("{time}", "∞"));
            }
        }else {
            if (p.getName().equals(target_player.getName())) {
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_potions").replace("{potion}", Message.getMessage(p.getUniqueId(), getPotionConfigName)).replace("{time}", "" + duration));
            } else {
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_potions").replace("{player}", target_player.getName()).replace("{potion}", Message.getMessage(p.getUniqueId(), getPotionConfigName)).replace("{time}", "" + duration));
                target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_potions").replace("{player}", p.getName()).replace("{potion}", Message.getMessage(target_player.getUniqueId(), getPotionConfigName)).replace("{time}", "" + duration));
            }
        }
    }

    public static void refreshPermissions(Player player){
        String rank = AdminGUI.getInstance().getPermissions().getString("ranks."+player.getUniqueId().toString(), null);

        AdminUI.permissions.get(player.getUniqueId()).remove();
        AdminUI.permissions.put(player.getUniqueId(), player.addAttachment(AdminGUI.getInstance()));
        PermissionAttachment permissionAttachment = AdminUI.permissions.get(player.getUniqueId());

        List<?> permissions;
        List<?> inheritance = null;
        if(rank == null){
            permissions = AdminGUI.getInstance().getPermissions().getList("groups.default.permissions");
        }else{
            permissions = AdminGUI.getInstance().getPermissions().getList("groups." + rank + ".permissions");
            inheritance = AdminGUI.getInstance().getPermissions().getList("groups." + rank + ".inheritance");
        }

        for (Object permission: permissions) {
            permissionAttachment.setPermission(permission.toString(), true);
        }

        if(inheritance != null){
            for (Object inter : inheritance) {
                permissions = AdminGUI.getInstance().getPermissions().getList("groups." + inter + ".permissions");
                for (Object permission: permissions) {
                    permissionAttachment.setPermission(permission.toString(), true);
                }
            }
        }
    }

    public static void refreshPlayerTabList(Player player){

        String prefix = "";
        String suffix = "";
        if(AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)){
            String rank = AdminGUI.getInstance().getPermissions().getString("ranks." + player.getUniqueId().toString(), null);
            if(rank == null){
                prefix = AdminGUI.getInstance().getPermissions().getString("groups.default.prefix", "");
                suffix = AdminGUI.getInstance().getPermissions().getString("groups.default.suffix", "");
            }else{
                prefix = AdminGUI.getInstance().getPermissions().getString("groups." + rank + ".prefix", "");
                suffix = AdminGUI.getInstance().getPermissions().getString("groups." + rank + ".suffix", "");
            }
        }

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            player.setPlayerListHeader(PlaceholderAPI.setPlaceholders(player, Message.chat(AdminGUI.getInstance().getConf().getString("atl_header", "&a{display_name}").replace("{name}", player.getName()).replace("{display_name}", player.getDisplayName()).replace("{prefix}", prefix).replace("{suffix}", suffix))));
            player.setPlayerListName(PlaceholderAPI.setPlaceholders(player, Message.chat(AdminGUI.getInstance().getConf().getString("atl_format", "&a{display_name}").replace("{name}", player.getName()).replace("{display_name}", player.getDisplayName()).replace("{prefix}", prefix).replace("{suffix}", suffix))));
            player.setPlayerListFooter(PlaceholderAPI.setPlaceholders(player, Message.chat(AdminGUI.getInstance().getConf().getString("atl_footer", "&a{display_name}").replace("{name}", player.getName()).replace("{display_name}", player.getDisplayName()).replace("{prefix}", prefix).replace("{suffix}", suffix))));
        }else{
            player.setPlayerListHeader(Message.chat(AdminGUI.getInstance().getConf().getString("atl_header", "&a{display_name}").replace("{name}", player.getName()).replace("{display_name}", player.getDisplayName()).replace("{prefix}", prefix).replace("{suffix}", suffix)));
            player.setPlayerListName(Message.chat(AdminGUI.getInstance().getConf().getString("atl_format", "&a{display_name}").replace("{name}", player.getName()).replace("{display_name}", player.getDisplayName()).replace("{prefix}", prefix).replace("{suffix}", suffix)));
            player.setPlayerListFooter(Message.chat(AdminGUI.getInstance().getConf().getString("atl_footer", "&a{display_name}").replace("{name}", player.getName()).replace("{display_name}", player.getDisplayName()).replace("{prefix}", prefix).replace("{suffix}", suffix)));
        }
    }

    public static void ban(UUID player_uuid, String player_name, UUID target_uuid, String target, String reason, Date expired){
        if(getServer().getPluginManager().getPlugin("AdminBans") != null && getServer().getPluginManager().isPluginEnabled("AdminBans")){
            if(expired == null){
                AdminBansAPI.banPlayer(player_uuid.toString(), player_name, target_uuid.toString(), target, reason, null);
            }else{
                String until = AdminBansAPI.date_format.format(expired);
                AdminBansAPI.banPlayer(player_uuid.toString(), player_name, target_uuid.toString(), target, reason, until);
            }
        }else if(getServer().getPluginManager().getPlugin("LiteBans") != null || getServer().getPluginManager().getPlugin("AdvancedBan") != null) {
            if(expired == null){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + target + " " + reason);
            }else{
                Date date = new Date(System.currentTimeMillis());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban " + target + " " + ((expired.getTime() - date.getTime())/60000) + "m " + reason);
            }
        }else{
            Player target_player = Bukkit.getPlayer(target_uuid);
            if(expired == null){
                Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(target, permBanReason(target_uuid, reason), null, null);
                if(target_player != null)
                    target_player.kickPlayer(Message.getMessage(target_player.getUniqueId(), "prefix") + TargetPlayer.permBanReason(target_player.getUniqueId(), reason));
            }else{
                Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(target, banReason(target_uuid, reason, AdminUI.date_format.format(expired)), expired, null);
                if(target_player != null)
                    target_player.kickPlayer(Message.getMessage(target_player.getUniqueId(), "prefix") + TargetPlayer.banReason(target_player.getUniqueId(), reason, AdminUI.date_format.format(expired)));
            }
        }

    }

    public static String banReason(UUID target, String reason, String time){
        String bumper = org.apache.commons.lang.StringUtils.repeat("\n", 35);

        return bumper + Message.getMessage(target, "ban_time").replace("{reason}", reason).replace("{time}", time) + bumper;
    }

    public static String permBanReason(UUID target, String reason){
        String bumper = org.apache.commons.lang.StringUtils.repeat("\n", 35);

        return bumper + Message.getMessage(target, "ban_perm").replace("{reason}", reason) + bumper;
    }

}
