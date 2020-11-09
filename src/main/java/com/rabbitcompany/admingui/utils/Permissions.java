package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;

import java.util.UUID;

public class Permissions {

    public static String getPrefix(UUID uuid){
        String rank = AdminGUI.getInstance().getPermissions().getString("ranks." + uuid, null);
        if(rank == null){
            return AdminGUI.getInstance().getPermissions().getString("groups.default.prefix", "");
        }else{
            return AdminGUI.getInstance().getPermissions().getString("groups." + rank + ".prefix", "");
        }
    }

    public static String getSuffix(UUID uuid){
        String rank = AdminGUI.getInstance().getPermissions().getString("ranks." + uuid, null);
        if(rank == null){
            return AdminGUI.getInstance().getPermissions().getString("groups.default.suffix", "");
        }else{
            return AdminGUI.getInstance().getPermissions().getString("groups." + rank + ".suffix", "");
        }
    }

    public static String getRank(UUID uuid){
        String rank = AdminGUI.getInstance().getPermissions().getString("ranks." + uuid, null);
        if(rank == null){
            return "default";
        }else{
            return rank;
        }
    }

}
