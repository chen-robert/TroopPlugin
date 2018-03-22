package body;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Menu {
	static{
		Inventory inv = Bukkit.createInventory(null, 9, "Spawn Troops");
		ItemStack item = new ItemStack(Material.STICK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Militia");
		meta.setLore(Arrays.asList(new String[]{"Basic foot dudes"}));
		
		item.setItemMeta(meta);
		inv.addItem(item);
		
		item = new ItemStack(Material.WOOD_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("Melee");
		meta.setLore(Arrays.asList(new String[]{"Hits harder than usual"}));
		
		item.setItemMeta(meta);
		inv.addItem(item);
		
		item = new ItemStack(Material.BOW, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("Artillery");
		meta.setLore(Arrays.asList(new String[]{"Big and slow guns"}));
		
		item.setItemMeta(meta);
		inv.addItem(item);
		
		spawnOptions = inv;
	}
	public static final  Inventory spawnOptions;
	
	
}
