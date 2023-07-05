package com.rabbitcompany.admingui.utils;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class Entity {

	public static void spawn(Location loc, EntityType entity) {
		if (loc.getWorld() != null) loc.getWorld().spawnEntity(loc, entity);
	}

}
