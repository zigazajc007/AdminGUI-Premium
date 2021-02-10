package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.admingui.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private AdminGUI adminGUI;
    private AdminUI adminUI = new AdminUI();

    public InventoryClickListener(AdminGUI plugin){
        adminGUI = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(InventoryClickEvent e){
        String title = e.getView().getTitle();
        String player = e.getWhoClicked().getName();
        Player p = (Player) e.getWhoClicked();

        if(Settings.freeze.getOrDefault(p.getUniqueId(), false) && AdminGUI.getInstance().getConf().getBoolean("freeze_move_inventory", true)){
            e.setCancelled(true);
            return;
        }

        try{
            if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_main")) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_player").replace("{player}", player)) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_world")) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_players")) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_plugins")) || title.contains(Message.getMessage(p.getUniqueId(), "inventory_commands")) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_unban")) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_unmute")) || title.equals(Message.getMessage(p.getUniqueId(), "players_color").replace("{player}", Settings.target_player.get(p).getName())) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_actions").replace("{player}", Settings.target_player.get(p).getName())) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_kick").replace("{player}", Settings.target_player.get(p).getName())) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_ban").replace("{player}", Settings.target_player.get(p).getName())) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_potions").replace("{player}", Settings.target_player.get(p).getName())) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_spawner").replace("{player}", Settings.target_player.get(p).getName())) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_inventory").replace("{player}", Settings.target_player.get(p).getName())) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_money_give").replace("{player}", Settings.target_player.get(p).getName())) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_money_set").replace("{player}", Settings.target_player.get(p).getName())) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_money_take").replace("{player}", Settings.target_player.get(p).getName())) || title.equals(Message.getMessage(p.getUniqueId(), "inventory_money").replace("{player}", Settings.target_player.get(p).getName()))) {

                e.setCancelled(true);

                if(e.getCurrentItem() != null){
                    if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_main"))) {
                        adminUI.clicked_main(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), e.isLeftClick());
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_player").replace("{player}", player))) {
                        adminUI.clicked_player(p, e.getSlot(), e.getCurrentItem(), e.getInventory());
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_world"))) {
                        adminUI.clicked_world(p, e.getSlot(), e.getCurrentItem(), e.getInventory());
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_players"))) {
                        adminUI.clicked_players(p, e.getSlot(), e.getCurrentItem(), e.getInventory());
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_plugins"))) {
                        adminUI.clicked_plugins(p, e.getSlot(), e.getCurrentItem(), e.getInventory());
                    } else if (title.contains(Message.getMessage(p.getUniqueId(), "inventory_commands"))) {
                        adminUI.clicked_commands(p, e.getSlot(), e.getCurrentItem(), e.getInventory());
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_unban"))) {
                        adminUI.clicked_unban_players(p, e.getSlot(), e.getCurrentItem(), e.getInventory());
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_unmute"))) {
                        adminUI.clicked_unmute_players(p, e.getSlot(), e.getCurrentItem(), e.getInventory());
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "players_color").replace("{player}", Settings.target_player.get(p).getName()))) {
                        adminUI.clicked_players_settings(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), Settings.target_player.get(p));
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_actions").replace("{player}", Settings.target_player.get(p).getName()))) {
                        adminUI.clicked_actions(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), Settings.target_player.get(p));
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_kick").replace("{player}", Settings.target_player.get(p).getName()))) {
                        adminUI.clicked_kick(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), Settings.target_player.get(p));
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_ban").replace("{player}", Settings.target_player.get(p).getName()))) {
                        adminUI.clicked_ban(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), Settings.target_player.get(p));
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_potions").replace("{player}", Settings.target_player.get(p).getName()))) {
                        adminUI.clicked_potions(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), Settings.target_player.get(p));
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_spawner").replace("{player}", Settings.target_player.get(p).getName()))) {
                        adminUI.clicked_spawner(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), Settings.target_player.get(p));
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_money").replace("{player}", Settings.target_player.get(p).getName()))) {
                        adminUI.clicked_money(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), Settings.target_player.get(p));
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_money_give").replace("{player}", Settings.target_player.get(p).getName()))) {
                        adminUI.clicked_money_amount(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), Settings.target_player.get(p), 1);
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_money_set").replace("{player}", Settings.target_player.get(p).getName()))) {
                        adminUI.clicked_money_amount(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), Settings.target_player.get(p), 2);
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_money_take").replace("{player}", Settings.target_player.get(p).getName()))) {
                        adminUI.clicked_money_amount(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), Settings.target_player.get(p), 3);
                    } else if (title.equals(Message.getMessage(p.getUniqueId(), "inventory_inventory").replace("{player}", Settings.target_player.get(p).getName()))) {
                        adminUI.clicked_inventory(p, e.getSlot(), e.getCurrentItem(), e.getInventory(), Settings.target_player.get(p), e.isLeftClick());
                    }
                }
            }
        }catch (Exception ignored){}
    }
}
