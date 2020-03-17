package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class AdminBanSystem {

    public static boolean isPlayerBanned(String uuid){
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
                return "&aYou have banned " + username_to + " until " + until;
            } catch (SQLException ignored) {
                return "&cSomething went wrong while trying to ban " + username_to + ".";
            }
        }else{
            return "&cYou don't have enabled Admin Ban System in config or settled up correctly Mysql database!";
        }
    }

    public static String playerBannedMessage(String uuid){
        if(isPlayerBanned(uuid)){
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
            return TargetPlayer.banReason(UUID.fromString(uuid), reason.get(), until.get());
        }else{
            return "";
        }
    }

}
