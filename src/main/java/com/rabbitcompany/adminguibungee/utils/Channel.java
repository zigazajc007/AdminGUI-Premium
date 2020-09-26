package com.rabbitcompany.adminguibungee.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.rabbitcompany.adminguibungee.AdminGUIBungee;

public class Channel {

    public static void send(String player, String subchannel, String... data){
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(player);
        output.writeUTF(subchannel);
        for (String da: data) {
            output.writeUTF(da);
        }

        try{
            if(AdminGUIBungee.getInstance().getProxy().getPlayer(player).isConnected())
                AdminGUIBungee.getInstance().getProxy().getPlayer(player).getServer().getInfo().sendData("my:admingui", output.toByteArray());
        }catch (Exception ignored){ }

    }

}
