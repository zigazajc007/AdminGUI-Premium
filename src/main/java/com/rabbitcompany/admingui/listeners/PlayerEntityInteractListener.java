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
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerEntityInteractListener implements Listener {

    private final AdminUI adminUI = new AdminUI();
    private final AdminGUI adminGUI;

    public PlayerEntityInteractListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event){
        if(event.getRightClicked() instanceof Player){
            Player player = event.getPlayer();
            Player target = (Player) event.getRightClicked();

            if(player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta() != null && player.getItemInHand().getItemMeta().getLore() != null){
                if(player.getItemInHand().getItemMeta().getLore().contains(Message.chat(AdminGUI.getInstance().getConf().getString("admin_tools_lore", "&dClick me to open Admin GUI")))){
                    if(TargetPlayer.hasPermission(player, "admingui.admin")){
                        Settings.target_player.put(player.getUniqueId(), target);
                        if(player.getName().equals(target.getName())){
                            player.openInventory(adminUI.GUI_Player(player));
                        }else{
                            player.openInventory(adminUI.GUI_Players_Settings(player, target, target.getName()));
                        }
                    }else{
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "prefix") + Message.getMessage(player.getUniqueId(), "permission"));
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

}
