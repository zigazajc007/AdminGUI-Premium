package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class AdminBanSystem {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static boolean isPlayerBanned(String player){
        String query = "SELECT * FROM admin_gui_banned_players WHERE username_to = '" + player + "' ORDER BY until DESC;";
        AtomicBoolean isPlayerBanned = new AtomicBoolean(false);
        try {
            AdminGUI.mySQL.query(query, results -> {
                if (results.next()) {
                    Timestamp until = results.getTimestamp("until");
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    if(now.before(until)){
                        isPlayerBanned.set(true);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isPlayerBanned.get();
    }

    public static boolean isPlayerBannedUUID(String uuid){
        String query = "SELECT * FROM admin_gui_banned_players WHERE uuid_to = '" + uuid + "' ORDER BY until DESC;";
        AtomicBoolean isPlayerBanned = new AtomicBoolean(false);
        try {
            AdminGUI.mySQL.query(query, results -> {
                if (results.next()) {
                    Timestamp until = results.getTimestamp("until");
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    if(now.before(until)){
                        isPlayerBanned.set(true);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isPlayerBanned.get();
    }

    public static String banPlayer(String uuid_from, String username_from, String uuid_to, String username_to, String reason, String until){
        if(AdminGUI.conn != null && AdminGUI.getInstance().getConf().getBoolean("admin_ban_system_enabled", false)) {
            try {
                AdminGUI.mySQL.update("INSERT INTO admin_gui_banned_players(uuid_from, username_from, uuid_to, username_to, reason, until) VALUES ('" + uuid_from + "', '" + username_from + "', '" + uuid_to + "', '" + username_to + "', '" + reason + "', '" + until + "');");
                return Message.getMessage(UUID.randomUUID(), "message_ban_bypass").replace("{player}", username_to).replace("{reason}", reason);
            } catch (SQLException ignored) {
                return Message.chat("&cSomething went wrong while trying to ban " + username_to + ".");
            }
        }else{
            return Message.chat("&cYou don't have enabled Admin Ban System in config or settled up correctly Mysql database!");
        }
    }

    public static String kickPlayer(String uuid_from, String username_from, String uuid_to, String username_to, String reason){
        if(AdminGUI.conn != null && AdminGUI.getInstance().getConf().getBoolean("admin_ban_system_enabled", false)) {
            try {
                AdminGUI.mySQL.update("INSERT INTO admin_gui_kicked_players(uuid_from, username_from, uuid_to, username_to, reason) VALUES ('" + uuid_from + "', '" + username_from + "', '" + uuid_to + "', '" + username_to + "', '" + reason + "');");
                return Message.chat("&aYou have kicked " + username_to);
            } catch (SQLException ignored) {
                return Message.chat("&cSomething went wrong while trying to kick " + username_to + ".");
            }
        }else{
            return Message.chat("&cYou don't have enabled Admin Ban System in config or settled up correctly Mysql database!");
        }
    }

    public static boolean unBanPlayerName(String player){
        if(AdminGUI.conn != null && AdminGUI.getInstance().getConf().getBoolean("admin_ban_system_enabled", false)) {
            if(isPlayerBanned(player)){
                try {
                    Date until = new Date(System.currentTimeMillis());
                    AdminGUI.mySQL.update("UPDATE admin_gui_banned_players SET until = '" + sdf.format(until) + "' WHERE username_to = '" + player + "';");
                    return true;
                } catch (SQLException ignored) { }
            }
        }
        return false;
    }

    public static boolean unBanPlayerUUID(String uuid){
        if(AdminGUI.conn != null && AdminGUI.getInstance().getConf().getBoolean("admin_ban_system_enabled", false)) {
            if(isPlayerBannedUUID(uuid)){
                try {
                    Date until = new Date(System.currentTimeMillis());
                    AdminGUI.mySQL.update("UPDATE admin_gui_banned_players SET until = '" + sdf.format(until) + "' WHERE uuid_to = '" + uuid + "';");
                    return true;
                } catch (SQLException ignored) { }
            }
        }
        return false;
    }

    public static ArrayList<BannedPlayer> getBannedPlayers(){
        ArrayList<BannedPlayer> banned_players = new ArrayList<>();

        String query = "SELECT * FROM admin_gui_banned_players ORDER BY until DESC;";

        try {
            AdminGUI.mySQL.query(query, results -> {
                while (results.next()){
                    Timestamp until = results.getTimestamp("until");
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    if(now.before(until)){
                        banned_players.add(new BannedPlayer(results.getString("uuid_from"), results.getString("username_from"), results.getString("uuid_to"), results.getString("username_to"), results.getString("reason"), results.getTimestamp("until"), results.getTimestamp("created")));
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return banned_players;
    }

    public static String playerBannedMessage(String uuid){
        if(isPlayerBannedUUID(uuid)){
            String query = "SELECT * FROM admin_gui_banned_players WHERE uuid_to = '" + uuid + "' ORDER BY until DESC;";
            AtomicReference<String> reason = new AtomicReference<>("");
            AtomicReference<Date> until = new AtomicReference<>(new Date(System.currentTimeMillis()));
            try {
                AdminGUI.mySQL.query(query, results -> {
                    if (results.next()) {
                        until.set(results.getTimestamp("until"));
                        reason.set(results.getString("reason"));
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if(TargetPlayer.banReason(UUID.fromString(uuid), reason.get(), sdf.format(until.get())).contains(".yml file! Please add it or delete")){
                return TargetPlayer.banCustomReason(UUID.fromString(uuid), reason.get(), sdf.format(until.get()));
            }else{
                return TargetPlayer.banReason(UUID.fromString(uuid), reason.get(), sdf.format(until.get()));
            }
        }else{
            return "";
        }
    }

}
