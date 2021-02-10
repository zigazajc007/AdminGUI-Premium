package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Settings {

    //Target players
    public static HashMap<Player, Player> target_player = new HashMap<>();

    //Player heads
    public static HashMap<String, ItemStack> skulls = new HashMap<>();
    public static HashMap<String, ItemStack> skulls_players = new HashMap<>();

    //RTP delay
    public static HashMap<UUID, Long> rtp_delay = new HashMap<>();

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

}
