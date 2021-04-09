package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.XMaterial;
import com.rabbitcompany.admingui.ui.AdminUI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class TargetPlayer {

    public void setPotionEffect(org.bukkit.entity.Player p, org.bukkit.entity.Player target_player, PotionEffectType potion, String getPotionConfigName, int duration, int level){
        if(target_player.hasPotionEffect(potion)) target_player.removePotionEffect(potion);

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

    public static void givePermissions(Player player){
        String rank = Permissions.getRank(player.getUniqueId(), player.getName());

        if(Settings.permissions.containsKey(player.getUniqueId())) return;

        Settings.permissions.put(player.getUniqueId(), player.addAttachment(AdminGUI.getInstance()));

        List<?> permissions;
        List<?> inheritance = AdminGUI.getInstance().getPermissions().getList("groups." + rank + ".inheritance");

        if(inheritance != null){
            for (Object inter : inheritance) {
                permissions = AdminGUI.getInstance().getPermissions().getList("groups." + inter + ".permissions");
                if(permissions != null){
                    for (Object permission: permissions) {
                        if(permission.toString().charAt(0) == '!'){
                            Settings.permissions.get(player.getUniqueId()).unsetPermission(permission.toString().substring(1));
                        }else{
                            Settings.permissions.get(player.getUniqueId()).setPermission(permission.toString(), true);
                        }
                    }
                }
            }
        }

        permissions = AdminGUI.getInstance().getPermissions().getList("groups." + rank + ".permissions");

        if(permissions != null){
            for (Object permission: permissions) {
                if(permission.toString().charAt(0) == '!'){
                    Settings.permissions.get(player.getUniqueId()).unsetPermission(permission.toString().substring(1));
                }else{
                    Settings.permissions.get(player.getUniqueId()).setPermission(permission.toString(), true);
                }
            }
        }

    }

    public static void refreshPermissions(Player player){
        String rank = Permissions.getRank(player.getUniqueId(), player.getName());

        List<?> permissions;
        List<?> inheritance = AdminGUI.getInstance().getPermissions().getList("groups." + rank + ".inheritance");

        for(String perm : Settings.permissions.get(player.getUniqueId()).getPermissions().keySet())
            Settings.permissions.get(player.getUniqueId()).unsetPermission(perm);

        if(inheritance != null){
            for (Object inter : inheritance) {
                permissions = AdminGUI.getInstance().getPermissions().getList("groups." + inter + ".permissions");
                if(permissions != null){
                    for (Object permission: permissions) {
                        if(permission.toString().charAt(0) == '!'){
                            Settings.permissions.get(player.getUniqueId()).unsetPermission(permission.toString().substring(1));
                        }else{
                            Settings.permissions.get(player.getUniqueId()).setPermission(permission.toString(), true);
                        }
                    }
                }
            }
        }

        permissions = AdminGUI.getInstance().getPermissions().getList("groups." + rank + ".permissions");

        if(permissions != null){
            for (Object permission: permissions) {
                if(permission.toString().charAt(0) == '!'){
                    Settings.permissions.get(player.getUniqueId()).unsetPermission(permission.toString().substring(1));
                }else{
                    Settings.permissions.get(player.getUniqueId()).setPermission(permission.toString(), true);
                }
            }
        }

    }

    public static void removePermissions(Player player){
        if(Settings.permissions.containsKey(player.getUniqueId())){
            try{
                player.removeAttachment(Settings.permissions.get(player.getUniqueId()));
            }catch (IllegalArgumentException ignored){}
            Settings.permissions.remove(player.getUniqueId());
        }
    }

    public static void refreshPlayerTabList(Player player){

        String prefix = "";
        String suffix = "";
        if(AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)){
            String rank = Permissions.getRank(player.getUniqueId(), player.getName());
            prefix = AdminGUI.getInstance().getPermissions().getString("groups." + rank + ".prefix", "");
            suffix = AdminGUI.getInstance().getPermissions().getString("groups." + rank + ".suffix", "");
        }

        String vault_prefix = "";
        String vault_suffix = "";
        if(AdminGUI.getVaultChat() != null){
            vault_prefix = AdminGUI.getVaultChat().getPlayerPrefix(player);
            vault_suffix = AdminGUI.getVaultChat().getPlayerSuffix(player);
        }

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            if(!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12")){
                player.setPlayerListHeader(Colors.toHex(PlaceholderAPI.setPlaceholders(player, Message.chat(AdminGUI.getInstance().getConf().getString("atl_header", "&a{display_name}").replace("{name}", player.getName()).replace("{display_name}", player.getDisplayName()).replace("{prefix}", prefix).replace("{suffix}", suffix).replace("{vault_prefix}", vault_prefix).replace("{vault_suffix}", vault_suffix)))));
                player.setPlayerListFooter(Colors.toHex(PlaceholderAPI.setPlaceholders(player, Message.chat(AdminGUI.getInstance().getConf().getString("atl_footer", "&a{display_name}").replace("{name}", player.getName()).replace("{display_name}", player.getDisplayName()).replace("{prefix}", prefix).replace("{suffix}", suffix).replace("{vault_prefix}", vault_prefix).replace("{vault_suffix}", vault_suffix)))));
            }
            player.setPlayerListName(Colors.toHex(PlaceholderAPI.setPlaceholders(player, Message.chat(AdminGUI.getInstance().getConf().getString("atl_format", "&a{display_name}").replace("{name}", player.getName()).replace("{display_name}", player.getDisplayName()).replace("{prefix}", prefix).replace("{suffix}", suffix).replace("{vault_prefix}", vault_prefix).replace("{vault_suffix}", vault_suffix)))));
        }else{
            if(!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9") && !Bukkit.getVersion().contains("1.10") && !Bukkit.getVersion().contains("1.11") && !Bukkit.getVersion().contains("1.12")) {
                player.setPlayerListHeader(Colors.toHex(Message.chat(AdminGUI.getInstance().getConf().getString("atl_header", "&a{display_name}").replace("{name}", player.getName()).replace("{display_name}", player.getDisplayName()).replace("{prefix}", prefix).replace("{suffix}", suffix).replace("{vault_prefix}", vault_prefix).replace("{vault_suffix}", vault_suffix))));
                player.setPlayerListFooter(Colors.toHex(Message.chat(AdminGUI.getInstance().getConf().getString("atl_footer", "&a{display_name}").replace("{name}", player.getName()).replace("{display_name}", player.getDisplayName()).replace("{prefix}", prefix).replace("{suffix}", suffix).replace("{vault_prefix}", vault_prefix).replace("{vault_suffix}", vault_suffix))));
            }
            player.setPlayerListName(Colors.toHex(Message.chat(AdminGUI.getInstance().getConf().getString("atl_format", "&a{display_name}").replace("{name}", player.getName()).replace("{display_name}", player.getDisplayName()).replace("{prefix}", prefix).replace("{suffix}", suffix).replace("{vault_prefix}", vault_prefix).replace("{vault_suffix}", vault_suffix))));
        }
    }

    public static boolean hasPermission(Player player, String permission){

        if(player.isOp() || player.hasPermission(permission)) return true;

        String[] sections = permission.split("\\.");

        for(int i = 0; i < sections.length; i++){
            StringBuilder perm = new StringBuilder();
            for(int j = 0; j < i; j++) perm.append(sections[j]).append(".");
            perm.append("*");
            if(player.hasPermission(perm.toString())) return true;
        }
        return false;
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
                Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(target, banReason(target_uuid, reason, Settings.date_format.format(expired)), expired, null);
                if(target_player != null)
                    target_player.kickPlayer(Message.getMessage(target_player.getUniqueId(), "prefix") + TargetPlayer.banReason(target_player.getUniqueId(), reason, Settings.date_format.format(expired)));
            }
        }

    }

    public static void giveAdminTools(Player player, int slot){
        List<String> lore = Collections.singletonList(Message.chat(AdminGUI.getInstance().getConf().getString("admin_tools_lore", "&dClick me to open Admin GUI")));
        ItemStack item = new ItemStack(XMaterial.matchXMaterial(AdminGUI.getInstance().getConf().getString("admin_tools_material", "NETHER_STAR")).get().parseMaterial(), 1, XMaterial.matchXMaterial(AdminGUI.getInstance().getConf().getString("admin_tools_material", "NETHER_STAR")).get().getData());

        if(AdminGUI.getInstance().getConf().getBoolean("admin_tools_enchantment", false)){
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Message.chat(AdminGUI.getInstance().getConf().getString("admin_tools_name", "&c&lAdmin Tools")));
        meta.setLore(lore);

        item.setItemMeta(meta);

        player.getInventory().setItem(slot, item);
    }

    public static boolean safeTeleport(Player player){
        Random rnd = new Random();

        int min_x = AdminGUI.getInstance().getConf().getInt("rtp_min_x", -1000);
        int max_x = AdminGUI.getInstance().getConf().getInt("rtp_max_x", 1000);
        int min_z = AdminGUI.getInstance().getConf().getInt("rtp_min_z", -1000);
        int max_z = AdminGUI.getInstance().getConf().getInt("rtp_max_z", 1000);

        int x;
        int z;
        int y;
        int count = 0;

        do{
            x = rnd.nextInt((max_x - min_x) + 1) + min_x;
            z = rnd.nextInt((max_z - min_z) + 1) + min_z;
            y = player.getWorld().getHighestBlockYAt(x, z)+1;
            if(count >= AdminGUI.getInstance().getConf().getInt("rtp_attempts", 20)) return false;
            count++;
        }while (!isSafeLocation(new Location(player.getWorld(), x+0.5, y, z+0.5)));

        player.teleport(new Location(player.getWorld(), x+0.5, y ,z+0.5));
        return true;
    }

    public static boolean isSafeLocation(Location location) {
        Block feet = location.getBlock();
        if (!feet.getType().isAir() || !feet.getRelative(BlockFace.UP).getType().isAir()) return false;
        if (!feet.getRelative(BlockFace.DOWN).getType().isSolid() && !feet.getRelative(BlockFace.DOWN).getLocation().subtract(0, 1, 0).getBlock().getType().isSolid()) return false;
        return true;
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
