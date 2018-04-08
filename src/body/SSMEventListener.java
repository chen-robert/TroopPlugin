package body;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import troops.Artillery;
import troops.Melee;
import troops.Militia;

public class SSMEventListener implements Listener{
	private Main m;
	public SSMEventListener(Main m){
		this.m = m;
	}
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_AIR){
			if(e.getItem().getType() == Material.ANVIL){
				m.fire(e.getPlayer());
			}else if(e.getItem().getType() == Material.DIAMOND_HOE){
				e.getPlayer().openInventory(Menu.spawnOptions);
			}else if(e.getItem().getType() == Material.LEATHER_BOOTS){
				m.move(e.getPlayer(), e.getPlayer().getLocation(), e.getPlayer().getEyeLocation().getDirection().normalize());
			}else if(e.getItem().getType() == Material.STONE_BUTTON){
				m.mainThread.incIndex(e.getPlayer(), -1);
			}else if(e.getItem().getType() == Material.WOOD_BUTTON){
				m.mainThread.incIndex(e.getPlayer(), 1);
			}
		}
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		
		ItemStack item = e.getCurrentItem();
		switch(item.getType()){
		case STICK:
			m.summon(p, Militia.class);
			break;
		case WOOD_SWORD:
			m.summon(p, Melee.class);
			break;
		case BOW:
			m.summon(p, Artillery.class);
			break;
		default:
			break;
		}
		
		p.closeInventory();
	}
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e){
		/*if(e.getEntityType().equals(EntityType.SNOWBALL)){
			Location loc = e.getEntity().getLocation();
			Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 10, 10, 10);
			
			List<LivingEntity> ret = new ArrayList<>();
			for(Entity c: entities){
				if(c instanceof LivingEntity && c.hasMetadata("SSMTeam")){
					ret.add((LivingEntity) c);
				}
			}
			//mainThread.setTargets(ret);
		}*/
	}
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
		if(e.getDamager().hasMetadata("SSMGroup")){
			if(e.getEntity().hasMetadata("SSMGroup")){
				if(e.getDamager().getMetadata("SSMGroup").get(0).asInt() == 
						e.getEntity().getMetadata("SSMGroup").get(0).asInt()){
					e.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent e){
		Entity c = e.getEntity();
		
		if(c instanceof Projectile){
			Projectile p = (Projectile) c;
			if(p.getShooter() instanceof LivingEntity){
				LivingEntity owner = (LivingEntity) p.getShooter();
				
				if(owner.hasMetadata("SSMGroup")){
					e.getEntity().setMetadata("SSMGroup", 
							new FixedMetadataValue(Main.metaDataLock, owner.getMetadata("SSMGroup")));
				}
				if(owner.hasMetadata("SSMShot")){
					e.getEntity().setMetadata("SSMShot", 
							new FixedMetadataValue(Main.metaDataLock, owner.getMetadata("SSMShot")));
					
				}
			}
		}
	}
}
