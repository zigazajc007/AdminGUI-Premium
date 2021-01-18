package com.rabbitcompany.admingui.ui;

import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.adminbans.utils.BannedPlayer;
import com.rabbitcompany.adminbans.utils.MutedPlayer;
import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.XMaterial;
import com.rabbitcompany.admingui.utils.*;
import com.rabbitcompany.admingui.utils.potions.Version_12;
import com.rabbitcompany.admingui.utils.potions.Version_14;
import com.rabbitcompany.admingui.utils.potions.Version_8;
import com.rabbitcompany.admingui.utils.spawners.materials.*;
import com.rabbitcompany.admingui.utils.spawners.messages.*;
import de.myzelyam.api.vanish.VanishAPI;
import net.milkbowl.vault.economy.EconomyResponse;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.Bukkit.getVersion;

public class AdminUI {

    //Target players
    public static HashMap<Player, Player> target_player = new HashMap<>();

    //Player heads
    public static HashMap<String, ItemStack> skulls = new HashMap<>();
    public static HashMap<String, ItemStack> skulls_players = new HashMap<>();

    //Player GUI color
    private static HashMap<UUID, String> gui_color = new HashMap<>();

    //Chat Delay
    public static HashMap<UUID, Long> admin_chat_delay = new HashMap<>();

    //Custom Chat Channels
    public static HashMap<UUID, String> custom_chat_channel = new HashMap<>();

    //Permissions
    public static HashMap<UUID, PermissionAttachment> permissions = new HashMap<>();

    //Mute chat
    public static boolean muted_chat = false;

    //Initialize task
    public static HashMap<UUID, Integer> task_gui = new HashMap<>();
    public static HashMap<UUID, Integer> task_players = new HashMap<>();

    //Online players
    public static ArrayList<String> online_players = new ArrayList<>();

    //Language
    public static HashMap<UUID, String> language = new HashMap<>();

    //Kick
    private HashMap<UUID, Boolean> kick_silence = new HashMap<>();

    //Ban
    private HashMap<UUID, Integer> ban_years = new HashMap<>();
    private HashMap<UUID, Integer> ban_months = new HashMap<>();
    private HashMap<UUID, Integer> ban_days = new HashMap<>();
    private HashMap<UUID, Integer> ban_hours = new HashMap<>();
    private HashMap<UUID, Integer> ban_minutes = new HashMap<>();
    private HashMap<UUID, Boolean> ban_silence = new HashMap<>();

    //Page
    private HashMap<UUID, Integer> page = new HashMap<>();
    private HashMap<UUID, Integer> pages = new HashMap<>();

    //Unban Page
    private HashMap<UUID, Integer> unban_page = new HashMap<>();
    private HashMap<UUID, Integer> unban_pages = new HashMap<>();

    //Unmute Page
    private HashMap<UUID, Integer> unmute_page = new HashMap<>();
    private HashMap<UUID, Integer> unmute_pages = new HashMap<>();

    //Potions
    private HashMap<UUID, Integer> duration = new HashMap<>();
    private HashMap<UUID, Integer> level = new HashMap<>();

    //God
    public static HashMap<UUID, Boolean> god = new HashMap<>();

    //Chat Color
    public static HashMap<UUID, String> chat_color = new HashMap<>();

    //Custom commands
    public static HashMap<UUID, Integer> custom_method = new HashMap<>();
    public static HashMap<UUID, Integer> plugin_slot = new HashMap<>();

    //Date Format
    public static SimpleDateFormat date_format = new SimpleDateFormat(AdminGUI.getInstance().getConf().getString("date_format", "yyyy-MM-dd HH:mm:ss"));

    //Command Spy
    public static HashMap<UUID, Boolean> command_spy = new HashMap<>();

    //Freeze
    public static HashMap<UUID, Boolean> freeze = new HashMap<>();

    //Maintenance mode
    public static boolean maintenance_mode = false;

    public Inventory GUI_Main(Player p){

        Inventory inv_main = Bukkit.createInventory(null, 36, Message.getMessage(p.getUniqueId(), "inventory_main"));

        for(int i = 1; i < 36; i++){
                Item.create(inv_main, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
        }

        Item.after_createPlayerHead(inv_main, skulls_players.get(p.getName()), 1, 11, Message.getMessage(p.getUniqueId(),"main_player").replace("{player}", p.getName()));
        Item.after_createPlayerHead(inv_main, skulls.get("Black1_TV"), 1, 15, Message.getMessage(p.getUniqueId(), "main_players"));

        switch (AdminGUI.gui_type){
            case 1:
                Item.after_createPlayerHead(inv_main, skulls.get("0qt"), 1, 13, Message.getMessage(p.getUniqueId(), "main_world"));
                Item.after_createPlayerHead(inv_main, skulls.get("mattijs"), 1, 17, Message.getMessage(p.getUniqueId(), "main_plugins"));
                if(maintenance_mode){
                    Item.after_createPlayerHead(inv_main, skulls.get("BKing2012"),1,28, Message.getMessage(p.getUniqueId(), "main_maintenance_mode"));
                }else{
                    Item.after_createPlayerHead(inv_main, skulls.get("AverageJoe"),1,28, Message.getMessage(p.getUniqueId(), "main_maintenance_mode"));
                }
                if(p.hasPermission("admingui.unban") || p.hasPermission("admingui.unmute")) {
                    Item.after_createPlayerHead(inv_main, skulls.get("LobbyPlugin"),1,32, Message.getMessage(p.getUniqueId(), "main_unban_players"));
                }
                Item.after_createPlayerHead(inv_main, skulls.get("Opp"), 1, 34, Message.getMessage(p.getUniqueId(), "main_language") + language.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("default_language")));
                Item.after_createPlayerHead(inv_main, skulls.get("MHF_Redstone"),1,36, Message.getMessage(p.getUniqueId(), "main_quit"));
                break;
            default:
                Item.create(inv_main, "GRASS_BLOCK", 1, 13, Message.getMessage(p.getUniqueId(), "main_world"));
                Item.create(inv_main, "BOOKSHELF", 1, 17, Message.getMessage(p.getUniqueId(), "main_plugins"));
                if(maintenance_mode){
                    Item.create(inv_main, "GLOWSTONE_DUST", 1, 28, Message.getMessage(p.getUniqueId(), "main_maintenance_mode"));
                }else{
                    Item.create(inv_main, "REDSTONE", 1, 28, Message.getMessage(p.getUniqueId(), "main_maintenance_mode"));
                }
                if(p.hasPermission("admingui.unban") || p.hasPermission("admingui.unmute")) {
                    Item.create(inv_main, "BARRIER",1,32, Message.getMessage(p.getUniqueId(), "main_unban_players"));
                }
                Item.create(inv_main, "COMMAND_BLOCK", 1, 34, Message.getMessage(p.getUniqueId(), "main_language") + language.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("default_language")));
                Item.create(inv_main, "REDSTONE_BLOCK",1,36, Message.getMessage(p.getUniqueId(), "main_quit"));
                break;
        }

