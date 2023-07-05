package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.admingui.utils.Settings;
import com.rabbitcompany.admingui.utils.TargetPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

	private final AdminGUI adminGUI;

	public PlayerLoginListener(AdminGUI plugin) {
		adminGUI = plugin;

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
			if (Settings.maintenance_mode && !TargetPlayer.hasPermission(event.getPlayer(), "admingui.maintenance"))
				event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Message.getMessage(event.getPlayer().getUniqueId(), "prefix") + Message.getMessage(event.getPlayer().getUniqueId(), "message_maintenance"));
		}
	}

}
