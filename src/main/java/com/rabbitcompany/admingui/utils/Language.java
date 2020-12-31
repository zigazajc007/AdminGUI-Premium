package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.ui.AdminUI;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Language {

    public static ArrayList<String> default_languages = new ArrayList<>(Arrays.asList("Bulgarian", "Chinese", "Dutch", "English", "Finnish", "French", "Hebrew", "Italian", "Japanese", "Korean", "Portuguese", "Russian", "Slovak", "Spanish", "Swedish", "Turkish"));
    public static ArrayList<String> enabled_languages = new ArrayList<>();
    static HashMap<String, YamlConfiguration> languages = new HashMap<>();

    public static ArrayList<String> getLanguages(){
        enabled_languages.clear();
        File dir = new File(AdminGUI.getInstance().getDataFolder(), "Languages");
        if(dir.exists()){
            String[] files = dir.list();
            for (String file: files) {
                if(isLangFile(file)){
                    enabled_languages.add(file.replace(".yml", ""));
                }
            }
        }
        return enabled_languages;
    }

    public static String getMessages(UUID uuid, String config){
        String language = AdminUI.language.getOrDefault(uuid, AdminGUI.getInstance().getConf().getString("default_language"));
        if(enabled_languages.contains(language)) return languages.getOrDefault(language, null).getString(config, null);
        return null;
    }

    public static boolean downloadLanguage(String language){
        if(default_languages.contains(language)){
            File lang_file = new File(AdminGUI.getInstance().getDataFolder(), "Languages/" + language + ".yml");
            if(!lang_file.exists()){
                AdminGUI.getInstance().saveResource("Languages/" + language + ".yml", false);
                getLanguages();
            }
            return true;
        }
        return false;
    }

    public static boolean fixLanguage(String language){
        if(default_languages.contains(language) && enabled_languages.contains(language)){
            AdminGUI.getInstance().saveResource("Languages/" + language + ".yml", true);
            getLanguages();
            return true;
        }
        return false;
    }

    public static boolean advancedFixLanguage(String language){
        if(enabled_languages.contains(language)){
            YamlConfiguration temp_lang = new YamlConfiguration();
            YamlConfiguration lang = languages.getOrDefault(language, null);
            YamlConfiguration ml_lang = new YamlConfiguration();

            InputStream lang_inputStream = AdminGUI.getInstance().getResource("Languages/" + language + ".yml");
            if(lang_inputStream == null) lang_inputStream = AdminGUI.getInstance().getResource("Languages/English.yml");

            try {
                temp_lang.load(new InputStreamReader(lang_inputStream));
            } catch (InvalidConfigurationException | IOException e) {
                e.printStackTrace();
            }

            Set<String> temp_lang_keys = temp_lang.getKeys(false);
            Set<String> lang_keys = lang.getKeys(false);

            if(!temp_lang_keys.equals(lang_keys)){
                for (String key : temp_lang_keys) {
                    if(!lang_keys.contains(key)){
                        ml_lang.set(key, temp_lang.getString(key, ""));
                    }
                }
                try {
                    ml_lang.save(new File(AdminGUI.getInstance().getDataFolder(), "Languages/" + language + "-Missing-Lines.txt"));
                } catch (IOException ignored) {}
            }
            return true;
        }
        return false;
    }

    public static boolean isLangFile(String language){
        YamlConfiguration lang = new YamlConfiguration();
        File lang_file = new File(AdminGUI.getInstance().getDataFolder(), "Languages/" + language);
        if(lang_file.exists()){
            try {
                lang.load(lang_file);
                if(lang.isString("prefix")){
                    languages.put(language.replace(".yml", ""), lang);
                    return true;
                }
            } catch (IOException | InvalidConfigurationException ignored) { }
        }
        return false;
    }

}