        switch (gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE"))){
            case "LIGHT_BLUE_STAINED_GLASS_PANE":
                    Item.create(inv_main, "RED_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "RED_STAINED_GLASS_PANE":
                    Item.create(inv_main, "ORANGE_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "ORANGE_STAINED_GLASS_PANE":
                Item.create(inv_main, "PINK_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "PINK_STAINED_GLASS_PANE":
                Item.create(inv_main, "YELLOW_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "YELLOW_STAINED_GLASS_PANE":
                Item.create(inv_main, "LIME_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "LIME_STAINED_GLASS_PANE":
                Item.create(inv_main, "GREEN_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "GREEN_STAINED_GLASS_PANE":
                Item.create(inv_main, "CYAN_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "CYAN_STAINED_GLASS_PANE":
                Item.create(inv_main, "BLUE_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "BLUE_STAINED_GLASS_PANE":
                Item.create(inv_main, "MAGENTA_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "MAGENTA_STAINED_GLASS_PANE":
                Item.create(inv_main, "PURPLE_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "PURPLE_STAINED_GLASS_PANE":
                Item.create(inv_main, "BROWN_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "BROWN_STAINED_GLASS_PANE":
                Item.create(inv_main, "GRAY_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "GRAY_STAINED_GLASS_PANE":
                Item.create(inv_main, "LIGHT_GRAY_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "LIGHT_GRAY_STAINED_GLASS_PANE":
                Item.create(inv_main, "BLACK_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "BLACK_STAINED_GLASS_PANE":
                Item.create(inv_main, "WHITE_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
            case "WHITE_STAINED_GLASS_PANE":
                Item.create(inv_main, "LIGHT_BLUE_STAINED_GLASS_PANE", 1, 30, "  ");
                break;
        }

        return inv_main;
    }

    public Inventory GUI_Player(Player p){

        String inventory_player_name = Message.getMessage(p.getUniqueId(), "inventory_player").replace("{player}", p.getName());

        Inventory inv_player = Bukkit.createInventory(null, 45, inventory_player_name);

        for(int i = 1; i < 45; i++){
            Item.create(inv_player, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
        }

        if(p.hasPermission("admingui.info")) {
            if(AdminGUI.vault){
                if(p.getAddress() != null && p.getAddress().getAddress() != null){
                    Item.createPlayerHead(inv_player, p.getName(), 1, 5, Message.getMessage(p.getUniqueId(), "player_info").replace("{player}", p.getName()), Message.chat("&eHeal: " + Math.round(p.getHealth())), Message.chat("&7Feed: " + Math.round(p.getFoodLevel())), Message.chat("&2Money: " + AdminGUI.getEconomy().format(AdminGUI.getEconomy().getBalance(p.getName()))) ,Message.chat("&aGamemode: " + p.getGameMode().toString()), Message.chat("&5IP: " + p.getAddress().getAddress().toString().replace("/", "")));
                }else{
                    Item.createPlayerHead(inv_player, p.getName(), 1, 5, Message.getMessage(p.getUniqueId(), "player_info").replace("{player}", p.getName()), Message.chat("&eHeal: " + Math.round(p.getHealth())), Message.chat("&7Feed: " + Math.round(p.getFoodLevel())), Message.chat("&2Money: " + AdminGUI.getEconomy().format(AdminGUI.getEconomy().getBalance(p.getName()))) ,Message.chat("&aGamemode: " + p.getGameMode().toString()));
                }
            }else{
                if(p.getAddress() != null && p.getAddress().getAddress() != null){
                    Item.createPlayerHead(inv_player, p.getName(), 1, 5, Message.getMessage(p.getUniqueId(), "player_info").replace("{player}", p.getName()), Message.chat("&eHeal: " + Math.round(p.getHealth())), Message.chat("&7Feed: " + Math.round(p.getFoodLevel())), Message.chat("&aGamemode: " + p.getGameMode().toString()), Message.chat("&5IP: " + p.getAddress().getAddress().toString().replace("/", "")));
                }else{
                    Item.createPlayerHead(inv_player, p.getName(), 1, 5, Message.getMessage(p.getUniqueId(), "player_info").replace("{player}", p.getName()), Message.chat("&eHeal: " + Math.round(p.getHealth())), Message.chat("&7Feed: " + Math.round(p.getFoodLevel())), Message.chat("&aGamemode: " + p.getGameMode().toString()));
                }
            }
        }else{
            Item.createPlayerHead(inv_player, p.getName(), 1, 5, Message.getMessage(p.getUniqueId(), "player_info").replace("{player}", p.getName()));
        }

        switch (AdminGUI.gui_type){
            case 1:
                if(p.hasPermission("admingui.heal")) {
                    Item.after_createPlayerHead(inv_player, skulls.get("Ground15"), 1, 11, Message.getMessage(p.getUniqueId(), "player_heal"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 11,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.feed")) {
                    Item.after_createPlayerHead(inv_player, skulls.get("Burger_guy"), 1, 13, Message.getMessage(p.getUniqueId(), "player_feed"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 13,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.gamemode")) {
                    if (p.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                        Item.after_createPlayerHead(inv_player, skulls.get("Zyne"), 1, 15, Message.getMessage(p.getUniqueId(), "player_survival"));
                    } else if (p.getPlayer().getGameMode() == GameMode.ADVENTURE) {
                        Item.after_createPlayerHead(inv_player, skulls.get("Mannahara"), 1, 15, Message.getMessage(p.getUniqueId(), "player_adventure"));
                    } else if (p.getPlayer().getGameMode() == GameMode.CREATIVE) {
                        Item.after_createPlayerHead(inv_player, skulls.get("ThaBrick"), 1, 15, Message.getMessage(p.getUniqueId(), "player_creative"));
                    } else if (p.getPlayer().getGameMode() == GameMode.SPECTATOR) {
                        Item.after_createPlayerHead(inv_player, skulls.get("3i5g00d"), 1, 15, Message.getMessage(p.getUniqueId(), "player_spectator"));
                    }
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 15,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.god")) {
                    if(!Bukkit.getVersion().contains("1.8")) {
                        if (p.isInvulnerable()) {
                            Item.after_createPlayerHead(inv_player, skulls.get("Ground15"), 1, 17, Message.getMessage(p.getUniqueId(), "player_god_disabled"));
                        } else {
                            Item.after_createPlayerHead(inv_player, skulls.get("EDDxample"), 1, 17, Message.getMessage(p.getUniqueId(), "player_god_enabled"));
                        }
                    }else{
                        if(god.getOrDefault(p.getUniqueId(), false)){

                            Item.after_createPlayerHead(inv_player, skulls.get("Ground15"), 1, 17, Message.getMessage(p.getUniqueId(), "player_god_disabled"));
                        }else{
                            Item.after_createPlayerHead(inv_player, skulls.get("EDDxample"), 1, 17, Message.getMessage(p.getUniqueId(), "player_god_enabled"));
                        }
                    }
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 17,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.spawner")) {
                    Item.after_createPlayerHead(inv_player, skulls.get("MFH_Spawner"), 1, 21, Message.getMessage(p.getUniqueId(), "player_spawner"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 21,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.kill")) {
                    Item.after_createPlayerHead(inv_player, skulls.get("ZeeFear"),1,23, Message.getMessage(p.getUniqueId(), "player_kill"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 23,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.burn")) {
                    Item.after_createPlayerHead(inv_player, skulls.get("haohanklliu"), 1, 25, Message.getMessage(p.getUniqueId(), "player_burn"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 25,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.lightning")) {
                    Item.after_createPlayerHead(inv_player, skulls.get("raichuthink"), 1, 27, Message.getMessage(p.getUniqueId(), "player_lightning"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 27,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.money")) {
                    Item.after_createPlayerHead(inv_player, skulls.get("MrSnowDK"), 1, 31, Message.getMessage(p.getUniqueId(), "player_money"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 31,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.custom")) {
                    Item.after_createPlayerHead(inv_player, skulls.get("Opp"),1,35, Message.getMessage(p.getUniqueId(), "player_custom"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 35,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                Item.after_createPlayerHead(inv_player, skulls.get("MHF_Redstone"),1,45, Message.getMessage(p.getUniqueId(), "player_back"));
                break;
            default:
                if(p.hasPermission("admingui.heal")) {
                    Item.create(inv_player, "GOLDEN_APPLE", 1, 11, Message.getMessage(p.getUniqueId(), "player_heal"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 11,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.feed")) {
                    Item.create(inv_player, "COOKED_BEEF", 1, 13, Message.getMessage(p.getUniqueId(), "player_feed"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 13,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.gamemode")) {
                    if (p.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                        Item.create(inv_player, "DIRT", 1, 15, Message.getMessage(p.getUniqueId(), "player_survival"));
                    } else if (p.getPlayer().getGameMode() == GameMode.ADVENTURE) {
                        Item.create(inv_player, "GRASS_BLOCK", 1, 15, Message.getMessage(p.getUniqueId(), "player_adventure"));
                    } else if (p.getPlayer().getGameMode() == GameMode.CREATIVE) {
                        Item.create(inv_player, "BRICKS", 1, 15, Message.getMessage(p.getUniqueId(), "player_creative"));
                    } else if (p.getPlayer().getGameMode() == GameMode.SPECTATOR) {
                        if(Bukkit.getVersion().contains("1.8")){
                            Item.create(inv_player, "POTION", 1, 15, Message.getMessage(p.getUniqueId(), "player_spectator"));
                        }else{
                            Item.create(inv_player, "SPLASH_POTION", 1, 15, Message.getMessage(p.getUniqueId(), "player_spectator"));
                        }
                    }
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 15,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.god")) {
                    if(!Bukkit.getVersion().contains("1.8")) {
                        if (p.isInvulnerable()) {
                            Item.create(inv_player, "RED_TERRACOTTA", 1, 17, Message.getMessage(p.getUniqueId(), "player_god_disabled"));
                        } else {
                            Item.create(inv_player, "LIME_TERRACOTTA", 1, 17, Message.getMessage(p.getUniqueId(), "player_god_enabled"));
                        }
                    }else{
                        if(god.getOrDefault(p.getUniqueId(), false)){
                            Item.create(inv_player, "RED_TERRACOTTA", 1, 17, Message.getMessage(p.getUniqueId(), "player_god_disabled"));
                        }else{
                            Item.create(inv_player, "LIME_TERRACOTTA", 1, 17, Message.getMessage(p.getUniqueId(), "player_god_enabled"));
                        }
                    }
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 17,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.spawner")) {
                    Item.create(inv_player, "SPAWNER", 1, 21, Message.getMessage(p.getUniqueId(), "player_spawner"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 21,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.kill")) {
                    Item.create(inv_player, "DIAMOND_SWORD",1,23, Message.getMessage(p.getUniqueId(), "player_kill"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 23,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.burn")) {
                    Item.create(inv_player, "FLINT_AND_STEEL", 1, 25, Message.getMessage(p.getUniqueId(), "player_burn"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 25,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.lightning")) {
                    Item.create(inv_player, "STICK", 1, 27, Message.getMessage(p.getUniqueId(), "player_lightning"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 27,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.money")) {
                    Item.create(inv_player, "PAPER", 1, 31, Message.getMessage(p.getUniqueId(), "player_money"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 31,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.custom")) {
                    Item.create(inv_player, "COMMAND_BLOCK",1,35, Message.getMessage(p.getUniqueId(), "player_custom"));
                }else{
                    Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 35,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                Item.create(inv_player, "REDSTONE_BLOCK",1,45, Message.getMessage(p.getUniqueId(), "player_back"));
                break;
        }

        if(p.hasPermission("admingui.potions")) {
            Item.create(inv_player, "POTION", 1, 19, Message.getMessage(p.getUniqueId(), "player_potions"));
        }else{
            Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 19,  Message.getMessage(p.getUniqueId(), "permission"));
        }

        if(p.hasPermission("admingui.firework")) {
            Item.create(inv_player, "FIREWORK_ROCKET", 1, 29, Message.getMessage(p.getUniqueId(), "player_firework"));
        }else{
            Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 29,  Message.getMessage(p.getUniqueId(), "permission"));
        }

        if (p.hasPermission("admingui.vanish")) {
            if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
                if (VanishAPI.isInvisible(p)) {
                    Item.create(inv_player, "FEATHER", 1, 33, Message.getMessage(p.getUniqueId(), "player_vanish_disabled"));
                } else {
                    Item.create(inv_player, "FEATHER", 1, 33, Message.getMessage(p.getUniqueId(), "player_vanish_enabled"));
                }
            } else {
                Item.create(inv_player, "FEATHER", 1, 33, Message.getMessage(p.getUniqueId(), "player_vanish_enabled"));
            }
        }else{
            Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 33, Message.getMessage(p.getUniqueId(), "permission"));
        }

        if(AdminGUI.getInstance().getConf().getBoolean("ac_enabled")){
            if(p.hasPermission("admingui.chat.color.change")){
                Item.create(inv_player, chat_color.getOrDefault(p.getUniqueId(), "LIGHT_GRAY_WOOL"),1,37, Message.getMessage(p.getUniqueId(), "player_chat_color"));
            }else{
                Item.create(inv_player, "RED_STAINED_GLASS_PANE", 1, 37,  Message.getMessage(p.getUniqueId(), "permission"));
            }
        }

        return inv_player;
    }

    private Inventory GUI_World(Player p){

        Inventory inv_world = Bukkit.createInventory(null, 27, Message.getMessage(p.getUniqueId(), "inventory_world"));

        for(int i = 1; i < 27; i++){
            Item.create(inv_world, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
        }

        switch (AdminGUI.gui_type){
            case 1:
                if(p.hasPermission("admingui.time")) {
                    if (p.getPlayer().getWorld().getTime() < 13000) {
                        Item.after_createPlayerHead(inv_world,  skulls.get("Ground15"),1, 11, Message.getMessage(p.getUniqueId(), "world_day"));
                    } else {
                        Item.after_createPlayerHead(inv_world,  skulls.get("EDDxample"),1, 11, Message.getMessage(p.getUniqueId(), "world_night"));
                    }
                }else{
                    Item.create(inv_world, "RED_STAINED_GLASS_PANE", 1, 11,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.weather")) {
                    if (p.getPlayer().getWorld().isThundering()) {
                        Item.after_createPlayerHead(inv_world,  skulls.get("LapisBlock"),1, 13, Message.getMessage(p.getUniqueId(), "world_thunder"));
                    } else if (p.getPlayer().getWorld().hasStorm()) {
                        Item.after_createPlayerHead(inv_world,  skulls.get("emack0714"),1, 13, Message.getMessage(p.getUniqueId(), "world_rain"));
                    } else {
                        Item.after_createPlayerHead(inv_world, skulls.get("Super_Sniper"),1, 13, Message.getMessage(p.getUniqueId(), "world_clear"));
                    }
                }else{
                    Item.create(inv_world, "RED_STAINED_GLASS_PANE", 1, 13,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                Item.after_createPlayerHead(inv_world, skulls.get("MHF_Redstone"),1, 27, Message.getMessage(p.getUniqueId(), "world_back"));
                break;
            default:
                if(p.hasPermission("admingui.time")) {
                    if (p.getPlayer().getWorld().getTime() < 13000) {
                        Item.create(inv_world,  "GOLD_BLOCK",1, 11, Message.getMessage(p.getUniqueId(), "world_day"));
                    } else {
                        Item.create(inv_world,  "COAL_BLOCK",1, 11, Message.getMessage(p.getUniqueId(), "world_night"));
                    }
                }else{
                    Item.create(inv_world, "RED_STAINED_GLASS_PANE", 1, 11,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.weather")) {
                    if (p.getPlayer().getWorld().isThundering()) {
                        Item.create(inv_world,  "BLUE_TERRACOTTA",1, 13, Message.getMessage(p.getUniqueId(), "world_thunder"));
                    } else if (p.getPlayer().getWorld().hasStorm()) {
                        Item.create(inv_world,  "CYAN_TERRACOTTA",1, 13, Message.getMessage(p.getUniqueId(), "world_rain"));
                    } else {
                        Item.create(inv_world,  "LIGHT_BLUE_TERRACOTTA",1, 13, Message.getMessage(p.getUniqueId(), "world_clear"));
                    }
                }else{
                    Item.create(inv_world, "RED_STAINED_GLASS_PANE", 1, 13,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                Item.create(inv_world,  "REDSTONE_BLOCK",1, 27, Message.getMessage(p.getUniqueId(), "world_back"));
                break;
        }

        return inv_world;
    }

    private Inventory GUI_Players(Player p){

        Inventory inv_players = Bukkit.createInventory(null, 54, Message.getMessage(p.getUniqueId(), "inventory_players"));

        int online = online_players.size();

        pages.put(p.getUniqueId(), (int) Math.ceil((float)online / 45));

        for (int i = 46; i <= 53; i++){
            Item.create(inv_players, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
        }

        int player_slot = (page.getOrDefault(p.getUniqueId(),1)-1) * 45;

        for (int i = 0; i < 45; i++){
            if(player_slot < online){
                Item.createPlayerHead(inv_players, online_players.get(player_slot),1, i+1, Message.getMessage(p.getUniqueId(), "players_color").replace("{player}", online_players.get(player_slot)), Message.getMessage(p.getUniqueId(), "players_more"));
                player_slot++;
            }else{
                Item.create(inv_players, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i+1, " ");
            }
        }

        switch (AdminGUI.gui_type){
            case 1:
                if(page.getOrDefault(p.getUniqueId(), 1) > 1){
                    Item.after_createPlayerHead(inv_players, skulls.get("MHF_ArrowLeft"), 1, 49, Message.getMessage(p.getUniqueId(), "players_previous"));
                }

                if(pages.getOrDefault(p.getUniqueId(), 1) > 1){
                    Item.after_createPlayerHead(inv_players, skulls.get("MHF_Question"), page.getOrDefault(p.getUniqueId(), 1), 50, Message.getMessage(p.getUniqueId(), "players_page") + " " + page.getOrDefault(p.getUniqueId(), 1));
                }

                if(pages.get(p.getUniqueId()) > page.getOrDefault(p.getUniqueId(), 1)){
                    Item.after_createPlayerHead(inv_players, skulls.get("MHF_ArrowRight"), 1, 51, Message.getMessage(p.getUniqueId(), "players_next"));
                }

                Item.after_createPlayerHead(inv_players, skulls.get("MHF_Redstone"),1,54, Message.getMessage(p.getUniqueId(), "players_back"));
                break;
            default:
                if(page.getOrDefault(p.getUniqueId(), 1) > 1){
                    Item.create(inv_players, "PAPER", 1, 49, Message.getMessage(p.getUniqueId(), "players_previous"));
                }

                if(pages.getOrDefault(p.getUniqueId(), 1) > 1){
                    Item.create(inv_players, "BOOK", page.getOrDefault(p.getUniqueId(), 1), 50, Message.getMessage(p.getUniqueId(), "players_page") + " " + page.getOrDefault(p.getUniqueId(), 1));
                }

                if(pages.get(p.getUniqueId()) > page.getOrDefault(p.getUniqueId(), 1)){
                    Item.create(inv_players, "PAPER", 1, 51, Message.getMessage(p.getUniqueId(), "players_next"));
                }

                Item.create(inv_players, "REDSTONE_BLOCK",1,54, Message.getMessage(p.getUniqueId(), "players_back"));
                break;
        }

        return inv_players;
    }

    private Inventory GUI_Plugins(Player p){

        ConfigurationSection one;
        YamlConfiguration yamlConfiguration;

        switch (custom_method.getOrDefault(p.getUniqueId(), 0)){
            case 1:
                one = AdminGUI.getInstance().getComm().getConfigurationSection("plugins");
                yamlConfiguration = AdminGUI.getInstance().getComm();
                break;
            case 2:
                one = AdminGUI.getInstance().getComo().getConfigurationSection("plugins");
                yamlConfiguration = AdminGUI.getInstance().getComo();
                break;
            default:
                one = AdminGUI.getInstance().getPlug().getConfigurationSection("plugins");
                yamlConfiguration = AdminGUI.getInstance().getPlug();
                break;
        }

        Inventory inv_plugins = Bukkit.createInventory(null, 54, Message.getMessage(p.getUniqueId(), "inventory_plugins"));

        for (int i = 1; i < 54; i++){
            Item.create(inv_plugins, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
        }

        for (Map.Entry<String, Object> plug_slot : one.getValues(false).entrySet()) {
            int i = Integer.parseInt(plug_slot.getKey());
            Item.create(inv_plugins, yamlConfiguration.getString("plugins."+i+".material"), 1, i, yamlConfiguration.getString("plugins."+i+".name"));
        }

        switch (AdminGUI.gui_type){
            case 1:
                Item.after_createPlayerHead(inv_plugins, skulls.get("MHF_Redstone"),1,54, Message.getMessage(p.getUniqueId(), "plugins_back"));
                break;
            default:
                Item.create(inv_plugins, "REDSTONE_BLOCK",1,54, Message.getMessage(p.getUniqueId(), "plugins_back"));
                break;
        }

        return inv_plugins;
    }

    private Inventory GUI_Commands(Player p, int slot){

        ConfigurationSection two;
        YamlConfiguration yamlConfiguration;

        switch (custom_method.getOrDefault(p.getUniqueId(), 0)){
            case 1:
                two = AdminGUI.getInstance().getComm().getConfigurationSection("plugins."+slot+".commands");
                yamlConfiguration = AdminGUI.getInstance().getComm();
                break;
            case 2:
                two = AdminGUI.getInstance().getComo().getConfigurationSection("plugins."+slot+".commands");
                yamlConfiguration = AdminGUI.getInstance().getComo();
                break;
            default:
                two = AdminGUI.getInstance().getPlug().getConfigurationSection("plugins."+slot+".commands");
                yamlConfiguration = AdminGUI.getInstance().getPlug();
                break;
        }

        Inventory inv_commands = Bukkit.createInventory(null, 54, Message.chat(yamlConfiguration.getString("plugins."+slot+".name"))+ " " + Message.getMessage(p.getUniqueId(), "inventory_commands"));

        for (int i = 1; i < 54; i++){
            Item.create(inv_commands, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
        }

        for (Map.Entry<String, Object> comm_slot : two.getValues(false).entrySet()) {
            int j = Integer.parseInt(comm_slot.getKey());
            if(yamlConfiguration.getString("plugins."+slot+".commands."+slot+".permission") != null){
                if(p.hasPermission(yamlConfiguration.getString("plugins."+slot+".commands."+j+".permission")+"")){
                    Item.create(inv_commands, yamlConfiguration.getString("plugins."+slot+".commands."+j+".material"), 1, j, yamlConfiguration.getString("plugins."+slot+".commands."+j+".name"));
                }else{
                    Item.create(inv_commands, "RED_STAINED_GLASS_PANE", 1, j,  Message.getMessage(p.getUniqueId(), "permission"));
                }
            }else{
                Item.create(inv_commands, yamlConfiguration.getString("plugins."+slot+".commands."+j+".material"), 1, j, yamlConfiguration.getString("plugins."+slot+".commands."+j+".name"));
            }
        }

        switch (AdminGUI.gui_type){
            case 1:
                Item.after_createPlayerHead(inv_commands, skulls.get("MHF_Redstone"),1,54, Message.getMessage(p.getUniqueId(), "commands_back"));
                break;
            default:
                Item.create(inv_commands, "REDSTONE_BLOCK",1,54, Message.getMessage(p.getUniqueId(), "commands_back"));
                break;
        }

        return inv_commands;
    }

    public Inventory GUI_Unban_Players(Player p){

        Inventory inv_unban_players = Bukkit.createInventory(null, 54, Message.getMessage(p.getUniqueId(), "inventory_unban"));

        if(Bukkit.getPluginManager().isPluginEnabled("AdminBans")){

            ArrayList<BannedPlayer> abs = new ArrayList<>(AdminBansAPI.getBannedPlayers());

            int online = abs.size();

            unban_pages.put(p.getUniqueId(), (int) Math.ceil((float)online / 45));

            for (int i = 46; i <= 53; i++){
                Item.create(inv_unban_players, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
            }

            int player_slot = (unban_page.getOrDefault(p.getUniqueId(),1)-1) * 45;

            for (int i = 0; i < 45; i++){
                if(player_slot < online){
                    Item.createPlayerHead(inv_unban_players, abs.get(player_slot).username_to, 1, i + 1, Message.getMessage(p.getUniqueId(), "unban_color").replace("{player}", abs.get(player_slot).username_to), Message.chat("&aBanned by: &6" + abs.get(player_slot).username_from), Message.chat("&aBanned on: &6" + AdminBansAPI.date_format.format(abs.get(player_slot).created)), Message.chat("&aExpiration: &6" + AdminBansAPI.date_format.format(abs.get(player_slot).until)), " ", Message.getMessage(p.getUniqueId(), "unban_more"));
                    player_slot++;
                }else{
                    Item.create(inv_unban_players, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i+1, " ");
                }
            }
        }else{
            ArrayList<String> pl = new ArrayList<>();

            for (OfflinePlayer all : getServer().getBannedPlayers()) {
                pl.add(all.getName());
            }

            Collections.sort(pl);

            int online = pl.size();

            unban_pages.put(p.getUniqueId(), (int) Math.ceil((float)online / 45));

            for (int i = 46; i <= 53; i++){
                Item.create(inv_unban_players, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
            }

            int player_slot = (unban_page.getOrDefault(p.getUniqueId(),1)-1) * 45;

            for (int i = 0; i < 45; i++){
                if(player_slot < online){
                    if(Bukkit.getBanList(BanList.Type.NAME).getBanEntry(pl.get(player_slot)).getExpiration() == null){
                        Item.createPlayerHead(inv_unban_players, pl.get(player_slot), 1, i + 1, Message.getMessage(p.getUniqueId(), "unban_color").replace("{player}", pl.get(player_slot)), Message.chat("&aBanned: &6" + Bukkit.getBanList(BanList.Type.NAME).getBanEntry(pl.get(player_slot)).getCreated()), Message.chat("&aExpiration: &6Never"), " ", Message.getMessage(p.getUniqueId(), "unban_more"));
                    }else{
                        Item.createPlayerHead(inv_unban_players, pl.get(player_slot), 1, i + 1, Message.getMessage(p.getUniqueId(), "unban_color").replace("{player}", pl.get(player_slot)), Message.chat("&aBanned: &6" + Bukkit.getBanList(BanList.Type.NAME).getBanEntry(pl.get(player_slot)).getCreated()), Message.chat("&aExpiration: &6" + Bukkit.getBanList(BanList.Type.NAME).getBanEntry(pl.get(player_slot)).getExpiration()), " ", Message.getMessage(p.getUniqueId(), "unban_more"));
                    }
                    player_slot++;
                }else{
                    Item.create(inv_unban_players, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i+1, " ");
                }
            }
        }

        switch (AdminGUI.gui_type){
            case 1:
                if(unban_page.getOrDefault(p.getUniqueId(), 1) > 1){
                    Item.after_createPlayerHead(inv_unban_players, skulls.get("MHF_ArrowLeft"), 1, 49, Message.getMessage(p.getUniqueId(), "unban_previous"));
                }

                if(unban_pages.getOrDefault(p.getUniqueId(), 1) > 1){
                    Item.after_createPlayerHead(inv_unban_players, skulls.get("MHF_Question"), unban_page.getOrDefault(p.getUniqueId(), 1), 50, Message.getMessage(p.getUniqueId(), "unban_page") + " " + unban_page.getOrDefault(p.getUniqueId(), 1));
                }

                if(unban_pages.get(p.getUniqueId()) > unban_page.getOrDefault(p.getUniqueId(), 1)){
                    Item.after_createPlayerHead(inv_unban_players, skulls.get("MHF_ArrowRight"), 1, 51, Message.getMessage(p.getUniqueId(), "unban_next"));
                }

                Item.after_createPlayerHead(inv_unban_players, skulls.get("MHF_Redstone"),1,54, Message.getMessage(p.getUniqueId(), "unban_back"));
                break;
            default:
                if(unban_page.getOrDefault(p.getUniqueId(), 1) > 1){
                    Item.create(inv_unban_players, "PAPER", 1, 49, Message.getMessage(p.getUniqueId(), "unban_previous"));
                }

                if(unban_pages.getOrDefault(p.getUniqueId(), 1) > 1){
                    Item.create(inv_unban_players, "BOOK", unban_page.getOrDefault(p.getUniqueId(), 1), 50, Message.getMessage(p.getUniqueId(), "unban_page") + " " + unban_page.getOrDefault(p.getUniqueId(), 1));
                }

                if(unban_pages.get(p.getUniqueId()) > unban_page.getOrDefault(p.getUniqueId(), 1)){
                    Item.create(inv_unban_players, "PAPER", 1, 51, Message.getMessage(p.getUniqueId(), "unban_next"));
                }

                Item.create(inv_unban_players, "REDSTONE_BLOCK",1,54, Message.getMessage(p.getUniqueId(), "unban_back"));
                break;
        }

        return inv_unban_players;
    }

    public Inventory GUI_Unmute_Players(Player p){

        Inventory inv_unmute_players = Bukkit.createInventory(null, 54, Message.getMessage(p.getUniqueId(), "inventory_unmute"));

        if(Bukkit.getPluginManager().isPluginEnabled("AdminBans")){
            ArrayList<MutedPlayer> abs = new ArrayList<>(AdminBansAPI.getMutedPlayers());

            int online = abs.size();

            unmute_pages.put(p.getUniqueId(), (int) Math.ceil((float)online / 45));

            for (int i = 46; i <= 53; i++){
                Item.create(inv_unmute_players, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
            }

            int player_slot = (unmute_page.getOrDefault(p.getUniqueId(),1)-1) * 45;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (int i = 0; i < 45; i++){
                if(player_slot < online){
                    Item.createPlayerHead(inv_unmute_players, abs.get(player_slot).username_to, 1, i + 1, Message.getMessage(p.getUniqueId(), "unmute_color").replace("{player}", abs.get(player_slot).username_to), Message.chat("&aMuted by: &6" + abs.get(player_slot).username_from), Message.chat("&aMuted on: &6" + sdf.format(abs.get(player_slot).created)), Message.chat("&aExpiration: &6" + sdf.format(abs.get(player_slot).until)), " ", Message.getMessage(p.getUniqueId(), "unmute_more"));
                    player_slot++;
                }else{
                    Item.create(inv_unmute_players, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i+1, " ");
                }
            }

            switch (AdminGUI.gui_type){
                case 1:
                    if(unmute_page.getOrDefault(p.getUniqueId(), 1) > 1){
                        Item.after_createPlayerHead(inv_unmute_players, skulls.get("MHF_ArrowLeft"), 1, 49, Message.getMessage(p.getUniqueId(), "unmute_previous"));
                    }

                    if(unmute_pages.getOrDefault(p.getUniqueId(), 1) > 1){
                        Item.after_createPlayerHead(inv_unmute_players, skulls.get("MHF_Question"), unmute_page.getOrDefault(p.getUniqueId(), 1), 50, Message.getMessage(p.getUniqueId(), "unmute_page") + " " + unmute_page.getOrDefault(p.getUniqueId(), 1));
                    }

                    if(unmute_pages.get(p.getUniqueId()) > unmute_page.getOrDefault(p.getUniqueId(), 1)){
                        Item.after_createPlayerHead(inv_unmute_players, skulls.get("MHF_ArrowRight"), 1, 51, Message.getMessage(p.getUniqueId(), "unmute_next"));
                    }
                    break;
                default:
                    if(unmute_page.getOrDefault(p.getUniqueId(), 1) > 1){
                        Item.create(inv_unmute_players, "PAPER", 1, 49, Message.getMessage(p.getUniqueId(), "unmute_previous"));
                    }

                    if(unmute_pages.getOrDefault(p.getUniqueId(), 1) > 1){
                        Item.create(inv_unmute_players, "BOOK", unmute_page.getOrDefault(p.getUniqueId(), 1), 50, Message.getMessage(p.getUniqueId(), "unmute_page") + " " + unmute_page.getOrDefault(p.getUniqueId(), 1));
                    }

                    if(unmute_pages.get(p.getUniqueId()) > unmute_page.getOrDefault(p.getUniqueId(), 1)){
                        Item.create(inv_unmute_players, "PAPER", 1, 51, Message.getMessage(p.getUniqueId(), "unmute_next"));
                    }
                    break;
            }

        }else{
            for (int i = 0; i < 53; i++){
                Item.create(inv_unmute_players, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i+1, " ");
            }
        }

        switch (AdminGUI.gui_type){
            case 1:
                Item.after_createPlayerHead(inv_unmute_players, skulls.get("MHF_Redstone"),1,54, Message.getMessage(p.getUniqueId(), "unmute_back"));
                break;
            default:
                Item.create(inv_unmute_players, "REDSTONE_BLOCK",1,54, Message.getMessage(p.getUniqueId(), "unmute_back"));
                break;
        }

        return inv_unmute_players;
    }

    public Inventory GUI_Players_Settings(Player p, Player target_player, String target_name){

        String inventory_players_settings_name = Message.getMessage(p.getUniqueId(), "players_color").replace("{player}", target_name);
        Inventory inv_players_settings = Bukkit.createInventory(null, 27, inventory_players_settings_name);

        for(int i = 1; i < 27; i++){
                Item.create(inv_players_settings, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
        }

        if(p.hasPermission("admingui.info")) {
            //TODO: Bungee
            if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false) && target_player == null){
                Item.createPlayerHead(inv_players_settings, target_name, 1, 5, Message.getMessage(p.getUniqueId(), "players_settings_info").replace("{player}", target_name));
            }else{
                if(AdminGUI.vault){
                    if(target_player.getAddress() != null && target_player.getAddress().getAddress() != null){
                        Item.createPlayerHead(inv_players_settings, target_name, 1, 5, Message.getMessage(p.getUniqueId(), "players_settings_info").replace("{player}", target_name), Message.chat("&eHeal: " + Math.round(target_player.getHealth())), Message.chat("&7Feed: " + Math.round(target_player.getFoodLevel())), Message.chat("&2Money: " + AdminGUI.getEconomy().format(AdminGUI.getEconomy().getBalance(target_name))) ,Message.chat("&aGamemode: " + target_player.getGameMode().toString()), Message.chat("&5IP: " + target_player.getAddress().getAddress().toString().replace("/", "")));
                    }else{
                        Item.createPlayerHead(inv_players_settings, target_name, 1, 5, Message.getMessage(p.getUniqueId(), "players_settings_info").replace("{player}", target_name), Message.chat("&eHeal: " + Math.round(target_player.getHealth())), Message.chat("&7Feed: " + Math.round(target_player.getFoodLevel())), Message.chat("&2Money: " + AdminGUI.getEconomy().format(AdminGUI.getEconomy().getBalance(target_name))) ,Message.chat("&aGamemode: " + target_player.getGameMode().toString()));
                    }
                }else{
                    if(target_player.getAddress() != null && target_player.getAddress().getAddress() != null){
                        Item.createPlayerHead(inv_players_settings, target_name, 1, 5, Message.getMessage(p.getUniqueId(), "players_settings_info").replace("{player}", target_name), Message.chat("&eHeal: " + Math.round(target_player.getHealth())), Message.chat("&7Feed: " + Math.round(target_player.getFoodLevel())), Message.chat("&aGamemode: " + target_player.getGameMode().toString()), Message.chat("&5IP: " + target_player.getAddress().getAddress().toString().replace("/", "")));
                    }else{
                        Item.createPlayerHead(inv_players_settings, target_name, 1, 5, Message.getMessage(p.getUniqueId(), "players_settings_info").replace("{player}", target_name), Message.chat("&eHeal: " + Math.round(target_player.getHealth())), Message.chat("&7Feed: " + Math.round(target_player.getFoodLevel())), Message.chat("&aGamemode: " + target_player.getGameMode().toString()));
                    }
                }
            }
        }else{
            Item.createPlayerHead(inv_players_settings, target_name, 1, 5, Message.getMessage(p.getUniqueId(), "players_settings_info").replace("{player}", target_name));
        }

        switch (AdminGUI.gui_type){
            case 1:
                Item.after_createPlayerHead(inv_players_settings, skulls.get("ZiGmUnDo"), 1, 11, Message.getMessage(p.getUniqueId(), "players_settings_actions"));

                if(p.hasPermission("admingui.money.other")) {
                    Item.after_createPlayerHead(inv_players_settings, skulls.get("MrSnowDK"), 1, 13, Message.getMessage(p.getUniqueId(), "players_settings_money"));
                }else{
                    Item.create(inv_players_settings, "RED_STAINED_GLASS_PANE", 1, 13,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.kick.other")) {
                    Item.after_createPlayerHead(inv_players_settings, skulls.get("Push_red_button"), 1, 15, Message.getMessage(p.getUniqueId(), "players_settings_kick_player"));
                }else{
                    Item.create(inv_players_settings, "RED_STAINED_GLASS_PANE", 1, 15,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.ban")) {
                    Item.after_createPlayerHead(inv_players_settings, skulls.get("LobbyPlugin"), 1, 17, Message.getMessage(p.getUniqueId(), "players_settings_ban_player"));
                }else{
                    Item.create(inv_players_settings, "RED_STAINED_GLASS_PANE", 1, 17,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                Item.after_createPlayerHead(inv_players_settings, skulls.get("MHF_Redstone"),1,27, Message.getMessage(p.getUniqueId(), "players_settings_back"));

                break;
            default:
                Item.create(inv_players_settings, "DIAMOND_SWORD", 1, 11, Message.getMessage(p.getUniqueId(), "players_settings_actions"));

                if(p.hasPermission("admingui.money.other")) {
                    Item.create(inv_players_settings, "PAPER", 1, 13, Message.getMessage(p.getUniqueId(), "players_settings_money"));
                }else{
                    Item.create(inv_players_settings, "RED_STAINED_GLASS_PANE", 1, 13,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.kick.other")) {
                    Item.create(inv_players_settings, "BLACK_TERRACOTTA", 1, 15, Message.getMessage(p.getUniqueId(), "players_settings_kick_player"));
                }else{
                    Item.create(inv_players_settings, "RED_STAINED_GLASS_PANE", 1, 15,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.ban")) {
                    Item.create(inv_players_settings, "BARRIER", 1, 17, Message.getMessage(p.getUniqueId(), "players_settings_ban_player"));
                }else{
                    Item.create(inv_players_settings, "RED_STAINED_GLASS_PANE", 1, 17,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                Item.create(inv_players_settings, "REDSTONE_BLOCK",1,27, Message.getMessage(p.getUniqueId(), "players_settings_back"));

                break;
        }

        return inv_players_settings;
    }

    public Inventory GUI_Actions(Player p, Player target){

        String inventory_actions_name = Message.getMessage(p.getUniqueId(), "inventory_actions").replace("{player}", target.getName());
        target_player.put(p, target);

        Inventory inv_actions = Bukkit.createInventory(null, 54, inventory_actions_name);

        for(int i = 1; i < 54; i++){
            Item.create(inv_actions, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
        }

        if(p.hasPermission("admingui.info")) {
            if(AdminGUI.vault){
                if(target.getAddress() != null && target.getAddress().getAddress() != null){
                    Item.createPlayerHead(inv_actions, target.getName(), 1, 5, Message.getMessage(p.getUniqueId(), "players_settings_info").replace("{player}", target.getName()), Message.chat("&eHeal: " + Math.round(target.getHealth())), Message.chat("&7Feed: " + Math.round(target.getFoodLevel())), Message.chat("&2Money: " + AdminGUI.getEconomy().format(AdminGUI.getEconomy().getBalance(target.getName()))) ,Message.chat("&aGamemode: " + target.getGameMode().toString()), Message.chat("&5IP: " + target.getAddress().getAddress().toString().replace("/", "")));
                }else{
                    Item.createPlayerHead(inv_actions, target.getName(), 1, 5, Message.getMessage(p.getUniqueId(), "players_settings_info").replace("{player}", target.getName()), Message.chat("&eHeal: " + Math.round(target.getHealth())), Message.chat("&7Feed: " + Math.round(target.getFoodLevel())), Message.chat("&2Money: " + AdminGUI.getEconomy().format(AdminGUI.getEconomy().getBalance(target.getName()))) ,Message.chat("&aGamemode: " + target.getGameMode().toString()));
                }
            }else{
                if(target.getAddress() != null && target.getAddress().getAddress() != null){
                    Item.createPlayerHead(inv_actions, target.getName(), 1, 5, Message.getMessage(p.getUniqueId(), "players_settings_info").replace("{player}", target.getName()), Message.chat("&eHeal: " + Math.round(target.getHealth())), Message.chat("&7Feed: " + Math.round(target.getFoodLevel())), Message.chat("&aGamemode: " + target.getGameMode().toString()), Message.chat("&5IP: " + target.getAddress().getAddress().toString().replace("/", "")));
                }else{
                    Item.createPlayerHead(inv_actions, target.getName(), 1, 5, Message.getMessage(p.getUniqueId(), "players_settings_info").replace("{player}", target.getName()), Message.chat("&eHeal: " + Math.round(target.getHealth())), Message.chat("&7Feed: " + Math.round(target.getFoodLevel())), Message.chat("&aGamemode: " + target.getGameMode().toString()));
                }
            }
        }else{
            Item.createPlayerHead(inv_actions, target.getName(), 1, 5, Message.getMessage(p.getUniqueId(), "actions_info").replace("{player}", target.getName()));
        }

        switch (AdminGUI.gui_type){
            case 1:
                if(p.hasPermission("admingui.heal.other")) {
                    Item.after_createPlayerHead(inv_actions, skulls.get("IM_"), 1, 11, Message.getMessage(p.getUniqueId(), "actions_heal"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 11,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.feed.other")) {
                    Item.after_createPlayerHead(inv_actions, skulls.get("Burger_guy"), 1, 13, Message.getMessage(p.getUniqueId(), "actions_feed"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 13,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.gamemode.other")) {
                    if (target.getGameMode() == GameMode.SURVIVAL) {
                        Item.after_createPlayerHead(inv_actions, skulls.get("Zyne"), 1, 15, Message.getMessage(p.getUniqueId(), "actions_survival"));
                    } else if (target.getGameMode() == GameMode.ADVENTURE) {
                        Item.after_createPlayerHead(inv_actions, skulls.get("Mannahara"), 1, 15, Message.getMessage(p.getUniqueId(), "actions_adventure"));
                    } else if (target.getGameMode() == GameMode.CREATIVE) {
                        Item.after_createPlayerHead(inv_actions, skulls.get("ThaBrick"), 1, 15, Message.getMessage(p.getUniqueId(), "actions_creative"));
                    } else if (target.getGameMode() == GameMode.SPECTATOR) {
                        Item.after_createPlayerHead(inv_actions, skulls.get("3i5g00d"), 1, 15, Message.getMessage(p.getUniqueId(), "actions_spectator"));
                    }
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 15,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.god.other")) {
                    if(!Bukkit.getVersion().contains("1.8")) {
                        if (target.isInvulnerable()) {
                            Item.after_createPlayerHead(inv_actions, skulls.get("Ground15"), 1, 17, Message.getMessage(p.getUniqueId(), "actions_god_disabled"));
                        } else {
                            Item.after_createPlayerHead(inv_actions, skulls.get("EDDxample"), 1, 17, Message.getMessage(p.getUniqueId(), "actions_god_enabled"));
                        }
                    }else{
                        if(god.getOrDefault(target.getUniqueId(), false)){
                            Item.after_createPlayerHead(inv_actions, skulls.get("Ground15"), 1, 17, Message.getMessage(p.getUniqueId(), "actions_god_disabled"));
                        }else{
                            Item.after_createPlayerHead(inv_actions, skulls.get("EDDxample"), 1, 17, Message.getMessage(p.getUniqueId(), "actions_god_enabled"));
                        }
                    }
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 17,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.kill.other")) {
                    Item.after_createPlayerHead(inv_actions, skulls.get("ZeeFear"),1,23, Message.getMessage(p.getUniqueId(), "actions_kill_player"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 23,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.spawner.other")) {
                    Item.after_createPlayerHead(inv_actions, skulls.get("MFH_Spawner"), 1, 25, Message.getMessage(p.getUniqueId(), "actions_spawner"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 25,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.inventory")) {
                    Item.after_createPlayerHead(inv_actions, skulls.get("ElMarcosFTW"), 1, 29, Message.getMessage(p.getUniqueId(), "actions_inventory"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 29,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.burn.other")) {
                    Item.after_createPlayerHead(inv_actions, skulls.get("haohanklliu"), 1, 31, Message.getMessage(p.getUniqueId(), "actions_burn_player"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 31,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.lightning.other")){
                    Item.after_createPlayerHead(inv_actions, skulls.get("raichuthink"), 1, 35, Message.getMessage(p.getUniqueId(), "actions_lightning"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 35, Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.custom")) {
                    Item.after_createPlayerHead(inv_actions, skulls.get("Opp"),1,41, Message.getMessage(p.getUniqueId(), "actions_custom"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 41,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                Item.after_createPlayerHead(inv_actions, skulls.get("MHF_Redstone"),1,54, Message.getMessage(p.getUniqueId(), "actions_back"));
                break;
            default:
                if(p.hasPermission("admingui.heal.other")) {
                    Item.create(inv_actions, "GOLDEN_APPLE", 1, 11, Message.getMessage(p.getUniqueId(), "actions_heal"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 11,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.feed.other")) {
                    Item.create(inv_actions, "COOKED_BEEF", 1, 13, Message.getMessage(p.getUniqueId(), "actions_feed"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 13,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.gamemode.other")) {
                    if (target.getGameMode() == GameMode.SURVIVAL) {
                        Item.create(inv_actions, "DIRT", 1, 15, Message.getMessage(p.getUniqueId(), "actions_survival"));
                    } else if (target.getGameMode() == GameMode.ADVENTURE) {
                        Item.create(inv_actions, "GRASS_BLOCK", 1, 15, Message.getMessage(p.getUniqueId(), "actions_adventure"));
                    } else if (target.getGameMode() == GameMode.CREATIVE) {
                        Item.create(inv_actions, "BRICKS", 1, 15, Message.getMessage(p.getUniqueId(), "actions_creative"));
                    } else if (target.getGameMode() == GameMode.SPECTATOR) {
                        if(Bukkit.getVersion().contains("1.8")){
                            Item.create(inv_actions, "POTION", 1, 15, Message.getMessage(p.getUniqueId(), "actions_spectator"));
                        }else{
                            Item.create(inv_actions, "SPLASH_POTION", 1, 15, Message.getMessage(p.getUniqueId(), "actions_spectator"));
                        }
                    }
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 15,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.god.other")) {
                    if(!Bukkit.getVersion().contains("1.8")) {
                        if (target.isInvulnerable()) {
                            Item.create(inv_actions, "RED_TERRACOTTA", 1, 17, Message.getMessage(p.getUniqueId(), "actions_god_disabled"));
                        } else {
                            Item.create(inv_actions, "LIME_TERRACOTTA", 1, 17, Message.getMessage(p.getUniqueId(), "actions_god_enabled"));
                        }
                    }else{
                        if(god.getOrDefault(target.getUniqueId(), false)){
                            Item.create(inv_actions, "RED_TERRACOTTA", 1, 17, Message.getMessage(p.getUniqueId(), "actions_god_disabled"));
                        }else{
                            Item.create(inv_actions, "LIME_TERRACOTTA", 1, 17, Message.getMessage(p.getUniqueId(), "actions_god_enabled"));
                        }
                    }
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 17,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.kill.other")) {
                    Item.create(inv_actions, "DIAMOND_SWORD",1,23, Message.getMessage(p.getUniqueId(), "actions_kill_player"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 23,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.spawner.other")) {
                    Item.create(inv_actions, "SPAWNER", 1, 25, Message.getMessage(p.getUniqueId(), "actions_spawner"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 25,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.inventory")) {
                    Item.create(inv_actions, "BOOK", 1, 29, Message.getMessage(p.getUniqueId(), "actions_inventory"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 29,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.burn.other")) {
                    Item.create(inv_actions, "FLINT_AND_STEEL", 1, 31, Message.getMessage(p.getUniqueId(), "actions_burn_player"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 31,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.lightning.other")){
                    if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")){
                        Item.create(inv_actions, "TRIDENT", 1, 35, Message.getMessage(p.getUniqueId(), "actions_lightning"));
                    }else{
                        Item.create(inv_actions, "STICK", 1, 35, Message.getMessage(p.getUniqueId(), "actions_lightning"));
                    }
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 35, Message.getMessage(p.getUniqueId(), "permission"));
                }

                if(p.hasPermission("admingui.custom")) {
                    Item.create(inv_actions, "COMMAND_BLOCK",1,41, Message.getMessage(p.getUniqueId(), "actions_custom"));
                }else{
                    Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 41,  Message.getMessage(p.getUniqueId(), "permission"));
                }

                Item.create(inv_actions, "REDSTONE_BLOCK",1,54, Message.getMessage(p.getUniqueId(), "actions_back"));
                break;
        }

        if(p.hasPermission("admingui.teleport")) {
            Item.create(inv_actions, "ENDER_PEARL", 1, 19, Message.getMessage(p.getUniqueId(), "actions_teleport_to_player"));
        }else{
            Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 19,  Message.getMessage(p.getUniqueId(), "permission"));
        }

        if(p.hasPermission("admingui.potions.other")) {
            Item.create(inv_actions, "POTION", 1, 21, Message.getMessage(p.getUniqueId(), "actions_potions"));
        }else{
            Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 21,  Message.getMessage(p.getUniqueId(), "permission"));
        }

        if(p.hasPermission("admingui.teleport.other")) {
            if(Bukkit.getVersion().contains("1.8")){
                Item.create(inv_actions, "ENDER_PEARL", 1, 27, Message.getMessage(p.getUniqueId(), "actions_teleport_player_to_you"));
            }else{
                Item.create(inv_actions, "END_CRYSTAL", 1, 27, Message.getMessage(p.getUniqueId(), "actions_teleport_player_to_you"));
            }
        }else{
            Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 27,  Message.getMessage(p.getUniqueId(), "permission"));
        }

        if (p.hasPermission("admingui.vanish.other")) {
            if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
                if (VanishAPI.isInvisible(target)) {
                    Item.create(inv_actions, "FEATHER", 1, 33, Message.getMessage(p.getUniqueId(), "actions_vanish_disabled"));
                } else {
                    Item.create(inv_actions, "FEATHER", 1, 33, Message.getMessage(p.getUniqueId(), "actions_vanish_enabled"));
                }
            } else {
                Item.create(inv_actions, "FEATHER", 1, 33, Message.getMessage(p.getUniqueId(), "actions_vanish_enabled"));
            }
        }else{
            Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 33, Message.getMessage(p.getUniqueId(), "permission"));
        }

        if(p.hasPermission("admingui.firework.other")){
            Item.create(inv_actions, "FIREWORK_ROCKET", 1, 37, Message.getMessage(p.getUniqueId(), "actions_firework"));
        }else{
            Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 37, Message.getMessage(p.getUniqueId(), "permission"));
        }

        if(p.hasPermission("admingui.fakeop")){
            Item.create(inv_actions, "PAPER", 1, 39, Message.getMessage(p.getUniqueId(), "actions_fakeop"));
        }else{
            Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 39, Message.getMessage(p.getUniqueId(), "permission"));
        }

        if(AdminGUI.getInstance().getConf().getBoolean("ac_enabled")){
            if(p.hasPermission("admingui.chat.color.change.other")){
                Item.create(inv_actions, chat_color.getOrDefault(target.getUniqueId(), "LIGHT_GRAY_WOOL"),1,41, Message.getMessage(p.getUniqueId(), "actions_chat_color"));
            }else{
                Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 41,  Message.getMessage(p.getUniqueId(), "permission"));
            }
        }

        //TODO: Freeze
        if(p.hasPermission("admingui.freeze.other")){
            if(freeze.getOrDefault(target.getUniqueId(), false)){
                Item.create(inv_actions, "ICE", 1, 43, Message.getMessage(p.getUniqueId(), "actions_freeze_disabled"));
            }else{
                Item.create(inv_actions, "ICE", 1, 43, Message.getMessage(p.getUniqueId(), "actions_freeze_enabled"));
            }
        }else{
            Item.create(inv_actions, "RED_STAINED_GLASS_PANE", 1, 43,  Message.getMessage(p.getUniqueId(), "permission"));
        }

        return inv_actions;
    }

    public Inventory GUI_Kick(Player p, Player target){

        String inventory_kick_name = Message.getMessage(p.getUniqueId(), "inventory_kick").replace("{player}", target.getName());
        target_player.put(p, target);

        Inventory inv_kick = Bukkit.createInventory(null, 27, inventory_kick_name);

        for (int i = 1; i < 27; i++){
                Item.create(inv_kick, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
        }

        for (Map.Entry<String, Object> kick_slot : AdminGUI.getInstance().getKick().getConfigurationSection("slots").getValues(false).entrySet()) {
            int i = Integer.parseInt(kick_slot.getKey());
            Item.create(inv_kick, AdminGUI.getInstance().getKick().getString("slots."+i+".material"), 1, i, AdminGUI.getInstance().getKick().getString("slots."+i+".name"));
        }

        switch (AdminGUI.gui_type){
            case 1:
                if(kick_silence.getOrDefault(p.getUniqueId(), false)){
                    Item.after_createPlayerHead(inv_kick, skulls.get("Ground15"), 1, 19, Message.getMessage(p.getUniqueId(), "ban_silence_enabled"));
                }else{
                    Item.after_createPlayerHead(inv_kick, skulls.get("EDDxample"), 1, 19, Message.getMessage(p.getUniqueId(), "ban_silence_disabled"));
                }

                Item.after_createPlayerHead(inv_kick, skulls.get("MHF_Redstone"),1,27, Message.getMessage(p.getUniqueId(), "kick_back"));
                break;
            default:
                if(kick_silence.getOrDefault(p.getUniqueId(), false)){
                    Item.create(inv_kick, "LIME_TERRACOTTA", 1, 19, Message.getMessage(p.getUniqueId(), "ban_silence_enabled"));
                }else{
                    Item.create(inv_kick, "RED_TERRACOTTA", 1, 19, Message.getMessage(p.getUniqueId(), "ban_silence_disabled"));
                }

                Item.create(inv_kick, "REDSTONE_BLOCK",1,27, Message.getMessage(p.getUniqueId(), "kick_back"));
                break;
        }

        return inv_kick;
    }

    public Inventory GUI_Ban(Player p, Player target){

        String inventory_ban_name = Message.getMessage(p.getUniqueId(), "inventory_ban").replace("{player}", target.getName());
        target_player.put(p, target);

        Inventory inv_ban = Bukkit.createInventory(null, 36, inventory_ban_name);

        for (int i = 1; i < 36; i++){
            Item.create(inv_ban, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
        }

        if(ban_years.getOrDefault(p.getUniqueId(), 0) == 0){
            Item.create(inv_ban, "RED_STAINED_GLASS_PANE", 1, 12, Message.getMessage(p.getUniqueId(), "ban_years"));
        }else{
            Item.create(inv_ban, "CLOCK", ban_years.getOrDefault(p.getUniqueId(),0), 12, Message.getMessage(p.getUniqueId(), "ban_years"));
        }

        if(ban_months.getOrDefault(p.getUniqueId(),0) == 0){
            Item.create(inv_ban, "RED_STAINED_GLASS_PANE", 1, 13, Message.getMessage(p.getUniqueId(), "ban_months"));
        }else{
            Item.create(inv_ban, "CLOCK", ban_months.getOrDefault(p.getUniqueId(),0), 13, Message.getMessage(p.getUniqueId(), "ban_months"));
        }

        if(ban_days.getOrDefault(p.getUniqueId(), 0) == 0){
            Item.create(inv_ban, "RED_STAINED_GLASS_PANE", 1, 14, Message.getMessage(p.getUniqueId(), "ban_days"));
        }else{
            Item.create(inv_ban, "CLOCK", ban_days.getOrDefault(p.getUniqueId(),0), 14, Message.getMessage(p.getUniqueId(), "ban_days"));
        }

        if(ban_hours.getOrDefault(p.getUniqueId(), 0) == 0){
            Item.create(inv_ban, "RED_STAINED_GLASS_PANE", 1, 15, Message.getMessage(p.getUniqueId(), "ban_hours"));
        }else{
            Item.create(inv_ban, "CLOCK", ban_hours.getOrDefault(p.getUniqueId(),0), 15, Message.getMessage(p.getUniqueId(), "ban_hours"));
        }

        if(ban_minutes.getOrDefault(p.getUniqueId(),0) == 0){
            Item.create(inv_ban, "RED_STAINED_GLASS_PANE", 1, 16, Message.getMessage(p.getUniqueId(), "ban_minutes"));
        }else{
            Item.create(inv_ban, "CLOCK", ban_minutes.getOrDefault(p.getUniqueId(),0), 16, Message.getMessage(p.getUniqueId(), "ban_minutes"));
        }

        Item.create(inv_ban, "WHITE_TERRACOTTA", 1, 30, Message.getMessage(p.getUniqueId(), "ban_hacking"));
        Item.create(inv_ban, "ORANGE_TERRACOTTA", 1, 31, Message.getMessage(p.getUniqueId(), "ban_griefing"));
        Item.create(inv_ban, "MAGENTA_TERRACOTTA", 1, 32, Message.getMessage(p.getUniqueId(), "ban_spamming"));
        Item.create(inv_ban, "LIGHT_BLUE_TERRACOTTA", 1, 33, Message.getMessage(p.getUniqueId(), "ban_advertising"));
        Item.create(inv_ban, "YELLOW_TERRACOTTA", 1, 34, Message.getMessage(p.getUniqueId(), "ban_swearing"));

        switch (AdminGUI.gui_type){
            case 1:
                if(ban_silence.getOrDefault(p.getUniqueId(), false)){
                    Item.after_createPlayerHead(inv_ban, skulls.get("Ground15"), 1, 28, Message.getMessage(p.getUniqueId(), "ban_silence_enabled"));
                }else{
                    Item.after_createPlayerHead(inv_ban, skulls.get("EDDxample"), 1, 28, Message.getMessage(p.getUniqueId(), "ban_silence_disabled"));
                }
                Item.after_createPlayerHead(inv_ban, skulls.get("MHF_Redstone"),1,36, Message.getMessage(p.getUniqueId(), "ban_back"));
                break;
            default:
                if(ban_silence.getOrDefault(p.getUniqueId(), false)){
                    Item.create(inv_ban, "LIME_TERRACOTTA", 1, 28, Message.getMessage(p.getUniqueId(), "ban_silence_enabled"));
                }else{
                    Item.create(inv_ban, "RED_TERRACOTTA", 1, 28, Message.getMessage(p.getUniqueId(), "ban_silence_disabled"));
                }
                Item.create(inv_ban, "REDSTONE_BLOCK",1,36, Message.getMessage(p.getUniqueId(), "ban_back"));
                break;
        }

        return inv_ban;
    }

    public Inventory GUI_potions(Player p, Player target){

        String inventory_potions_name = Message.getMessage(p.getUniqueId(), "inventory_potions").replace("{player}", target.getName());
        target_player.put(p, target);

        Inventory inv_potions = Bukkit.createInventory(null, 36, inventory_potions_name);

        for (int i = 1; i < 36; i++){
            Item.create(inv_potions, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
        }

        if (Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.13")) {
            for(Version_14 potion : Version_14.values()){
                Item.create(inv_potions, "POTION", 1, potion.ordinal() + 1, Message.getMessage(p.getUniqueId(), potion.name()));
            }
        }else if(Bukkit.getVersion().contains("1.12") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.9")){
            for(Version_12 potion : Version_12.values()){
                Item.create(inv_potions, "POTION", 1, potion.ordinal() + 1, Message.getMessage(p.getUniqueId(), potion.name()));
            }
        }else if(Bukkit.getVersion().contains("1.8")){
            for(Version_8 potion : Version_8.values()){
                Item.create(inv_potions, "POTION", 1, potion.ordinal() + 1, Message.getMessage(p.getUniqueId(), potion.name()));
            }
        }

        Item.create(inv_potions, "CLOCK", duration.getOrDefault(p.getUniqueId(),1), 31, Message.getMessage(p.getUniqueId(), "potions_time"));
        Item.create(inv_potions, "RED_STAINED_GLASS_PANE", 1, 32, Message.getMessage(p.getUniqueId(), "potions_remove_all"));
        Item.create(inv_potions, "BEACON", level.getOrDefault(p.getUniqueId(), 1), 33, Message.getMessage(p.getUniqueId(), "potions_level"));

        switch (AdminGUI.gui_type){
            case 1:
                Item.after_createPlayerHead(inv_potions, skulls.get("MHF_Redstone"),1,36, Message.getMessage(p.getUniqueId(), "potions_back"));
                break;
            default:
                Item.create(inv_potions, "REDSTONE_BLOCK",1,36, Message.getMessage(p.getUniqueId(), "potions_back"));
                break;
        }

        return inv_potions;
    }

    public Inventory GUI_Spawner(Player p, Player target){

        String inventory_spawner_name = Message.getMessage(p.getUniqueId(), "inventory_spawner").replace("{player}", target.getName());
        Inventory inv_spawner = Bukkit.createInventory(null, 54, inventory_spawner_name);

        target_player.put(p, target);

        if(Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.15")){
            for(Material_Version_15 material : Material_Version_15.values()){
                Item.create(inv_spawner, material.name(), 1, material.ordinal()+1, Message.getMessage(p.getUniqueId(), Message_Version_15.values()[material.ordinal()].name()));
            }
        }else if (Bukkit.getVersion().contains("1.14")) {
            for(Material_Version_14 material : Material_Version_14.values()){
                Item.create(inv_spawner, material.name(), 1, material.ordinal()+1, Message.getMessage(p.getUniqueId(), Message_Version_14.values()[material.ordinal()].name()));
            }
        }else if(Bukkit.getVersion().contains("1.13")){
            for(Material_Version_13 material : Material_Version_13.values()){
                Item.create(inv_spawner, material.name(), 1, material.ordinal()+1, Message.getMessage(p.getUniqueId(), Message_Version_13.values()[material.ordinal()].name()));
            }
        }else if(Bukkit.getVersion().contains("1.12")){
            for(Material_Version_12 material : Material_Version_12.values()){
                Item.create(inv_spawner, material.name(), 1, material.ordinal()+1, Message.getMessage(p.getUniqueId(), Message_Version_12.values()[material.ordinal()].name()));
            }
        }else if(Bukkit.getVersion().contains("1.11")){
            for(Material_Version_11 material : Material_Version_11.values()){
                Item.create(inv_spawner, material.name(), 1, material.ordinal()+1, Message.getMessage(p.getUniqueId(), Message_Version_11.values()[material.ordinal()].name()));
            }
        }else if(Bukkit.getVersion().contains("1.10")){
            for(Material_Version_10 material : Material_Version_10.values()){
                Item.create(inv_spawner, material.name(), 1, material.ordinal()+1, Message.getMessage(p.getUniqueId(), Message_Version_10.values()[material.ordinal()].name()));
            }
        }else if(Bukkit.getVersion().contains("1.9")){
            for(Material_Version_9 material : Material_Version_9.values()){
                Item.create(inv_spawner, material.name(), 1, material.ordinal()+1, Message.getMessage(p.getUniqueId(), Message_Version_9.values()[material.ordinal()].name()));
            }
        }else if(Bukkit.getVersion().contains("1.8")){
            for(Material_Version_8 material : Material_Version_8.values()){
                Item.create(inv_spawner, material.name(), 1, material.ordinal()+1, Message.getMessage(p.getUniqueId(), Message_Version_8.values()[material.ordinal()].name()));
            }
        }

        switch (AdminGUI.gui_type){
            case 1:
                Item.after_createPlayerHead(inv_spawner, skulls.get("MHF_Redstone"),1,54, Message.getMessage(p.getUniqueId(), "spawner_back"));
                break;
            default:
                Item.create(inv_spawner, "REDSTONE_BLOCK",1,54, Message.getMessage(p.getUniqueId(), "spawner_back"));
                break;
        }

        return inv_spawner;
    }

    public Inventory GUI_Money(Player p, Player target){

        String inventory_money_name = Message.getMessage(p.getUniqueId(), "inventory_money").replace("{player}", target.getName());
        target_player.put(p, target);

        Inventory inv_money = Bukkit.createInventory(null, 27, inventory_money_name);

        if(target.isOnline()){

            for(int i = 1; i < 27; i++){
                Item.create(inv_money, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
            }

            Item.create(inv_money, "PAPER", 1, 12, Message.getMessage(p.getUniqueId(), "money_give"));
            Item.create(inv_money, "BOOK", 1, 14, Message.getMessage(p.getUniqueId(), "money_set"));
            Item.create(inv_money, "PAPER", 1, 16, Message.getMessage(p.getUniqueId(), "money_take"));

        }else{
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
            p.closeInventory();
        }

        switch (AdminGUI.gui_type){
            case 1:
                Item.after_createPlayerHead(inv_money, skulls.get("MHF_Redstone"),1,27, Message.getMessage(p.getUniqueId(), "money_back"));
                break;
            default:
                Item.create(inv_money, "REDSTONE_BLOCK",1,27, Message.getMessage(p.getUniqueId(), "money_back"));
                break;
        }

        return inv_money;
    }

    public Inventory GUI_Money_Amount(Player p, Player target, int option){

        String inventory_money_amount_name;

        switch (option){
            case 1:
                inventory_money_amount_name = Message.getMessage(p.getUniqueId(), "inventory_money_give").replace("{player}", target.getName());
                break;
            case 3:
                inventory_money_amount_name = Message.getMessage(p.getUniqueId(), "inventory_money_take").replace("{player}", target.getName());
                break;
            default:
                inventory_money_amount_name = Message.getMessage(p.getUniqueId(), "inventory_money_set").replace("{player}", target.getName());
        }
        target_player.put(p, target);

        Inventory inv_money_amount = Bukkit.createInventory(null, 36, inventory_money_amount_name);

        if(target.isOnline()){

            for(int i = 1; i < 36; i++){
                Item.create(inv_money_amount, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
            }

            for (int i = 1; i <= 10; i++){
                Item.create(inv_money_amount, "PAPER", 1, i, "&a&l" + AdminGUI.getEconomy().format(i*100));
            }

            for (int i = 11, j = 1; i < 20; i++, j++){
                Item.create(inv_money_amount, "PAPER", 1, i, "&a&l" + AdminGUI.getEconomy().format(j * 1500));
            }

            for (int i = 20, j = 1; i <= 25; i++, j++){
                Item.create(inv_money_amount, "PAPER", 1, i, "&a&l" + AdminGUI.getEconomy().format(j * 15000));
            }

            for (int i = 26, j = 1; i < 36; i++, j++){
                Item.create(inv_money_amount, "PAPER", 1, i, "&a&l" + AdminGUI.getEconomy().format(j * 100000));
            }

        }else{
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
            p.closeInventory();
        }

        switch (AdminGUI.gui_type){
            case 1:
                Item.after_createPlayerHead(inv_money_amount, skulls.get("MHF_Redstone"),1,36, Message.getMessage(p.getUniqueId(), "money_back"));
                break;
            default:
                Item.create(inv_money_amount, "REDSTONE_BLOCK",1,36, Message.getMessage(p.getUniqueId(), "money_back"));
                break;
        }

        return inv_money_amount;
    }

    public Inventory GUI_Inventory(Player p, Player target) {

        String inventory_inventory_name = Message.getMessage(p.getUniqueId(), "inventory_inventory").replace("{player}", target.getName());
        target_player.put(p, target);

        Inventory inv_inventory = Bukkit.createInventory(null, 54, inventory_inventory_name);

        if(target.isOnline()){

            ItemStack[] items = target.getInventory().getContents();
            ItemStack[] armor = target.getInventory().getArmorContents();

            for(int i = 0; i < items.length; i++){
                if(items[i] != null){
                    inv_inventory.setItem(i, items[i]);
                }else{
                    inv_inventory.setItem(i, null);
                }
            }

            for (int i = 0, j = 36; i < armor.length; i++, j++){
                if(armor[i] != null){
                    inv_inventory.setItem(j, armor[i]);
                }else{
                    inv_inventory.setItem(j, null);
                }
            }
        }else{
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
            p.closeInventory();
        }

        for (int i = 42; i < 54; i++){
            Item.create(inv_inventory, gui_color.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("gui_default_color", "LIGHT_BLUE_STAINED_GLASS_PANE")), 1, i, " ");
        }

        Item.create(inv_inventory, "GREEN_TERRACOTTA", 1, 46, Message.getMessage(p.getUniqueId(), "inventory_refresh"));

        Item.create(inv_inventory, "BLUE_TERRACOTTA", 1, 50, Message.getMessage(p.getUniqueId(), "inventory_clear"));

        switch (AdminGUI.gui_type){
            case 1:
                Item.after_createPlayerHead(inv_inventory, skulls.get("MHF_Redstone"),1,54, Message.getMessage(p.getUniqueId(), "inventory_back"));
                break;
            default:
                Item.create(inv_inventory, "REDSTONE_BLOCK",1,54, Message.getMessage(p.getUniqueId(), "inventory_back"));
                break;
        }

        return inv_inventory;
    }

    public void clicked_main(Player p, int slot, ItemStack clicked, Inventory inv, boolean isLeftClick){

        if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "main_quit"))){
            p.closeInventory();
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "main_player").replace("{player}", p.getName()))) {
            p.openInventory(GUI_Player(p));
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "main_world"))){
            p.openInventory(GUI_World(p));
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "main_players"))){
            //TODO: Bungee
            if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)){
                Channel.send(p.getName(),"send", "online_players");
            }
            p.openInventory(GUI_Players(p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "main_plugins"))){
            custom_method.put(p.getUniqueId(), 0);
            p.openInventory(GUI_Plugins(p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "main_maintenance_mode"))) {
            if(p.hasPermission("admingui.maintenance.manage")){
                if (maintenance_mode) {
                    maintenance_mode = false;
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_maintenance_disabled"));
                    p.closeInventory();
                } else {
                    maintenance_mode = true;
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_maintenance_enabled"));
                    p.closeInventory();
                    for (Player pl : getServer().getOnlinePlayers()) {
                        if (!pl.isOp() && !pl.hasPermission("admingui.maintenance")) {
                            pl.kickPlayer(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_maintenance"));
                        }
                    }
                }
            }else{
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "permission"));
                p.closeInventory();
            }
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "main_unban_players"))){
            if(isLeftClick){
                p.openInventory(GUI_Unban_Players(p));
            }else{
                p.openInventory(GUI_Unmute_Players(p));
            }
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "main_language") + language.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("default_language")))){
            String lang = language.getOrDefault(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("default_language"));
            if(Language.enabled_languages.size() > 1){
                for (int i = 0; i < Language.enabled_languages.size(); i++) {
                    if(Language.enabled_languages.get(i).equals(lang)){
                        if(Language.enabled_languages.size()-1 == i){
                            language.put(p.getUniqueId(), Language.enabled_languages.get(0));
                        }else{
                            language.put(p.getUniqueId(), Language.enabled_languages.get(i+1));
                        }
                        break;
                    }
                }
            }
            p.openInventory(GUI_Main(p));
        }else if(InventoryGUI.getClickedItem(clicked, "  ")){
            gui_color.put(p.getUniqueId(), clicked.getType().toString());
            p.openInventory(GUI_Main(p));
        }
    }

    public void clicked_player(Player p, int slot, ItemStack clicked, Inventory inv){

        if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_back"))) {
            p.openInventory(GUI_Main(p));
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "player_info").replace("{player}", p.getName()))){
            p.openInventory(GUI_Player(p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_heal"))){
            p.setHealth(20);
            p.setFireTicks(0);
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_heal"));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_feed"))){
            p.setFoodLevel(20);
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_feed"));
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "player_survival"))){
            if(p.hasPermission("admingui.gamemode.adventure")){
                p.setGameMode(GameMode.ADVENTURE);
            }else if(p.hasPermission("admingui.gamemode.creative")){
                p.setGameMode(GameMode.CREATIVE);
            }else if(p.hasPermission("admingui.gamemode.spectator")){
                p.setGameMode(GameMode.SPECTATOR);
            }else{
                p.setGameMode(GameMode.SURVIVAL);
            }
            p.openInventory(GUI_Player(p));
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "player_adventure"))){
            if(p.hasPermission("admingui.gamemode.creative")){
                p.setGameMode(GameMode.CREATIVE);
            }else if(p.hasPermission("admingui.gamemode.spectator")){
                p.setGameMode(GameMode.SPECTATOR);
            }else if(p.hasPermission("admingui.gamemode.survival")){
                p.setGameMode(GameMode.SURVIVAL);
            }else{
                p.setGameMode(GameMode.ADVENTURE);
            }
            p.openInventory(GUI_Player(p));
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "player_creative"))){
            if(p.hasPermission("admingui.gamemode.spectator")){
                p.setGameMode(GameMode.SPECTATOR);
            }else if(p.hasPermission("admingui.gamemode.survival")){
                p.setGameMode(GameMode.SURVIVAL);
            }else if(p.hasPermission("admingui.gamemode.adventure")){
                p.setGameMode(GameMode.ADVENTURE);
            }else{
                p.setGameMode(GameMode.CREATIVE);
            }
            p.openInventory(GUI_Player(p));
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "player_spectator"))){
            if(p.hasPermission("admingui.gamemode.survival")){
                p.setGameMode(GameMode.SURVIVAL);
            }else if(p.hasPermission("admingui.gamemode.adventure")){
                p.setGameMode(GameMode.ADVENTURE);
            }else if(p.hasPermission("admingui.gamemode.creative")){
                p.setGameMode(GameMode.CREATIVE);
            }else{
                p.setGameMode(GameMode.SPECTATOR);
            }
            p.openInventory(GUI_Player(p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_god_enabled"))){
            if(Bukkit.getVersion().contains("1.8")){
                god.put(p.getUniqueId(), true);
            }else{
                p.setInvulnerable(true);
            }
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_god_enabled"));
            p.openInventory(GUI_Player(p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_god_disabled"))){
            if(Bukkit.getVersion().contains("1.8")){
                god.put(p.getUniqueId(), false);
            }else{
                p.setInvulnerable(false);
            }
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_god_disabled"));
            p.openInventory(GUI_Player(p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_potions"))){
            p.openInventory(GUI_potions(p, p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_spawner"))){
            p.openInventory(GUI_Spawner(p, p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_kill"))){
            p.setHealth(0);
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_kill"));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_burn"))){
            p.setFireTicks(500);
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_burn"));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_lightning"))) {
            p.getWorld().strikeLightning(p.getLocation());
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_firework"))){
            Fireworks.createRandom(p);
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_money"))){
            p.openInventory(GUI_Money(p, p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_vanish_enabled"))){
            if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
                VanishAPI.hidePlayer(p);
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_hide"));
            }else{
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "vanish_required"));
            }
            p.closeInventory();
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_vanish_disabled"))){
            if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
                VanishAPI.showPlayer(p);
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_visible"));
            }else{
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "vanish_required"));
            }
            p.closeInventory();
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_custom"))){
            custom_method.put(p.getUniqueId(), 1);
            p.openInventory(GUI_Plugins(p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "player_chat_color"))){
            switch (chat_color.getOrDefault(p.getUniqueId(), "LIGHT_GRAY_WOOL")){
                case "WHITE_WOOL":
                    chat_color.put(p.getUniqueId(), "ORANGE_WOOL");
                    break;
                case "ORANGE_WOOL":
                    chat_color.put(p.getUniqueId(), "MAGENTA_WOOL");
                    break;
                case "MAGENTA_WOOL":
                    chat_color.put(p.getUniqueId(), "LIGHT_BLUE_WOOL");
                    break;
                case "LIGHT_BLUE_WOOL":
                    chat_color.put(p.getUniqueId(), "YELLOW_WOOL");
                    break;
                case "YELLOW_WOOL":
                    chat_color.put(p.getUniqueId(), "LIME_WOOL");
                    break;
                case "LIME_WOOL":
                    chat_color.put(p.getUniqueId(), "GRAY_WOOL");
                    break;
                case "GRAY_WOOL":
                    chat_color.put(p.getUniqueId(), "CLOCK");
                    break;
                case "CLOCK":
                    if(getVersion().contains("1.16")){
                        chat_color.put(p.getUniqueId(), "EXPERIENCE_BOTTLE");
                    }else{
                        chat_color.put(p.getUniqueId(), "LIGHT_GRAY_WOOL");
                    }
                    break;
                case "EXPERIENCE_BOTTLE":
                    chat_color.put(p.getUniqueId(), "LIGHT_GRAY_WOOL");
                    break;
                case "LIGHT_GRAY_WOOL":
                    chat_color.put(p.getUniqueId(), "CYAN_WOOL");
                    break;
                case "CYAN_WOOL":
                    chat_color.put(p.getUniqueId(), "PURPLE_WOOL");
                    break;
                case "PURPLE_WOOL":
                    chat_color.put(p.getUniqueId(), "BLUE_WOOL");
                    break;
                case "BLUE_WOOL":
                    chat_color.put(p.getUniqueId(), "GREEN_WOOL");
                    break;
                case "GREEN_WOOL":
                    chat_color.put(p.getUniqueId(), "RED_WOOL");
                    break;
                case "RED_WOOL":
                    chat_color.put(p.getUniqueId(), "BLACK_WOOL");
                    break;
                case "BLACK_WOOL":
                    chat_color.put(p.getUniqueId(), "WHITE_WOOL");
                    break;
            }
            p.openInventory(GUI_Player(p));
        }
    }

    public void clicked_world(Player p, int slot, ItemStack clicked, Inventory inv){

        if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "world_back"))) {
            p.openInventory(GUI_Main(p));
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "world_day"))){
            p.getPlayer().getWorld().setTime(13000);
            p.openInventory(GUI_World(p));
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "world_night"))){
            p.getPlayer().getWorld().setTime(0);
            p.openInventory(GUI_World(p));
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "world_clear"))){
            World world = p.getWorld();
            world.setThundering(false);
            world.setStorm(true);
            p.openInventory(GUI_World(p));
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "world_rain"))){
            World world = p.getWorld();
            world.setStorm(true);
            world.setThundering(true);
            p.openInventory(GUI_World(p));
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "world_thunder"))){
            World world = p.getWorld();
            world.setStorm(false);
            world.setThundering(false);
            p.openInventory(GUI_World(p));
        }

    }

    public void clicked_players(Player p, int slot, ItemStack clicked, Inventory inv){

        if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "players_back"))){
            p.openInventory(GUI_Main(p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "players_previous"))){
            page.put(p.getUniqueId(), page.getOrDefault(p.getUniqueId(),1)-1);
            p.openInventory(GUI_Players(p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "players_next"))){
            page.put(p.getUniqueId(), page.getOrDefault(p.getUniqueId(),1)+1);
            p.openInventory(GUI_Players(p));
        }else if(clicked.getItemMeta().getLore() != null){
            if(clicked.getItemMeta().getLore().get(0).equals(Message.getMessage(p.getUniqueId(), "players_more"))){
                Player target_p = getServer().getPlayer(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
                if(target_p != null){
                    if(p.getUniqueId() != target_p.getUniqueId()){
                        target_player.put(p, target_p);
                        p.openInventory(GUI_Players_Settings(p,target_p, target_p.getName()));
                    }else{
                        p.openInventory(GUI_Player(p));
                    }
                }else if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false) && online_players.contains(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))) {
                    //TODO: Bungee
                    switch(AdminGUI.getInstance().getConf().getInt("control_type", 0)){
                        case 0:
                            Channel.send(p.getName(),"connect", ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
                            break;
                        case 1:
                            target_player.put(p, null);
                            p.openInventory(GUI_Players_Settings(p,null, ChatColor.stripColor(clicked.getItemMeta().getDisplayName())));
                            break;
                        default:
                            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.chat("&cPlayer " + ChatColor.stripColor(clicked.getItemMeta().getDisplayName()) + " is not located in the same server as you."));
                            break;
                    }
                }else{
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
                    p.closeInventory();
                }
            }
        }

    }

    public void clicked_plugins(Player p, int slot, ItemStack clicked, Inventory inv){
        if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "plugins_back"))){
            p.openInventory(GUI_Main(p));
        }else if(clicked.getType() != Material.AIR && clicked.getType() != XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE.parseMaterial()){
            plugin_slot.put(p.getUniqueId(), slot+1);
            p.openInventory(GUI_Commands(p, slot+1));
        }
    }

    public void clicked_commands(Player p, int slot, ItemStack clicked, Inventory inv){
        slot++;
        if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "commands_back"))){
            p.openInventory(GUI_Plugins(p));
        }else if(clicked.getType() != Material.AIR && clicked.getType() != XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE.parseMaterial() && clicked.getType() != XMaterial.RED_STAINED_GLASS_PANE.parseMaterial()){
            YamlConfiguration yamlConfiguration;

            switch (custom_method.getOrDefault(p.getUniqueId(), 0)){
                case 1:
                    yamlConfiguration = AdminGUI.getInstance().getComm();
                    break;
                case 2:
                    yamlConfiguration = AdminGUI.getInstance().getComo();
                    break;
                default:
                    yamlConfiguration = AdminGUI.getInstance().getPlug();
                    break;
            }

            if(yamlConfiguration.getBoolean("plugins."+ plugin_slot.getOrDefault(p.getUniqueId(), 1) +".commands."+slot+".console_sender")){
                if(yamlConfiguration.isList("plugins."+plugin_slot.getOrDefault(p.getUniqueId(), 1)+".commands."+slot+".command")){
                    List<String> comm_list = yamlConfiguration.getStringList("plugins."+plugin_slot.getOrDefault(p.getUniqueId(), 1)+".commands."+slot+".command");
                    for(String command : comm_list){
                        getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("/","").replace("{player}", p.getName()).replace("{target_player}", target_player.getOrDefault(p, p).getName()));
                    }
                }else{
                    getServer().dispatchCommand(Bukkit.getConsoleSender(), yamlConfiguration.getString("plugins."+plugin_slot.getOrDefault(p.getUniqueId(), 1)+".commands."+slot+".command").replace("/","").replace("{player}", p.getName()).replace("{target_player}", target_player.getOrDefault(p, p).getName()));
                }
            }else{
                if(yamlConfiguration.isList("plugins."+plugin_slot.getOrDefault(p.getUniqueId(), 1)+".commands."+slot+".command")){
                    List<String> comm_list = yamlConfiguration.getStringList("plugins."+plugin_slot.getOrDefault(p.getUniqueId(), 1)+".commands."+slot+".command");
                    for(String command : comm_list){
                        getServer().dispatchCommand(p, command.replace("/","").replace("{player}", p.getName()).replace("{target_player}", target_player.getOrDefault(p, p).getName()));
                    }
                }else{
                    getServer().dispatchCommand(p, yamlConfiguration.getString("plugins."+plugin_slot.getOrDefault(p.getUniqueId(), 1)+".commands."+slot+".command").replace("/","").replace("{player}", p.getName()).replace("{target_player}", target_player.getOrDefault(p, p).getName()));
                }
            }
        }
    }

    public void clicked_unban_players(Player p, int slot, ItemStack clicked, Inventory inv){

        if(clicked.getItemMeta().getLore() != null){
            if(clicked.getItemMeta().getLore().contains(Message.getMessage(p.getUniqueId(), "unban_more"))){
                if(Bukkit.getPluginManager().isPluginEnabled("AdminBans")){
                    if(AdminBansAPI.unBanPlayer(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))){
                        p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_unban_player").replace("{player}", ChatColor.stripColor(clicked.getItemMeta().getDisplayName())));
                    }else{
                        p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
                    }
                }else if(Bukkit.getPluginManager().isPluginEnabled("LiteBans")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "unban " + ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
                }else{
                    OfflinePlayer target_p = getServer().getOfflinePlayer(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
                    if(target_p.isBanned()){
                        Bukkit.getBanList(BanList.Type.NAME).pardon(target_p.getName());
                        p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_unban_player").replace("{player}", target_p.getName()));
                    }else{
                        p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
                    }
                }

                p.closeInventory();
            }
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "unban_back"))){
            p.openInventory(GUI_Main(p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "unban_previous"))){
            unban_page.put(p.getUniqueId(), unban_page.getOrDefault(p.getUniqueId(),1)-1);
            p.openInventory(GUI_Unban_Players(p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "unban_next"))){
            unban_page.put(p.getUniqueId(), unban_page.getOrDefault(p.getUniqueId(),1)+1);
            p.openInventory(GUI_Unban_Players(p));
        }

    }

    public void clicked_unmute_players(Player p, int slot, ItemStack clicked, Inventory inv){

        if(clicked.getItemMeta().getLore() != null){
            if(clicked.getItemMeta().getLore().contains(Message.getMessage(p.getUniqueId(), "unmute_more"))){
                if(Bukkit.getPluginManager().isPluginEnabled("AdminBans")){
                    if(AdminBansAPI.unMutePlayer(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))){
                        p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_unmute_player").replace("{player}", ChatColor.stripColor(clicked.getItemMeta().getDisplayName())));
                    }else{
                        p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
                    }
                }else if(Bukkit.getPluginManager().isPluginEnabled("LiteBans")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "unmute " + ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
                }
                p.closeInventory();
            }
        }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "unmute_back"))){
            p.openInventory(GUI_Main(p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "unmute_previous"))){
            unmute_page.put(p.getUniqueId(), unmute_page.getOrDefault(p.getUniqueId(),1)-1);
            p.openInventory(GUI_Unban_Players(p));
        }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "unmute_next"))){
            unmute_page.put(p.getUniqueId(), unmute_page.getOrDefault(p.getUniqueId(),1)+1);
            p.openInventory(GUI_Unban_Players(p));
        }

    }

    public void clicked_players_settings(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player){

        if(target_player.isOnline()){
            if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "players_settings_back"))){
                p.openInventory(GUI_Players(p));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "players_settings_info").replace("{player}", target_player.getName()))){
                p.openInventory(GUI_Players_Settings(p, target_player, target_player.getName()));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "players_settings_actions"))){
                p.openInventory(GUI_Actions(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "players_settings_money"))){
                p.openInventory(GUI_Money(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "players_settings_kick_player"))){
                p.openInventory(GUI_Kick(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "players_settings_ban_player"))){
                p.openInventory(GUI_Ban(p, target_player));
            }
        }else{
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
            p.closeInventory();
        }

    }

    public void clicked_actions(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player){

        if(target_player.isOnline()){
            if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_back"))){
                p.openInventory(GUI_Players_Settings(p, target_player, target_player.getName()));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_info").replace("{player}", target_player.getName()))){
                p.openInventory(GUI_Actions(p,target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_survival"))){
                if(p.hasPermission("admingui.gamemode.other.adventure")){
                    target_player.setGameMode(GameMode.ADVENTURE);
                }else if(p.hasPermission("admingui.gamemode.other.creative")){
                    target_player.setGameMode(GameMode.CREATIVE);
                }else if(p.hasPermission("admingui.gamemode.other.spectator")){
                    target_player.setGameMode(GameMode.SPECTATOR);
                }else{
                    target_player.setGameMode(GameMode.SURVIVAL);
                }
                p.openInventory(GUI_Actions(p,target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_adventure"))){
                if(p.hasPermission("admingui.gamemode.other.creative")){
                    target_player.setGameMode(GameMode.CREATIVE);
                }else if(p.hasPermission("admingui.gamemode.other.spectator")){
                    target_player.setGameMode(GameMode.SPECTATOR);
                }else if(p.hasPermission("admingui.gamemode.other.survival")){
                    target_player.setGameMode(GameMode.SURVIVAL);
                }else{
                    target_player.setGameMode(GameMode.ADVENTURE);
                }
                p.openInventory(GUI_Actions(p,target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_creative"))){
                if(p.hasPermission("admingui.gamemode.other.spectator")){
                    target_player.setGameMode(GameMode.SPECTATOR);
                }else if(p.hasPermission("admingui.gamemode.other.survival")){
                    target_player.setGameMode(GameMode.SURVIVAL);
                }else if(p.hasPermission("admingui.gamemode.other.adventure")){
                    target_player.setGameMode(GameMode.ADVENTURE);
                }else{
                    target_player.setGameMode(GameMode.CREATIVE);
                }
                p.openInventory(GUI_Actions(p,target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_spectator"))){
                if(p.hasPermission("admingui.gamemode.other.survival")){
                    target_player.setGameMode(GameMode.SURVIVAL);
                }else if(p.hasPermission("admingui.gamemode.other.adventure")){
                    target_player.setGameMode(GameMode.ADVENTURE);
                }else if(p.hasPermission("admingui.gamemode.other.creative")){
                    target_player.setGameMode(GameMode.CREATIVE);
                }else{
                    target_player.setGameMode(GameMode.SPECTATOR);
                }
                p.openInventory(GUI_Actions(p,target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_teleport_to_player"))){
                p.teleport(target_player.getLocation());
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_target_player_teleport").replace("{player}", target_player.getName()));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_kill_player"))){
                target_player.setHealth(0);
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_kill").replace("{player}", target_player.getName()));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_burn_player"))){
                target_player.setFireTicks(500);
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_burn").replace("{player}", target_player.getName()));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_teleport_player_to_you"))){
                target_player.teleport(p.getLocation());
                target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_teleport").replace("{player}", p.getName()));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_heal"))){
                target_player.setHealth(20);
                target_player.setFireTicks(0);
                target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_heal").replace("{player}", p.getName()));
                p.sendMessage(Message.chat(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_heal").replace("{player}", target_player.getName())));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_feed"))){
                target_player.setFoodLevel(20);
                target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_feed").replace("{player}", p.getName()));
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_feed").replace("{player}", target_player.getName()));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_god_enabled"))){
                if(Bukkit.getVersion().contains("1.8")){
                    god.put(target_player.getUniqueId(), true);
                }else{
                    target_player.setInvulnerable(true);
                }
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_god_enabled").replace("{player}", target_player.getName()));
                target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_god_enabled").replace("{player}", p.getName()));
                p.openInventory(GUI_Actions(p,target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_god_disabled"))){
                if(Bukkit.getVersion().contains("1.8")){
                    god.put(target_player.getUniqueId(), false);
                }else{
                    target_player.setInvulnerable(false);
                }
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_god_disabled").replace("{player}", target_player.getName()));
                target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_god_disabled").replace("{player}", p.getName()));
                p.openInventory(GUI_Actions(p,target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_potions"))){
                p.openInventory(GUI_potions(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_spawner"))){
                p.openInventory(GUI_Spawner(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_inventory"))){
                p.openInventory(GUI_Inventory(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_vanish_enabled"))){
                if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
                    VanishAPI.hidePlayer(target_player);
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_hide").replace("{player}", target_player.getName()));
                    target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_hide"));
                }else{
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "vanish_required"));
                }
                p.closeInventory();
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_vanish_disabled"))){
                if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
                    VanishAPI.showPlayer(target_player);
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_visible").replace("{player}", target_player.getName()));
                    target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_visible"));
                }else{
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "vanish_required"));
                }
                p.closeInventory();
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_lightning"))){
                target_player.getWorld().strikeLightning(target_player.getLocation());
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_firework"))){
                Fireworks.createRandom(target_player);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_fakeop"))){
                Bukkit.broadcastMessage(Message.chat("&7&o[Server: Made " + target_player.getName() +" a server operator]"));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_custom"))){
                custom_method.put(p.getUniqueId(), 2);
                p.openInventory(GUI_Plugins(p));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_chat_color"))){
                switch (chat_color.getOrDefault(target_player.getUniqueId(), "LIGHT_GRAY_WOOL")){
                    case "WHITE_WOOL":
                        chat_color.put(target_player.getUniqueId(), "ORANGE_WOOL");
                        break;
                    case "ORANGE_WOOL":
                        chat_color.put(target_player.getUniqueId(), "MAGENTA_WOOL");
                        break;
                    case "MAGENTA_WOOL":
                        chat_color.put(target_player.getUniqueId(), "LIGHT_BLUE_WOOL");
                        break;
                    case "LIGHT_BLUE_WOOL":
                        chat_color.put(target_player.getUniqueId(), "YELLOW_WOOL");
                        break;
                    case "YELLOW_WOOL":
                        chat_color.put(target_player.getUniqueId(), "LIME_WOOL");
                        break;
                    case "LIME_WOOL":
                        chat_color.put(target_player.getUniqueId(), "GRAY_WOOL");
                        break;
                    case "GRAY_WOOL":
                        chat_color.put(target_player.getUniqueId(), "CLOCK");
                        break;
                    case "CLOCK":
                        if(getVersion().contains("1.16")){
                            chat_color.put(target_player.getUniqueId(), "EXPERIENCE_BOTTLE");
                        }else{
                            chat_color.put(target_player.getUniqueId(), "LIGHT_GRAY_WOOL");
                        }
                        break;
                    case "EXPERIENCE_BOTTLE":
                        chat_color.put(target_player.getUniqueId(), "LIGHT_GRAY_WOOL");
                        break;
                    case "LIGHT_GRAY_WOOL":
                        chat_color.put(target_player.getUniqueId(), "CYAN_WOOL");
                        break;
                    case "CYAN_WOOL":
                        chat_color.put(target_player.getUniqueId(), "PURPLE_WOOL");
                        break;
                    case "PURPLE_WOOL":
                        chat_color.put(target_player.getUniqueId(), "BLUE_WOOL");
                        break;
                    case "BLUE_WOOL":
                        chat_color.put(target_player.getUniqueId(), "GREEN_WOOL");
                        break;
                    case "GREEN_WOOL":
                        chat_color.put(target_player.getUniqueId(), "RED_WOOL");
                        break;
                    case "RED_WOOL":
                        chat_color.put(target_player.getUniqueId(), "BLACK_WOOL");
                        break;
                    case "BLACK_WOOL":
                        chat_color.put(target_player.getUniqueId(), "WHITE_WOOL");
                        break;
                }
                p.openInventory(GUI_Actions(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_freeze_enabled")) || InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_freeze_disabled"))){
                if(freeze.getOrDefault(target_player.getUniqueId(), false)){
                    freeze.put(target_player.getUniqueId(), false);
                    custom_chat_channel.remove(p.getUniqueId());
                    target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "message_freeze_disabled").replace("{player}", p.getName()));
                }else{
                    if(!target_player.hasPermission("admingui.freeze.bypass")){
                        freeze.put(target_player.getUniqueId(), true);
                        custom_chat_channel.put(p.getUniqueId(), AdminGUI.getInstance().getConf().getString("freeze_admin_chat", "adminchat"));
                        target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "message_freeze_enabled").replace("{player}", p.getName()));
                    }else{
                        p.closeInventory();
                        p.sendMessage(Message.getMessage(p.getUniqueId(), "permission"));
                    }
                }
                p.openInventory(GUI_Actions(p, target_player));
            }
        }else if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)){
            //TODO: Bungee
            if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_back"))){
                p.openInventory(GUI_Players_Settings(p, target_player, target_player.getName()));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_info").replace("{player}", target_player.getName()))){
                p.openInventory(GUI_Actions(p,target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_survival"))){
                Channel.send(p.getName(),"gamemode", target_player.getName(), "adventure");
                //target_player.setGameMode(GameMode.ADVENTURE);
                p.openInventory(GUI_Actions(p,target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_adventure"))){
                target_player.setGameMode(GameMode.CREATIVE);
                p.openInventory(GUI_Actions(p,target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_creative"))){
                target_player.setGameMode(GameMode.SPECTATOR);
                p.openInventory(GUI_Actions(p,target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_spectator"))){
                target_player.setGameMode(GameMode.SURVIVAL);
                p.openInventory(GUI_Actions(p,target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_teleport_to_player"))){
                p.teleport(target_player.getLocation());
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_target_player_teleport").replace("{player}", target_player.getName()));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_kill_player"))){
                target_player.setHealth(0);
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_kill").replace("{player}", target_player.getName()));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_burn_player"))){
                target_player.setFireTicks(500);
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_burn").replace("{player}", target_player.getName()));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_teleport_player_to_you"))){
                target_player.teleport(p.getLocation());
                target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_teleport").replace("{player}", p.getName()));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_heal"))){
                target_player.setHealth(20);
                target_player.setFireTicks(0);
                target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_heal").replace("{player}", p.getName()));
                p.sendMessage(Message.chat(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_heal").replace("{player}", target_player.getName())));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_feed"))){
                target_player.setFoodLevel(20);
                target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_feed").replace("{player}", p.getName()));
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_feed").replace("{player}", target_player.getName()));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_god_enabled"))){
                if(Bukkit.getVersion().contains("1.8")){
                    god.put(target_player.getUniqueId(), true);
                }else{
                    target_player.setInvulnerable(true);
                }
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_god_enabled").replace("{player}", target_player.getName()));
                target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_god_enabled").replace("{player}", p.getName()));
                p.openInventory(GUI_Actions(p,target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_god_disabled"))){
                if(Bukkit.getVersion().contains("1.8")){
                    god.put(target_player.getUniqueId(), false);
                }else{
                    target_player.setInvulnerable(false);
                }
                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_god_disabled").replace("{player}", target_player.getName()));
                target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_god_disabled").replace("{player}", p.getName()));
                p.openInventory(GUI_Actions(p,target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_potions"))){
                p.openInventory(GUI_potions(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "actions_spawner"))){
                p.openInventory(GUI_Spawner(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_inventory"))){
                p.openInventory(GUI_Inventory(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_vanish_enabled"))){
                if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
                    VanishAPI.hidePlayer(target_player);
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_hide").replace("{player}", target_player.getName()));
                    target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_hide"));
                }else{
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "vanish_required"));
                }
                p.closeInventory();
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_vanish_disabled"))){
                if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
                    VanishAPI.showPlayer(target_player);
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_visible").replace("{player}", target_player.getName()));
                    target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_visible"));
                }else{
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "vanish_required"));
                }
                p.closeInventory();
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_lightning"))){
                target_player.getWorld().strikeLightning(target_player.getLocation());
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_firework"))){
                Fireworks.createRandom(target_player);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_fakeop"))){
                Bukkit.broadcastMessage(Message.chat("&7&o[Server: Made " + target_player.getName() +" a server operator]"));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_custom"))){
                custom_method.put(p.getUniqueId(), 2);
                p.openInventory(GUI_Plugins(p));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_chat_color"))){
                switch (chat_color.getOrDefault(target_player.getUniqueId(), "LIGHT_GRAY_WOOL")){
                    case "WHITE_WOOL":
                        chat_color.put(target_player.getUniqueId(), "ORANGE_WOOL");
                        break;
                    case "ORANGE_WOOL":
                        chat_color.put(target_player.getUniqueId(), "MAGENTA_WOOL");
                        break;
                    case "MAGENTA_WOOL":
                        chat_color.put(target_player.getUniqueId(), "LIGHT_BLUE_WOOL");
                        break;
                    case "LIGHT_BLUE_WOOL":
                        chat_color.put(target_player.getUniqueId(), "YELLOW_WOOL");
                        break;
                    case "YELLOW_WOOL":
                        chat_color.put(target_player.getUniqueId(), "LIME_WOOL");
                        break;
                    case "LIME_WOOL":
                        chat_color.put(target_player.getUniqueId(), "GRAY_WOOL");
                        break;
                    case "GRAY_WOOL":
                        chat_color.put(target_player.getUniqueId(), "CLOCK");
                        break;
                    case "CLOCK":
                        if(getVersion().contains("1.16")){
                            chat_color.put(target_player.getUniqueId(), "EXPERIENCE_BOTTLE");
                        }else{
                            chat_color.put(target_player.getUniqueId(), "LIGHT_GRAY_WOOL");
                        }
                        break;
                    case "EXPERIENCE_BOTTLE":
                        chat_color.put(target_player.getUniqueId(), "LIGHT_GRAY_WOOL");
                        break;
                    case "LIGHT_GRAY_WOOL":
                        chat_color.put(target_player.getUniqueId(), "CYAN_WOOL");
                        break;
                    case "CYAN_WOOL":
                        chat_color.put(target_player.getUniqueId(), "PURPLE_WOOL");
                        break;
                    case "PURPLE_WOOL":
                        chat_color.put(target_player.getUniqueId(), "BLUE_WOOL");
                        break;
                    case "BLUE_WOOL":
                        chat_color.put(target_player.getUniqueId(), "GREEN_WOOL");
                        break;
                    case "GREEN_WOOL":
                        chat_color.put(target_player.getUniqueId(), "RED_WOOL");
                        break;
                    case "RED_WOOL":
                        chat_color.put(target_player.getUniqueId(), "BLACK_WOOL");
                        break;
                    case "BLACK_WOOL":
                        chat_color.put(target_player.getUniqueId(), "WHITE_WOOL");
                        break;
                }
                p.openInventory(GUI_Actions(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_freeze_enabled")) || InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "actions_freeze_disabled"))){
                if(freeze.getOrDefault(target_player.getUniqueId(), false)){
                    freeze.put(target_player.getUniqueId(), false);
                    custom_chat_channel.remove(target_player.getUniqueId());
                    target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "message_freeze_disabled").replace("{player}", p.getName()));
                }else{
                    if(!target_player.hasPermission("admingui.freeze.bypass")){
                        freeze.put(target_player.getUniqueId(), true);
                        custom_chat_channel.put(target_player.getUniqueId(), AdminGUI.getInstance().getConf().getString("freeze_admin_chat", "adminchat"));
                        target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "message_freeze_enabled").replace("{player}", p.getName()));
                    }else{
                        p.closeInventory();
                        p.sendMessage(Message.getMessage(p.getUniqueId(), "permission"));
                    }
                }
                p.openInventory(GUI_Actions(p, target_player));
            }
        }else{
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
            p.closeInventory();
        }

    }

    public void clicked_kick(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player){

        if(target_player.isOnline()){
            if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "kick_back"))){
                p.openInventory(GUI_Players_Settings(p, target_player, target_player.getName()));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "ban_silence_disabled"))){
                kick_silence.put(p.getUniqueId(), true);
                p.openInventory(GUI_Kick(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "ban_silence_enabled"))){
                kick_silence.put(p.getUniqueId(), false);
                p.openInventory(GUI_Kick(p, target_player));
            }else if(clicked.getType() != Material.AIR && !Objects.requireNonNull(clicked.getItemMeta()).getDisplayName().equals(" ")){
                if(target_player.hasPermission("admingui.kick.bypass")){
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_kick_bypass"));
                }else{
                    if(getServer().getPluginManager().isPluginEnabled("AdminBans")){
                        AdminBansAPI.kickPlayer(p.getUniqueId().toString(), p.getName(), target_player.getUniqueId().toString(), target_player.getName(), AdminGUI.getInstance().getKick().getString("slots."+(slot+1)+".name"));
                    }else{
                        target_player.kickPlayer(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "kick") + Message.chat(AdminGUI.getInstance().getKick().getString("slots."+(slot+1)+".name")));
                    }
                    if(kick_silence.getOrDefault(p.getUniqueId(), false)){
                        p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_kick").replace("{player}", target_player.getName()).replace("{reason}", Message.chat(AdminGUI.getInstance().getKick().getString("slots."+(slot+1)+".name"))));
                    }else{
                        Bukkit.broadcastMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_kick").replace("{player}", target_player.getName()).replace("{reason}", Message.chat(AdminGUI.getInstance().getKick().getString("slots."+(slot+1)+".name"))));
                    }
                }
                p.closeInventory();
            }
        }else{
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
            p.closeInventory();
        }

    }

    public void clicked_ban(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player){

        long mil_year = 31556952000L;
        long mil_month = 2592000000L;
        long mil_day = 86400000L;
        long mil_hour = 3600000L;
        long mil_minute = 60000L;

        Date time = null;

        if(!(ban_minutes.getOrDefault(p.getUniqueId(), 0) == 0 && ban_hours.getOrDefault(p.getUniqueId(), 0) == 0 && ban_days.getOrDefault(p.getUniqueId(), 0) == 0 && ban_months.getOrDefault(p.getUniqueId(), 0) == 0 && ban_years.getOrDefault(p.getUniqueId(), 0) == 0)){
            time = new Date(System.currentTimeMillis()+(mil_minute*ban_minutes.getOrDefault(p.getUniqueId(),0))+(mil_hour*ban_hours.getOrDefault(p.getUniqueId(),0))+(mil_day*ban_days.getOrDefault(p.getUniqueId(),0))+(mil_month*ban_months.getOrDefault(p.getUniqueId(),0))+(mil_year*ban_years.getOrDefault(p.getUniqueId(),0)));
        }

        if(target_player.isOnline()){
            if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "ban_back"))) {
                p.openInventory(GUI_Players_Settings(p, target_player, target_player.getName()));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "ban_silence_disabled"))){
                ban_silence.put(p.getUniqueId(), true);
                p.openInventory(GUI_Ban(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "ban_silence_enabled"))){
                ban_silence.put(p.getUniqueId(), false);
                p.openInventory(GUI_Ban(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "ban_hacking"))){
                if(target_player.hasPermission("admingui.ban.bypass")){
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_ban_bypass"));
                }else{
                    TargetPlayer.ban(p.getUniqueId(), p.getName(), target_player.getUniqueId(), target_player.getName(), Message.getMessage(target_player.getUniqueId(), "ban_hacking"), time);

                    if(ban_silence.getOrDefault(p.getUniqueId(), false)){
                        p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_ban").replace("{player}", target_player.getName()).replace("{reason}", Message.getMessage(p.getUniqueId(), "ban_hacking")));
                    }else{
                        getServer().broadcastMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_ban").replace("{player}", target_player.getName()).replace("{reason}", Message.getMessage(p.getUniqueId(), "ban_hacking")));
                    }
                }
                p.closeInventory();
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "ban_griefing"))){
                if(target_player.hasPermission("admingui.ban.bypass")){
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_ban_bypass"));
                }else{
                    TargetPlayer.ban(p.getUniqueId(), p.getName(), target_player.getUniqueId(), target_player.getName(), Message.getMessage(target_player.getUniqueId(), "ban_griefing"), time);

                    if(ban_silence.getOrDefault(p.getUniqueId(), false)){
                        p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_ban").replace("{player}", target_player.getName()).replace("{reason}", Message.getMessage(p.getUniqueId(), "ban_griefing")));
                    }else{
                        getServer().broadcastMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_ban").replace("{player}", target_player.getName()).replace("{reason}", Message.getMessage(p.getUniqueId(), "ban_griefing")));
                    }
                }
                p.closeInventory();
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "ban_spamming"))){
                if(target_player.hasPermission("admingui.ban.bypass")){
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_ban_bypass"));
                }else{
                    TargetPlayer.ban(p.getUniqueId(), p.getName(), target_player.getUniqueId(), target_player.getName(), Message.getMessage(target_player.getUniqueId(), "ban_spamming"), time);

                    if(ban_silence.getOrDefault(p.getUniqueId(), false)){
                        p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_ban").replace("{player}", target_player.getName()).replace("{reason}", Message.getMessage(p.getUniqueId(), "ban_spamming")));
                    }else{
                        getServer().broadcastMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_ban").replace("{player}", target_player.getName()).replace("{reason}", Message.getMessage(p.getUniqueId(), "ban_spamming")));
                    }
                }
                p.closeInventory();
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "ban_advertising"))){
                if(target_player.hasPermission("admingui.ban.bypass")){
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_ban_bypass"));
                }else{
                    TargetPlayer.ban(p.getUniqueId(), p.getName(), target_player.getUniqueId(), target_player.getName(), Message.getMessage(target_player.getUniqueId(), "ban_advertising"), time);

                    if(ban_silence.getOrDefault(p.getUniqueId(), false)){
                        p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_ban").replace("{player}", target_player.getName()).replace("{reason}", Message.getMessage(p.getUniqueId(), "ban_advertising")));
                    }else{
                        getServer().broadcastMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_ban").replace("{player}", target_player.getName()).replace("{reason}", Message.getMessage(p.getUniqueId(), "ban_advertising")));
                    }
                }
                p.closeInventory();
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "ban_swearing"))){
                if(target_player.hasPermission("admingui.ban.bypass")){
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_ban_bypass"));
                }else{
                    TargetPlayer.ban(p.getUniqueId(), p.getName(), target_player.getUniqueId(), target_player.getName(), Message.getMessage(target_player.getUniqueId(), "ban_swearing"), time);

                    if(ban_silence.getOrDefault(p.getUniqueId(), false)){
                        p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_ban").replace("{player}", target_player.getName()).replace("{reason}", Message.getMessage(p.getUniqueId(), "ban_swearing")));
                    }else{
                        getServer().broadcastMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_ban").replace("{player}", target_player.getName()).replace("{reason}", Message.getMessage(p.getUniqueId(), "ban_swearing")));
                    }
                }
                p.closeInventory();
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "ban_years"))){
                switch (ban_years.getOrDefault(p.getUniqueId(),0)){
                    case 0:
                        ban_years.put(p.getUniqueId(), 1);
                        break;
                    case 1:
                        ban_years.put(p.getUniqueId(), 2);
                        break;
                    case 2:
                        ban_years.put(p.getUniqueId(), 3);
                        break;
                    case 3:
                        ban_years.put(p.getUniqueId(), 4);
                        break;
                    case 4:
                        ban_years.put(p.getUniqueId(), 5);
                        break;
                    case 5:
                        ban_years.put(p.getUniqueId(), 6);
                        break;
                    case 6:
                        ban_years.put(p.getUniqueId(), 7);
                        break;
                    case 7:
                        ban_years.put(p.getUniqueId(), 8);
                        break;
                    case 8:
                        ban_years.put(p.getUniqueId(), 9);
                        break;
                    case 9:
                        ban_years.put(p.getUniqueId(), 10);
                        break;
                    case 10:
                        ban_years.put(p.getUniqueId(), 15);
                        break;
                    case 15:
                        ban_years.put(p.getUniqueId(), 20);
                        break;
                    case 20:
                        ban_years.put(p.getUniqueId(), 30);
                        break;
                    case 30:
                        ban_years.put(p.getUniqueId(), 0);
                        break;
                }
                p.openInventory(GUI_Ban(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "ban_months"))){
                switch (ban_months.getOrDefault(p.getUniqueId(),0)){
                    case 0:
                        ban_months.put(p.getUniqueId(), 1);
                        break;
                    case 1:
                        ban_months.put(p.getUniqueId(), 2);
                        break;
                    case 2:
                        ban_months.put(p.getUniqueId(), 3);
                        break;
                    case 3:
                        ban_months.put(p.getUniqueId(), 4);
                        break;
                    case 4:
                        ban_months.put(p.getUniqueId(), 5);
                        break;
                    case 5:
                        ban_months.put(p.getUniqueId(), 6);
                        break;
                    case 6:
                        ban_months.put(p.getUniqueId(), 7);
                        break;
                    case 7:
                        ban_months.put(p.getUniqueId(), 8);
                        break;
                    case 8:
                        ban_months.put(p.getUniqueId(), 9);
                        break;
                    case 9:
                        ban_months.put(p.getUniqueId(), 10);
                        break;
                    case 10:
                        ban_months.put(p.getUniqueId(), 11);
                        break;
                    case 11:
                        ban_months.put(p.getUniqueId(), 12);
                        break;
                    case 12:
                        ban_months.put(p.getUniqueId(), 0);
                        break;
                }
                p.openInventory(GUI_Ban(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "ban_days"))){
                switch (ban_days.getOrDefault(p.getUniqueId(),0)){
                    case 0:
                        ban_days.put(p.getUniqueId(), 1);
                        break;
                    case 1:
                        ban_days.put(p.getUniqueId(), 2);
                        break;
                    case 2:
                        ban_days.put(p.getUniqueId(), 3);
                        break;
                    case 3:
                        ban_days.put(p.getUniqueId(), 4);
                        break;
                    case 4:
                        ban_days.put(p.getUniqueId(), 5);
                        break;
                    case 5:
                        ban_days.put(p.getUniqueId(), 6);
                        break;
                    case 6:
                        ban_days.put(p.getUniqueId(), 7);
                        break;
                    case 7:
                        ban_days.put(p.getUniqueId(), 8);
                        break;
                    case 8:
                        ban_days.put(p.getUniqueId(), 9);
                        break;
                    case 9:
                        ban_days.put(p.getUniqueId(), 10);
                        break;
                    case 10:
                        ban_days.put(p.getUniqueId(), 15);
                        break;
                    case 15:
                        ban_days.put(p.getUniqueId(), 20);
                        break;
                    case 20:
                        ban_days.put(p.getUniqueId(), 30);
                        break;
                    case 30:
                        ban_days.put(p.getUniqueId(), 0);
                        break;
                }
                p.openInventory(GUI_Ban(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "ban_hours"))){
                switch (ban_hours.getOrDefault(p.getUniqueId(), 0)){
                    case 0:
                        ban_hours.put(p.getUniqueId(), 1);
                        break;
                    case 1:
                        ban_hours.put(p.getUniqueId(), 2);
                        break;
                    case 2:
                        ban_hours.put(p.getUniqueId(), 3);
                        break;
                    case 3:
                        ban_hours.put(p.getUniqueId(), 4);
                        break;
                    case 4:
                        ban_hours.put(p.getUniqueId(), 5);
                        break;
                    case 5:
                        ban_hours.put(p.getUniqueId(), 6);
                        break;
                    case 6:
                        ban_hours.put(p.getUniqueId(), 7);
                        break;
                    case 7:
                        ban_hours.put(p.getUniqueId(), 8);
                        break;
                    case 8:
                        ban_hours.put(p.getUniqueId(), 9);
                        break;
                    case 9:
                        ban_hours.put(p.getUniqueId(), 10);
                        break;
                    case 10:
                        ban_hours.put(p.getUniqueId(), 15);
                        break;
                    case 15:
                        ban_hours.put(p.getUniqueId(), 20);
                        break;
                    case 20:
                        ban_hours.put(p.getUniqueId(), 0);
                        break;
                }
                p.openInventory(GUI_Ban(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked,Message.getMessage(p.getUniqueId(), "ban_minutes"))){
                switch (ban_minutes.getOrDefault(p.getUniqueId(),0)){
                    case 0:
                        ban_minutes.put(p.getUniqueId(), 5);
                        break;
                    case 5:
                        ban_minutes.put(p.getUniqueId(), 10);
                        break;
                    case 10:
                        ban_minutes.put(p.getUniqueId(), 15);
                        break;
                    case 15:
                        ban_minutes.put(p.getUniqueId(), 20);
                        break;
                    case 20:
                        ban_minutes.put(p.getUniqueId(), 25);
                        break;
                    case 25:
                        ban_minutes.put(p.getUniqueId(), 30);
                        break;
                    case 30:
                        ban_minutes.put(p.getUniqueId(), 35);
                        break;
                    case 35:
                        ban_minutes.put(p.getUniqueId(), 40);
                        break;
                    case 40:
                        ban_minutes.put(p.getUniqueId(), 45);
                        break;
                    case 45:
                        ban_minutes.put(p.getUniqueId(), 50);
                        break;
                    case 50:
                        ban_minutes.put(p.getUniqueId(), 55);
                        break;
                    case 55:
                        ban_minutes.put(p.getUniqueId(), 0);
                        break;
                }
                p.openInventory(GUI_Ban(p, target_player));
            }
        }else{
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
            p.closeInventory();
        }

    }

    public void clicked_potions(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player){

        TargetPlayer targetPlayer = new TargetPlayer();

        if(target_player.isOnline()){
            if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_back"))){
                if(p.getName().equals(target_player.getName())){
                    p.openInventory(GUI_Player(p));
                }else{
                    p.openInventory(GUI_Actions(p,target_player));
                }

            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_time"))){
                switch (duration.getOrDefault(p.getUniqueId(), 1)){
                    case 1:
                        duration.put(p.getUniqueId(), 2);
                        break;
                    case 2:
                        duration.put(p.getUniqueId(), 3);
                        break;
                    case 3:
                        duration.put(p.getUniqueId(), 4);
                        break;
                    case 4:
                        duration.put(p.getUniqueId(), 5);
                        break;
                    case 5:
                        duration.put(p.getUniqueId(), 7);
                        break;
                    case 7:
                        duration.put(p.getUniqueId(), 10);
                        break;
                    case 10:
                        duration.put(p.getUniqueId(), 15);
                        break;
                    case 15:
                        duration.put(p.getUniqueId(), 20);
                        break;
                    case 20:
                        duration.put(p.getUniqueId(), 1000000);
                        break;
                    case 1000000:
                        duration.put(p.getUniqueId(), 1);
                        break;
                }
                p.openInventory(GUI_potions(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_level"))){
                switch (level.getOrDefault(p.getUniqueId(), 1)){
                    case 1:
                        level.put(p.getUniqueId(), 2);
                        break;
                    case 2:
                        level.put(p.getUniqueId(), 3);
                        break;
                    case 3:
                        level.put(p.getUniqueId(), 4);
                        break;
                    case 4:
                        level.put(p.getUniqueId(), 5);
                        break;
                    case 5:
                        level.put(p.getUniqueId(), 1);
                        break;
                }
                p.openInventory(GUI_potions(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_remove_all"))) {
                for (PotionEffect effect : target_player.getActivePotionEffects()){
                    target_player.removePotionEffect(effect.getType());
                }

                if(p.getName().equals(target_player.getName())){
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_potions_remove"));
                }else{
                    target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_target_player_potions_remove").replace("{player}", p.getName()));
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_potions_remove").replace("{player}", target_player.getName()));
                }

                p.openInventory(GUI_potions(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_night_vision"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.NIGHT_VISION, "potions_night_vision", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_invisibility"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.INVISIBILITY, "potions_invisibility", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_jump_boost"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.JUMP, "potions_jump_boost", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_fire_resistance"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.FIRE_RESISTANCE, "potions_fire_resistance", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_speed"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.SPEED, "potions_speed", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_slowness"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.SLOW, "potions_slowness", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_water_breathing"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.WATER_BREATHING, "potions_water_breathing", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_instant_health"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.HEAL, "potions_instant_health", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_instant_damage"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.HARM, "potions_instant_damage", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_poison"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.POISON, "potions_poison", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_regeneration"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.REGENERATION, "potions_regeneration", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_strength"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.INCREASE_DAMAGE, "potions_strength", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_weakness"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.WEAKNESS, "potions_weakness", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_luck"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.LUCK, "potions_luck", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "potions_slow_falling"))){
                targetPlayer.setPotionEffect(p, target_player, PotionEffectType.SLOW_FALLING, "potions_slow_falling", duration.getOrDefault(p.getUniqueId(), 1), level.getOrDefault(p.getUniqueId(), 1));
            }
        }else{
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
            p.closeInventory();
        }
    }

    public void clicked_spawner(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player){

        if(target_player.isOnline()){
            if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_back"))){
                if(p.getName().equals(target_player.getName())){
                    p.openInventory(GUI_Player(p));
                }else{
                    p.openInventory(GUI_Actions(p, target_player));
                }
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_bat"))){
                Entity.spawn(target_player.getLocation(), EntityType.BAT);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_bee"))){
                Entity.spawn(target_player.getLocation(), EntityType.BEE);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_blaze"))){
                Entity.spawn(target_player.getLocation(), EntityType.BLAZE);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_cat"))){
                Entity.spawn(target_player.getLocation(), EntityType.CAT);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_cave_spider"))){
                Entity.spawn(target_player.getLocation(), EntityType.CAVE_SPIDER);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_chicken"))){
                Entity.spawn(target_player.getLocation(), EntityType.CHICKEN);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_cod"))){
                Entity.spawn(target_player.getLocation(), EntityType.COD);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_cow"))){
                Entity.spawn(target_player.getLocation(), EntityType.COW);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_creeper"))){
                Entity.spawn(target_player.getLocation(), EntityType.CREEPER);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_dolphin"))){
                Entity.spawn(target_player.getLocation(), EntityType.DOLPHIN);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_donkey"))){
                Entity.spawn(target_player.getLocation(), EntityType.DONKEY);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_drowned"))){
                Entity.spawn(target_player.getLocation(), EntityType.DROWNED);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_elder_guardian"))){
                Entity.spawn(target_player.getLocation(), EntityType.ELDER_GUARDIAN);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_enderman"))){
                Entity.spawn(target_player.getLocation(), EntityType.ENDERMAN);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_endermite"))){
                Entity.spawn(target_player.getLocation(), EntityType.ENDERMITE);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_evoker"))){
                Entity.spawn(target_player.getLocation(), EntityType.EVOKER);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_fox"))){
                Entity.spawn(target_player.getLocation(), EntityType.FOX);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_ghast"))){
                Entity.spawn(target_player.getLocation(), EntityType.GHAST);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_guardian"))){
                Entity.spawn(target_player.getLocation(), EntityType.GUARDIAN);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_horse"))){
                Entity.spawn(target_player.getLocation(), EntityType.HORSE);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_husk"))){
                Entity.spawn(target_player.getLocation(), EntityType.HUSK);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_llama"))){
                Entity.spawn(target_player.getLocation(), EntityType.LLAMA);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_magma_cube"))){
                Entity.spawn(target_player.getLocation(), EntityType.MAGMA_CUBE);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_mooshroom"))){
                Entity.spawn(target_player.getLocation(), EntityType.MUSHROOM_COW);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_mule"))){
                Entity.spawn(target_player.getLocation(), EntityType.MULE);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_ocelot"))){
                Entity.spawn(target_player.getLocation(), EntityType.OCELOT);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_panda"))){
                Entity.spawn(target_player.getLocation(), EntityType.PANDA);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_parrot"))){
                Entity.spawn(target_player.getLocation(), EntityType.PARROT);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_phantom"))){
                Entity.spawn(target_player.getLocation(), EntityType.PHANTOM);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_pig"))){
                Entity.spawn(target_player.getLocation(), EntityType.PIG);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_pillager"))){
                Entity.spawn(target_player.getLocation(), EntityType.PILLAGER);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_polar_bear"))){
                Entity.spawn(target_player.getLocation(), EntityType.POLAR_BEAR);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_pufferfish"))){
                Entity.spawn(target_player.getLocation(), EntityType.PUFFERFISH);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_rabbit"))){
                Entity.spawn(target_player.getLocation(), EntityType.RABBIT);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_ravager"))){
                Entity.spawn(target_player.getLocation(), EntityType.RAVAGER);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_salmon"))){
                Entity.spawn(target_player.getLocation(), EntityType.SALMON);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_sheep"))){
                Entity.spawn(target_player.getLocation(), EntityType.SHEEP);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_shulker"))){
                Entity.spawn(target_player.getLocation(), EntityType.SHULKER);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_silverfish"))){
                Entity.spawn(target_player.getLocation(), EntityType.SILVERFISH);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_skeleton"))){
                Entity.spawn(target_player.getLocation(), EntityType.SKELETON);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_skeleton_horse"))){
                Entity.spawn(target_player.getLocation(), EntityType.SKELETON_HORSE);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_slime"))){
                Entity.spawn(target_player.getLocation(), EntityType.SLIME);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_spider"))){
                Entity.spawn(target_player.getLocation(), EntityType.SPIDER);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_squid"))){
                Entity.spawn(target_player.getLocation(), EntityType.SQUID);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_stray"))){
                Entity.spawn(target_player.getLocation(), EntityType.STRAY);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_tropical_fish"))){
                Entity.spawn(target_player.getLocation(), EntityType.TROPICAL_FISH);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_turtle"))){
                Entity.spawn(target_player.getLocation(), EntityType.TURTLE);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_vex"))){
                Entity.spawn(target_player.getLocation(), EntityType.VEX);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_villager"))){
                Entity.spawn(target_player.getLocation(), EntityType.VILLAGER);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_vindicator"))){
                Entity.spawn(target_player.getLocation(), EntityType.VINDICATOR);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_wandering_trader"))){
                Entity.spawn(target_player.getLocation(), EntityType.WANDERING_TRADER);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_witch"))){
                Entity.spawn(target_player.getLocation(), EntityType.WITCH);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_wolf"))){
                Entity.spawn(target_player.getLocation(), EntityType.WOLF);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_zombie"))){
                Entity.spawn(target_player.getLocation(), EntityType.ZOMBIE);
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "spawner_zombie_pigman"))){
                Entity.spawn(target_player.getLocation(), EntityType.valueOf("PIG_ZOMBIE"));
            }
        }else{
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
            p.closeInventory();
        }
    }

    public void clicked_money(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player){
        if(target_player.isOnline()){
            if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "money_back"))){
                if(p.getName().equals(target_player.getName())){
                    p.openInventory(GUI_Player(p));
                }else{
                    p.openInventory(GUI_Players_Settings(p, target_player, target_player.getName()));
                }
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "money_give"))){
                if(AdminGUI.vault){
                    p.openInventory(GUI_Money_Amount(p, target_player, 1));
                }else{
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "vault_required"));
                    p.closeInventory();
                }
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "money_set"))){
                if(AdminGUI.vault){
                    p.openInventory(GUI_Money_Amount(p, target_player, 2));
                }else{
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "vault_required"));
                    p.closeInventory();
                }
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "money_take"))){
                if(AdminGUI.vault){
                    p.openInventory(GUI_Money_Amount(p, target_player, 3));
                }else{
                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "vault_required"));
                    p.closeInventory();
                }
            }
        }else{
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
            p.closeInventory();
        }
    }

    public void clicked_money_amount(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player, int option){
        if(target_player.isOnline()){
            if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "money_amount_back"))){
                p.openInventory(GUI_Money(p, target_player));
            }else {
                    if (clicked.hasItemMeta()) {
                        if (clicked.getItemMeta().hasDisplayName()) {
                            String amount = stripNonDigits(clicked.getItemMeta().getDisplayName());
                            if (NumberUtils.isNumber(amount)) {
                                if (AdminGUI.vault) {
                                    switch (option) {
                                        case 1:
                                            EconomyResponse r = AdminGUI.getEconomy().depositPlayer(target_player, Double.parseDouble(amount));
                                            if (r.transactionSuccess()) {
                                                if(p.getName().equals(target_player.getName())){
                                                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_give").replace("{amount}", AdminGUI.getEconomy().format(r.amount)).replace("{player}", target_player.getName()).replace("{balance}", AdminGUI.getEconomy().format(r.balance)));
                                                }else{
                                                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_give").replace("{amount}", AdminGUI.getEconomy().format(r.amount)).replace("{player}", target_player.getName()).replace("{balance}", AdminGUI.getEconomy().format(r.balance)));
                                                    target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_give").replace("{amount}", AdminGUI.getEconomy().format(r.amount)).replace("{player}", p.getName()).replace("{balance}", AdminGUI.getEconomy().format(r.balance)));
                                                }
                                            } else {
                                                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_transaction_error").replace("{amount}", AdminGUI.getEconomy().format(r.amount)).replace("{player}", target_player.getName()));
                                            }
                                            p.closeInventory();
                                            break;
                                        case 3:
                                            if(AdminGUI.getEconomy().getBalance(target_player) >= Double.parseDouble(amount)){
                                                EconomyResponse s = AdminGUI.getEconomy().withdrawPlayer(target_player, Double.parseDouble(amount));
                                                if(s.transactionSuccess()) {
                                                    if(p.getName().equals(target_player.getName())){
                                                        p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_take").replace("{amount}", AdminGUI.getEconomy().format(s.amount)).replace("{player}", target_player.getName()).replace("{balance}", AdminGUI.getEconomy().format(s.balance)));
                                                    }else{
                                                        p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_take").replace("{amount}", AdminGUI.getEconomy().format(s.amount)).replace("{player}", target_player.getName()).replace("{balance}", AdminGUI.getEconomy().format(s.balance)));
                                                        target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_take").replace("{amount}", AdminGUI.getEconomy().format(s.amount)).replace("{player}", p.getName()).replace("{balance}", AdminGUI.getEconomy().format(s.balance)));
                                                    }
                                                }else{
                                                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_transaction_error").replace("{amount}", AdminGUI.getEconomy().format(s.amount)).replace("{player}", target_player.getName()));
                                                }
                                            }else{
                                                if(p.getName().equals(target_player.getName())){
                                                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_take_error"));
                                                }else{
                                                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_take_error"));
                                                }
                                            }
                                            p.closeInventory();
                                            break;
                                        default:
                                            double balance = AdminGUI.getEconomy().getBalance(target_player);
                                            AdminGUI.getEconomy().withdrawPlayer(target_player, balance);
                                            EconomyResponse t = AdminGUI.getEconomy().depositPlayer(target_player, Double.parseDouble(amount));
                                            if(t.transactionSuccess()) {
                                                if(p.getName().equals(target_player.getName())){
                                                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_set").replace("{amount}", AdminGUI.getEconomy().format(t.amount)).replace("{player}", target_player.getName()).replace("{balance}", AdminGUI.getEconomy().format(t.balance)));
                                                }else{
                                                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_set").replace("{amount}", AdminGUI.getEconomy().format(t.amount)).replace("{player}", target_player.getName()).replace("{balance}", AdminGUI.getEconomy().format(t.balance)));
                                                    target_player.sendMessage(Message.getMessage(target_player.getUniqueId(), "prefix") + Message.getMessage(target_player.getUniqueId(), "message_target_player_set").replace("{amount}", AdminGUI.getEconomy().format(t.amount)).replace("{player}", p.getName()).replace("{balance}", AdminGUI.getEconomy().format(t.balance)));
                                                }
                                            }else{
                                                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_transaction_error").replace("{amount}", AdminGUI.getEconomy().format(t.amount)).replace("{player}", target_player.getName()));
                                            }
                                            p.closeInventory();
                                    }
                                } else {
                                    p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "vault_required"));
                                    p.closeInventory();
                                }
                            } else {
                                p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "is_not_a_number").replace("{number}", amount));
                                p.closeInventory();
                            }
                        }
                    }
            }
        }else{
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
            p.closeInventory();
        }
    }

    public void clicked_inventory(Player p, int slot, ItemStack clicked, Inventory inv, Player target_player, boolean left_click){

        if(target_player.isOnline()){
            if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "inventory_back"))){
                p.openInventory(GUI_Actions(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "inventory_clear"))){
                target_player.getInventory().clear();
                p.openInventory(GUI_Inventory(p, target_player));
            }else if(InventoryGUI.getClickedItem(clicked, Message.getMessage(p.getUniqueId(), "inventory_refresh"))){
                p.openInventory(GUI_Inventory(p, target_player));
            }else{
                if(p.hasPermission("admingui.inventory.edit")){
                    if(left_click){
                        target_player.getInventory().addItem(clicked);
                    }else{
                        if(clicked.getType() == target_player.getInventory().getItem(slot).getType() && clicked.getAmount() == target_player.getInventory().getItem(slot).getAmount()){
                            target_player.getInventory().setItem(slot, null);
                        }
                    }
                    target_player.updateInventory();
                    p.openInventory(GUI_Inventory(p, target_player));
                }
            }
        }else{
            p.sendMessage(Message.getMessage(p.getUniqueId(), "prefix") + Message.getMessage(p.getUniqueId(), "message_player_not_found"));
            p.closeInventory();
        }
    }

    public static String stripNonDigits(final CharSequence input){
        final StringBuilder sb = new StringBuilder(input.length());
        for(int i = 0; i < input.length(); i++){
            final char c = input.charAt(i);
            if(c > 47 && c < 58){
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
