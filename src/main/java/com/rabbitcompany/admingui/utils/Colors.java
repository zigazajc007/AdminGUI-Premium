package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Colors {

    public static Color getColor(int i) {
        Color c;
        switch (i){
            case 1:
                c = Color.AQUA;
                break;
            case 2:
                c = Color.BLACK;
                break;
            case 3:
                c = Color.BLUE;
                break;
            case 4:
                c = Color.FUCHSIA;
                break;
            case 5:
                c = Color.GRAY;
                break;
            case 6:
                c = Color.GREEN;
                break;
            case 7:
                c = Color.LIME;
                break;
            case 8:
                c = Color.MAROON;
                break;
            case 9:
                c = Color.NAVY;
                break;
            case 10:
                c = Color.OLIVE;
                break;
            case 11:
                c = Color.ORANGE;
                break;
            case 12:
                c = Color.PURPLE;
                break;
            case 14:
                c = Color.SILVER;
                break;
            case 15:
                c = Color.TEAL;
                break;
            case 16:
                c = Color.WHITE;
                break;
            case 17:
                c = Color.YELLOW;
                break;
            default:
                c = Color.RED;
                break;
        }
        return c;
    }

    private static final Pattern hex_pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");

    public static String toHex(String message) {
        if(Bukkit.getVersion().contains("1.16")){
            Matcher matcher = hex_pattern.matcher(message);
            while (matcher.find()) {
                String color = message.substring(matcher.start(), matcher.end());
                if(color.matches("(?<!\\\\)(#[a-fA-F0-9]{6})")){
                    message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color).toString());
                }
                matcher = hex_pattern.matcher(message);
            }
            String color_prefix = AdminGUI.getInstance().getConf().getString("ac_color_prefix", ";");
            if(message.contains(""+color_prefix)){
                for (HexColors color : HexColors.values()) {
                    if(message.contains(color_prefix + color.name().toLowerCase())){
                        message = message.replace(color_prefix + color.name().toLowerCase(), net.md_5.bungee.api.ChatColor.of(color.hex).toString());
                    }
                }
            }
        }
        return message;
    }

    public static String rainbowText(String text){

        text = ChatColor.stripColor(text);

        StringBuilder sb = new StringBuilder();

        for(int i = 0, j = 0; i < text.length(); i++, j++){

            if(text.charAt(i) == ' ') j--;

            switch(j){
                case 0:
                    sb.append("&4");
                    break;
                case 1:
                    sb.append("&c");
                    break;
                case 2:
                    sb.append("&6");
                    break;
                case 3:
                    sb.append("&e");
                    break;
                case 4:
                    sb.append("&a");
                    break;
                case 5:
                    sb.append("&2");
                    break;
                case 6:
                    sb.append("&b");
                    break;
                case 7:
                    sb.append("&9");
                    break;
                case 8:
                    sb.append("&1");
                    break;
                case 9:
                    sb.append("&5");
                    break;
                case 10:
                    sb.append("&d");
                    j = 0;
                    break;
            }
            sb.append(text.charAt(i));
        }

        return sb.toString();
    }

    public static String gradientText(String text)
    {
        StringBuilder builder = new StringBuilder(text.length() + 7 * (text.length())+1);

        int index = 0;
        int index_color = 0;
        String prefix = "#80f320";
        while (index < text.length())
        {
            builder.append(prefix);

            index_color++;
            if(index_color >= 32) index_color = 0;

            prefix = gradientHex(index_color);

            builder.append(text.substring(index, Math.min(index + 1, text.length())));
            index++;
            index_color++;
        }
        return builder.toString();
    }

    public static String gradientHex(int i){
        double frequency = .3;

        int red = (int) Math.round(Math.sin(frequency*i + 0) * 127 + 128);
        int green = (int) Math.round(Math.sin(frequency*i + 2) * 127 + 128);
        int blue  = (int) Math.round(Math.sin(frequency*i + 4) * 127 + 128);

        return RGBtoHex(red, green, blue);
    }

    public static String RGBtoHex(int r, int g, int b){
        return String.format("#%02x%02x%02x", r, g, b);
    }

}
