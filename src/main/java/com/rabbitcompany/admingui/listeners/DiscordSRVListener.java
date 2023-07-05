package com.rabbitcompany.admingui.listeners;

import com.rabbitcompany.admingui.AdminGUI;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePostProcessEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class DiscordSRVListener {

	private final Plugin plugin;

	public DiscordSRVListener(Plugin plugin) {
		this.plugin = plugin;
	}

	@Subscribe
	public void discordMessageProcessed(DiscordGuildMessagePostProcessEvent event) {
		List<String> filters = AdminGUI.getInstance().getConf().getStringList("ac_filter");
		String message = event.getProcessedMessage();
		for (String filter : filters) message = message.replace(filter, "****");
		event.setProcessedMessage(message);
	}
}
