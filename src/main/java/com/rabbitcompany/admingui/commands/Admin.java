package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.XMaterial;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

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
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_reload_finish"));
                }
            }else if(args.length == 2){
                if(args[0].equals("rank")) {
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_rank_arguments"));
                }
            }else if(args.length == 3){
                if(args[0].equals("rank")) {
                    if (args[1].equals("up")) {
                            Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[2]));
                            if (target_player != null) {
                                String cur_rank = AdminGUI.getInstance().getPermissions().getString("ranks." + target_player.getUniqueId().toString(), "default");
                                int cur_priority = AdminGUI.getInstance().getPermissions().getInt("groups." + cur_rank + ".priority", Integer.MAX_VALUE);
                                ArrayList<Integer> priorities = new ArrayList<>();
                                for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
                                    int pri = AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority");
                                    if (cur_priority > pri) priorities.add(pri);
                                }
                                if (priorities.size() > 0) {
                                    int max_pri = Collections.max(priorities);
                                    for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
                                        if (AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority") == max_pri) {
                                            Permissions.setRank(target_player.getUniqueId(), priority.getKey());
                                            TargetPlayer.refreshPlayerTabList(target_player);
                                            TargetPlayer.refreshPermissions(target_player);
                                            sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", priority.getKey()));
                                        }
                                    }
                                } else {
                                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", cur_rank));
                                }
                            } else {
                                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "is_not_a_player").replace("{player}", args[2]));
                            }
                    } else if (args[1].equals("down")) {
                            Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[2]));
                            if (target_player != null) {
                                String cur_rank = AdminGUI.getInstance().getPermissions().getString("ranks." + target_player.getUniqueId().toString(), "owner");
                                int cur_priority = AdminGUI.getInstance().getPermissions().getInt("groups." + cur_rank + ".priority", Integer.MIN_VALUE);
                                ArrayList<Integer> priorities = new ArrayList<>();
                                for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
                                    int pri = AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority");
                                    if (cur_priority < pri) priorities.add(pri);
                                }
                                if (priorities.size() > 0) {
                                    int min_pri = Collections.min(priorities);
                                    for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
                                        if (AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority") == min_pri) {
                                            Permissions.setRank(target_player.getUniqueId(), priority.getKey());
                                            TargetPlayer.refreshPlayerTabList(target_player);
                                            TargetPlayer.refreshPermissions(target_player);
                                            sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", priority.getKey()));
                                        }
                                    }
                                }
                            } else {
                                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "is_not_a_player").replace("{player}", args[2]));
                            }
                    } else {
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_rank_arguments"));
                    }
                }else{
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_arguments"));
                }
            }else if(args.length == 4){
                if(args[0].equals("rank")){
                    if(args[1].equals("set")) {
                        Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[2]));
                        if (target_player != null) {
                            String rank = args[3];
                            if(Permissions.setRank(target_player.getUniqueId(), rank)){
                                TargetPlayer.refreshPlayerTabList(target_player);
                                TargetPlayer.refreshPermissions(target_player);
                                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", rank));
                            }else{
                                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "wrong_rank_arguments"));
                            }
                        } else {
                            sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "is_not_a_player").replace("{player}", args[2]));
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
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_reload_finish"));
                    }else{
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
                    }
                }else if(args[0].equals("initialize")) {
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_initialize"));
                }else if(args[0].equals("rank")){
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
                }else if(args[0].equals("tools") || args[0].equals("tool")){
                    if(player.hasPermission("admingui.admin") && AdminGUI.getInstance().getConf().getBoolean("admin_tools_enabled", true)){
                        List<String> lore = Collections.singletonList(Message.chat(AdminGUI.getInstance().getConf().getString("admin_tools_lore", "&dClick me to open Admin GUI")));
                        ItemStack item = new ItemStack(XMaterial.matchXMaterial(AdminGUI.getInstance().getConf().getString("admin_tools_material", "NETHER_STAR")).get().parseMaterial(true), 1, XMaterial.matchXMaterial(AdminGUI.getInstance().getConf().getString("admin_tools_material", "NETHER_STAR")).get().getData());

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
                }else if(args[0].equals("rank")){
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
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
                                String cur_rank = AdminGUI.getInstance().getPermissions().getString("ranks." + target_player.getUniqueId().toString(), "default");
                                int cur_priority = AdminGUI.getInstance().getPermissions().getInt("groups." + cur_rank + ".priority", Integer.MAX_VALUE);
                                ArrayList<Integer> priorities = new ArrayList<>();
                                for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
                                    int pri = AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority");
                                    if (cur_priority > pri) priorities.add(pri);
                                }
                                if (priorities.size() > 0) {
                                    int max_pri = Collections.max(priorities);
                                    for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
                                        if (AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority") == max_pri) {
                                            Permissions.setRank(target_player.getUniqueId(), priority.getKey());
                                            TargetPlayer.refreshPlayerTabList(target_player);
                                            TargetPlayer.refreshPermissions(target_player);
                                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", priority.getKey()));
                                        }
                                    }
                                } else {
                                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", cur_rank));
                                }
                            } else {
                                player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "is_not_a_player").replace("{player}", args[2]));
                            }
                        } else {
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
                        }
                    } else if (args[1].equals("down")) {
                        if (player.hasPermission("admingui.rank.down")) {
                            Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[2]));
                            if (target_player != null) {
                                String cur_rank = AdminGUI.getInstance().getPermissions().getString("ranks." + target_player.getUniqueId().toString(), "owner");
                                int cur_priority = AdminGUI.getInstance().getPermissions().getInt("groups." + cur_rank + ".priority", Integer.MIN_VALUE);
                                ArrayList<Integer> priorities = new ArrayList<>();
                                for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
                                    int pri = AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority");
                                    if (cur_priority < pri) priorities.add(pri);
                                }
                                if (priorities.size() > 0) {
                                    int min_pri = Collections.min(priorities);
                                    for (Map.Entry<String, Object> priority : AdminGUI.getInstance().getPermissions().getConfigurationSection("groups").getValues(false).entrySet()) {
                                        if (AdminGUI.getInstance().getPermissions().getInt("groups." + priority.getKey() + ".priority") == min_pri) {
                                            Permissions.setRank(target_player.getUniqueId(), priority.getKey());
                                            TargetPlayer.refreshPlayerTabList(target_player);
                                            TargetPlayer.refreshPermissions(target_player);
                                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", priority.getKey()));
                                        }
                                    }
                                }
                            } else {
                                player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "is_not_a_player").replace("{player}", args[2]));
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
                            if (target_player != null) {
                                String rank = args[3];
                                if(Permissions.setRank(target_player.getUniqueId(), rank)){
                                    TargetPlayer.refreshPlayerTabList(target_player);
                                    TargetPlayer.refreshPermissions(target_player);
                                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_admin_rank").replace("{player}", target_player.getName()).replace("{rank}", rank));
                                }else{
                                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_rank_arguments"));
                                }
                            } else {
                                player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "is_not_a_player").replace("{player}", args[2]));
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
