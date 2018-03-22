package body;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Main extends JavaPlugin{
	public static JavaPlugin metaDataLock;
	public HandlerLoop mainThread;
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(new SSMEventListener(this), this);
		
		mainThread = new HandlerLoop();
		
		mainThread.runTaskTimer(this, 0, 1);
		
		metaDataLock = this;
	}
	@Override
	public void onDisable(){
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(command.getName().equals("mycommand")){
			if(sender instanceof Player){
				
			}
		}else if(command.getName().equals("fire")){
			if(sender instanceof Player)fire((Player) sender);
		}
		sender.sendMessage(command.getName());
		Bukkit.broadcastMessage("HI");
		return true;
	}
	/**
	 * Summons troops under the command of a given player.
	 * @param sender
	 * @param c
	 */
	public void summon(Player sender, Class<?> c){
		Location loc = sender.getLocation();
		
		ArrayList<Creature> f = new ArrayList<>();
		for(int i = 0; i < 20; i++){
			Zombie z = (Zombie) loc.getWorld().spawnEntity(Util.getSurface(loc.getX() + i, loc.getZ(), loc.getWorld()), EntityType.ZOMBIE);
			
			z.setMetadata("SSMTeam", new FixedMetadataValue(metaDataLock, 1));
			
			z.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
			z.setBaby(false);
			
			z.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
			
			f.add(z);
		}
		mainThread.addTroops(sender, f, c);
	}
	public void move(Player p, Location loc, Vector look){		
		mainThread.setGoals(p, loc, look);
	}
	public void fire(Player p){
		if(!mainThread.fire(p)){
			p.sendMessage("Can't shoot");
		}else{
		}
	}
}
