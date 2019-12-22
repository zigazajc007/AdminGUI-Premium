package com.rabbitcompany.admingui.commands;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Admin implements CommandExecutor {

    private AdminUI adminUI = new AdminUI();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission("admingui.admin")){
            if(args.length == 0){
                AdminUI.target_player.put(player, player);
                player.openInventory(adminUI.GUI_Main(player));
            }else if(args.length == 1){
                if(args[0].equals("reload")){
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.chat("&aPlugin is now reloading..."));
                    AdminGUI.getInstance().loadYamls();
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.chat("&aPlugin reloaded successfully!"));
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
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "is_not_a_player").replace("{player}", args[0]));
                    }
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
