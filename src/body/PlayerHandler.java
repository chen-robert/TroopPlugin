package body;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import troops.Artillery;
import troops.Group;
import troops.Melee;
import troops.Militia;

public class PlayerHandler{
	/**
	 * The groups under player's control
	 */
	private ArrayList<Group> groups = new ArrayList<>();
	/**
	 * Index of the group that is in use currently
	 */
	private int currIndex = -1;
	
	public PlayerHandler(){
	}

	public void tick() {
		for(int z = 0; z < groups.size(); z++){
			Group g = groups.get(z);
			g.cleanUp();
			
			boolean glowing = g.equals(groups.get(currIndex));
			
			ArrayList<Creature> zombies = g.getTroops();
			
			//Remove dead entities
			for(int i = 0; i < zombies.size(); i++){
				if(zombies.get(i).isDead()){
					zombies.get(i).remove();
					zombies.remove(i);
					i--;
					continue;
				}
			}
			
			//Set targets
			if(g.hasGoals()){
				ArrayList<LivingEntity> targets = g.getTargets();
				for(int i = 0; i < zombies.size(); i++){					
					Location l = targets.get(i).getLocation();
					if(zombies.get(i).getLocation().distance(l) < 1)
						zombies.get(i).teleport(l);//zombies[i].getLocation().add(goals[i].toVector().
								//subtract(zombies[i].getLocation().toVector()).normalize()));
					
					zombies.get(i).setTarget(targets.get(i));
					zombies.get(i).setGlowing(glowing);				
				}
			}
			//Remove if the group is empty
			if(zombies.size() == 0){
				if(currIndex >= z)incIndex(-1);
				groups.remove(g);
				z--;
				return;
			}
			
			g.tick();
		}
		
	}
	
	public void addZombies(ArrayList<Creature> z, Class<?> c){
		ArrayList<Location> l = new ArrayList<>();
		for(Creature a: z)l.add(a.getLocation());

		if(c == Melee.class){
			groups.add(new Melee(z));
		}else if(c == Militia.class){
			groups.add(new Militia(z));
		}else if(c == Artillery.class){
			groups.add(new Artillery(z));
		}
		incIndex(100000);
		
		
		groups.get(currIndex).getTargetsFromLocations(l);
	}
	/**
	 * Safely change the index
	 * 
	 * @param n		The number to change the index by
	 */
	public void incIndex(int n){		
		currIndex += n;
		currIndex = Math.max(0, Math.min(groups.size() - 1, currIndex));
	}
	/**
	 * Fire!
	 * <p>
	 * Attempts to fire at least.
	 * @return		Success of operation
	 */
	public boolean fire(){		
		if(currIndex == -1)return false;
		
		return groups.get(currIndex).fire();
	}
	/**
	 * Set the goals based on location and look vector
	 * 
	 * @param loc		Starting location
	 * @param look		Direction to face
	 */
	public void setGoals(Location loc, Vector look) {
		if(currIndex == -1)return;
		
		groups.get(currIndex).moveTo(loc, look);
		
	}
	/**
	 * Set targets to a list of entities
	 * <p>
	 * To be used for melee attacks usually. 
	 * 
	 * @param ret		The list of entities to attack
	 */
	public void setTargets(List<LivingEntity> ret) {
		if(currIndex != -1);
		
		groups.get(currIndex).setTargets(ret);
		
	}
}
