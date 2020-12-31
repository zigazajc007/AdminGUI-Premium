package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.XMaterial;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class Admin implements CommandExecutor {

    private final AdminUI adminUI = new AdminUI();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(!(sender instanceof Player)) {

            if(args.length == 1) {
                if (args[0].equals("reload")) {
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_reload_start"));
                    AdminGUI.getInstance().mkdir();
                    AdminGUI.getInstance().loadYamls();
                    Language.getLanguages();
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_reload_finish"));
                }else if(args[0].equals("language")){
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cYou can only use /admin language download <language> or /admin language fix <language>"));
                }else if(args[0].equals("check")){
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_check_arguments"));
                }else if(args[0].equals("maintenance")){
                    if(AdminUI.maintenance_mode){
                        AdminUI.maintenance_mode = false;
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance_disabled"));
                    }else{
                        AdminUI.maintenance_mode = true;
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance_enabled"));
                        for (Player pl : getServer().getOnlinePlayers()) {
                            if (!pl.isOp() && !pl.hasPermission("admingui.maintenance")) {
                                pl.kickPlayer(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance"));
                            }
                        }
                    }
                }
            }else if(args.length == 2){
                if(args[0].equals("rank")) {
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_rank_arguments"));
                }else if(args[0].equals("language")) {
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cYou can only use /admin language download <language> or /admin language fix <language>"));
                }else if(args[0].equals("maintenance")){
                    String enabled = ChatColor.stripColor(args[1]);
                    if(enabled.equals("on") || enabled.equals("enable")){
                        if(!AdminUI.maintenance_mode){
                            AdminUI.maintenance_mode = true;
                            for (Player pl : getServer().getOnlinePlayers()) {
                                if (!pl.isOp() && !pl.hasPermission("admingui.maintenance")) {
                                    pl.kickPlayer(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance"));
                                }
                            }
                        }
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance_enabled"));
                    }else if(enabled.equals("off") || enabled.equals("disable")){
                        AdminUI.maintenance_mode = false;
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance_disabled"));
                    }else{
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cYou can only use /admin maintenance <on/off>"));
                    }
                }else if(args[0].equals("check")){
                    String name = ChatColor.stripColor(args[1]);
                    Set<String> con_sec = AdminGUI.getInstance().getPlayers().getConfigurationSection("").getKeys(false);
                    for (String uuid_name : con_sec){
                        if(AdminGUI.getInstance().getPlayers().getString(uuid_name + ".name").equals(name)){
                            sender.sendMessage(Message.chat("&9"+ name + " stats:"));
                            sender.sendMessage(Message.chat("  &9UUID: &b" + uuid_name));
                            if(AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)){
                                sender.sendMessage(Message.chat("  &9Rank: &b" + AdminGUI.getInstance().getPlayers().getString(uuid_name+ ".rank", "default")));
                            }
                            StringBuilder ips = new StringBuilder();
                            for (String ip: AdminGUI.getInstance().getPlayers().getStringList(uuid_name + ".ips")) {
                                ips.append(ip).append(", ");
                            }
                            ips.delete(ips.length()-2, ips.length());
                            sender.sendMessage(Message.chat("  &9IPs: &b" + ips.toString()));
                            sender.sendMessage(Message.chat("  &9Last Join: &b" + Database.date_format.format(new Date(AdminGUI.getInstance().getPlayers().getLong(uuid_name + ".lastJoin")))));
                            sender.sendMessage(Message.chat("  &9First Join: &b" + Database.date_format.format(new Date(AdminGUI.getInstance().getPlayers().getLong(uuid_name + ".firstJoin")))));
                            break;
                        }
                    }
                }
            }else if(args.length == 3){
                if(args[0].equals("rank")) {
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
                                }else{
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
                                }else{
                                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", ChatColor.stripColor(args[2])).replace("{rank}", cur_rank));
                                }
                            }
                    } else {
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_rank_arguments"));
                    }
                }else if(args[0].equals("language")){
                    if(args[1].equals("download")){
                        if(Language.downloadLanguage(args[2])){
                            sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&a"+args[2] + " language has been downloaded."));
                        }else{
                            sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&c"+args[2] + " language don't exists. Please check list of available languages."));
                        }
                    }else if(args[1].equals("fix")){
                        if(Language.fixLanguage(args[2])){
                            sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&a"+args[2] + " language has been fixed."));
                        }else{
                            sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&c"+args[2] + ".yml don't exists. Please download it first."));
                        }
                    }else{
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cYou can only use /admin language download <language> or /admin language fix <language>"));
                    }
                }else{
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_arguments"));
                }
            }else if(args.length == 4){
                if(args[0].equals("rank")){
                    if(args[1].equals("set")) {
                        Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[2]));
                        String rank = args[3];
                        if (target_player != null) {
                            if(Permissions.setRank(target_player.getUniqueId(), target_player.getName(), rank)){
                                TargetPlayer.refreshPlayerTabList(target_player);
                                TargetPlayer.refreshPermissions(target_player);
                                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", rank));
                            }else{
                                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_rank_arguments"));
                            }
                        } else {
                            if(Permissions.setRank(null, ChatColor.stripColor(args[2]), rank)){
                                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", ChatColor.stripColor(args[2])).replace("{rank}", rank));
                            }else{
                                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_rank_arguments"));
                            }
                        }
                    }else{
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_rank_arguments"));
                    }
                }else{
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_arguments"));
                }
            }else{
                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cYou can only use admin GUI in game."));
            }

            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission("admingui.admin")){

            //TODO: Bungee
            Channel.send(player.getName(),"send", "online_players");

            if(args.length == 0){
                AdminUI.target_player.put(player, player);
                player.openInventory(adminUI.GUI_Main(player));
            }else if(args.length == 1){
                if(args[0].equals("reload")) {
                    if(player.hasPermission("admingui.reload")){
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_reload_start"));
                        AdminGUI.getInstance().mkdir();
                        AdminGUI.getInstance().loadYamls();
                        Language.getLanguages();
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_reload_finish"));
                    }else{
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
                    }
                }else if(args[0].equals("initialize")) {
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_initialize"));
                }else if(args[0].equals("rank")) {
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
                }else if(args[0].equals("check")) {
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_check_arguments"));
                }else if(args[0].equals("maintenance")){
                    if(player.hasPermission("admingui.maintenance.manage")){
                        if(AdminUI.maintenance_mode){
                            AdminUI.maintenance_mode = false;
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance_disabled"));
                        }else{
                            AdminUI.maintenance_mode = true;
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance_enabled"));
                            for (Player pl : getServer().getOnlinePlayers()) {
                                if (!pl.isOp() && !pl.hasPermission("admingui.maintenance")) {
                                    pl.kickPlayer(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance"));
                                }
                            }
                        }
                    }else {
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
                    }
                }else if(args[0].equals("tools") || args[0].equals("tool")){
                    if(player.hasPermission("admingui.admin") && AdminGUI.getInstance().getConf().getBoolean("admin_tools_enabled", true)){
                        List<String> lore = Collections.singletonList(Message.chat(AdminGUI.getInstance().getConf().getString("admin_tools_lore", "&dClick me to open Admin GUI")));
                        ItemStack item = new ItemStack(XMaterial.matchXMaterial(AdminGUI.getInstance().getConf().getString("admin_tools_material", "NETHER_STAR")).get().parseMaterial(), 1, XMaterial.matchXMaterial(AdminGUI.getInstance().getConf().getString("admin_tools_material", "NETHER_STAR")).get().getData());

                        if(AdminGUI.getInstance().getConf().getBoolean("admin_tools_enchantment", false)){
                            item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
                        }

                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(Message.chat(AdminGUI.getInstance().getConf().getString("admin_tools_name", "&c&lAdmin Tools")));
                        meta.setLore(lore);

                        item.setItemMeta(meta);

                        player.getInventory().setItem(0, item);
                    }else{
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
                    }
                }else{
                    Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[0]));
                    if(target_player != null){
                        AdminUI.target_player.put(player, target_player);
                        if(player.getName().equals(target_player.getName())){
                            player.openInventory(adminUI.GUI_Player(player));
                        }else{
                            player.openInventory(adminUI.GUI_Players_Settings(player, target_player, target_player.getName()));
                        }
                    }else if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false) && AdminUI.online_players.contains(ChatColor.stripColor(args[0]))){
                        //TODO: Bungee
                        switch(AdminGUI.getInstance().getConf().getInt("control_type", 0)){
                            case 0:
                                Channel.send(player.getName(),"connect", ChatColor.stripColor(args[0]));
                                break;
                            case 1:
                                AdminUI.target_player.put(player, null);
                                player.openInventory(adminUI.GUI_Players_Settings(player,null, ChatColor.stripColor(args[0])));
                                break;
                            default:
                                player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.chat("&cPlayer " + ChatColor.stripColor(args[0]) + " is not located in the same server as you."));
                                break;
                        }
                    }else{
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "is_not_a_player").replace("{player}", args[0]));
                    }
                }
            }else if(args.length == 2){
                if(args[0].equals("initialize")){
                    if(args[1].equals("gui")){
                        if(!AdminUI.task_gui.containsKey(player.getUniqueId()) && !AdminUI.task_players.containsKey(player.getUniqueId())){
                            Initialize.GUI(player, player.getInventory().getHelmet());
                        }else{
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_already_initializing"));
                        }
                    }else if(args[1].equals("players")){
                        if(!AdminUI.task_gui.containsKey(player.getUniqueId()) && !AdminUI.task_players.containsKey(player.getUniqueId())){
                            Initialize.Players(player, player.getInventory().getHelmet());
                        }else{
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_already_initializing"));
                        }
                    }else{
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_initialize"));
                    }
                }else if(args[0].equals("rank")) {
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
                }else if(args[0].equals("maintenance")){
                    if(player.hasPermission("admingui.maintenance.manage")){
                        String enabled = ChatColor.stripColor(args[1]);
                        if(enabled.equals("on") || enabled.equals("enable")){
                            if(!AdminUI.maintenance_mode){
                                AdminUI.maintenance_mode = true;
                                for (Player pl : getServer().getOnlinePlayers()) {
                                    if (!pl.isOp() && !pl.hasPermission("admingui.maintenance")) {
                                        pl.kickPlayer(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_maintenance"));
                                    }
                                }
                            }
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_maintenance_enabled"));
                        }else if(enabled.equals("off") || enabled.equals("disable")){
                            AdminUI.maintenance_mode = false;
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_maintenance_disabled"));
                        }else{
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.chat("&cYou can only use /admin maintenance <on/off>"));
                        }
                    }else{
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
                    }
                }else if(args[0].equals("check")){
                    if(player.hasPermission("admingui.check")){
                        String name = ChatColor.stripColor(args[1]);
                        Set<String> con_sec = AdminGUI.getInstance().getPlayers().getConfigurationSection("").getKeys(false);
                        for (String uuid_name : con_sec){
                            if(AdminGUI.getInstance().getPlayers().getString(uuid_name + ".name").equals(name)){
                                player.sendMessage(Message.chat("&9"+ name + " stats:"));
                                player.sendMessage(Message.chat("  &9UUID: &b" + uuid_name));
                                if(AdminGUI.getInstance().getConf().getBoolean("ap_enabled", false)){
                                    player.sendMessage(Message.chat("  &9Rank: &b" + AdminGUI.getInstance().getPlayers().getString(uuid_name+ ".rank", "default")));
                                }
                                StringBuilder ips = new StringBuilder();
                                for (String ip: AdminGUI.getInstance().getPlayers().getStringList(uuid_name + ".ips")) {
                                    ips.append(ip).append(", ");
                                }
                                ips.delete(ips.length()-2, ips.length());
                                player.sendMessage(Message.chat("  &9IPs: &b" + ips.toString()));
                                player.sendMessage(Message.chat("  &9Last Join: &b" + Database.date_format.format(new Date(AdminGUI.getInstance().getPlayers().getLong(uuid_name + ".lastJoin")))));
                                player.sendMessage(Message.chat("  &9First Join: &b" + Database.date_format.format(new Date(AdminGUI.getInstance().getPlayers().getLong(uuid_name + ".firstJoin")))));
                                break;
                            }
                        }
                    }else{
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
                    }
                }else{
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_arguments"));
                }
            }else if(args.length == 3){
                //TODO: Permissions
                if(args[0].equals("rank")) {
                    if (args[1].equals("up")) {
                        if (player.hasPermission("admingui.rank.up")) {
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
                        if (player.hasPermission("admingui.rank.down")) {
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
                                }else{
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
                                }else{
                                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", ChatColor.stripColor(args[2])).replace("{rank}", cur_rank));
                                }
                            }
                        } else {
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
                        }
                    } else {
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
                    }
                }else{
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_arguments"));
                }
            }else if(args.length == 4){
                if(args[0].equals("rank")){
                    if(args[1].equals("set")) {
                        if (player.hasPermission("admingui.rank.set")){
                            Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[2]));
                            String rank = args[3];
                            if (target_player != null) {
                                if(Permissions.setRank(target_player.getUniqueId(), target_player.getName(), rank)){
                                    TargetPlayer.refreshPlayerTabList(target_player);
                                    TargetPlayer.refreshPermissions(target_player);
                                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", rank));
                                }else{
                                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
                                }
                            } else {
                                if(Permissions.setRank(null, ChatColor.stripColor(args[2]), rank)){
                                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", ChatColor.stripColor(args[2])).replace("{rank}", rank));
                                }else{
                                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
                                }
                            }
                        }else{
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
                        }
                    }else{
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
                    }
                }else{
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_arguments"));
                }
            }else{
                player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_arguments"));
            }
        }else{
            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
        }
        return true;
    }
}
