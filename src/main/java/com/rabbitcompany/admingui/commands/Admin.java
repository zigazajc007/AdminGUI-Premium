package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Initialize;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Admin implements CommandExecutor {

    private AdminUI adminUI = new AdminUI();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(!(sender instanceof Player)) {
            sender.sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.chat("&cYou can only use admin GUI in game."));
            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission("admingui.admin")){
            if(args.length == 0){
                AdminUI.target_player.put(player, player);
                player.openInventory(adminUI.GUI_Main(player));
            }else if(args.length == 1){
                if(args[0].equals("reload")) {
                    if(player.hasPermission("admingui.reload")){
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_reload_start"));
                        AdminGUI.getInstance().loadYamls();
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_reload_finish"));
                    }else{
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
                    }
                }else if(args[0].equals("initialize")) {
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_initialize"));
                }else{
                    Player target_player = Bukkit.getServer().getPlayer(ChatColor.stripColor(args[0]));
                    if(target_player != null){
                        AdminUI.target_player.put(player, target_player);
                        if(player.getName().equals(target_player.getName())){
                            player.openInventory(adminUI.GUI_Player(player));
                        }else{
                            player.openInventory(adminUI.GUI_Players_Settings(player, target_player));
                        }
                    }else{
                        //SQL
                        String sql_player = ChatColor.stripColor(args[0]);
                        if(AdminGUI.conn != null && AdminUI.online_players.contains(sql_player)){
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.chat("&cPlayer " + sql_player + " is not located in the same server as you."));
                        }else{
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "is_not_a_player").replace("{player}", args[0]));
                        }
                    }
                }
            }else if(args.length == 2){
                if(args[0].equals("initialize") && (args[1].equals("gui") || args[1].equals("players"))){
                    if(args[1].equals("gui")){
                        if(!AdminUI.task_gui.containsKey(player.getUniqueId()) && !AdminUI.task_players.containsKey(player.getUniqueId())){
                            Initialize.GUI(player, player.getInventory().getHelmet());
                        }else{
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_already_initializing"));
                        }
                    }else{
                        if(!AdminUI.task_gui.containsKey(player.getUniqueId()) && !AdminUI.task_players.containsKey(player.getUniqueId())){
                            Initialize.Players(player, player.getInventory().getHelmet());
                        }else{
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "message_already_initializing"));
                        }
                    }
                }else{
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "wrong_initialize"));
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
