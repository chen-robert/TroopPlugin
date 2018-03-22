package troops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import body.Main;
import body.Util;

public class Group {
	protected ArrayList<Creature> troops;
	
	protected ArrayList<LivingEntity> targets;
	/**
	 * The normalized look direction with no Y value
	 */
	protected Vector angle;
	/**
	 * Assign an id
	 */
	public final int id = (int) (100000 * Math.random());
	public Group(ArrayList<Creature> z){		
		troops = z;
		
		for(Creature c: z)c.setMetadata("SSMGroup", new FixedMetadataValue(Main.metaDataLock, id));
	}
	public boolean hasGoals(){
		return targets != null;
	}
	public ArrayList<Creature> getTroops(){
		return troops;
	}
	public ArrayList<LivingEntity> getTargets(){		
		return targets;
	}
	public void moveTo(Location loc, Vector look){
		look = look.setY(0).normalize();
		look = Util.rotateVectorAroundY(look, 90);
		
		angle = Util.rotateVectorAroundY(look.normalize(), -90);
		
		ArrayList<Location> goals = new ArrayList<>();
		
		int n = troops.size();
		loc.subtract(look.clone().multiply(n/2));
		for(int i = 0; i < n; i++){
			loc = loc.add(look);
			goals.add(Util.getSurface(loc.getX(), loc.getZ(), loc.getWorld()));
		}		
		
		getTargetsFromLocations(goals);
	}
	/**
	 * Construct and set targets given a list of positions
	 * 
	 * @param goals		The list of locations to create targets at
	 */
	public void getTargetsFromLocations(List<Location> goals){		
		ArrayList<LivingEntity> targets = new ArrayList<>();
		
		for(int i = 0; i < goals.size(); i++){
			LivingEntity en = (LivingEntity) goals.get(i).getWorld().spawnEntity(goals.get(i), EntityType.BAT);
			en.setInvulnerable(true);
			en.setAI(false);
			en.setCollidable(false);
			
			en.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
			
			targets.add(en);
		}
		cleanUpTargets();
		this.targets = targets;
	}
	/**
	 * Set the targets to a list of creatures
	 * 
	 * @param targets		Set the targets to a list of targets
	 */
	public void setTargets(List<LivingEntity> targets){
		for(Creature c: troops)targets.remove(c);
		
		if(targets.size() == 0)return;
		cleanUpTargets();
		this.targets = new ArrayList<LivingEntity>();
		

		
		for(int i = 0; i < Math.min(targets.size(), troops.size()); i++){
			this.targets.add(targets.get(i));
		}
		for(int i = targets.size(); i < troops.size(); i++){
			this.targets.add(targets.get((int) (targets.size() * Math.random())));
		}
	}
	/**
	 * Clean up the targets
	 */
	private void cleanUpTargets(){
		if(targets == null)return;
		
		for(LivingEntity t: targets){
			if(t.getType() == EntityType.BAT)t.remove();
		}
	}
	/**
	 * Remove dead troops
	 */
	public void cleanUp(){
		for(int i = 0; i < troops.size(); i++){
			if(troops.get(i).isDead())troops.remove(i--);
		}
	}
	/**
	 * Fire.
	 * <p>
	 * Classes that wish to implement firing should
	 * override this method.
	 * 
	 * @return	Success of operation
	 */
	public boolean fire(){
		return false;
	}
	/**
	 * Recalculate targets from surrounding enemies
	 */
	public void tick(){
		Collection<Entity> all = new HashSet<>();	
		for(int i = 0; i < troops.size(); i++){
			Location loc = troops.get(i).getLocation();
			Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 10, 10, 10);
			
			all.addAll(entities);
		}
		
		List<LivingEntity> ret = new ArrayList<>();
		for(Entity c: all){
			if(c instanceof LivingEntity && c.hasMetadata("SSMTeam")){
				ret.add((LivingEntity) c);
			}
		}
		setTargets(ret);
	}
}
