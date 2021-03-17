package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.admingui.utils.Settings;
import com.rabbitcompany.admingui.utils.TargetPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryView;

public class PlayerInteractListener implements Listener {

    private AdminUI adminUI = new AdminUI();
    private AdminGUI adminGUI;

    public PlayerInteractListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.hasItem()){
            if(event.getItem() != null){
                if(event.getItem().hasItemMeta() && event.getItem().getItemMeta() != null){
                    if(event.getItem().getItemMeta().hasLore() && event.getItem().getItemMeta().getLore() != null){
                        if(event.getItem().getItemMeta().getLore().contains(Message.chat(AdminGUI.getInstance().getConf().getString("admin_tools_lore", "&dClick me to open Admin GUI")))){
                            Player player = event.getPlayer();
                            if(player.getOpenInventory().getType() != InventoryType.CHEST){
                                if(TargetPlayer.hasPermission(player, "admingui.admin")){
                                    Settings.target_player.put(player.getUniqueId(), player);
                                    player.openInventory(adminUI.GUI_Main(player));
                                }else{
                                    player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
                                }
                            }
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }

    }

}
