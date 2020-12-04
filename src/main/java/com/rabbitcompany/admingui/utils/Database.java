package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.adminbans.AdminBans;
import com.rabbitcompany.admingui.AdminGUI;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Database {

    public static SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String cacheRank(UUID uuid){
        String query = "SELECT rank FROM admingui_ranks WHERE uuid = '" + uuid.toString() + "'";
        String rank = null;
        try {
            Connection conn = AdminGUI.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                rank = rs.getString("rank");
                Permissions.cache_ranks.put(uuid, rank);
            }
            conn.close();
            return rank;
        } catch (SQLException ignored) { }
        return null;
    }

    public static boolean setRank(UUID uuid, String name, String rank){
        if(playerHasRank(uuid, name)){
            if(uuid != null){
                try {
                    Date until = new Date(System.currentTimeMillis());
                    Connection conn = AdminGUI.hikari.getConnection();
                    conn.createStatement().executeUpdate("UPDATE admingui_ranks SET rank = '" + rank + "', created = '" + date_format.format(until) + "' WHERE uuid = '" + uuid.toString() + "';");
                    conn.close();
                    return true;
                } catch (SQLException ignored) { }
            }else{
                try {
                    Date until = new Date(System.currentTimeMillis());
                    Connection conn = AdminGUI.hikari.getConnection();
                    conn.createStatement().executeUpdate("UPDATE admingui_ranks SET rank = '" + rank + "', created = '" + date_format.format(until) + "' WHERE username = '" + name + "';");
                    conn.close();
                    return true;
                } catch (SQLException ignored) { }
            }
        }else{
            if(uuid != null){
                try {
                    Connection conn = AdminGUI.hikari.getConnection();
                    conn.createStatement().executeUpdate("INSERT INTO admingui_ranks(uuid, username, rank) VALUES ('" + uuid.toString() + "', '" + name + "', '" + rank + "');");
                    conn.close();
                    return true;
                } catch (SQLException ignored) { }
            }else{
                try {
                    Connection conn = AdminGUI.hikari.getConnection();
                    conn.createStatement().executeUpdate("INSERT INTO admingui_ranks(uuid, username, rank) VALUES ('!!!" + UUID.randomUUID().toString().substring(3) + "', '" + name + "', '" + rank + "');");
                    conn.close();
                    return true;
                } catch (SQLException ignored) { }
            }
        }
        return false;
    }

    public static boolean fixRank(UUID uuid, String name){
        try {
            Connection conn = AdminGUI.hikari.getConnection();
            conn.createStatement().executeUpdate("UPDATE admingui_ranks SET uuid = '" + uuid.toString() + "' WHERE username = '" + name + "';");
            conn.close();
            return true;
        } catch (SQLException ignored) { }
        return false;
    }

    public static boolean rankNeedFix(String name){
        String query = "SELECT uuid FROM admingui_ranks WHERE username = '" + name + "'";
        String uuid = "";
        try {
            Connection conn = AdminGUI.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                uuid = rs.getString("uuid");
            }
            conn.close();
            if(uuid.startsWith("!!!")){
                return true;
            }
        } catch (SQLException ignored) { }
        return false;
    }

    public static boolean playerHasRank(UUID uuid, String name){
        if(uuid != null){
            String query = "SELECT rank FROM admingui_ranks WHERE uuid = '" + uuid.toString() + "' OR username = '" + name + "'";

            try {
                Connection conn = AdminGUI.hikari.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                boolean exists = rs.next();
                conn.close();
                return exists;
            } catch (SQLException ignored) { }
        }else{
            String query = "SELECT rank FROM admingui_ranks WHERE username = '" + name + "'";

            try {
                Connection conn = AdminGUI.hikari.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                boolean exists = rs.next();
                conn.close();
                return exists;
            } catch (SQLException ignored) { }
        }
        return false;
    }

}
