package com.rabbitcompany.admingui.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.rabbitcompany.admingui.AdminGUI;
import org.bukkit.Bukkit;

public class Channel {

    public static void send(String sender, String subchannel, String... data){

        if(AdminGUI.getInstance().getConf().getBoolean("bungeecord_enabled", false)){
            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF(sender);
            output.writeUTF(subchannel);
            for (String da: data) output.writeUTF(da);
            Bukkit.getServer().sendPluginMessage(AdminGUI.getInstance(), "my:admingui", output.toByteArray());
        }
    }

}
