package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.XMaterial;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Channel;
import com.rabbitcompany.admingui.utils.Initialize;
import com.rabbitcompany.admingui.utils.Message;
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

    private AdminUI adminUI = new AdminUI();

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
