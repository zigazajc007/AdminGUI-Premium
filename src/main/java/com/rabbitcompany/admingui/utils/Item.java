package com.rabbitcompany.admingui.utils;

import com.rabbitcompany.admingui.XMaterial;
import org.bukkit.SkullType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class Item {

	public static ItemStack create(Inventory inv, String material, int amount, int invSlot, String displayName, String... loreString) {
		ItemStack item;
		ArrayList<String> lore = new ArrayList<>();

		item = new ItemStack(XMaterial.matchXMaterial(material).get().parseMaterial(), amount, XMaterial.matchXMaterial(material).get().getData());

		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(Message.chat(displayName));
			for (String s : loreString) lore.add(Message.chat(s));
			meta.setLore(lore);
			item.setItemMeta(meta);
		}

		inv.setItem(invSlot - 1, item);
		return item;
	}

	public static ItemStack createPlayerHead(Inventory inv, String player, int amount, int invSlot, String displayName, String... loreString) {
		ItemStack item;
		ArrayList<String> lore = new ArrayList<>();

		item = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), amount, (short) SkullType.PLAYER.ordinal());

		SkullMeta skullMeta = (SkullMeta) item.getItemMeta();

		skullMeta.setOwner(player);
		skullMeta.setDisplayName(Message.chat(displayName));
		item.setItemMeta(skullMeta);

		ItemMeta meta = item.getItemMeta();

		for (String s : loreString) lore.add(Message.chat(s));

		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(invSlot - 1, item);
		return item;
	}

	public static ItemStack pre_createPlayerHead(String player) {
		ItemStack item;

		item = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (short) SkullType.PLAYER.ordinal());

		SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
		if (skullMeta != null) {
			skullMeta.setOwner(player);
			skullMeta.setDisplayName(Message.chat(player));
			item.setItemMeta(skullMeta);
		}

		return item;
	}

	public static ItemStack after_createPlayerHead(Inventory inv, ItemStack item, int amount, int invSlot, String displayName, String... loreString) {

		ArrayList<String> lore = new ArrayList<>();

		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(Message.chat(displayName));
			for (String s : loreString) lore.add(Message.chat(s));
			meta.setLore(lore);
			item.setItemMeta(meta);
		}

		inv.setItem(invSlot - 1, item);
		return item;
	}

}
